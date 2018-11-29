/**
 * 
 */
package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.slf4j.LoggerFactory;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;
import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * @author user
 *
 */
public class Accessibility extends AbstractCriterion {

	private static final String CRITERION_NAME = "ACCESSIBILITY";
	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Accessibility.class);
	private FileWriter fileWriter;
	private int alternativeCounter = 0;

	/**
	 * 
	 */
	public Accessibility() {
		try {
			fileWriter = new FileWriter("C:\\Accessibility.doc");
		} catch (IOException e) {
			// LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = ".getValue(): ";
		// int sr = paths.size();
		Map<String, Shape> assignedTasks = new HashMap<String, Shape>();
		double accessibilitySum = 0d;
		// int srIndex = 0; //for logging purposes
		String alternative = "[ ";
		for (AssignmentDataModel anAssignment : helper.getAssignments()) {
			TaskDataModel taskDataModel = anAssignment.getTaskDataModel();
			ResourceDataModel resourceDataModel = anAssignment.getResourceDataModel();
			String taskId = anAssignment.getTaskDataModel().getTaskId();
			Shape taskRectangle = DataModelToAWTHelper.createShape(
					taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), taskDataModel,
					resourceDataModel);
			String angle = resourceDataModel.getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
			Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resourceDataModel);
			if (!angle.equals("180") && !angle.equals("360") && !angle.equals("0"))
				taskRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), taskRectangle,
						resourcePoint.x, resourcePoint.y,
						taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));

			assignedTasks.put(taskId, taskRectangle);
		}

		for (TreeNode[] path : paths) {
			// srIndex++;
			// LOGGER.trace("{} sample: {} of {}.", msg, srIndex, sr);
			Map<String, Shape> tasksToAssign = new HashMap<String, Shape>();
			Rectangle humanResourceRectangle = null;
			for (TreeNode treeNode : path) {
				LayerNode layerNode = (LayerNode) treeNode;
				for (Assignment assignment : layerNode.getNodeAssignments()) {
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();
					alternative += taskDataModel.getTaskId() + " X: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME) + " Y: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME);
					if (taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
							.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN))
						humanResourceRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
					else {
						Shape unassignedTaskRectangle = DataModelToAWTHelper.createShape(
								taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), taskDataModel,
								resourceDataModel);
						String angle = resourceDataModel.getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
						Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resourceDataModel);
						if (!angle.equals("180") && !angle.equals("360") && !angle.equals("0"))
							unassignedTaskRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle),
									unassignedTaskRectangle, resourcePoint.x, resourcePoint.y,
									taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
						tasksToAssign.put(taskDataModel.getTaskId(), unassignedTaskRectangle);
					}
				}
			}

			if (humanResourceRectangle != null) {
				for (Entry<String, Shape> checkinEntry : tasksToAssign.entrySet()) {
					Shape checkingRectangle = checkinEntry.getValue();
					for (Entry<String, Shape> checkingWithEntry : tasksToAssign.entrySet()) {
						Shape checkingWithRectangle = checkingWithEntry.getValue();
						if (checkingRectangle.getBounds().x > checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y > checkingWithRectangle.getBounds().y) {
							if ((checkingWithRectangle.getBounds().x
									+ checkingWithRectangle.getBounds().width) < checkingRectangle.getBounds().x
									&& (checkingWithRectangle.getBounds().y
											+ checkingWithRectangle.getBounds().height < checkingRectangle
													.getBounds().y))
								accessibilitySum += (humanResourceRectangle.width < (checkingRectangle.getBounds().x
										- (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if ((checkingWithRectangle.getBounds().x
									+ checkingWithRectangle.getBounds().width) >= checkingRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.height < (checkingRectangle.getBounds().y
										- (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x > checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y <= checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().y >= (checkingWithRectangle.getBounds().y
									+ checkingWithRectangle.getBounds().height))
								accessibilitySum += (humanResourceRectangle.width < (checkingRectangle.getBounds().x
										- (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().y < (checkingWithRectangle.getBounds().y
									+ checkingWithRectangle.getBounds().height))
								accessibilitySum += (humanResourceRectangle.height < (-checkingRectangle.getBounds().y
										+ (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x <= checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y > checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().x
									+ checkingRectangle.getBounds().height > checkingWithRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.width < (-checkingRectangle.getBounds().x
										+ (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().x
									+ checkingRectangle.getBounds().height <= checkingWithRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.height < (checkingRectangle.getBounds().y
										- (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x <= checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y <= checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().y
									+ checkingRectangle.getBounds().width > checkingWithRectangle.getBounds().y)
								accessibilitySum += (humanResourceRectangle.width < (-checkingRectangle.getBounds().x
										+ (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().y
									+ checkingRectangle.getBounds().width <= checkingWithRectangle.getBounds().y)
								accessibilitySum += (humanResourceRectangle.height < (-checkingRectangle.getBounds().y
										+ (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						}
					}
				}

				for (Entry<String, Shape> checkinEntry : assignedTasks.entrySet()) {
					Shape checkingRectangle = checkinEntry.getValue();
					for (Entry<String, Shape> checkingWithEntry : tasksToAssign.entrySet()) {
						Shape checkingWithRectangle = checkingWithEntry.getValue();
						if (checkingRectangle.getBounds().x > checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y > checkingWithRectangle.getBounds().y) {
							if ((checkingWithRectangle.getBounds().x
									+ checkingWithRectangle.getBounds().width) < checkingRectangle.getBounds().x
									&& (checkingWithRectangle.getBounds().y
											+ checkingWithRectangle.getBounds().height < checkingRectangle
													.getBounds().y))
								accessibilitySum += (humanResourceRectangle.width < (checkingRectangle.getBounds().x
										- (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if ((checkingWithRectangle.getBounds().x
									+ checkingWithRectangle.getBounds().width) >= checkingRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.height < (checkingRectangle.getBounds().y
										- (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x > checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y <= checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().y >= (checkingWithRectangle.getBounds().y
									+ checkingWithRectangle.getBounds().height))
								accessibilitySum += (humanResourceRectangle.width < (checkingRectangle.getBounds().x
										- (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().y < (checkingWithRectangle.getBounds().y
									+ checkingWithRectangle.getBounds().height))
								accessibilitySum += (humanResourceRectangle.height < (-checkingRectangle.getBounds().y
										+ (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x <= checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y > checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().x
									+ checkingRectangle.getBounds().height > checkingWithRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.width < (-checkingRectangle.getBounds().x
										+ (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().x
									+ checkingRectangle.getBounds().height <= checkingWithRectangle.getBounds().x)
								accessibilitySum += (humanResourceRectangle.height < (checkingRectangle.getBounds().y
										- (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						} else if (checkingRectangle.getBounds().x <= checkingWithRectangle.getBounds().x
								&& checkingRectangle.getBounds().y <= checkingWithRectangle.getBounds().y) {
							if (checkingRectangle.getBounds().y
									+ checkingRectangle.getBounds().width > checkingWithRectangle.getBounds().y)
								accessibilitySum += (humanResourceRectangle.width < (-checkingRectangle.getBounds().x
										+ (checkingWithRectangle.getBounds().x
												+ checkingWithRectangle.getBounds().width))) ? 1 : 0;
							else if (checkingRectangle.getBounds().y
									+ checkingRectangle.getBounds().width <= checkingWithRectangle.getBounds().y)
								accessibilitySum += (humanResourceRectangle.height < (-checkingRectangle.getBounds().y
										+ (checkingWithRectangle.getBounds().y
												+ checkingWithRectangle.getBounds().height))) ? 1 : 0;
						}
					}
				}
			} else
				accessibilitySum = paths.size();
		}
		LOGGER.trace("{} the result of the criterion {} is: {}", msg, CRITERION_NAME, accessibilitySum);
		return accessibilitySum / paths.size();
	}

	@Override
	public double getWeight() {
		return 0.17;
	}

	@Override
	public boolean isBenefit() {
		return true;
	}

	@Override
	public String getCriterionName() {
		return CRITERION_NAME;
	}

}
