/*
 * package lms.robopartner.mfg_planning_tool.api;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import org.slf4j.LoggerFactory;
 * 
 * 
 * import eu.robopartner.datamodel.HumanAssignement; import
 * eu.robopartner.datamodel.HumanResource; import
 * eu.robopartner.datamodel.ManufacturingAssignement; import
 * eu.robopartner.datamodel.Order; import
 * eu.robopartner.datamodel.RoboPartnerDataModelFactory; import
 * eu.robopartner.datamodel.RobotAssignement; import
 * eu.robopartner.datamodel.RobotResource;
 * 
 *//**
	 * @author Spyros Koukas
	 *
	 */
/*
 * public class TaskPlannerController { private static org.slf4j.Logger logger =
 * LoggerFactory.getLogger(TaskPlannerController.class); private static int
 * instanceCounter = 0; private static int instanceCounterMax = 0; private
 * RoboPartnerDataModelFactory aRoboPartnerDataModelFactory;
 * 
 *//**
	 * @param aRoboPartnerDataModelFactory
	 * @author Spyros Koukas
	 */
/*
 * public TaskPlannerController(final RoboPartnerDataModelFactory
 * aRoboPartnerDataModelFactory) { super();
 * 
 * String msg=".TaskSchedulerController(): "; this.aRoboPartnerDataModelFactory
 * = aRoboPartnerDataModelFactory; instanceCounter++; instanceCounterMax++;
 * 
 * logger.trace(msg + " new instance created" + instanceCounter + " of " +
 * instanceCounterMax + " active.");
 * 
 * }
 * 
 * 
 *//**
	 * @param anOrder
	 * @param availableHumanResources
	 * @param availableRobotResources
	 * @return List of Manufacturing Assignements, which is a list of
	 *         {@link RobotAssignement} and {@link HumanAssignement}
	 *//*
		 * public List<ManufacturingAssignement> getNewTaskSchedule(Order anOrder,
		 * List<HumanResource> availableHumanResources,List<RobotResource>
		 * availableRobotResources) {
		 * 
		 * String msg=".getNewTaskSchedule(): "; logger.trace(msg+"Start");
		 * List<ManufacturingAssignement> results = new
		 * ArrayList<ManufacturingAssignement>(); //TODO add code here to fill the
		 * results. HumanAssignement and RobotAssignement can be added to the results.
		 * //TODO use RoboPartnerAutogeneratedFactory only to create RobotAssignement,
		 * HumanAssignement. //EXAMPLE DELETE THEM HumanAssignement
		 * aHumanAssignement=null; RobotAssignement aRobotAssignement=null;
		 * results.add(aRobotAssignement); results.add(aHumanAssignement);
		 * 
		 * return results;
		 * 
		 * }
		 * 
		 * 
		 * 
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#finalize()
		 * 
		 * @Override protected void finalize() throws Throwable { super.finalize();
		 * instanceCounter--; this.aRoboPartnerDataModelFactory=null; String msg =
		 * ".finalize(): "; logger.trace(msg + "instance deleted." + instanceCounter +
		 * " of " + instanceCounterMax + " active."); } }
		 */