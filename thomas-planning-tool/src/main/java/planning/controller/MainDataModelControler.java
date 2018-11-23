package planning.controller;

import java.util.Calendar;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import planning.ControllerToPlanningInputDataModelInterface;
import planning.ControllerToPlanningOutputDataModelInterface;
import planning.model.AssignmentDataModel;
import planning.model.JobDataModel;
import planning.model.PlanningInputDataModel;
import planning.model.PlanningOutputDataModel;
import planning.model.ResourceAvailabilityDataModel;
import planning.model.ResourceDataModel;
import planning.model.SetUpMatrixDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.WorkcenterDataModel;
import planning.model.io.AbstractOutputSource;
import planning.model.io.XMLPlanningEntityResolver;
import planning.model.util.TimePeriodDataModel;
import planning.scheduler.simulation.PlanEndRule;
import planning.scheduler.simulation.PlanSimulator;
import planning.scheduler.simulation.WorkloadAllocationUntilDateEndRule;

/**
 * The main controller class. The controller in order to perform various task
 * (i.e. reschedule, assignment move etc) must be aware of the Planning input as
 * well as the planning output models
 */

public class MainDataModelControler implements ControllerToPlanningInputDataModelInterface, ControllerToPlanningOutputDataModelInterface {
    /**
     * The input model that will be manipulated
     */
    private PlanningInputDataModel planningInputDataModel = null;

    /**
     * The output model that will be manipulated
     */
    private PlanningOutputDataModel planningOutputDataModel = null;

    /**
     * The output source that will be used to save the models
     */
    private AbstractOutputSource aos = null;

    /**
     * Default constructor
     * 
     * @param pidm
     *            The Planning Input Data Model
     * @param podm
     *            The Planning Output Data Model
     */
    public MainDataModelControler(PlanningInputDataModel pidm, PlanningOutputDataModel podm, AbstractOutputSource aos) {
        this.planningInputDataModel = pidm;
        this.planningOutputDataModel = podm;
        this.aos = aos;
    }

    public ControllerToPlanningInputDataModelInterface getControllerToPlanningInputDataModelInterface() {
        return this;
    }

    public ControllerToPlanningOutputDataModelInterface getControllerToPlanningOutputDataModelInterface() {
        return this;
    }

    /********************************************************************************/
    /***** FUNCTIONS FROM ControllerToPlanningInputDataModelInterface INTERFACE *****/
    /********************************************************************************/

    public void addWorkcenterDataModel(WorkcenterDataModel newWorkcenter) {
        // Getting the jobs
        Vector<WorkcenterDataModel> workcentersVector = planningInputDataModel.getWorkcenterDataModelVector();
        // Adding the new job
        workcentersVector.add(newWorkcenter);
        // Setting again the vector since we just got a copy of the original
        // vector
        planningInputDataModel.setWorkcenterDataModelVector(workcentersVector);
    }

    public void removeWorkcenterDataModel(WorkcenterDataModel workcenter) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public void setNameToWorkcenterDataModel(String newName, WorkcenterDataModel workcenter) {
        planningInputDataModel.setNameToWorkcenterDataModel(newName, workcenter);
    }

    public void setAlgorithmToWorkcenterDataModel(String algorithm, WorkcenterDataModel workcenter) {
        planningInputDataModel.setAlgorithmToWorkcenterDataModel(algorithm, workcenter);
    }

    public void addResourceDataModelToWorkcenter(ResourceDataModel newResource, WorkcenterDataModel workcenter) {
        planningInputDataModel.addResourceDataModelToWorkcenter(newResource, workcenter);
    }

    public void setNameToResourceDataModel(String newName, ResourceDataModel resource) {
        planningInputDataModel.setNameToResourceDataModel(newName, resource);
    }

    public void removeResourceDataModelToWorkcenter(ResourceDataModel resource) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public void setResourceAvailabilityDataModelToResourceDataModelName(ResourceAvailabilityDataModel newResourceAvailabilityDataModel, ResourceDataModel resource) {
        planningInputDataModel.setResourceAvailabilityDataModelToResourceDataModelName(newResourceAvailabilityDataModel, resource);
    }

    public void setSetUpCodeToTaskSuitableResourceDataModel(String newSetUpCode, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        planningInputDataModel.setSetUpCodeToTaskSuitableResourceDataModel(newSetUpCode, taskSuitableResourceDataModel);
    }

    public void addJobDataModel(JobDataModel newJobDataModel) {
        // Getting the jobs
        Vector<JobDataModel> jobsVector = planningInputDataModel.getJobDataModelVector();
        // Adding the new job
        jobsVector.add(newJobDataModel);
        // Setting again the vector since we just got a copy of the original
        // vector
        planningInputDataModel.setJobDataModelVector(jobsVector);
        // Updating the task start/due dates of the job
        planningInputDataModel.calculateTaskArrivalAndDueDates(newJobDataModel);
    }

