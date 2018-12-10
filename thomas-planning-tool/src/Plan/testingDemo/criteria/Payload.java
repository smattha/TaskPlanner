package testingDemo.criteria;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.ManualPlanHelperInterface;
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


		public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
			// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
			Random rand = new Random();
			if (2 == 1) {
				double num = rand.nextDouble();

			}
			int sr = paths.size();
			double value = 0;


			ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
			for (int i = 0; i < sr; i++) {
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
						
						String taskName=taskSimulator.getTaskDataModel().getTaskName();
						String resourceName=resourceSimulator.getResourceDataModel().getResourceName();
						
						System.out.println("taskName "+taskName + "    getResource "+ resourceName);
						
						
						
						
						

					}
				}
				
			}


			return value / (double) sr;
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
