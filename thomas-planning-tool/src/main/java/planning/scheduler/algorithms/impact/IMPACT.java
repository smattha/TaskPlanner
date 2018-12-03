package planning.scheduler.algorithms.impact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.NumberFormatter;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.math3.random.RandomDataImpl;

import planning.MainPlanningTool;
import planning.model.AssignmentDataModel;
import planning.model.JobDataModel;
import planning.model.MainDataModel;
import planning.model.MobileResourceDataModel;
import planning.model.MobileResourceTypeDataModel;
import planning.model.PlanningInputDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.ToolDataModel;
import planning.model.ToolTypeDataModel;
import planning.model.WorkcenterDataModel;
import planning.scheduler.algorithms.AbstractAlgorithm;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.algorithms.impact.criteria.Cost;
import planning.scheduler.algorithms.impact.criteria.DummyCriterion;
import planning.scheduler.algorithms.impact.interfaces.DecisionPointAssignmentsConsumerInterface;
import planning.scheduler.algorithms.impact.interfaces.DynamicAssignmentsAuthorityInterface;
import planning.scheduler.algorithms.impact.interfaces.DynamicResourceTaskSuitabilityAuthorityInterface;
import planning.scheduler.simulation.PlanSimulator;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class IMPACT extends AbstractAlgorithm {

    public class DecisionPointOrderedSolutionsAndUtilitiesCarrier {
        PlanHelperInterface helper = null;
        Vector<HashMap<String, Object>> orderedAlternatives = new Vector<HashMap<String, Object>>();
        HashMap<TreeNode[], Double> solutionUtilities = null;
        Calendar timeNow = null;

        @SuppressWarnings("unchecked")
        DecisionPointOrderedSolutionsAndUtilitiesCarrier(HashMap<TreeNode[], Double> solutionUtilities, Calendar timeNow, PlanHelperInterface helper) {
            this.solutionUtilities = (HashMap<TreeNode[], Double>) solutionUtilities.clone();
            this.timeNow = timeNow;
            this.helper = helper;
        }

        public Vector<HashMap<String, Object>> getOrderedAlternatives() {
            if (orderedAlternatives.size() == 0) {
                // Sort the assignments

                // Get a list of the entries in the map
                List<Map.Entry<TreeNode[], Double>> list = new Vector<Map.Entry<TreeNode[], Double>>(solutionUtilities.entrySet());

                // Sort the list using an anonymous inner class implementing
                // Comparator for the compare method
                java.util.Collections.sort(list, new Comparator<Map.Entry<TreeNode[], Double>>() {
                    @Override
                    public int compare(Map.Entry<TreeNode[], Double> entry, Map.Entry<TreeNode[], Double> entry1) {
                        // Return 0 for a match, 1 for less than and -1 for more
                        // then -- sorting descenting
                        return (entry.getValue().equals(entry1.getValue()) ? 0 : (entry.getValue() > entry1.getValue() ? -1 : 1));
                    }
                });

                // Copy back the entries now in order
                for (Map.Entry<TreeNode[], Double> entry : list) {
                    HashMap<String, Object> pair = new HashMap<String, Object>();
                    TreeNode[] alternative = entry.getKey();
                    Double utility = entry.getValue();

                    Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();

                    if (alternative.length < 2) {
                    }
                    else {
                        LayerNode assignmentNode = (LayerNode) alternative[1];
                        Vector<Assignment> assignmentsVector = assignmentNode.getNodeAssignments();
                        for (int i = 0; i < assignmentsVector.size(); i++) {
                            Assignment layerAssignment = assignmentsVector.get(i);
                            TaskSimulator taskSimulator = layerAssignment.getTask();
                            ResourceSimulator resourceSimulator = layerAssignment.getResource();

                            // Creating the assignment
                            // TODO: CREATE DEFAULT ASSIGNMENT CREATOR AS
                            // FOLLOWS ... FOR AUTORECON MUST OVERRIDE
                            AssignmentDataModel assignment = new AssignmentDataModel(taskSimulator.getTaskDataModel(), resourceSimulator.getResourceDataModel(), timeNow,
                                    helper.getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, assignmentsVector)
                                            + helper.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, assignmentsVector)
                                            + helper.getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, assignmentsVector), false, null);
                            assignments.add(assignment);
                        }
                    }

                    pair.put("ASSIGNMENTS", assignments);
                    pair.put("UTILITY", utility);
                    orderedAlternatives.add(pair);
                }
            }

            return orderedAlternatives;
        }
    }

    /**
     * Initial default value of {@link IMPACT#decisionHorizon}
     */
    private static final int DEFAULT_DH = 1;

    /**
     * Initial default value of {@link IMPACT#decisionHorizonInLayers}
     */
    private static final boolean DEFAULT_DH_IN_LAYERS = true;

    /**
     * Initial default value of {@link IMPACT#maximumNumberOfAlternatives}
     */
    private static final int DEFAULT_MNA = 100;

    /**
     * Initial default value of {@link IMPACT#samplingRate}
     */
    private static final int DEFAULT_SR = 1;

    /**
     * Criteria Array for this impact
     */
    private AbstractCriterion[] criteria = new AbstractCriterion[] { new Cost() };

    double[] cumulatiCriteriaKpis = null;

    /**
     * Decision Horizon (DH)
     */
    private int decisionHorizon = IMPACT.DEFAULT_DH;

    /**
     * Decision Horizon in Layers (DH_IN_LAYERS)
     */
    private boolean decisionHorizonInLayers = IMPACT.DEFAULT_DH_IN_LAYERS;

    private DecisionPointAssignmentsConsumerInterface decisionPointAssignmentsConsumer = new DefaultDecisionPointAssignmentsConsumer();
    /**
     * DecisionPointOrderedUtilityListeners is a Vector of {@link ActionListener} for this impact.
     */
    private final Vector<ActionListener> decisionPointOrderedUtilityListeners = new Vector<ActionListener>();
    /*
     * Custom implementation for dynamic suitabilities. Suitabilities can only
     * be removed so at the beginning of the plan all should exist
     */
    // MOVE THIS TO HELPER INTERFACE CLASS
    private Vector<DynamicResourceTaskSuitabilityAuthorityInterface> dynamicResourceTaskSuitabilityAuthorities = new Vector<DynamicResourceTaskSuitabilityAuthorityInterface>();
    /**
     * Maximum Number of alternatives (MNA)
     */
    private int maximumNumberOfAlternatives = IMPACT.DEFAULT_MNA;

    private final RandomDataImpl r = new RandomDataImpl();

    /**
     * Sampling Rate (SR)
     */
    private int samplingRate = IMPACT.DEFAULT_SR;
    /*
     * Custom implementation of dynamic assignment validity
     */
    private Vector<DynamicAssignmentsAuthorityInterface> dynamicAssignmentsAuthorityInterfaces = null;

    private void addCumulativeCriteriaValues(double[] criteriaValues) {
        //System.err.println("AD HOC IMPLEMENTATION OF KPIS");
        if (cumulatiCriteriaKpis == null) {
            // Initialize the criteria cumulative sum kpis
            cumulatiCriteriaKpis = new double[criteria.length];
            for (int i = 0; i < cumulatiCriteriaKpis.length; i++) {
                cumulatiCriteriaKpis[i] = 0d;
            }
        }

        for (int i = 0; i < criteriaValues.length; i++) {
            cumulatiCriteriaKpis[i] += criteriaValues[i];
        }
    }

    public void addDecisionPointOrderedSolutionsAndUtilitiesListener(ActionListener listener) {
        this.decisionPointOrderedUtilityListeners.add(listener);
    }

    public AbstractCriterion[] getCriteria() {
       // System.err.println("AD HOC IMPLEMENTATION OF KPIS");
        return criteria;
    }

    public double[] getCumulatiCriteriaKpis() {
        //System.err.println("AD HOC IMPLEMENTATION OF KPIS");
        if (cumulatiCriteriaKpis == null) {
            // Initialize the criteria cumulative sum kpis
            cumulatiCriteriaKpis = new double[criteria.length];
            for (int i = 0; i < cumulatiCriteriaKpis.length; i++) {
                cumulatiCriteriaKpis[i] = 0d;
            }
        }
        return cumulatiCriteriaKpis;
    }

    /**
     * Get {@link IMPACT#decisionHorizon}
     * 
     */
    public int getDH() {
        return this.decisionHorizon;
    }

    /**
     * @return {@link IMPACT#maximumNumberOfAlternatives}
     */
    public int getMNA() {
        return this.maximumNumberOfAlternatives;
    }

    /**
     * Get {@link IMPACT#samplingRate}
     * 
     * @return {@link IMPACT#samplingRate}
     */
    public int getSR() {
        return this.samplingRate;
    }

    /**
     * 1) Removing assigned tasks
     * 2) Removing tasks that cannot be performed during the resource
     * availability time (tasks are non preemptive)
     * 3) Removing tasks that has precedence constrains
     * 4) Checking for next task in chain constrains
     */
    private Vector<TaskSimulator> initializeSuitableTasksForRootNode(Vector<TaskSimulator> suitableTasksSimulators, Vector<TaskSimulator> pendingTaskSimulators, ResourceSimulator resourceSimulator, Calendar timeNow, PlanHelperInterface helper) {
        Vector<TaskSimulator> cleanSuitableTaskSimulators = new Vector<TaskSimulator>();
        for (int i = 0; i < suitableTasksSimulators.size(); i++) {
            TaskSimulator taskSimulator = suitableTasksSimulators.get(i);
            if (pendingTaskSimulators.indexOf(taskSimulator) != -1
                    && helper.canResourceFullfilTheTaskUniterrupted(resourceSimulator, taskSimulator, timeNow)
                    && !helper.areAnyPendingPresendenceConstraintsForTaskOnResource(resourceSimulator, taskSimulator, timeNow)) {

                if (dynamicResourceTaskSuitabilityAuthorities.size() > 0) {
                    boolean authorityPermited = true;
                    for (DynamicResourceTaskSuitabilityAuthorityInterface dynamicResourceTaskSuitabilityAuthority : dynamicResourceTaskSuitabilityAuthorities) {
                        if (!dynamicResourceTaskSuitabilityAuthority.isSuitabilityValid(resourceSimulator, taskSimulator, timeNow, helper)) {
                            authorityPermited = false;
                            break;
                        }
                    }
                    if (authorityPermited) {
                        cleanSuitableTaskSimulators.add(taskSimulator);
                    }
                }
                else {
                    cleanSuitableTaskSimulators.add(taskSimulator);
                }
            }
        }
        return cleanSuitableTaskSimulators;
    }

    /**
     * Get {@link IMPACT#decisionHorizonInLayers}
     * 
     * @return {@link IMPACT#decisionHorizonInLayers}
     */
    public boolean isDH_IN_LAYERS() {
        return this.decisionHorizonInLayers;
    }

    private void printSolutionSpace(HashMap<TreeNode[], Vector<TreeNode[]>> solutionSpace) {
        int s = 0;
        for (Iterator<TreeNode[]> iterator = solutionSpace.keySet().iterator(); iterator.hasNext();) {
            // Making the solution
            TreeNode[] alternative = iterator.next();
            Vector<TreeNode[]> samples = solutionSpace.get(alternative);
            s++;
            PlanSimulator.printlnDebugMessage("ALTERNATIVE " + s + "");
            for (int i = 0; i < samples.size(); i++) {
                PlanSimulator.printDebugMessage("\tSAMPLE " + (i + 1) + " : [");
                TreeNode[] path = samples.get(i);
                for (int j = 0; j < path.length; j++) {
                    LayerNode node = (LayerNode) path[j];
                    if (node.getUserObject() == null)
                        continue;
                    Vector<Assignment> assignments = node.getNodeAssignments();
                    for (int k = 0; k < assignments.size(); k++) {
                        Assignment assignment = assignments.get(k);
                        if (j <= this.decisionHorizon) {
                            PlanSimulator.printDebugMessage("\t(" + assignment.getResource().getResourceId() + ":" + assignment.getTask().getTaskId() + ")");
                        }
                        else
                            PlanSimulator.printDebugMessage("\t" + assignment.getResource().getResourceId() + ":" + assignment.getTask().getTaskId());
                    }
                }
                PlanSimulator.printlnDebugMessage("\t]");

            }
            // for (int i = 0; i < alternative.length; i++)
            // {
            // LayerNode node = (LayerNode)alternative[i];
            // Vector<Assignment> nodeAssignments = node.getNodeAssignments();
            // for (int j = 0; j < nodeAssignments.size(); j++)
            // {
            // Assignment nodeAssignment = nodeAssignments.get(j);
            // PlanSimulator.printlnDebugMessage("\tASSIGNMENT "+(j+1)+" : "+nodeAssignment.getResource().getResourceId()+" -> "+nodeAssignment.getTask().getTaskId());
            // }
            // }
            // PlanSimulator.printDebugMessage("\t"+s);
            // for(int j=1; j<alternative.length; j++)
            // {
            // LayerNode assignmentNode = (LayerNode)alternative[j];
            // Vector<Assignment> assignmentsVector =
            // assignmentNode.getNodeAssignments();
            // for (int i = 0; i < assignmentsVector.size(); i++)
            // {
            // Assignment layerAssignment = assignmentsVector.get(i);
            // TaskSimulator taskSimulator = layerAssignment.getTask();
            // ResourceSimulator resourceSimulator =
            // layerAssignment.getResource();
            // if(j+1!=alternative.length)
            // PlanSimulator.printDebugMessage("\t"+resourceSimulator.getResourceId()+"\t"+taskSimulator.getTaskId()+"\t");
            // else
            // PlanSimulator.printDebugMessage("\t"+resourceSimulator.getResourceId()+"\t"+taskSimulator.getTaskId());
            // }
            // }
            // PlanSimulator.printlnDebugMessage("");
        }
    }

    private void printSolutionUtilities(HashMap<TreeNode[], Double> solutionUtilities, HashMap<TreeNode[], double[]> solutionValues) {
        int s = 0;
        for (Iterator<TreeNode[]> iterator = solutionValues.keySet().iterator(); iterator.hasNext();) {
            // Making the solution
            TreeNode[] alternative = iterator.next();
            double[] samples = solutionValues.get(alternative);
            Double utility = solutionUtilities.get(alternative);
            s++;
            NumberFormatter formatNumber = new NumberFormatter(new DecimalFormat("###0.#########"));
            String criteriaString = "";
            for (int i = 0; i < criteria.length; i++) {
                try {
                    criteriaString += " \"" + criteria[i].getCriterionName() + "\":" + formatNumber.valueToString(new Double(samples[i]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            try {
                PlanSimulator.printlnDebugMessage("ALTERNATIVE " + (s) + criteriaString + " UTILITY : " + formatNumber.valueToString(utility));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < alternative.length; i++) {
                LayerNode node = (LayerNode) alternative[i];
                Vector<Assignment> nodeAssignments = node.getNodeAssignments();
                for (int j = 0; j < nodeAssignments.size(); j++) {
                    Assignment nodeAssignment = nodeAssignments.get(j);
                    PlanSimulator.printlnDebugMessage("\tASSIGNMENT " + (j + 1) + " : " + nodeAssignment.getResource().getResourceId() + " -> " + nodeAssignment.getTask().getTaskId());
                }
            }
        }
    }

    private void printSolutionValues(HashMap<TreeNode[], double[]> solutionValues) {
        int s = 0;
        for (Iterator<TreeNode[]> iterator = solutionValues.keySet().iterator(); iterator.hasNext();) {
            // Making the solution
            TreeNode[] alternative = iterator.next();
            double[] samples = solutionValues.get(alternative);
            s++;
            NumberFormatter formatNumber = new NumberFormatter(new DecimalFormat("###0.######"));
            String criteriaString = "";
            for (int i = 0; i < criteria.length; i++) {
                try {
                    criteriaString += " \"" + criteria[i].getCriterionName() + "\":" + formatNumber.valueToString(new Double(samples[i]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            PlanSimulator.printlnDebugMessage("ALTERNATIVE " + (s) + criteriaString);

            for (int i = 0; i < alternative.length; i++) {
                LayerNode node = (LayerNode) alternative[i];
                Vector<Assignment> nodeAssignments = node.getNodeAssignments();
                for (int j = 0; j < nodeAssignments.size(); j++) {
                    Assignment nodeAssignment = nodeAssignments.get(j);
                    PlanSimulator.printlnDebugMessage("\tASSIGNMENT " + (j + 1) + " : " + nodeAssignment.getResource().getResourceId() + " -> " + nodeAssignment.getTask().getTaskId());
                }
            }
        }
    }

    public void resetCumulatiCriteriaKpis() {
        //System.err.println("AD HOC IMPLEMENTATION OF KPIS");
        for (int i = 0; i < cumulatiCriteriaKpis.length; i++) {
            cumulatiCriteriaKpis[i] = 0d;
        }
    }

    public void setCriteria(AbstractCriterion[] newCriteria) {
        criteria = newCriteria;
    }

    public void setDecisionPointAssignmentsConsumer(DecisionPointAssignmentsConsumerInterface decisionPointAssignmentsConsumer) {
        this.decisionPointAssignmentsConsumer = decisionPointAssignmentsConsumer;
    }

    /**
     * Set {@link IMPACT#decisionHorizon}
     * 
     * @param dh
     */
    public void setDH(int dh) {
        this.decisionHorizon = dh;
    }

    /**
     * Set {@link IMPACT#decisionHorizonInLayers}
     * 
     * @param dh_in_layers
     */
    public void setDH_IN_LAYERS(boolean dh_in_layers) {
        this.decisionHorizonInLayers = dh_in_layers;
    }

    public void setDynamicResourceTaskSuitabilityAuthorities(Vector<DynamicResourceTaskSuitabilityAuthorityInterface> dynamicResourceTaskSuitabilityAuthorities) {
        if (dynamicResourceTaskSuitabilityAuthorities != null) {
            this.dynamicResourceTaskSuitabilityAuthorities = dynamicResourceTaskSuitabilityAuthorities;
        }
    }

    /**
     * Set {@link IMPACT#maximumNumberOfAlternatives}
     * 
     * @param mna
     */
    public void setMNA(int mna) {
        this.maximumNumberOfAlternatives = mna;
    }

    /**
     * Set {@link IMPACT#samplingRate}
     * 
     * @param sr
     */
    public void setSR(int sr) {
        this.samplingRate = sr;
    }

    @Override
    public Vector<AssignmentDataModel> solve(Vector<TaskSimulator> pendingTaskSimulators, Vector<ResourceSimulator> idleResourceSimulators, Calendar timeNow, PlanHelperInterface helper) {
        // Making the internal structure
        HashMap<ResourceSimulator, Vector<TaskSimulator>> rootNodeSuitabilities = new HashMap<ResourceSimulator, Vector<TaskSimulator>>();
        Vector<ResourceSimulator> cleanResources = new Vector<ResourceSimulator>();
        for (int i = 0; i < idleResourceSimulators.size(); i++) {
            ResourceSimulator resourceSimulator = idleResourceSimulators.get(i);
            Vector<TaskSimulator> suitableTasksSimulators = helper.getSuitableTasksSimulatorForResource(resourceSimulator);
            Vector<TaskSimulator> cleanResourceTasks = initializeSuitableTasksForRootNode(suitableTasksSimulators, pendingTaskSimulators, resourceSimulator, timeNow, helper);
            if (cleanResourceTasks.size() > 0) {
                rootNodeSuitabilities.put(resourceSimulator, cleanResourceTasks);
                cleanResources.add(resourceSimulator);
            }
        }
        // Making the root node
        LayerNode rootNode = new LayerNode(rootNodeSuitabilities, new Vector<Assignment>(), cleanResources);
        LayerNode lastCreated = null; // used to check if hole solution space was created before the MNA for the current decision point
        for (long i = 0; i < this.getMNA(); i++) {
            // Making the other nodes
            // PlanSimulator.printDebugMessage("\n");
            // PlanSimulator.printDebugMessage("IMPACT : Building alternative "+(i+1)+"/"+IMPACT.maximumNumberOfAlternatives+" ...");
            // PlanSimulator.printDebugMessage("\t");

            // TODO: Check assignments validity by the dynamic authority ... if not recalculate:
            // 1) Convert to dummy assignments for easy access
            // 2) Check validity.
            // 3) Check that invalid assignments not to be added inside MNA

            // CHECK ASSIGNMENTS VALIDITY
            if (this.dynamicAssignmentsAuthorityInterfaces != null && !rootNode.isLeaf()) {
                boolean areAssignmentsValid = false;
                while (!areAssignmentsValid) {
                    areAssignmentsValid = true;
                    // Create the DH assignments
                    rootNode.createRandomChildNodeAssignment(this.getDH());
                    LayerNode dhNode = (LayerNode) rootNode.getLastChild();
                    if (lastCreated != null && dhNode != null && dhNode.equals(lastCreated)) {
                        // NO NEW NODE WAS CREATED ... so validity has already been checked
                        break;
                    }

                    if (dhNode != null && dhNode.getNodeAssignments() != null && dhNode.getNodeAssignments().size() > 0) {
                        // Form the assignments
                        Vector<AssignmentDataModel> assignmentDataModels = new Vector<AssignmentDataModel>();
                        Vector<Assignment> assignments = dhNode.getNodeAssignments();
                        for (Assignment assignment : assignments) {
                            ResourceDataModel resource = assignment.getResource().getResourceDataModel();
                            TaskDataModel task = assignment.getTask().getTaskDataModel();
                            long durationInMilliseconds = helper.getOperationTimeInMillisecondsForTaskOnResource(assignment.getTask(), assignment.getResource(), timeNow, new Vector<LayerNode.Assignment>());
                            AssignmentDataModel assignmentDataModel = new AssignmentDataModel(task, resource, timeNow, durationInMilliseconds, false, null);
                            assignmentDataModels.add(assignmentDataModel);
                        }
                        for (DynamicAssignmentsAuthorityInterface assignmentsAuthorityInterface : dynamicAssignmentsAuthorityInterfaces) {
                            if (!assignmentsAuthorityInterface.areAssignmentsValid(assignmentDataModels, timeNow, helper)) {
                                // RECALCULATE
                                areAssignmentsValid = false;
                                rootNode.remove((MutableTreeNode) rootNode.getLastChild());
                                break;
                            }
                        }
                    } else {
                        /// No assignments, nothing to check
                    }
                }
            } else {
                // Create the DH assignments
                rootNode.createRandomChildNodeAssignment(this.getDH());
            }

            // TODO:
            // 1) Sort Assignments on time
            // 2) Dispose "assignments" starting before or at "timeNow"
            // 3) Convert Vector<AssignmentDataModel> to LayerNode and append it to the rootNode's DH assignment layerNode  (in position i). 
            //    Sample LayerNode suitabilities and resources must be NOT NULL empty hashmap, vector accordingly
            //    ATTENTION !!! A LayerNode represent a DH layer so:
            //      i)  Assignments on the same time must be added on the same Layer
            //      ii) Subsequent assignments must have their own LayerNode and linked to previous 
            //          (previous in time ... thus forming a chain) node by the add method
            if (!rootNode.isLeaf()) {
                LayerNode dhNode = (LayerNode) rootNode.getLastChild();
                if (dhNode.equals(lastCreated)) {
                    // NO NEW NODE WAS CREATED SO SKIP SAMPLES CREATION
                } else {
                    // dhNode = (LayerNode) rootNode.getChildAt((int) i );
                    for (int j = 0; j < this.getSR(); j++) {

                        // ----------------START create DH Assignments -------------------------
                        Vector<Assignment> dhAssignements = new Vector<Assignment>();
                        LayerNode currentDhNode = dhNode;
                        while (currentDhNode != null) {
                            dhAssignements.addAll(currentDhNode.getNodeAssignments());
                            // Move into next DH if exist
                            if (!currentDhNode.isLeaf())
                                currentDhNode = (LayerNode) currentDhNode.getFirstChild();
                            else
                                currentDhNode = null;
                        }
                        // ----------------END create DH Assignments -------------------------
                        Vector<AssignmentDataModel> sample = runSamplingImpact(helper, timeNow, dhAssignements);
                        Vector<Vector<AssignmentDataModel>> timeOrderedAssignments = IMPACT.getTimeOrderedAssignmentsAfterTimeNow(sample, timeNow);

                        // ----------------START create LayerNodes and add them to DH -------------------------
                        LayerNode tempLayerNodeToAccessAssignmentConstructor = new LayerNode(new HashMap<ResourceSimulator, Vector<TaskSimulator>>(), new Vector<Assignment>(), new Vector<ResourceSimulator>());
                        LayerNode tempParentNode = dhNode;
                        for (Vector<AssignmentDataModel> currentAssignments : timeOrderedAssignments) {
                            // Add assignments with same time to same layer
                            Vector<Assignment> currentLayerAssignments = new Vector<Assignment>();
                            for (AssignmentDataModel anAssignmentDataModel : currentAssignments) {
                                // Using currentLayerNode for accessing Assignment constructor
                                Assignment anAssignment = tempLayerNodeToAccessAssignmentConstructor.new Assignment(helper.getResourceSimulatorFromResourceDataModel(anAssignmentDataModel.getResourceDataModel()), helper.getTaskSimulatorFromTaskDataModel(anAssignmentDataModel.getTaskDataModel()));
                                currentLayerAssignments.add(anAssignment);
                            }
                            // createNewNode
                            LayerNode currentLayerNode = new LayerNode(new HashMap<ResourceSimulator, Vector<TaskSimulator>>(), currentLayerAssignments, new Vector<ResourceSimulator>());
                            tempParentNode.add(currentLayerNode);
                            tempParentNode = currentLayerNode;
                        }
                        // ----------------END create LayerNodes and add them to
                        // DH-------------------------

                        lastCreated = (LayerNode) rootNode.getLastChild();
                    }//END for SR
                }
            }//END CHECK for Root node
             // PlanSimulator.printDebugMessage("\r");
        }//END for MNA
         // PlanSimulator.printDebugMessage("\n");
         // Try to form the alternatives generated
        HashMap<TreeNode[], Vector<TreeNode[]>> solutionSpace = new HashMap<TreeNode[], Vector<TreeNode[]>>();
        for (@SuppressWarnings("unchecked")
        Enumeration<LayerNode> e = rootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
            LayerNode node = e.nextElement();
            if (node.getLevel() == this.getDH() || (node.getLevel() < this.getDH() && node.isLeaf())) {
                TreeNode[] alternative = node.getPath();
                Vector<TreeNode[]> samples = new Vector<TreeNode[]>();
                for (@SuppressWarnings("unchecked")
                Enumeration<LayerNode> leafsEnum = node.depthFirstEnumeration(); leafsEnum.hasMoreElements();) {
                    LayerNode leaf = leafsEnum.nextElement();
                    if (leaf.isLeaf()) {
                        TreeNode[] samplePath = leaf.getPath();
                        samples.add(samplePath);
                    }
                }
                solutionSpace.put(alternative, samples);
            }
            else if (node.getLevel() > this.getDH()) {
                break;
            }
        }
        PlanSimulator.printlnDebugMessage("\nIMPACT : SOLUTION SPACE CREATED (" + solutionSpace.size() + "/" + this.getMNA() + ") ALTERNATIVES");
        printSolutionSpace(solutionSpace);

        PlanSimulator.printlnDebugMessage("IMPACT : EVALUATING SOLUTION SPACE ...");
        // Try to evaluate the alternatives generated
        HashMap<TreeNode[], double[]> solutionValues = new HashMap<TreeNode[], double[]>();
        double[] criteriaSums = new double[criteria.length];
        for (Iterator<TreeNode[]> iterator = solutionSpace.keySet().iterator(); iterator.hasNext();) {
            // Making the solution
            TreeNode[] alternative = iterator.next();
            Vector<TreeNode[]> solutionToBeEvaluated = null;
            Vector<TreeNode[]> solutions = solutionSpace.get(alternative);
            if (solutions.size() > 0) {
                solutionToBeEvaluated = solutions;
            }
            else {
                solutionToBeEvaluated = new Vector<TreeNode[]>();
                solutionToBeEvaluated.add(alternative);
            }
            // Generating the criterion value
            double[] criteriaValues = new double[criteria.length];
            for (int i = 0; i < criteria.length; i++) {
                AbstractCriterion criterion = criteria[i];
                double criterionValue = criterion.getValue(solutionToBeEvaluated, helper, timeNow);
                criteriaValues[i] = criterionValue;
                criteriaSums[i] += criterionValue;
            }
            solutionValues.put(alternative, criteriaValues);
        }
        printSolutionValues(solutionValues);
        PlanSimulator.printlnDebugMessage("IMPACT : EVALUATING SOLUTION SPACE DONE");
        PlanSimulator.printlnDebugMessage("IMPACT : NORMALIZING EVALUATION ...");

        // Normalizing the criteria values
        HashMap<TreeNode[], Double> solutionUtilities = new HashMap<TreeNode[], Double>();
        for (Iterator<TreeNode[]> iterator = solutionValues.keySet().iterator(); iterator.hasNext();) {
            // Making the solution
            TreeNode[] alternative = iterator.next();
            double[] criteriaValues = solutionValues.get(alternative);

            double[] normilizedCriteriaValues = new double[criteria.length];
            for (int i = 0; i < criteria.length; i++) {
                if (criteria[i].isBenefit()) {
                    normilizedCriteriaValues[i] = criteriaValues[i] / criteriaSums[i];
                    if (criteriaSums[i] == 0) {
                        // DIVISION BY ZERO : means that we have the smallest
                        // value so split the smallest value (0) among all the
                        // alternatives equally
                        // WE ASSUME THAT IN THE BENEFIT CRITERIA THERE CAN NOT
                        // BE A NEGATIVE VALUES
                        normilizedCriteriaValues[i] = 0;
                    }
                }
                else {
                    normilizedCriteriaValues[i] = (1d / ((solutionValues.size()) - 1d)) * (1d - (criteriaValues[i] / criteriaSums[i]));
                    if (criteriaSums[i] == 0) {
                        // DIVISION BY ZERO : means that we have the biggest
                        // value so split the biggest value (1) among all the
                        // alternatives equally
                        // WE ASSUME THAT IN THE COST CRITERIA THERE CAN NOT BE
                        // A NEGATIVE VALUES
                        normilizedCriteriaValues[i] = 1d / (solutionValues.size());
                    }
                }
            }

            // Calculating the utilities
            double utilityValue = 0;
            for (int i = 0; i < criteria.length; i++) {
                utilityValue += criteria[i].getWeight() * normilizedCriteriaValues[i];
            }
            solutionUtilities.put(alternative, utilityValue);
        }
        printSolutionUtilities(solutionUtilities, solutionValues);

        // Notify the listeners of new decision point's solution space
        if (decisionPointOrderedUtilityListeners.size() > 0) {
            for (int i = 0; i < decisionPointOrderedUtilityListeners.size(); i++) {
                ActionListener listener = decisionPointOrderedUtilityListeners.get(i);
                DecisionPointOrderedSolutionsAndUtilitiesCarrier decisionPointSolutionAndUtilityCarrier = new DecisionPointOrderedSolutionsAndUtilitiesCarrier(solutionUtilities, timeNow, helper);
                ActionEvent event = new ActionEvent(decisionPointSolutionAndUtilityCarrier, ActionEvent.ACTION_PERFORMED, "New Assignments");
                listener.actionPerformed(event);
            }
        }

        // Make out the best alternative ...
        Vector<TreeNode[]> bestAlternatives = new Vector<TreeNode[]>();
        Double bestUtilityScore = null;
        for (Iterator<TreeNode[]> iterator = solutionValues.keySet().iterator(); iterator.hasNext();) {
            TreeNode[] alternative = iterator.next();
            Double utility = solutionUtilities.get(alternative);
            if (bestUtilityScore == null) {
                bestUtilityScore = utility;
                bestAlternatives.add(alternative);
            }
            else {
                if (bestUtilityScore.doubleValue() < utility.doubleValue()) {
                    bestUtilityScore = utility;
                    bestAlternatives.clear();
                    bestAlternatives.add(alternative);
                }
                else if (bestUtilityScore.doubleValue() == utility.doubleValue()) {
                    bestAlternatives.add(alternative);
                }
            }
        }

        PlanSimulator.printlnDebugMessage("IMPACT : SOLUTION SPACE NORMALIZED ...");
        // Make the first level assignments according to the alternative with
        // the highest score
        TreeNode[] assignmentNodes = null;
        if (bestAlternatives.size() == 0)
            return new Vector<AssignmentDataModel>();
        else if (bestAlternatives.size() > 1)
            assignmentNodes = bestAlternatives.get(r.nextInt(0, bestAlternatives.size() - 1));
        else
            assignmentNodes = bestAlternatives.get(0);

        // FOO FOR SALESMAN PROBLEM
        /*
         * NumberFormatter nf = new NumberFormatter(); nf.setFormat(new
         * DecimalFormat("###0.#########")); try {
         * //PlanSimulator.printlnDebugMessage(Calendar.getInstance().getTime
         * ().toString() +" : "+taskSimulator.getTaskId() + " on " +
         * resourceSimulator.getResourceId() + " with utility "+
         * nf.valueToString(bestUtilityScore) );
         * //PlanSimulator.printlnDebugMessage
         * ("SR\t"+IMPACT.getSR()+"\tTASK\t"+taskSimulator
         * .getTaskId()+"\tRESOURCE\t"
         * +resourceSimulator.getResourceId()+"\tUTILITY\t"
         * +nf.valueToString(bestUtilityScore)); for(int j=1;
         * j<assignmentNodes.length; j++) { LayerNode assignmentNode =
         * (LayerNode)assignmentNodes[j]; Vector<Assignment> assignmentsVector =
         * assignmentNode.getNodeAssignments(); for (int i = 0; i <
         * assignmentsVector.size(); i++) { Assignment layerAssignment =
         * assignmentsVector.get(i); TaskSimulator taskSimulator =
         * layerAssignment.getTask(); ResourceSimulator resourceSimulator =
         * layerAssignment.getResource(); if(j+1!=assignmentNodes.length)
         * PlanSimulator
         * .printDebugMessage(resourceSimulator.getResourceId()+"\t"
         * +taskSimulator.getTaskId()+"\t"); else
         * PlanSimulator.printDebugMessage
         * (resourceSimulator.getResourceId()+"\t"+taskSimulator.getTaskId()); }
         * } PlanSimulator.printlnDebugMessage("\t"+IMPACT.getMNA()+"\t"+nf.
         * valueToString
         * (bestUtilityScore)+"\t"+nf.valueToString(solutionValues.get
         * (assignmentNodes)[0])); }catch(Exception ex){ex.printStackTrace();}
         */
        // END FOO FOR SALESMAN PROBLEM

        if (assignmentNodes.length < 2) {
            return new Vector<AssignmentDataModel>();
            // No assignments ???
            // TODO must investigate
        }
        LayerNode assignmentNode = (LayerNode) assignmentNodes[1];
        Vector<Assignment> assignmentsVector = assignmentNode.getNodeAssignments();
        // TODO : Check if should be moved to PlanSimulator ... seems more
        // appropriate
        Vector<AssignmentDataModel> assignments = decisionPointAssignmentsConsumer.consumeDecisionPointAssignments(timeNow, helper, assignmentsVector, solutionValues.get(assignmentNodes));
        // Update the kpis
        addCumulativeCriteriaValues(solutionValues.get(assignmentNodes));

        return assignments;
    }

    /**
     * @param planHelperInterface
     * @param timeNow
     * @return
     */
    private Vector<AssignmentDataModel> runSamplingImpact(PlanHelperInterface planHelperInterface, Calendar timeNow, Vector<Assignment> decisionHorizonAssignments) {
        int dh = 1;
        int mna = 1;
        int sr = 0;

        // TODO PERFORMANCE OPTMIZATION by removing tasks that will have
        // finished by time now.

        // TODO:
        // 1) Must create the "assignmentDataModels" out of "assignments" of
        // current Decision Point alternative.
        // 2) Must lock the previous created "assignmentDataModels" (of current
        // Decision Point alternative).
        // 3) Must put timeNow on the assignmentDataModels dispatching time
        // 4) Must create copies and lock the "assignmentDataModels" created so
        // far (from PlanningHelper)
        // 5) ADD ALL THE ABOVE "assignmentDataModels" to the
        // "lockedAssignments" vector

        // Creating the data model
        Vector<TaskSuitableResourceDataModel> tasksSuitableResources = planHelperInterface.getAllSuitabilities();

        Vector<ResourceDataModel> resources = new Vector<ResourceDataModel>();
        Vector<TaskDataModel> tasks = new Vector<TaskDataModel>();
        Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints = planHelperInterface.getTaskPrecedenceConstraints();

        Vector<WorkcenterDataModel> workcenters = planHelperInterface.getAllWorkcentrers();
        for (WorkcenterDataModel workcenterDataModel : workcenters) {
            resources.addAll(workcenterDataModel.getResourceDataModelVector());
        }

        Vector<JobDataModel> jobs = planHelperInterface.getJobs();
        for (JobDataModel jobDataModel : jobs) {
            tasks.addAll(jobDataModel.getTasks());
        }

        // TODO : Implement copies for ToolTypeDataModels and ToolDataModels and
        // MobileResourceTypeDataModels and MobileResourceDataModels
        Vector<ToolTypeDataModel> toolTypes = new Vector<ToolTypeDataModel>();
        Vector<ToolDataModel> tools = new Vector<ToolDataModel>();
        Vector<MobileResourceTypeDataModel> mobileResourceTypes = new Vector<MobileResourceTypeDataModel>();
        Vector<MobileResourceDataModel> mobileResources = new Vector<MobileResourceDataModel>();

        PlanningInputDataModel planningInputDataModel = new PlanningInputDataModel(resources, tasks, precedenceConstraints, tasksSuitableResources, workcenters, jobs, toolTypes, tools, mobileResourceTypes, mobileResources);

        // TODO CHECK
        planningInputDataModel.setPlanStartDate(timeNow);

        MainDataModel mainDataModel = new MainDataModel(planningInputDataModel, null);

        // Set the criteria
        DummyCriterion dummyCriterion = new DummyCriterion();

        // lock the assignments
        Vector<AssignmentDataModel> tempLockedAssignments = new Vector<AssignmentDataModel>();
        tempLockedAssignments.addAll(planHelperInterface.getAssignments());

        Vector<AssignmentDataModel> lockedAssignments = new Vector<AssignmentDataModel>();
        boolean lock = true;
        for (AssignmentDataModel unlockedAssignmentDataModel : tempLockedAssignments) {
            AssignmentDataModel lockedAssignmentDataModel = new AssignmentDataModel(unlockedAssignmentDataModel.getTaskDataModel(), unlockedAssignmentDataModel.getResourceDataModel(), timeNow, unlockedAssignmentDataModel.getDurationInMilliseconds(), lock, unlockedAssignmentDataModel.getProperties());
            lockedAssignments.add(lockedAssignmentDataModel);
        }
        for (Assignment aDHAssignment : decisionHorizonAssignments) {
            AssignmentDataModel lockedAssignmentDataModel = new AssignmentDataModel(aDHAssignment.getTask().getTaskDataModel(), aDHAssignment.getResource().getResourceDataModel(), timeNow, planHelperInterface.getOperationTimeInMillisecondsForTaskOnResource(aDHAssignment.getTask(), aDHAssignment.getResource(), timeNow, new Vector<LayerNode.Assignment>()), lock,null);
            lockedAssignments.add(lockedAssignmentDataModel);
        }

        // MAKE THE RESULTS
        MainPlanningTool mpt = new MainPlanningTool(mainDataModel);
        mpt.initializeSimulator(lockedAssignments);

        // configure impact
        PlanSimulator.DEBUG = false;
        //Get reference to IMPACT for configuration.
        IMPACT mptIMPACT = (IMPACT) mpt.getAlgorithmFactoryforConfiguration().getAlgorithmInstance(IMPACT.MULTICRITERIA);
        mptIMPACT.setDH(dh);
        mptIMPACT.setMNA(mna);
        mptIMPACT.setSR(sr);
        mptIMPACT.setCriteria(
                new AbstractCriterion[] { dummyCriterion });

        mpt.simulate();
        Vector<AssignmentDataModel> assignments = mpt.getAssignmentDataModelVector();
        PlanSimulator.DEBUG = true;
        return assignments;
    }

    /**
     * Get assignments ordered by time of dispatch
     * 
     * @param assignments
     * @return
     */
    private static final Vector<Vector<AssignmentDataModel>> getTimeOrderedAssignmentsAfterTimeNow(Vector<AssignmentDataModel> assignments, Calendar timeNow) {
        // TODO Perhaps refactor to some other class
        HashMap<Calendar, Vector<AssignmentDataModel>> timeOrderedAssignments = new HashMap<Calendar, Vector<AssignmentDataModel>>();
        List<Calendar> allDispathesDates = new Vector<Calendar>();

        for (AssignmentDataModel assignment : assignments) {
            Calendar dateOfDispatch = assignment.getTimeOfDispatch();
            if (timeNow.getTimeInMillis() < dateOfDispatch.getTimeInMillis()) {
                if (!allDispathesDates.contains(dateOfDispatch)) {
                    allDispathesDates.add(dateOfDispatch);
                }
                if (timeOrderedAssignments.get(dateOfDispatch) == null) {
                    Vector<AssignmentDataModel> timeAssignments = new Vector<AssignmentDataModel>();
                    timeAssignments.add(assignment);
                    timeOrderedAssignments.put(dateOfDispatch, timeAssignments);
                }
                else {
                    Vector<AssignmentDataModel> timeAssignments = timeOrderedAssignments.get(dateOfDispatch);
                    timeAssignments.add(assignment);
                }
            }
        }
        // timeOrderedAssignments is completed here.
        List<Calendar> validAssignmentDispatchTimes = new ArrayList<Calendar>();
        validAssignmentDispatchTimes.addAll(timeOrderedAssignments.keySet());
        Collections.sort(validAssignmentDispatchTimes);
        Vector<Vector<AssignmentDataModel>> timeOrderedAssignementVectors = new Vector<Vector<AssignmentDataModel>>();
        for (Calendar nextDate : validAssignmentDispatchTimes)
        {
            timeOrderedAssignementVectors.add(timeOrderedAssignments.get(nextDate));
        }
        return timeOrderedAssignementVectors;
    }

    public void setDynamicAssignmentsAuthorityInterfaces(Vector<DynamicAssignmentsAuthorityInterface> dynamicAssignmentsAuthorityInterfaces) {
        this.dynamicAssignmentsAuthorityInterfaces = dynamicAssignmentsAuthorityInterfaces;
    }

}
