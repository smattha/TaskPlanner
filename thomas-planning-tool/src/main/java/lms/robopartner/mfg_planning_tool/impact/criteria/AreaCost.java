package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.tree.TreeNode;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import gr.upatras.lms.util.Convert;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class AreaCost extends AbstractCriterion {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AreaCost.class);
	private static final String CRITERION_NAME = "AREA COST";
	private FileWriter fileWriter = null;
	private int alternativeCounter = 0;

	/**
	 * 
	 */
	public AreaCost(PLANNINGINPUT planninginput) {
		try {
			fileWriter = new FileWriter("C:\\AreaCost.doc");
		} catch (IOException e) {
			// LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = ".getValue(): ";
		// Each path is a solution. There are SR paths provided each time.
		int sr = paths.size();
		double areaCostSum = 0;
		String alternative = "[ ";
		for (TreeNode[] path : paths) {
			for (TreeNode treeNode : path) {
				LayerNode node = (LayerNode) treeNode;
				// alternative = node.get;
				for (Assignment assignment : node.getNodeAssignments()) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();
					double partialAreaCost;
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					double widthTask = Convert
							.getDouble(taskDataModel.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
					double heightTask = Convert
							.getDouble(taskDataModel.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
					ResourceDataModel resourceDataModel = assignment.getResource().getResourceDataModel();
					alternative += taskId + " X: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME) + " Y: "
							+ resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME);
					double resourceX = Convert
							.getDouble(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME));
					double resourceY = Convert
							.getDouble(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME));

					partialAreaCost = widthTask * heightTask + resourceX * resourceY;
					areaCostSum += partialAreaCost;

					LOGGER.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + "- partial"
							+ AreaCost.CRITERION_NAME + "=" + partialAreaCost);
				}
			}

		}
		areaCostSum = ((MapParameters.MAP_HEIGHT * MapParameters.MAP_WIDTH) - areaCostSum) / (double) sr;
		if (areaCostSum < 0)
			areaCostSum = -areaCostSum;
		LOGGER.trace(msg + AreaCost.CRITERION_NAME + "=" + areaCostSum + ".");
		if (alternativeCounter >= 10 && alternativeCounter < 170) {
			try {
				fileWriter.append("Alternative " + alternativeCounter + ":\n\t" + alternative + " ]\n\t\tScore: "
						+ areaCostSum + "\n\n");
			} catch (IOException e) {
				// LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		alternativeCounter++;
		return areaCostSum;
	}

	@Override
	public double getWeight() {
		return 0.17;
	}

	@Override
	public boolean isBenefit() {
		return false;
	}

	@Override
	public String getCriterionName() {
		return AreaCost.CRITERION_NAME;
	}

}
