/**
 * 
 */
package lms.robopartner.task_planner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lms.robopartner.datamodel.map.DimensionObjectInterface;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import eu.robopartner.ps.planner.planninginputmodel.DATE;
import eu.robopartner.ps.planner.planninginputmodel.JOB;
import eu.robopartner.ps.planner.planninginputmodel.JOBS;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.WORKCENTER;
import eu.robopartner.ps.planner.planninginputmodel.WORKCENTERRESOURCEREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.WORKCENTERS;
import planning.model.io.XMLPlanningEntityResolver;
import planning.scheduler.algorithms.AbstractAlgorithm;
import unused.ComparableTask;

/**
 * The purpose of this class is to provide functionality that helps creating the input for the layout generation layer.
 * 
 * @author Spyros
 *
 */
public abstract class LayoutPlanningInputGenerator {

	private static org.slf4j.Logger	logger								= LoggerFactory.getLogger(LayoutPlanningInputGenerator.class);
	// TODO for optimization purposes check if the static methods can be replaced.
	// TODO find which objects are declared in every method and change them to global.

	/**
	 * To be used for testing
	 */
	private static final int		TASK_REACHABILITY					= 4;
	private static final int		TASK_WEIGHT							= 5;
	// width,height,number of TASKS,task reachability
	private static final int[][]	TASKS								= new int[][] {
			{ 2, 3, 1, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT, 1 },
			{ 1, 2, 1, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT, 1 },
			{ 1, 1, 2, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT, 1 }
																		// { 1, 1, 4, TASK_REACHABILITY }
																		};

	// width,height,taskid,task reachability
	// Task id here should match with a manufacturing resource id or part id
	@SuppressWarnings ("unused")
	private static final int[][]	TASKSwithIDS						= new int[][] {
			{ 2, 3, 1, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT },
			{ 1, 2, 2, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT },
			{ 1, 1, 3, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT },
			{ 1, 1, 4, LayoutPlanningInputGenerator.TASK_REACHABILITY, LayoutPlanningInputGenerator.TASK_WEIGHT }
																		// { 1, 1, 4, TASK_REACHABILITY }
																		};

	private static final int		MAP_WIDTH							= MapParameters.MAP_WIDTH;
	private static final int		MAP_HEIGHT							= MapParameters.MAP_HEIGHT;
	public static final int			OPERATION_TIME_PER_BATCH_SECONDS	= 1;
	public static final String		SETUP_CODE							= "1";
	private static final int		DEGREE_ANGLE						= 180;

