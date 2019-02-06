package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.LayoutEvaluator;
import lms.robopartner.datamodel.map.controller.MapParameters;

import org.slf4j.LoggerFactory;

import planning.model.JobDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.model.WorkcenterDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * @author Spyros Do not use, anywhere. Kept for reference.
 */
@Deprecated
public class OldSchedulingCost extends AbstractCriterion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(OldSchedulingCost.class);
	private static final String CRITERION_NAME = "SCHEDULING COST";
	private static final int DEFAULT_MAX_TRIES = 100;
	private int maxTries = OldSchedulingCost.DEFAULT_MAX_TRIES;

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
		String msg = ".getValue(): ";
		int sr = paths.size();
		int srIndex = 0;// for logging purposes
		double schedulingCostSum = 0;
		boolean totalFoundPlacement = false;

		// Get all tasks ids
		Map<String, TaskDataModel> allTaskIdsReference = new TreeMap<String, TaskDataModel>();
		for (JobDataModel aJob : helper.getJobs()) {
			for (TaskDataModel aTask : aJob.getTasks()) {
				allTaskIdsReference.put(aTask.getTaskId(), aTask);
			}
		}
		// Get all resources ids
		Map<String, ResourceDataModel> allResourcesIdsReference = new TreeMap<String, ResourceDataModel>();
		for (WorkcenterDataModel aWorkcenter : helper.getAllWorkcentrers()) {
			for (ResourceDataModel aResourceDataModel : aWorkcenter.getResourceDataModelVector()) {
				allResourcesIdsReference.put(aResourceDataModel.getResourceId(), aResourceDataModel);
			}
		}

		for (TreeNode[] path : paths) {
			srIndex++;// increase starting from 0.
			logger.trace(msg + "Sample " + srIndex + " of " + sr + ".");
			boolean srFoundPlacement = true;
			List<String> tempTaskIdsList = new ArrayList<String>();
			tempTaskIdsList.addAll(allTaskIdsReference.keySet());// initialize with all tasks.
			Vector<Assignment> allAssignments = new Vector<Assignment>();
			// Check if all tasks have been placed.
			for (TreeNode treeNode : path) {
				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;

				for (Assignment assignment : layerNode.getNodeAssignments()) {
					tempTaskIdsList.remove(assignment.getTask().getTaskDataModel().getTaskId());

				}
			}
			// created for access to enclosing type
			LayerNode aTempLayerNodeToAccessEnclosingType = new LayerNode(
					new HashMap<ResourceSimulator, Vector<TaskSimulator>>(), new Vector<LayerNode.Assignment>(),
					new Vector<ResourceSimulator>());

			// Now tempTaskIdsList contains the ids of all the tasks that have not been
			// assigned.
			for (String aTaskId : tempTaskIdsList) {
				logger.trace(msg + "Sample " + srIndex + " of " + sr + ". task not assigned:" + aTaskId);
				Point aPoint = LayoutEvaluator.getAValidPointForResource(allTaskIdsReference.get(aTaskId),
						allAssignments, this.maxTries);
				if (aPoint != null) {
					// create a fake assignment

					Assignment anAssignment = aTempLayerNodeToAccessEnclosingType.new Assignment(
							new ResourceSimulator(
									allResourcesIdsReference.get(MapParameters.getIDfromLocation(aPoint))),
							new TaskSimulator(allTaskIdsReference.get(aTaskId)));
					allAssignments.add(anAssignment);
				} else {
//					issue a warning
					logger.debug(msg + "Sample " + srIndex + " of " + sr + "." + "Unable to find a placement for task:"
							+ aTaskId);
					srFoundPlacement = false;
				}
				// assign a random resource.

			}

			for (TreeNode treeNode : path) {
				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;
				for (Assignment assignment : layerNode.getNodeAssignments()) {
					@SuppressWarnings("unused")
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					// Here implementation could continue
				}

				// Create input

				// Send input to Scheduling Impact

				// Read Criteria values

				// Combine Criteria values and return a value.
			}
			totalFoundPlacement = totalFoundPlacement || srFoundPlacement;
		} // end for all paths

		// Normalize with SR.
		double resultCost = (double) schedulingCostSum / (double) sr;

		logger.trace(
				msg + OldSchedulingCost.CRITERION_NAME + "=" + resultCost + ". Solution found=" + totalFoundPlacement);
		return resultCost;

	}

	@Override
	public double getWeight() {
		return 1;
	}

	@Override
	public boolean isBenefit() {
		return false;
	}

	@Override
	public String getCriterionName() {
		return OldSchedulingCost.CRITERION_NAME;
	}

}
