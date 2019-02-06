package lms.thomas.planning.criteria;

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
import lms.thomas.*;//.Simulation;
import lms.thomas.planning.Simulation;

public class Payload extends AbstractCriterion {
	
	static int counter=0;

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Payload.class);
	private static final String CRITERION_NAME = "PAYLOAD";

	private FileWriter fileWriter;
	private int alternativeCounter = 0;

	//public Payload() {
	//	super();
	//	try {
	//		fileWriter = new FileWriter("C:\\Payload.doc");
	//	} catch (IOException e) {
	//		// logger.error(ExceptionUtils.getStackTrace(e));
	//	}
	//}


	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
			// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
			
		    counter++;

			//Random rand = new Random();
			//if (2 == 1) {
			// num = rand.nextDouble();

			//}
		    String msg = ".getValue(): ";
			int sr = paths.size();
			double value = 0;
			double partialHumanWeight = 0;
			String alternative = "[ ";


			//ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
			for (int i = 0; i < sr; i++) {
				TreeNode[] path = paths.get(i);
				//Calendar currentTime = Calendar.getInstance();
				//currentTime.setTimeInMillis(timeNow.getTimeInMillis());
				
				int oi=path.length;
				double HumanWeightSum = 0;
				
				for (int j = 0; j < path.length; j++) {
					LayerNode node = (LayerNode) path[j];
					//if (node.getUserObject() == null)
						//continue;
					//Vector<Assignment> assignments = node.getNodeAssignments();
					//for (int k = 0; k < assignments.size(); k++) {
						//Assignment assignment = assignments.get(k);
						//TaskSimulator taskSimulator = assignment.getTask();
						//ResourceSimulator resourceSimulator = assignment.getResource();
						
						//String taskName=taskSimulator.getTaskDataModel().getTaskName();
						//String resourceName=resourceSimulator.getResourceDataModel().getResourceName();
					int ass=0;
					for ( Assignment assignment : node.getNodeAssignments() ) {
						ass++;
						
						String taskName=assignment.getTask().getTaskDataModel().getTaskName();
						String resourceName=assignment.getResource().getResourceDataModel().getResourceName();
						
					 //System.out.println("taskName "+taskName + "    getResource "+ resourceName);
					 System.out.println("                      Counter        " +counter+" sr "+sr+" pathLengh "+path.length+ " assi "+ ass+" res "+ resourceName+" task "+ taskName);
						
					 //double partialHumanWeightTemp = 0;
					 double partialHumanWeightTemp[] = {0,0};

												
					 partialHumanWeightTemp=Simulation.simulationDemo1(resourceName, taskName);
						
					 partialHumanWeight += partialHumanWeightTemp[0];											
				}
					
				HumanWeightSum += partialHumanWeight;
				partialHumanWeight = 0;
			}
			value =value+HumanWeightSum;
		}
			
		System.out.println("                                 Counter        " +counter+"                               "+value);
			
		if(value==0)
		{
			return 100000;
		}
			
		return value/(sr*paths.get(0).length);

		//return value / (double) sr;
	}
		
	private void time(double v1) {
			// TODO Auto-generated method stub
			
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
