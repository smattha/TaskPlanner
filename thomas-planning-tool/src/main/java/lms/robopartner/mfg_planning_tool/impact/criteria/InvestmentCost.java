package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * this criterion calculates the operational cost for the assignment. This is
 * calculated differently if the resource executing the task is a human or a
 * robot. In the case of the robot, there is a cost per part that has been
 * calculated and is used (for the calculated of this cost/part for the robot
 * please refer to the documentation.docx) In the case of the human operator,
 * the time that requires a task is calculated and is cross-referenced against
 * its annual or monthly payment.
 * 
 * The //TODO commented lines should be enabled when the code is to run with the
 * layout that takes into consideration the z axis as well
 * 
 * @author Jason
 *
 */
public class InvestmentCost extends AbstractCriterion {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(InvestmentCost.class);
	private static final String CRITERION_NAME = "INVESTMENT COST";
	private Map<String, Point3D> resourcesAndPartsMapingForSR;
	private FileWriter fileWriter;
	private int alternativeCounter = 0;

	/**
	 * Hide constructor
	 */
	@SuppressWarnings("unused")
	private InvestmentCost() {
		super();
	}

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public InvestmentCost(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		super();
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
		try {
			fileWriter = new FileWriter("C:\\InvestmentCost.doc");
		} catch (IOException e) {
			// logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = ".getValue(): ";
		// Each path is a solution. There are SR paths provided each time.
		int sr = paths.size();
		double investmentCostSum = 0;
		double partialInvestmentCost = 0;
		String alternative = "[ ";
		for (int i = 0; i < sr; i++) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);

			// path is solution
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				for (Assignment assignment : node.getNodeAssignments()) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();
					alternative += "taskId: " + taskId + " resourceId: " + resourceId;
					// calculate
					double investmentCost = 0;
					double partialInvestmentCostTemp = 0;
					if (assignment.getResource().getResourceDataModel()
							.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
							.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
						// this is the cost per part of the robot
						investmentCost = Double.parseDouble(
								assignment.getResource().getResourceDataModel().getProperty("Cost (euro)"));
					} else if (assignment.getResource().getResourceDataModel()
							.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
							.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
						// calculated from the monthly payment
						double costPerSecond = Double.parseDouble(
								assignment.getResource().getResourceDataModel().getProperty("Cost (euro)"));
						double centerXTask;
						double centerYTask;
						double centerXResource;
						double centerYResource;

						Point3D taskPoint3D = resourcesAndPartsMapingForSR.get(taskId);
						double xTask = taskPoint3D.getX();
						double yTask = taskPoint3D.getY();
						// TODO the below commented line(s) are the 3D criteria changes
						// double zTask =
						// Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));

						Point3D resourcePoint3D = resourcesAndPartsMapingForSR.get(resourceId);
						double xResource = resourcePoint3D.getX();
						double yResource = resourcePoint3D.getY();
						// TODO the below commented line(s) are the 3D criteria changes
						// double zResource =
						// Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));

						double widthTask = Double.parseDouble(assignment.getTask().getTaskDataModel()
								.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
						double heightTask = Double.parseDouble(assignment.getTask().getTaskDataModel()
								.getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME));
						double widthResource = Double.parseDouble(assignment.getResource().getResourceDataModel()
								.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
						double heightResource = Double.parseDouble(assignment.getResource().getResourceDataModel()
								.getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME));

						centerXTask = xTask + (widthTask / 2);
						centerYTask = yTask + (heightTask / 2);
						centerXResource = xResource + (widthResource / 2);
						centerYResource = yResource + (heightResource / 2);

						double distance = Math.sqrt(Math.pow((centerXResource - centerXTask), 2)
								+ Math.pow((centerYResource - centerYTask), 2));
						// TODO the below commented line(s) are the 3D criteria changes
						// double distance = Math.sqrt(Math.pow((centerXResource-centerXTask),
						// 2)+Math.pow((centerYResource-centerYTask), 2)+Math.pow((zResource-zTask),
						// 2));

						double timeToCompleteMovement = distance / (Double.parseDouble(
								assignment.getResource().getResourceDataModel().getProperty("Speed (mm/s)")) / 1);
						investmentCost = timeToCompleteMovement * costPerSecond;

					}
					// partialInvestmentCostTemp =
					// ((ReschedulingPlanningGenerator.MAX_INVESTMENT_COST - investmentCost) /
					// (ReschedulingPlanningGenerator.MAX_INVESTMENT_COST -
					// ReschedulingPlanningGenerator.MIN_INVESTMENT_COST));

					partialInvestmentCost += partialInvestmentCostTemp;

					logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + InvestmentCost.CRITERION_NAME
							+ "=" + partialInvestmentCost);
				}
				// partialInvestmentCost = partialInvestmentCost/path.length;
				investmentCostSum += partialInvestmentCost;
			}
		}
		investmentCostSum = investmentCostSum / (double) sr;
		alternativeCounter++;
		return investmentCostSum;
	}

	@Override
	public double getWeight() {
		return 0.2;
	}

	@Override
	public boolean isBenefit() {
		return true;
	}

	@Override
	public String getCriterionName() {
		return InvestmentCost.CRITERION_NAME;
	}

}
