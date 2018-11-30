package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
//import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class Reachability extends AbstractCriterion {

	private PLANNINGINPUT	aPlanninginput;
	private FileWriter		fileWriter;
	private int				alternativeCounter	= 0;

	public Reachability() {}

	public Reachability(PLANNINGINPUT aPlanninginput) {
		this.aPlanninginput = aPlanninginput;
		try {
			fileWriter = new FileWriter("C:\\Reachability.doc");
		}
		catch ( IOException e ) {
			//LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private static org.slf4j.Logger	LOGGER			= LoggerFactory.getLogger(Reachability.class);
	private static final String		CRITERION_NAME	= "REACHABILITY";

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = "getValue(): ";
		double result = 0;
		double reachabilitySum = 0;
		String alternative = "[ ";
		for ( TreeNode[] path : paths ) {
			Map<String, String> activeResourceReachability = new HashMap<String, String>();
			HashMap<String, Shape> activeResourceRectangles = new HashMap<String, Shape>();
			HashMap<String, Shape> passiveResourceRectangles = new HashMap<String, Shape>();
			for ( TreeNode treeNode : path ) {
				LayerNode layerNode = (LayerNode) treeNode;
				for ( Assignment assignment : layerNode.getNodeAssignments() ) {
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();
					alternative += taskDataModel.getTaskId() + " X: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME) + " Y: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME);
					if ( taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK) ) {
						Shape rectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
						String angle = resourceDataModel.getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
						Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resourceDataModel);
						if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
							rectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), rectangle, resourcePoint.x, resourcePoint.y, taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
						passiveResourceRectangles.put(taskDataModel.getTaskId(), rectangle);
					}
				}
			}

			for ( AssignmentDataModel anAssignment : helper.getAssignments() ) {
				TaskDataModel taskDataModel = anAssignment.getTaskDataModel();
				ResourceDataModel resourceDataModel = anAssignment.getResourceDataModel();
				if ( (taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT) || taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN))
						&& !activeResourceRectangles.containsKey(taskDataModel.getTaskId()) ) {
					String angle = resourceDataModel.getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
					Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resourceDataModel);
					Shape rectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
					if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
						rectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), rectangle, resourcePoint.x, resourcePoint.y, taskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
					// LOGGER.debug(msg + "{}", taskDataModel.getTaskId());
					activeResourceRectangles.put(taskDataModel.getTaskId(), rectangle);
					activeResourceReachability.put(taskDataModel.getTaskId(), taskDataModel.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME));
				}
			}

			if ( !activeResourceRectangles.isEmpty() ) {
				String resourceId = "";
				for ( Map.Entry<String, Shape> passiveResourceEntry : passiveResourceRectangles.entrySet() ) {
					for ( int i = 0; i < aPlanninginput.getTASKS().getTASK().size(); i++ ) {
						if ( passiveResourceEntry.getKey().equals(aPlanninginput.getTASKS().getTASK().get(i).getId()) )
							resourceId = aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE().get(i).getRESOURCEREFERENCE().getRefid();
					}
					if ( activeResourceRectangles.containsKey(resourceId) ) {
						Shape activeResource = activeResourceRectangles.get(resourceId);
						Shape passiveResource = passiveResourceEntry.getValue();
						int reachability = Integer.parseInt(activeResourceReachability.get(resourceId));
						Dimension aDimension = activeResource.getBounds().getSize();
						Point aPoint = activeResource.getBounds().getLocation();
						// LOGGER.debug(msg + "init_x: {} init_y: ", aPoint.x, aPoint.y);
						int resourceReachabilityWidthDif = reachability - aDimension.width;
						int resourceReachabilityHeigntDif = reachability - aDimension.height;
						Dimension reachabilityDimension = new Dimension(2 * reachability + aDimension.width, 2 * reachability + aDimension.height);
						Point reachabilityPoint = new Point(aPoint.x - resourceReachabilityHeigntDif, aPoint.y - resourceReachabilityWidthDif);
						Rectangle reachabilityRectangle = new Rectangle(reachabilityPoint, reachabilityDimension);
						reachabilitySum += (passiveResource.getBounds().intersects(reachabilityRectangle) && !passiveResource.intersects(activeResource.getBounds())) ? 1
								: 0;
					}
				}
			}
		}
		result = reachabilitySum / paths.size();
		// result = (double) reachabilitySumTotal/*/ (double) sr*/;
		LOGGER.trace("{} criterion: {} result: {}", msg, CRITERION_NAME, result);
		try {
			fileWriter.append("Alternative " + alternativeCounter + ":\n\t" + alternative + " ]\n\t\tScore: " + result + "\n\n");
		}
		catch ( IOException e ) {
			//LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		alternativeCounter++;
		return result;
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
