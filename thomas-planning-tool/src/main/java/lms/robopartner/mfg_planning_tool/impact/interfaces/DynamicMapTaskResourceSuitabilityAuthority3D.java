/**
 * 
 */
package lms.robopartner.mfg_planning_tool.impact.interfaces;

import java.awt.Point;
import java.awt.Shape;
import java.util.Calendar;
import java.util.Vector;

import lms.robopartner.datamodel.map.controller.LayoutEvaluator3D;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.interfaces.DynamicResourceTaskSuitabilityAuthorityInterface;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;
import eu.integration.services.FindLayout;

/**
 * An instance of this class is responsible for identifying which resources are
 * available for assignments on tasks. This class makes the following
 * assumptions: + Objects that are modeled by Tasks of given size are place into
 * a floor which is modeled as a grid. Grid squares are the resources. + All
 * objects and floor space are orthogonal (width*height) + Location are always
 * modeled as the upper-left corner of objects and map. + (0,0) is the
 * upper-left corner of the grid. + Objects cannot overlap. For this reason this
 * class checks assignments and considers the resources (grid squares) that are
 * occupied by an object as unavailable.
 * 
 * @author Spyros
 */
// TODO now is in 2D it should be in 3D.
public class DynamicMapTaskResourceSuitabilityAuthority3D implements DynamicResourceTaskSuitabilityAuthorityInterface {

	private static org.slf4j.Logger	LOGGER	= LoggerFactory.getLogger(DynamicMapTaskResourceSuitabilityAuthority3D.class);
	private PLANNINGINPUT			startingPlanningInput;
	private LayoutEvaluator3D		layoutEvaluator3D;

	public void setStartingPlanningInput(PLANNINGINPUT startingPlanningInput) {
		this.startingPlanningInput = startingPlanningInput;
	}

	/**
	 * Initialize, works only with DH=1. This means {@link IMPACT#getDH() should
	 * be equals to 1} Also utilizes the {@link MapParameters}
	 */
	public DynamicMapTaskResourceSuitabilityAuthority3D() {
		layoutEvaluator3D = new LayoutEvaluator3D();
	}

	/*
	 * (non-Javadoc)
	 * @see lms.robopartner.mfg_planning_tool.impact.interfaces.
	 * DynamicResourceTaskSuitabilityAuthorityInterface
	 * #isSuitabilityValid(lms.robopartner
	 * .planning.scheduler.simulation.ResourceSimulator,
	 * lms.robopartner.planning.scheduler.simulation.TaskSimulator,
	 * java.util.Calendar,
	 * lms.robopartner.planning.scheduler.simulation.interfaces
	 * .PlanHelperInterface)
	 */

	/**
     * 
     */
	public boolean isSuitabilityValid(ResourceSimulator resource, TaskSimulator task, Calendar timeNow, PlanHelperInterface planHelperInterface) {
		String msg = ".isSuitabilityValid(): taskId: " + task.getTaskId() + " resourceId: " + resource.getResourceId();
		Vector<AssignmentDataModel> existingAssignements = planHelperInterface.getAssignments();
		FindLayout.sizeOfExistingAss = Math.max(existingAssignements.size(), FindLayout.sizeOfExistingAss);
		boolean isSuitable = layoutEvaluator3D.isSuitabilityValid3D(resource, task, existingAssignements, this.startingPlanningInput);
		// LOGGER.debug(msg + " isSuitable = " + isSuitable);
		@SuppressWarnings ("unchecked")
		Vector<AssignmentDataModel> checkVector = (Vector<AssignmentDataModel>) existingAssignements.clone();
		AssignmentDataModel tempAssign = new AssignmentDataModel(task.getTaskDataModel(), resource.getResourceDataModel(), timeNow, 0, false, null);
		checkVector.add(tempAssign);
		@SuppressWarnings ("unused")
		boolean notIntersecting = testIntersectionOfFinalAssignments(checkVector); // used for conditional breakpoint
																					// only
		return isSuitable;
	}

	private boolean testIntersectionOfFinalAssignments(Vector<AssignmentDataModel> assignments) {
		for ( AssignmentDataModel aAssignment : assignments ) {
			TaskDataModel aTaskDataModel = aAssignment.getTaskDataModel();
			ResourceDataModel aResourceDataModel = aAssignment.getResourceDataModel();
			Shape aRectangle = DataModelToAWTHelper.createShape(aTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), aTaskDataModel, aResourceDataModel);
			String angle = aAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
			Point resourcePoint = DataModelToAWTHelper.getPointFromResource(aResourceDataModel);
			if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") ) {
				aRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), aRectangle, resourcePoint.x, resourcePoint.y, aTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
				// LOGGER.debug(resourcePoint.x + " " + resourcePoint.y);
			}
			for ( AssignmentDataModel bAssignment : assignments ) {
				TaskDataModel bTaskDataModel = bAssignment.getTaskDataModel();
				if ( aTaskDataModel.getTaskId().equals(bTaskDataModel.getTaskId()) ) continue;
				ResourceDataModel bResourceDataModel = bAssignment.getResourceDataModel();
				Shape bRectangle = DataModelToAWTHelper.createShape(bTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), bTaskDataModel, bResourceDataModel);
				angle = bAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
				resourcePoint = DataModelToAWTHelper.getPointFromResource(bResourceDataModel);
				if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
					bRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), bRectangle, resourcePoint.x, resourcePoint.y, bTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
				if ( aRectangle.intersects(bRectangle.getBounds()) ) {
					LOGGER.debug(".testIntersectionOfFinalAssignments():\n\t\t\t\t atask: {} aresource: {}\n\t\t\t\t btask: {} bresource: {}", aTaskDataModel.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)
							+ " " + aTaskDataModel.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME), aResourceDataModel.getResourceId(), bTaskDataModel.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)
							+ " " + bTaskDataModel.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME), bResourceDataModel.getResourceId());
					LOGGER.debug("\naRectangle: \n\t\tx: {} \n\t\tw: {} \nbRectangle: \n\t\tx: {} \n\t\tw: {}", aRectangle.getBounds().x + " y: "
							+ aRectangle.getBounds().y, aRectangle.getBounds().width + " h: " + aRectangle.getBounds().height, bRectangle.getBounds().x
							+ " y: " + bRectangle.getBounds().y, bRectangle.getBounds().width + " h: " + bRectangle.getBounds().height);
					return false;

				}
			}
		}
		return true;
	}
}
