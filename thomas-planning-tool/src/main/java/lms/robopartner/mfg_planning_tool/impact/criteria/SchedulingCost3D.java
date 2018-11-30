package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;//PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import javafx.geometry.Point3D;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.mfg_planning_tool.TaskSchedulerForLayout;
import lms.robopartner.mfg_planning_tool.impact.interfaces.AverageRawCriteriaValuesDecisionPointAssignmentsConsumer;
import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class SchedulingCost3D extends AbstractCriterion {
    private int internalDH= 1;
    private int internalMNA = 100;
    private int internalSR = 10;
    private PLANNINGINPUT aPlanninginput;
    private Document plannignInputDocument;
    
	public SchedulingCost3D(int internalDH, int internalMNA, int internalSR, PLANNINGINPUT aPlanninginput, Document plannignInputDocument) {
        super();
        this.internalDH = internalDH;
        this.internalMNA = internalMNA;
        this.internalSR = internalSR;
        this.aPlanninginput = aPlanninginput;
        this.plannignInputDocument = plannignInputDocument;
    }
	   public SchedulingCost3D() {
	        super();
	
	    }


   private static org.slf4j.Logger logger = LoggerFactory.getLogger(SchedulingCost3D.class);
	private static final String CRITERION_NAME = "SCHEDULING COST";
	
	
	/* (non-Javadoc)
	 * @see planning.scheduler.algorithms.impact.criteria.AbstractCriterion#getValue(java.util.Vector, planning.scheduler.simulation.interfaces.PlanHelperInterface, java.util.Calendar)
	 */  
	@Override
	public double getValue(Vector<TreeNode[]> paths,
		PlanHelperInterface helper, Calendar timeNow) {
		// INITIALIZE A NEW PLAN IN ORDER TO MAKE THE ASSIGNMENTS
		String msg = ".getValue(): ";
		int sr = paths.size();
		double theSumOfAllCostsForThisLayout=0;
		int srIndex = 0;// for logging purposes
		double schedulingCostSum = 0;
		boolean totalFoundPlacement=false;
		
		//This Map contains the locations that should be used for the next impact run.
		//Resource ID or Part Id - Location
	
	
		for (TreeNode[] path : paths) {
			srIndex++;// increase starting from 0.
			logger.trace(msg + "Sample " + srIndex + " of " + sr + ".");
			 Map<String,Point3D> resourcesAndPartsMapingForSR=new HashMap<String, Point3D>(); 
			 
			for (TreeNode treeNode : path) {
			   
				// Cast to layer node
				LayerNode layerNode = (LayerNode) treeNode;
				//Getting all locations for the next impact run
				for (Assignment assignment : layerNode.getNodeAssignments()) {
				    //The resource or part
					TaskDataModel taskDataModel = assignment.getTask().getTaskDataModel();
					//the location
					ResourceDataModel resourceDataModel=assignment.getResource().getResourceDataModel();

					resourcesAndPartsMapingForSR.put(taskDataModel.getTaskId(), MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
					logger.trace(msg+"(Sample) PartOrResourceID="+taskDataModel.getTaskId()+" Location="+MapParameters.getLocationFromID(resourceDataModel.getResourceId()));
				}
			}//end for all nodes
				
			for(AssignmentDataModel anAssignment:helper.getAssignments()){
			    resourcesAndPartsMapingForSR.put(anAssignment.getTaskDataModel().getTaskId(), MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));
			    logger.trace(msg+"(Assignment) PartOrResourceID="+anAssignment.getTaskDataModel().getTaskId()+" Location="+MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));
			    
			}
			        
			
			     //Check if all resources and tasks in the input have been assigned
			      //if one has not been assigned return max value.
			        boolean foundUnassignedResource=false;
			        for(RESOURCE aResource : this.aPlanninginput.getRESOURCES().getRESOURCE()){
			            if(resourcesAndPartsMapingForSR.containsKey(aResource.getId())){
			            foundUnassignedResource=true;
			            break;}
			        }
			        if(!foundUnassignedResource){
			            
			        for(TASK aTask: this.aPlanninginput.getTASKS().getTASK()){
                        if(resourcesAndPartsMapingForSR.containsKey(aTask.getId())){
                        foundUnassignedResource=true;
                        break;}
                    }}

			        if(!foundUnassignedResource){
			            
			            Document planningInputDocument = this.plannignInputDocument;
			            //Create planning impact.
			            TaskSchedulerForLayout taskSchedulerForGivenLayout= new TaskSchedulerForLayout(resourcesAndPartsMapingForSR, this.internalDH, this.internalMNA, this.internalSR);
			            taskSchedulerForGivenLayout.evaluate(planningInputDocument);
			            
			            AverageRawCriteriaValuesDecisionPointAssignmentsConsumer criteriaValueConsumer=taskSchedulerForGivenLayout.getCriteriaConsumer();
			            
			    theSumOfAllCostsForThisLayout=criteriaValueConsumer.getAveragedSumCriteriaValues();//assign with criteria read values
			    logger.trace(msg+" SR:"+srIndex+" DecisionPoint3Ds:"+criteriaValueConsumer.getDecisionPoints()+" SumOfCriteria:"+criteriaValueConsumer.getAveragedSumCriteriaValues()+" AverageCriteria:"+criteriaValueConsumer.getAverageCriteriaValues());
			        }else{
			        theSumOfAllCostsForThisLayout=Double.MAX_VALUE/(double)sr;}    
			    schedulingCostSum+=theSumOfAllCostsForThisLayout;
				
			

		}// end for all paths

		// Normalize with SR.
		double resultCost = (double) schedulingCostSum / (double) sr;

		logger.trace(msg + SchedulingCost3D.CRITERION_NAME + "=" + resultCost	+ ". Solution found="+totalFoundPlacement);
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
		return SchedulingCost3D.CRITERION_NAME;
	}

}
