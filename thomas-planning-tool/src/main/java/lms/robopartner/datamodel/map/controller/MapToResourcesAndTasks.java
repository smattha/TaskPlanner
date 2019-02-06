/**
 * 
 */
package lms.robopartner.datamodel.map.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import lms.robopartner.datamodel.map.DimensionObjectInterface;

import org.slf4j.LoggerFactory;

import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import eu.robopartner.ps.planner.planninginputmodel.DATE;
import eu.robopartner.ps.planner.planninginputmodel.JOB;
import eu.robopartner.ps.planner.planninginputmodel.JOBTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.JOBWORKCENTERREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.NONWORKINGPERIODS;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.POSTCONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PRECONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCEAVAILABILITY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCEREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINT;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.WORKCENTER;
import eu.robopartner.ps.planner.planninginputmodel.WORKCENTERRESOURCEREFERENCE;

/**
 * Class responsible for translating a map to resources and classes. It is
 * abstract to prevent creating objects.
 * 
 * @author Spyros Koukas
 *
 */
public abstract class MapToResourcesAndTasks {

	public static final String SUITABLE_RESOURCE_PROPERTY_NAME = "Suitable resource";
	/**
	 * constants for coordinates and rotation
	 */
	public static final String LOCATION_X_PROPERTY_NAME = "Position X";
	public static final String LOCATION_Y_PROPERTY_NAME = "Position Y";
	public static final String LOCATION_Z_PROPERTY_NAME = "Position Z";
	public static final String ROTATION_Z_PROPERTY_NAME = "Rotation Z";

	/**
	 * constants for the shape of the parts/tables
	 */
	public static final String SHAPE_TYPE_NAME = "Shape";
	public static final String SHAPE_CIRCLE = "CIRCLE";
	public static final String SHAPE_REACTANGLE = "RECTANGLE";
	public static final String SHAPE_OVAL = "OVAL";
	public static final String SHAPE_LINE = "LINE";
	public static final String SHAPE_ROUND_RECTANGLE = "ROUND RECTANGLE";
	public static final String SHAPE_POLYGON = "POLYGON";
	/**
	 * TODO Write the unit of measurement for this property here
	 */
	public static final String WIDTH_PROPERTY_NAME = "WIDTH";

	// TODO check value for INTRALOGISTICS
	public static final String VIRTUAL_IMAU_PROPERTY_NAME = "VirtualIMAU";
	/**
	 * TODO Write the unit of measurement for this property here
	 */
	public static final String LENGTH_PROPERTY_NAME = "LENGTH";
	/**
	 * TODO Write the unit of measurement for this property here
	 */
	public static final String HEIGHT_PROPERTY_NAME = "HEIGHT";
	public static final String BASE_PART_PROPERTY_NAME = "Base";
	public static final String TABLE_PART_PROPERTY_NAME = "Table/part";
	public static final String SPEED_PROPERTY_NAME = "Speed";
	public static final String TYPE_PROPERTY_NAME = "Type";
	public static final String TYPE_PROPERTY_VALUE_IMAU = "IMAU";
	public static final String TYPE_PROPERTY_VALUE_STATION = "Station";
	public static final String TYPE_PROPERTY_VALUE_MARKET = "Market";
	public static final String TYPE_PROPERTY_VALUE_ROBOT = "Robot";
	public static final String TYPE_PROPERTY_VALUE_HUMAN = "Human";
	public static final String TYPE_PROPERTY_VALUE_TASK = "Task";
	public static final String TYPE_PROPERTY_VALUE_MOVEMENT = "Movement";
	public static final String TABLE_PART_PROPERTY_VALUE_PART = "Part";
	public static final String TABLE_PART_PROPERTY_VALUE_TABLE = "Table";