    public void removeJobDataModel(JobDataModel job) {
        // Removing from jobs
        Vector<JobDataModel> jobsVector = planningInputDataModel.getJobDataModelVector();
        jobsVector.remove(job);
        // Removing from tasks
        Vector<TaskDataModel> tasksToBeRemoved = job.getTasks();
        Vector<TaskDataModel> tasksVector = planningInputDataModel.getTaskDataModelVector();
        tasksVector.removeAll(tasksToBeRemoved);
        // Removing from suitable resources
        Vector<TaskSuitableResourceDataModel> suitableResourcesToBeRemoved = new Vector<TaskSuitableResourceDataModel>();
        Vector<TaskSuitableResourceDataModel> suitabilities = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        for (int i = 0; i < suitabilities.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResourceDataModel = (TaskSuitableResourceDataModel) suitabilities.get(i);
            if (tasksToBeRemoved.indexOf(taskSuitableResourceDataModel.getTaskDataModel()) != -1) {
                suitableResourcesToBeRemoved.add(taskSuitableResourceDataModel);
            }
        }
        suitabilities.removeAll(suitableResourcesToBeRemoved);
        // Removing from constrains
        Vector<TaskPrecedenceConstraintDataModel> constraintsToBeRemoved = new Vector<TaskPrecedenceConstraintDataModel>();
        Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        for (int i = 0; i < constraints.size(); i++) {
            TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel = (TaskPrecedenceConstraintDataModel) constraints.get(i);
            if (tasksToBeRemoved.indexOf(taskPrecedenceConstraintDataModel.getPreconditionTask()) != -1 || tasksToBeRemoved.indexOf(taskPrecedenceConstraintDataModel.getPostconditionTask()) != -1) {
                constraintsToBeRemoved.add(taskPrecedenceConstraintDataModel);
            }
        }
        constraints.removeAll(constraintsToBeRemoved);
        // Removing from schedule
        Vector<AssignmentDataModel> assignmentsToBeRemoved = new Vector<AssignmentDataModel>();
        Vector<AssignmentDataModel> assignments = planningOutputDataModel.getAssignments();
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignmnet = (AssignmentDataModel) assignments.get(i);
            if (tasksToBeRemoved.indexOf(assignmnet.getTaskDataModel()) != -1) {
                assignmentsToBeRemoved.add(assignmnet);
            }
        }
        assignments.removeAll(assignmentsToBeRemoved);
        // Commit
        planningOutputDataModel.setAssignments(assignments);
        planningInputDataModel.setTaskPrecedenceConstraintsDataModelVector(constraints);
        planningInputDataModel.setTaskSuitableResourceDataModelVector(suitabilities);
        planningInputDataModel.setTaskDataModelVector(tasksVector);
        planningInputDataModel.setJobDataModelVector(jobsVector);
    }

    public void setNameToJobDataModel(String newName, JobDataModel jobDataModel) {
        planningInputDataModel.setNameToJobDataModel(newName, jobDataModel);
    }

    public void setArrivalDateToJobDataModel(Calendar newArrivalCalendarDate, JobDataModel jobDataModel) {
        planningInputDataModel.setArrivalDateToJobDataModel(newArrivalCalendarDate, jobDataModel);
        // Updating the task start/due dates of the job
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void setDueDateToJobDataModel(Calendar newDueCalendarDate, JobDataModel jobDataModel) {
        planningInputDataModel.setDueDateToJobDataModel(newDueCalendarDate, jobDataModel);
        // Updating the task start/due dates of the job
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void addTaskDataModelToJob(TaskDataModel newTaskDataModel, JobDataModel jobDataModel) {
        planningInputDataModel.addTaskDataModelToJob(newTaskDataModel, jobDataModel);
        // Updating the task start/due dates of the job
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void setNameToTaskDataModel(String newName, TaskDataModel jobDataModel) {
        planningInputDataModel.setNameToTaskDataModel(newName, jobDataModel);
    }

    public void removeTaskDataModel(TaskDataModel task) {
        // Removing the task from the job
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(task);
        Vector<TaskDataModel> jobTasksVector = jobDataModel.getTasks();
        jobTasksVector.remove(task);

        // Removing the task form the workload
        Vector<TaskDataModel> tasksVector = planningInputDataModel.getTaskDataModelVector();
        tasksVector.remove(task);

        // Removing from suitable resources
        Vector<TaskSuitableResourceDataModel> suitableResourcesToBeRemoved = new Vector<TaskSuitableResourceDataModel>();
        Vector<TaskSuitableResourceDataModel> suitabilities = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        for (int i = 0; i < suitabilities.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResourceDataModel = suitabilities.get(i);
            if (task == taskSuitableResourceDataModel.getTaskDataModel()) {
                suitableResourcesToBeRemoved.add(taskSuitableResourceDataModel);
            }
        }
        suitabilities.removeAll(suitableResourcesToBeRemoved);

        // Removing from constrains
        Vector<TaskPrecedenceConstraintDataModel> constraintsToBeRemoved = new Vector<TaskPrecedenceConstraintDataModel>();
        Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        for (int i = 0; i < constraints.size(); i++) {
            TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel = constraints.get(i);
            if (task == taskPrecedenceConstraintDataModel.getPreconditionTask() || task == taskPrecedenceConstraintDataModel.getPostconditionTask()) {
                constraintsToBeRemoved.add(taskPrecedenceConstraintDataModel);
            }
        }
        constraints.removeAll(constraintsToBeRemoved);

        // Removing from schedule
        Vector<AssignmentDataModel> assignmentsToBeRemoved = new Vector<AssignmentDataModel>();
        Vector<AssignmentDataModel> assignments = planningOutputDataModel.getAssignments();
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignmnet = assignments.get(i);
            if (task == assignmnet.getTaskDataModel()) {
                assignmentsToBeRemoved.add(assignmnet);
            }
        }
        assignments.removeAll(assignmentsToBeRemoved);
        // Commit
        planningOutputDataModel.setAssignments(assignments);
        planningInputDataModel.setTaskPrecedenceConstraintsDataModelVector(constraints);
        planningInputDataModel.setTaskSuitableResourceDataModelVector(suitabilities);
        planningInputDataModel.setTaskDataModelVectorToJobDataModel(jobTasksVector, jobDataModel);
        planningInputDataModel.setTaskDataModelVector(tasksVector);

        // Updating the task start/due dates of the job
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void addTaskSuitableResourceDataModel(TaskSuitableResourceDataModel newSuitable) {
        // Getting the suitabilities
        Vector<TaskSuitableResourceDataModel> suitabilities = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        // Adding the new constraint
        suitabilities.add(newSuitable);
        // Setting again the vector since we just got a copy of the original
        // vector
        planningInputDataModel.setTaskSuitableResourceDataModelVector(suitabilities);
        // Updating the task start/due dates of the job
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(newSuitable.getTaskDataModel());
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void removeTaskSuitableResourceDataModel(TaskSuitableResourceDataModel suitable) {
        // Getting the suitabilities
        Vector<TaskSuitableResourceDataModel> suitabilities = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        // Remove the suitable
        suitabilities.remove(suitable);
        // Setting again the vector since we just got a copy of the original
        // vector
        planningInputDataModel.setTaskSuitableResourceDataModelVector(suitabilities);
        // Updating the task start/due dates of the job
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(suitable.getTaskDataModel());
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void setTaskDataModelToTaskSuitableResourceDataModel(TaskDataModel task, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public void setResourceDataModelToTaskSuitableResourceDataModel(ResourceDataModel resource, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public void setOperationTimeInMillisecondsToTaskSuitableResourceDataModel(long operationTimeInMilliseconds, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        planningInputDataModel.setOperationTimeInMillisecondsToTaskSuitableResourceDataModel(operationTimeInMilliseconds, taskSuitableResourceDataModel);
        // Updating the task start/due dates of the job
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(taskSuitableResourceDataModel.getTaskDataModel());
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void addTaskPrecedenceConstraintDataModel(TaskPrecedenceConstraintDataModel newConstraint) {
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(newConstraint.getPreconditionTask());
        // Checking if tasks are of the same job
        if (jobDataModel == planningInputDataModel.getJobDataModelForTaskDataModel(newConstraint.getPostconditionTask())) {
            // Getting the constraints
            Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
            // Adding the new constraint
            constraints.add(newConstraint);
            // Setting again the vector since we just got a copy of the original
            // vector
            planningInputDataModel.setTaskPrecedenceConstraintsDataModelVector(constraints);

            // Updating the task start/due dates of the job
            planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
        }
    }

    public void removeTaskPrecedenceConstraintDataModel(TaskPrecedenceConstraintDataModel constraint) {
        // Getting the constraints
        Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        // Remove the constraint
        constraints.remove(constraint);
        // Setting again the vector since we just got a copy of the original
        // vector
        planningInputDataModel.setTaskPrecedenceConstraintsDataModelVector(constraints);
        // Updating the task start/due dates of the job
        JobDataModel jobDataModel = planningInputDataModel.getJobDataModelForTaskDataModel(constraint.getPreconditionTask());
        planningInputDataModel.calculateTaskArrivalAndDueDates(jobDataModel);
    }

    public void setPreconditionTaskToTaskPrecedenceConstraintDataModel(TaskDataModel task, TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public void setPostconditionTaskToTaskPrecedenceConstraintDataModel(TaskDataModel task, TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel) {
        throw new UnsupportedOperationException("Function not implented yet");
    }

    public boolean isResourceDown(ResourceDataModel resource, Calendar currentTime) {
        // Checking first the exception dates that the resource is surely up
        Vector<TimePeriodDataModel> exceptionPeriods = resource.getResourceAvailabilityDataModel().getWorkingPeriods();
        for (int i = 0; i < exceptionPeriods.size(); i++) {
            TimePeriodDataModel period = (TimePeriodDataModel) exceptionPeriods.get(i);
            Calendar fromDate = period.getFromDate();
            long fromDateInMilliseconds = fromDate.getTimeInMillis();
            Calendar toDate = period.getToDate();
            long toDateInMilliseconds = toDate.getTimeInMillis();
            long periodInMilliseconds = period.getPeriodInMilliseconds();
            long currentTimeInMilliseconds = currentTime.getTimeInMillis();
            if (currentTimeInMilliseconds > fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = currentTimeInMilliseconds - fromDateInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds;
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - ((quotient * periodInMilliseconds) + fromDateInMilliseconds);
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        return false;
                    }
                } else {
                    if (fromDate.getTimeInMillis() < currentTime.getTimeInMillis() && currentTime.getTimeInMillis() <= toDate.getTimeInMillis()) {
                        return false;
                    }
                }
            } else if (currentTimeInMilliseconds < fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = fromDateInMilliseconds - currentTimeInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds + 1;// We
                                                                                                             // add
                                                                                                             // one
                                                                                                             // because
                                                                                                             // we
                                                                                                             // want
                                                                                                             // the
                                                                                                             // first
                                                                                                             // period
                                                                                                             // start
                                                                                                             // BEFORE
                                                                                                             // the
                                                                                                             // current
                                                                                                             // time
                                                                                                             // (Rounded
                                                                                                             // up)
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - (fromDateInMilliseconds - (quotient * periodInMilliseconds));
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        return false;
                    }
                } else {
                    if (fromDate.getTimeInMillis() < currentTime.getTimeInMillis() && currentTime.getTimeInMillis() <= toDate.getTimeInMillis()) {
                        return false;
                    }
                }
            } else
            // currentTimeInMilliseconds == fromDateInMilliseconds thus the
            // resource is about to get down
            {
                return false;
            }
        }

        // Checking the rest of the resource calendar
        Vector<TimePeriodDataModel> periods = resource.getResourceAvailabilityDataModel().getNonWorkingPeriods();
        boolean isResourceDown = false;
        for (int i = 0; i < periods.size(); i++) {
            TimePeriodDataModel period = (TimePeriodDataModel) periods.get(i);
            Calendar fromDate = period.getFromDate();
            long fromDateInMilliseconds = fromDate.getTimeInMillis();
            Calendar toDate = period.getToDate();
            long toDateInMilliseconds = toDate.getTimeInMillis();
            long periodInMilliseconds = period.getPeriodInMilliseconds();
            long currentTimeInMilliseconds = currentTime.getTimeInMillis();
            if (currentTimeInMilliseconds > fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = currentTimeInMilliseconds - fromDateInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds;
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - ((quotient * periodInMilliseconds) + fromDateInMilliseconds);
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        isResourceDown = true;
                        break;
                    }
                } else {
                    if (fromDate.getTimeInMillis() <= currentTime.getTimeInMillis() && currentTime.getTimeInMillis() < toDate.getTimeInMillis()) {
                        isResourceDown = true;
                        break;
                    }
                }
            } else if (currentTimeInMilliseconds < fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = fromDateInMilliseconds - currentTimeInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds + 1; // We
                                                                                                              // add
                                                                                                              // one
                                                                                                              // because
                                                                                                              // we
                                                                                                              // want
                                                                                                              // the
                                                                                                              // first
                                                                                                              // period
                                                                                                              // start
                                                                                                              // BEFORE
                                                                                                              // the
                                                                                                              // current
                                                                                                              // time
                                                                                                              // (Rounded
                                                                                                              // up)
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - (fromDateInMilliseconds - (quotient * periodInMilliseconds));
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        isResourceDown = true;
                        break;
                    }
                } else {
                    if (fromDate.getTimeInMillis() <= currentTime.getTimeInMillis() && currentTime.getTimeInMillis() < toDate.getTimeInMillis()) {
                        isResourceDown = true;
                        break;
                    }
                }
            } else
            // currentTimeInMilliseconds == fromDateInMilliseconds thus the
            // resource is about to get down
            {
                isResourceDown = true;
                break;
            }
        }
        return isResourceDown;
    }

    public Calendar getPlanStartDate() {
        return this.planningInputDataModel.getPlanStartDate();
    }

    public void setPlanStartDate(Calendar newPlanStartDate) {
        this.planningInputDataModel.setPlanStartDate(newPlanStartDate);
    }

	@Override
	public Calendar getPlanEndDate() {
        return this.planningInputDataModel.getPlanEndDate();
	}

	@Override
	public void setPlanEndDate(Calendar newPlanEndDate) {
        this.planningInputDataModel.setPlanEndDate(newPlanEndDate);
	}

	@Override
	public void setContinueAssignmentsAfterPlanEndDate(boolean continueAssignments) {
		this.planningInputDataModel.setContinueAssignmentsAfterPlanEndDate(continueAssignments);
	}

	// The save method
    public void savePlanningInputDataModel() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new XMLPlanningEntityResolver());
            // DocumentType dt = null;
            // dt =
            // db.getDOMImplementation().createDocumentType(getRootElement(),
            // null, this.getSystemDTD());
            // document = db.getDOMImplementation().createDocument(null,
            // getRootElement(), dt);

            Document document = db.getDOMImplementation().createDocument(null, "PLANNING_INPUT", null);
            Element xmlRootElement = document.getDocumentElement();

            Node xmlPlanningInputNode = planningInputDataModel.toXMLNode(document);

            xmlRootElement.appendChild(xmlPlanningInputNode);

            aos.savePlanningInputXMLDocument(document);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /************************************************************************************/
    /***** END FUNCTIONS FROM ControllerToPlanningInputDataModelInterface INTERFACE *****/
    /************************************************************************************/

    /*********************************************************************************/
    /***** FUNCTIONS FROM ControllerToPlanningOutputDataModelInterface INTERFACE *****/
    /*********************************************************************************/

    public void addAssignmentDataModel(AssignmentDataModel newAssignment) {
        planningOutputDataModel.addAssignmentDataModel(newAssignment);
    }

    public void removeAssignmentDataModel(AssignmentDataModel assignment) {
        planningOutputDataModel.removeAssignmentDataModel(assignment);
    }

    public void setTaskDataModelToAssignmentDataModel(TaskDataModel task, AssignmentDataModel assignment) {
        planningOutputDataModel.setTaskDataModelToAssignmentDataModel(task, assignment);
    }

    public void setResourceDataModelToAssignmentDataModel(ResourceDataModel resource, AssignmentDataModel assignment) {
        planningOutputDataModel.setResourceDataModelToAssignmentDataModel(resource, assignment);
    }

    public void setDurationInMillisecondsToAssignmentDataModel(long newDurationInMilliseconds, AssignmentDataModel assignment) {
        planningOutputDataModel.setDurationInMillisecondsToAssignmentDataModel(newDurationInMilliseconds, assignment);
    }

    public void setTimeOfDispatchToAssignmentDataModel(Calendar newTimeOfDispatch, AssignmentDataModel assignment) {
        planningOutputDataModel.setTimeOfDispatchToAssignmentDataModel(newTimeOfDispatch, assignment);
    }

    public boolean isTaskSuitableForResource(TaskDataModel taskDataModel, ResourceDataModel resourceDataModel) {
        Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = (TaskSuitableResourceDataModel) taskSuitableResourceDataModelVector.get(i);
            if (taskDataModel.equals(taskSuitableResource.getTaskDataModel()) && resourceDataModel.equals(taskSuitableResource.getResourceDataModel())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if a task assignment on a given dispatch time with a
     * given duration (independent of the assigned resource) fulfills his pre
     * and post conditions
     * 
     * @param taskDataModel
     * @param currentTime
     * @param durationInMilliseconds
     * @return
     */
    public boolean hasTaskPendingPresendenceConstraints(TaskDataModel taskDataModel, Calendar currentTime, long durationInMilliseconds) {
        Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintDataModelVector = planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        for (int i = 0; i < precedenceConstraintDataModelVector.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = (TaskPrecedenceConstraintDataModel) precedenceConstraintDataModelVector.get(i);

            // Checking the preconditions
            if (constraint.getPostconditionTask().equals(taskDataModel)) {
                // Check if the precondition task is already assigned
                AssignmentDataModel assignment = this.getAssignmentForTask(constraint.getPreconditionTask());
                if (assignment != null) {
                    // An assignment has been made checking if the currentTime
                    // is greater than the pre codition assignment finish date
                    long finishDateInMilliseconds = assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds();
                    if (finishDateInMilliseconds > currentTime.getTimeInMillis()) {
                        // System.err.println("PRECONDITIONS:: \n"
                        // +"TaskName= " + taskDataModel.getTaskName() + "\n"
                        // +"PreCondTN= " +
                        // assignment.getTaskDataModel().getTaskName() + "\n");
                        // System.out.println("Checking Preconditions :: An assignment has been made checking if the currentTime is greater than the pre codition assignment finish date");
                        // System.out.println("TaskName:: " +
                        // assignment.getTaskDataModel().getTaskName());
                        // System.out.println("finishDateInMilliseconds :: " +
                        // finishDateInMilliseconds);
                        // System.out.println("currentTime.getTimeInMillis() :: "
                        // + currentTime.getTimeInMillis());
                        return true;
                    }
                } else {
                    // No assignment is made so the constraint is pending
                    return true;
                }
            }
            // Checking the post coditions
            else if (constraint.getPreconditionTask().equals(taskDataModel)) {
                // find if the post codition has been assigned and if it has,
                // then check
                // if the post codition assignment starts before pre codition
                // assignment finish
                AssignmentDataModel postcoditionAssignment = getAssignmentForTask(constraint.getPostconditionTask());
                if (postcoditionAssignment != null && postcoditionAssignment.getTimeOfDispatch().getTimeInMillis() < currentTime.getTimeInMillis() + durationInMilliseconds) {
                    // System.err.println("POST CONDITIONS");
                    return true;
                }

            }
        }
        // No constraint found for the task
        return false;
    }

    public boolean canResourceFullfilTheTaskUniterrupted(ResourceDataModel resource, TaskDataModel task, Calendar timeOfHypotheticalDispatch) {
        // TODO: Implement lock of tasks in a given dispatch time
        Vector<AssignmentDataModel> resourceAssignments = planningOutputDataModel.getAssignmentsForResource(resource.getResourceId());
        long durationOfHypotheticalAssignmnetInMilliseconds = this.getSetUpTimeInMillisecondsForTaskOnResource(task, resource, timeOfHypotheticalDispatch) + this.getOperationTimeInMillisecondsForTaskOnResource(task, resource) + this.getResourceDownTimeInMillisecondsForTaskOnResource(task, resource, timeOfHypotheticalDispatch);

        // Sorting the assignments ...
        // Copying it into an array and sorting the array
        // TODO: Better sorting algorithm
        AssignmentDataModel[] resourceAssignmentsArray = null;
        int resourceAssignmentsSize = resourceAssignments.size();
        if (resourceAssignmentsSize > 0) {
            resourceAssignmentsArray = new AssignmentDataModel[resourceAssignmentsSize];
            resourceAssignments.copyInto(resourceAssignmentsArray);
            for (int i = resourceAssignmentsArray.length; --i >= 0;) {
                boolean flipped = false;
                for (int j = 0; j < i; j++) {
                    if (resourceAssignmentsArray[j].getTimeOfDispatch().getTimeInMillis() > resourceAssignmentsArray[j + 1].getTimeOfDispatch().getTimeInMillis()) {
                        AssignmentDataModel temp = resourceAssignmentsArray[j];
                        resourceAssignmentsArray[j] = resourceAssignmentsArray[j + 1];
                        resourceAssignmentsArray[j + 1] = temp;
                        flipped = true;
                    }
                }
                if (!flipped) {
                    continue;
                }
            }
        }
        // Traverse the sorted array and see if the assignment can be fulfilled.
        if (resourceAssignmentsArray != null) {
            for (int i = 0; i < resourceAssignmentsArray.length; i++) {
                AssignmentDataModel assignment = resourceAssignmentsArray[i];
                // long assignmentFinishTimeInMilliseconds =
                // assignment.getTimeOfDispatch().getTimeInMillis()+assignment.getDurationInMilliseconds();
                long assignmentFinishTimeInMilliseconds = assignment.getTimeOfDispatch().getTimeInMillis() + +getOperationTimeInMillisecondsForTaskOnResource(assignment.getTaskDataModel(), assignment.getResourceDataModel()) + getSetUpTimeInMillisecondsForTaskOnResource(assignment.getTaskDataModel(), assignment.getResourceDataModel(), assignment.getTimeOfDispatch()) + getResourceDownTimeInMillisecondsForTaskOnResource(assignment.getTaskDataModel(), assignment.getResourceDataModel(), assignment.getTimeOfDispatch());
                if (assignmentFinishTimeInMilliseconds <= timeOfHypotheticalDispatch.getTimeInMillis()) {
                    if (i == resourceAssignmentsArray.length - 1) {
                        // Means that we are checking the last assignmnet so the
                        // new hypothetical dispatch can be done
                        // since the resource has fulfilled all its obligations
                        return true;
                    }
                    // else // The next assignmnet exists
                    // {
                    // Try to find out if the new hypothetical dispatch can be
                    // performed between the time
                    // of the current assignment and the next one.
                    AssignmentDataModel nextAssignment = resourceAssignmentsArray[i + 1];
                    if ((timeOfHypotheticalDispatch.getTimeInMillis() + durationOfHypotheticalAssignmnetInMilliseconds) <= nextAssignment.getTimeOfDispatch().getTimeInMillis())
                        return true;
                    // }
                } else {
                    // Check if the assignment start time is after the
                    // hypothetical finish time
                    if (timeOfHypotheticalDispatch.getTimeInMillis() + durationOfHypotheticalAssignmnetInMilliseconds <= assignment.getTimeOfDispatch().getTimeInMillis()) {
                        if (i == 0) {
                            // Means that we are checking the first assignmnet
                            // so the new hypothetical dispatch can be done
                            // since the hypothetical assignment finish time is
                            // before the first assignmnet start time
                            return true;
                        }
                        // else // The previous assignmnet exists
                        // {
                        // Try to find out if the new hypothetical dispatch can
                        // be performed between the time
                        // of the current assignment and the previous one.
                        AssignmentDataModel previousAssignment = resourceAssignmentsArray[i - 1];
                        if (timeOfHypotheticalDispatch.getTimeInMillis() >= previousAssignment.getTimeOfDispatch().getTimeInMillis() + previousAssignment.getDurationInMilliseconds())
                            return true;
                        // }
                    }
                }
            }
        } else
        // No assignments thus it can fulfill the hypothetical assignment
        {
            return true;
        }
        return false;
    }

    public long getOperationTimeInMillisecondsForTaskOnResource(TaskDataModel taskDataModel, ResourceDataModel resourceDataModel) {
        long time = 0;
        Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSuitableResource.getTaskDataModel().equals(taskDataModel) && taskSuitableResource.getResourceDataModel().equals(resourceDataModel)) {
                time = taskSuitableResource.getOperationTimeInMilliseconds();
                break;
            }
        }
        return time;
    }

    public String getSetUpCodeForTaskOnResource(TaskDataModel task, ResourceDataModel resource) {
        String setUpCode = "";
        Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector = planningInputDataModel.getTaskSuitableResourceDataModelVector();
        for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = taskSuitableResourceDataModelVector.get(i);
            if (taskSuitableResource.getTaskDataModel().equals(task) && taskSuitableResource.getResourceDataModel().equals(resource)) {
                setUpCode = taskSuitableResource.getSetUpCode();
                break;
            }
        }
        return setUpCode;
    }

    @Deprecated
    public long getSetUpTimeInMillisecondsFromSetUpCodeToSetUpCodeOnResource(String fromSetUpCode, String toSetUpCode, ResourceDataModel resource) {
        long setUpTimeInMilliseconds = 0L;
        SetUpMatrixDataModel setUpMatrixDataModel = resource.getSetUpMatrixDataModel();
        if (setUpMatrixDataModel != null) {
            setUpTimeInMilliseconds = setUpMatrixDataModel.getSetUpTimeInMilliseconds(fromSetUpCode, toSetUpCode);
        }
        return setUpTimeInMilliseconds;
    }

    public long getSetUpTimeInMillisecondsForTaskOnResource(TaskDataModel task, ResourceDataModel resource, Calendar currentTime) {
        SetUpMatrixDataModel setUpMatrixDataModel = resource.getSetUpMatrixDataModel();
        String toCode = getSetUpCodeForTaskOnResource(task, resource);
        // In order to find the from set up code we must search for the latest
        // assignment of this resource.
        // If such an assignment does not exist we assume the set up code is
        // idle.
        String fromCode = null;
        AssignmentDataModel latestAssignmentDataModel = null;
        long latestAssignmentFinishTimeInMilliseconds = 0;
        Vector<AssignmentDataModel> assignmentDataModelVector = planningOutputDataModel.getAssignmentsForResource(resource.getResourceId());
        for (int i = 0; i < assignmentDataModelVector.size(); i++) {
            AssignmentDataModel assignmentDataModel = (AssignmentDataModel) assignmentDataModelVector.get(i);
            long currentAssignmentFinishTimeInMilliseconds = assignmentDataModel.getTimeOfDispatch().getTimeInMillis() + assignmentDataModel.getDurationInMilliseconds();
            if (currentAssignmentFinishTimeInMilliseconds <= currentTime.getTimeInMillis()) {
                if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resource.getResourceId()) && latestAssignmentDataModel == null) {
                    latestAssignmentDataModel = assignmentDataModel;
                    // Updating the latestAssignmentFinishTimeInMilliseconds
                    latestAssignmentFinishTimeInMilliseconds = currentAssignmentFinishTimeInMilliseconds;
                } else if (assignmentDataModel.getResourceDataModel().getResourceId().equals(resource.getResourceId())) {
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
            fromCode = getSetUpCodeForTaskOnResource(latestAssignmentDataModel.getTaskDataModel(), resource);
        }

        if (setUpMatrixDataModel != null) {
            return setUpMatrixDataModel.getSetUpTimeInMilliseconds(fromCode, toCode);
        }
        return 0;
    }

    public long getResourceDownTimeInMillisecondsForTaskOnResource(TaskDataModel task, ResourceDataModel resource, Calendar timeNow) {
        long durationOfAssignmentInMilliseconds = getSetUpTimeInMillisecondsForTaskOnResource(task, resource, timeNow) + getOperationTimeInMillisecondsForTaskOnResource(task, resource);
        long resourceDownTime = 0;
        while (durationOfAssignmentInMilliseconds > 0) {
            Calendar nextNonWorkingDate = getNextNonWorkingDate(resource, timeNow);
            if (nextNonWorkingDate != null) {
                durationOfAssignmentInMilliseconds = durationOfAssignmentInMilliseconds - (nextNonWorkingDate.getTimeInMillis() - timeNow.getTimeInMillis());
                timeNow = getNextWorkingDate(resource, nextNonWorkingDate);
                if (durationOfAssignmentInMilliseconds > 0) {
                    resourceDownTime = resourceDownTime + (timeNow.getTimeInMillis() - nextNonWorkingDate.getTimeInMillis());
                }
            } else {
                // Means that there is no further nonWorking period defined for
                // the
                // resource so return the already calculated resource down time
                // plus any time until the resource is up again in case
                // that the current time is inside the LAST non working period
                if (isResourceDown(resource, timeNow)) {
                    long resourceRemainingDownTime = 0;
                    Calendar nextWorkingDate = getNextNonWorkingDate(resource, timeNow);
                    resourceRemainingDownTime = nextWorkingDate.getTimeInMillis() - timeNow.getTimeInMillis();
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

    public void reschedule(Calendar startDate, Vector<AssignmentDataModel> lockedAssignmnets) {
        PlanSimulator simulator = new PlanSimulator(this.planningInputDataModel.getWorkcenterDataModelVector(), this.planningInputDataModel.getJobDataModelVector(), lockedAssignmnets, this.planningInputDataModel.getTaskPrecedenceConstraintDataModelVector(), this.planningInputDataModel.getTaskSuitableResourceDataModelVector(), startDate);
        Vector<AssignmentDataModel> assignments = null;
        if (this.planningInputDataModel.continueAssignmentsAfterPlanEndDate()) {
            assignments = simulator.simulate(null);
        } else {
            PlanEndRule endRule = new WorkloadAllocationUntilDateEndRule(this.planningInputDataModel.getPlanEndDate());
            assignments = simulator.simulate(endRule);
        }

        planningOutputDataModel.setAssignments(assignments);
    }

    public void lockRecursive(AssignmentDataModel assignmentDataModel) {
        // Looking for locking the preconditions
        planningOutputDataModel.lock(assignmentDataModel);
        Vector<TaskPrecedenceConstraintDataModel> constraints = this.planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        for (int i = 0; i < constraints.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = constraints.get(i);
            if (constraint.getPostconditionTask().equals(assignmentDataModel.getTaskDataModel())) {
                AssignmentDataModel preconditionAssignment = this.planningOutputDataModel.getAssignmentForTask(constraint.getPreconditionTask());
                lockRecursive(preconditionAssignment);
            }
        }
    }

    public void unlockRecursive(AssignmentDataModel assignmentDataModel) {
        planningOutputDataModel.unlock(assignmentDataModel);
        Vector<TaskPrecedenceConstraintDataModel> constraints = this.planningInputDataModel.getTaskPrecedenceConstraintDataModelVector();
        for (int i = 0; i < constraints.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = constraints.get(i);
            if (constraint.getPreconditionTask().equals(assignmentDataModel.getTaskDataModel())) {
                AssignmentDataModel postconditionAssignment = this.planningOutputDataModel.getAssignmentForTask(constraint.getPostconditionTask());
                unlockRecursive(postconditionAssignment);
            }
        }
    }

    // The save method
    public void savePlanningOutputDataModel() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new XMLPlanningEntityResolver());
            // DocumentType dt = null;
            // dt =
            // db.getDOMImplementation().createDocumentType(getRootElement(),
            // null, this.getSystemDTD());
            // document = db.getDOMImplementation().createDocument(null,
            // getRootElement(), dt);

            Document document = db.getDOMImplementation().createDocument(null, "PLANNING_OUTPUT", null);
            Element xmlRootElement = document.getDocumentElement();

            Node xmlPlanningOutputNode = planningOutputDataModel.toXMLNode(document);

            xmlRootElement.appendChild(xmlPlanningOutputNode);

            aos.savePlanningOutputXMLDocument(document);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*************************************************************************************/
    /*****
     * END FUNCTIONS FROM ControllerToPlanningOutputDataModelInterface INTERFACE
     *****/
    /*************************************************************************************/

    private AssignmentDataModel getAssignmentForTask(TaskDataModel taskDataModel) {
        Vector<AssignmentDataModel> assignments = planningOutputDataModel.getAssignments();
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            if (assignment.getTaskDataModel().equals(taskDataModel)) {
                return assignment;
            }
        }
        return null;
    }

    // private TaskSuitableResourceDataModelVector
    // getSuitableResourcesForTask(TaskDataModel taskDataModel)
    // {
    // TaskSuitableResourceDataModelVector taskSuitableResourceDataModelVector =
    // planningInputDataModel.getTaskSuitableResourceDataModelVector();
    // TaskSuitableResourceDataModelVector suitableResources = new
    // TaskSuitableResourceDataModelVector();
    // for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++)
    // {
    // TaskSuitableResourceDataModel taskSuitableResource =
    // (TaskSuitableResourceDataModel)taskSuitableResourceDataModelVector.get(i);
    // if( taskSuitableResource.getTaskDataModel().equals(taskDataModel) )
    // {
    // suitableResources.add(taskSuitableResource.getResourceDataModel());
    // }
    // }
    // return suitableResources;
    // }

    /********************************************************************************/
    /*************************** HELPER PRIVATE FUNCTIONS ***************************/
    /********************************************************************************/

    private Calendar getNextNonWorkingDate(ResourceDataModel resource, Calendar date) {
        if (isResourceDown(resource, date)) {
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(resource, date);
            Calendar nextNonWorkingDateDueToForcedWorkingPeriods = getNextNonWorkingDateFromForcedWorkingDates(resource, date);
            if (nextNonWorkingDateDueToForcedWorkingPeriods!=null && (nextNonWorkingDateDueToForcedWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis())) {
                return nextNonWorkingDateDueToForcedWorkingPeriods;
            }
            // else
            {
                Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(resource, date);
                Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(resource, nextNonWorkingDateDueToNonWorkingPeriods);
                if(nextNonWorkingDateDueToForcedWorkingPeriods2!=null) {
                	return nextNonWorkingDateDueToForcedWorkingPeriods2;
                } else {
                	return nextNonWorkingDateDueToNonWorkingPeriods;
                }
            }
        }
        // else
        {
            if (isDateInsideTheResoureNonWorkingPeriods(date, resource)) {
                // Must check if the next day the resource will go down due to
                // forced working periods
                // is smallest from the day that the resource will be up due to
                // non workingperiod.
                // This means that the next date the resource will go down due
                // to forced working periods is allready up
                // so next non working period is the next time the resource will
                // be down also checking the exceptions
                Calendar nextNonWorkingDateDueToForcedWorkingPeriods = getNextNonWorkingDateFromForcedWorkingDates(resource, date);
                Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(resource, date);
                if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() > nextNonWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                    return nextNonWorkingDateDueToForcedWorkingPeriods;
                }
                // else
                {
                    Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(resource, date);
                    Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(resource, nextNonWorkingDateDueToNonWorkingPeriods);
                    return nextNonWorkingDateDueToForcedWorkingPeriods2;
                }
            }
            // else
            {
                Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(resource, date);
                if (nextNonWorkingDateDueToNonWorkingPeriods == null) {
                    return null;
                }
                if (isDateInsideTheResoureForcedWorkingPeriods(nextNonWorkingDateDueToNonWorkingPeriods, resource))// Checking
                                                                                                                   // if
                                                                                                                   // the
                                                                                                                   // next
                                                                                                                   // working
                                                                                                                   // date
                                                                                                                   // due
                                                                                                                   // to
                                                                                                                   // non
                                                                                                                   // working
                                                                                                                   // periods
                                                                                                                   // is
                                                                                                                   // inside
                                                                                                                   // a
                                                                                                                   // forced
                                                                                                                   // working
                                                                                                                   // period
                {
                    Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(resource, nextNonWorkingDateDueToNonWorkingPeriods);
                    if (nextNonWorkingDateDueToForcedWorkingPeriods2 == null) {
                        return nextNonWorkingDateDueToNonWorkingPeriods;
                    }
                    return nextNonWorkingDateDueToForcedWorkingPeriods2;
                }
                // else
                {
                    return nextNonWorkingDateDueToNonWorkingPeriods;
                }
            }
        }
    }

    private Calendar getNextNonWorkingDateFromNonWorkingDates(ResourceDataModel resource, Calendar date) {
        Calendar copyOfDateToCheck = (Calendar) date.clone();// this is done in
                                                             // order not to
                                                             // mess with the
                                                             // schedule date

        Calendar nextNonWorkingDate = null;

        int loopTimes = 0;
        while (isDateInsideTheResoureNonWorkingPeriods(copyOfDateToCheck, resource)) {
            // The current copyOfDateToCheck is inside non working period ...
            // So must check when this period ends and then find the next non
            // working copyOfDateToCheck
            ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
            Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
            for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
                TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
                // Checking if is actually non working period for this down time
                // period
                Calendar startPeriodDate = timePeriod.getFromDate();
                long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
                long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
                if (copyOfDateToCheck.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                    if (periodInMilliseconds != 0) {
                        long periodTimes = (copyOfDateToCheck.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                        if (startPeriodDate.getTimeInMillis() + (periodTimes * periodInMilliseconds) + durationInMilliseconds > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(startPeriodDate.getTimeInMillis() + (periodTimes * periodInMilliseconds) + durationInMilliseconds);
                        }
                    } else {
                        if (timePeriod.getToDate().getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(timePeriod.getToDate().getTimeInMillis());
                        }
                    }
                } else {
                    if (periodInMilliseconds != 0) {
                        // TODO: check if start period copyOfDateToCheck is after the copyOfDateToCheck assuming not and continue...
                        throw new RuntimeException("Start period copyOfDateToCheck is after the copyOfDateToCheck assuming not implemented yet");
                    }
                    // else
                    {
                        if (timePeriod.getToDate().getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(timePeriod.getToDate().getTimeInMillis());
                        }
                    }
                }
            }
            // TODO : Find a condition in order to end the loop if the resource is completely DOWN.
            System.out.println("LOOP : " + loopTimes);
        }

        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time
            // period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();

            if (copyOfDateToCheck.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextNonWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (copyOfDateToCheck.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;

                    periodTimes++; // Adding one more period makes the next non
                                   // working start copyOfDateToCheck

                    nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds;

                    Calendar temp = Calendar.getInstance();
                    temp.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                } else {
                    if (startPeriodDate.getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis();
                    }
                }

                if (nextNonWorkingDate == null) {
                    if (nextNonWorkingDateInMilliseconds != 0) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                } else {
                    // check for the smallest copyOfDateToCheck
                    if (nextNonWorkingDate.getTimeInMillis() > nextNonWorkingDateInMilliseconds) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds == 0) {
                    if (nextNonWorkingDate == null) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(startPeriodDate.getTimeInMillis());
                    } else {
                        // check for the smallest copyOfDateToCheck
                        if (nextNonWorkingDate.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                            nextNonWorkingDate = Calendar.getInstance();
                            nextNonWorkingDate.setTimeInMillis(startPeriodDate.getTimeInMillis());
                        }
                    }
                } else {
                    // TODO: check if start period copyOfDateToCheck is after the copyOfDateToCheck assuming not and continue...
                    throw new RuntimeException("Start period copyOfDateToCheck is after the copyOfDateToCheck assuming not implemented yet");
                }
            }
        }
        return nextNonWorkingDate;
    }

    private Calendar getNextNonWorkingDateFromForcedWorkingDates(ResourceDataModel resource, Calendar date) {
        Calendar nextNonWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time
            // period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextNonWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (date.getTimeInMillis() <= startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds) {
                        // Means we are inside a forced working period so the
                        // end might be the next non working date
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    } else {
                        // We are outside the non working period so we must add
                        // one period and then is the next period's end date
                        periodTimes++;
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    }
                } else {
                    if (date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        nextNonWorkingDateInMilliseconds = timePeriod.getToDate().getTimeInMillis();
                    }
                }
                // Make a check ...
                if (nextNonWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextNonWorkingDate == null) {
                    if (nextNonWorkingDateInMilliseconds != 0) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                } else
                // check for the smallest
                {
                    if (nextNonWorkingDate.getTimeInMillis() > nextNonWorkingDateInMilliseconds) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("NotImpmented Yet");
                }
                // else
                {
                    if (nextNonWorkingDate == null) {
                        nextNonWorkingDate = (Calendar) timePeriod.getToDate().clone();
                    } else
                    // check for the smallest
                    {
                        if (nextNonWorkingDate.getTimeInMillis() > timePeriod.getToDate().getTimeInMillis()) {
                            nextNonWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        }
                    }
                }
            }
        }
        return nextNonWorkingDate;
    }

    // NOTICE: the resource must be DOWN
    // If the resource is idle or busy then this time reflect the next time the
    // resource will be up from down
    protected Calendar getNextWorkingDate(ResourceDataModel resource, Calendar date) {
        if (isResourceDown(resource, date)) {
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(resource, date);
            Calendar nextWorkingDateDueToForcedWorkingPeriods = getNextWorkingDateFromForcedWorkingDates(resource, date);
            if (nextWorkingDateDueToNonWorkingPeriods == null) {
                return null;
            } else if (nextWorkingDateDueToForcedWorkingPeriods == null) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else {
                return nextWorkingDateDueToForcedWorkingPeriods;
            }
        }
        // else
        {
            // For this date calculate the next working date as before ...
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(resource, date);
            Calendar nextWorkingDateDueToForcedWorkingPeriods = getNextWorkingDateFromForcedWorkingDates(resource, date);

            if (nextWorkingDateDueToNonWorkingPeriods == null) {
                return null;
            } else if (nextWorkingDateDueToForcedWorkingPeriods == null) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else {
                return nextWorkingDateDueToForcedWorkingPeriods;
            }
        }
    }

    private Calendar getNextWorkingDateFromNonWorkingDates(ResourceDataModel resource, Calendar date) {
        Calendar nextWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time
            // period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    nextWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    while (nextWorkingDateInMilliseconds <= date.getTimeInMillis()) {
                        periodTimes++; // Adding one more period makes the NEXT
                                       // working start date
                        nextWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    }
                } else {
                    if (timePeriod.getToDate().getTimeInMillis() > date.getTimeInMillis()) {
                        nextWorkingDateInMilliseconds = timePeriod.getToDate().getTimeInMillis();
                    }
                }

                if (nextWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextWorkingDate == null) {
                    if (nextWorkingDateInMilliseconds != 0) {
                        nextWorkingDate = Calendar.getInstance();
                        nextWorkingDate.setTimeInMillis(nextWorkingDateInMilliseconds);
                    }
                } else if (nextWorkingDate.getTimeInMillis() > nextWorkingDateInMilliseconds) {
                    nextWorkingDate = Calendar.getInstance();
                    nextWorkingDate.setTimeInMillis(nextWorkingDateInMilliseconds);
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("NotImpmented Yet");
                }
                // else
                {
                    if (timePeriod.getToDate().getTimeInMillis() > date.getTimeInMillis()) {
                        if (nextWorkingDate == null) {
                            nextWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        } else if (nextWorkingDate.getTimeInMillis() > timePeriod.getToDate().getTimeInMillis()) {
                            nextWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        }
                    }
                }
            }
        }

        if (nextWorkingDate == null) {
            return null;
        }

        if (isDateInsideTheResoureNonWorkingPeriods(nextWorkingDate, resource)) {
            return getNextWorkingDateFromNonWorkingDates(resource, nextWorkingDate);
        }
        return nextWorkingDate;
    }

    private Calendar getNextWorkingDateFromForcedWorkingDates(ResourceDataModel resource, Calendar date) {
        Calendar nextForcedWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();

            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextForcedWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    periodTimes++; // Adding one more period makes the next
                                   // working start date
                    nextForcedWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds;
                } else {
                    if (startPeriodDate.getTimeInMillis() > date.getTimeInMillis()) {
                        nextForcedWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis();
                    }
                }
                if (nextForcedWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextForcedWorkingDate == null) {
                    if (nextForcedWorkingDateInMilliseconds != 0) {
                        nextForcedWorkingDate = Calendar.getInstance();
                        nextForcedWorkingDate.setTimeInMillis(nextForcedWorkingDateInMilliseconds);
                    }
                } else
                // check for the smallest date
                {
                    if (nextForcedWorkingDate.getTimeInMillis() > nextForcedWorkingDateInMilliseconds) {
                        nextForcedWorkingDate = Calendar.getInstance();
                        nextForcedWorkingDate.setTimeInMillis(nextForcedWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() > date.getTimeInMillis()) {
                        if (nextForcedWorkingDate == null) {
                            nextForcedWorkingDate = (Calendar) startPeriodDate.clone();
                        } else
                        // check for the smallest date
                        {
                            if (nextForcedWorkingDate.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                                nextForcedWorkingDate = (Calendar) startPeriodDate.clone();
                            }
                        }
                    }
                }
            }
        }
        return nextForcedWorkingDate;
    }

    // BE CAREFULL this function does not take into account the forced working
    // periods
    private boolean isDateInsideTheResoureNonWorkingPeriods(Calendar date, ResourceDataModel resource) {
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) <= date.getTimeInMillis() && date.getTimeInMillis() < startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) + durationInMilliseconds) {
                        return true;
                    }
                } else {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDateInsideTheResoureForcedWorkingPeriods(Calendar date, ResourceDataModel resource) {
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) <= date.getTimeInMillis() && date.getTimeInMillis() < startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) + durationInMilliseconds) {
                        return true;
                    }
                } else {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
