package planning;

import java.util.Calendar;

import planning.model.JobDataModel;
import planning.model.ResourceAvailabilityDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.WorkcenterDataModel;

/**
 * The plannning input manipulation interface
 */

public interface ControllerToPlanningInputDataModelInterface
{
    public Calendar getPlanStartDate();
    public void setPlanStartDate(Calendar newPlanStartDate);
    public Calendar getPlanEndDate();
    public void setPlanEndDate(Calendar newPlanEndDate);
	public void setContinueAssignmentsAfterPlanEndDate(boolean continueAssignments);
    /**** Manipulating facility ****/
    // Manipulating workcenters
    public void addWorkcenterDataModel(WorkcenterDataModel newWorkcenter);
    public void removeWorkcenterDataModel(WorkcenterDataModel workcenter);
    public void setNameToWorkcenterDataModel(String newName, WorkcenterDataModel workcenter);
    public void setAlgorithmToWorkcenterDataModel(String algorithm, WorkcenterDataModel workcenter);
    // Manipulating resources
    public void addResourceDataModelToWorkcenter(ResourceDataModel newResource, WorkcenterDataModel workcenter);
    public void removeResourceDataModelToWorkcenter(ResourceDataModel resource);
    public void setResourceAvailabilityDataModelToResourceDataModelName(ResourceAvailabilityDataModel newResourceAvailabilityDataModel, ResourceDataModel resource);
    public void setNameToResourceDataModel(String newName, ResourceDataModel resource);
    // Manipulating suitable resources
    public void addTaskSuitableResourceDataModel(TaskSuitableResourceDataModel newSuitable);
    public void removeTaskSuitableResourceDataModel(TaskSuitableResourceDataModel suitable);
    public void setTaskDataModelToTaskSuitableResourceDataModel(TaskDataModel task, TaskSuitableResourceDataModel taskSuitableResourceDataModel);
    public void setResourceDataModelToTaskSuitableResourceDataModel(ResourceDataModel resource, TaskSuitableResourceDataModel taskSuitableResourceDataModel);
    public void setOperationTimeInMillisecondsToTaskSuitableResourceDataModel(long operationTimeInMilliseconds, TaskSuitableResourceDataModel taskSuitableResourceDataModel);
    public void setSetUpCodeToTaskSuitableResourceDataModel(String newSetUpCode, TaskSuitableResourceDataModel taskSuitableResourceDataModel);

    /**** Manipulating workload ****/
    // Manipulating jobs
    public void addJobDataModel(JobDataModel newJob);
    public void removeJobDataModel(JobDataModel job);
    public void setNameToJobDataModel(String newName, JobDataModel jobDataModel);
    public void setArrivalDateToJobDataModel(Calendar newArrivalCalendarDate, JobDataModel jobDataModel);
    public void setDueDateToJobDataModel(Calendar newDueCalendarDate, JobDataModel jobDataModel);
    // Manipulating tasks
    public void addTaskDataModelToJob(TaskDataModel newTask, JobDataModel job);
    public void removeTaskDataModel(TaskDataModel task);
    public void setNameToTaskDataModel(String newName, TaskDataModel jobDataModel);
    // Manipulating precendence constraints
    public void addTaskPrecedenceConstraintDataModel(TaskPrecedenceConstraintDataModel newConstraint);
    public void removeTaskPrecedenceConstraintDataModel(TaskPrecedenceConstraintDataModel constraint);
    public void setPreconditionTaskToTaskPrecedenceConstraintDataModel(TaskDataModel task, TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel);
    public void setPostconditionTaskToTaskPrecedenceConstraintDataModel(TaskDataModel task, TaskPrecedenceConstraintDataModel taskPrecedenceConstraintDataModel);
    public boolean isResourceDown( ResourceDataModel resourceDataModel, Calendar currentTime);
    // The save method
    public void savePlanningInputDataModel();
}
