package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class TimeToComplete extends AbstractCriterion {

	private static org.slf4j.Logger	logger				= LoggerFactory.getLogger(TimeToComplete.class);
	private static final String		CRITERION_NAME		= "TIME TO COMPLETE";
	private Map<String, Point3D>	resourcesAndPartsMapingForSR;
	private FileWriter				fileWriter;
	private int						alternativeCounter	= 0;

	@SuppressWarnings ("unused")
	private TimeToComplete() {
		super();
	}

	public TimeToComplete(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		super();
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
		try {
			fileWriter = new FileWriter("C:\\TimeToComplete.doc");
		}
		catch ( IOException e ) {
			//logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {

		String msg = ".getValue(): ";
		int sr = paths.size();
		double timeToCompleteSum = 0;
		double partialTimeToComplete = 0;
		String alternative = "[ ";
		for ( int i = 0; i < sr; i++ ) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);
			for ( int j = 0; j < path.length; j++ ) {
				LayerNode node = (LayerNode) path[j];
				for ( Assignment assignment : node.getNodeAssignments() ) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();
					alternative += "taskId: " + taskId + " resourceId: " + resourceId;
					double partialTimeToCompleteTemp = 0;
					double centerXTask;
					double centerYTask;
					double centerXResource;
					double centerYResource;
					Point3D taskPoint3D = resourcesAndPartsMapingForSR.get(taskId);
					double xTask = taskPoint3D.getX();
					double yTask = taskPoint3D.getY();
					Point3D resourcePoint3D = resourcesAndPartsMapingForSR.get(resourceId);
					double xResource = resourcePoint3D.getX();
					double yResource = resourcePoint3D.getY();
					double widthTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
					double heightTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME));
					double widthResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
					double heightResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME));
					centerXTask = xTask + (widthTask / 2);
					centerYTask = yTask + (heightTask / 2);
					centerXResource = xResource + (widthResource / 2);
					centerYResource = yResource + (heightResource / 2);
					double distance = Math.sqrt(Math.pow((centerXResource - centerXTask), 2) + Math.pow((centerYResource - centerYTask), 2));
					double timeToCompleteMovement = distance
							/ (Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty("Speed (mm/s)")) / 1);
					double maxTimeToComplete = Math.sqrt((Math.pow(MapParameters.MAP_HEIGHT, 2) + Math.pow(MapParameters.MAP_WIDTH, 2))) / 0.25;
					partialTimeToCompleteTemp = ((maxTimeToComplete - timeToCompleteMovement) / (maxTimeToComplete - 0));
					partialTimeToComplete += partialTimeToCompleteTemp;

					logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + TimeToComplete.CRITERION_NAME + "="
							+ partialTimeToComplete);
				}
				partialTimeToComplete = partialTimeToComplete / path.length;
				timeToCompleteSum += partialTimeToComplete;
			}
		}
		timeToCompleteSum = timeToCompleteSum / (double) sr;
		logger.trace(msg + TimeToComplete.CRITERION_NAME + "=" + timeToCompleteSum + ".");
		try {
			fileWriter.append("Alternative " + alternativeCounter + ":\n\t" + alternative + " ]\n\t\tScore: " + timeToCompleteSum + "\n\n");
		}
		catch ( IOException e ) {
			//logger.error(ExceptionUtils.getStackTrace(e));
		}
		alternativeCounter++;
		return timeToCompleteSum;
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
		return TimeToComplete.CRITERION_NAME;
	}

}
