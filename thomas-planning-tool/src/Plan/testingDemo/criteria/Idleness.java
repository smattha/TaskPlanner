package testingDemo.criteria;

import java.util.Calendar;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.ManualPlanHelperInterface;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;
import testingDemo.Simulation;


public class Idleness extends AbstractCriterion {
	
	static int counter=0;
	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
		
		
		counter++;
		//Random rand = new Random();
		//if (2 == 1) {
			//double num = rand.nextDouble();
		//}		
		String msg = ".getValue(): ";
		int sr = paths.size();
		
		
		double partialIdleTime = 0;
		double value=0;
		String alternative = "[ ";


		//ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
		for (int i = 0; i < sr; i++) {
			// System.out.print("\t\t\tSample "+(i+1)+"/"+sr+" sequence :");
			TreeNode[] path = paths.get(i);
			//Calendar currentTime = Calendar.getInstance();
			//currentTime.setTimeInMillis(timeNow.getTimeInMillis());
			
			int oi=path.length;
			double IdleTimeSum = 0;
			
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				
				int ass=0;
				for ( Assignment assignment : node.getNodeAssignments() ) {
					ass++;
				//if (node.getUserObject() == null)
					//continue;
				//Vector<Assignment> assignments = node.getNodeAssignments();
				//for (int k = 0; k < assignments.size(); k++) {
					//Assignment assignment = assignments.get(k);
					//TaskSimulator taskSimulator = assignment.getTask();
					//ResourceSimulator resourceSimulator = assignment.getResource();
					
					//String taskName=taskSimulator.getTaskDataModel().getTaskName();
					//String resourceName=resourceSimulator.getResourceDataModel().getResourceName();
					
					String taskName=assignment.getTask().getTaskDataModel().getTaskName();
					String resourceName=assignment.getResource().getResourceDataModel().getResourceName();
					
					System.out.println("                      Counter        " +counter+" sr "+sr+" pathLengh "+path.length+ " assi "+ ass+" res "+ resourceName+" task "+ taskName);
					
					
                    //double partialIdleTimeTemp=0;
                    double partialIdleTimeTemp[] = {0,0};					
					
                    partialIdleTimeTemp=Simulation.simulationDemo1(resourceName, taskName);
					
					partialIdleTime += partialIdleTimeTemp[0];

					

				}
				//partialTimeToComplete = partialTimeToComplete;// path.length;
				IdleTimeSum += partialIdleTime;
				partialIdleTime = 0;
			}
			value =value+IdleTimeSum;
		}

		//timeToCompleteSum = timeToCompleteSum / (double) sr;
		
		
		System.out.println("                                 Counter        " +counter+"                               "+value);
		
		
		if(value==0)
		{
			return 100000;
		}
		
		return value/(sr*paths.get(0).length);
		
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
		return false;
	}

	@Override
	public String getCriterionName() {
		return "OPERATION TIME";
	}

}