	/**
	 * Use this function to create a suitable planning input for the location assignment
	 * 
	 * @param tasksWithIDs
	 *            See {@link LayoutPlanningInputGenerator#TASKSwithIDS} for an example
	 * @param maxMapX
	 * @param maxMapY
	 * @param planStartDay
	 * @param planStartMonth
	 * @param planStartYear
	 * @param planEndDay
	 * @param planEndMonth
	 * @param planEndYear
	 * @param continueAfterEnd
	 * @return
	 */
	// create the resources -new x,y,z
	public static PLANNINGINPUT getMapPlanninginput(Vector<DimensionObjectInterface> tasksWithIDs, int maxMapX, int maxMapY, int maxMapRz, int planStartDay, int planStartMonth, int planStartYear, int planEndDay, int planEndMonth, int planEndYear, boolean continueAfterEnd, Map<String, String> taskName, Map<String, String> resourceName) {
		String msg = ".getMapPlanninginput(): ";
		String id = null;
		logger.trace(msg + " tasksWithIds" + tasksWithIDs + " maxMapX=" + maxMapX + " maxMapY=" + maxMapY + " PlanStart=" + planStartDay + "."
				+ planStartMonth + "." + planStartYear + ". PlanEnd-" + planEndDay + "." + planEndMonth + "." + planEndYear + " continueAfterEnd="
				+ continueAfterEnd);
		id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, planStartDay, planStartMonth, planStartYear, planEndDay, planEndMonth, planEndYear, continueAfterEnd);
		logger.trace(msg + "Created aPlanningInput");
		TASKS theTasks = LayoutPlanningInputGenerator.addTasksWithIDS(aPlanninginput, tasksWithIDs);
		logger.trace(msg + "Created theTasks");
		LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, taskName, resourceName, tasksWithIDs);
		// logger.trace(msg + "Created Constraints");
		RESOURCES theResources = LayoutPlanningInputGenerator.addResources(aPlanninginput, maxMapX, maxMapY, maxMapRz);
		logger.trace(msg + "Created theResources");
		aPlanninginput.setTASKSUITABLERESOURCES(MapToResourcesAndTasks.getTaskSuitableResources(theResources, theTasks, LayoutPlanningInputGenerator.SETUP_CODE, LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS));
		logger.trace(msg + "Created setTASKSUITABLERESOURCES");
		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, theResources, AbstractAlgorithm.MULTICRITERIA);
		logger.trace(msg + "Created Workcenters");

		DATE arrivalDate = MapToResourcesAndTasks.getDate(planStartDay, planStartMonth, planStartYear, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(planEndDay, planEndMonth, planEndYear, 0, 0, 0);
		logger.trace(msg + "Created JObs");
		LayoutPlanningInputGenerator.addJobs(aPlanninginput, theTasks, aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);

		return aPlanninginput;

	}

	/**
	 * @return
	 *         not used
	 */
	public static PLANNINGINPUT getDemoPlanninginput() {
		String msg = ".getDemoPlanninginput(): ";
		String id = null;
		logger.trace(msg);
		id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2014, 1, 2, 2018, true);
		logger.trace(msg + "Created aPlanningInput");
		TASKS theTasks = LayoutPlanningInputGenerator.addTasks(aPlanninginput, LayoutPlanningInputGenerator.TASKS);
		logger.trace(msg + "Created theTasks");
		// LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput,
		// theTasks);
		// logger.trace(msg + "Created Constraints");
		RESOURCES theResources = LayoutPlanningInputGenerator.addResources(aPlanninginput, LayoutPlanningInputGenerator.MAP_WIDTH, LayoutPlanningInputGenerator.MAP_HEIGHT);
		logger.trace(msg + "Created theResources");

		aPlanninginput.setTASKSUITABLERESOURCES(MapToResourcesAndTasks.getTaskSuitableResources(theResources, theTasks, LayoutPlanningInputGenerator.SETUP_CODE, LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS));
		logger.trace(msg + "Created setTASKSUITABLERESOURCES");

		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, theResources, AbstractAlgorithm.MULTICRITERIA);
		logger.trace(msg + "Created Workcenters");

		DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);
		logger.trace(msg + "Created JObs");

		LayoutPlanningInputGenerator.addJobs(aPlanninginput, theTasks, aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);

		return aPlanninginput;

	}

	/**
	 * @param aPlanninginput
	 * @param theResources
	 * @param algorithmName
	 * @return
	 */
	public static WORKCENTERS addWorkcenters(PLANNINGINPUT aPlanninginput, RESOURCES theResources, String algorithmName) {
		// width,height
		ObjectFactory anObjectFactory = new ObjectFactory();
		WORKCENTERS theWorkcenters = anObjectFactory.createWORKCENTERS();
		String id = /*"w_" + */IDGenerator.getNewID() + "";

		WORKCENTER aWorkcenter = MapToResourcesAndTasks.getWorkcenter(id, "Workcenter" + id, "Workcenter " + id, algorithmName);
		for ( RESOURCE aResource : theResources.getRESOURCE() ) {

			WORKCENTERRESOURCEREFERENCE aWorkcenterresourcereference = anObjectFactory.createWORKCENTERRESOURCEREFERENCE();
			aWorkcenterresourcereference.setRefid(aResource.getId());
			aWorkcenter.getWORKCENTERRESOURCEREFERENCE().add(aWorkcenterresourcereference);
		}

		theWorkcenters.getWORKCENTER().add(aWorkcenter);

		anObjectFactory = null;
		aPlanninginput.setWORKCENTERS(theWorkcenters);

		return theWorkcenters;
	}

	/**
	 * @param aPlanninginput
	 */
	public static RESOURCES addResources(PLANNINGINPUT aPlanninginput, int mapWidth, int mapHeight) {
		String msg = ".addResources(): ";
		logger.trace(msg);
		// width,height
		ObjectFactory anObjectFactory = new ObjectFactory();
		RESOURCES theResources = anObjectFactory.createRESOURCES();
		String id = null;

		for ( int x = 0; x < mapWidth; x++ ) {
			for ( int y = 0; y < mapHeight; y++ ) {
				id = MapParameters.getIDfromLocation(x, y);
				theResources.getRESOURCE().add(MapToResourcesAndTasks.getResource3D("Tile X:" + x + " Y:" + y + " ID:" + id, "Resource_X" + x + "Y"
						+ y + "ID" + id, id, x, y, 1));
			}

		}

		anObjectFactory = null;
		aPlanninginput.setRESOURCES(theResources);
		return theResources;
	}

	/**
	 * @param aPlanninginput
	 */
	public static RESOURCES addResources(PLANNINGINPUT aPlanninginput, int mapX, int mapY, int mapRz) {
		String msg = ".addResources3D(): ";
		logger.trace(msg);
		// width,height
		ObjectFactory anObjectFactory = new ObjectFactory();
		RESOURCES theResources = anObjectFactory.createRESOURCES();
		String id = null;

		for ( int x = 0; x < mapX; x++ ) {
			for ( int y = 0; y < mapY; y++ ) {
				for ( int rz = 0; rz < mapRz; rz++ ) {
					id = MapParameters.getIDfromLocationAndRotation(x, y, DEGREE_ANGLE / (rz + 1));
					theResources.getRESOURCE().add(MapToResourcesAndTasks.getResourceWithRotation("Tile X:" + x + " Y:" + y + "Rz:" + DEGREE_ANGLE
							/ (rz + 1) + " ID:" + id, "Resource_X" + x + "Y" + y + "Rz:" + DEGREE_ANGLE / (rz + 1) + "ID" + id, id, x, y, DEGREE_ANGLE
							/ (rz + 1)));
				}
			}
		}

		anObjectFactory = null;
		aPlanninginput.setRESOURCES(theResources);
		return theResources;
	}

	/**
	 * To be used for testing
	 * 
	 * @param aPlanninginput
	 * @param TASKS
	 *            {width,height,number of TASKS}
	 */
	public static TASKS addTasks(PLANNINGINPUT aPlanninginput, int[][] tasks) {

		ObjectFactory anObjectFactory = new ObjectFactory();
		TASKS theTasks = anObjectFactory.createTASKS();
		String id = null;

		for ( int[] task : tasks ) {
			for ( int number = 0; number < task[2]; number++ ) {
				int width = task[0];
				int height = task[1];
				int reachability = task[3];
				double weight = task[4];
				int zheight = task[5];
				id = IDGenerator.getNewID() + "";
				theTasks.getTASK().add(MapToResourcesAndTasks.getTask("The Task W:" + width + " H:" + height + " ID:" + id, "Task_W" + width + "H"
						+ height + "ID" + id, id, width, height, reachability, weight, false, zheight));
			}
		}

		// Compare and order
		List<ComparableTask> theComparableList = LayoutPlanningInputGenerator.getComparableList(theTasks);
		Collections.sort(theComparableList, Collections.reverseOrder());
		theTasks = LayoutPlanningInputGenerator.getTASKSFromComparableList(theComparableList);
		anObjectFactory = null;
		aPlanninginput.setTASKS(theTasks);
		return theTasks;
	}

	/**
	 * use of 3D planner
	 * 
	 * @param initialPlanninginput
	 * @return
	 */
	public static PLANNINGINPUT getPlanninginputForLayoutScheduler(final PLANNINGINPUT unfilteredPlanningInput) {
		boolean flag = true;
		ObjectFactory objectFactory = new ObjectFactory();
		int counter = 0;
		PLANNINGINPUT filteredPlanningInput = objectFactory.createPLANNINGINPUT();
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS tasks = myObjectFactory.createTASKS();
		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
		tasks.getTASK().add(unfilteredPlanningInput.getTASKS().getTASK().get(0));

		for ( int i = 1; i < unfilteredPlanningInput.getTASKS().getTASK().size(); i++ ) {
			String checkTaskNamesi = unfilteredPlanningInput.getTASKS().getTASK().get(i).getDESCRIPTION();
			String[] filter = checkTaskNamesi.split(" ");
			flag = true;
			counter = 0;
			while ( flag && counter < tasks.getTASK().size() ) {
				String checkTaskNamesk = tasks.getTASK().get(counter).getDESCRIPTION();
				if ( checkTaskNamesk.startsWith(filter[0]) ) {
					flag = false;
				}
				counter++;
			}
			if ( flag ) tasks.getTASK().add(unfilteredPlanningInput.getTASKS().getTASK().get(i));

		}

		for ( TASKSUITABLERESOURCE aTaskSuitableResource : unfilteredPlanningInput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE() ) {
			for ( TASK aTask : tasks.getTASK() ) {
				if ( aTask.getId().equals(aTaskSuitableResource.getTASKREFERENCE().getRefid()) ) {
					tasksuitableresources.getTASKSUITABLERESOURCE().add(aTaskSuitableResource);
					break;
				}
			}
		}
		filteredPlanningInput.setTASKS(tasks);
		filteredPlanningInput.setRESOURCES(unfilteredPlanningInput.getRESOURCES());
		filteredPlanningInput.setTASKSUITABLERESOURCES(tasksuitableresources);
		filteredPlanningInput.setJOBS(unfilteredPlanningInput.getJOBS());
		filteredPlanningInput.setWORKCENTERS(unfilteredPlanningInput.getWORKCENTERS());
		filteredPlanningInput.setPlanEndDateDay(unfilteredPlanningInput.getPlanEndDateDay());
		filteredPlanningInput.setPlanEndDateMonth(unfilteredPlanningInput.getPlanEndDateMonth());
		filteredPlanningInput.setPlanEndDateYear(unfilteredPlanningInput.getPlanEndDateYear());
		filteredPlanningInput.setPlanStartDateDay(unfilteredPlanningInput.getPlanStartDateDay());
		filteredPlanningInput.setPlanStartDateMonth(unfilteredPlanningInput.getPlanStartDateMonth());
		filteredPlanningInput.setPlanStartDateYear(unfilteredPlanningInput.getPlanStartDateYear());
		filteredPlanningInput.setTASKPRECEDENCECONSTRAINTS(unfilteredPlanningInput.getTASKPRECEDENCECONSTRAINTS());
		filteredPlanningInput.setSETUPMATRICES(unfilteredPlanningInput.getSETUPMATRICES());
		filteredPlanningInput.setTOOLS(unfilteredPlanningInput.getTOOLS());
		filteredPlanningInput.setTOOLTYPES(unfilteredPlanningInput.getTOOLTYPES());
		filteredPlanningInput.setContinueAssignmentsAfterPlanEndDate(unfilteredPlanningInput.getContinueAssignmentsAfterPlanEndDate());

		// PLANNINGINPUT
		// a=LayoutPlanningInputGenerator.filterTasksFor3DLayoutScheduler(DemoPlanningGenerator3D.getResourcesAndTasksOriginalDemoPlanninginput());

		return filteredPlanningInput;

	}

	/**
	 * @param aPlanninginput
	 * @param TASKS
	 *            {width,height,number of TASKS}
	 */
	public static TASKS addTasksWithIDS(PLANNINGINPUT aPlanninginput, Vector<DimensionObjectInterface> tasksWithIDs) {

		ObjectFactory anObjectFactory = new ObjectFactory();
		TASKS theTasks = anObjectFactory.createTASKS();

		// turn a task into width, height, length
		// int counter=0;
		for ( DimensionObjectInterface aTask : tasksWithIDs ) {

			int width = aTask.getDimension().width;
			int height = aTask.getDimension().height;
			int zheight = (int) aTask.getHeight();
			String id = aTask.getID();
			String shape = aTask.getShape();
			int reachability = (int) aTask.getReachability();
			double weight = aTask.getWeight();
			String type = aTask.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME);
			if ( type == null ) {
				type = MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK;
			}
			theTasks.getTASK().add(MapToResourcesAndTasks.getTask("The Task W:" + width + " H:" + height + " ID:" + id, "Task_W" + width + "H"
					+ height + "ID" + id, id, width, height, reachability, weight, type, false, zheight, shape));
		}

		// Compare and order
		List<ComparableTask> theComparableList = LayoutPlanningInputGenerator.getComparableList(theTasks);
		Collections.sort(theComparableList, Collections.reverseOrder());
		theTasks = LayoutPlanningInputGenerator.getTASKSFromComparableList(theComparableList);
		anObjectFactory = null;
		aPlanninginput.setTASKS(theTasks);
		return theTasks;
	}

	/**
	 * @param aPlanninginput
	 * @param theTasks
	 * @param aWorkcenter
	 * @param arrivalDate
	 * @param dueDate
	 * @return
	 */
	public static JOBS addJobs(PLANNINGINPUT aPlanninginput, TASKS theTasks, WORKCENTER aWorkcenter, DATE arrivalDate, DATE dueDate) {

		ObjectFactory anObjectFactory = new ObjectFactory();
		JOBS theJobs = anObjectFactory.createJOBS();
		String id = null;

		int arrivalTimeDelay = 0;
		for ( TASK aTASK : theTasks.getTASK() ) {
			id = IDGenerator.getNewID() + "";
			TASKS aTempListOfTasks = anObjectFactory.createTASKS();
			aTempListOfTasks.getTASK().add(aTASK);

			// arrivalDate.setHOUR(arrivalDate.getHOUR() + arrivalTimeDelay);
			// dueDate.setHOUR(arrivalDate.getHOUR() + arrivalTimeDelay);
			arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, arrivalTimeDelay, 0, 0);
			dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, arrivalTimeDelay + 1, 0, 0);
			arrivalTimeDelay++;

			JOB theJob = MapToResourcesAndTasks.getJob(id, "Job" + id, "Job id =" + id, arrivalDate, dueDate, aTempListOfTasks, aWorkcenter);
			theJobs.getJOB().add(theJob);
		}
		anObjectFactory = null;
		aPlanninginput.setJOBS(theJobs);
		return theJobs;
	}

	/**
	 * @param aPlanninginput
	 * @param theTasks
	 * @return
	 */
	public static TASKPRECEDENCECONSTRAINTS addTaskPrecedenceConstraints(PLANNINGINPUT aPlanninginput, TASKS theTasks) {

		TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints = MapToResourcesAndTasks.getPrecedenceConstraints(theTasks);
		aPlanninginput.setTASKPRECEDENCECONSTRAINTS(theTaskprecedenceconstraints);
		return theTaskprecedenceconstraints;
	}

	public static TASKPRECEDENCECONSTRAINTS addTaskPrecedenceConstraints(PLANNINGINPUT aPlanninginput, Map<String, String> taskName, Map<String, String> resourceName, Vector<DimensionObjectInterface> tasksWithIDs) {

		TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints = MapToResourcesAndTasks.getTaskPrecedenceconstraints(taskName, resourceName, tasksWithIDs);
		aPlanninginput.setTASKPRECEDENCECONSTRAINTS(theTaskprecedenceconstraints);
		return theTaskprecedenceconstraints;
	}

	/**
	 * @param tasks
	 * @return
	 */
	private static List<ComparableTask> getComparableList(TASKS tasks) {
		List<ComparableTask> aTaskList = new ArrayList<ComparableTask>();
		for ( TASK aTask : tasks.getTASK() ) {
			aTaskList.add(new ComparableTask(aTask));
		}
		return aTaskList;
	}

	/**
	 * @param aList
	 * @return
	 */
	private static TASKS getTASKSFromComparableList(List<ComparableTask> aTaskList) {

		ObjectFactory anObjectFactory = new ObjectFactory();
		TASKS theTasks = anObjectFactory.createTASKS();

		for ( ComparableTask aComparableTask : aTaskList ) {
			theTasks.getTASK().add(aComparableTask.getTask());
		}
		return theTasks;
	}

	public static Document getPlanningInputXMLDocumentFromJaxb(PLANNINGINPUT planningInput) throws Exception {
		String msg = ".getPlanningInputXMLDocumentFromJaxb(PLANNINGINPUT): ";
		logger.trace(msg);
		// create a JAXBContext capable of handling classes generated into
		JAXBContext jc = JAXBContext.newInstance("eu.robopartner.ps.planner.planninginputmodel");
		// create a marshaller
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream myStream = new ByteArrayOutputStream();
		m.marshal(planningInput, System.out);
		m.marshal(planningInput, myStream);
		// To obtain an instance of a factory that can give us a document
		// builder:
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Get a instance of a builder, and use it to parse the stream:
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new XMLPlanningEntityResolver());
		Document document = builder.parse(new ByteArrayInputStream(myStream.toByteArray()));
		return document;
	}
}