	private static final String MOVEMENT_LOGISTIC_TASK_NAME_PREFIX = "GO TO";
	private static final String MOVEMENT_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String LOADING_LOGISTIC_TASK_NAME_PREFIX = "LOADING FROM";
	private static final String LOADING_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String UNLOADING_LOGISTIC_TASK_NAME_PREFIX = "UNLOADING TO";
	private static final String UNLOADING_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String LOADINGUNLOADING_LOGISTIC_TASK_NAME_PREFIX = "LOADING AND UNLOADING";
	private static final String LOADINGUNLOADING_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String LOCK_LOGISTIC_TASK_NAME_PREFIX = "LOCK";
	private static final String LOCK_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String UNLOCK_LOGISTIC_TASK_NAME_PREFIX = "UNLOCK";
	private static final String UNLOCK_LOGISTIC_TASK_NAME_POSTFIX = "";
	private static final String MOVEMENT_LOGISTIC_TASK_TYPE = "Movement";
	private static final String LOADING_LOGISTIC_TASK_TYPE = "Loading";
	private static final String LOADINGUNLOADING_LOGISTIC_TASK_TYPE = "Loading/Unloading";
	private static final String UNLOADING_LOGISTIC_TASK_TYPE = "Unloading";
	private static final String DUMMY_LOGISTIC_TASK_TYPE = "Dummy";
	public static final int OPERATION_TIME_PER_BATCH_SECONDS = 1;
	public static final String SETUP_CODE = "1";

	/**
	 * Load a Box TO IMAU
	 */

	public static final String TYPE_PROPERTY_VALUE_LOADING = "Loading";
	public static final String TYPE_PROPERTY_VALUE_LOADINGUNLOADING = "Loading/Unloading";

	/**
	 * Unload a Box FROM IMAU
	 */

	public static final String TYPE_PROPERTY_VALUE_UNLOADING = "Unloading";
	public static final String TYPE_PROPERTY_VALUE_DUMMY = "Dummy";
	public static final String IMAU_MAX_NUMBER_OF_BOXES = "IMAU max number of boxes";
	public static final String IMAU_INITIAL_NUMBER_OF_BOXES = "IMAU initial number of boxes";
	public static final String ORIENTATION_PROPERTY_NAME = "ORIENTATION";
	public static final String REACHABILTY_PROPERTY_NAME = "REACHABILITY";
	public static final String WEIGHT_PROPERTY_NAME = "Weight (Kg)";

	/**
	 * Unload a Box FROM IMAU
	 */

	public static final String ORIENTATION_ZERO_DEGREES_VALUE = "0";
	public static final String ORIENTATION_NINETY_DEGREES_VALUE = "90";
	public static final int DEFAULT_REACHABILITY = 4;

