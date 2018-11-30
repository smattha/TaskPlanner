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
import Elements.resources.ThomasResource;
import eu.robopartner.ps.planner.planninginputmodel.DATE;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import planning.scheduler.algorithms.AbstractAlgorithm;
import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Pick;
import Plan.Process.Task.Operations.Place;

/**
 * The purpose of this class is to dynamically prepare the input for the use of
 * the {@link LayoutScheduler} For a demo run.
 * 
 * @author Spyros
 *
 */
public class DemoPlanningGenerator3D {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DemoPlanningGenerator3D.class);
	public static double MAX_INVESTMENT_COST = 1000000.0;
	public static double MIN_INVESTMENT_COST = 1000000.0;
	public static double MIN_RESOURCE_FLOOR_SPACE = 0;
	public static double MIN_FLOORSPACE_TASKS = 0;
	private static final double DISCRETIZATION = 1000.0;
	private List<JSONObject> tasksWithProperties;

	/**
	 * @return the tasksWithProperties
	 */
	public List<JSONObject> getTasksWithProperties() {
		return tasksWithProperties;
	}

	/**
	 * @param tasksWithProperties the tasksWithProperties to set
	 */
	public void setTasksWithProperties(List<JSONObject> tasksWithProperties) {
		this.tasksWithProperties = tasksWithProperties;
	}

	/**
	 * @return the tasksWithTables
	 */
	public List<JSONObject> getTasksWithTables() {
		return tasksWithTables;
	}

	/**
	 * @param tasksWithTables the tasksWithTables to set
	 */
	public void setTasksWithTables(List<JSONObject> tasksWithTables) {
		this.tasksWithTables = tasksWithTables;
	}

	/**
	 * @return the cellDimensions
	 */
	public List<JSONObject> getCellDimensions() {
		return cellDimensions;
	}

	/**
	 * @param cellDimensions the cellDimensions to set
	 */
	public void setCellDimensions(List<JSONObject> cellDimensions) {
		this.cellDimensions = cellDimensions;
	}

	private List<JSONObject> tasksWithTables;
	private List<JSONObject> cellDimensions;

	public PROPERTIES getPROPERTIES(String resourceType, double payloadKg, double costEuro, double floorSpaceM2,
			double speedMMS, int width, int lenght, double reachability, double height, String shape) {
		ObjectFactory myObjectFactory = new ObjectFactory();

		PROPERTY isRobotProperty = myObjectFactory.createPROPERTY();
		isRobotProperty.setNAME(MapToResourcesAndTasks.TYPE_PROPERTY_NAME);
		isRobotProperty.setVALUE(resourceType);

		PROPERTIES resourceProperties = myObjectFactory.createPROPERTIES();

		PROPERTY resourceProperty1 = myObjectFactory.createPROPERTY();
		resourceProperty1.setNAME("Payload (kg)");
		resourceProperty1.setVALUE(payloadKg + "");

		PROPERTY resourceProperty2 = myObjectFactory.createPROPERTY();
		resourceProperty2.setNAME("Cost (euro)");
		resourceProperty2.setVALUE(costEuro + "");
		// MAX_INVESTMENT_COST += costEuro;
		if (MIN_INVESTMENT_COST > costEuro) {
			MIN_INVESTMENT_COST = costEuro;
		}

		PROPERTY resourceProperty3 = myObjectFactory.createPROPERTY();
		resourceProperty3.setNAME("Floor Space (m2)");
		resourceProperty3.setVALUE(floorSpaceM2 + "");
		MIN_RESOURCE_FLOOR_SPACE += floorSpaceM2;

		PROPERTY resourceProperty5 = myObjectFactory.createPROPERTY();
		resourceProperty5.setNAME("Speed (mm/s)");
		resourceProperty5.setVALUE(speedMMS + "");

		PROPERTY property = myObjectFactory.createPROPERTY();
		property.setNAME(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME);
		property.setVALUE(width + "");

		PROPERTY property1 = myObjectFactory.createPROPERTY();
		property1.setNAME(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME);
		property1.setVALUE(lenght + "");

		PROPERTY propertyZHeight = myObjectFactory.createPROPERTY();
		propertyZHeight.setNAME(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME);
		propertyZHeight.setVALUE(height + "");

		PROPERTY property2 = myObjectFactory.createPROPERTY();
		property2.setNAME(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME);
		property2.setVALUE(reachability + "");

		PROPERTY propertyShape = myObjectFactory.createPROPERTY();
		propertyShape.setNAME(MapToResourcesAndTasks.SHAPE_TYPE_NAME);
		propertyShape.setVALUE(shape);

		resourceProperties.getPROPERTY().add(resourceProperty1);
		resourceProperties.getPROPERTY().add(resourceProperty2);
		resourceProperties.getPROPERTY().add(resourceProperty3);
		resourceProperties.getPROPERTY().add(resourceProperty5);
		resourceProperties.getPROPERTY().add(property);
		resourceProperties.getPROPERTY().add(property1);
		resourceProperties.getPROPERTY().add(property2);
		resourceProperties.getPROPERTY().add(propertyZHeight);
		resourceProperties.getPROPERTY().add(isRobotProperty);
		resourceProperties.getPROPERTY().add(propertyShape);
		return resourceProperties;
	}

	/**
	 * 
	 * @return aPlanningInput
	 */
	public PLANNINGINPUT getResourceAndTasksOriginalPlanningInput() {
		String id = "";
		id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanningInput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2015, 1, 2, 2018, true);
		return aPlanningInput;
	}

	/**
	 * Thomas Testing Demo
	 * 
	 * @return
	 */
	public PLANNINGINPUT generatePlanningInput() {
		String msg = ".getDemoPlanninginput(): ";
		LOGGER.trace(msg);

		String id = null;

		id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2015, 1, 2, 2018, true);

		LOGGER.trace(msg + "Created aPlanningInput");

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////// Step 1 CREATE
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// TASKS////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS tasks = myObjectFactory.createTASKS();

		Screwdriver screw5 = new Screwdriver("screw", 5);
		Screwdriver screw10 = new Screwdriver("screw", 10);

		Place aTask = new Place("place 1", screw5);
		Place bTask = new Place("place 2", screw5);
		Place cTask = new Place("place 3", screw5);
		Place dTask = new Place("place 4", screw5);
		Place eTask = new Place("place 5", screw5);
		Place fTask = new Place("place 6", screw5);
		Place gTask = new Place("place 7", screw5);
		Place hTask = new Place("place 8", screw5);
		Place iTask = new Place("place 9", screw5);
		Place t1 = new Place("place 10", screw10);

		aTask.genImpactTask();
		bTask.genImpactTask();
		cTask.genImpactTask();
		dTask.genImpactTask();
		eTask.genImpactTask();
		fTask.genImpactTask();
		gTask.genImpactTask();
		hTask.genImpactTask();
		iTask.genImpactTask();
		t1.genImpactTask();

		tasks.getTASK().add(aTask);
		tasks.getTASK().add(bTask);
		tasks.getTASK().add(cTask);
		tasks.getTASK().add(dTask);
		tasks.getTASK().add(eTask);
		tasks.getTASK().add(fTask);
		tasks.getTASK().add(gTask);
		tasks.getTASK().add(hTask);
		tasks.getTASK().add(iTask);
		tasks.getTASK().add(t1);

		Vector<Operations> ops = new Vector<Operations>();

		ops.add(aTask);
		ops.add(aTask);
		ops.add(bTask);
		ops.add(cTask);
		ops.add(dTask);
		ops.add(eTask);
		ops.add(fTask);
		ops.add(gTask);
		ops.add(hTask);
		ops.add(iTask);
		ops.add(t1);

		LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, tasks);
		aPlanninginput.setTASKS(tasks);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////// Resources///////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// ObjectFactory myObjectFactory1 = new ObjectFactory();
		RESOURCES resources = myObjectFactory.createRESOURCES();

		ThomasResource aResource1 = new ThomasResource("Robot");
		ThomasResource aResource2 = new ThomasResource("Human");

		aResource1.addCompatibleTool(screw5);

		//aResource1.addCompatibleTool(screw5);
		
		aResource2.addCompatibleTool(screw10);

		//aResource1.gererateResource();
		//aResource2.gererateResource();

		resources.getRESOURCE().add(aResource1);
		resources.getRESOURCE().add(aResource2);

		LOGGER.trace(msg + "Created theResources");
		aPlanninginput.setRESOURCES(resources);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////// Suitable
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Resources//////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();

		aResource1.fillTasksuitableresources(tasksuitableresources, ops);
		aResource2.fillTasksuitableresources(tasksuitableresources, ops);

		LOGGER.trace(msg + "Created setTASKSUITABLERESOURCES");
		aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
		LOGGER.trace(msg + "Created Workcenters");

		DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

		LOGGER.trace(msg + "Created JObs");
		LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
				aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);

		return aPlanninginput;

	}

}