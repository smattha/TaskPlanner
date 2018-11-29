/**
 * 
 */
package lms.robopartner.mfg_planning_tool;

import java.math.BigInteger;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
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

import Plan.Process.Task.Operations.Pick;

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

	public PLANNINGINPUT getPlanninginputFromPSinput() {
		/*
		 * String msg = ".getPlanninginputFromPSinput() "; ObjectFactory myObjectFactory
		 * = new ObjectFactory(); String planningIpnutID = IDGenerator.getNewID() + "";
		 * PLANNINGINPUT aPlanninginput =
		 * MapToResourcesAndTasks.getPlanningInput(planningIpnutID, 1, 1, 2015, 1, 2,
		 * 2018, true); RESOURCES resources = myObjectFactory.createRESOURCES(); TASKS
		 * tasks = myObjectFactory.createTASKS(); TASKSUITABLERESOURCES
		 * taskSuitableResources = myObjectFactory.createTASKSUITABLERESOURCES();
		 * 
		 * MapList<TASK, String> listTasks = Maps.mapList(); Map<String, RESOURCE>
		 * listResources = new HashMap<String, RESOURCE>();
		 * 
		 * for ( JSONObject jsonObject : tasksWithProperties ) { BigInteger id =
		 * IDGenerator.getNewID(); if (
		 * jsonObject.getString(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY).
		 * equals(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY_PART) ||
		 * jsonObject.getString(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY).
		 * equals(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY_TABLE) ) { TASK task
		 * = MapToResourcesAndTasks.getTask(jsonObject.getString(JsonPropertiesHelper.
		 * DESCRIPTION), jsonObject.getString(JsonPropertiesHelper.LINKED_RESOURCE) +
		 * " " + id, id + "", (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.WIDTH) / DISCRETIZATION),
		 * (int) Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.LENGTH) /
		 * DISCRETIZATION), (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.REACHABILITY) /
		 * DISCRETIZATION), jsonObject.getDouble(JsonPropertiesHelper.WEIGHT),
		 * jsonObject.getString(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY),
		 * false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE); String[] taskSuitRes =
		 * jsonObject.getString(JsonPropertiesHelper.SUITABLE_RESOURCE).split("@"); for
		 * ( String suitableResource : taskSuitRes ) listTasks.add(task,
		 * suitableResource); tasks.getTASK().add(task); } else if (
		 * jsonObject.getString(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY).
		 * equals(JsonPropertiesHelper.PART_TABLE_RESOURCE_PROPERTY_RESOURCE) ) { if (
		 * jsonObject.getString(JsonPropertiesHelper.SUITABLE_RESOURCE).equals(
		 * JsonPropertiesHelper.SUITABLE_RESOURCES_HUMAN) ) { PROPERTIES properties =
		 * getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN,
		 * jsonObject.getDouble(JsonPropertiesHelper.WEIGHT), 200.0,
		 * Math.ceil((jsonObject.getDouble(JsonPropertiesHelper.WIDTH) *
		 * jsonObject.getDouble(JsonPropertiesHelper.LENGTH)) /
		 * (Math.pow(DISCRETIZATION, 2))),
		 * (jsonObject.getDouble(JsonPropertiesHelper.SPEED)), (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.WIDTH) / DISCRETIZATION),
		 * (int) Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.LENGTH) /
		 * DISCRETIZATION), (jsonObject.getDouble(JsonPropertiesHelper.REACHABILITY) /
		 * DISCRETIZATION), (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.HEIGHT) /
		 * DISCRETIZATION), MapToResourcesAndTasks.SHAPE_REACTANGLE); RESOURCE resource
		 * = MapToResourcesAndTasks.getResource("Human" + "t_" + id,
		 * jsonObject.getString(JsonPropertiesHelper.LINKED_RESOURCE) + " " + id, "t_" +
		 * id + "", properties);
		 * listResources.put(jsonObject.getString(JsonPropertiesHelper.SUITABLE_RESOURCE
		 * ), resource); resources.getRESOURCE().add(resource); } else { PROPERTIES
		 * properties = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT,
		 * jsonObject.getDouble(JsonPropertiesHelper.WEIGHT), 20000.0,
		 * Math.ceil((jsonObject.getDouble(JsonPropertiesHelper.WIDTH) *
		 * jsonObject.getDouble(JsonPropertiesHelper.LENGTH)) /
		 * (Math.pow(DISCRETIZATION, 2))),
		 * (jsonObject.getDouble(JsonPropertiesHelper.SPEED)), (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.WIDTH) / DISCRETIZATION),
		 * (int) Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.LENGTH) /
		 * DISCRETIZATION), (jsonObject.getDouble(JsonPropertiesHelper.REACHABILITY) /
		 * DISCRETIZATION), (int)
		 * Math.ceil(jsonObject.getDouble(JsonPropertiesHelper.HEIGHT) /
		 * DISCRETIZATION), MapToResourcesAndTasks.SHAPE_REACTANGLE); RESOURCE resource
		 * = MapToResourcesAndTasks.getResource("Robot" + "t_" + id,
		 * jsonObject.getString(JsonPropertiesHelper.LINKED_RESOURCE) + " " + id, "t_" +
		 * id + "", properties);
		 * listResources.put(jsonObject.getString(JsonPropertiesHelper.SUITABLE_RESOURCE
		 * ), resource); resources.getRESOURCE().add(resource); } }
		 * 
		 * } for ( TASK entry : listTasks.keySet() ) { List<String> list =
		 * listTasks.getList(entry); for ( String resource : list ) {
		 * TASKSUITABLERESOURCE atasksuitableresource =
		 * MapToResourcesAndTasks.getTaskSuitableResource(listResources.get(resource),
		 * entry, LayoutPlanningInputGenerator.SETUP_CODE,
		 * LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
		 * atasksuitableresource.setPROPERTIES(entry.getPROPERTIES());
		 * taskSuitableResources.getTASKSUITABLERESOURCE().add(atasksuitableresource); }
		 * } LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput,
		 * tasks); aPlanninginput.setTASKS(tasks); aPlanninginput.setTASKS(tasks);
		 * aPlanninginput.setRESOURCES(resources);
		 * aPlanninginput.setTASKSUITABLERESOURCES(taskSuitableResources);
		 * LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources,
		 * AbstractAlgorithm.MULTICRITERIA); LOGGER.trace(msg + "Created Workcenters");
		 * 
		 * DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0); DATE
		 * dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);
		 * 
		 * LOGGER.trace(msg + "Created JObs");
		 * LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
		 * aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate,
		 * dueDate); double height = 0; double width = 0; for ( TASKSUITABLERESOURCE e :
		 * aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE() ) { for (
		 * PROPERTY p : e.getPROPERTIES().getPROPERTY() ) { if ( p.getNAME() == "HEIGHT"
		 * ) { height = Double.parseDouble(p.getVALUE()); } if ( p.getNAME() == "WIDTH"
		 * ) { width = Double.parseDouble(p.getVALUE()); } } MIN_FLOORSPACE_TASKS =
		 * height * width; height = 0; width = 0; }
		 */
		PLANNINGINPUT aPlanninginput = new PLANNINGINPUT();
		return aPlanninginput;
	}

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

	/*
	 * public static void main(String[] args) {
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n",
	 * "Gripper_loading_table ", (int) Math.ceil(500/DISCRETIZATION), (int)
	 * Math.ceil(900/DISCRETIZATION), (int) Math.ceil(550/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n",
	 * "Axle_Table_Loading ", (int) Math.ceil(1400/DISCRETIZATION), (int)
	 * Math.ceil(900/DISCRETIZATION), (int) Math.ceil(1200/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n",
	 * "Axle_Table_Assembly ", (int) Math.ceil(1400/DISCRETIZATION), (int)
	 * Math.ceil(900/DISCRETIZATION), (int) Math.ceil(1200/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n",
	 * "Wheel_table_loading ", (int) Math.ceil(720/DISCRETIZATION), (int)
	 * Math.ceil(400/DISCRETIZATION), (int) Math.ceil(1200/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n",
	 * "Human_Working_table ", (int) Math.ceil(350/DISCRETIZATION), (int)
	 * Math.ceil(1500/DISCRETIZATION), (int) Math.ceil(475/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n", "Robot",
	 * (int) Math.ceil(1400/DISCRETIZATION), (int) Math.ceil(2500/DISCRETIZATION),
	 * (int)(5000/DISCRETIZATION));
	 * System.out.format("task: %s width: %d heigh: %d reachability: %d\n", "Human",
	 * (int) Math.ceil(1500/DISCRETIZATION), (int) Math.ceil(1200/DISCRETIZATION)
	 * ,(int)(1000/DISCRETIZATION)); }
	 */
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
	 * use by 3D planner
	 * 
	 * @return
	 */
	public PLANNINGINPUT getResourcesAndTasksOriginalDemoPlanninginput(String scenario) {
		String msg = ".getDemoPlanninginput(): ";
		String id = null;
		LOGGER.trace(msg);
		id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2015, 1, 2, 2018, true);

		LOGGER.trace(msg + "Created aPlanningInput");
		// Get tasks

		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS tasks = myObjectFactory.createTASKS();

		BigInteger id1 = IDGenerator.getNewID();
		String newId = "t_" + id1;
		if (scenario.startsWith("tofas")) {
			// getTask(String description, String name, String id, int width, int length,
			// int reachability, double
			// weight, boolean isBasePart, int zheight)
			// put width, length, reachability in mm: int width: width/discretization-> both
			// in mm-> results: how many

			TASK aTask = MapToResourcesAndTasks.getTask("Gripper_loading_table " + newId,
					"Gripper_loading_table " + newId, newId + "", (int) Math.ceil(500 / DISCRETIZATION),
					(int) Math.ceil(1010 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION), 25.0, false, 1,
					MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK bTask = MapToResourcesAndTasks.getTask("Axle_Table_Loading " + newId, "Axle_Table_Loading " + newId,
					newId + "", (int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(1400 / DISCRETIZATION),
					(int) Math.ceil(1200 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK cTask = MapToResourcesAndTasks.getTask("Axle_Table_Assembly " + newId, "Axle_Table_Assembly " + newId,
					newId + "", (int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(1400 / DISCRETIZATION),
					(int) Math.ceil(1200 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK dTask = MapToResourcesAndTasks.getTask("Gripper_loading_table " + newId,
					"Gripper_loading_table " + newId, newId + "", (int) Math.ceil(500 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION), 1.0, false, 1,
					MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK eTask = MapToResourcesAndTasks.getTask("Gripper_loading_table " + newId,
					"Gripper_loading_table " + newId, newId + "", (int) Math.ceil(500 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION), 1.0, false, 1,
					MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK fTask = MapToResourcesAndTasks.getTask("Wheel_table_loading " + newId, "Wheel_table_loading " + newId,
					newId + "", (int) Math.ceil(1020 / DISCRETIZATION), (int) Math.ceil(400 / DISCRETIZATION),
					(int) Math.ceil(1200 / DISCRETIZATION), 1.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK gTask = MapToResourcesAndTasks.getTask("Axle_Table_Assembly " + newId, "Axle_Table_Assembly " + newId,
					newId + "", (int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(1400 / DISCRETIZATION),
					(int) Math.ceil(1200 / 500), 1.0, true, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK hTask = MapToResourcesAndTasks.getTask("Wheel_table_loading " + newId, "Wheel_table_loading " + newId,
					newId + "", (int) Math.ceil(1020 / DISCRETIZATION), (int) Math.ceil(400 / DISCRETIZATION),
					(int) Math.ceil(1200 / DISCRETIZATION), 1.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK iTask = MapToResourcesAndTasks.getTask("Human_Working_table " + newId, "Human_Working_table " + newId,
					newId + "", (int) Math.ceil(350 / DISCRETIZATION), (int) Math.ceil(1500 / DISCRETIZATION),
					(int) Math.ceil(475 / DISCRETIZATION), 1.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;

			TASK t1 = (TASK) MapToResourcesAndTasks.getTask("THOMAS PICK" + newId, "THOMAS PICK " + newId, newId + "",
					(int) Math.ceil(900 / DISCRETIZATION), (int) Math.ceil(1400 / DISCRETIZATION),
					(int) Math.ceil(1200 / 500), 1.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			Pick p1 = new Pick(t1);
			// p1=(TASK)t1;

			tasks.getTASK().add(aTask);
			tasks.getTASK().add(bTask);
			tasks.getTASK().add(cTask);
			tasks.getTASK().add(dTask);
			tasks.getTASK().add(eTask);
			tasks.getTASK().add(fTask);
			tasks.getTASK().add(gTask);
			tasks.getTASK().add(hTask);
			tasks.getTASK().add(iTask);
			// tasks.getTASK().add(jTask);
			tasks.getTASK().add(t1);
			LOGGER.trace(msg + "Created theTasks");

			LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, tasks);
			LOGGER.trace(msg + "Created Constraints");
			aPlanninginput.setTASKS(tasks);

			// Get Resources
			// ObjectFactory myObjectFactory1 = new ObjectFactory();
			RESOURCES resources = myObjectFactory.createRESOURCES();
			BigInteger id11 = IDGenerator.getNewID();
			// TODO set suitable height
			// getPROPERTIES(String resourceType, double payloadKg, double costEuro, double
			// floorSpaceM2, double
			// speedMMS, int width, int height, double reachability,int zheight)
			PROPERTIES props1 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (1000.0 * 1000.0)), (1500.0 / DISCRETIZATION),
					(int) Math.ceil(1000 / DISCRETIZATION), (int) Math.ceil(1000 / DISCRETIZATION),
					Math.ceil(3617 / DISCRETIZATION), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			// getResource(String description, String name,String id, PROPERTIES properties)
			RESOURCE aResource1 = MapToResourcesAndTasks.getResource("Robot" + "t_" + id11,
					"sm_nj_130_26 " + "t_" + id11, "t_" + id11 + "", props1);
			id11 = IDGenerator.getNewID();
			// TODO set suitable height
			PROPERTIES props2 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN, 11.0, 200.0,
					(int) Math.ceil((1500 * 1200) / (1000.0 * 1000.0)), (1500.0 / DISCRETIZATION),
					(int) Math.ceil(500 / DISCRETIZATION), (int) Math.ceil(500 / DISCRETIZATION),
					(500 / DISCRETIZATION), (int) Math.ceil(1750 / DISCRETIZATION),
					MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource2 = MapToResourcesAndTasks.getResource("Human" + "t_" + id11, "Human " + "t_" + id11,
					"t_" + id11 + "", props2);
			resources.getRESOURCE().add(aResource1);
			resources.getRESOURCE().add(aResource2);
			LOGGER.trace(msg + "Created theResources");
			aPlanninginput.setRESOURCES(resources);

			// Suitable resources
			// ObjectFactory myObjectFactory11 = new ObjectFactory();
			TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
			// atask-resource 1
			TASKSUITABLERESOURCE atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					aTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(aTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			// btask2-resourc 1
			TASKSUITABLERESOURCE btasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					bTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			btasksuitableresource.setPROPERTIES(bTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(btasksuitableresource);
			// ctask-resource 1
			TASKSUITABLERESOURCE ctasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					cTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			ctasksuitableresource.setPROPERTIES(cTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(ctasksuitableresource);
			// dtask-resource 1
			TASKSUITABLERESOURCE dtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					dTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			dtasksuitableresource.setPROPERTIES(dTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(dtasksuitableresource);
			// etask-resource 1
			TASKSUITABLERESOURCE etasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					eTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			etasksuitableresource.setPROPERTIES(eTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(etasksuitableresource);
			// ftask-resource 1
			TASKSUITABLERESOURCE ftasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					fTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			ftasksuitableresource.setPROPERTIES(fTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(ftasksuitableresource);
			// gtask-resource 1
			TASKSUITABLERESOURCE gtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					gTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			gtasksuitableresource.setPROPERTIES(gTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(gtasksuitableresource);
			// htask-resource 1
			TASKSUITABLERESOURCE htasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					hTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			htasksuitableresource.setPROPERTIES(hTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(htasksuitableresource);
			// itask-resource 1
			TASKSUITABLERESOURCE itasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource2,
					iTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			itasksuitableresource.setPROPERTIES(iTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(itasksuitableresource);
			// jtask-resource 1
			TASKSUITABLERESOURCE jtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource2, t1,
					LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			jtasksuitableresource.setPROPERTIES(t1.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(jtasksuitableresource);

			TASKSUITABLERESOURCE itasksuitableresource1 = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					iTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			itasksuitableresource1.setPROPERTIES(iTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(itasksuitableresource1);
			// jtask-resource 1
			TASKSUITABLERESOURCE jtasksuitableresource1 = MapToResourcesAndTasks.getTaskSuitableResource(aResource1, t1,
					LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			jtasksuitableresource1.setPROPERTIES(t1.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(jtasksuitableresource1);

			LOGGER.trace(msg + "Created setTASKSUITABLERESOURCES");
			aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
			LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
			LOGGER.trace(msg + "Created Workcenters");

			DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
			DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

			LOGGER.trace(msg + "Created JObs");
			LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
					aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
			double height = 0;
			double width = 0;
			for (TASKSUITABLERESOURCE e : aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE()) {
				for (PROPERTY p : e.getPROPERTIES().getPROPERTY()) {
					if (p.getNAME() == "HEIGHT") {
						height = Double.parseDouble(p.getVALUE());
					}
					if (p.getNAME() == "WIDTH") {
						width = Double.parseDouble(p.getVALUE());
					}
				}
				MIN_FLOORSPACE_TASKS = height * width;
				height = 0;
				width = 0;
			}
		} else if (scenario.equals("autorecon")) {
			// edw vazeis to input pou 8es sumfwna me auto tou tofas
			// getTask(String description, String name, String id, int width, int length,
			// int reachability, double
			// weight, boolean isBasePart, int zheight)
			// put width, length, reachability in mm: int width: width/discretization-> both
			// in mm-> results: how many

			TASK aTask = MapToResourcesAndTasks.getTask("Rack1 " + newId, "Rack1 " + newId, newId + "",
					(int) Math.ceil(1400 / DISCRETIZATION), (int) Math.ceil(700 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK bTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK cTask = MapToResourcesAndTasks.getTask("Rack2 " + newId, "Rack2 " + newId, newId + "",
					(int) Math.ceil(1300 / DISCRETIZATION), (int) Math.ceil(250 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK dTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK eTask = MapToResourcesAndTasks.getTask("Rack3 " + newId, "Rack3 " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(500 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK fTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK gTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK hTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK iTask = MapToResourcesAndTasks.getTask("Fixture " + newId, "Fixture " + newId, newId + "",
					(int) Math.ceil(1600 / DISCRETIZATION), (int) Math.ceil(550 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			TASK jTask = MapToResourcesAndTasks.getTask("Rack4 " + newId, "Rack4 " + newId, newId + "",
					(int) Math.ceil(730 / DISCRETIZATION), (int) Math.ceil(820 / DISCRETIZATION),
					(int) Math.ceil(900 / DISCRETIZATION), 11.0, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);

			tasks.getTASK().add(aTask);
			tasks.getTASK().add(bTask);
			tasks.getTASK().add(cTask);
			tasks.getTASK().add(dTask);
			tasks.getTASK().add(eTask);
			tasks.getTASK().add(fTask);
			tasks.getTASK().add(gTask);
			tasks.getTASK().add(hTask);
			tasks.getTASK().add(iTask);
			tasks.getTASK().add(jTask);
			LOGGER.trace(msg + "Created theTasks");

			LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, tasks);
			LOGGER.trace(msg + "Created Constraints");
			aPlanninginput.setTASKS(tasks);

			// Get Resources
			// ObjectFactory myObjectFactory1 = new ObjectFactory();
			RESOURCES resources = myObjectFactory.createRESOURCES();
			BigInteger id11 = IDGenerator.getNewID();
			// TODO set suitable height
			// getPROPERTIES(String resourceType, double payloadKg, double costEuro, double
			// floorSpaceM2, double
			// speedMMS, int width, int height, double reachability,int zheight)
			PROPERTIES props1 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0,
					(int) Math.ceil((140 * 250) / (1000.0 * 1000.0)), (700.0 / DISCRETIZATION),
					(int) Math.ceil(700 / DISCRETIZATION), (int) Math.ceil(700 / DISCRETIZATION),
					Math.ceil(2617 / DISCRETIZATION), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			// getResource(String description, String name,String id, PROPERTIES properties)
			RESOURCE aResource1 = MapToResourcesAndTasks.getResource("Robot" + "t_" + id11, "NJ370_1 " + "t_" + id11,
					"t_" + id11 + "", props1);
			id11 = IDGenerator.getNewID();
			// TODO set suitable height
			PROPERTIES props2 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0,
					(int) Math.ceil((140 * 250) / (1000.0 * 1000.0)), (500.0 / DISCRETIZATION),
					(int) Math.ceil(700 / DISCRETIZATION), (int) Math.ceil(700 / DISCRETIZATION),
					Math.ceil(2617 / DISCRETIZATION), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource2 = MapToResourcesAndTasks.getResource("Robot" + "t_" + id11, "NJ370_2 " + "t_" + id11,
					"t_" + id11 + "", props2);

			id11 = IDGenerator.getNewID();
			PROPERTIES props3 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (1000.0 * 1000.0)), (500.0 / DISCRETIZATION),
					(int) Math.ceil(700 / DISCRETIZATION), (int) Math.ceil(700 / DISCRETIZATION),
					Math.ceil(2617 / DISCRETIZATION), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource3 = MapToResourcesAndTasks.getResource("Robot" + "t_" + id11, "NJ130 " + "t_" + id11,
					"t_" + id11 + "", props3);

			id11 = IDGenerator.getNewID();
			PROPERTIES props4 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (1000.0 * 1000.0)), (500.0 / DISCRETIZATION),
					(int) Math.ceil(7200 / DISCRETIZATION), (int) Math.ceil(560 / DISCRETIZATION),
					Math.ceil(200 / DISCRETIZATION), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource4 = MapToResourcesAndTasks.getResource("Conveyor" + "t_" + id11,
					"Conveyor " + "t_" + id11, "t_" + id11 + "", props4);

			resources.getRESOURCE().add(aResource1);
			resources.getRESOURCE().add(aResource2);
			resources.getRESOURCE().add(aResource3);
			resources.getRESOURCE().add(aResource4);
			LOGGER.trace(msg + "Created theResources");
			aPlanninginput.setRESOURCES(resources);

			// Suitable resources
			// ObjectFactory myObjectFactory11 = new ObjectFactory();
			TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
			// atask-resource 1
			TASKSUITABLERESOURCE atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					aTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(aTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			// btask2-resourc 1
			TASKSUITABLERESOURCE btasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					bTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			btasksuitableresource.setPROPERTIES(bTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(btasksuitableresource);
			// ctask-resource 1
			TASKSUITABLERESOURCE ctasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					cTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			ctasksuitableresource.setPROPERTIES(cTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(ctasksuitableresource);
			// dtask-resource 1
			TASKSUITABLERESOURCE dtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					dTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			dtasksuitableresource.setPROPERTIES(dTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(dtasksuitableresource);
			// etask-resource 1
			TASKSUITABLERESOURCE etasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					eTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			etasksuitableresource.setPROPERTIES(eTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(etasksuitableresource);
			// ftask-resource 1
			TASKSUITABLERESOURCE ftasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					fTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			ftasksuitableresource.setPROPERTIES(fTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(ftasksuitableresource);
			// gtask-resource 1
			TASKSUITABLERESOURCE gtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource4,
					gTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			gtasksuitableresource.setPROPERTIES(gTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(gtasksuitableresource);
			// htask-resource 1
			TASKSUITABLERESOURCE htasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource2,
					hTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			htasksuitableresource.setPROPERTIES(hTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(htasksuitableresource);
			// itask-resource 1
			TASKSUITABLERESOURCE itasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource3,
					iTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			itasksuitableresource.setPROPERTIES(iTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(itasksuitableresource);
			// jtask-resource 1
			TASKSUITABLERESOURCE jtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource2,
					jTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			jtasksuitableresource.setPROPERTIES(jTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(jtasksuitableresource);

			LOGGER.trace(msg + "Created setTASKSUITABLERESOURCES");
			aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
			LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
			LOGGER.trace(msg + "Created Workcenters");

			DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
			DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

			LOGGER.trace(msg + "Created JObs");
			LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
					aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
			double height = 0;
			double width = 0;
			for (TASKSUITABLERESOURCE e : aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE()) {
				for (PROPERTY p : e.getPROPERTIES().getPROPERTY()) {
					if (p.getNAME() == "HEIGHT") {
						height = Double.parseDouble(p.getVALUE());
					}
					if (p.getNAME() == "WIDTH") {
						width = Double.parseDouble(p.getVALUE());
					}
				}
				MIN_FLOORSPACE_TASKS = height * width;
				height = 0;
				width = 0;
			}
		} else if (scenario.startsWith("parts")) {
			TASK[] task = new TASK[2];
			task[0] = MapToResourcesAndTasks.getTask("part1", "part1", newId, 1, 1, 1, 3.0,
					MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE,
					MapToResourcesAndTasks.TABLE_PART_PROPERTY_VALUE_PART);
			tasks.getTASK().add(task[0]);

			id1 = IDGenerator.getNewID();
			newId = "t_" + id1;
			task[1] = MapToResourcesAndTasks.getTask("part2", "part2", newId, 1, 1, 1, 1.0,
					MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK, false, 1, MapToResourcesAndTasks.SHAPE_REACTANGLE,
					MapToResourcesAndTasks.TABLE_PART_PROPERTY_VALUE_PART);
			tasks.getTASK().add(task[1]);
			aPlanninginput.setTASKS(tasks);

			RESOURCES resources = myObjectFactory.createRESOURCES();
			id1 = IDGenerator.getNewID();
			newId = "r_" + id1;
			PROPERTIES properties = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN, 7.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(800 / 500.0),
					(int) Math.ceil(800 / 500.0), (1200 / 500.0), 1.75, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE[] resource = new RESOURCE[2];
			resource[0] = MapToResourcesAndTasks.getResource("Human", "Human ", newId, properties);
			resources.getRESOURCE().add(resource[0]);

			id1 = IDGenerator.getNewID();
			newId = "r_" + id1;
			properties = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 7.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(800 / 500.0),
					(int) Math.ceil(800 / 500.0), (1200 / 500.0), 2.0, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			resource[1] = MapToResourcesAndTasks.getResource("Robot", "Robot", newId, properties);
			resources.getRESOURCE().add(resource[1]);

			aPlanninginput.setRESOURCES(resources);

			TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
			TASKSUITABLERESOURCE atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(resource[0],
					task[0], LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(task[0].getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(resource[1], task[0],
					LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(task[0].getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);

			atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(resource[0], task[1],
					LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(task[0].getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(resource[1], task[1],
					LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(task[0].getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);

			aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);

			LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
			LOGGER.trace(msg + "Created Workcenters");

			DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
			DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

			LOGGER.trace(msg + "Created JObs");
			LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
					aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
			double height = 0;
			double width = 0;
			for (TASKSUITABLERESOURCE e : aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE()) {
				for (PROPERTY p : e.getPROPERTIES().getPROPERTY()) {
					if (p.getNAME() == "HEIGHT") {
						height = Double.parseDouble(p.getVALUE());
					}
					if (p.getNAME() == "WIDTH") {
						width = Double.parseDouble(p.getVALUE());
					}
				}
				MIN_FLOORSPACE_TASKS = height * width;
				height = 0;
				width = 0;
			}
		} else {
			// getTask(String description, String name, String id, int width, int length,
			// int reachability, double
			// weight, boolean isBasePart, int zheight)
			// put width, length, reachability in mm: int width: width/discretization-> both
			// in mm-> results: how many
			TASK aTask = MapToResourcesAndTasks.getTask("Conveyor " + id1, "Conveyor " + id1, id1 + "",
					(int) Math.ceil(880 / 500.0), (int) Math.ceil(4400 / 500.0), (int) Math.ceil(880 / 500.0), 25.0,
					false, 1);
			// TASK aTask = MapToResourcesAndTasks.getTask("LOAD GRIPPER_1" + id1,
			// "GRIPPER_1 " + id1, id1 + "", (int)
			// 500, (int) 600, (int) 600, 25.0,true,1);
			id1 = IDGenerator.getNewID();
			TASK bTask = MapToResourcesAndTasks.getTask("Human_working_table " + id1, "Human_working_table " + id1,
					id1 + "", (int) Math.ceil(1200 / 500.0), (int) Math.ceil(750 / 500.0),
					(int) Math.ceil(2650 / 500.0), 11.0, false, 1);

			id1 = IDGenerator.getNewID();
			TASK cTask = MapToResourcesAndTasks.getTask("Conveyor " + id1, "Conveyor " + id1, id1 + "",
					(int) Math.ceil(880 / 500.0), (int) Math.ceil(4400 / 500.0), (int) Math.ceil(880 / 500.0), 25.0,
					false, 1);

			id1 = IDGenerator.getNewID();
			TASK dTask = MapToResourcesAndTasks.getTask("Conveyor " + id1, "Conveyor " + id1, id1 + "",
					(int) Math.ceil(880 / 500.0), (int) Math.ceil(4400 / 500.0), (int) Math.ceil(880 / 500.0), 25.0,
					false, 1);

			tasks.getTASK().add(aTask);
			tasks.getTASK().add(bTask);
			tasks.getTASK().add(cTask);
			tasks.getTASK().add(dTask);
			LOGGER.trace(msg + "Created theTasks");

			LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, tasks);
			LOGGER.trace(msg + "Created Constraints");
			aPlanninginput.setTASKS(tasks);

			// Get Resources
			// ObjectFactory myObjectFactory1 = new ObjectFactory();
			RESOURCES resources = myObjectFactory.createRESOURCES();
			BigInteger id11 = IDGenerator.getNewID();
			// TODO set suitable height
			// getPROPERTIES(String resourceType, double payloadKg, double costEuro, double
			// floorSpaceM2, double
			// speedMMS, int width, int height, double reachability,int zheight)
			PROPERTIES props1 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 7.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(800 / 500.0),
					(int) Math.ceil(800 / 500.0), (1200 / 500.0), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			// getResource(String description, String name,String id, PROPERTIES properties)
			RESOURCE aResource1 = MapToResourcesAndTasks.getResource("Robot2" + id11, "Robot2 " + id11, id11 + "",
					props1);
			id11 = IDGenerator.getNewID();

			PROPERTIES props2 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 7.0, 20000.0,
					(int) Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(800 / 500.0),
					(int) Math.ceil(800 / 500.0), (1200 / 500.0), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource2 = MapToResourcesAndTasks.getResource("Robot1" + id11, "Robot1 " + id11, id11 + "",
					props2);

			id11 = IDGenerator.getNewID();
			PROPERTIES props3 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN, 11.0, 200.0,
					(int) Math.ceil((1500 * 1200) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(1500 / 500.0),
					(int) Math.ceil(1200 / 500.0), (1000 / 500.0), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource3 = MapToResourcesAndTasks.getResource("Human2" + id11, "Human2 " + id11, id11 + "",
					props3);

			id11 = IDGenerator.getNewID();
			PROPERTIES props4 = getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN, 11.0, 200.0,
					(int) Math.ceil((1500 * 1200) / (500.0 * 500.0)), (1500.0 / 500.0), (int) Math.ceil(1500 / 500.0),
					(int) Math.ceil(1200 / 500.0), (1000 / 500.0), 1, MapToResourcesAndTasks.SHAPE_REACTANGLE);
			RESOURCE aResource4 = MapToResourcesAndTasks.getResource("Human1" + id11, "Human1 " + id11, id11 + "",
					props4);
			resources.getRESOURCE().add(aResource1);
			resources.getRESOURCE().add(aResource2);
			resources.getRESOURCE().add(aResource3);
			resources.getRESOURCE().add(aResource4);
			LOGGER.trace(msg + "Created theResources");
			aPlanninginput.setRESOURCES(resources);

			// Suitable resources
			// ObjectFactory myObjectFactory11 = new ObjectFactory();
			TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
			// atask-resource 1
			TASKSUITABLERESOURCE atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource1,
					aTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			atasksuitableresource.setPROPERTIES(aTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			// btask2-resourc 1
			TASKSUITABLERESOURCE btasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource3,
					bTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			btasksuitableresource.setPROPERTIES(bTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(btasksuitableresource);
			// ctask-resource 1
			TASKSUITABLERESOURCE ctasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource2,
					cTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			ctasksuitableresource.setPROPERTIES(cTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(ctasksuitableresource);
			// dtask-resource 1
			TASKSUITABLERESOURCE dtasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource4,
					dTask, LayoutPlanningInputGenerator.SETUP_CODE,
					LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
			dtasksuitableresource.setPROPERTIES(dTask.getPROPERTIES());
			tasksuitableresources.getTASKSUITABLERESOURCE().add(dtasksuitableresource);

			LOGGER.trace(msg + "Created setTASKSUITABLERESOURCES");
			aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);
			// TODO
			LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);
			LOGGER.trace(msg + "Created Workcenters");

			DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
			DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

			LOGGER.trace(msg + "Created JObs");
			LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
					aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
			double height = 0;
			double width = 0;
			for (TASKSUITABLERESOURCE e : aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE()) {
				for (PROPERTY p : e.getPROPERTIES().getPROPERTY()) {
					if (p.getNAME() == "HEIGHT") {
						height = Double.parseDouble(p.getVALUE());
					}
					if (p.getNAME() == "WIDTH") {
						width = Double.parseDouble(p.getVALUE());
					}
				}
				MIN_FLOORSPACE_TASKS = height * width;
				height = 0;
				width = 0;
			}
		}

		return aPlanninginput;

	}
}
