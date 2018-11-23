package planning.scheduler.algorithms.impact;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.math3.random.RandomDataImpl;

import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

public class LayerNode extends DefaultMutableTreeNode {
    /**
     * Generated Serial Version ID
     */
    private static final long serialVersionUID = -5276645406515609676L;

    private HashMap<ResourceSimulator, Vector<TaskSimulator>> resourceSuitabilities = new HashMap<ResourceSimulator, Vector<TaskSimulator>>();
    private Vector<ResourceSimulator> resources = null;
    private Vector<Assignment> nodeAssignments = new Vector<Assignment>();
    private RandomDataImpl r = new RandomDataImpl();
    private RandomUniqueAssignmentGenerator ruag = null;
    private HashMap<Integer, TaskSimulator> taskIndexesToTaskSimulator = new HashMap<Integer, TaskSimulator>();
    private HashMap<TaskSimulator, Integer> taskSimulatorToTaskIndexes = new HashMap<TaskSimulator, Integer>();

    public LayerNode(HashMap<ResourceSimulator, Vector<TaskSimulator>> resourceSuitabilities, Vector<Assignment> assignments, Vector<ResourceSimulator> resources) {
        this.children = new Vector<LayerNode>();
        this.resources = resources;
        this.resourceSuitabilities = resourceSuitabilities;
        this.nodeAssignments = assignments;
        this.setUserObject(makeId(assignments));
        // Initializing the random generator
        Vector<Vector<Integer>> resourcesIndexes = new Vector<Vector<Integer>>();
        // Initializing the structures
        for (int i = 0; i < resources.size(); i++) {
            ResourceSimulator resource = resources.get(i);
            Vector<TaskSimulator> suitableTaskForCurrentResource = resourceSuitabilities.get(resource);
            Vector<Integer> suitableTaskIndexesForCurrentResourceIndexes = new Vector<Integer>();
            for (int j = 0; j < suitableTaskForCurrentResource.size(); j++) {
                TaskSimulator taskSimulator = suitableTaskForCurrentResource.get(j);
                Integer taskSimulatorIndex = null;
                if (taskSimulatorToTaskIndexes.get(taskSimulator) != null) {
                    taskSimulatorIndex = taskSimulatorToTaskIndexes.get(taskSimulator);
                } else {
                    taskSimulatorIndex = new Integer(taskSimulatorToTaskIndexes.size() + 1);

                    taskSimulatorToTaskIndexes.put(taskSimulator, taskSimulatorIndex);
                    taskIndexesToTaskSimulator.put(taskSimulatorIndex, taskSimulator);
                }
                suitableTaskIndexesForCurrentResourceIndexes.add(taskSimulatorIndex);
            }
            if (suitableTaskForCurrentResource.size() > 0)
                resourcesIndexes.add(suitableTaskIndexesForCurrentResourceIndexes);
        }
        // Getting the random assignments
        ruag = new RandomUniqueAssignmentGenerator(resourcesIndexes);
    }

    public Vector<Assignment> getNodeAssignments() {
        return this.nodeAssignments;
    }

    private boolean canProduceLayeredAlternatives(int dh) {
        if (dh == 1)
            return ruag.hasNext();
        else if (dh > 1) {
            if (ruag.hasNext())
                return true;
            // Must search the children
            for (int i = 0; i < children.size(); i++) {
                LayerNode child = (LayerNode) children.get(i);
                if (child.canProduceLayeredAlternatives(dh - 1))
                    return true;
            }
        }
        return false;
    }

    private boolean canProduceSampleAlternatives() {
        if (ruag.hasNext())
            return true;

        // Must search the children
        for (int i = 0; i < children.size(); i++) {
            LayerNode child = (LayerNode) children.get(i);
            if (child.canProduceSampleAlternatives())
                return true;
        }
        return false;
    }

    private String makeId(Vector<Assignment> assignments) {
        String id = "";
        HashMap<String, ResourceSimulator> resourceIdToResourceSimulator = new HashMap<String, ResourceSimulator>();
        HashMap<ResourceSimulator, TaskSimulator> resourceSimulatorToTaskSimulator = new HashMap<ResourceSimulator, TaskSimulator>();

        Object[] resourceIdsArray = new Object[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            Assignment currentAssignment = assignments.get(i);
            ResourceSimulator currentAssignmentResource = currentAssignment.getResource();
            resourceIdsArray[i] = currentAssignmentResource.getResourceId();
            resourceIdToResourceSimulator.put(currentAssignmentResource.getResourceId(), currentAssignmentResource);
            resourceSimulatorToTaskSimulator.put(currentAssignmentResource, currentAssignment.getTask());
        }

        Arrays.sort(resourceIdsArray);
        for (int i = 0; i < resourceIdsArray.length; i++) {
            String resourceId = (String) resourceIdsArray[i];
            String taskId = resourceSimulatorToTaskSimulator.get(resourceIdToResourceSimulator.get(resourceId)).getTaskId();
            id += resourceId + taskId;
        }

        return id;
    }

