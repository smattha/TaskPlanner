package lms.robopartner.mfg_planning_tool;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import gr.upatras.lms.collections.MapList;
import gr.upatras.lms.collections.Maps;
import gr.upatras.lms.util.Convert;
import javafx.geometry.Point3D;
import lms.robopartner.datamodel.map.DimensionObject;
import lms.robopartner.datamodel.map.DimensionObjectInterface;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;
import lms.robopartner.mfg_planning_tool.impact.criteria.Accessibility;
import lms.robopartner.mfg_planning_tool.impact.criteria.AreaCost;
import lms.robopartner.mfg_planning_tool.impact.criteria.Reachability;
import lms.robopartner.mfg_planning_tool.impact.interfaces.DynamicMapTaskResourceSuitabilityAuthority3D;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import planning.MainPlanningTool;
import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.algorithms.impact.interfaces.DynamicResourceTaskSuitabilityAuthorityInterface;

/**
 * The main application. This class is responsible for identifying a suitable
 * layout in 3D.
 * Then using this layout it produces the assignments for this layout using the {@link TaskSchedulerForLayout}
 * 
 * @author Spyros
 *
 */
public class LayoutScheduler3D {

	/**
	 * constructor
	 */
	public LayoutScheduler3D() {}

	private static final org.slf4j.Logger	LOGGER			= LoggerFactory.getLogger(LayoutScheduler3D.class);
	private int								map_x			= 15;
	private int								map_y			= 15;
	private int								map_rz			= 2;
	public static final int					DH				= 1;
	public static final int					MNA				= 100;
	public static final int					SR				= 10;
	private PLANNINGINPUT					planningInput;
	private Vector<AssignmentDataModel>		layoutAssignments;
	private MapList<String, String>			resourcesAndPartsMaping;
	private Map<String, String>				resourceName, taskName;
	private Vector<String>					resultForAxisZ;
	private Map<String, Point3D>			resourcesAndPartsMapingForSR;
	private List<JSONObject>				tasksWithProperties;
	private static final int				DISCRETIZATION	= 1000;
	private DataOutputStream				outputStream	= null;
	private InputStream						inputStream		= null;

	/**
	 * @param outputStream
	 *            the outputStream to set
	 */
	public void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the tasksWithProperties
	 */

	public List<JSONObject> getTasksWithProperties() {
		return tasksWithProperties;
	}

	/**
	 * @param tasksWithProperties
	 *            the tasksWithProperties to set
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
	 * @param tasksWithTables
	 *            the tasksWithTables to set
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
	 * @param cellDimensions
	 *            the cellDimensions to set
	 */
	public void setCellDimensions(List<JSONObject> cellDimensions) {
		this.cellDimensions = cellDimensions;
		for ( JSONObject jsonObject : cellDimensions ) {
			//map_x = (jsonObject.getInt(JsonPropertiesHelper.WIDTH) / DISCRETIZATION) * 3;
			 map_x = (1000/ DISCRETIZATION) * 3;
		}
	}

	private List<JSONObject>	tasksWithTables;
	private List<JSONObject>	cellDimensions;

	/**
	 * @return the resourcesAndPartsMapingForSR
	 */
	public Map<String, Point3D> getResourcesAndPartsMapingForSR() {
		return resourcesAndPartsMapingForSR;
	}

	/**
	 * @param resourcesAndPartsMapingForSR
	 *            the resourcesAndPartsMapingForSR to set
	 */
	public void setResourcesAndPartsMapingForSR(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
	}

	/**
	 * @return the resultForAxisZ
	 */
	public Vector<String> getResultForAxisZ() {
		return resultForAxisZ;
	}

	/**
	 * @param resultForAxisZ
	 *            the resultForAxisZ to set
	 */
	public void setResultForAxisZ(Vector<String> resultForAxisZ) {
		this.resultForAxisZ = resultForAxisZ;
	}

	/**
	 * @return the layoutAssignments
	 */
	public Vector<AssignmentDataModel> getLayoutAssignments() {
		return layoutAssignments;
	}

	/**
	 * @param layoutAssignments
	 *            the layoutAssignments to set
	 */
	public void setLayoutAssignments(Vector<AssignmentDataModel> layoutAssignments) {
		this.layoutAssignments = layoutAssignments;
	}

