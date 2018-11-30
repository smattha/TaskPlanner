package lms.robopartner.mfg_planning_tool.impact.criteria;

import gr.upatras.lms.util.Convert;

import java.awt.Point;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.mfg_planning_tool.DemoPlanningGenerator3D;

import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * It expresses a value indicative of the percentage of the overall Floor Space that is occupied by the components.
 * This is done by accepting that the limits of the floor space that is occupied can be derived by the objects that are
 * placed the farthest
 * to the +x and -x axis and to the +y and -y axis
 * 
 * @author Jason
 *
 */
public class FloorSpace extends AbstractCriterion {

	private static org.slf4j.Logger	LOGGER			= LoggerFactory.getLogger(FloorSpace.class);
	private static final String		CRITERION_NAME	= "FLOOR SPACE";
	private Map<String, Point>		resourcesAndPartsMapingForSR;

	/**
	 * Hide constructor
	 */
	public FloorSpace() {}

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public FloorSpace(Map<String, Point> resourcesAndPartsMapingForSR) {
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {

		String msg = ".getValue(): ";
		int sr = paths.size();
		double floorSpaceSum = 0;
		double partialFloorSpace = 0;

		for ( TreeNode[] path : paths ) {
			for ( TreeNode treeNode : path ) {
				LayerNode node = (LayerNode) treeNode;
				for ( Assignment assignment : node.getNodeAssignments() ) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();

					resourcesAndPartsMapingForSR.isEmpty();

					// calculate
					double partialFloorSpaceTemp = 0;
					double minxFloor;
					double minyFloor;
					double maxxFloor;
					double maxyFloor;

					Point taskPoint = resourcesAndPartsMapingForSR.get(taskId);
					double xTask = taskPoint.getX();
					double yTask = taskPoint.getY();

					Point resourcePoint = resourcesAndPartsMapingForSR.get(resourceId);
					double xResource = resourcePoint.getX();
					double yResource = resourcePoint.getY();

					double widthTask = Convert.getDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
					double heightTask = Convert.getDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
					double widthResource = Convert.getDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
					double heightResource = Convert.getDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));

					minxFloor = ((xTask < xResource) ? xTask : xResource);
					minyFloor = ((yTask < yResource) ? yTask : yResource);
					maxxFloor = (((xTask + widthTask) > (xResource + widthResource)) ? (xTask + widthTask) : (xResource + widthResource));
					maxyFloor = (((yTask + heightTask) > (yResource + heightResource)) ? (yTask + heightTask) : (yResource + heightResource));

					partialFloorSpaceTemp = (((MapParameters.MAP_HEIGHT * MapParameters.MAP_WIDTH) - ((maxxFloor - minxFloor) * (maxyFloor - minyFloor))) / ((MapParameters.MAP_HEIGHT * MapParameters.MAP_WIDTH) - (DemoPlanningGenerator3D.MIN_RESOURCE_FLOOR_SPACE + DemoPlanningGenerator3D.MIN_FLOORSPACE_TASKS)));
					partialFloorSpace += partialFloorSpaceTemp;

					LOGGER.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + FloorSpace.CRITERION_NAME + "=" + partialFloorSpace);
				}
				partialFloorSpace = partialFloorSpace / path.length;
				floorSpaceSum += partialFloorSpace;
			}
		}
		floorSpaceSum = floorSpaceSum / (double) sr;
		LOGGER.trace(msg + FloorSpace.CRITERION_NAME + "=" + floorSpaceSum + ".");
		return floorSpaceSum;
	}

	@Override
	public double getWeight() {
		return 0.2;
	}

	@Override
	public boolean isBenefit() {
		return false;
	}

	@Override
	public String getCriterionName() {
		return FloorSpace.CRITERION_NAME;
	}

}