    public void createRandomChildNodeAssignment(int dh) {
        if (dh > 0) {
            if (ruag.hasNext()) {
                Integer[] assignmentIndexes = ruag.getNextAlternative();
                if (assignmentIndexes == null)
                    return;
                Vector<Assignment> childAssignments = new Vector<Assignment>();
                Vector<TaskSimulator> tasksAssigned = new Vector<TaskSimulator>();
                for (int i = 0; i < assignmentIndexes.length; i++) {
                    if (assignmentIndexes[i] == null)
                        continue;// No assignment for current resource

                    ResourceSimulator resource = resources.get(i);

                    TaskSimulator task = taskIndexesToTaskSimulator.get(new Integer(assignmentIndexes[i]));
                    // PlanSimulator.printDebugMessage(" "+task.getTaskId()+" ");
                    Assignment assignment = new Assignment(resource, task);
                    childAssignments.add(assignment);
                    tasksAssigned.add(task);
                }

                // Now find out if the child exist
                LayerNode child = null;
                String childId = makeId(childAssignments);
                for (int i = 0; i < children.size(); i++) {
                    if (((LayerNode) children.get(i)).getUserObject().equals(childId)) {
                        child = (LayerNode) children.get(i);
                        break;
                    }
                }

                if (child == null) {
                    // Now calculate the applicable tasks
                    HashMap<ResourceSimulator, Vector<TaskSimulator>> childResourceSuitabilities = new HashMap<ResourceSimulator, Vector<TaskSimulator>>();
                    Vector<ResourceSimulator> childResources = new Vector<ResourceSimulator>();
                    for (int i = 0; i < resources.size(); i++) {
                        ResourceSimulator resource = resources.get(i);
                        Vector<TaskSimulator> suitableTasks = resourceSuitabilities.get(resource);
                        @SuppressWarnings("unchecked")
                        Vector<TaskSimulator> childSuitableTasks = (Vector<TaskSimulator>) suitableTasks.clone();
                        childSuitableTasks.removeAll(tasksAssigned);
                        if (childSuitableTasks.size() > 0) {
                            childResources.add(resource);
                            childResourceSuitabilities.put(resource, childSuitableTasks);
                        }
                    }
                    // Now make the child layer
                    child = new LayerNode(childResourceSuitabilities, childAssignments, childResources);
                    // Now add the new child to this node children
                    this.add(child);
                }
                // Now for the child's children ... going to next layer
                child.createRandomChildNodeAssignment(dh - 1);
            } else {
                // We have made all the possible children thus now we just
                // pickup a random child that
                // 1) Has dh==1 and ruag.hasNext==true
                // or
                // 2) dh > 1 but has children that can provide a different path
                // ...
                Vector<LayerNode> childrenCapableOfProducingAlternativePaths = new Vector<LayerNode>(); // Used for DH >1
                for (int i = 0; i < children.size(); i++) {
                    LayerNode child = (LayerNode) children.get(i);
                    if (child.canProduceLayeredAlternatives(dh - 1)) {
                        childrenCapableOfProducingAlternativePaths.add(child);
                    }
                }
                // Now choose one if exists
                if (childrenCapableOfProducingAlternativePaths.size() == 0)
                    return;
                else if (childrenCapableOfProducingAlternativePaths.size() == 1) {
                    LayerNode child = childrenCapableOfProducingAlternativePaths.get(0);
                    child.createRandomChildNodeAssignment(dh - 1);
                    // PlanSimulator.printDebugMessage(" "+
                    // child.getNodeAssignments().get(0).getTask().getTaskId()+" ");
                } else {
                    LayerNode child = childrenCapableOfProducingAlternativePaths.get(r.nextInt(0, childrenCapableOfProducingAlternativePaths.size() - 1));
                    child.createRandomChildNodeAssignment(dh - 1);
                    // PlanSimulator.printDebugMessage(" "+
                    // child.getNodeAssignments().get(0).getTask().getTaskId()+" ");
                }
            }
        } else {
            // TODO clean up code ... i.e. remove else + private not used functions
            
            // making the samples with the given sample rate
            // if(sr>0)
            // PlanSimulator.printlnDebugMessage("");
//            for (int i = 0; i < sr; i++) {
//                // PlanSimulator.printDebugMessage("\r\t\tMaking sample "+(i+1)+"/"+sr+" :\t");
//                createRandomSolution();
//            }
            // if(sr>0)
            // PlanSimulator.printDebugMessage("\n");
        }
    }

