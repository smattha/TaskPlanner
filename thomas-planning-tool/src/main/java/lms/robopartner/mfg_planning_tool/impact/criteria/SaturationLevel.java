package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;


/**
 * The saturation is the value of the time that the robot is 
 * occupied by a task compared to the overall time of the assignment.
 * 
 * The //TODO commented lines should be enabled when the code is to run with the
 * layout that takes into consideration the z axis as well
 * 
 * @author Jason
 *
 */
public class SaturationLevel extends AbstractCriterion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Fatigue.class);
    private static final String CRITERION_NAME = "SATURATION LEVEL";
    private Map<String, Point3D> resourcesAndPartsMapingForSR;
    public double humanCounter=0;
    double robotTimeToComplete=0;
    double humanTimeToComplete=0;
 

    /**
     * Hide constructor
     */
    @SuppressWarnings("unused")
    private SaturationLevel() {
        super();
    }

    /**
     * @param resourcesAndPartsMapingForSR
     */
    public SaturationLevel(Map<String, Point3D> resourcesAndPartsMapingForSR) {
        super();
        this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;        
    }

    @Override
    public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
        
    	String msg = ".getValue(): ";
        // Each path is a solution. There are SR paths provided each time.
        int sr = paths.size();
        double saturationLevelSum = 0;
        double partialSaturationLevel=0;
        
        
        for (int i = 0; i < sr; i++) {
            logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
            TreeNode[] path = paths.get(i);

            // path is solution
            for (int j = 0; j < path.length; j++) {
                LayerNode node = (LayerNode) path[j];
                for (Assignment assignment : node.getNodeAssignments()) {
                    String taskId = assignment.getTask().getTaskId();
                    String resourceId = assignment.getResource().getResourceId();

                    // calculate
                    boolean missingTask = !resourcesAndPartsMapingForSR.containsKey(taskId);
                    boolean missingResource = !resourcesAndPartsMapingForSR.containsKey(resourceId);
                    if (missingTask) {
                        String msg2 = msg + "No task mapping found for task with id=" + taskId;
                         RuntimeException runtimeException=new
                         RuntimeException(msg2);
                         logger.error(msg2,runtimeException);
                        logger.error(msg2);
                         throw runtimeException;
                    }
                    if (missingResource) {
                        String msg2 = msg + "No resource mapping found for resource with id=" + resourceId;
                         RuntimeException runtimeException=new
                         RuntimeException(msg2);
                         logger.error(msg2,runtimeException);
                        logger.error(msg2);
                         throw runtimeException;
                    }
                    double partialSaturationLevelTemp=0;
                    if (missingTask || missingResource) {
//                        // If a resource or task is missing, this solution is
//                        // totally bad.
//                        partialAreaCost = Double.MAX_VALUE;
                        logger.debug(msg+"Missing Task Or Resource");
                    }
                    else {
                    	double centerXTask;
                    	double centerYTask;
                    	double centerXResource;
                    	double centerYResource;
                    	
                        Point3D taskPoint3D = resourcesAndPartsMapingForSR.get(taskId);
                        double xTask = taskPoint3D.getX();
                        double yTask = taskPoint3D.getY();
                        //TODO the below commented line(s) are the 3D criteria changes
//                      double zTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));

                      Point3D resourcePoint3D = resourcesAndPartsMapingForSR.get(resourceId);
                      double xResource = resourcePoint3D.getX();
                      double yResource = resourcePoint3D.getY();
                    //TODO the below commented line(s) are the 3D criteria changes
//                      double zResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));
                      
                      double widthTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
                      double heightTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
                      double widthResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
                      double heightResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
                      
                      centerXTask = xTask+(widthTask/2);
                      centerYTask = yTask+(heightTask/2);
                      centerXResource = xResource+(widthResource/2);
                      centerYResource = yResource+(heightResource/2);  
                      
                      double distance = Math.sqrt(Math.pow((centerXResource-centerXTask), 2)+Math.pow((centerYResource-centerYTask), 2));
                    //TODO the below commented line(s) are the 3D criteria changes
//                      double distance = Math.sqrt(Math.pow((centerXResource-centerXTask), 2)+Math.pow((centerYResource-centerYTask), 2)+Math.pow((zResource-zTask), 2));
                     
                      double timeToCompleteMovement = distance/(Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty("Speed (mm/s)"))/1);
                    	if (assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)){
                    		robotTimeToComplete = timeToCompleteMovement;        
                    			
                    /* This has to be specific to the resource. Saturation levels should be created for all resources and the minimum will be examined as a criterion 
                     * */
                     
                    }else if (assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
                    	humanTimeToComplete = timeToCompleteMovement;
					}
                    	}
                    partialSaturationLevelTemp = robotTimeToComplete/(robotTimeToComplete+humanTimeToComplete);
                    partialSaturationLevel += partialSaturationLevelTemp;

                    logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + SaturationLevel.CRITERION_NAME + "=" + partialSaturationLevel);
                }
                partialSaturationLevel = partialSaturationLevel/path.length;
                saturationLevelSum += partialSaturationLevel;
            }
        }
        //saturationLevelSum = saturationLevelSum / (double) sr;
        logger.trace(msg + SaturationLevel.CRITERION_NAME + "=" + saturationLevelSum + ".");
        return saturationLevelSum;
    }

    @Override
    public double getWeight() {
        return 1;
    }

    @Override
    public boolean isBenefit() {
        return true;
    }

    @Override
    public String getCriterionName() {
        return SaturationLevel.CRITERION_NAME;
    }

}
