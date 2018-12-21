package testingDemo;


import java.math.BigInteger;
import java.util.List;
import java.util.Vector;

import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import Elements.Tools.BarcodeScanner;
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
import xmlParser.CreateXmlFileDemo;
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
public class xmlDemo {

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
		BarcodeScanner scanner  = new BarcodeScanner("scanner");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		WorkingArea w1=new WorkingArea("Station 1");
		//WorkingArea w2=new WorkingArea("Station 2");
		
		Position data[]=new Position[4];
	   	
    	Position pose=new Position(1.0,2.0,3.0,4.0,5.0,6.0,7.0);
    	
    	data[Place.navigatePoseId]=pose;
    	data[Place.movePoseId]=pose;
    	data[Place.reactPoseId]=pose;
    	data[Place.releasePoseId]=pose;
		
		Place pickDumper = new Place("pickDamper", screw10,w1,tasks,theTaskprecedenceconstraints,null,"description",data);
		
		Place readBarcode = new Place("readBarcode", screw5,w1,tasks,theTaskprecedenceconstraints,pickDumper,"description",data);
		Place placeDamper = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,readBarcode,"description",data);
		Place placeDamper1 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper,"description",data);
		
		Place placeDamper2 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper1,"description",data);
		Place placeDamper3 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper2,"description",data);
		Place placeDamper4 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper3,"description",data);
		Place placeDamper5 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper4,"description",data);
		Place placeDamper6 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper5,"description",data);
		Place placeDamper7 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper6,"description",data);
		Place placeDamper8 = new Place("placeDamper", screw5,w1,tasks,theTaskprecedenceconstraints,placeDamper7,"description",data);
		

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////// Resources///////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		Arm1 robot1 = new Arm1(IDGenerator.getNewID(),resources,"Robot1");
		Arm2 robot2 = new Arm2(IDGenerator.getNewID(),resources,"Robot2");
		
		//Arm2 aResource4 = new Arm2(IDGenerator.getNewID(),resources,"Robot3");
		
	    ThomasResource human = new ThomasResource(IDGenerator.getNewID(),resources, "Human");

		robot1.addCompatibleTool(screw5);
		robot1.addCompatibleTool(scanner);
		
		robot2.addCompatibleTool(screw10);
		robot2.addCompatibleTool(scanner);
		
		human.addCompatibleTool(screw5);
		human.addCompatibleTool(scanner);


		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();

		robot1.fillTasksuitableresources(tasksuitableresources, tasks);
		human.fillTasksuitableresources(tasksuitableresources, tasks);
		robot2.fillTasksuitableresources(tasksuitableresources, tasks);

		
		
		aPlanninginput.setTASKPRECEDENCECONSTRAINTS(theTaskprecedenceconstraints);
		
		aPlanninginput.setTASKS(tasks);

		aPlanninginput.setRESOURCES(resources);
		
		aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
		
		
		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
		
		DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);
		LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
				aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
		
		
		CreateXmlFileDemo doc= new CreateXmlFileDemo();
		
		pickDumper.convert2XmlElement(doc);

		
		
		doc.store("C:\\Users\\smatt\\Desktop\\thomas\\xmlfile1.xml");
		return aPlanninginput;

	}

	public static void main(String argv[]) {
		xmlDemo d1=new xmlDemo();
		d1.generatePlanningInput();	
		}
	}
