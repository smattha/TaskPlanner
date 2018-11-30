package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * An initial ergonomy evaluation based on only to parameters of the NIOSH equation,
 * the Vertical Multiplier and the Distance Multiplier.
 * It is used in order to have a somewhat good layout concerning the z-axis.
 * 
 * The //TODO commented lines should be enabled when the code is to run with the
 * layout that takes into consideration the z axis as well
 * 
 * @author Jason
 *
 */
public class LayoutGenErgonomy extends AbstractCriterion {

	private int				internalDH	= 1;
	private int				internalMNA	= 100;
	private int				internalSR	= 10;
	private PLANNINGINPUT	aPlanninginput;
	private Document		planningInputDocument;

	public LayoutGenErgonomy(int internalDH, int internalMNA, int internalSR, PLANNINGINPUT aPlanninginput, Document planningInputDocument) {
		super();
		this.internalDH = internalDH;
		this.internalMNA = internalMNA;
		this.internalSR = internalSR;
		this.aPlanninginput = aPlanninginput;
		this.planningInputDocument = planningInputDocument;
	}

	public LayoutGenErgonomy() {
		super();

	}

	private static org.slf4j.Logger	logger			= LoggerFactory.getLogger(LayoutGenErgonomy.class);
	private static final String		CRITERION_NAME	= "ACCESSIBILITY";

	/*
	 * (non-Javadoc)
	 * 
	 * @see planning.scheduler.algorithms.impact.criteria.AbstractCriterion#getValue(java.util.Vector, planning.scheduler.simulation.interfaces.PlanHelperInterface, java.util.Calendar)
	 */
	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
		String msg = ".getValue(): ";
		int sr = paths.size();
		double theSumOfAllErgonomyForThisLayout = 0;
		int srIndex = 0;// for logging purposes
		double verticalMultiplierSumTemp = 0;
		double verticalMultiplierSum = 0;
		double distanceMultiplierSumTemp = 0;
		double distanceMultiplierSum = 0;
		double ergonomySumTotal = 0;
		boolean totalFoundPlacement = false;

		// This Map contains the locations that should be used for the next impact run.
		// Resource ID or Part Id - Location

