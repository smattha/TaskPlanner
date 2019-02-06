package lms.thomas.planning.criteria;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import javafx.geometry.Point3D;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.mfg_planning_tool.impact.criteria.TimeToComplete;
import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.ManualPlanHelperInterface;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;
import lms.thomas.*;
import lms.thomas.planning.Simulation;


public class FlowTime extends AbstractCriterion {

    static int counter=0;
	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		


	    counter++;
		

		String msg = ".getValue(): ";
		int sr = paths.size();
		
		
		//System.out.println("                                 Counter        " +counter" sr:"+sr);
		
		double partialTimeToComplete = 0;
		double value=0;		
		String alternative = "[ ";
		
		
		for ( int i = 0; i < sr; i++ ) {
			
			TreeNode[] path = paths.get(i);

			int oi=path.length;
			double timeToCompleteSum = 0;
			//System.out.println("                                 Counter        " +counter+" sr:"+sr);
			for ( int j = 0; j < path.length; j++ ) {
				LayerNode node = (LayerNode) path[j];
				
				int ass=0;
				for ( Assignment assignment : node.getNodeAssignments() ) {
					ass++;
					
					String taskName=assignment.getTask().getTaskDataModel().getTaskName();
					String resourceName=assignment.getResource().getResourceDataModel().getResourceName();
					
					System.out.println("                      Counter        " +counter+" sr "+sr+" pathLengh "+path.length+ " assi "+ ass+" res "+ resourceName+" task "+ taskName);
					
					
					//double partialTimeToCompleteTemp = 0;
					double partialTimeToCompleteTemp[] = {0,0};

					
				    partialTimeToCompleteTemp=Simulation.simulationDemo1(resourceName, taskName);
				    //System.out.println(""+partialTimeToCompleteTemp);
					
					partialTimeToComplete += partialTimeToCompleteTemp[0];

					

				}
				//partialTimeToComplete = partialTimeToComplete;// path.length;
				timeToCompleteSum += partialTimeToComplete;
				partialTimeToComplete = 0;
			}
			value =value+timeToCompleteSum;
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