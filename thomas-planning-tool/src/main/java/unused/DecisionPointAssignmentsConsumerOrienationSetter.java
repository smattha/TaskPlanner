/**
 * 
 */
package unused;

import java.util.Calendar;
import java.util.Vector;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.interfaces.DecisionPointAssignmentsConsumerInterface;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * @author Spyros Check and decide orientation of task
 */
public class DecisionPointAssignmentsConsumerOrienationSetter implements DecisionPointAssignmentsConsumerInterface {
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(DecisionPointAssignmentsConsumerOrienationSetter.class);

	/**
	 * 
	 */
	public DecisionPointAssignmentsConsumerOrienationSetter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see planning.scheduler.algorithms.impact.interfaces.
	 * DecisionPointAssignmentsConsumerInterface
	 * #consumeDecisionPointAssignments(java.util.Calendar,
	 * planning.scheduler.simulation.interfaces.PlanHelperInterface,
	 * java.util.Vector, double[])
	 */
	public Vector<AssignmentDataModel> consumeDecisionPointAssignments(Calendar timeNow, PlanHelperInterface helper,
			Vector<Assignment> decisionPointAssignments, double[] criteriaValues) {
		String msg = ".consumeDecisionPointAssignments(): ";
		Vector<AssignmentDataModel> theResultsAssignmentsVector = new Vector<AssignmentDataModel>();

		for (Assignment layerAssignment : decisionPointAssignments) {
//TODO
//	    Rectangle theZeroDegreeRectangle = DataModelToAWTHelper
//		    .createRectangle(layerAssignment.getResource(),
//			    layerAssignment.getTask());
//	    Dimension theNinetyDegreeDimension = new Dimension(
//		    theZeroDegreeRectangle.height, theZeroDegreeRectangle.width);// translated
//										 // dimensions.
//	    Rectangle theNinetyDegreeRectangle = new Rectangle(
//		    theZeroDegreeRectangle.getLocation(),
//		    theNinetyDegreeDimension);
//	    Vector<String> theOrientationPropertyValue = new Vector<String>();
//	    if (LayoutEvaluator.isSuitabilityValid(theZeroDegreeRectangle,
//		    helper.getAssignments())) {
//		theOrientationPropertyValue
//			.add(MapToResourcesAndTasks.ORIENTATION_ZERO_DEGREES_VALUE);
//	    }
//	    if (LayoutEvaluator.isSuitabilityValid(theNinetyDegreeRectangle,
//		    helper.getAssignments())) {
//		theOrientationPropertyValue
//			.add(MapToResourcesAndTasks.ORIENTATION_NINETY_DEGREES_VALUE);
//	    }

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
			// assignment.getTaskDataModel().setProperty(MapToResourcesAndTasks.ORIENTATION_PROPERTY_NAME).g;
			// TODO assign a random value of orientation if both orientations
			// are available currently not used
			theResultsAssignmentsVector.add(assignment);
		}

		logger.trace(msg + "decisionPointAssignements.size():" + decisionPointAssignments.size());
		return theResultsAssignmentsVector;
	}

}
