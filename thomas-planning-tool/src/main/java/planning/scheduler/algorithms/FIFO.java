package planning.scheduler.algorithms;

import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class FIFO extends AbstractAlgorithm {
	protected FIFO() {
	}

	public Vector<AssignmentDataModel> solve(Vector<TaskSimulator> pendingTaskSimulators,
			Vector<ResourceSimulator> idleResourceSimulators, Calendar timeNow, PlanHelperInterface helper) {
		// Sort the pendingTaskSimulators vector with the first arrival date
		// Copying it into an array and sorting the array
		// TODO: Better sorting algorithm
		TaskSimulator[] pendingTaskSimulatorArray = null;
		int pendingTaskSimulatorSize = pendingTaskSimulators.size();
		if (pendingTaskSimulatorSize > 0) {
			pendingTaskSimulatorArray = new TaskSimulator[pendingTaskSimulatorSize];
			pendingTaskSimulators.copyInto(pendingTaskSimulatorArray);
			for (int i = pendingTaskSimulatorArray.length; --i >= 0;) {
				boolean flipped = false;
				for (int j = 0; j < i; j++) {
					if (pendingTaskSimulatorArray[j].getArrivalDate()
							.getTimeInMillis() > pendingTaskSimulatorArray[j + 1].getArrivalDate().getTimeInMillis()) {
						TaskSimulator temp = pendingTaskSimulatorArray[j];
						pendingTaskSimulatorArray[j] = pendingTaskSimulatorArray[j + 1];
						pendingTaskSimulatorArray[j + 1] = temp;
						flipped = true;
					}
				}
				if (!flipped) {
					continue;
				}
			}
		}

		Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();

		if (pendingTaskSimulatorArray != null) {
			for (int i = 0; i < pendingTaskSimulatorArray.length; i++) {
				TaskSimulator taskSimulator = pendingTaskSimulatorArray[i];
				Vector<ResourceSimulator> suitableResources = helper
						.getSuitableResourcesSimulatorForTask(taskSimulator);
				// Loop to get rid of the unwanted resources
				Vector<ResourceSimulator> potentialSuitableResourceSimulators = new Vector<ResourceSimulator>();
				for (int j = 0; j < suitableResources.size(); j++) {
					ResourceSimulator resourceSimulator = suitableResources.get(j);
					// Checks if the suitable resource is idle, is currently available and if it's
					// lock constrains are fulfilled
					if ((resourceSimulator.getResourceState().equals(ResourceSimulator.IDLE))
							&& (idleResourceSimulators.indexOf(resourceSimulator) != -1)
							&& (helper.canResourceFullfilTheTaskUniterrupted(resourceSimulator, taskSimulator,
									timeNow))) {
						potentialSuitableResourceSimulators.add(resourceSimulator);
					}
				}
				// Shuffle the idle resources vector to choose one randomly
				// TODO: Maybe implement another algorithm to select a idle resource
				Collections.shuffle(potentialSuitableResourceSimulators);
				// Make an assignment if a resource is available and the task has fulfilled its
				// precedence constrains
				for (ResourceSimulator resourceSimulator : potentialSuitableResourceSimulators) {
					if (!helper.areAnyPendingPresendenceConstraintsForTaskOnResource(resourceSimulator, taskSimulator,
							timeNow)) {
						// Creating the assignment
						AssignmentDataModel assignment = new AssignmentDataModel(taskSimulator.getTaskDataModel(),
								resourceSimulator.getResourceDataModel(), timeNow,
								helper.getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator,
										timeNow, null)
										+ helper.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator,
												resourceSimulator, timeNow, null)
										+ helper.getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator,
												resourceSimulator, timeNow, null),
								false, null);
						// Adding it to the assignment vector for return it
						assignments.add(assignment);
						// Remove the resource from the available resources vector (not available any
						// more an assignment has been made)
						idleResourceSimulators.remove(resourceSimulator);
					}
					break;
				}
			}
		}
		return assignments;
	}
}
