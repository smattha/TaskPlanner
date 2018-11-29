package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.print.Doc;
import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;
import lms.robopartner.mfg_planning_tool.LayoutScheduler3D;
import lms.robopartner.mfg_planning_tool.TaskSchedulerForLayout;
import lms.robopartner.mfg_planning_tool.impact.interfaces.AverageRawCriteriaValuesDecisionPointAssignmentsConsumer;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * This is mainly used to evaluate the plan’s calculated z-axis positioning. The
 * reachability is used in order to provide a second way of initial evaluation
 * of the z axis positioning of the components. The reachability of the robot is
 * considered as being a circle around the robot having its centre placed at the
 * 35% of the robot’s overall height from its bottom (that value has just been
 * noticed as being close to true for the COMAU robots. Further investigation of
 * this value should be done in order to be accepted as the correct one)
 * 
 * @author Jason
 *
 */
public class ThreeDimensionalReachability extends AbstractCriterion {
	private int internalDH = 1;
	private int internalMNA = 100;
	private int internalSR = 10;
	private PLANNINGINPUT aPlanninginput;
	private Document planningInputDocument;

	public ThreeDimensionalReachability(int internalDH, int internalMNA, int internalSR, PLANNINGINPUT aPlanninginput,
			Document planningInputDocument) {
		super();
		this.internalDH = internalDH;
		this.internalMNA = internalMNA;
		this.internalSR = internalSR;
		this.planningInputDocument = planningInputDocument;
		this.aPlanninginput = aPlanninginput;
	}

	public ThreeDimensionalReachability() {
		super();

	}

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ThreeDimensionalReachability.class);
	private static final String CRITERION_NAME = "ACCESSIBILITY";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * planning.scheduler.algorithms.impact.criteria.AbstractCriterion#getValue(java
	 * .util.Vector, planning.scheduler.simulation.interfaces.PlanHelperInterface,
	 * java.util.Calendar)
	 */
	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
		String msg = ".getValue(): ";
		int sr = paths.size();
		double theSumOfAllCostsForThisLayout = 0;
		int srIndex = 0;// for logging purposes
		double accessibilitySumTemp = 0;
		double accessibilitySum = 0;
		double accessibilitySumTotal = 0;
		boolean totalFoundPlacement = false;
		int Counter = 0;

		// This Map contains the locations that should be used for the next impact run.
		// Resource ID or Part Id - Location

		for (TreeNode[] path : paths) {
			srIndex++;// increase starting from 0.
			logger.trace(msg + "Sample " + srIndex + " of " + sr + ".");
			Map<String, Point3D> resourcesAndPartsMapingForSR = new HashMap<String, Point3D>();

			for (TreeNode treeNode : path) {

				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;
				// Getting all locations for the next impact run
				for (Assignment assignment : layerNode.getNodeAssignments()) {
					// The resource or part
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					// the location
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();

					resourcesAndPartsMapingForSR.put(taskDataModel.getTaskId(),
							MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
					logger.trace(msg + "(Sample) PartOrResourceID=" + taskDataModel.getTaskId() + " Location="
							+ MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
				}
			} // end for all nodes

			for (AssignmentDataModel anAssignment : helper.getAssignments()) {
				resourcesAndPartsMapingForSR.put(anAssignment.getTaskDataModel().getTaskId(),
						MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));
				logger.trace(msg + "(Assignment) PartOrResourceID=" + anAssignment.getTaskDataModel().getTaskId()
						+ " Location="
						+ MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));

			}

			// Check if all resources and tasks in the input have been assigned
			// if one has not been assigned return max value.
			boolean foundUnassignedResource = false;
			for (RESOURCE aResource : this.aPlanninginput.getRESOURCES().getRESOURCE()) {
				if (resourcesAndPartsMapingForSR.containsKey(aResource.getId())) {
					foundUnassignedResource = true;
					break;
				}
			}
			if (!foundUnassignedResource) {

				for (TASK aTask : this.aPlanninginput.getTASKS().getTASK()) {
					if (resourcesAndPartsMapingForSR.containsKey(aTask.getId())) {
						foundUnassignedResource = true;
						break;
					}
				}
			}

			if (!foundUnassignedResource) {

				Document planningInputDocument = this.planningInputDocument;
				TaskSchedulerForLayout taskSchedulerForGivenLayout = new TaskSchedulerForLayout(
						resourcesAndPartsMapingForSR, this.internalDH, this.internalMNA, this.internalSR);
				taskSchedulerForGivenLayout.evaluate(planningInputDocument);
				AverageRawCriteriaValuesDecisionPointAssignmentsConsumer criteriaValueConsumer = taskSchedulerForGivenLayout
						.getCriteriaConsumer();

				theSumOfAllCostsForThisLayout = criteriaValueConsumer.getAveragedSumCriteriaValues();// assign with
																										// criteria read
																										// values
				logger.trace(msg + " SR:" + srIndex + " DecisionPoint3Ds:" + criteriaValueConsumer.getDecisionPoints()
						+ " SumOfCriteria:" + criteriaValueConsumer.getAveragedSumCriteriaValues() + " AverageCriteria:"
						+ criteriaValueConsumer.getAverageCriteriaValues());
			} else {
				ArrayList<Rectangle> RobotPositions = new ArrayList<Rectangle>();
				ArrayList<Rectangle> HumanPositions = new ArrayList<Rectangle>();
				ArrayList<Rectangle> PartsPositions = new ArrayList<Rectangle>();
				ArrayList<Integer> PartsPositionsZ = new ArrayList<Integer>();
				ArrayList<Integer> RobotReachability = new ArrayList<Integer>();
				ArrayList<Integer> RobotZDimension = new ArrayList<Integer>();
				for (TreeNode treeNode : path) {

					// Cast to layer node
					LayerNode layerNode = (LayerNode) treeNode;
					// Getting all locations for the next impact run
					for (Assignment assignment : layerNode.getNodeAssignments()) {
						// The resource or part
						TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
						// the location
						ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();
//					        	if the task is a robot create a robotpositions rectangle
						if (taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
								.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
							Rectangle RobotRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel,
									taskDataModel);
							RobotPositions.add(RobotRectangle);
							RobotReachability.add(Integer.parseInt(
									taskDataModel.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)));
							// TODO A Z-Height Property has to be added to all robots (or all components for
							// that matter)
							RobotZDimension.add(Integer
									.parseInt(taskDataModel.getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME)));
						}
//					        	if the task is a human create a humanpositions rectangle
						else if (taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
								.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
							Rectangle HumanRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel,
									taskDataModel);
							HumanPositions.add(HumanRectangle);
						}
