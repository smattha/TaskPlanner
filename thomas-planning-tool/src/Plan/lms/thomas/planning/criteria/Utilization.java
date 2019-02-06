package lms.thomas.planning.criteria;

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
import lms.thomas.*;//.Simulation;
import lms.thomas.planning.Simulation;

public class Utilization extends AbstractCriterion {
	private String resourceName;
	public Utilization(String name )
	{
		this.resourceName=name;
	}
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
		double partialTimeToComplete = 0;
		double partialUtilizationTime1=0;
		//double partialUtilizationTime2=0;
		//double partialUtilizationTime3=0;		
		double value = 0;
		double value1 = 0;
		//double value2 = 0;
		//double value3 = 0;

		//ManualPlanHelperInterface manualHelper = helper.getManualPlanningHelperInterface();
		for (int i = 0; i < sr; i++) {
			TreeNode[] path = paths.get(i);
			//Calendar currentTime = Calendar.getInstance();
			//currentTime.setTimeInMillis(timeNow.getTimeInMillis());
			int oi=path.length;
			double UtilizationTimeSum = 0;
			double timeToCompleteSum = 0;
			
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
					
					//System.out.println("taskName "+taskName + "    getResource "+ resourceName);
				     int ass=0;
				     for ( Assignment assignment : node.getNodeAssignments() ) {
					     ass++;
					
					     String taskName=assignment.getTask().getTaskDataModel().getTaskName();
					     String resourceName=assignment.getResource().getResourceDataModel().getResourceName();
					
				         System.out.println("                      Counter        " +counter+" sr "+sr+" pathLengh "+path.length+ " assi "+ ass+" res "+ resourceName+" task "+ taskName);
					
				         //double partialUtilizationTimeTemp=0;
				         double partialUtilizationTimeTemp[]= {0,0};
				         				     				         				         
				         //partialUtilizationTimeTemp=Simulation.simulationDemo1(resourceName, taskName);		        	 	        	 

				         //if (resourceName.equals("Human")){				       
				        	
				        	 partialUtilizationTimeTemp=Simulation.simulationDemo1(resourceName, taskName);
				        	 System.out.println(""+partialUtilizationTimeTemp[0]);
				        	 System.out.println(""+partialUtilizationTimeTemp[1]);
				        	 partialTimeToComplete += partialUtilizationTimeTemp[0];
				        	 			        	 
					         //Calculate flowtime
				        	 partialUtilizationTime1 += partialUtilizationTimeTemp[1];	  	
						    
				        //}

				        // else if(resourceName.equals("Robot1")) {
				        	 
				        //	 partialUtilizationTimeTemp=Simulation.simulationDemo1(resourceName, taskName);
				        //	 System.out.println(""+partialUtilizationTimeTemp[0]);
				        //	 System.out.println(""+partialUtilizationTimeTemp[1]);
				        //	 partialTimeToComplete += partialUtilizationTimeTemp[0];				        	 		        	 
				        	 
					         //Calculate flowtime
				        //	 partialUtilizationTime1 = partialUtilizationTimeTemp[1];	
				       //  } 
				         
				       //  else if(resourceName.equals("Robot2")){
				        	 
				       // 	 partialUtilizationTimeTemp=Simulation.simulationDemo1(resourceName, taskName);
				       // 	 System.out.println(""+partialUtilizationTimeTemp[0]);
				       // 	 System.out.println(""+partialUtilizationTimeTemp[1]);
				       // 	 partialTimeToComplete += partialUtilizationTimeTemp[0];	        	 		        	 
				        	 
					         //Calculate flowtime						    
				        //	 partialUtilizationTime1 = partialUtilizationTimeTemp[1];	
				       //  }      									         
				         							
					   	//partialUtilizationTime += partialUtilizationTimeTemp;	
				         
							//partialUtilizationTime1 = 0;
				}
				     timeToCompleteSum += partialTimeToComplete;
			         UtilizationTimeSum += partialUtilizationTime1;
						partialTimeToComplete = 0;		    
			}	
			value =value+timeToCompleteSum;
			value1 =value1+UtilizationTimeSum;
			//value2 =value2+partialUtilizationTime2;
			//value3 =value3+partialUtilizationTime3;
		}
		 
		
		System.out.println("                                 Counter        " +counter+"                               "+value1/value);


		if(value1==0)
		{
			return 100000;
		}
		//System.out.println("hiiiii "+value1);
		return (value1/value)/(sr*paths.get(0).length);
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