		for ( TreeNode[] path : paths ) {
			srIndex++;// increase starting from 0.
			logger.trace(msg + "Sample " + srIndex + " of " + sr + ".");
			Map<String, Point3D> resourcesAndPartsMapingForSR = new HashMap<String, Point3D>();

			for ( TreeNode treeNode : path ) {

				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;
				// Getting all locations for the next impact run
				for ( Assignment assignment : layerNode.getNodeAssignments() ) {
					// The resource or part
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					// the location
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();

					resourcesAndPartsMapingForSR.put(taskDataModel.getTaskId(), MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
					logger.trace(msg + "(Sample) PartOrResourceID=" + taskDataModel.getTaskId() + " Location="
							+ MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
				}
			}// end for all nodes

			ArrayList<Rectangle> RobotPositions = new ArrayList<Rectangle>();
			ArrayList<Rectangle> HumanPositions = new ArrayList<Rectangle>();
			ArrayList<Rectangle> PartsPositions = new ArrayList<Rectangle>();
			ArrayList<Integer> PartsPositionsZ = new ArrayList<Integer>();
			// the default basepartposition is the best conscerning the ergonomics
			int BasePartPositionZ = 762;
			for ( TreeNode treeNode : path ) {

				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;
				// Getting all locations for the next impact run
				for ( Assignment assignment : layerNode.getNodeAssignments() ) {
					// The resource or part
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					// the location
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();
					// if the task is a robot create a robotpositions rectangle
					if ( taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT) ) {
						Rectangle RobotRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
						RobotPositions.add(RobotRectangle);
					}
					// if the task is a human create a human position's rectangle
					else if ( taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN) ) {
						Rectangle HumanRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
						HumanPositions.add(HumanRectangle);
					}
					// if the task is a task create a task position's rectangle
					else if ( taskDataModel.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK) ) {
						Rectangle PartRectangle = DataModelToAWTHelper.createRectangle(resourceDataModel, taskDataModel);
						PartsPositions.add(PartRectangle);
						if ( taskDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME) != null ) {
							PartsPositionsZ.add(Integer.parseInt(taskDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME)));
						}
						// TODO A property for the base part should be entered in the planning generator. That can also
						// be done by
						// testing whether this is the first task, as the base part will always be manipulate as the
						// first task.
						// this has also been implemented in Ergonomy.java
						if ( taskDataModel.getProperty(MapToResourcesAndTasks.BASE_PART_PROPERTY_NAME) != null
								&& taskDataModel.getProperty(MapToResourcesAndTasks.BASE_PART_PROPERTY_NAME).equals(true) ) {
							BasePartPositionZ = Integer.parseInt(taskDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));
						}
					}
				}
				int partsCounter = 0;
				if ( !PartsPositions.isEmpty() ) {
					// All distances below have been expressed in mm this should possibly be changed depending on the
					// way the Z-Axis will be modeled
					for ( int PartsPositionZ : PartsPositionsZ ) {
						if ( PartsPositionZ <= 0 ) {
							verticalMultiplierSumTemp = 0;
						}
						else if ( (PartsPositionZ > 0) && (PartsPositionZ < 127) ) {
							verticalMultiplierSumTemp = 0.78;
						}
						else if ( (PartsPositionZ >= 127) && (PartsPositionZ < 254) ) {
							verticalMultiplierSumTemp = 0.81;
						}
						else if ( (PartsPositionZ >= 254) && (PartsPositionZ < 381) ) {
							verticalMultiplierSumTemp = 0.85;
						}
						else if ( (PartsPositionZ >= 381) && (PartsPositionZ < 508) ) {
							verticalMultiplierSumTemp = 0.89;
						}
						else if ( (PartsPositionZ >= 508) && (PartsPositionZ < 635) ) {
							verticalMultiplierSumTemp = 0.93;
						}
						else if ( (PartsPositionZ >= 635) && (PartsPositionZ < 762) ) {
							verticalMultiplierSumTemp = 0.96;
						}
						else if ( (PartsPositionZ >= 762) && (PartsPositionZ < 889) ) {
							verticalMultiplierSumTemp = 1;
						}
						else if ( (PartsPositionZ >= 889) && (PartsPositionZ < 1016) ) {
							verticalMultiplierSumTemp = 0.96;
						}
						else if ( (PartsPositionZ >= 1016) && (PartsPositionZ < 1143) ) {
							verticalMultiplierSumTemp = 0.93;
						}
						else if ( (PartsPositionZ >= 1143) && (PartsPositionZ < 1270) ) {
							verticalMultiplierSumTemp = 0.89;
						}
						else if ( (PartsPositionZ >= 1270) && (PartsPositionZ < 1397) ) {
							verticalMultiplierSumTemp = 0.85;
						}
						else if ( (PartsPositionZ >= 1397) && (PartsPositionZ < 1524) ) {
							verticalMultiplierSumTemp = 0.81;
						}
						else if ( (PartsPositionZ >= 1524) && (PartsPositionZ < 1651) ) {
							verticalMultiplierSumTemp = 0.78;
						}
						else if ( (PartsPositionZ >= 1651) && (PartsPositionZ < 1778) ) {
							verticalMultiplierSumTemp = 0.74;
						}
						else if ( (PartsPositionZ >= 1778) && (PartsPositionZ < 1905) ) {
							verticalMultiplierSumTemp = 0.7;
						}
						else if ( PartsPositionZ >= 1905 ) {
							verticalMultiplierSumTemp = 0;
						}
						verticalMultiplierSum += verticalMultiplierSumTemp;

						double D = Math.abs(BasePartPositionZ - PartsPositionZ);
						if ( D <= 254 ) {
							distanceMultiplierSumTemp = 1;
						}
						else if ( (D > 254) && (D <= 381) ) {
							distanceMultiplierSumTemp = 0.94;
						}
						else if ( (D > 381) && (D <= 508) ) {
							distanceMultiplierSumTemp = 0.91;
						}
						else if ( (D > 508) && (D <= 635) ) {
							distanceMultiplierSumTemp = 0.89;
						}
						else if ( (D > 635) && (D <= 762) ) {
							distanceMultiplierSumTemp = 0.88;
						}
						else if ( (D > 762) && (D <= 1016) ) {
							distanceMultiplierSumTemp = 0.87;
						}
						else if ( (D > 1016) && (D <= 1270) ) {
							distanceMultiplierSumTemp = 0.86;
						}
						else if ( (D > 1270) && (D <= 1778) ) {
							distanceMultiplierSumTemp = 0.85;
						}
						else if ( D > 1778 ) {
							distanceMultiplierSumTemp = 0;
						}

						distanceMultiplierSum += distanceMultiplierSumTemp;

						partsCounter++;
					}

				}
				verticalMultiplierSum = verticalMultiplierSum / partsCounter;
				distanceMultiplierSum = distanceMultiplierSum / partsCounter;
				theSumOfAllErgonomyForThisLayout = (verticalMultiplierSum * distanceMultiplierSum) / (double) sr;
			}
			ergonomySumTotal += theSumOfAllErgonomyForThisLayout;

		}// end for all paths

		// Normalize with SR.
		double resultErgonomy = (double) ergonomySumTotal / (double) 1;

		logger.trace(msg + LayoutGenErgonomy.CRITERION_NAME + "=" + resultErgonomy + ". Solution found=" + totalFoundPlacement);
		return resultErgonomy;

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
		return LayoutGenErgonomy.CRITERION_NAME;
	}

}
