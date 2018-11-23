/**
 * 
 */
package lms.robopartner.mfg_planning_tool;

import java.math.BigInteger;

import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapParameters;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

import org.slf4j.LoggerFactory;



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

/**
 * The purpose of this class is to dynamically prepare the input for the use of the {@link LayoutScheduler}
 * For a demo run.
 * @author Spyros
 *
 */
 public class DemoPlanningGenerator {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DemoPlanningGenerator.class);
    public static double MAX_INVESTMENT_COST=1000000.0;
    public static double MIN_INVESTMENT_COST=1000000.0;
    public static double MIN_RESOURCE_FLOOR_SPACE = 0;
    public static double MIN_FLOORSPACE_TASKS=0;
  

    public static PROPERTIES getPROPERTIES(String resourceType, double payloadKg, double costEuro, double floorSpaceM2, double speedMMS, int width, int height, double reachability,int zheight) {
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
        //MAX_INVESTMENT_COST += costEuro;
        if (MIN_INVESTMENT_COST>costEuro){
        	MIN_INVESTMENT_COST=costEuro;
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
        property1.setVALUE(height + "");
        
        PROPERTY propertyZHeight = myObjectFactory.createPROPERTY();
        propertyZHeight.setNAME(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME);
        propertyZHeight.setVALUE(zheight + "");
        

        PROPERTY property2 = myObjectFactory.createPROPERTY();
        property2.setNAME(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME);
        property2.setVALUE(reachability + "");

        resourceProperties.getPROPERTY().add(resourceProperty1);
        resourceProperties.getPROPERTY().add(resourceProperty2);
        resourceProperties.getPROPERTY().add(resourceProperty3);
        resourceProperties.getPROPERTY().add(resourceProperty5);
        resourceProperties.getPROPERTY().add(property);
        resourceProperties.getPROPERTY().add(property1);
        resourceProperties.getPROPERTY().add(property2);
        resourceProperties.getPROPERTY().add(propertyZHeight);
        resourceProperties.getPROPERTY().add(isRobotProperty);
        return resourceProperties;
    }
    

    /**
     * @return
     */
    public static final PLANNINGINPUT getDemoPlanningInput() {
        String msg = ".getDemoPlanningInput(): ";
        try {

            return DemoPlanningGenerator.getResourcesAndTasksOriginalDemoPlanninginput();

        }
        catch (Exception e) {
            RuntimeException rte = new RuntimeException(msg, e);
            logger.debug(msg, rte);
            throw rte;
        }

    }

    /**
     * @param maxTasks
     * @return
     */
    public static TASKS getTasks(int maxTasks) {
        ObjectFactory myObjectFactory = new ObjectFactory();
        TASKS tasks = myObjectFactory.createTASKS();

        for (int i = 0; i < maxTasks; i++) {
            BigInteger id = IDGenerator.getNewID();
            TASK aTask = MapToResourcesAndTasks.getTask("Task Axle Load" + id, "Axle " + id, id + "", (int) Math.ceil(500/500.0), (int) Math.ceil(600/500.0), (int) Math.ceil(600/500.0), 25.0,true,1);
           
            
            id = IDGenerator.getNewID();
            TASK bTask = MapToResourcesAndTasks.getTask("Task Drum" + id, "Drum " + id, id + "", (int) Math.ceil(1400/500.0), (int) Math.ceil(700/500.0), (int) Math.ceil(1400/500.0), 11.0,false,1);
            
            id = IDGenerator.getNewID();
            TASK cTask = MapToResourcesAndTasks.getTask("Task Cable" + id, "Cable " + id, id + "", (int) Math.ceil(100/500.0), (int) Math.ceil(100/500.0), (int) Math.ceil(100/500.0), 1.0,false,1);
            
            id = IDGenerator.getNewID();
            TASK dTask = MapToResourcesAndTasks.getTask("Task Screws" + id, "Screws " + id, id + "", (int) Math.ceil(100/500.0), (int) Math.ceil(100/500.0), (int) Math.ceil(100/500.0), 1.0,false,1);
            tasks.getTASK().add(aTask);
            tasks.getTASK().add(bTask);
            tasks.getTASK().add(cTask);
            tasks.getTASK().add(dTask);
        }
        return tasks;

    }

    /**
     * @param maxResources
     * @return
     */
    public static RESOURCES getResources(int maxResources) {
        ObjectFactory myObjectFactory = new ObjectFactory();
        RESOURCES resources = myObjectFactory.createRESOURCES();

        for (int i = 0; i < maxResources; i++) {
        	// 1 -> 500
            BigInteger id = IDGenerator.getNewID();
            //TODO set suitable height
            PROPERTIES props1 = DemoPlanningGenerator.getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 370.0, 20000.0, (int) Math.ceil((1400*2500)/(500.0*500.0)), (1000.0/500.0), (int) Math.ceil(1400/500.0), (int) Math.ceil(2500/500.0) , (3000/500.0),1);
            RESOURCE aResource1 = MapToResourcesAndTasks.getResource("Resource" + id, "The Resource " + id, id + "", props1);
            
//            id = IDGenerator.getNewID();
//            PROPERTIES props3 = DemoPlanningGenerator.getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT, 70.0, 20000.0, 2.0, 10.0, 1, 1 ,2);
//            RESOURCE aResource3 = MapToResourcesAndTasks.getResource("Resource" + id, "The Resource " + id, id + "", props3);
            
            id = IDGenerator.getNewID();
            //TODO set suitable height
            PROPERTIES props2 = DemoPlanningGenerator.getPROPERTIES(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN, 11.0, 200.0, (int) Math.ceil((500*600)/(5000*500.0)), (500.0/500.0), (int) Math.ceil(500/500.0), (int) Math.ceil(600/500.0) , MapParameters.MAP_WIDTH,1);
            RESOURCE aResource2= MapToResourcesAndTasks.getResource("Resource" + id, "The Resource " + id, id + "", props2);
            resources.getRESOURCE().add(aResource1);
            resources.getRESOURCE().add(aResource2);
//            resources.getRESOURCE().add(aResource3);
        }
        return resources;

    }

    /**
     * Helper method. Make all resources suitable for all tasks
     * 
     * @param theResources
     * @param theTasks
     * @param setupcode
     * @param operationTimePerBatchSeconds
     * @return
     */
    public static TASKSUITABLERESOURCES getTaskSuitableResources(
        RESOURCES theResources, TASKS theTasks, String setupcode,
        int operationTimePerBatchSeconds) {
        ObjectFactory myObjectFactory = new ObjectFactory();
        TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
        for (RESOURCE aResource : theResources.getRESOURCE()) {
            for (TASK aTask : theTasks.getTASK()) {
            	if (!(aTask.getNAME().equals("Axle 2"))){
            		if (aResource.getNAME().equals("The Resource 7")){
                TASKSUITABLERESOURCE tasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource, aTask, setupcode, operationTimePerBatchSeconds);
                tasksuitableresource.setPROPERTIES(aTask.getPROPERTIES());
                tasksuitableresources.getTASKSUITABLERESOURCE().add(tasksuitableresource);
            		}
            	}
            	if (!(aTask.getNAME().equals("Cable 4"))){
            		if (aResource.getNAME().equals("The Resource 6")){
                TASKSUITABLERESOURCE tasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(aResource, aTask, setupcode, operationTimePerBatchSeconds);
                tasksuitableresource.setPROPERTIES(aTask.getPROPERTIES());
                tasksuitableresources.getTASKSUITABLERESOURCE().add(tasksuitableresource);
            		}
            	}
            }
        }

        myObjectFactory = null;

        return tasksuitableresources;
    }

    /**
     * @return
     */
    public static PLANNINGINPUT getResourcesAndTasksOriginalDemoPlanninginput() {
        String msg = ".getDemoPlanninginput(): ";
        String id = null;
        logger.trace(msg);
        id = IDGenerator.getNewID() + "";
        PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2014, 1, 2, 2018, true);

        logger.trace(msg + "Created aPlanningInput");

        TASKS theTasks = DemoPlanningGenerator.getTasks(1);
        logger.trace(msg + "Created theTasks");

        LayoutPlanningInputGenerator.addTaskPrecedenceConstraints(aPlanninginput, theTasks);
        logger.trace(msg + "Created Constraints");
        aPlanninginput.setTASKS(theTasks);
        RESOURCES theResources = DemoPlanningGenerator.getResources(1);
        logger.trace(msg + "Created theResources");
        aPlanninginput.setRESOURCES(theResources);
        aPlanninginput.setTASKSUITABLERESOURCES(DemoPlanningGenerator.getTaskSuitableResources(theResources, theTasks, LayoutPlanningInputGenerator.SETUP_CODE, LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS));
        logger.trace(msg + "Created setTASKSUITABLERESOURCES");

        LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, theResources, AbstractAlgorithm.MULTICRITERIA);
        logger.trace(msg + "Created Workcenters");

        DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
        DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);

        logger.trace(msg + "Created JObs");
        LayoutPlanningInputGenerator.addJobs(aPlanninginput, theTasks, aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);
        double height=0;
        double width=0;
        for (TASKSUITABLERESOURCE e : aPlanninginput.getTASKSUITABLERESOURCES().getTASKSUITABLERESOURCE()){
        	for (PROPERTY p : e.getPROPERTIES().getPROPERTY()){
                if (p.getNAME()=="HEIGHT"){
                	height=Double.parseDouble(p.getVALUE());
                }
                if (p.getNAME()=="WIDTH"){
                	width=Double.parseDouble(p.getVALUE());
                }
            }
        	MIN_FLOORSPACE_TASKS=height*width;
        	height=0;
        	width=0;
        }

        return aPlanninginput;

    }

}
