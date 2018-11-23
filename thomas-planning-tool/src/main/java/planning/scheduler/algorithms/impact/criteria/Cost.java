package planning.scheduler.algorithms.impact.criteria;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.ManualPlanHelperInterface;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class Cost extends AbstractCriterion {

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS

		int sr = paths.size();
		double costSum = 0;
		// DEBUG INFO ...
		// System.out.print("\tFOR ALTERNATIVE : ");
		// TreeNode[] tmppath = paths.get(0);
		// if(tmppath!=null && tmppath.length>=IMPACT.getDH())
		// {
		// for (int j = 0; j < IMPACT.getDH()+1; j++)
		// {
		// LayerNode node = (LayerNode)tmppath[j];
		// if(node.getUserObject()==null)
		// continue;
		// Vector<Assignment> assignments = node.getNodeAssignments();
		// for (int k = 0; k < assignments.size(); k++)
		// {
		// Assignment assignment = assignments.get(k);
		// TaskSimulator taskSimulator = assignment.getTask();
		// System.out.print(" "+taskSimulator.getTaskDataModel().getTaskId());
		// }
		// }
		// }
		// System.out.println();

		ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
		for (int i = 0; i < sr; i++) {
			// System.out.print("\t\t\tSample "+(i+1)+"/"+sr+" sequence :");
			TreeNode[] path = paths.get(i);
			Calendar currentTime = Calendar.getInstance();
			currentTime.setTimeInMillis(timeNow.getTimeInMillis());
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				if (node.getUserObject() == null)
					continue;
				Vector<Assignment> assignments = node.getNodeAssignments();
				for (int k = 0; k < assignments.size(); k++) {
					Assignment assignment = assignments.get(k);
					TaskSimulator taskSimulator = assignment.getTask();
					ResourceSimulator resourceSimulator = assignment.getResource();
					double partialCost = manualHelper.getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, currentTime, assignments)
						+ manualHelper.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, assignments)
						+ manualHelper.getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, currentTime, assignments);

					Calendar assignmentTime = Calendar.getInstance();
					assignmentTime.setTimeInMillis(currentTime.getTimeInMillis());
					manualHelper.addManualAssignment(new AssignmentDataModel(taskSimulator.getTaskDataModel(), resourceSimulator.getResourceDataModel(), assignmentTime, (long) partialCost, false, null));

					currentTime.setTimeInMillis(currentTime.getTimeInMillis() + (long) partialCost);
					manualHelper.setManualTimeNow(currentTime);
					costSum += partialCost;
					// System.out.print(" "+taskSimulator.getTaskDataModel().getTaskId());
				}
			}
			// System.out.print("\n");
		}
		// System.out.println("\t\tSR : "+ sr
		// +" ( IMPACT.SR = "+IMPACT.getSR()+")" );

		return costSum / (double) sr;
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
		return "OPERATION TIME";
	}

}
