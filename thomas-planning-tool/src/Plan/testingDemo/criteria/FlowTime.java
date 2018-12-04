package testingDemo.criteria;

import java.util.Calendar;
import java.util.Random;
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

public class FlowTime extends AbstractCriterion {

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS

		int sr = paths.size();
		double value = 0;
		
		double[] a;
		a=new double[sr];
		
		double[] sum;
		sum=new double[sr];
		
		double minValue=0;
		
		
		
		double timeToCompleteSum = 0;
		double partialTimeToComplete = 0;
		
		
		ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
		for (int i = 0; i < sr; i++)
		{
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
					
					String taskName=taskSimulator.getTaskDataModel().getTaskName();
					String resourceName=resourceSimulator.getResourceDataModel().getResourceName();
					
				
					
					double v1=Simulation.simulationDemo1(resourceName,taskName);
					
					System.out.println("taskName "+taskName + "    getResource "+ resourceName + "   ");
					//minValue=minValue+v1;
					
					
					partialTimeToComplete += v1;
					
					//value=v1;
					//a[k]=v1;					
																    	
					//System.out.print("The value is:"+a[k]);
					//System.out.print("\n");
				}
				partialTimeToComplete = partialTimeToComplete / path.length;
				timeToCompleteSum += partialTimeToComplete;
			}
			 
		}
		int i;
		/*
		for(i=0;i<a.length;i++) {
			sum[i]+=a[i];
			minValue=sum[0];
			
			if (sum[i]<minValue) {
				minValue=sum[i];
			}
			
		}
		*/
		
		
		timeToCompleteSum = timeToCompleteSum / (double) sr;            
		
		return timeToCompleteSum;
		
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
		return "OPERTION TIME";
	}	
}