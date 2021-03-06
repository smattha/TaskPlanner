package lms.robopartner.mfg_planning_tool;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javafx.geometry.Point3D;
import lms.robopartner.datamodel.map.DimensionObject;
import lms.robopartner.datamodel.map.DimensionObjectInterface;
import lms.robopartner.datamodel.map.controller.LayoutEvaluator;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;
import lms.robopartner.mfg_planning_tool.impact.criteria.Accessibility;
import lms.robopartner.mfg_planning_tool.impact.criteria.LayoutGenErgonomy;
import lms.robopartner.mfg_planning_tool.impact.criteria.SchedulingCost;
import lms.robopartner.mfg_planning_tool.impact.interfaces.DynamicMapTaskResourceSuitabilityAuthority;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import planning.MainPlanningTool;
import planning.model.AssignmentDataModel;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.algorithms.impact.interfaces.DynamicResourceTaskSuitabilityAuthorityInterface;

/**
 * The main application. This class is responsible for identifying a suitable
 * layout. Then using this layout it produces the assignments for this layout
 * using the {@link TaskSchedulerForLayout}
 * 
 * @author Spyros
 *
 */
public class LayoutScheduler {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(LayoutScheduler.class);
	/**
	 * The static reference to the {@link PLANNINGINPUT} it is used in
	 * {@link SchedulingCost} to call internally impact.
	 */
	@Deprecated
	private static Document planningInputDocument = null;
	@Deprecated
	private static PLANNINGINPUT thePLANNINGINPUT = null;

	/**
	 * @return the planningInput
	 */
	@Deprecated
	public static final Document getPlanningInputDocument() {
		return planningInputDocument;
	}

	private static final int RESOLUTION_FOR_DRAWER = 50;