    private void createRandomSolution() {
        if (ruag.hasNext()) {
            Integer[] assignmentIndexes = ruag.getNextAlternative();
            if (assignmentIndexes == null)
                return;
            Vector<Assignment> childAssignments = new Vector<Assignment>();
            Vector<TaskSimulator> tasksAssigned = new Vector<TaskSimulator>();
            for (int i = 0; i < assignmentIndexes.length; i++) {
                if (assignmentIndexes[i] == null)
                    continue;// No assignment for current resource

                ResourceSimulator resource = resources.get(i);

                TaskSimulator task = taskIndexesToTaskSimulator.get(new Integer(assignmentIndexes[i]));

                // PlanSimulator.printDebugMessage(" "+task.getTaskId()+" ");

                Assignment assignment = new Assignment(resource, task);
                childAssignments.add(assignment);
                tasksAssigned.add(task);
            }

            // Now find out if the child exist
            LayerNode child = null;
            String childId = makeId(childAssignments);
            for (int i = 0; i < children.size(); i++) {
                if (((LayerNode) children.get(i)).getUserObject().equals(childId)) {
                    child = (LayerNode) children.get(i);
                    break;
                }
            }

            if (child == null) {
                // Now calculate the applicable tasks
                HashMap<ResourceSimulator, Vector<TaskSimulator>> childResourceSuitabilities = new HashMap<ResourceSimulator, Vector<TaskSimulator>>();
                Vector<ResourceSimulator> childResources = new Vector<ResourceSimulator>();
                for (int i = 0; i < resources.size(); i++) {
                    ResourceSimulator resource = resources.get(i);
                    Vector<TaskSimulator> suitableTasks = resourceSuitabilities.get(resource);
                    @SuppressWarnings("unchecked")
                    Vector<TaskSimulator> childSuitableTasks = (Vector<TaskSimulator>) suitableTasks.clone();
                    childSuitableTasks.removeAll(tasksAssigned);
                    if (childSuitableTasks.size() > 0) {
                        childResources.add(resource);
                        childResourceSuitabilities.put(resource, childSuitableTasks);
                    }
                }
                // Now make the child layer
                child = new LayerNode(childResourceSuitabilities, childAssignments, childResources);
                // Now add the new child to this node children
                this.add(child);
            }
            // Now for the child's children ... going to next layer
            child.createRandomSolution();
        } else {
            // We have made all the possible children thus now we just pickup a
            // random child that
            // 1) ruag.hasNext==true
            // or
            // 2) has children that can provide a different path (has
            // ruag.hasNext==true)...
            Vector<LayerNode> childrenCapableOfProducingAlternativePaths = new Vector<LayerNode>(); // Used
                                                                                                    // for
                                                                                                    // DH
                                                                                                    // >1
            for (int i = 0; i < children.size(); i++) {
                LayerNode child = (LayerNode) children.get(i);
                if (child.canProduceSampleAlternatives()) {
                    childrenCapableOfProducingAlternativePaths.add(child);
                }
            }
            // Now choose one if exists
            if (childrenCapableOfProducingAlternativePaths.size() == 0)
                return;
            else if (childrenCapableOfProducingAlternativePaths.size() == 1) {
                LayerNode child = childrenCapableOfProducingAlternativePaths.get(0);
                // PlanSimulator.printDebugMessage(" "+child.nodeAssignments.get(0).getTask().getTaskId()+" ");
                child.createRandomSolution();
            } else {
                LayerNode child = childrenCapableOfProducingAlternativePaths.get(r.nextInt(0, childrenCapableOfProducingAlternativePaths.size() - 1));
                // PlanSimulator.printDebugMessage(" "+child.nodeAssignments.get(0).getTask().getTaskId()+" ");
                child.createRandomSolution();
            }
        }
    }

    public class Assignment {
        private ResourceSimulator resource;
        private TaskSimulator task;

        public Assignment(ResourceSimulator resource, TaskSimulator task) {
            this.resource = resource;
            this.task = task;
        }

        public ResourceSimulator getResource() {
            return resource;
        }

        public TaskSimulator getTask() {
            return task;
        }
    }

