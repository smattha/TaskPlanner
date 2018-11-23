/**
 * 
 */
package lms.robopartner.datamodel.map.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Vector;

import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

/**
 * @author Spyros This class makes the following assumptions: + Objects that are
 *         modeled by Tasks of given size are place into a floor which is
 *         modeled as a grid. Grid squares are the resources. + All objects and
 *         floor space are Boxes (width*length*height) + Location are always
 *         modeled as the upper-left corner of objects and map. + (0,0) is the
 *         upper-left corner of the grid. + Objects cannot overlap. For this
 *         reason this class checks assignments and considers the resources
 *         (grid squares) that are occupied by an object as unavailable.
 * 
 */
public class LayoutEvaluator3D {

	private static org.slf4j.Logger	LOGGER	= LoggerFactory.getLogger(LayoutEvaluator3D.class);

	/**
	 * 
	 * @param resource
	 * @param task
	 * @param existingAssignments
	 * @param startingPlanningInput
	 * @return
	 */
	public boolean isSuitabilityValid3D(ResourceSimulator resource, TaskSimulator task, Vector<AssignmentDataModel> existingAssignments, PLANNINGINPUT startingPlanningInput) {
		// Get box from task
		// get angles from resource - TO DO
		// int angleRx=0;
		// int angleRy=0;
		// int angleRz=0;
		// get point 3D from resource
		boolean isXYZIndependentAndReachabilityMatching = this.isSuitabilityValid3D(Integer.parseInt(task.getTaskDataModel().getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)), existingAssignments, startingPlanningInput, task.getTaskId(), task, resource);

		// Relationships and restriction between parts