	public static double MAX_WEIGHT = 0;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MapToResourcesAndTasks.class);

	/**
	 * @param name
	 * @param value
	 * @return
	 */

	public static PROPERTIES getLocationProperties(int locationX, int locationY) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME, locationX + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME, locationY + ""));

		myObjectFactory = null;
		return properties;
	}

	public static PROPERTIES getVirtualIMAUProperties(int virtualIMAU) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();
		properties.getPROPERTY().add(MapToResourcesAndTasks
				.getProperty(MapToResourcesAndTasks.VIRTUAL_IMAU_PROPERTY_NAME, virtualIMAU + ""));

		myObjectFactory = null;
		return properties;
	}

	public static PROPERTIES getSpeedIMAUProperties(double speed) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.SPEED_PROPERTY_NAME, speed + ""));

		myObjectFactory = null;
		return properties;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public static PROPERTIES getLocationPropertiesWithRotation(int locationX, int locationY, int rotationZ) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();

		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME, locationX + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME, locationY + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME, rotationZ + ""));

		myObjectFactory = null;
		return properties;
	}

	/**
	 * 
	 * @param locationX
	 * @param locationY
	 * @param locationZ
	 * @return
	 */
	public static PROPERTIES getLocationProperties3D(int locationX, int locationY, int locationZ) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();

		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME, locationX + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME, locationY + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME, locationZ + ""));

		myObjectFactory = null;
		return properties;
	}

	/**
	 * 
	 * @param locationZ
	 * @return
	 */
	public static PROPERTIES getLocationProperties(double locationZ) {
		ObjectFactory objectFactory = new ObjectFactory();
		PROPERTIES properties = objectFactory.createPROPERTIES();
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME, locationZ + ""));
		objectFactory = null;
		return properties;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param algorithmName
	 * @param theResources
	 * @return
	 */
	public static WORKCENTER getWorkcenter(String id, String name, String description, String algorithmName,
			RESOURCES theResources) {
		WORKCENTER aWORKCENTER = MapToResourcesAndTasks.getWorkcenter(id, name, description, algorithmName);
		ObjectFactory myObjectFactory = new ObjectFactory();
		for (RESOURCE aResource : theResources.getRESOURCE()) {
			WORKCENTERRESOURCEREFERENCE ref = myObjectFactory.createWORKCENTERRESOURCEREFERENCE();
			ref.setRefid(aResource.getId());
			aWORKCENTER.getWORKCENTERRESOURCEREFERENCE().add(ref);
		}
		myObjectFactory = null;
		return aWORKCENTER;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public static PROPERTIES getSizeProperties(int width, int length, int height) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTIES properties = myObjectFactory.createPROPERTIES();

		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME, width + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME, length + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME, height + ""));
		myObjectFactory = null;
		return properties;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public static PROPERTY getProperty(String name, String value) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		PROPERTY property = myObjectFactory.createPROPERTY();
		property.setNAME(name);
		property.setVALUE(value);
		myObjectFactory = null;
		return property;
	}

	/**
	 * use by 3D planner
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param properties
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, int width, int length, int reachability,
			double weight, boolean isBasePart, int zheight) {

		if (weight > MAX_WEIGHT) {
			MAX_WEIGHT = weight;
		}
		PROPERTIES properties = MapToResourcesAndTasks.getSizeProperties(width, length, zheight);

		properties.getPROPERTY().add(MapToResourcesAndTasks
				.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME, reachability + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.WEIGHT_PROPERTY_NAME, weight + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.BASE_PART_PROPERTY_NAME, isBasePart + ""));
		TASK task = MapToResourcesAndTasks.getTask(description, name, id, properties);
		return task;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param width
	 * @param length
	 * @param reachability
	 * @param weight
	 * @param isBasePart
	 * @param zheight
	 * @param shape
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, int width, int length, int reachability,
			double weight, boolean isBasePart, int zheight, String shape) {

		if (weight > MAX_WEIGHT) {
			MAX_WEIGHT = weight;
		}
		PROPERTIES properties = MapToResourcesAndTasks.getSizeProperties(width, length, zheight);

		properties.getPROPERTY().add(MapToResourcesAndTasks
				.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME, reachability + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.WEIGHT_PROPERTY_NAME, weight + ""));
		properties.getPROPERTY().add(
				MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.BASE_PART_PROPERTY_NAME, isBasePart + ""));
		properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty(SHAPE_TYPE_NAME, shape));

		TASK task = MapToResourcesAndTasks.getTask(description, name, id, properties);
		return task;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param width
	 * @param length
	 * @param reachability
	 * @param weight
	 * @param type
	 * @param isBasePart
	 * @param zheight
	 * @param shape
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, int width, int length, int reachability,
			double weight, String type, boolean isBasePart, int zheight, String shape) {

		if (weight > MAX_WEIGHT) {
			MAX_WEIGHT = weight;
		}
		PROPERTIES properties = MapToResourcesAndTasks.getSizeProperties(width, length, zheight);

		properties.getPROPERTY().add(MapToResourcesAndTasks
				.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME, reachability + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.WEIGHT_PROPERTY_NAME, weight + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, type + ""));
		properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME, shape));

		TASK task = MapToResourcesAndTasks.getTask(description, name, id, properties);
		return task;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param width
	 * @param length
	 * @param reachability
	 * @param weight
	 * @param type
	 * @param isBasePart
	 * @param zheight
	 * @param shape
	 * @param tableOrPartProperty
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, int width, int length, int reachability,
			double weight, String type, boolean isBasePart, int zheight, String shape, String tableOrPartProperty) {

		if (weight > MAX_WEIGHT) {
			MAX_WEIGHT = weight;
		}
		PROPERTIES properties = MapToResourcesAndTasks.getSizeProperties(width, length, zheight);

		properties.getPROPERTY().add(MapToResourcesAndTasks
				.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME, reachability + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.WEIGHT_PROPERTY_NAME, weight + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, type + ""));
		properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME, shape));
		properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.TABLE_PART_PROPERTY_NAME,
				tableOrPartProperty));

		TASK task = MapToResourcesAndTasks.getTask(description, name, id, properties);
		return task;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param virtualIMAU
	 * @param type
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, int virtualIMAU, String type) {

		PROPERTIES properties = MapToResourcesAndTasks.getVirtualIMAUProperties(virtualIMAU);

		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, type + ""));

		TASK task = MapToResourcesAndTasks.getTask(description, name, id, properties);
		return task;
	}

	/**
	 * use by 3D planner
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param properties
	 * @return
	 */
	public static TASK getTask(String description, String name, String id, PROPERTIES properties) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASK task = myObjectFactory.createTASK();
		task.setDESCRIPTION(description);
		task.setId(id);
		task.setNAME(name);
		task.setPROPERTIES(properties);
		myObjectFactory = null;
		return task;
	}

	/**
	 * @param id
	 * @param planStartDateDay
	 * @param planStartDateMonth
	 * @param planStartDateYear
	 * @param planEndDateDay
	 * @param planEndDateMonth
	 * @param planEndDateYear
	 * @param continueAssignmentsAfterPlanEndDate
	 * @return
	 */
	public static PLANNINGINPUT getPlanningInput(String id, int planStartDateDay, int planStartDateMonth,
			int planStartDateYear, int planEndDateDay, int planEndDateMonth, int planEndDateYear,
			boolean continueAssignmentsAfterPlanEndDate) {
		return MapToResourcesAndTasks.getPlanningInput(id, "" + planStartDateDay, "" + planStartDateMonth,
				"" + planStartDateYear, "" + planEndDateDay, "" + planEndDateMonth, "" + planEndDateYear,
				continueAssignmentsAfterPlanEndDate);
	}

	/**
	 * @param id
	 * @param planStartDateDay
	 * @param planStartDateMonth
	 * @param planStartDateYear
	 * @param planEndDateDay
	 * @param planEndDateMonth
	 * @param planEndDateYear
	 * @param continueAssignmentsAfterPlanEndDate
	 * @return
	 */
	public static PLANNINGINPUT getPlanningInput(String id, String planStartDateDay, String planStartDateMonth,
			String planStartDateYear, String planEndDateDay, String planEndDateMonth, String planEndDateYear,
			boolean continueAssignmentsAfterPlanEndDate) {

		ObjectFactory myObjectFactory = new ObjectFactory();
		PLANNINGINPUT planningInput = myObjectFactory.createPLANNINGINPUT();
		planningInput.setId(id);
		planningInput.setPlanStartDateDay(planStartDateDay);
		planningInput.setPlanStartDateMonth(planStartDateMonth);
		planningInput.setPlanStartDateYear(planStartDateYear);
		planningInput.setPlanEndDateDay(planEndDateDay);
		planningInput.setPlanEndDateMonth(planEndDateMonth);
		planningInput.setPlanEndDateYear(planEndDateYear);
		planningInput.setContinueAssignmentsAfterPlanEndDate(continueAssignmentsAfterPlanEndDate + "");
		myObjectFactory = null;

		return planningInput;
	}

	/**
	 * @param description
	 * @param name
	 * @param id
	 * @param properties
	 * @return
	 */
	public static RESOURCE getResource(String description, String name, String id, PROPERTIES properties) {

		ObjectFactory myObjectFactory = new ObjectFactory();
		RESOURCEAVAILABILITY resourceavailability = myObjectFactory.createRESOURCEAVAILABILITY();
		NONWORKINGPERIODS nonworkingperiods = myObjectFactory.createNONWORKINGPERIODS();
		resourceavailability.setNONWORKINGPERIODS(nonworkingperiods);
		RESOURCE resource = myObjectFactory.createRESOURCE();
		resource.setRESOURCEAVAILABILITY(resourceavailability);
		resource.setDESCRIPTION(description);
		resource.setId(id);
		resource.setNAME(name);
		resource.setPROPERTIES(properties);
		myObjectFactory = null;

		return resource;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param locationX
	 * @param locationY
	 * @param locationZ
	 * @param rotationZ
	 * @return
	 */

	public static RESOURCE getResourceWithRotation(String description, String name, String id, int locationX,
			int locationY, int rotationZ) {
		PROPERTIES properties = MapToResourcesAndTasks.getLocationPropertiesWithRotation(locationX, locationY,
				rotationZ);
		RESOURCE resource = MapToResourcesAndTasks.getResource(description, name, id, properties);

		return resource;
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @param id
	 * @param locationZ
	 * @return
	 */
	public static RESOURCE getResource(String description, String name, String id, double locationZ) {
		PROPERTIES properties = MapToResourcesAndTasks.getLocationProperties(locationZ);
		RESOURCE resource = MapToResourcesAndTasks.getResource(description, name, id, properties);

		return resource;
	}

	/**
	 * @param description
	 * @param name
	 * @param id
	 * @param properties
	 * @return
	 */
	public static RESOURCE getResource3D(String description, String name, String id, int locationX, int locationY,
			int locationZ) {

		PROPERTIES properties = MapToResourcesAndTasks.getLocationProperties3D(locationX, locationY, locationZ);
		RESOURCE resource = MapToResourcesAndTasks.getResource(description, name, id, properties);

		return resource;
	}

	public static RESOURCE getResource(String description, String name, String id, double speed, String type) {

		PROPERTIES properties = MapToResourcesAndTasks.getSpeedIMAUProperties(speed);
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.SPEED_PROPERTY_NAME, speed + ""));
		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, type + ""));
		RESOURCE resource = MapToResourcesAndTasks.getResource(description, name, id, properties);

		return resource;
	}

	/**
	 * @param task
	 * @return
	 */
	public static TASKREFERENCE getTaskReference(TASK task) {

		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKREFERENCE taskreference = myObjectFactory.createTASKREFERENCE();
		taskreference.setRefid(task.getId());
		myObjectFactory = null;
		return taskreference;

	}

	/**
	 * @param resource
	 * @return
	 */
	public static RESOURCEREFERENCE getResourceReference(RESOURCE resource) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		RESOURCEREFERENCE resourcereference = myObjectFactory.createRESOURCEREFERENCE();
		resourcereference.setRefid(resource.getId());
		myObjectFactory = null;
		return resourcereference;

	}

	public static TASKSUITABLERESOURCES getTaskSuitableResources(RESOURCES theResources, TASKS theTasks,
			String setupcode, int operationTimePerBatchSeconds) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
		for (RESOURCE aResource : theResources.getRESOURCE()) {
			for (TASK aTask : theTasks.getTASK()) {
				TASKSUITABLERESOURCE tasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource,
						aTask, setupcode, operationTimePerBatchSeconds);
				tasksuitableresources.getTASKSUITABLERESOURCE().add(tasksuitableresource);
			}
		}

		myObjectFactory = null;

		return tasksuitableresources;
	}

	public static TASKSUITABLERESOURCE getTaskSuitableResource(RESOURCE aResource, TASK aTask, String setupcode,
			int operationTimePerBatchSecondssource) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKSUITABLERESOURCE tasksuitableresource = myObjectFactory.createTASKSUITABLERESOURCE();
		TASKREFERENCE aTaskReference = MapToResourcesAndTasks.getTaskReference(aTask);
		RESOURCEREFERENCE resourcereference = MapToResourcesAndTasks.getResourceReference(aResource);
		tasksuitableresource.setOPERATIONTIMEPERBATCHINSECONDS(operationTimePerBatchSecondssource);
		tasksuitableresource.setTASKREFERENCE(aTaskReference);
		tasksuitableresource.setRESOURCEREFERENCE(resourcereference);
		tasksuitableresource.setSETUPCODE(setupcode);
		return tasksuitableresource;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param algorithmName
	 * @return
	 */
	public static WORKCENTER getWorkcenter(String id, String name, String description, String algorithmName) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		WORKCENTER workcenter = myObjectFactory.createWORKCENTER();
		workcenter.setDESCRIPTION(description);
		workcenter.setId(id);
		workcenter.setNAME(name);
		workcenter.setALGORITHM(algorithmName);
		myObjectFactory = null;
		return workcenter;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param arrivalDate
	 * @param dueDate
	 * @param jobTasks
	 * @param aWorkcenter
	 * @return
	 */
	public static JOB getJob(String id, String name, String description, DATE arrivalDate, DATE dueDate, TASKS jobTasks,
			WORKCENTER aWorkcenter) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		JOB job = myObjectFactory.createJOB();
		job.setId(id);
		job.setNAME(name);
		job.setDESCRIPTION(description);
		// arrival date
		job.setARRIVALDATE(arrivalDate);
		// due date
		job.setDUEDATE(dueDate);
		// released to station
		JOBWORKCENTERREFERENCE jwref = myObjectFactory.createJOBWORKCENTERREFERENCE();
		jwref.setRefid(aWorkcenter.getId());
		job.setJOBWORKCENTERREFERENCE(jwref);
		// Tasks

		for (TASK aTask : jobTasks.getTASK()) {
			JOBTASKREFERENCE jtref = myObjectFactory.createJOBTASKREFERENCE();
			jtref.setRefid(aTask.getId());
			job.getJOBTASKREFERENCE().add(jtref);
		}
		myObjectFactory = null;
		return job;
	}

	/**
	 * @param day
	 * @param month
	 * @param year
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static DATE getDate(int day, int month, int year, int hours, int minutes, int seconds) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		DATE date = myObjectFactory.createDATE();
		date.setDAY(day);
		date.setMONTH(month);
		date.setYEAR(year);
		date.setHOUR(hours);
		date.setMINUTE(minutes);
		date.setSECOND(seconds);
		myObjectFactory = null;
		return date;
	}

	/**
	 * @param theTasks
	 * @return
	 */
	public static TASKPRECEDENCECONSTRAINTS getPrecedenceConstraints(TASKS theTasks) {

		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKPRECEDENCECONSTRAINTS constraints = myObjectFactory.createTASKPRECEDENCECONSTRAINTS();

		String previousTaskId = null;
		for (TASK aTask : theTasks.getTASK()) {
			if (previousTaskId != null) {
				TASKPRECEDENCECONSTRAINT constraint = new TASKPRECEDENCECONSTRAINT();

				PRECONDITIONTASKREFERENCE pre = new PRECONDITIONTASKREFERENCE();
				POSTCONDITIONTASKREFERENCE post = new POSTCONDITIONTASKREFERENCE();

				pre.setRefid(previousTaskId);
				post.setRefid(aTask.getId());
				constraint.setPRECONDITIONTASKREFERENCE(pre);
				constraint.setPOSTCONDITIONTASKREFERENCE(post);
				constraints.getTASKPRECEDENCECONSTRAINT().add(constraint);
			}
			previousTaskId = aTask.getId();
		}

		return constraints;
	}

	public static TASKPRECEDENCECONSTRAINTS getTaskPrecedenceconstraints(Map<String, String> taskName,
			Map<String, String> resourceName, Vector<DimensionObjectInterface> tasksWithIDs) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKPRECEDENCECONSTRAINTS taskConstraints = myObjectFactory.createTASKPRECEDENCECONSTRAINTS();
		String previousTaskId = "";
		String humanId = "";
		for (DimensionObjectInterface aTask : tasksWithIDs) {
			if (resourceName.containsKey(aTask.getID())) {
				if (aTask.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
						.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT))
					previousTaskId = aTask.getID();
				else if (aTask.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
						.equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN))
					humanId = aTask.getID();

				if (!previousTaskId.isEmpty() && !humanId.isEmpty())
					break;
			}
		}

		// create constraints according the size of an object
		Map<Integer, String> maxToMinTaskIds = new HashMap<Integer, String>();
		int mapKey = 0;
		String minId = "";
		double minArea = 0d;
		for (DimensionObjectInterface aTask : tasksWithIDs) {
			if (!maxToMinTaskIds.containsValue(aTask.getID()) && taskName.containsKey(aTask.getID())) {
				String maxId = aTask.getID();
				double maxArea = MapToResourcesAndTasks.findArea(aTask.getShape(), aTask.getDimension().width,
						aTask.getDimension().height);
				minId = aTask.getID();
				minArea = MapToResourcesAndTasks.findArea(aTask.getShape(), aTask.getDimension().width,
						aTask.getDimension().height);
				for (int i = tasksWithIDs.size() - 1; i >= 0; i--) {
					DimensionObjectInterface bTask = tasksWithIDs.get(i);
					if (!maxToMinTaskIds.containsValue(aTask.getID()) && taskName.containsKey(bTask.getID())
							&& !maxId.equals(bTask.getID()) && !maxToMinTaskIds.containsValue(bTask.getID())) {
						double bArea = MapToResourcesAndTasks.findArea(bTask.getShape(), bTask.getDimension().width,
								bTask.getDimension().height);
						maxId = (maxArea < bArea) ? bTask.getID() : maxId;

						maxArea = (maxArea < bArea) ? bArea : maxArea;
						minId = (minArea >= bArea) ? bTask.getID() : minId;
						minArea = (minArea < bArea) ? bArea : minArea;
					}
				}
				maxToMinTaskIds.put(mapKey, maxId);
				mapKey++;
			}
		}

		if (!maxToMinTaskIds.containsValue(minId)) {
			maxToMinTaskIds.put(mapKey, minId);
			mapKey++;
		}

		for (int i = 0; i < mapKey; i++) {
			TASKPRECEDENCECONSTRAINT constraint = new TASKPRECEDENCECONSTRAINT();

			PRECONDITIONTASKREFERENCE pre = new PRECONDITIONTASKREFERENCE();
			POSTCONDITIONTASKREFERENCE post = new POSTCONDITIONTASKREFERENCE();

			pre.setRefid(previousTaskId);
			post.setRefid(maxToMinTaskIds.get(i));
			constraint.setPRECONDITIONTASKREFERENCE(pre);
			constraint.setPOSTCONDITIONTASKREFERENCE(post);
			taskConstraints.getTASKPRECEDENCECONSTRAINT().add(constraint);
			previousTaskId = maxToMinTaskIds.get(i);
		}

		if (!humanId.equals("")) {
			TASKPRECEDENCECONSTRAINT constraint = new TASKPRECEDENCECONSTRAINT();

			PRECONDITIONTASKREFERENCE pre = new PRECONDITIONTASKREFERENCE();
			POSTCONDITIONTASKREFERENCE post = new POSTCONDITIONTASKREFERENCE();

			pre.setRefid(previousTaskId);
			post.setRefid(humanId);
			constraint.setPRECONDITIONTASKREFERENCE(pre);
			constraint.setPOSTCONDITIONTASKREFERENCE(post);
			taskConstraints.getTASKPRECEDENCECONSTRAINT().add(constraint);
		}

		return taskConstraints;
	}

	private static double findArea(String shape, int width, int length) {
		double area = 0d;
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			area = width * length;
			break;
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			area = Math.PI * Math.pow(width, 2);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			area = Math.PI * width * length;
			break;
		}
		if (area == 0d) {
			RuntimeException e = new RuntimeException("not correct shape as an input");
			throw e;
		}
		return area;
	}

	public static TASKPRECEDENCECONSTRAINTS getAllPrecedenceContraints(TASKPRECEDENCECONSTRAINTS constraints,
			TASKPRECEDENCECONSTRAINTS allContraints) {
		// ObjectFactory myObjectFactory = new ObjectFactory();
		// TASKPRECEDENCECONSTRAINTS allContraints =
		// myObjectFactory.createTASKPRECEDENCECONSTRAINTS();
		for (TASKPRECEDENCECONSTRAINT aConstrain : constraints.getTASKPRECEDENCECONSTRAINT()) {
			allContraints.getTASKPRECEDENCECONSTRAINT().add(aConstrain);

		}
		return allContraints;
	}

	// Logistic Schedukle MapToResourcesAndTasks

	// Create MarketProperties

	public static PROPERTIES getPartProperties(double height) {
		ObjectFactory objectFactory = new ObjectFactory();
		PROPERTIES partProperties = objectFactory.createPROPERTIES();
		PROPERTY suitableResourceProperty = objectFactory.createPROPERTY();
		suitableResourceProperty.setNAME(MapToResourcesAndTasks.SUITABLE_RESOURCE_PROPERTY_NAME);
		String propertyValue = height + "";
		suitableResourceProperty.setVALUE(propertyValue);
		partProperties.getPROPERTY().add(suitableResourceProperty);
		objectFactory = null;

		return partProperties;
	}

	// Create TaskProperties

	// get Station Resource
	/**
	 * 
	 * @param aNewStation
	 * @return
	 */

	public static RESOURCES getLogisticResources(RESOURCE aResource) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		RESOURCES theResources = myObjectFactory.createRESOURCES();
		theResources.getRESOURCE().add(aResource);

		return theResources;
	}

	/**
	 * @param aNewLogisticTask
	 * @param virtualIMAU
	 * @return
	 * @author Spyros Koukas
	 */

	public static TASKS getLogisticTasks(TASK aTask) {
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS theTasks = myObjectFactory.createTASKS();
		theTasks.getTASK().add(aTask);

		return theTasks;
	}

	public static boolean isTaskDummy(TASK task) {

		if ((task.getNAME().startsWith(LOCK_LOGISTIC_TASK_NAME_PREFIX))
				&& (task.getNAME().endsWith(LOCK_LOGISTIC_TASK_NAME_POSTFIX))
				|| ((task.getNAME().startsWith(UNLOCK_LOGISTIC_TASK_NAME_PREFIX))
						&& (task.getNAME().endsWith(UNLOCK_LOGISTIC_TASK_NAME_POSTFIX)))) {
			return true;
		} else {
			return false;
		}
	}

}