    // This class does not handle decision horizon concept properly since
    // it generates unique assignments for the current decision horizon
    public class RandomUniqueAssignmentGenerator {
        private Integer[] nextAlternative = null;
        private Vector<Vector<Integer>> resourceToTasksSuitabilities = null;

        private int resourcesSize = 0;
        private HashMap<String, Integer[]> assignments = new HashMap<String, Integer[]>();
        private HashMap<String, HashMap<Integer, Vector<Integer>>> assignmentsToTasks = new HashMap<String, HashMap<Integer, Vector<Integer>>>();
        private boolean c = true;
        private HashMap<Integer, Integer> finishedResources = new HashMap<Integer, Integer>();
        // shuffling
        private int[] sampleResource = null;

        public RandomUniqueAssignmentGenerator(Vector<Vector<Integer>> resourceToTasksSuitabilities) {
            this.resourceToTasksSuitabilities = resourceToTasksSuitabilities;
            resourcesSize = resourceToTasksSuitabilities.size();
            sampleResource = makeSample(resourcesSize);
            nextAlternative = this.produceNextAlternative();
        }

        public boolean hasNext() {
            if (this.nextAlternative == null)
                return false;
            return true;
        }

        public Integer[] getNextAlternative() {
            Integer[] producedAlternative = null;
            if (nextAlternative != null)
                producedAlternative = this.produceNextAlternative();
            Integer[] returnedAlternative = this.nextAlternative;
            this.nextAlternative = producedAlternative;
            return returnedAlternative;
        }

