package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * Evaluates the Fatigue of the operator in executing a specific task based on
 * the equations in the job rotation paper of LMS.
 * 
 * The //TODO commented lines should be enabled when the code is to run with the
 * layout that takes into consideration the z axis as well
 * 
 * @author Jason
 *
 */
public class Fatigue extends AbstractCriterion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Fatigue.class);
	private static final String CRITERION_NAME = "FATIGUE";
	private Map<String, Point3D> resourcesAndPartsMapingForSR;

	/**
	 * Hide constructor
	 */
	@SuppressWarnings("unused")
	private Fatigue() {
		super();
	}

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public Fatigue(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		super();
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {

		String msg = ".getValue(): ";
		// Each path is a solution. There are SR paths provided each time.
		int sr = paths.size();
		double fatigueSum = 0;
		double partialFatigue = 0;

		for (int i = 0; i < sr; i++) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);

			// path is solution
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				for (Assignment assignment : node.getNodeAssignments()) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();

					// calculate
					boolean missingTask = !resourcesAndPartsMapingForSR.containsKey(taskId);
					boolean missingResource = !resourcesAndPartsMapingForSR.containsKey(resourceId);
					if (missingTask) {
						String msg2 = msg + "No task mapping found for task with id=" + taskId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}
					if (missingResource) {
						String msg2 = msg + "No resource mapping found for resource with id=" + resourceId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}
					double partialFatigueTemp = 0;
					if (missingTask || missingResource) {
//                        // If a resource or task is missing, this solution is
//                        // totally bad.
//                        partialAreaCost = Double.MAX_VALUE;
						logger.debug(msg + "Missing Task Or Resource");
					} else {

						if (assignment.getResource().getResourceDataModel()
								.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
								.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
							double centerXTask;
							double centerYTask;
							double centerXResource;
							double centerYResource;

							Point3D taskPoint = resourcesAndPartsMapingForSR.get(taskId);
							double xTask = taskPoint.getX();
							double yTask = taskPoint.getY();
							// TODO the below commented line(s) are the 3D criteria changes
//                      double zTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));

							Point3D resourcePoint = resourcesAndPartsMapingForSR.get(resourceId);
							double xResource = resourcePoint.getX();
							double yResource = resourcePoint.getY();
							// TODO the below commented line(s) are the 3D criteria changes
//                      double zResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));

							double widthTask = Double.parseDouble(assignment.getTask().getTaskDataModel()
									.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
							double heightTask = Double.parseDouble(assignment.getTask().getTaskDataModel()
									.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
							double widthResource = Double.parseDouble(assignment.getResource().getResourceDataModel()
									.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
							double heightResource = Double.parseDouble(assignment.getResource().getResourceDataModel()
									.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));

							centerXTask = xTask + (widthTask / 2);
							centerYTask = yTask + (heightTask / 2);
							centerXResource = xResource + (widthResource / 2);
							centerYResource = yResource + (heightResource / 2);

							double distance = Math.sqrt(Math.pow((centerXResource - centerXTask), 2)
									+ Math.pow((centerYResource - centerYTask), 2));
							// TODO the below commented line(s) are the 3D criteria changes
//                          double distance = Math.sqrt(Math.pow((centerXResource-centerXTask), 2)+Math.pow((centerYResource-centerYTask), 2)+Math.pow((zResource-zTask), 2));

							double timeToCompleteMovement = distance / (Double.parseDouble(
									assignment.getResource().getResourceDataModel().getProperty("Speed (mm/s)")) / 1);

							/////////////////////////////////////////////////////////// IF CORRECT SPEED
							/////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////////////
							// double timeToCompleteMovement =
							/////////////////////////////////////////////////////////// distance/(Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty("Speed
							/////////////////////////////////////////////////////////// (mm/s)"))/1000);
							//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							double Fatigue = 0.5 * Math.exp(2 * (Double
									.parseDouble(assignment.getTask().getTaskDataModel().getProperty("Weight (Kg)"))
									* 9.81 * timeToCompleteMovement / 300));

							double maxFatigue = 0.5 * Math.exp(2 * (Double.parseDouble(
									assignment.getResource().getResourceDataModel().getProperty("Payload (kg)")) * 9.81
									* timeToCompleteMovement / 300));

							partialFatigueTemp = ((maxFatigue - Fatigue) / (maxFatigue - 0));
						} else {
							partialFatigueTemp = 1;
						}

					}
					partialFatigue += partialFatigueTemp;

					logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + Fatigue.CRITERION_NAME + "="
							+ partialFatigue);
				}
				// partialFatigue = partialFatigue/path.length;
				fatigueSum += partialFatigue;
			}
		}
		fatigueSum = fatigueSum / (double) sr;
		logger.trace(msg + Fatigue.CRITERION_NAME + "=" + fatigueSum + ".");
		return fatigueSum;
	}

	@Override
	public double getWeight() {
		return 1;
	}

	@Override
	public boolean isBenefit() {
		return true;
	}

	@Override
	public String getCriterionName() {
		return Fatigue.CRITERION_NAME;
	}

}