	/**
	 * @param aPlanninginput
	 * @param maxMapX
	 * @param maxMapY
	 * @return
	 */
	public static final Vector<AssignmentDataModel> evaluate(PLANNINGINPUT aPlanninginput, int maxMapX, int maxMapY) {
		String msg = ".evaluate(): ";

		try {
			LayoutScheduler.thePLANNINGINPUT = aPlanninginput;
			LayoutScheduler.planningInputDocument = LayoutPlanningInputGenerator
					.getPlanningInputXMLDocumentFromJaxb(aPlanninginput);
		} catch (Exception e) {
			RuntimeException anException = new RuntimeException("Error reading input", e);
			logger.error(msg, e);
			throw anException;
		}
		Vector<DimensionObjectInterface> tasksAndParts = new Vector<DimensionObjectInterface>();
		Set<String> allIdsFound = new TreeSet<String>();
		for (RESOURCE aResource : aPlanninginput.getRESOURCES().getRESOURCE()) {
			if (aResource != null && aResource.getId() != null && aResource.getPROPERTIES() != null) {

				DimensionObjectInterface tempDimensionObjectInterface = LayoutScheduler
						.getDimensionObjectInterfaceFromProperties(aResource.getId(), aResource.getPROPERTIES());
				for (PROPERTY aResourceProperty : aResource.getPROPERTIES().getPROPERTY()) {
					if (aResourceProperty.getNAME().equals(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)) {
						if (aResourceProperty.getVALUE().equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
							tempDimensionObjectInterface.setProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME,
									MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT);
						}
						if (aResourceProperty.getVALUE().equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
							tempDimensionObjectInterface.setProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME,
									MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN);
						}
					}
				}

				tasksAndParts.add(tempDimensionObjectInterface);
				allIdsFound.add(aResource.getId());
			} else {
				String msg2 = (msg + " Either null task found, or task with null id or task with null properties");
				RuntimeException rte = new RuntimeException(msg2);
				logger.error(msg2, rte);
				throw rte;
			}

		}

		for (TASKSUITABLERESOURCE aTaskSuitableResource : aPlanninginput.getTASKSUITABLERESOURCES()
				.getTASKSUITABLERESOURCE()) {
			if (aTaskSuitableResource != null && aTaskSuitableResource.getRESOURCEREFERENCE() != null
					&& aTaskSuitableResource.getRESOURCEREFERENCE().getRefid() != null
					&& aTaskSuitableResource.getPROPERTIES() != null) {
				String id = aTaskSuitableResource.getTASKREFERENCE().getRefid();

				if (!allIdsFound.contains(id)) {
					tasksAndParts.add(LayoutScheduler.getDimensionObjectInterfaceFromProperties(id,
							aTaskSuitableResource.getPROPERTIES()));
					allIdsFound.add(id);
					logger.trace(msg + " Found referenced suitable resource:" + id
							+ ".This resource has not been found again. Each resource should be found only once.");
				} else {
					logger.trace(msg + " Found referenced suitable resource:" + id
							+ ".This resource has been found again. Each resource should be found only once.");
				}
			}
		}
		int planStartDay = Integer.parseInt(aPlanninginput.getPlanStartDateDay());
		int planStartMonth = Integer.parseInt(aPlanninginput.getPlanStartDateMonth());
		int planStartYear = Integer.parseInt(aPlanninginput.getPlanStartDateYear());
		int planEndDay = Integer.parseInt(aPlanninginput.getPlanEndDateDay());
		int planEndMonth = Integer.parseInt(aPlanninginput.getPlanEndDateMonth());
		int planEndYear = Integer.parseInt(aPlanninginput.getPlanEndDateYear());
		boolean continueAfterEnd = Boolean.parseBoolean(aPlanninginput.getContinueAssignmentsAfterPlanEndDate());
		logger.trace(msg + " Collected input and started evaluation");
		return LayoutScheduler.evaluate(tasksAndParts, maxMapX, maxMapY, planStartDay, planStartMonth, planStartYear,
				planEndDay, planEndMonth, planEndYear, continueAfterEnd);
	}

	/**
	 * @return the thePLANNINGINPUT
	 */
	@Deprecated
	public static final PLANNINGINPUT getThePLANNINGINPUT() {
		return thePLANNINGINPUT;
	}

	/**
	 * @param tasksAndParts
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
	private static final Vector<AssignmentDataModel> evaluate(Vector<DimensionObjectInterface> tasksAndParts,
			int maxMapX, int maxMapY, int planStartDay, int planStartMonth, int planStartYear, int planEndDay,
			int planEndMonth, int planEndYear, boolean continueAfterEnd) {
		String msg = ".evaluate(): ";
		PLANNINGINPUT layoutPlanningInput /*
											 * = LayoutPlanningInputGenerator.getMapPlanninginput(tasksAndParts,
											 * maxMapX, maxMapY, planStartDay, planStartMonth, planStartYear,
											 * planEndDay, planEndMonth, planEndYear, continueAfterEnd)
											 */ = null;
		Document document;
		try {
			document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
		} catch (Exception e) {
			RuntimeException anException = new RuntimeException(e);
			logger.error(msg, e);
			throw anException;
		}
		MainPlanningTool mpt = new MainPlanningTool(document);
		logger.trace("");
		logger.trace("");
		logger.trace("************************** Started algorithm **************************" + "on "
				+ Calendar.getInstance().getTime());
		// MAKE THE RESULTS

		int dh = 1;
		int mna = 100;
		int sr = 10;

		// Set the criteria
		int internalDH = 1;
		int internalMNA = 100;
		int internalSR = 10;
		SchedulingCost schedulingCost = new SchedulingCost(internalDH, internalMNA, internalSR);
		Accessibility accessibility /* = new Accessibility(internalDH, internalMNA, internalSR) */ = null;
		// ThreeDimensionalReachability threeDimensionalReachability = new
		// ThreeDimensionalReachability(internalDH, internalMNA, internalSR);
		LayoutGenErgonomy layoutGenErgonomy /* = new LayoutGenErgonomy(internalDH, internalMNA, internalSR) */ = null;

		// MAKE THE RESULTS
		mpt.initializeSimulator();

		// configure impact
		IMPACT mptIMPACT = (IMPACT) mpt.getAlgorithmFactoryforConfiguration()
				.getAlgorithmInstance(IMPACT.MULTICRITERIA);
		mptIMPACT.setDH(dh);
		mptIMPACT.setMNA(mna);
		mptIMPACT.setSR(sr);