        private Integer[] produceNextAlternative() {
            Integer[] assignment = new Integer[resourcesSize];

            if (resourcesSize == finishedResources.size()) {
                return null;
            }
            String newAssignmentId = null;
            while (c) {
                Vector<Integer> resourceToAssignmentOrder = new Vector<Integer>();// In
                                                                                  // this
                                                                                  // vector
                                                                                  // we
                                                                                  // keep
                                                                                  // the
                                                                                  // order
                                                                                  // of
                                                                                  // the
                                                                                  // resources
                                                                                  // that
                                                                                  // decided
                                                                                  // for
                                                                                  // their
                                                                                  // assignment
                for (int i = 0; i < resourcesSize; i++) {
                    int resourcePossition = sampleResource[i];

                    Vector<Integer> clearedResourceTasks = null;
                    String assignmentKey = makeId(assignment);
                    if (assignmentsToTasks.get(assignmentKey) == null) {
                        Vector<Integer> resourceTasks = resourceToTasksSuitabilities.get(resourcePossition);
                        // Clearing not applicable tasks
                        clearedResourceTasks = clearTasks(resourceTasks, assignment);
                        // make the resource hash map
                        HashMap<Integer, Vector<Integer>> resourcesToTasks = new HashMap<Integer, Vector<Integer>>();
                        resourcesToTasks.put(new Integer(resourcePossition), clearedResourceTasks);

                        assignmentsToTasks.put(assignmentKey, resourcesToTasks);
                    } else {
                        // checking if for the current resource a task list
                        // exist
                        HashMap<Integer, Vector<Integer>> resourcesToTasks = assignmentsToTasks.get(assignmentKey);
                        if (resourcesToTasks.get(new Integer(resourcePossition)) != null) {
                            clearedResourceTasks = resourcesToTasks.get(new Integer(resourcePossition));
                        } else {
                            Vector<Integer> resourceTasks = resourceToTasksSuitabilities.get(resourcePossition);
                            // Clearing not applicable tasks
                            clearedResourceTasks = clearTasks(resourceTasks, assignment);
                            // make the resource hash map
                            resourcesToTasks.put(new Integer(resourcePossition), clearedResourceTasks);
                        }
                    }
                    resourceToAssignmentOrder.add(new Integer(resourcePossition));

                    if (clearedResourceTasks.size() != 0) {
                        Integer assignmentTaskPossition = null;
                        if (clearedResourceTasks.size() == 1) {
                            assignmentTaskPossition = new Integer(0);
                        } else {
                            assignmentTaskPossition = new Integer(r.nextInt(0, clearedResourceTasks.size() - 1));
                        }

                        int task = clearedResourceTasks.get(assignmentTaskPossition.intValue());

                        if (i + 1 == resourcesSize)// Last assignment
                        {
                            Integer[] potentialAssignment = new Integer[resourcesSize];
                            System.arraycopy(assignment, 0, potentialAssignment, 0, resourcesSize);
                            potentialAssignment[resourcePossition] = new Integer(task);
                            String newPotentialAssignemntKey = makeId(potentialAssignment);
                            // Release the assignment if not yet
                            if (assignments.get(newPotentialAssignemntKey) != null) {
                                // assignment exist probably it has to do with
                                // the resource order that we reached this point
                                // Remove suitability and try again
                                clearedResourceTasks.remove(new Integer(task));
                                i--;// One time in order for the current
                                    // resource to reconsider his assignment
                                resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                                continue;
                            } else {
                                // checking for result validity ... i.e. if all
                                // tasks are assigned or all resources have
                                // tasks
                                // if not valid then BACK BACK
                                boolean allResourcesHaveTasks = true;
                                Vector<Integer> idleResourcesPossitionInPotentialAssignmentArray = new Vector<Integer>();
                                boolean allTasksAreAssigned = true;
                                Vector<Integer> assignedTasks = new Vector<Integer>();
                                for (int j = 0; j < potentialAssignment.length; j++) {
                                    if (potentialAssignment[j] == null) {
                                        allResourcesHaveTasks = false;
                                        idleResourcesPossitionInPotentialAssignmentArray.add(new Integer(j));
                                    } else {
                                        assignedTasks.add(potentialAssignment[j]);
                                    }
                                }
                                for (int j = 0; j < potentialAssignment.length; j++) {
                                    Vector<Integer> resourceSuitabilities = resourceToTasksSuitabilities.get(j);
                                    for (int k = 0; k < resourceSuitabilities.size(); k++) {
                                        if (assignedTasks.indexOf(resourceSuitabilities.get(k)) == -1) {
                                            allTasksAreAssigned = false;
                                            break;
                                        }
                                    }
                                    if (!allTasksAreAssigned)
                                        break;
                                }
                                // ONE MORE CRITERIO ... pending tasks can be
                                // performed on idle resources
                                boolean pendingTasksCanBePerformedOnIdleResources = false;
                                for (int j = 0; j < idleResourcesPossitionInPotentialAssignmentArray.size(); j++) {
                                    Vector<Integer> resourceSuitabilities = resourceToTasksSuitabilities.get(idleResourcesPossitionInPotentialAssignmentArray.get(j));
                                    for (int k = 0; k < resourceSuitabilities.size(); k++) {
                                        if (assignedTasks.indexOf(resourceSuitabilities.get(k)) == -1) {
                                            pendingTasksCanBePerformedOnIdleResources = true;
                                            break;
                                        }
                                    }
                                    if (pendingTasksCanBePerformedOnIdleResources)
                                        break;
                                }

                                // checking the validity
                                if (!allResourcesHaveTasks && !allTasksAreAssigned && pendingTasksCanBePerformedOnIdleResources) {
                                    // First delete the path that reached us
                                    // here ...
                                    assignmentsToTasks.get(assignmentKey).get(resourcePossition).remove(new Integer(task));

                                    // Not valid solution so BACK BACK
                                    Integer previousResourcePossition = null;
                                    if (resourceToAssignmentOrder.size() > 1)
                                        previousResourcePossition = resourceToAssignmentOrder.get(resourceToAssignmentOrder.size() - 2);
                                    else {
                                        // mark the resource as not capable

                                        // Now mark the resource as cannot
                                        // produce results ...
                                        Integer resourceFinished = new Integer(resourcePossition);
                                        finishedResources.put(resourceFinished, resourceFinished);

                                        // DO NOT SHUFFLE just rotate the
                                        // resources array. This way all
                                        // resources will be parsed quicker and
                                        // for sure.
                                        int[] tmpResources = new int[resourcesSize];
                                        for (int j = 0; j < resourcesSize - 1; j++) {
                                            tmpResources[j] = sampleResource[j + 1];
                                        }
                                        tmpResources[resourcesSize - 1] = sampleResource[0];
                                        // Copy back
                                        System.arraycopy(tmpResources, 0, sampleResource, 0, resourcesSize);

                                        break;
                                    }

                                    if (previousResourcePossition != null) {
                                        // assignment cannot be made hence we
                                        // remove the previous assigned task
                                        // from the previous resource tasks
                                        // The previous assignment key (before
                                        // the resource assigned the problematic
                                        // task ) was
                                        // the current assignment key with null
                                        // value at the previous resource
                                        // position
                                        Integer[] previousAssignment = new Integer[resourcesSize];
                                        System.arraycopy(assignment, 0, previousAssignment, 0, resourcesSize);
                                        Integer taskToBeRemoved = previousAssignment[previousResourcePossition];
                                        previousAssignment[previousResourcePossition] = null;
                                        String previousKey = makeId(previousAssignment);
                                        HashMap<Integer, Vector<Integer>> previousResourcesToTasks = assignmentsToTasks.get(previousKey);
                                        Vector<Integer> resourceTasks = previousResourcesToTasks.get(previousResourcePossition);

                                        resourceTasks.remove(taskToBeRemoved);
                                        assignment = previousAssignment;
                                        // we are going to roll back the
                                        // previous resource index to the
                                        // previous of previous resource index.
                                        resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                                        resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                                        // Now that we removed it recalculate
                                        // the previous assignment
                                        i--;
                                        i--;// Two times in order for the
                                            // previous resource to reconsider
                                            // his assignment
                                        continue;
                                    }
                                } else {
                                    // making the new assignment
                                    assignment[resourcePossition] = new Integer(task);
                                    // making the new key
                                    String newAssignemntKey = makeId(assignment);
                                    // register the new assignment
                                    assignments.put(newAssignemntKey, assignment);
                                    // removing the suitability ...
                                    clearedResourceTasks.remove(new Integer(task));
                                    // Re shuffle the resources ...
                                    sampleResource = makeSample(resourcesSize);
                                    // exiting the loop
                                    newAssignmentId = newAssignemntKey;
                                }
                            }
                        } else {
                            assignment[resourcePossition] = new Integer(task);
                        }
                    } else {
                        // No tasks available

                        // TODO either --;--; as hereafter or continue.
                        // Decision is based on
                        // 1) whether the solution just lead to an already
                        // created solution
                        // so the current resource should be skipped in order
                        // for the task to be assigned on
                        // the next available resource (if one exists)
                        // 2) all the tasks are already assigned so the
                        // resource can be left without any assignment
                        // (skip the resource)

                        /**
                         * IMPLEMENTATTION OF THE ABOVE
                         */
                        HashMap<Integer, Vector<Integer>> futureSolutionSpace = assignmentsToTasks.get(assignmentKey);
                        int tempI = i + 1;
                        // CHECKING THE NEXT RESOURCES IF THEY HAVE DATA FOR
                        // THIS SOLUTION
                        boolean notAllSolutionsAreParsed = false;
                        for (; tempI < sampleResource.length; tempI++) {
                            if (futureSolutionSpace.get(sampleResource[tempI]) == null || futureSolutionSpace.get(sampleResource[tempI]).size() != 0) {
                                // not all the resources are considered
                                notAllSolutionsAreParsed = true;
                                break;
                            }
                        }
                        if (notAllSolutionsAreParsed) {
                            // skip this resource
                            continue;
                        }

                        // CHECKING IF THIS RESOURCE HAS TASKS THAT CAN BE DONE
                        Vector<Integer> resourceAllTasks = resourceToTasksSuitabilities.get(resourcePossition);
                        // Clearing not applicable tasks
                        Vector<Integer> clearedResourceAllTasks = clearTasks(resourceAllTasks, assignment);
                        if (clearedResourceAllTasks.size() == 0) {
                            // OK THE RESOURCE HAS NO PENDING TASKS SO CONTINUE
                            // ASSIGNMENTS
                            // LIKE AN ASSIGNMENT HAS BEEN MADE.

                            // if is also the last resource make the assignemnt
                            if (i + 1 == resourcesSize) {
                                // Release the assignment if not yet
                                if (assignments.get(assignmentKey) != null) {
                                    // assignment exist probably it has to do
                                    // with the resource order that we reached
                                    // this point
                                    // GO BACK BACK
                                } else {
                                    // checking for result validity ... i.e. if
                                    // all tasks are assigned or all resources
                                    // have tasks
                                    // if not valid then BACK BACK
                                    boolean allResourcesHaveTasks = true;
                                    Vector<Integer> idleResourcesPossitionInPotentialAssignmentArray = new Vector<Integer>();
                                    boolean allTasksAreAssigned = true;
                                    Vector<Integer> assignedTasks = new Vector<Integer>();
                                    for (int j = 0; j < assignment.length; j++) {
                                        if (assignment[j] == null) {
                                            allResourcesHaveTasks = false;
                                            idleResourcesPossitionInPotentialAssignmentArray.add(new Integer(j));
                                        } else {
                                            assignedTasks.add(assignment[j]);
                                        }
                                    }
                                    for (int j = 0; j < assignment.length; j++) {
                                        Vector<Integer> resourceSuitabilities = resourceToTasksSuitabilities.get(j);
                                        for (int k = 0; k < resourceSuitabilities.size(); k++) {
                                            if (assignedTasks.indexOf(resourceSuitabilities.get(k)) == -1) {
                                                allTasksAreAssigned = false;
                                                break;
                                            }
                                        }
                                        if (!allTasksAreAssigned)
                                            break;
                                    }
                                    // ONE MORE CRITERIO ... pending tasks can
                                    // be performed on idle resources
                                    boolean pendingTasksCanBePerformedOnIdleResources = false;
                                    for (int j = 0; j < idleResourcesPossitionInPotentialAssignmentArray.size(); j++) {
                                        Vector<Integer> resourceSuitabilities = resourceToTasksSuitabilities.get(idleResourcesPossitionInPotentialAssignmentArray.get(j));
                                        for (int k = 0; k < resourceSuitabilities.size(); k++) {
                                            if (assignedTasks.indexOf(resourceSuitabilities.get(k)) == -1) {
                                                pendingTasksCanBePerformedOnIdleResources = true;
                                                break;
                                            }
                                        }
                                        if (pendingTasksCanBePerformedOnIdleResources)
                                            break;
                                    }

                                    // checking the validity
                                    if (!allResourcesHaveTasks && !allTasksAreAssigned && pendingTasksCanBePerformedOnIdleResources) {
                                        // First delete the path that reached us
                                        // here ...
                                        assignmentsToTasks.get(assignmentKey).get(resourcePossition).remove(assignment[resourcePossition]);

                                        // Not valid solution so BACK BACK
                                        Integer previousResourcePossition = null;
                                        if (resourceToAssignmentOrder.size() > 1)
                                            previousResourcePossition = resourceToAssignmentOrder.get(resourceToAssignmentOrder.size() - 2);
                                        else {
                                            // mark the resource as not capable

                                            // Now mark the resource as cannot
                                            // produce results ...
                                            Integer resourceFinished = new Integer(resourcePossition);
                                            finishedResources.put(resourceFinished, resourceFinished);

                                            // DO NOT SHUFFLE just rotate the
                                            // resources array. This way all
                                            // resources will be parsed quicker
                                            // and for sure.
                                            int[] tmpResources = new int[resourcesSize];
                                            for (int j = 0; j < resourcesSize - 1; j++) {
                                                tmpResources[j] = sampleResource[j + 1];
                                            }
                                            tmpResources[resourcesSize - 1] = sampleResource[0];
                                            // Copy back
                                            System.arraycopy(tmpResources, 0, sampleResource, 0, resourcesSize);

                                            break;
                                        }

                                        if (previousResourcePossition != null) {
                                            // assignment cannot be made hence
                                            // we remove the previous assigned
                                            // task from the previous resource
                                            // tasks
                                            // The previous assignment key
                                            // (before the resource assigned the
                                            // problematic task ) was
                                            // the current assignment key with
                                            // null value at the previous
                                            // resource position
                                            Integer[] previousAssignment = new Integer[resourcesSize];
                                            System.arraycopy(assignment, 0, previousAssignment, 0, resourcesSize);
                                            Integer taskToBeRemoved = previousAssignment[previousResourcePossition];
                                            previousAssignment[previousResourcePossition] = null;
                                            String previousKey = makeId(previousAssignment);
                                            HashMap<Integer, Vector<Integer>> previousResourcesToTasks = assignmentsToTasks.get(previousKey);
                                            Vector<Integer> resourceTasks = previousResourcesToTasks.get(previousResourcePossition);

                                            resourceTasks.remove(taskToBeRemoved);
                                            assignment = previousAssignment;
                                            // we are going to roll back the
                                            // previous resource index to the
                                            // previous of previous resource
                                            // index.
                                            resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                                            resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                                            // Now that we removed it
                                            // recalculate the previous
                                            // assignment
                                            i--;
                                            i--;// Two times in order for the
                                                // previous resource to
                                                // reconsider his assignment
                                            continue;
                                        }
                                    } else {
                                        // registering the assignment
                                        String newAssignemntKey = makeId(assignment);
                                        // register the new assignment
                                        assignments.put(newAssignemntKey, assignment);
                                        // Re shuffle the resources ...
                                        sampleResource = makeSample(resourcesSize);
                                        // exiting the loop
                                        newAssignmentId = newAssignemntKey;
                                        continue;
                                    }

                                    // // registering the assignment
                                    // String newAssignemntKey =
                                    // makeId(assignment);
                                    // // register the new assignment
                                    // assignments.put(newAssignemntKey,
                                    // assignment);
                                    // // Re shuffle the resources ...
                                    // sampleResource =
                                    // makeSample(resourcesSize);
                                    // // exiting the loop
                                    // newAssignmentId = newAssignemntKey;
                                    // continue;
                                }
                            } else {
                                // Must add code to check if here we came from a
                                // desicion of a previous task
                                // that is leading to an existing solution. SO
                                // BACK BACK OR CONTINUE
                                if (notAllSolutionsAreParsed)
                                    continue;
                            }
                        }

                        /**
                         * END IMPLEMENTATTION OF THE ABOVE
                         */

                        Integer previousResourcePossition = null;
                        if (resourceToAssignmentOrder.size() > 1)
                            previousResourcePossition = resourceToAssignmentOrder.get(resourceToAssignmentOrder.size() - 2);
                        else {
                            // mark the resource as not capable

                            // Now mark the resource as cannot produce results
                            // ...
                            Integer resourceFinished = new Integer(resourcePossition);
                            finishedResources.put(resourceFinished, resourceFinished);

                            // DO NOT SHUFFLE just rotate the resources array.
                            // This way all resources will be parsed quicker and
                            // for sure.
                            int[] tmpResources = new int[resourcesSize];
                            for (int j = 0; j < resourcesSize - 1; j++) {
                                tmpResources[j] = sampleResource[j + 1];
                            }
                            tmpResources[resourcesSize - 1] = sampleResource[0];
                            // Copy back
                            System.arraycopy(tmpResources, 0, sampleResource, 0, resourcesSize);

                            break;
                        }

                        if (previousResourcePossition != null) {
                            // assignment cannot be made hence we remove the
                            // previous assigned task from the previous resource
                            // tasks
                            // The previous assignment key (before the resource
                            // assigned the problematic task ) was
                            // the current assignment key with null value at the
                            // previous resource position
                            Integer[] previousAssignment = new Integer[resourcesSize];
                            System.arraycopy(assignment, 0, previousAssignment, 0, resourcesSize);
                            Integer taskToBeRemoved = previousAssignment[previousResourcePossition];
                            previousAssignment[previousResourcePossition] = null;
                            String previousKey = makeId(previousAssignment);
                            HashMap<Integer, Vector<Integer>> previousResourcesToTasks = assignmentsToTasks.get(previousKey);
                            Vector<Integer> resourceTasks = previousResourcesToTasks.get(previousResourcePossition);

                            resourceTasks.remove(taskToBeRemoved);
                            assignment = previousAssignment;
                            // we are going to roll back the previous resource
                            // index to the previous of previous resource index.
                            resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                            resourceToAssignmentOrder.remove(resourceToAssignmentOrder.size() - 1);
                            // Now that we removed it recalculate the previous
                            // assignment
                            i--;
                            i--;// Two times in order for the previous resource
                                // to reconsider his assignment
                            continue;
                        }
                    }
                }
                // Checking for exiting the loop
                if (resourcesSize == finishedResources.size() || newAssignmentId != null) {
                    break;
                }
            }

            if (newAssignmentId == null)
                return null;
            return assignments.get(newAssignmentId);
        }

