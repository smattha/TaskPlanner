/**
 * 
 */
package testingDemo;

import java.math.BigInteger;
import java.util.List;
import java.util.Vector;

import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import Elements.Tools.Screwdriver;
import Elements.resources.Arm1;
import Elements.resources.Arm2;
import Elements.resources.ThomasResource;
import eu.robopartner.ps.planner.planninginputmodel.DATE;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import planning.scheduler.algorithms.AbstractAlgorithm;
import Plan.WorkingArea;
import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Pick;
import Plan.Process.Task.Operations.Place;
import Plan.Process.Task.Operations.Actions.Parameters.Position;

/**
 * The purpose of this class is to dynamically prepare the input for the use of
 * the {@link LayoutScheduler} For a demo run.
 * 
 * @author Spyros
 *
 */
public class ThomasTestingDemo {

     /**
	 * Thomas Testing Demo
	 * 
	 * @return
	 */
	public PLANNINGINPUT generatePlanningInput() {

		String id =  IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2015, 1, 2, 2018, true);

		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS tasks = myObjectFactory.createTASKS();
		TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints = myObjectFactory.createTASKPRECEDENCECONSTRAINTS();
		RESOURCES resources = myObjectFactory.createRESOURCES();

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Screwdriver screw5 = new Screwdriver("screw", 5);
		Screwdriver screw10 = new Screwdriver("screw", 10);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		WorkingArea w1=new WorkingArea("Station 1");
		WorkingArea w2=new WorkingArea("Station 2");
		
		
		Position data[]=new Position[4];
	   	
    	Position pose=new Position(1.0,2.0,3.0,4.0,5.0,6.0,7.0);
    	
    	data[Place.navigatePoseId]=pose;
    	data[Place.movePoseId]=pose;
    	data[Place.reactPoseId]=pose;
    	data[Place.releasePoseId]=pose;
		
		Place aTask = new Place("place 1", screw10,w1,tasks,theTaskprecedenceconstraints,null,"",data);
		Place bTask = new Place("place 2", screw5,w1,tasks,theTaskprecedenceconstraints,aTask,"",data);
		Place cTask = new Place("place 3", screw5,w1,tasks,theTaskprecedenceconstraints,bTask,"",data);
		Place dTask = new Place("place 4", screw5,w1,tasks,theTaskprecedenceconstraints,cTask,"",data);
		Place eTask = new Place("place 5", screw5,w1,tasks,theTaskprecedenceconstraints,dTask,"",data);
		Place fTask = new Place("place 6", screw5,w1,tasks,theTaskprecedenceconstraints,eTask,"",data);
		Place gTask = new Place("place 7", screw5,w2,tasks,theTaskprecedenceconstraints,fTask,"",data);
		Place hTask = new Place("place 8", screw5,w1,tasks,theTaskprecedenceconstraints,gTask,"",data);
		Place t1 = new Place("place 10", screw10,w1,tasks,theTaskprecedenceconstraints,gTask,"",data);
		Place iTask = new Place("place 9", screw5,w1,tasks,theTaskprecedenceconstraints,t1,"",data);
	


		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////// Resources///////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		Arm1 aResource1 = new Arm1(IDGenerator.getNewID(),resources,"Robot1");
		
		Arm2 aResource3 = new Arm2(IDGenerator.getNewID(),resources,"Robot2");
		
		Arm2 aResource4 = new Arm2(IDGenerator.getNewID(),resources,"Robot3");
		
	    ThomasResource aResource2 = new ThomasResource(IDGenerator.getNewID(),resources, "Human");

		aResource1.addCompatibleTool(screw5);
		aResource3.addCompatibleTool(screw10);
		aResource2.addCompatibleTool(screw5);
		aResource4.addCompatibleTool(screw10);


		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();

		aResource1.fillTasksuitableresources(tasksuitableresources, tasks);
		aResource2.fillTasksuitableresources(tasksuitableresources, tasks);
		aResource3.fillTasksuitableresources(tasksuitableresources, tasks);

		
		
		aPlanninginput.setTASKPRECEDENCECONSTRAINTS(theTaskprecedenceconstraints);
		
		aPlanninginput.setTASKS(tasks);

		aPlanninginput.setRESOURCES(resources);
		
		aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
		
		
		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
		
		DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);
		LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
				aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);

		return aPlanninginput;

	}

}