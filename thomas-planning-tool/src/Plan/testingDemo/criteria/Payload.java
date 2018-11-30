package testingDemo.criteria;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class Payload extends AbstractCriterion {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Payload.class);
	private static final String CRITERION_NAME = "PAYLOAD";

	private FileWriter fileWriter;
	private int alternativeCounter = 0;

	public Payload() {
		super();
		try {
			fileWriter = new FileWriter("C:\\Payload.doc");
		} catch (IOException e) {
			// logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = ".getValue(): ";
		int sr = paths.size();
		double payloadSum = 0;
		double partialPayload = 0;
		String alternative = "[ ";
		for (int i = 0; i < sr; i++) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				for (Assignment assignment : node.getNodeAssignments()) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();
					alternative += "taskId: " + taskId + " resourceId: " + resourceId;
					double partialPayloadTemp = 0;
					double payloadLimit = Double
							.parseDouble(assignment.getResource().getResourceDataModel().getProperty("Payload (kg)"));
					double partWeight = Double
							.parseDouble(assignment.getTask().getTaskDataModel().getProperty("Weight (Kg)"));
					if (assignment.getResource().getResourceDataModel()
							.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
							.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
						if (partWeight > payloadLimit)
							return 0;
						else
							partialPayloadTemp = partWeight;
					} else if (assignment.getResource().getResourceDataModel()
							.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
							.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
						if (partWeight > payloadLimit)
							return 0;
						else
							partialPayloadTemp = partWeight;
					}
					partialPayload += partialPayloadTemp;
					logger.trace(msg + " taskId: {} resourceId: {} {}: {}", taskId, resourceId, Payload.CRITERION_NAME,
							partialPayload);
				}
				payloadSum += partialPayload;
			}
		}
		payloadSum = payloadSum / (double) sr;
		logger.trace(msg + "{} : {}", Payload.CRITERION_NAME, payloadSum);
		try {
			fileWriter.append("Alternative " + alternativeCounter + ":\n\t" + alternative + " ]\n\t\tScore: "
					+ payloadSum + "\n\n");
		} catch (IOException e) {
			// logger.error(ExceptionUtils.getStackTrace(e));
		}
		alternativeCounter++;
		return payloadSum;
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
		return Payload.CRITERION_NAME;
	}

}