//        mptIMPACT.setCriteria(new AbstractCriterion[] { schedulingCost});
//        mptIMPACT.setCriteria(new AbstractCriterion[] { schedulingCost, accessibility,threeDimensionalReachability          ,            layoutGenErgonomy });
		mptIMPACT.setCriteria(new AbstractCriterion[] { schedulingCost, accessibility, layoutGenErgonomy });

		Vector<DynamicResourceTaskSuitabilityAuthorityInterface> theDynamicResourceTaskSuitabilityAuthorityInterfaces = new Vector<DynamicResourceTaskSuitabilityAuthorityInterface>();
		theDynamicResourceTaskSuitabilityAuthorityInterfaces.add(new DynamicMapTaskResourceSuitabilityAuthority());
		mptIMPACT.setDynamicResourceTaskSuitabilityAuthorities(theDynamicResourceTaskSuitabilityAuthorityInterfaces);

		// mpt.getIMPACTforConfiguration().setDynamicResourceTaskSuitabilityAuthorities(dynamicResourceTaskSuitabilityAuthorities);

		mpt.simulate();
		logger.trace("************************* Finished algorithm **************************" + " on "
				+ Calendar.getInstance().getTime());
		Vector<AssignmentDataModel> assignments = mpt.getAssignmentDataModelVector();
		// LayoutScheduler.prettyPrint(assignments);
		LayoutScheduler.prettyDraw(assignments, LayoutScheduler.RESOLUTION_FOR_DRAWER);
		// logger.trace("************************* Suitability
		// calculated:"+DynamicMapTaskResourceSuitabilityAuthority.isSuitabilityValid+"
		// times"
		// );
		return assignments;
	}

	/**
	 * The following properties should exist otherwise throw exception
	 * {@link LayoutScheduler#LENGTH_PROPERTY_NAME}
	 * {@link LayoutScheduler#WIDTH_PROPERTY_NAME}
	 * {@link LayoutScheduler#REACHABILITY_PROPERTY_NAME}
	 * 
	 * 
	 * @param properties
	 * @return
	 */
	private static final DimensionObjectInterface getDimensionObjectInterfaceFromProperties(String id,
			PROPERTIES properties) {
		String msg = ".getDimensionObjectInterfaceFromProperties(): ";
		boolean widthFound = false;
		boolean heightFound = false;
		boolean reachabilityFound = false;
		int height = 0;
		int width = 0;
		double reachability = 0;
		for (PROPERTY aProperty : properties.getPROPERTY()) {
			if (aProperty != null && aProperty.getNAME() != null) {
//            	if (aProperty.getNAME().equals(MapToResourcesAndTasks.TYPE_PROPERTY_)) {
//                    height = Integer.parseInt(aProperty.getVALUE());
//                    heightFound = true;
//                }
				if (aProperty.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME)) {
					height = Integer.parseInt(aProperty.getVALUE());
					heightFound = true;
				}
				if (aProperty.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)) {
					width = Integer.parseInt(aProperty.getVALUE());
					widthFound = true;
				}

				if (aProperty.getNAME().equals(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)) {
					reachability = Double.parseDouble(aProperty.getVALUE());
					reachabilityFound = true;
				}
				if (widthFound && heightFound && reachabilityFound) {
					break;
				}
			} else {
				logger.warn(msg + "Null Property or Property with null name found. Property=" + aProperty);
			}

		}
		if (!widthFound || !heightFound || !reachabilityFound || id == null || id.isEmpty()) {
			String msg2 = msg + "Height, Width, Reachability and id are required. widthFound=" + widthFound
					+ " heightFound=" + heightFound + " reachabilityFound=" + reachabilityFound + " " + " id=" + id;
			RuntimeException aRuntimeException = new RuntimeException(msg2);
			logger.error(msg2, aRuntimeException);
			throw aRuntimeException;
		}
		return new DimensionObject(new Dimension(width, height), id, reachability);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void demoRun() throws Exception {
		logger.trace("************************** Started Application ************************" + "on "
				+ Calendar.getInstance().getTime());

		// change that to fit your input START--------------
		Vector<DimensionObjectInterface> tasksAndParts = new Vector<DimensionObjectInterface>();
		tasksAndParts.add(new DimensionObject(new Dimension(2, 3), 1 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 2), 2 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 1), 3 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 1), 4 + "", 4.0));

		int maxMapX = 5;
		int maxMapY = 5;
		int planStartDay = 1;
		int planStartMonth = 1;
		int planStartYear = 2014;
		int planEndDay = 1;
		int planEndMonth = 1;
		int planEndYear = 2018;
		boolean continueAfterEnd = true;

		LayoutScheduler.evaluate(tasksAndParts, maxMapX, maxMapY, planStartDay, planStartMonth, planStartYear,
				planEndDay, planEndMonth, planEndYear, continueAfterEnd);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String msg = ".main():";
		logger.trace(msg + "************************** Started Application ************************" + "on "
				+ Calendar.getInstance().getTime());
		// LayoutScheduler.demoRun();
		logger.trace(msg + "************************** Started Layout Generation ************************");
		Vector<AssignmentDataModel> layoutAssignments = LayoutScheduler
				.evaluate(DemoPlanningGenerator.getDemoPlanningInput(), 5, 5);
		logger.trace(msg + "************************** Ended Layout Generation ************************");
		logger.trace(
				msg + "************************** Started Task Plan Generation For Layout************************");
		// Prepare input for producing schedule assignments.
		Map<String, Point3D> resourcesAndPartsMapingForSR = new HashMap<String, Point3D>();
		for (AssignmentDataModel anAssignment : layoutAssignments) {
			resourcesAndPartsMapingForSR.put(anAssignment.getTaskDataModel().getTaskId(),
					MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));
			logger.trace(
					msg + "(Assignment) PartOrResourceID=" + anAssignment.getTaskDataModel().getTaskId() + " Location="
							+ MapParameters.getLocationFromID(anAssignment.getResourceDataModel().getResourceId()));

		}
		int internalDH = 1;
		int internalMNA = 100;
		int internalSR = 10;
		Document planningInputDocument = LayoutScheduler.getPlanningInputDocument();
		TaskSchedulerForLayout taskSchedulerForGivenLayout = new TaskSchedulerForLayout(resourcesAndPartsMapingForSR,
				internalDH, internalMNA, internalSR);
		taskSchedulerForGivenLayout.evaluate(planningInputDocument);
		Vector<AssignmentDataModel> taskPlanningAssignments = taskSchedulerForGivenLayout.getTheResultAssignments();
		LayoutScheduler.prettyPrint(taskPlanningAssignments, resourcesAndPartsMapingForSR);
		logger.trace(msg + "************************** Ended Task Plan Generation For Layout************************");

	}

	/**
	 * @param assignments
	 * @param resolution
	 */
	private static void prettyDraw(Vector<AssignmentDataModel> assignments, int resolution) {
		Dimension map = new Dimension(MapParameters.MAP_WIDTH * resolution, MapParameters.MAP_HEIGHT * resolution);
		Vector<Rectangle> theTaskRectangles = new Vector<Rectangle>();
		Vector<Rectangle> theReachabilityRectangles = new Vector<Rectangle>();
		for (AssignmentDataModel anAssignmentDataModel : assignments) {
			Rectangle aRectangle = DataModelToAWTHelper.createRectangle(anAssignmentDataModel.getResourceDataModel(),
					anAssignmentDataModel.getTaskDataModel(), resolution);
			theTaskRectangles.add(aRectangle);
			Rectangle theReachabilityRectangle = LayoutEvaluator
					.getReachabilityRectangle(
							aRectangle, Integer
									.parseInt(anAssignmentDataModel.getTaskDataModel()
											.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME))
									* resolution);
			theReachabilityRectangles.add(theReachabilityRectangle);
		}
		SolutionPainter.drawSolution(map, theTaskRectangles, theReachabilityRectangles);
	}

	private static void prettyPrint(Vector<AssignmentDataModel> assignments,
			Map<String, Point3D> resourcesAndPartsMapingForSR) {

		String msg = ".prettyPrint(): ";
		logger.trace(msg + "SEQUENCE : ");
		HashMap<String, Vector<AssignmentDataModel>> timeOrderedAssignments = new HashMap<String, Vector<AssignmentDataModel>>();
		List<Date> allDispathesDates = new Vector<Date>();

		for (AssignmentDataModel assignment : assignments) {
			Date dateOfDispatch = assignment.getTimeOfDispatch().getTime();
			String dateOfDispatchStringRepresentation = "" + dateOfDispatch.getTime();
			if (!allDispathesDates.contains(dateOfDispatch)) {
				allDispathesDates.add(dateOfDispatch);
			}
			if (timeOrderedAssignments.get(dateOfDispatchStringRepresentation) == null) {
				Vector<AssignmentDataModel> timeAssignments = new Vector<AssignmentDataModel>();
				timeAssignments.add(assignment);
				timeOrderedAssignments.put(dateOfDispatchStringRepresentation, timeAssignments);
			} else {
				Vector<AssignmentDataModel> timeAssignments = timeOrderedAssignments
						.get(dateOfDispatchStringRepresentation);
				timeAssignments.add(assignment);
			}

		}

		Collections.sort(allDispathesDates);
		for (Date date : allDispathesDates) {
			String dateOfDispatchStringRepresentation = "" + date.getTime();
			Vector<AssignmentDataModel> dateAssignments = timeOrderedAssignments
					.get(dateOfDispatchStringRepresentation);
			String msg2 = "->" + date.toString() + (" milisec(" + date.getTime() + ")");
			logger.trace(msg2);
			for (AssignmentDataModel assignment : dateAssignments) {
				String msg3 = "TASK : ->" + assignment.getTaskDataModel().getTaskName() + "<- on RESOURCE : ->"
						+ assignment.getResourceDataModel().getResourceName() + "<- DURATION IN MILLIS : ->"
						+ assignment.getDurationInMilliseconds() + "<-" + "Resource Location: "
						+ resourcesAndPartsMapingForSR.get(assignment.getResourceDataModel().getResourceId())
						+ "Task Location: "
						+ resourcesAndPartsMapingForSR.get(assignment.getTaskDataModel().getTaskId());
				logger.trace(msg3);

			}

		}
	}
}
