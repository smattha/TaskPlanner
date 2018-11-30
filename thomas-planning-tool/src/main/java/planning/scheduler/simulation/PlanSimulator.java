package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AbstractDataModel;
import planning.model.AssignmentDataModel;
import planning.model.DockingStationDataModel;
import planning.model.JobDataModel;
import planning.model.MobileResourceDataModel;
import planning.model.ResourceDataModel;
import planning.model.SetUpMatrixDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.ToolDataModel;
import planning.model.WorkcenterDataModel;
import planning.scheduler.algorithms.AbstractAlgorithm;
import planning.scheduler.algorithms.AlgorithmFactory;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.interfaces.ManualPlanHelperInterface;
import planning.scheduler.simulation.interfaces.OperationTimeCalculatorInterface;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class PlanSimulator implements PlanHelperInterface, ManualPlanHelperInterface {
    public static boolean DEBUG = true;

    private static final OperationTimeCalculatorInterface DEFAULT_OPERATION_TIME_CALCULATOR = new DefaultOperationTimeCalculator();

    private OperationTimeCalculatorInterface operationTimeCalculator = PlanSimulator.DEFAULT_OPERATION_TIME_CALCULATOR;

    /**
     * 
     */
    private final AlgorithmFactory algorithmFactory = new AlgorithmFactory();

    /**
     * @return the algorithmFactory
     */
    private final AlgorithmFactory getAlgorithmFactory() {
        return algorithmFactory;
    }

    /**
     * Get the {@link PlanSimulator#algorithmFactory} for configuration
     */
    public final AlgorithmFactory getAlgorithmFactoryInstance() {
        return this.algorithmFactory;
    }

    /**
     * @return the operationTimeCalculator
     */
    public final OperationTimeCalculatorInterface getOperationTimeCalculator() {
        return operationTimeCalculator;
    }

    /**
     * @param operationTimeCalculator
     */
    public void setOperationTimeCalculator(OperationTimeCalculatorInterface operationTimeCalculator) {
        this.operationTimeCalculator = operationTimeCalculator;
    }

    private double[] cumulatiCriteriaKpis = null;

    public double[] getKpis() {
        System.err.println("AD HOC IMPLEMENTATION OF KPIS");
        return cumulatiCriteriaKpis;
    }

    // temporary variables
    private Vector<JobDataModel> jobDataModelVector = null;
    private Vector<AssignmentDataModel> prereleasedAssignmentDataModelVector = null;
    private PoolOfResourcesSimulator resources = null;
    private PoolOfTasksSimulator tasks = null;
    private PoolOfAssignmentsSimulator assignments = null;
    private PoolOfPrereleasedAssignmentsSimulator prereleasedAssignments = null;
    private Vector<WorkcenterDataModel> workcenterDataModelVector = null; // Should change to a PoolOfWorkcenter class
    private Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintDataModelVector = null; // Should change to a PoolOfConstraints class
    private Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector = null; // Should change to a PoolOfConstraints class ??
    // Used when planning problem has no workcenters
    // private String planAlgorithm = null;
    private Calendar startTime = null;

    private Calendar timeNow = null;

    private PlanSimulator() {
    }

    public PlanSimulator(Vector<WorkcenterDataModel> workcenterDataModelVector, Vector<JobDataModel> jobDataModelVector, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintDataModelVector, Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector, Calendar startTime) {
        this.startTime = startTime;
        this.workcenterDataModelVector = workcenterDataModelVector;
        this.resources = new PoolOfResourcesSimulator(workcenterDataModelVector);
        this.tasks = new PoolOfTasksSimulator(jobDataModelVector);
        this.prereleasedAssignments = new PoolOfPrereleasedAssignmentsSimulator(new Vector<AssignmentDataModel>());
        this.assignments = new PoolOfAssignmentsSimulator(new Vector<AssignmentDataModel>());
        this.precedenceConstraintDataModelVector = precedenceConstraintDataModelVector;
        this.taskSuitableResourceDataModelVector = taskSuitableResourceDataModelVector;

        // Initialize the temporary variables
        this.jobDataModelVector = jobDataModelVector;
        this.prereleasedAssignmentDataModelVector = new Vector<AssignmentDataModel>();
    }

    public PlanSimulator(Vector<WorkcenterDataModel> workcenterDataModelVector, Vector<JobDataModel> jobDataModelVector, Vector<AssignmentDataModel> assignmentDataModelVector, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintDataModelVector, Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector, Calendar startTime) {
        this.startTime = startTime;
        this.workcenterDataModelVector = workcenterDataModelVector;
        this.resources = new PoolOfResourcesSimulator(workcenterDataModelVector);
        this.tasks = new PoolOfTasksSimulator(jobDataModelVector);
        this.prereleasedAssignments = new PoolOfPrereleasedAssignmentsSimulator(assignmentDataModelVector);
        this.precedenceConstraintDataModelVector = precedenceConstraintDataModelVector;
        this.taskSuitableResourceDataModelVector = taskSuitableResourceDataModelVector;
        this.assignments = new PoolOfAssignmentsSimulator(new Vector<AssignmentDataModel>());

        // Initialise the temporary variables
        this.jobDataModelVector = jobDataModelVector;
        this.prereleasedAssignmentDataModelVector = assignmentDataModelVector;
    }

    /**
     * Perform the full simulation.
     * The simulation will end until all tasks are released into resources to conform assignments.
     */
    public Vector<AssignmentDataModel> simulate(PlanEndRule planEndRule) {
        // Initialisation of plan
        this.assignments = new PoolOfAssignmentsSimulator(new Vector<AssignmentDataModel>());

        // Initialising the time now with the schedule start date
        this.timeNow = startTime;

        // Constructing the first interrupt that will trigger the plan simulation
        Vector<Interupt> interupts = new Vector<Interupt>();
        interupts.add(new Interupt(timeNow, PlanSimulator.class, Interupt.PLAN_START));

        // Checking for pre released (locked) assignments before the start date
        // of the schedule and dispatch them ...
        // Also if they are finished before the start date inform the
        // corresponding resources.
        Vector<AssignmentDataModel> allPrereleasedAssignments = this.prereleasedAssignments.getPrereleaseAssignmentDataModelVector();
        for (int i = 0; i < allPrereleasedAssignments.size(); i++) {
            AssignmentDataModel prereleasedAssignment = allPrereleasedAssignments.get(i);
            if (timeNow.getTimeInMillis() >= allPrereleasedAssignments.get(i).getTimeOfDispatch().getTimeInMillis()) {
                // Since the pre-released assignment must be assigned make a new
                // assignment with updated set up and operational times and
                // replace the old
                AssignmentDataModel newPrereleasedAssignment = new AssignmentDataModel(prereleasedAssignment.getTaskDataModel(), prereleasedAssignment.getResourceDataModel(), prereleasedAssignment.getTimeOfDispatch(),
                        getSetUpTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null)
                                + getOperationTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null)
                                + getResourceDownTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null), true, prereleasedAssignment.getProperties());
                prereleasedAssignment = newPrereleasedAssignment;
                allPrereleasedAssignments.set(i, prereleasedAssignment);

                this.assignments.addAssignment(prereleasedAssignment);

                // Checking the finish time ...
                if (timeNow.getTimeInMillis() >= allPrereleasedAssignments.get(i).getTimeOfDispatch().getTimeInMillis() + allPrereleasedAssignments.get(i).getDurationInMilliseconds()) {
                    this.resources.notifyForNewAssignment(prereleasedAssignment);
                    this.tasks.notifyForNewAssignment(prereleasedAssignment);

                    this.resources.notifyForAssignmentFinished(prereleasedAssignment);
                    this.tasks.notifyForAssignmentFinished(prereleasedAssignment);
                } else {
                    this.resources.notifyForNewAssignment(prereleasedAssignment);
                    this.tasks.notifyForNewAssignment(prereleasedAssignment);
                }
            }
        }

        // Loop until interrupts exist
        while (interupts.size() > 0) {
            // Print the info if applicable
            printDebugInfoForInterupt(interupts);

            // Check the end rule applicability
            if (planEndRule != null && !planEndRule.continuePlan(interupts, this)) {
                // Stop performing further allocations
                printDebugInfoForPlanEndRule(planEndRule);
                break;
            }

            // Initialising the time now from the interupt's time
            timeNow = interupts.get(0).getDate();

            // Initialise the resources
            resources.init(timeNow);

            // Initialise list with the new interrupt information.
            // Currently only used when an assignment finishes to set the resources state back to IDLE and the tasks to COMPLETED state
            for (Interupt interupt : interupts) {
                if (interupt.getMessage().equals(Interupt.ASSIGNMENTS_FINISHED)) {
                    // Notify the pools of the new assignment that's finished
                    @SuppressWarnings("unchecked")
                    Vector<AssignmentDataModel> assignmentsFinished = (Vector<AssignmentDataModel>) interupt.getSource();
                    for (int i = 0; i < assignmentsFinished.size(); i++) {
                        AssignmentDataModel assignmentFinished = assignmentsFinished.get(i);
                        this.resources.notifyForAssignmentFinished(assignmentFinished);
                        this.tasks.notifyForAssignmentFinished(assignmentFinished);
                    }
                    // Initialise the resources
                    resources.init(timeNow);
                } else if (interupt.getMessage().equals(Interupt.LOCKED_CONSTRAINTS)) {
                    // make and dispatch the locked assignment
                    @SuppressWarnings("unchecked")
                    Vector<AssignmentDataModel> lockedAssignments = (Vector<AssignmentDataModel>) interupt.getSource();
                    for (int i = 0; i < lockedAssignments.size(); i++) {
                        AssignmentDataModel lockedAssignment = lockedAssignments.get(i);

                        this.assignments.addAssignment(lockedAssignment);

                        this.resources.notifyForNewAssignment(lockedAssignment);
                        this.tasks.notifyForNewAssignment(lockedAssignment);

                        // Checking the finish time ...
                        if (timeNow.getTimeInMillis() >= lockedAssignment.getTimeOfDispatch().getTimeInMillis() + lockedAssignment.getDurationInMilliseconds()) {
                            this.resources.notifyForAssignmentFinished(lockedAssignment);
                            this.tasks.notifyForAssignmentFinished(lockedAssignment);
                        }
                    }
                }
            }

            // Make new assignment for every workcenter
            // TODO: Maybe implement better Interrupt class so that to plan only "interrupted" workcenters
            // TODO: The idle resources must be from the same workcenter ... ???
            Vector<AssignmentDataModel> assignmentVector = new Vector<AssignmentDataModel>();
            for (int i = 0; i < workcenterDataModelVector.size(); i++) {

                WorkcenterDataModel currentWorkcenterDataModel = workcenterDataModelVector.get(i);
                Vector<TaskSimulator> pendingTasks = this.findSuitableTaskSimulatorsForWorkcenterDataModelFromAvailableTaskSimulators(tasks.getPendingTaskSimulatorVector(timeNow), currentWorkcenterDataModel);
                Vector<ResourceSimulator> idleResource = resources.getIdleResourceSimulatorVector(currentWorkcenterDataModel);

                // Choosing the algorithm and performing the assignments
                AbstractAlgorithm algorithm = this.getAlgorithmFactory().getAlgorithmInstance(currentWorkcenterDataModel.getAlgorithm());
                Vector<AssignmentDataModel> workcenterAssignments = algorithm.solve(pendingTasks, idleResource, timeNow, this);

                assignmentVector.addAll(workcenterAssignments);

//                // Notify the engine for the new assignments
//                for (int j = 0; j < workcenterAssignments.size(); j++) {
//                    AssignmentDataModel assignment = workcenterAssignments.get(j);
//                    printDebugInfoForAssignment(assignment);
//                    this.assignments.addAssignment(assignment);
//                    this.resources.notifyForNewAssignment(assignment);
//                    this.tasks.notifyForNewAssignment(assignment);
//                }

                if (currentWorkcenterDataModel.getAlgorithm() != null
                        && AbstractAlgorithm.MULTICRITERIA.equals(currentWorkcenterDataModel.getAlgorithm())) {

                    System.err.println("AD HOC IMPLEMENTATION OF KPIS");
                    if (cumulatiCriteriaKpis == null) {
                        // Initialize the criteria cumulative sum kpis

                        cumulatiCriteriaKpis = new double[((IMPACT) algorithm).getCriteria().length];
                        for (int kpiIndex = 0; kpiIndex < cumulatiCriteriaKpis.length; kpiIndex++) {
                            cumulatiCriteriaKpis[kpiIndex] = 0d;
                        }
                    }
                    double[] cumulativeKpis = ((IMPACT) algorithm).getCumulatiCriteriaKpis();
                    for (int kpiIndex = 0; kpiIndex < cumulativeKpis.length; kpiIndex++) {
                        cumulatiCriteriaKpis[kpiIndex] += cumulativeKpis[kpiIndex];
                    }
                    ((IMPACT) algorithm).resetCumulatiCriteriaKpis();
                }
            }

//          Notify the engine for the new assignments
            for (int i = 0; i < assignmentVector.size(); i++) {
                AssignmentDataModel assignment = assignmentVector.get(i);
                printDebugInfoForAssignment(assignment);
                this.assignments.addAssignment(assignment);
                this.resources.notifyForNewAssignment(assignment);
                this.tasks.notifyForNewAssignment(assignment);
            }

            // Getting the next Interupt
            interupts = getNextInterupt();
        }

        // Mark locked the pre locked assignments and add the locked assignments
        Vector<AssignmentDataModel> lockedAssignments = prereleasedAssignments.getPrereleaseAssignmentDataModelVector();
        Vector<AssignmentDataModel> assignmentsDataModelVector = assignments.getAssignmentDataModelVector();
        // System.out.println("lockedAssignments.size() : "+lockedAssignments.size());
        // System.out.println("assignmentsDataModelVector.size() : "+assignmentsDataModelVector.size());
        for (int i = 0; i < lockedAssignments.size(); i++) {
            AssignmentDataModel lockedAssignment = lockedAssignments.get(i);
            for (int j = 0; j < assignmentsDataModelVector.size(); j++) {
                AssignmentDataModel currentAssignment = assignmentsDataModelVector.get(j);
                if (currentAssignment.getTaskDataModel().equals(lockedAssignment.getTaskDataModel())) {
                    // Since the prereleased assignment must be assigned make a
                    // new one with updated set up and operational times and
                    // replace the old
                    AssignmentDataModel newLockedAssignment = new AssignmentDataModel(currentAssignment.getTaskDataModel(), currentAssignment.getResourceDataModel(), currentAssignment.getTimeOfDispatch(),
                            getSetUpTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(currentAssignment.getTaskDataModel()), resources.getResourceSimulator(currentAssignment.getResourceDataModel()), currentAssignment.getTimeOfDispatch(), null)
                                    + getOperationTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(currentAssignment.getTaskDataModel()), resources.getResourceSimulator(currentAssignment.getResourceDataModel()), currentAssignment.getTimeOfDispatch(), null)
                                    + getResourceDownTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(currentAssignment.getTaskDataModel()), resources.getResourceSimulator(currentAssignment.getResourceDataModel()), currentAssignment.getTimeOfDispatch(), null), true, null);
                    assignments.removeAssignment(currentAssignment);
                    assignments.addAssignment(newLockedAssignment);
                    // System.out.println("LOCKED SPOTED");
                }
            }
        }
        //
        System.out.println("FINISHED");
        // Return the output of the plan
        return this.assignments.getAssignmentDataModelVector();
    }

    private void printDebugInfoForPlanEndRule(PlanEndRule planEndRule) {
        if (DEBUG) {
            System.out.println("PLAN END RULE CONFIRMS : " + planEndRule.getHumanReadableDescription());
        }
    }

    private void printDebugInfoForAssignment(AssignmentDataModel assignment) {
        if (DEBUG) {
            System.out.println("\tNEW ASSIGNMENT FOR TASK : " + assignment.getTaskDataModel().getTaskId() + " RESOURCE : " + assignment.getResourceDataModel().getResourceId() + " ON : " + assignment.getTimeOfDispatch().getTime());
        }
    }

    @SuppressWarnings("unchecked")
    private void printDebugInfoForInterupt(Vector<Interupt> interupts) {
        if (DEBUG) {
            for (Interupt interupt : interupts) {
                String interupDetails = "";
                if (interupt.getMessage().equals(Interupt.PLAN_START)) {
                    interupDetails = "";
                } else if (interupt.getMessage().equals(Interupt.TASKS_ARRIVED)) {
                    Vector<TaskSimulator> sources = (Vector<TaskSimulator>) interupt.getSource();
                    for (TaskSimulator taskSimulator : sources) {
                        interupDetails += "\n \t\t\t task = " + taskSimulator.getTaskId();
                    }
                } else if (interupt.getMessage().equals(Interupt.LOCKED_CONSTRAINTS)) {
                    for (int i = 0; i < ((Vector<AssignmentDataModel>) interupt.getSource()).size(); i++) {
                        interupDetails = interupDetails + "\n \t\t\t task = " + ((Vector<AssignmentDataModel>) interupt.getSource()).get(i).getTaskDataModel().getTaskId() + " resource = " + ((Vector<AssignmentDataModel>) interupt.getSource()).get(i).getResourceDataModel().getResourceId();
                    }
                } else if (interupt.getMessage().equals(Interupt.ASSIGNMENTS_FINISHED)) {
                    for (int i = 0; i < ((Vector<AssignmentDataModel>) interupt.getSource()).size(); i++) {
                        interupDetails = interupDetails + "\n \t\t\t task = " + ((Vector<AssignmentDataModel>) interupt.getSource()).get(i).getTaskDataModel().getTaskId() + " resource = " + ((Vector<AssignmentDataModel>) interupt.getSource()).get(i).getResourceDataModel().getResourceId();
                    }
                } else if (interupt.getMessage().equals(Interupt.RESOURCES_IDLE_FROM_RESOURCE_DOWN)) {
                    Vector<ResourceSimulator> sources = (Vector<ResourceSimulator>) interupt.getSource();
                    for (ResourceSimulator resourceSimulator : sources) {
                        interupDetails += "\n \t\t\t resource = " + resourceSimulator.getResourceId();
                    }
                }
                System.out.println("NEW INTERUPT AT : " + interupt.getDate().getTime() + " REASON : " + interupt.getMessage() + interupDetails);
            }
        }
    }

    public Calendar getStartTime() {
        return (Calendar) this.startTime.clone();
    }

    /**
     * There are 4 sources that produce interrupts
     * 
     * @return The smallest Interrupt
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Vector<Interupt> getNextInterupt() {
        Interupt resourceInterupt = resources.getNextInterupt(timeNow);
        Interupt tasksInterupt = tasks.getNextInterupt(timeNow);
        Interupt assignmentsInterupt = assignments.getNextInterupt(timeNow);
        Interupt lockInterupt = prereleasedAssignments.getNextInterupt(timeNow);

        // Check and return the smallest date

        Vector interuptsVector = new Vector();
        if (resourceInterupt != null)
            interuptsVector.add(resourceInterupt);
        if (tasksInterupt != null)
            interuptsVector.add(tasksInterupt);
        if (assignmentsInterupt != null)
            interuptsVector.add(assignmentsInterupt);
        if (lockInterupt != null)
            interuptsVector.add(lockInterupt);

        // Sorting the interrupts
        Vector<Interupt> interupts = new Vector<Interupt>();
        for (Interupt interupt : (Vector<Interupt>) interuptsVector) {
            if (interupts.size() == 0) {
                interupts.add(interupt);
            } else {
                if (interupt.getDate().getTimeInMillis() < interupts.get(0).getDate().getTimeInMillis()) {
                    interupts.removeAllElements();
                    interupts.add(interupt);
                } else if (interupt.getDate().getTimeInMillis() == interupts.get(0).getDate().getTimeInMillis()) {
                    interupts.add(interupt);
                }
            }
        }
        return interupts;
    }

    @Override
    public boolean isTaskSuitableForResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator) {
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSimulator.getTaskDataModel().equals(taskSuitableResource.getTaskDataModel()) && resourceSimulator.getResourceDataModel().equals(taskSuitableResource.getResourceDataModel()))
                return true;
        }
        return false;
    }

    @Override
    public boolean areAnyPendingPresendenceConstraintsForTaskOnResource(ResourceSimulator resourceSimulator, TaskSimulator taskSimulator, Calendar currentTime) {
        // MUST CHECK THE RESOURCE PREVIOUS TASKS IF HAS PENDING CONSTRAINTS ... TASK IN CHAIN WITH RESOURCE UNAVAILABLE ...
        AssignmentDataModel previousAssignment = assignments.getLastAssignmentOfResource(resourceSimulator.getResourceDataModel());
        if (previousAssignment != null) {
            // Check to see if previous assignment had next task in chain with resource unavailable 
            for (int i = 0; i < precedenceConstraintDataModelVector.size(); i++) {
                TaskPrecedenceConstraintDataModel constraint = precedenceConstraintDataModelVector.get(i);
                if (constraint.getPreconditionTask().equals(previousAssignment.getTaskDataModel())) {
                    // Constraint found check type and resource availability
                    if (constraint.getIsConstraintTypeNextTaskInChain() && constraint.getIsResourceUnavailableBetweenTasks() && !constraint.getPostconditionTask().equals(taskSimulator.getTaskDataModel())) {
                        return true;
                    }
                }
            }
        }
        // Now for the normal constraints
        for (int i = 0; i < precedenceConstraintDataModelVector.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = precedenceConstraintDataModelVector.get(i);
            if (constraint.getPostconditionTask().equals(taskSimulator.getTaskDataModel())) {
                // Check if the pre condition task is already assigned
                AssignmentDataModel assignment = assignments.getAssignmentForTask(constraint.getPreconditionTask());
                if (assignment != null) {
                    // An assignment has been made 
                    // Checking if the currentTime is greater than the assignment finish date
                    long finishDateInMilliseconds = assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds();
                    if (finishDateInMilliseconds <= currentTime.getTimeInMillis()) {
                        // OK
                    } else {
                        return true;
                    }
                    // Checking if the constraint is of type next task in chain ... and check the assignment resource with the current resource
                    if (constraint.getIsConstraintTypeNextTaskInChain()) {
                        ResourceDataModel previousResourceDataModel = assignment.getResourceDataModel();
                        ResourceDataModel currentResourceDataModel = resourceSimulator.getResourceDataModel();
                        if (!previousResourceDataModel.equals(currentResourceDataModel)) {
                            return true;
                        }
                    }
                } else {
                    // No assignment is made so the constraint is pending
                    return true;
                }
            }
        }
        // No constraint found for the task
        return false;
    }

    @Override
    public Vector<TaskPrecedenceConstraintDataModel> getTaskPendingPresendenceConstraints(TaskSimulator taskSimulator, Calendar currentTime) {
        Vector<TaskPrecedenceConstraintDataModel> pendingConstrains = new Vector<TaskPrecedenceConstraintDataModel>();
        for (int i = 0; i < precedenceConstraintDataModelVector.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = precedenceConstraintDataModelVector.get(i);
            if (constraint.getPostconditionTask().equals(taskSimulator.getTaskDataModel())) {
                // Check if the precondition task is already assigned
                AssignmentDataModel assignment = assignments.getAssignmentForTask(constraint.getPreconditionTask());
                if (assignment != null) {
                    // An assignment has been made checking if the currentTime
                    // is greater than the assignment finish date
                    long finishDateInMilliseconds = assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds();
                    if (finishDateInMilliseconds <= currentTime.getTimeInMillis()) {
                        // OK assignment is finished
                    } else {
                        // NOT OK assignment is not finished so add it to the
                        // pending constrains vector
                        pendingConstrains.add(constraint);
                    }
                } else {
                    // No assignment is made so the constraint is pending so add
                    // it to the pending constrains vector
                    pendingConstrains.add(constraint);
                }
            }
        }
        return pendingConstrains;
    }

    @Override
    public long getOperationTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator, Calendar timeNow, Vector<Assignment> assignments) {
        return operationTimeCalculator.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, this, assignments);
    }

    @Override
    public Vector<ResourceSimulator> getSuitableResourcesSimulatorForTask(TaskSimulator taskSimulator) {
        Vector<ResourceSimulator> suitableResourcesSimulators = new Vector<ResourceSimulator>();
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSuitableResource.getTaskDataModel().equals(taskSimulator.getTaskDataModel())) {
                ResourceSimulator resourceFound = resources.getResourceSimulator(taskSuitableResource.getResourceDataModel());
                suitableResourcesSimulators.add(resourceFound);
            }
        }
        return suitableResourcesSimulators;
    }

    @Override
    public Vector<TaskSimulator> getSuitableTasksSimulatorForResource(ResourceSimulator resourceSimulator) {
        Vector<TaskSimulator> suitableTasksSimulators = new Vector<TaskSimulator>();
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSuitableResource.getResourceDataModel().equals(resourceSimulator.getResourceDataModel())) {
                TaskSimulator taskFound = tasks.getTaskSimulator(taskSuitableResource.getTaskDataModel());
                suitableTasksSimulators.add(taskFound);
            }
        }
        return suitableTasksSimulators;
    }

    @SuppressWarnings("unchecked")
    public Vector<TaskSuitableResourceDataModel> getAllSuitabilities() {
        return (Vector<TaskSuitableResourceDataModel>) this.taskSuitableResourceDataModelVector.clone();
    }

    @Override
    public long getSetUpTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator, Calendar currentTime, Vector<Assignment> currentLevelAssignments) {
        ResourceDataModel resourceDataModel = resourceSimulator.getResourceDataModel();
        SetUpMatrixDataModel setUpMatrixDataModel = resourceSimulator.getResourceDataModel().getSetUpMatrixDataModel();
        String toCode = getSetUpCodeForTaskOnResource(taskSimulator, resourceSimulator);
        // In order to find the from set up code we must search for the latest
        // assignment of this resource.
        // If such an assignment does not exist we assume the set up code is
        // idle.
        String fromCode = null;
        AssignmentDataModel latestAssignmentDataModel = null;
        long latestAssignmentFinishTimeInMilliseconds = 0;
        Vector<AssignmentDataModel> assignmentDataModelVector = assignments.getAssignmentDataModelVector();
        for (int i = 0; i < assignmentDataModelVector.size(); i++) {
            AssignmentDataModel assignmentDataModel = assignmentDataModelVector.get(i);
            long currentAssignmentFinishTimeInMilliseconds = assignmentDataModel.getTimeOfDispatch().getTimeInMillis() + assignmentDataModel.getDurationInMilliseconds();
            if (currentAssignmentFinishTimeInMilliseconds <= currentTime.getTimeInMillis()) {
                if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resourceDataModel.getResourceId()) && latestAssignmentDataModel == null) {
                    latestAssignmentDataModel = assignmentDataModel;
                    // Updating the latestAssignmentFinishTimeInMilliseconds
                    latestAssignmentFinishTimeInMilliseconds = currentAssignmentFinishTimeInMilliseconds;
                } else if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resourceDataModel.getResourceId())) {
                    // Check the latest assignment dispatch time plus the operational time plus the set up time
                    if (currentAssignmentFinishTimeInMilliseconds > latestAssignmentFinishTimeInMilliseconds) {
                        latestAssignmentDataModel = assignmentDataModel;
                        // Updating the latestAssignmentFinishTimeInMilliseconds
                        latestAssignmentFinishTimeInMilliseconds = currentAssignmentFinishTimeInMilliseconds;
                    }
                }
            }
        }
        // checking also the pre-released ... a.k.a. locked
        for (int i = 0; i < prereleasedAssignmentDataModelVector.size(); i++) {
            AssignmentDataModel assignmentDataModel = prereleasedAssignmentDataModelVector.get(i);
            long currentAssignmentFinishTimeInMilliseconds = assignmentDataModel.getTimeOfDispatch().getTimeInMillis() + assignmentDataModel.getDurationInMilliseconds();
            if (currentAssignmentFinishTimeInMilliseconds <= currentTime.getTimeInMillis()) {
                if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resourceDataModel.getResourceId()) && latestAssignmentDataModel == null) {
                    latestAssignmentDataModel = assignmentDataModel;
                    // Updating the latestAssignmentFinishTimeInMilliseconds
                    latestAssignmentFinishTimeInMilliseconds = currentAssignmentFinishTimeInMilliseconds;
                } else if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resourceDataModel.getResourceId())) {
                    // Check the latest assignment dispatch time plus the
                    // opperational time plus the set up time
                    if (currentAssignmentFinishTimeInMilliseconds > latestAssignmentFinishTimeInMilliseconds) {
                        latestAssignmentDataModel = assignmentDataModel;
                        // Updating the latestAssignmentFinishTimeInMilliseconds
                        latestAssignmentFinishTimeInMilliseconds = currentAssignmentFinishTimeInMilliseconds;
                    }
                }
            }
        }

        if (latestAssignmentDataModel == null) {
            fromCode = "IDLE";
        } else {
            fromCode = getSetUpCodeForTaskOnResource(tasks.getTaskSimulator(latestAssignmentDataModel.getTaskDataModel()), resourceSimulator);
        }

        if (setUpMatrixDataModel != null) {
            return setUpMatrixDataModel.getSetUpTimeInMilliseconds(fromCode, toCode);
        }
        return 0;
    }

    @Override
    public long getResourceDownTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator, Calendar currentTime, Vector<Assignment> currentLevelAssignments) {
        long durationOfAssignmentInMilliseconds = getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, currentTime, currentLevelAssignments)
                + getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, currentTime, currentLevelAssignments);
        long resourceDownTime = 0;
        while (durationOfAssignmentInMilliseconds > 0) {
            Calendar nextNonWorkingDate = resourceSimulator.getNextNonWorkingDate(currentTime);
            if (nextNonWorkingDate != null) {
                durationOfAssignmentInMilliseconds = durationOfAssignmentInMilliseconds - (nextNonWorkingDate.getTimeInMillis() - currentTime.getTimeInMillis());
                currentTime = resourceSimulator.getNextWorkingDate(nextNonWorkingDate);
                if (durationOfAssignmentInMilliseconds > 0) {
                    resourceDownTime = resourceDownTime + (currentTime.getTimeInMillis() - nextNonWorkingDate.getTimeInMillis());
                }
            } else {
                // Means that there is no further nonWorking period defined for
                // the
                // resource so return the allready calculated resource down time
                // plus any time until the resource is up again in case
                // that the current time is inside the LAST non working period
                if (resourceSimulator.isResourceDown(currentTime)) {
                    long resourceRemainingDownTime = 0;
                    Calendar nextWorkingDate = resourceSimulator.getNextNonWorkingDate(currentTime);
                    resourceRemainingDownTime = nextWorkingDate.getTimeInMillis() - currentTime.getTimeInMillis();
                    return resourceDownTime + resourceRemainingDownTime;
                }
                // else
                {
                    return resourceDownTime;
                }
            }
        }
        return resourceDownTime;
    }

    // I have one assumption that the tasks cannot have suitabilities from different workcenters.
    // TWO exceptions exists ... for Mobile Resources that can travel from workcenter to workcenter and for 
    // Tools that can be transfered from workcenter to workcenter
    private Vector<TaskSimulator> findSuitableTaskSimulatorsForWorkcenterDataModelFromAvailableTaskSimulators(Vector<TaskSimulator> pendingTasksSimulators, WorkcenterDataModel workcenterDataModel) {
        Vector<TaskSimulator> workcenterPendingTasksSimulators = new Vector<TaskSimulator>();
        // NEW IMPLEMENTATION: check only if task belongs to a job released on the current workcenter
        for (TaskSimulator taskSimulator : pendingTasksSimulators) {
            // find the job that contains the task
            for (JobDataModel jobDataModel : jobDataModelVector) {
                if (jobDataModel.getWorkcenterDataModel() == null && jobDataModel.getTasks().contains(taskSimulator.getTaskDataModel())) {
                    // Means that there is not job/workcenter constrain so check incrementally
                    Vector<ResourceSimulator> suitabeResourceSimulators = getSuitableResourcesSimulatorForTask(taskSimulator);
                    for (ResourceSimulator resourceSimulator : suitabeResourceSimulators) {
                        if (workcenterDataModel.containsResourceDataModel(resourceSimulator.getResourceDataModel())) {
                            workcenterPendingTasksSimulators.add(taskSimulator);
                            break;
                        } else if (resourceSimulator.getResourceDataModel() instanceof ToolDataModel) {
                            // Check if tool type is compatible with workcenter's resources
                            boolean isToolSupportedByWorkcenterResource = false;
                            for (ResourceDataModel workcenterResourceDataModel : workcenterDataModel.getResourceDataModelVector()) {
                                if (workcenterResourceDataModel.getEndEffectorsDataModel().getSuportedToolTypes().contains(((ToolDataModel) resourceSimulator.getResourceDataModel()).getType())) {
                                    isToolSupportedByWorkcenterResource = true;
                                }
                            }
                            if (isToolSupportedByWorkcenterResource) {
                                workcenterPendingTasksSimulators.add(taskSimulator);
                                break;
                            }
                        } else if (resourceSimulator.getResourceDataModel() instanceof MobileResourceDataModel) {
                            // Check if workcenter has docking station compatible for mobile resource
                            boolean isMobileResourceSupportedByWorkcenter = false;
                            if (workcenterDataModel.getDockingStationDataModelVector() != null) {
                                for (DockingStationDataModel workcenterDockingStationDataModel : workcenterDataModel.getDockingStationDataModelVector()) {
                                    if (workcenterDockingStationDataModel.getSuportedMobileResourceTypes().contains(((MobileResourceDataModel) resourceSimulator.getResourceDataModel()).getType())) {
                                        isMobileResourceSupportedByWorkcenter = true;
                                    }
                                }
                                if (isMobileResourceSupportedByWorkcenter) {
                                    workcenterPendingTasksSimulators.add(taskSimulator);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                } else if (jobDataModel.getWorkcenterDataModel() != null && jobDataModel.getWorkcenterDataModel().equals(workcenterDataModel) && jobDataModel.getTasks().contains(taskSimulator.getTaskDataModel())) {
                    workcenterPendingTasksSimulators.add(taskSimulator);
                    break;
                }
            }
        }

        return workcenterPendingTasksSimulators;
    }

    /**
     * This method checks if the task is locked or if the task can be performed uninterrupted. If task is locked then checks the preassigned resource and then the preassigned time.
     * 
     * @param resourceSimulator
     *            The resource
     * @param taskSimulator
     *            The task
     * @return True if it is a good assignment
     */
    @Override
    public boolean canResourceFullfilTheTaskUniterrupted(ResourceSimulator resourceSimulator, TaskSimulator taskSimulator, Calendar time) {
        // Check for suitability...
        if (!isTaskSuitableForResource(taskSimulator, resourceSimulator))
            return false;

        // Checking the locked constraints of the task
        Vector<AssignmentDataModel> assignmentDataModelVector = prereleasedAssignments.getPrereleaseAssignmentDataModelVector();
        for (int i = 0; i < assignmentDataModelVector.size(); i++) {
            AssignmentDataModel assignment = assignmentDataModelVector.get(i);
            if (assignment.getTaskDataModel().equals(taskSimulator.getTaskDataModel())) {
                // Locked task
                if (assignment.getResourceDataModel().equals(resourceSimulator.getResourceDataModel())) {
                    // Locked on the same resource
                    if (assignment.getTimeOfDispatch().getTimeInMillis() == time.getTimeInMillis()) {
                        // Locked time is ok
                        return true;
                    }
                    // else
                    {
                        // Task is locked on other time
                        return false;
                    }
                }
                // else
                {
                    // Task is locked on other resource
                    return false;
                }
            }
        }

        // Checking the locked constraints of the resource since the task is not
        // locked
        for (int i = 0; i < assignmentDataModelVector.size(); i++) {
            AssignmentDataModel assignment = assignmentDataModelVector.get(i);

            if (assignment.getResourceDataModel().equals(resourceSimulator.getResourceDataModel())) {
                // Checking if the lock constraints can be fulfilled
                if (assignment.getTimeOfDispatch().getTimeInMillis() < time.getTimeInMillis()) {
                    // Checking if the locked assignment finishes before the
                    // time now.
                    if (assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds() > time.getTimeInMillis()) {
                        // The current pre-released assignment finishes after the
                        // time of the hypothetical assignment
                        return false;
                    }
                } else if (assignment.getTimeOfDispatch().getTimeInMillis() > time.getTimeInMillis()) {
                    // Checking if the current hypothetical assignment finishes
                    // before the pre-release assignment.
                    if (time.getTimeInMillis()
                            + getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, time, null)
                            + getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, time, null)
                            + getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, time, null) > assignment.getTimeOfDispatch().getTimeInMillis()) {
                        // The current pre-released assignment starts before the
                        // time of the hypothetical assignment finishes
                        return false;
                    }
                } else {
                    // The time of the hypothetical dispatch is locked for the
                    // desired resource
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getSetUpCodeForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator) {
        String setUpCode = "";
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSuitableResource.getTaskDataModel().equals(taskSimulator.getTaskDataModel()) && taskSuitableResource.getResourceDataModel().equals(resourceSimulator.getResourceDataModel())) {
                setUpCode = taskSuitableResource.getSetUpCode();
                break;
            }
        }
        return setUpCode;
    }

    @Override
    @Deprecated
    public long getSetUpTimeInMillisecondsFromSetUpCodeToSetUpCodeOnResource(String fromSetUpCode, String toSetUpCode, ResourceSimulator resourceSimulator) {
        long setUpTimeInMilliseconds = 0L;
        SetUpMatrixDataModel setUpMatrixDataModel = resourceSimulator.getResourceDataModel().getSetUpMatrixDataModel();
        if (setUpMatrixDataModel != null) {
            setUpTimeInMilliseconds = setUpMatrixDataModel.getSetUpTimeInMilliseconds(fromSetUpCode, toSetUpCode);
        }
        return setUpTimeInMilliseconds;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ManualPlanHelperInterface getManualPlanningHelperInterface() {
        PlanSimulator manualSimulator = new PlanSimulator();

        manualSimulator.startTime = Calendar.getInstance();
        manualSimulator.startTime.setTimeInMillis(this.startTime.getTimeInMillis());
        manualSimulator.timeNow = Calendar.getInstance();
        manualSimulator.timeNow.setTimeInMillis(manualSimulator.startTime.getTimeInMillis());
        manualSimulator.workcenterDataModelVector = (Vector<WorkcenterDataModel>) this.workcenterDataModelVector.clone();
        manualSimulator.precedenceConstraintDataModelVector = (Vector<TaskPrecedenceConstraintDataModel>) this.precedenceConstraintDataModelVector.clone();
        manualSimulator.taskSuitableResourceDataModelVector = (Vector<TaskSuitableResourceDataModel>) this.taskSuitableResourceDataModelVector.clone();

        manualSimulator.resources = new PoolOfResourcesSimulator(manualSimulator.workcenterDataModelVector);
        manualSimulator.tasks = new PoolOfTasksSimulator((Vector<JobDataModel>) jobDataModelVector.clone());
        Vector<AssignmentDataModel> prereleasedAssignmentsForManualPlanning = (Vector<AssignmentDataModel>) prereleasedAssignmentDataModelVector.clone();
        prereleasedAssignmentsForManualPlanning.addAll((Vector<AssignmentDataModel>) this.assignments.getAssignmentDataModelVector().clone());
        manualSimulator.prereleasedAssignments = new PoolOfPrereleasedAssignmentsSimulator(prereleasedAssignmentsForManualPlanning);
        manualSimulator.assignments = new PoolOfAssignmentsSimulator(new Vector<AssignmentDataModel>());

        // And lastly the temp variables ...
        manualSimulator.prereleasedAssignmentDataModelVector = (Vector<AssignmentDataModel>) prereleasedAssignmentsForManualPlanning.clone();
        manualSimulator.jobDataModelVector = (Vector<JobDataModel>) jobDataModelVector.clone();

        return manualSimulator;
    }

    @Override
    public void addManualAssignment(AssignmentDataModel assignment) {
        this.assignments.addAssignment(assignment);
        this.resources.notifyForNewAssignment(assignment);
        this.tasks.notifyForNewAssignment(assignment);
    }

    @Override
    public void addManualAssignments(Vector<AssignmentDataModel> assignments) {
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            this.assignments.addAssignment(assignment);
            this.resources.notifyForNewAssignment(assignment);
            this.tasks.notifyForNewAssignment(assignment);
        }
    }

    @Override
    public void setManualTimeNow(Calendar newTimeNow) {

        Vector<Interupt> interupts = new Vector<Interupt>();
        interupts.add(new Interupt(timeNow, PlanSimulator.class, Interupt.PLAN_START));
        // Interupt interupt = new Interupt(timeNow, null, Interupt.PLAN_START);
        PlanEndRule planEndRule = new WorkloadAllocationUntilDateEndRule(newTimeNow);

        // Checking for pre released (locked) assignments before the start date of the schedule and dispatch them ...
        // Also if they are finished before the start date inform the corresponding resources.
        Vector<AssignmentDataModel> allPrereleasedAssignments = this.prereleasedAssignments.getPrereleaseAssignmentDataModelVector();
        for (int i = 0; i < allPrereleasedAssignments.size(); i++) {
            AssignmentDataModel prereleasedAssignment = allPrereleasedAssignments.get(i);
            if (timeNow.getTimeInMillis() >= allPrereleasedAssignments.get(i).getTimeOfDispatch().getTimeInMillis()) {
                // Since the pre-released assignment must be assigned make a new one with updated set up and operational times and replace the old
                AssignmentDataModel newPrereleasedAssignment = new AssignmentDataModel(prereleasedAssignment.getTaskDataModel(), prereleasedAssignment.getResourceDataModel(), prereleasedAssignment.getTimeOfDispatch(),
                        getSetUpTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null)
                                + getOperationTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null)
                                + getResourceDownTimeInMillisecondsForTaskOnResource(tasks.getTaskSimulator(prereleasedAssignment.getTaskDataModel()), resources.getResourceSimulator(prereleasedAssignment.getResourceDataModel()), prereleasedAssignment.getTimeOfDispatch(), null), true, prereleasedAssignment.getProperties());
                prereleasedAssignment = newPrereleasedAssignment;
                allPrereleasedAssignments.set(i, prereleasedAssignment);

                this.assignments.addAssignment(prereleasedAssignment);

                // Checking the finish time ...
                if (timeNow.getTimeInMillis() >= allPrereleasedAssignments.get(i).getTimeOfDispatch().getTimeInMillis() + allPrereleasedAssignments.get(i).getDurationInMilliseconds()) {
                    this.resources.notifyForNewAssignment(prereleasedAssignment);
                    this.tasks.notifyForNewAssignment(prereleasedAssignment);

                    this.resources.notifyForAssignmentFinished(prereleasedAssignment);
                    this.tasks.notifyForAssignmentFinished(prereleasedAssignment);
                } else {
                    this.resources.notifyForNewAssignment(prereleasedAssignment);
                    this.tasks.notifyForNewAssignment(prereleasedAssignment);
                }
            }
        }

        // Loop until interupts exist
        while (interupts.size() > 0) {
            // Print the info if applicable
            // printDebugInfoForInterupt(interupt);

            // Check the end rule applicability
            if (planEndRule != null && !planEndRule.continuePlan(interupts, this)) {
                // Stop performing further allocations
                // printDebugInfoForPlanEndRule(planEndRule);
                break;
            }

            // Initializing the time now from the interupt's time
            timeNow = interupts.get(0).getDate();

            // Initialize the resources
            resources.init(timeNow);

            // Initialize list with the new interupt information.
            // Currently only used when an assignment finishes to set the
            // resources state back
            // and the task completed states
            for (Interupt interupt : interupts) {
                if (interupt.getMessage().equals(Interupt.ASSIGNMENTS_FINISHED)) { // Notify
                                                                                   // the
                                                                                   // pools
                                                                                   // of
                                                                                   // the
                                                                                   // new
                                                                                   // assignment
                                                                                   // that's
                                                                                   // finished
                    @SuppressWarnings("unchecked")
                    Vector<AssignmentDataModel> assignmentsFinished = (Vector<AssignmentDataModel>) interupt.getSource();
                    for (int i = 0; i < assignmentsFinished.size(); i++) {
                        AssignmentDataModel assignmentFinished = assignmentsFinished.get(i);
                        this.resources.notifyForAssignmentFinished(assignmentFinished);
                        this.tasks.notifyForAssignmentFinished(assignmentFinished);
                    }
                    // Initialize the resources
                    resources.init(timeNow);
                } else if (interupt.getMessage().equals(Interupt.LOCKED_CONSTRAINTS)) {
                    // make and dispatch the locked assignment
                    @SuppressWarnings("unchecked")
                    Vector<AssignmentDataModel> lockedAssignments = (Vector<AssignmentDataModel>) interupt.getSource();
                    for (int i = 0; i < lockedAssignments.size(); i++) {
                        AssignmentDataModel lockedAssignment = lockedAssignments.get(i);

                        this.assignments.addAssignment(lockedAssignment);

                        this.resources.notifyForNewAssignment(lockedAssignment);
                        this.tasks.notifyForNewAssignment(lockedAssignment);

                        // Checking the finish time ...
                        if (timeNow.getTimeInMillis() >= lockedAssignment.getTimeOfDispatch().getTimeInMillis() + lockedAssignment.getDurationInMilliseconds()) {
                            this.resources.notifyForAssignmentFinished(lockedAssignment);
                            this.tasks.notifyForAssignmentFinished(lockedAssignment);
                        }
                    }
                }
            }

            // Getting the next Interupt
            interupts = getNextInterupt();
        }
        timeNow = newTimeNow;
    }

    public static void printlnDebugMessage(String message) {
        if (PlanSimulator.DEBUG) {
            System.out.println(message);
        }
    }

    public static void printDebugMessage(String message) {
        if (PlanSimulator.DEBUG) {
            System.out.print(message);
        }
    }

    @Override
    public WorkcenterDataModel locateResource(ResourceDataModel resourceDataModel) {
        for (WorkcenterDataModel workcenterDataModel : workcenterDataModelVector) {
            // Checking docking stations in case of mobile robots
            if (workcenterDataModel.getDockingStationDataModelVector() != null) {
                for (DockingStationDataModel dockingStationDataModel : workcenterDataModel.getDockingStationDataModelVector()) {
                    if (dockingStationDataModel != null && dockingStationDataModel.getCurrentLoad() != null && dockingStationDataModel.getCurrentLoad().equals(resourceDataModel)) {
                        return workcenterDataModel;
                    }
                }
            }
            // Checking the workcenter resources
            if (workcenterDataModel.getResourceDataModelVector() != null) {
                for (ResourceDataModel workcenterResource : workcenterDataModel.getResourceDataModelVector()) {
                    if (workcenterResource.equals(resourceDataModel)) {
                        return workcenterDataModel;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDataModel locateTool(ToolDataModel toolDataModel) {
        for (WorkcenterDataModel workcenterDataModel : workcenterDataModelVector) {
            // Checking the workcenter inventory
            if (workcenterDataModel.getInventoryDataModel() != null && workcenterDataModel.getInventoryDataModel().getTools() != null &&
                    workcenterDataModel.getInventoryDataModel().getTools().contains(toolDataModel)) {
                return workcenterDataModel;
            }
            // Checking the resources
            for (ResourceDataModel resourceDataModel : workcenterDataModel.getResourceDataModelVector()) {
                if (resourceDataModel.getEndEffectorsDataModel() != null && resourceDataModel.getEndEffectorsDataModel().getTools() != null &&
                        resourceDataModel.getEndEffectorsDataModel().getTools().contains(toolDataModel)) {
                    return resourceDataModel;
                }
            }
            // Checking workcenter's mobile resources
            for (DockingStationDataModel dockingStation : workcenterDataModel.getDockingStationDataModelVector()) {
                if (dockingStation.getCurrentLoad() != null) {
                    if (dockingStation.getCurrentLoad().getEndEffectorsDataModel() != null) {
                        Vector<ToolDataModel> tools = dockingStation.getCurrentLoad().getEndEffectorsDataModel().getTools();
                        if (tools.contains(toolDataModel)) {
                            return dockingStation.getCurrentLoad();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public JobDataModel getJobDataModelForTaskSimulator(TaskSimulator taskSimulator) {
        for (JobDataModel job : jobDataModelVector) {
            if (job.getWorkcenterDataModel() != null && job.getTasks().contains(taskSimulator.getTaskDataModel())) {
                return job;
            }
        }
        return null;
    }

    @Override
    public AssignmentDataModel getAssignmentOfTaskDataModel(TaskDataModel taskDataModel) {
        return assignments.getAssignmentForTask(taskDataModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<WorkcenterDataModel> getAllWorkcentrers() {
        return (Vector<WorkcenterDataModel>) workcenterDataModelVector.clone();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<AssignmentDataModel> getAssignments() {
        return (Vector<AssignmentDataModel>) this.assignments.getAssignmentDataModelVector().clone();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<JobDataModel> getJobs() {
        return (Vector<JobDataModel>) this.jobDataModelVector.clone();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<TaskPrecedenceConstraintDataModel> getTaskPrecedenceConstraints() {
        return (Vector<TaskPrecedenceConstraintDataModel>) precedenceConstraintDataModelVector.clone();
    }

    @Override
    public TaskSimulator getTaskSimulatorFromTaskDataModel(TaskDataModel taskDataModel) {
        return tasks.getTaskSimulator(taskDataModel);
    }

    @Override
    public ResourceSimulator getResourceSimulatorFromResourceDataModel(ResourceDataModel resourceDataModel) {
        return resources.getResourceSimulator(resourceDataModel);
    }
}