	/**
	 * 
	 * @return
	 */
	public PLANNINGINPUT getPlanningInput() {
		return planningInput;
	}

	/**
	 * 
	 * @param planningInput
	 */
	public void setPlanningInput(PLANNINGINPUT planningInput) {
		this.planningInput = planningInput;
	}

	/**
	 * @param aPlanninginput
	 * @param maxMapX
	 * @param maxMapY
	 * @return
	 */
	public final Vector<AssignmentDataModel> evaluate(final PLANNINGINPUT filteredPlanningInput) {
		String msg = ".evaluate(): ";

		// make the resources tasks
		Vector<DimensionObjectInterface> tasksAndParts = new Vector<DimensionObjectInterface>();
		createTasksAndParts(tasksAndParts, filteredPlanningInput);
		int planStartDay = Integer.parseInt(filteredPlanningInput.getPlanStartDateDay());
		int planStartMonth = Integer.parseInt(filteredPlanningInput.getPlanStartDateMonth());
		int planStartYear = Integer.parseInt(filteredPlanningInput.getPlanStartDateYear());
		int planEndDay = Integer.parseInt(filteredPlanningInput.getPlanEndDateDay());
		int planEndMonth = Integer.parseInt(filteredPlanningInput.getPlanEndDateMonth());
		int planEndYear = Integer.parseInt(filteredPlanningInput.getPlanEndDateYear());
		boolean continueAfterEnd = Boolean.parseBoolean(filteredPlanningInput.getContinueAssignmentsAfterPlanEndDate());
		LOGGER.trace("{}  Collected input and started evaluation", msg);
		return callImpact(tasksAndParts, planStartDay, planStartMonth, planStartYear, planEndDay, planEndMonth, planEndYear, continueAfterEnd, this.planningInput);
	}