        // private void printAssignmentsToSysOut(HashMap<String, Integer[]>
        // assignments2)
        // {
        // Set<String> keyset = assignments2.keySet();
        // for (Iterator iter = keyset.iterator(); iter.hasNext();)
        // {
        // System.out.println(iter.next());
        // }
        // }

        private String makeId(Integer[] integerArray) {
            String returnString = "[";
            for (int i = 0; i < integerArray.length; i++) {
                if (i + 1 != integerArray.length)
                    returnString += integerArray[i] + ",";
                else
                    returnString += integerArray[i];
            }
            returnString += "]";
            return returnString;
        }

        private int[] makeSample(int arraySize) {
            int[] array = new int[arraySize];
            Vector<Integer> v = new Vector<Integer>();
            for (int i = 0; i < array.length; i++) {
                v.add(new Integer(i));
            }
            Collections.shuffle(v);
            for (int i = 0; i < array.length; i++) {
                array[i] = v.get(i).intValue();
            }
            return array;
        }

        private Vector<Integer> clearTasks(Vector<Integer> resourceTasks, Integer[] result) {
            @SuppressWarnings("unchecked")
            Vector<Integer> clearedTasks = (Vector<Integer>) resourceTasks.clone();

            Integer[] tmpTasks = new Integer[resourceTasks.size()];
            resourceTasks.toArray(tmpTasks);// new
                                            // Integer[resourceTasks.size()];
            Arrays.sort(tmpTasks);
            for (int i = 0; i < result.length; i++) {
                Integer task = result[i];
                if (task != null) {
                    if (Arrays.binarySearch(tmpTasks, task) >= 0) {
                        // exists so remove
                        clearedTasks.remove(task);
                    }
                }
            }
            return clearedTasks;
        }
    }
}
