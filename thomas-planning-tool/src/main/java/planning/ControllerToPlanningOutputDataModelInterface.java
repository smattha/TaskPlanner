package planning;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;

/**
 * The plannning output manipulation interface
 */

public interface ControllerToPlanningOutputDataModelInterface {
    public void addAssignmentDataModel(AssignmentDataModel newAssignment);

    public void removeAssignmentDataModel(AssignmentDataModel assignment);

    public void setTaskDataModelToAssignmentDataModel(TaskDataModel task, AssignmentDataModel assignment);

    public void setResourceDataModelToAssignmentDataModel(ResourceDataModel resource, AssignmentDataModel assignment);

    public void setDurationInMillisecondsToAssignmentDataModel(long newDurationInMilliseconds, AssignmentDataModel assignment);

    public void setTimeOfDispatchToAssignmentDataModel(Calendar newTimeOfDispatch, AssignmentDataModel assignment);

    // Methods implementing the constrains as well as helper methods
    public void lockRecursive(AssignmentDataModel assignmentDataModel);

    public void unlockRecursive(AssignmentDataModel assignmentDataModel);

    public boolean isTaskSuitableForResource(TaskDataModel task, ResourceDataModel resource);// Checks for suitability

    public boolean hasTaskPendingPresendenceConstraints(TaskDataModel task, Calendar timeNow, long durationInMilliseconds);// Checks for constrains

    public boolean canResourceFullfilTheTaskUniterrupted(ResourceDataModel resource, TaskDataModel task, Calendar timeOfHypotheticalDispatch);// Also checks for locked tasks

    public long getOperationTimeInMillisecondsForTaskOnResource(TaskDataModel task, ResourceDataModel resource);// Returns the pure operation time of this assignment

    public String getSetUpCodeForTaskOnResource(TaskDataModel task, ResourceDataModel resource);// Returns the set up code of this potential assignment

    public long getSetUpTimeInMillisecondsForTaskOnResource(TaskDataModel task, ResourceDataModel resource, Calendar currentTime);// Returns the set up time of this assignment

    @Deprecated
    public long getSetUpTimeInMillisecondsFromSetUpCodeToSetUpCodeOnResource(String fromSetUpCode, String toSetUpCode, ResourceDataModel resource);// Returns the set up time of this assignment

    public long getResourceDownTimeInMillisecondsForTaskOnResource(TaskDataModel task, ResourceDataModel resource, Calendar timeNow);// Returns the down time of the resource during the period of execution of this assignment (operation time + set up time)

    public void reschedule(Calendar startTime, Vector<AssignmentDataModel> lockedAssignmnets);// The reschedule method

    public void savePlanningOutputDataModel();// The save method
}
