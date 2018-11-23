package planning;

import java.util.Calendar;
import java.util.Vector;

import planning.model.JobDataModel;
import planning.model.ResourceDataModel;
import planning.model.SubjectDataModelInterface;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.WorkcenterDataModel;

// This interfaced is implemented by the data models and is used by the the views
public interface ViewToPlanningInputDataModelInterface extends SubjectDataModelInterface
{
    public Vector<ResourceDataModel> getResourceDataModelVector();
    public Vector<TaskDataModel> getTaskDataModelVector();
    public Vector<JobDataModel> getJobDataModelVector();
    public Vector<WorkcenterDataModel> getWorkcenterDataModelVector();
    public Vector<TaskSuitableResourceDataModel> getTaskSuitableResourceDataModelVector();
    public Vector<TaskPrecedenceConstraintDataModel> getTaskPrecedenceConstraintDataModelVector();
    public Calendar getPlanStartDate();
    public Calendar getPlanEndDate();
    public JobDataModel getJobDataModelForTaskDataModel(TaskDataModel taskDataModel);
    // Common functions for input and output models
    public TaskDataModel getTaskDataModel(String taskId);
    public ResourceDataModel getResourceDataModel(String resourceId);
}
