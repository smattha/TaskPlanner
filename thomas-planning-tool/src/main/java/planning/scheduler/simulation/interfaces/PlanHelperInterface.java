package planning.scheduler.simulation.interfaces;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AbstractDataModel;
import planning.model.AssignmentDataModel;
import planning.model.JobDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.ToolDataModel;
import planning.model.WorkcenterDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

public interface PlanHelperInterface {
	public boolean isTaskSuitableForResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator);// Checks
																												// for
																												// suitability

	public boolean areAnyPendingPresendenceConstraintsForTaskOnResource(ResourceSimulator resourceSimulator,
			TaskSimulator taskSimulator, Calendar timeNow);// Checks for constrains

	public Vector<TaskPrecedenceConstraintDataModel> getTaskPendingPresendenceConstraints(TaskSimulator taskSimulator,
			Calendar timeNow);// Returns the task constrains

	public boolean canResourceFullfilTheTaskUniterrupted(ResourceSimulator resourceSimulator,
			TaskSimulator taskSimulator, Calendar time);// Checks for locked tasks

	public long getOperationTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator,
			ResourceSimulator resourceSimulator, Calendar timeNow, Vector<Assignment> currentLevelAssignments);// Returns
																												// the
																												// pure
																												// operation
																												// time
																												// of
																												// this
																												// assignment

	public String getSetUpCodeForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator);// Returns
																													// the
																													// set
																													// up
																													// code
																													// of
																													// this
																													// assignment

	public long getSetUpTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator,
			ResourceSimulator resourceSimulator, Calendar timeNow, Vector<Assignment> currentLevelAssignments);// Returns
																												// the
																												// set
																												// up
																												// time
																												// of
																												// this
																												// assignment

	@Deprecated
	public long getSetUpTimeInMillisecondsFromSetUpCodeToSetUpCodeOnResource(String fromSetUpCode, String toSetUpCode,
			ResourceSimulator resourceSimulator);// Returns the set up time of this assignment

	public long getResourceDownTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator,
			ResourceSimulator resourceSimulator, Calendar timeNow, Vector<Assignment> currentLevelAssignments);// Returns
																												// the
																												// down
																												// time
																												// of
																												// the
																												// resource
																												// during
																												// the
																												// period
																												// of
																												// execution
																												// of
																												// this
																												// assignment
																												// (operation
																												// time
																												// + set
																												// up
																												// time)

	public Vector<ResourceSimulator> getSuitableResourcesSimulatorForTask(TaskSimulator taskSimulator);// Returns all
																										// suitable
																										// resources for
																										// a given task

	public Vector<TaskSimulator> getSuitableTasksSimulatorForResource(ResourceSimulator resourceSimulator);// Returns
																											// all
																											// suitable
																											// tasks for
																											// a given
																											// resource

	public Vector<TaskSuitableResourceDataModel> getAllSuitabilities();// Returns all suitabilities

	public ManualPlanHelperInterface getManualPlanningHelperInterface();

	public WorkcenterDataModel locateResource(ResourceDataModel resourceDataModel);

	public AbstractDataModel locateTool(ToolDataModel toolDataModel);

	public JobDataModel getJobDataModelForTaskSimulator(TaskSimulator taskSimulator);

	public AssignmentDataModel getAssignmentOfTaskDataModel(TaskDataModel endEffectorSelection);

	public Vector<WorkcenterDataModel> getAllWorkcentrers();

	public Vector<AssignmentDataModel> getAssignments();

	public Vector<JobDataModel> getJobs();

	public Vector<TaskPrecedenceConstraintDataModel> getTaskPrecedenceConstraints();

	public TaskSimulator getTaskSimulatorFromTaskDataModel(TaskDataModel taskDataModel);

	public ResourceSimulator getResourceSimulatorFromResourceDataModel(ResourceDataModel resourceDataModel);
}