	/**
	 * 
	 * @param tasksAndParts
	 * @param filteredPlanningInput
	 */
	private void createTasksAndParts(Vector<DimensionObjectInterface> tasksAndParts, PLANNINGINPUT filteredPlanningInput) {
		String msg = ".createTasksAndParts(): ";
		Set<String> allIdsFound = new TreeSet<String>();
		for ( RESOURCE aResource : filteredPlanningInput.getRESOURCES().getRESOURCE() ) {
			if ( aResource != null && aResource.getId() != null && aResource.getPROPERTIES() != null ) {

				DimensionObjectInterface tempDimensionObjectInterface = getDimensionObjectInterfaceFromProperties(aResource.getId(), aResource.getPROPERTIES());
				for ( PROPERTY aResourceProperty : aResource.getPROPERTIES().getPROPERTY() ) {
					if ( aResourceProperty.getNAME().equals(MapToResourcesAndTasks.TYPE_PROPERTY_NAME) ) {
						if ( aResourceProperty.getVALUE().equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT) ) {
							tempDimensionObjectInterface.setProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT);
						}
						if ( aResourceProperty.getVALUE().equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN) ) {
							tempDimensionObjectInterface.setProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME, MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN);
						}
					}
				}

				tasksAndParts.add(tempDimensionObjectInterface);
				allIdsFound.add(aResource.getId());
			}
			else {
				String msg2 = (msg + " Either null task found, or task with null id or task with null properties");
				RuntimeException rte = new RuntimeException(msg2);
				LOGGER.error(msg2, rte);
				throw rte;
			}

		}

		for ( TASKSUITABLERESOURCE aTaskSuitableResource : filteredPlanningInput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE() ) {
			if ( aTaskSuitableResource != null && aTaskSuitableResource.getRESOURCEREFERENCE() != null
					&& aTaskSuitableResource.getRESOURCEREFERENCE().getRefid() != null
					&& aTaskSuitableResource.getPROPERTIES() != null ) {
				String id = aTaskSuitableResource.getTASKREFERENCE().getRefid();

				if ( !allIdsFound.contains(id) ) {
					tasksAndParts.add(getDimensionObjectInterfaceFromProperties(id, aTaskSuitableResource.getPROPERTIES()));
					allIdsFound.add(id);
					LOGGER.trace(msg + " Found referenced suitable resource:" + id
							+ ".This resource has not been found again. Each resource should be found only once.");
				}
				else {
					LOGGER.trace(msg + " Found referenced suitable resource:" + id
							+ ".This resource has been found again. Each resource should be found only once.");
				}
			}
		}
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
	private final Vector<AssignmentDataModel> callImpact(final Vector<DimensionObjectInterface> tasksAndParts, final int planStartDay, final int planStartMonth, final int planStartYear, final int planEndDay, final int planEndMonth, final int planEndYear, final boolean continueAfterEnd, PLANNINGINPUT aPlanninginput) {
		String msg = ".evaluate(): ";
		PLANNINGINPUT layoutPlanningInput = LayoutPlanningInputGenerator.getMapPlanninginput(tasksAndParts, map_x, map_y, map_rz, planStartDay, planStartMonth, planStartYear, planEndDay, planEndMonth, planEndYear, continueAfterEnd, taskName, resourceName);

		Document document = null;
		try {
			document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
		}
		catch ( Exception e ) {
			LOGGER.error(msg, e);
		}
		Vector<AssignmentDataModel> assignments = null;
		if ( document != null ) {
			String filePath = "C://layoutSchedulingInput.xml";
			createInputXML(filePath, document);
			MainPlanningTool mpt = new MainPlanningTool(document);
			LOGGER.trace("\n\n************************** Started algorithm **************************on {}", Calendar.getInstance().getTime());
			Accessibility accessibility = new Accessibility();
			Reachability reachability = new Reachability(aPlanninginput);
			//Ergonomics ergonomics = new Ergonomics(aPlanninginput);
			//ergonomics.setInputStream(inputStream);
			//ergonomics.setOutputStream(outputStream);
			// Payload payload = new Payload();
			AreaCost areaCost = new AreaCost(aPlanninginput);
			// TimeToComplete timeToComplete = new TimeToComplete();
			mpt.initializeSimulator();
			IMPACT mptIMPACT = (IMPACT) mpt.getAlgorithmFactoryforConfiguration().getAlgorithmInstance(IMPACT.MULTICRITERIA);
			mptIMPACT.setDH(LayoutScheduler3D.DH);
			mptIMPACT.setMNA(LayoutScheduler3D.MNA);
			mptIMPACT.setSR(LayoutScheduler3D.SR);
			mptIMPACT.setCriteria(new AbstractCriterion[] { reachability, accessibility, areaCost });
			Vector<DynamicResourceTaskSuitabilityAuthorityInterface> theDynamicResourceTaskSuitabilityAuthorityInterfaces = new Vector<DynamicResourceTaskSuitabilityAuthorityInterface>();
			DynamicMapTaskResourceSuitabilityAuthority3D dynamicMapTaskResourceSuitabilityAuthority3D = new DynamicMapTaskResourceSuitabilityAuthority3D();
			dynamicMapTaskResourceSuitabilityAuthority3D.setStartingPlanningInput(aPlanninginput);
			theDynamicResourceTaskSuitabilityAuthorityInterfaces.add(dynamicMapTaskResourceSuitabilityAuthority3D);
			mptIMPACT.setDynamicResourceTaskSuitabilityAuthorities(theDynamicResourceTaskSuitabilityAuthorityInterfaces);
			mpt.simulate();
			LOGGER.trace("************************* Finished algorithm **************************" + " on "
					+ Calendar.getInstance().getTime());
			assignments = mpt.getAssignmentDataModelVector();

			testIntersectionOfFinalAssignments(assignments);
		}
		return assignments;
	}

	private void testIntersectionOfFinalAssignments(Vector<AssignmentDataModel> assignments) {
		String msg = ".testIntersectionOfFinalAssignments(): ";
		for ( AssignmentDataModel aAssignment : assignments ) {
			TaskDataModel aTaskDataModel = aAssignment.getTaskDataModel();
			ResourceDataModel aResourceDataModel = aAssignment.getResourceDataModel();
			Shape aRectangle = DataModelToAWTHelper.createShape(aTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), aTaskDataModel, aResourceDataModel);
			String angle = aAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
			Point resourcePoint = DataModelToAWTHelper.getPointFromResource(aResourceDataModel);
			if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
				aRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), aRectangle, resourcePoint.x, resourcePoint.y, aTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
			for ( AssignmentDataModel bAssignment : assignments ) {
				TaskDataModel bTaskDataModel = bAssignment.getTaskDataModel();
				ResourceDataModel bResourceDataModel = bAssignment.getResourceDataModel();
				Shape bRectangle = DataModelToAWTHelper.createShape(bTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME), bTaskDataModel, bResourceDataModel);
				angle = bAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME);
				resourcePoint = DataModelToAWTHelper.getPointFromResource(bResourceDataModel);
				if ( !angle.equals("180") && !angle.equals("360") && !angle.equals("0") )
					bRectangle = DataModelToAWTHelper.rotateShape(Integer.parseInt(angle), bRectangle, resourcePoint.x, resourcePoint.y, bTaskDataModel.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME));
				if ( !aTaskDataModel.getTaskId().equals(bTaskDataModel.getTaskId()) )
					LOGGER.debug("{}\n\taTask: {} aTaskLocation: {} bTask: {}  bTaskLocation: {}\n\tintersects: {} ", msg, aTaskDataModel.getTaskId(), aResourceDataModel.getResourceId(), bTaskDataModel.getTaskId(), bResourceDataModel.getResourceId(), aRectangle.getBounds().intersects(bRectangle.getBounds()));
			}
		}
	}

	/**
	 * The following properties should exist otherwise throw exception {@link LayoutScheduler3D#LENGTH_PROPERTY_NAME}
	 * {@link LayoutScheduler3D#WIDTH_PROPERTY_NAME} {@link LayoutScheduler3D#REACHABILITY_PROPERTY_NAME}
	 * 
	 * 
	 * @param properties
	 * @return
	 */
	private final DimensionObjectInterface getDimensionObjectInterfaceFromProperties(String id, PROPERTIES properties) {
		String msg = ".getDimensionObjectInterfaceFromProperties(): ";
		boolean widthFound = false;
		boolean lengthFound = false;
		boolean heightFound = false;
		boolean reachabilityFound = false;
		boolean shapeFound = false;
		String shape = "";
		int length = 0;
		int width = 0;
		double height = 0;
		double reachability = 0;
		for ( PROPERTY aProperty : properties.getPROPERTY() ) {
			if ( aProperty != null && aProperty.getNAME() != null ) {

				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME) ) {
					length = Integer.parseInt(aProperty.getVALUE());
					lengthFound = true;
				}

				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME) ) {
					width = Integer.parseInt(aProperty.getVALUE());
					widthFound = true;
				}

				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME) ) {
					height = Double.parseDouble(aProperty.getVALUE());
					heightFound = true;
				}

				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME) ) {
					reachability = Double.parseDouble(aProperty.getVALUE());
					reachabilityFound = true;
				}

				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.SHAPE_TYPE_NAME) ) {
					shape = aProperty.getVALUE();
					shapeFound = true;
				}

				if ( widthFound && lengthFound && reachabilityFound && heightFound && shapeFound ) {
					break;
				}
			}
			else {
				LOGGER.warn(msg + "Null Property or Property with null name found. Property=" + aProperty);
			}

		}
		if ( !widthFound || !heightFound || !lengthFound || !reachabilityFound || id == null || id.isEmpty()
				|| !shapeFound ) {
			String msg2 = msg + "Height, Width, Reachability and id are required. widthFound=" + widthFound
					+ " heightFound=" + lengthFound + " reachabilityFound=" + reachabilityFound + " " + " id=" + id
					+ " shapeFound: " + shapeFound;
			RuntimeException aRuntimeException = new RuntimeException(msg2);
			LOGGER.error(msg2, aRuntimeException);
			throw aRuntimeException;
		}
		return new DimensionObject(new Dimension(width, length), height, id, reachability, shape);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings ("unused")
	public static void demoRun() throws Exception {
		LOGGER.trace("************************** Started Application ************************" + "on "
				+ Calendar.getInstance().getTime());

		// change that to fit your input START--------------
		Vector<DimensionObjectInterface> tasksAndParts = new Vector<DimensionObjectInterface>();
		tasksAndParts.add(new DimensionObject(new Dimension(2, 3), 1 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 2), 2 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 1), 3 + "", 4.0));
		tasksAndParts.add(new DimensionObject(new Dimension(1, 1), 4 + "", 4.0));

		int maxMapX = 5;
		int maxMapY = 5;
		int maxMapZ = 5;
		int planStartDay = 1;
		int planStartMonth = 1;
		int planStartYear = 2014;
		int planEndDay = 1;
		int planEndMonth = 1;
		int planEndYear = 2018;
		boolean continueAfterEnd = true;

		// LayoutScheduler3D.evaluate(tasksAndParts, maxMapX, maxMapY, maxMapZ, planStartDay, planStartMonth,
		// planStartYear, planEndDay, planEndMonth, planEndYear, continueAfterEnd, aPlanninginput);
	}

	/**
	 * use for 3D planner- send results to C#
	 * 
	 * @return
	 * @throws Exception
	 */
	public final Vector<String> getLayoutSchedule() {

		DemoPlanningGenerator3D planningGenerator = new DemoPlanningGenerator3D();
		/*planningGenerator.setTasksWithProperties(tasksWithProperties);
		final PLANNINGINPUT planningInput = planningGenerator.getPlanninginputFromPSinput();*/
		final PLANNINGINPUT planningInput = planningGenerator.getResourcesAndTasksOriginalDemoPlanninginput("autorecon");// edw
																															// allazeis
																															// ton
																															// input
																															// string
		this.planningInput = planningInput;
		PLANNINGINPUT filteredPlanningInput = LayoutPlanningInputGenerator.getPlanninginputForLayoutScheduler(planningInput);
		try {
			String filePath = "C://filteredLayoutSchedulingInput.xml";
			Document document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(filteredPlanningInput);
			createInputXML(filePath, document);
		}
		catch ( Exception e ) {
			LOGGER.error("", e);
		}
		if ( checkIfPartExists(planningInput) ) resultForAxisZ = findHeightForParts(planningInput);
		taskName = new HashMap<String, String>();
		for ( TASK aTask : planningInput.getTASKS().getTASK() )
			taskName.put(aTask.getId(), aTask.getNAME());
		resourceName = new HashMap<String, String>();
		for ( RESOURCE aResource : planningInput.getRESOURCES().getRESOURCE() )
			resourceName.put(aResource.getId(), aResource.getNAME());
		Vector<AssignmentDataModel> layoutAssignments = evaluate(filteredPlanningInput);
		setLayoutAssignments(layoutAssignments);
		MapList<String, String> resourcesAndPartsMapingForCSV = Maps.mapList();
		for ( AssignmentDataModel anAssignment : layoutAssignments ) {
			resourcesAndPartsMapingForCSV.add(anAssignment.getTaskDataModel().getTaskId(), getStringFromPoint(MapParameters.getLocationFromResourceDataModel(anAssignment.getResourceDataModel()))
					+ "");
			resourcesAndPartsMapingForCSV.add(anAssignment.getTaskDataModel().getTaskId(), anAssignment.getResourceDataModel().getProperty(MapToResourcesAndTasks.ROTATION_Z_PROPERTY_NAME));
		}
		createResourcesAndPartsMapingForSR(layoutAssignments);
		Vector<String> layoutSchedulerResults = new Vector<String>();
		Vector<String> sizeOfParts = new Vector<String>();
		createOutput(layoutSchedulerResults, sizeOfParts, resourcesAndPartsMapingForCSV);
		// resourceAndPartsMapingForSr for the tasks that may be multiple.
		createResourcesAndPartsMapingForSRForUnfilteredInput();
		createCSVFile(layoutSchedulerResults, sizeOfParts);
		return layoutSchedulerResults;
	}

	/**
	 * 
	 * @param layoutSchedulerResults
	 * @param sizeOfParts
	 * @param resourcesAndPartsMapingForSR
	 */
	private void createOutput(Vector<String> layoutSchedulerResults, Vector<String> sizeOfParts, MapList<String, String> resourcesAndPartsMapingForSR) {
		for ( String key : resourcesAndPartsMapingForSR.keySet() ) {
			String value = "";
			String size = "";
			boolean isTask = false;
			java.util.List<String> angleAndCoordinates = resourcesAndPartsMapingForSR.getList(key);
			for ( TASK aTask : planningInput.getTASKS().getTASK() ) {
				if ( aTask.getId().equals(key) ) {
					value = "result@" + aTask.getNAME() + "@" + angleAndCoordinates.get(0) + "@"
							+ angleAndCoordinates.get(1);
					String width = "", height = "";
					for ( PROPERTY property : aTask.getPROPERTIES().getPROPERTY() ) {
						if ( property.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME) ) width = property.getVALUE();
						else if ( property.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME) )
							height = property.getVALUE();
					}
					size = "," + width + "," + height + ",0";
					isTask = true;
					break;
				}
			}

			if ( !isTask ) {
				for ( RESOURCE aResource : planningInput.getRESOURCES().getRESOURCE() ) {
					if ( aResource.getId().equals(key) ) {
						String width = "", height = "", reachability = "";
						value = "result@" + aResource.getNAME() + "@" + angleAndCoordinates.get(0) + "@"
								+ angleAndCoordinates.get(1);
						for ( PROPERTY property : aResource.getPROPERTIES().getPROPERTY() ) {
							if ( property.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME) ) width = property.getVALUE();
							else if ( property.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME) ) height = property.getVALUE();
							else if ( property.getNAME().equals(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME) )
								reachability = property.getVALUE();
							size = "," + width + "," + height + "," + reachability;
						}
						break;
					}
				}
			}
			if ( !value.isEmpty() ) layoutSchedulerResults.add(value);

			if ( !size.isEmpty() ) sizeOfParts.add(size);
		}
	}

	private String getStringFromPoint(Point point) {
		String stringFromPoint = "Point [ " + point.x + " , " + point.y + " ]";
		return stringFromPoint;
	}

	/**
	 * 
	 * @param layoutSchedulerResults
	 * @param sizeOfParts
	 */
	private void createCSVFile(Vector<String> layoutSchedulerResults, Vector<String> sizeOfParts) {
		try {
			FileWriter fileWriter = new FileWriter("c:\\resultVector.csv");
			fileWriter.append("Name ID,x,y,Rotation,width,length,reachability");
			for ( int i = 0; i < layoutSchedulerResults.size(); i++ ) {
				String[] splitArray = layoutSchedulerResults.get(i).split("@");
				splitArray[2] = splitArray[2].replace("[", "@").replace("]", "");
				String[] splitPoint = splitArray[2].split("@");
				fileWriter.append(splitArray[1] + "," + splitPoint[1] + "," + splitArray[3] + sizeOfParts.get(i) + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		}
		catch ( IOException e ) {
			LOGGER.error("file writer exception: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 
	 * @param initialPlanninginput
	 * @return
	 */
	private boolean checkIfPartExists(PLANNINGINPUT initialPlanninginput) {
		for ( TASK aTask : initialPlanninginput.getTASKS().getTASK() )
			for ( PROPERTY property : aTask.getPROPERTIES().getPROPERTY() )
				if ( property.getVALUE().equals(MapToResourcesAndTasks.TABLE_PART_PROPERTY_VALUE_PART) ) return true;
		return false;
	}

	private void createResourcesAndPartsMapingForSR(Vector<AssignmentDataModel> layoutAssignments) {
		resourcesAndPartsMapingForSR = new HashMap<String, Point3D>();
		for ( AssignmentDataModel anAssignment : layoutAssignments ) {
			resourcesAndPartsMapingForSR.put(anAssignment.getTaskDataModel().getTaskId(), MapParameters.getLocationFromResourceDataModel3D(anAssignment.getResourceDataModel()));
		}

		String[] filter;
		for ( TASK aTask : planningInput.getTASKS().getTASK() ) {
			if ( !resourcesAndPartsMapingForSR.containsKey(aTask.getId()) ) {
				filter = aTask.getDESCRIPTION().split(" ");
				for ( TASK bTask : planningInput.getTASKS().getTASK() )
					if ( bTask.getDESCRIPTION().startsWith(filter[0])
							&& resourcesAndPartsMapingForSR.containsKey(bTask.getId()) )
						resourcesAndPartsMapingForSR.put(aTask.getId(), resourcesAndPartsMapingForSR.get(bTask.getId()));
			}
		}
	}

	/**
	 * 
	 */
	private void createResourcesAndPartsMapingForSRForUnfilteredInput() {
		String[] filter;
		resourcesAndPartsMaping = Maps.mapList();
		for ( TASK aTask : planningInput.getTASKS().getTASK() ) {
			LOGGER.trace("first task ids= " + aTask.getId() + " " + aTask.getNAME() + " " + aTask.getDESCRIPTION());

			if ( !resourcesAndPartsMaping.containsKey(aTask.getId()) ) {
				filter = aTask.getDESCRIPTION().split(" ");
				for ( TASK bTask : planningInput.getTASKS().getTASK() )
					if ( bTask.getDESCRIPTION().startsWith(filter[0])
							&& resourcesAndPartsMaping.containsKey(bTask.getId()) ) {
						resourcesAndPartsMaping.add(aTask.getId(), resourcesAndPartsMaping.getList(bTask.getId()).get(0));
						resourcesAndPartsMaping.add(aTask.getId(), resourcesAndPartsMaping.getList(bTask.getId()).get(1));
					}
			}
		}
	}

	/**
	 * 
	 * @param filePath
	 * @param planningInputDocument
	 */
	private void createInputXML(String filePath, Document planningInputDocument) {
		String msg = ".createInputXML(): ";
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(planningInputDocument);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		}
		catch ( Exception e ) {
			RuntimeException anException = new RuntimeException("Error reading input", e);
			LOGGER.error(msg, e);
			throw anException;
		}
	}

	private static final double	CONSTANT				= -2.543;
	private static final double	HUMAN_HEIGHT_CONSTANT	= 62.86;

	private Vector<String> findHeightForParts(PLANNINGINPUT initialPlanninginput) {
		Vector<String> heightForParts = new Vector<String>();
		Map<String, Double> resourcesWithHeight = new HashMap<String, Double>();
		for ( RESOURCE aResource : initialPlanninginput.getRESOURCES().getRESOURCE() ) {
			for ( PROPERTY aProperty : aResource.getPROPERTIES().getPROPERTY() ) {
				if ( aProperty.getVALUE().equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN) ) {
					for ( PROPERTY bProperty : aResource.getPROPERTIES().getPROPERTY() ) {
						if ( bProperty.getNAME().equals(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME) ) {
							resourcesWithHeight.put(aResource.getId(), Convert.getDouble(bProperty.getVALUE()));
							break;
						}
					}
				}
			}
		}

		Map<String, String> partsWithIDsAndName = new HashMap<String, String>();
		for ( TASK aTask : initialPlanninginput.getTASKS().getTASK() ) {
			for ( PROPERTY aProperty : aTask.getPROPERTIES().getPROPERTY() ) {
				if ( aProperty.getNAME().equals(MapToResourcesAndTasks.TABLE_PART_PROPERTY_NAME) ) {
					if ( aProperty.getVALUE().equals(MapToResourcesAndTasks.TABLE_PART_PROPERTY_VALUE_PART) )
						partsWithIDsAndName.put(aTask.getId(), aTask.getDESCRIPTION());
					break;
				}
			}
		}

		for ( TASKSUITABLERESOURCE taskSuitableResource : initialPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE() ) {
			if ( partsWithIDsAndName.containsKey(taskSuitableResource.getTASKREFERENCE().getRefid())
					&& resourcesWithHeight.containsKey(taskSuitableResource.getRESOURCEREFERENCE().getRefid()) ) {
				double partHeight = HUMAN_HEIGHT_CONSTANT
						* resourcesWithHeight.get(taskSuitableResource.getRESOURCEREFERENCE().getRefid()) + CONSTANT;
				String value = "height@" + partsWithIDsAndName.get(taskSuitableResource.getTASKREFERENCE().getRefid())
						+ " " + taskSuitableResource.getTASKREFERENCE().getRefid() + "@"
						+ String.valueOf(partHeight / 100);
				heightForParts.add(value);

			}
		}
		return heightForParts;
	}
}
