package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.TaskSuitableResourceDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.interfaces.OperationTimeCalculatorInterface;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class DefaultOperationTimeCalculator implements OperationTimeCalculatorInterface {

	public long getOperationTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator,
			ResourceSimulator resourceSimulator, Calendar timeNow, PlanHelperInterface helperInterface,
			Vector<Assignment> assignments) {
		long time = Long.MIN_VALUE;
		Vector<TaskSuitableResourceDataModel> taskSuitableResourceDataModelVector = helperInterface
				.getAllSuitabilities();
		for (int i = 0; i < taskSuitableResourceDataModelVector.size(); i++) {
			TaskSuitableResourceDataModel taskSuitableResource = (TaskSuitableResourceDataModel) taskSuitableResourceDataModelVector
					.get(i);
			if (taskSuitableResource.getTaskDataModel().equals(taskSimulator.getTaskDataModel())
					&& taskSuitableResource.getResourceDataModel().equals(resourceSimulator.getResourceDataModel())) {
				time = taskSuitableResource.getOperationTimeInMilliseconds();
				break;
			}
		}
		if (time == Long.MIN_VALUE) {
			System.err.println("Warning: Task '" + taskSimulator.getTaskDataModel().getTaskName()
					+ "' has not operation time set. Using 1ms");
			time = 1;
		}
		return time;
	}

}
