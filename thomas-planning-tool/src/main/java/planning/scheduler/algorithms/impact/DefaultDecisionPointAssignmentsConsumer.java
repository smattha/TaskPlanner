package planning.scheduler.algorithms.impact;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.interfaces.DecisionPointAssignmentsConsumerInterface;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class DefaultDecisionPointAssignmentsConsumer implements DecisionPointAssignmentsConsumerInterface {

	@Override
	public Vector<AssignmentDataModel> consumeDecisionPointAssignments(Calendar timeNow, PlanHelperInterface helper,
			Vector<Assignment> decisionPointAssignments, double[] criteriaValues) {
		Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();
		for (int i = 0; i < decisionPointAssignments.size(); i++) {
			Assignment layerAssignment = decisionPointAssignments.get(i);
			TaskSimulator taskSimulator = layerAssignment.getTask();
			ResourceSimulator resourceSimulator = layerAssignment.getResource();

			// Creating the assignment

			AssignmentDataModel assignment = new AssignmentDataModel(taskSimulator.getTaskDataModel(),
					resourceSimulator.getResourceDataModel(), timeNow,
					helper.getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow,
							decisionPointAssignments)
							+ helper.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator,
									timeNow, decisionPointAssignments)
							+ helper.getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator,
									resourceSimulator, timeNow, decisionPointAssignments),
					false, null);
			assignments.add(assignment);
		}
		return assignments;
	}
}