		return isXYZIndependentAndReachabilityMatching;
	}

	public boolean isSuitabilityValid3D(int taskReachability, Vector<AssignmentDataModel> existingAssignments, PLANNINGINPUT startingPlanningInput, String taskId, TaskSimulator taskSimulator, ResourceSimulator resource) {

		String msg = ".isSuitabilityValid(): ";
		String angle = resource.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
		Shape assignmentRectangle = DataModelToAWTHelper.createShape(taskSimulator, resource);
		Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resource.getResourceDataModel());
		LOGGER.debug("{}\naRectangle: \n\t\tx: {} y: {} \n\t\tw: {} h: {}", msg, assignmentRectangle.getBounds().x, assignmentRectangle.getBounds().y, assignmentRectangle.getBounds().width, assignmentRectangle.getBounds().height);
		if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
			assignmentRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), assignmentRectangle, resourcePoint.x, resourcePoint.y, taskSimulator.getTaskDataModel().getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
		Rectangle mapRectangle = new Rectangle(new Dimension(MapParameters.MAP_WIDTH, MapParameters.MAP_HEIGHT));
		boolean isSuitable = true;

		boolean isSuitableForReachability = true;
		if ( mapRectangle.contains(assignmentRectangle.getBounds()) ) {
			for ( AssignmentDataModel anAssignment : existingAssignments ) {
				angle = anAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
				resourcePoint = DataModelToAWTHelper.getPointFromResource(anAssignment.getResourceDataModel());
				Shape assignedRectangle = DataModelToAWTHelper.createShape(anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), anAssignment.getTaskDataModel(), anAssignment.getResourceDataModel());
				if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
					assignedRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), assignedRectangle, resourcePoint.x, resourcePoint.y, anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
				LOGGER.debug(msg + "\n\tresource: {} \n\ttask: {} \n\tassignmentResource: {} \n\tassignmentTask: {}", resource.getResourceDataModel().getResourceId(), taskSimulator.getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)
						+ " " + taskSimulator.getTaskDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME), anAssignment.getResourceDataModel().getResourceId(), anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)
						+ " " + anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
				LOGGER.debug("\naRectangle: \n\t\tx: {} \n\t\tw: {} \nbRectangle: \n\t\tx: {} \n\t\tw: {}", assignmentRectangle.getBounds().x
						+ " y: " + assignmentRectangle.getBounds().y, assignmentRectangle.getBounds().width + " h: "
						+ assignmentRectangle.getBounds().height, assignedRectangle.getBounds().x + " y: " + assignedRectangle.getBounds().y, assignedRectangle.getBounds().width
						+ " h: " + assignedRectangle.getBounds().height);
				isSuitable = !assignmentRectangle.intersects(assignedRectangle.getBounds());
				if ( !isSuitable ) break;
			}
		}
		else isSuitable = false;

		if ( isSuitable
				&& !taskSimulator.getTaskDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT) )
			isSuitableForReachability = areWithinReachability(startingPlanningInput, existingAssignments, assignmentRectangle, taskId);

		// logger.trace(msg + "Rectangle = {}, isSuitable && isSuitableForReachability = {}, ", assignmentRectangle,
		// isSuitable && isSuitableForReachability);
		boolean returnValue = isSuitable && isSuitableForReachability;

		return returnValue;
	}

	/**
	 * @param resource
	 * @param task
	 * @param existingAssignments
	 * @return
	 */
	public Rectangle getReachabilityRectangle(Rectangle aTaskRectangle, int reachability) {
		String msg = ".getReachability() ";
		if ( reachability < 0 ) { throw new RuntimeException(msg + "reachability must be positive."); }
		int reachabilityWidth = Math.max(reachability, aTaskRectangle.width);
		int reachabilityHeight = Math.max(reachability, aTaskRectangle.height);
		Rectangle aReachabilityRectangle = new Rectangle(aTaskRectangle.getLocation(), new Dimension(reachabilityWidth, reachabilityHeight));

		aReachabilityRectangle.setLocation(aTaskRectangle.x + (int) ((aTaskRectangle.getCenterX() - aReachabilityRectangle.getCenterX())), aTaskRectangle.y
				+ (int) ((aTaskRectangle.getCenterY() - aReachabilityRectangle.getCenterY())));
		return aReachabilityRectangle;
	}

	public boolean areWithinReachability(Rectangle aReachabilityRectangle, Rectangle anotherReachabilityRectangle) {
		String msg = ".areWithinReach() ";
		if ( aReachabilityRectangle == null || anotherReachabilityRectangle == null ) { throw new RuntimeException(msg
				+ "aReachabilityRectangle or  anotherReachabilityRectangle should never be null."); }

		boolean areWithinReach = aReachabilityRectangle.intersects(anotherReachabilityRectangle)
				|| aReachabilityRectangle.contains(anotherReachabilityRectangle) || anotherReachabilityRectangle.contains(aReachabilityRectangle);
		LOGGER.trace(msg + " rectangle=" + aReachabilityRectangle + " and rectangle=" + anotherReachabilityRectangle + " areWithinReach="
				+ areWithinReach);
		return areWithinReach;
	}

	int		temp	= 0;
	String	prevId	= "";

	public boolean areWithinReachability(PLANNINGINPUT aPlanningInput, Vector<AssignmentDataModel> existingAssignments, Shape taskRectangle, String taskId) {
		if ( prevId.equals("") ) prevId = taskId;
		String msg = ".areWithinReachability(): ";
		boolean areWithinReachability = true;
		String resourceId = "";
		for ( TASKSUITABLERESOURCE aTask : aPlanningInput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE() ) {
			if ( aTask.getTASKREFERENCE().getRefid().equals(taskId) ) {
				resourceId = aTask.getRESOURCEREFERENCE().getRefid();
				for ( RESOURCE aResource : aPlanningInput.getRESOURCES().getRESOURCE() ) {
					if ( resourceId.equals(aResource.getId()) ) {
						// logger.debug(msg + "resourceName: {} TypePropertValue: {}", aResource.getNAME(),
						// MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN);
						if ( aResource.getNAME().startsWith(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN) ) return true;
						else break;
					}
				}
				break;
			}
		}

		for ( AssignmentDataModel anAssignment : existingAssignments ) {
			if ( resourceId.equals(anAssignment.getTaskDataModel().getTaskId()) ) {

				Dimension aDimension = DataModelToAWTHelper.getDimensionFromTask(anAssignment.getTaskDataModel());
				Point aPoint = DataModelToAWTHelper.getPointFromResource(anAssignment.getResourceDataModel());
				String shape = anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME);
				// logger.debug(msg + "init_x: {} init_y: ", aPoint.x, aPoint.y);
				Shape suitableResourceRectangle = DataModelToAWTHelper.createShape(aPoint, aDimension, shape);
				String angle = anAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
				Point resourcePoint = DataModelToAWTHelper.getPointFromResource(anAssignment.getResourceDataModel());
				if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
					suitableResourceRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), suitableResourceRectangle, resourcePoint.x, resourcePoint.y, anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
				int reachability = Integer.parseInt(anAssignment.getTaskDataModel().getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME));
				int resourceReachabilityWidthDif = reachability - aDimension.width;
				int resourceReachabilityHeigntDif = reachability - aDimension.height;
				Dimension reachabilityDimension = new Dimension(reachability + aDimension.width, reachability + aDimension.height);
				Point reachabilityPoint = new Point(aPoint.x - resourceReachabilityHeigntDif, aPoint.y - resourceReachabilityWidthDif);
				Rectangle reachabilityRectangle = new Rectangle(reachabilityPoint, reachabilityDimension);
				if ( taskRectangle.intersects(reachabilityRectangle) && !taskRectangle.intersects(suitableResourceRectangle.getBounds()) ) areWithinReachability = true;
				else areWithinReachability = false;
				LOGGER.debug(msg + "{}", areWithinReachability);
				break;
			}
		}
		if ( areWithinReachability && taskId.equals(prevId) ) {
			temp++;
		}

		if ( !taskId.equals(prevId) ) {
			LOGGER.debug("taskIdTocheck: {} boolean: {}  timesTrue: {}", taskId, areWithinReachability, temp);
			temp = 0;
		}
		return areWithinReachability;
	}
}