//					        	if the task is a task create a taskpositions rectangle
						else if (taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
								.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK)) {
							Rectangle PartRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel,
									taskDataModel);
							PartsPositions.add(PartRectangle);
							// System.out.println(taskDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));
							PartsPositionsZ.add(Integer.parseInt(
									taskDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME)));
						}
					}
				}

				int robotsCounter = 0;
				if (!RobotPositions.isEmpty() && !PartsPositions.isEmpty()) {
					for (Rectangle RobotPosition : RobotPositions) {
						int partsCounter = 0;
						for (Rectangle PartsPosition : PartsPositions) {
//			            			Most of the robots have maximum reachability at an approximate height of 35% of its overall height
							double RobotZ = (RobotZDimension.get(robotsCounter)) * 0.35;
							int PartZ = PartsPositionsZ.get(partsCounter);
							double distance = Math.sqrt(Math.pow((RobotPosition.x - PartsPosition.x), 2)
									+ Math.pow((RobotPosition.y - PartsPosition.y), 2) + Math.pow((RobotZ - PartZ), 2));
							accessibilitySumTemp += (distance > RobotReachability.get(robotsCounter)) ? 1 : 0;
							partsCounter++;
							Counter++;
						}
						accessibilitySum += accessibilitySumTemp / partsCounter;
						robotsCounter++;
					}

				}
				accessibilitySum = accessibilitySum / robotsCounter;
				theSumOfAllCostsForThisLayout = accessibilitySum / (double) sr;
			}
			accessibilitySumTotal += theSumOfAllCostsForThisLayout;

		} // end for all paths

		// Normalize with SR.
		double resultCost = (double) accessibilitySumTotal / (double) Counter;

		logger.trace(msg + ThreeDimensionalReachability.CRITERION_NAME + "=" + resultCost + ". Solution found="
				+ totalFoundPlacement);
		return resultCost;

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
		return ThreeDimensionalReachability.CRITERION_NAME;
	}

}
