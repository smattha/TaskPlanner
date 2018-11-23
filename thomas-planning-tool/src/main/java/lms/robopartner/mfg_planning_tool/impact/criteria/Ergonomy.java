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
 * Used for the evaluation of the ergonomy of the tasks executed by the human operator. 
 * If a task is executed by the robot, its value is maximized. 
 * The evaluation is based on NIOSH lifting equation (http://ergo-plus.com/niosh-lifting-equation-single-task/).
 * 
 * The //TODO commented lines should be enabled when the code is to run with the
 * layout that takes into consideration the z axis as well
 * 
 * @author Jason
 *
 */
public class Ergonomy extends AbstractCriterion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Ergonomy.class);
    private static final String CRITERION_NAME = "ERGONOMY";
    private Map<String, Point3D> resourcesAndPartsMapingForSR;
    public Point3D previousHumanPosition;
    public boolean firstTask=true;
    public double basePartX=0;
    public double basePartY=0;
    //TODO the below commented line(s) are the 3D criteria changes
    //public double basePartZ=0;
    public double basePartWidth=0;
    public double basePartHeight=0;
    //TODO the below commented line(s) are the 3D criteria changes
    //public double basePartZDimension=0;
    public double vectorX=0;
    public double vectorY=0;
    public double AM=0;
    
 

    /**
     * Hide constructor
     */
    @SuppressWarnings("unused")
    private Ergonomy() {
        super();
    }

    /**
     * @param resourcesAndPartsMapingForSR
     */
    public Ergonomy(Map<String, Point3D> resourcesAndPartsMapingForSR) {
        super();
        this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
    }

    @Override
    public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
        
    	String msg = ".getValue(): ";
        // Each path is a solution. There are SR paths provided each time.
        int sr = paths.size();
        double ergonomySum = 0;
        double partialErgonomy=0;
        
        
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
                    	RuntimeException runtimeException = new RuntimeException(msg2);
                    	logger.error(msg2,runtimeException);
                    	logger.error(msg2);
                    	throw runtimeException;
                    }
                    if (missingResource) {
                        String msg2 = msg + "No resource mapping found for resource with id=" + resourceId;
                        RuntimeException runtimeException=new RuntimeException(msg2);
                        logger.error(msg2,runtimeException);
                        logger.error(msg2);
                        throw runtimeException;
                    }
                    double partialErgonomyTemp=0;
                    if (missingTask || missingResource) {
//                        // If a resource or task is missing, this solution is
//                        // totally bad.
//                        partialAreaCost = Double.MAX_VALUE;
                        logger.debug(msg+"Missing Task Or Resource");
                    }
                    else {
                    	double LC=23;
                    	                   			
            			/*
            			 * vertical multiplier is dependent on the height of the part to be picked up. for 30inch=1
            			 */
            			double VM=1;
            			/*
            			 * Distance multiplier is the vertical distance to be traveled by the part
            			 */
            			double DM=1;
            			/*
            			 * Frequency multiplier is dependent on the hours of work/day and the lifts/min (production rate). possibly consider using user input
            			 * for 8hours and 1 lift/min=0.75
            			 */
            			double FM=0.75;
            			/*
            			 * Coupling multiplier used to identify the robustness of the grip that will be achieved
            			 */
            			double CM = 0.9;
            			double centerXTask;
                    	double centerYTask;
                    	double centerXResource;
                    	double centerYResource;
                    	
                        Point3D taskPoint3D = resourcesAndPartsMapingForSR.get(taskId);
                        double xTask = taskPoint3D.getX();
                        double yTask = taskPoint3D.getY();

                        Point3D resourcePoint3D = resourcesAndPartsMapingForSR.get(resourceId);
                        double xResource = resourcePoint3D.getX();
                        double yResource = resourcePoint3D.getY();
                        
                        double widthTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
                        double heightTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
                        //TODO the below commented line(s) are the 3D criteria changes
                        //double zTask = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));
                        double widthResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
                        double heightResource = Double.parseDouble(assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
                      
                        
                        centerXTask = xTask+(widthTask/2);
                        centerYTask = yTask+(heightTask/2);
                        centerXResource = xResource+(widthResource/2);
                        centerYResource = yResource+(heightResource/2);
                        
                        double distance = Math.sqrt(Math.pow((centerXResource - centerXTask), 2) + Math.pow((centerYResource - centerYTask), 2));
                        /*
            			 * Horizontal multiplier is dependent on the distance of the part to be picked up and the operator's ankles
            			 */
            			double HM=0;
            			if (distance <= 0.70)
                        {
                            if (distance <= 0.25)
                            {
                                HM = 1;
                            }
                            if ((distance > 0.25) && (distance <= 0.28))
                            {
                                HM = 0.91;
                            }
                            if ((distance > 0.28) && (distance <= 0.30))
                            {
                                HM = 0.83;
                            }
                            if ((distance > 0.28) && (distance <= 0.33))
                            {
                                HM = 0.77;
                            }
                            if ((distance > 0.28) && (distance <= 0.35))
                            {
                                HM = 0.71;
                            }
                            if ((distance > 0.28) && (distance <= 0.38))
                            {
                                HM = 0.67;
                            }
                            if ((distance > 0.28) && (distance <= 0.40))
                            {
                                HM = 0.63;
                            }
                            if ((distance > 0.28) && (distance <= 0.43))
                            {
                                HM = 0.59;
                            }
                            if ((distance > 0.28) && (distance <= 0.46))
                            {
                                HM = 0.56;
                            }
                            if ((distance > 0.28) && (distance <= 0.48))
                            {
                                HM = 0.53;
                            }
                            if ((distance > 0.28) && (distance <= 0.51))
                            {
                                HM = 0.5;
                            }
                            if ((distance > 0.28) && (distance <= 0.53))
                            {
                                HM = 0.48;
                            }
                            if ((distance > 0.28) && (distance <= 0.56))
                            {
                                HM = 0.46;
                            }
                            if ((distance > 0.28) && (distance <= 0.58))
                            {
                                HM = 0.44;
                            }
                            if ((distance > 0.28) && (distance <= 0.61))
                            {
                                HM = 0.42;
                            }
                            if ((distance > 0.28) && (distance <= 0.63))
                            {
                                HM = 0.4;
                            }
                            if (distance > 0.63)
                            {
                                HM = 0;
                            }
                            
                        }
                        
                    	if (assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) 
                    	{
                    		if (firstTask)
                    		{
                    			firstTask=false;
                    			partialErgonomy = 1;
                                basePartX=centerXTask;
                                basePartY=centerYTask; 
                                //TODO the below commented line(s) are the 3D criteria changes
                                //basePartZ=zTask;
                                basePartWidth=widthTask;
                                basePartHeight=heightTask; 
                    		}
                    		else
                    		{
                    			if (centerYResource>(basePartY+basePartHeight)){
                    				vectorX= 0;
                    				vectorY= -1;
                    			}else if (centerYResource<(basePartY-basePartHeight)) {
                    				vectorX= 0;
                    				vectorY= 1;
								}else {
									if (centerXResource>(basePartX+basePartWidth)) {
										vectorY= 0;
										vectorX= -1;
									}
									else if (centerXResource<(basePartX+basePartWidth)) {
										vectorY= 0;
										vectorX= 1;
									}
								}
                    			/*
                    			 * Assymetry multiplier is dependent on the rtation the body of the operator is asked to perform.
                    			 * if this was the first task, the operator is considered looking at the part and no rotation of the body will occur.
                    			 * in any other case the rotation is calculated as below.
                    			 */
                    			double PartVectorX = centerXTask-basePartX;
                    			double PartVectorY = centerYTask-basePartY;
                    			double angle = (vectorX*PartVectorX+vectorY*PartVectorY)/(Math.sqrt(Math.pow(vectorX, 2)+Math.pow(vectorY, 2))*(Math.sqrt(Math.pow(PartVectorX, 2)+Math.pow(PartVectorY, 2))));
                    			if ((angle>=0)&&(angle<15)){
                    				AM=1;
                    			}
                    			if ((angle>=15)&&(angle<30)){
                    				AM=0.95;
                    			}
                    			if ((angle>=30)&&(angle<45)){
                    				AM=0.9;
                    			}
                    			if ((angle>=45)&&(angle<60)){
                    				AM=0.86;
                    			}
                    			if ((angle>=60)&&(angle<75)){
                    				AM=0.81;
                    			}
                    			if ((angle>=75)&&(angle<90)){
                    				AM=0.76;
                    			}
                    			if ((angle>=90)&&(angle<105)){
                    				AM=0.71;
                    			}
                    			if ((angle>=105)&&(angle<120)){
                    				AM=0.66;
                    			}
                    			if ((angle>=120)&&(angle<135)){
                    				AM=0.62;
                    			}
                    			if (angle>135){
                    				AM=0;
                    			}
                    			/*
                        			 * vertical multiplier is dependent on the height of the part to be picked up relative to the floor. 
                        			 * in the best case scenario the part is placed in a height that corresponds to 30 inches = 889 mm
                        			 */
                    				//TODO the below commented line(s) are the 3D criteria changes
//                    				if (zTask<=0){
//			            				VM = 0;
//			            			}else if ((zTask>0)&&(zTask<127)) {
//			            				VM = 0.78;
//									}else if ((zTask>=127)&&(zTask<254)) {
//			            				VM = 0.81;
//									}else if ((zTask>=254)&&(zTask<381)) {
//			            				VM = 0.85;
//									}else if ((zTask>=381)&&(zTask<508)) {
//			            				VM = 0.89;
//									}else if ((zTask>=508)&&(zTask<635)) {
//			            				VM = 0.93;
//									}else if ((zTask>=635)&&(zTask<762)) {
//			            				VM = 0.96;
//									}else if ((zTask>=762)&&(zTask<889)) {
//			            				VM = 1;
//									}else if ((zTask>=889)&&(zTask<1016)) {
//			            				VM = 0.96;
//									}else if ((zTask>=1016)&&(zTask<1143)) {
//			            				VM = 0.93;
//									}else if ((zTask>=1143)&&(zTask<1270)) {
//			            				VM = 0.89;
//									}else if ((zTask>=1270)&&(zTask<1397)) {
//			            				VM = 0.85;
//									}else if ((zTask>=1397)&&(zTask<1524)) {
//			            				VM = 0.81;
//									}else if ((zTask>=1524)&&(zTask<1651)) {
//			            				VM = 0.78;
//									}else if ((zTask>=1651)&&(zTask<1778)) {
//			            				VM = 0.74;
//									}else if ((zTask>=1778)&&(zTask<1905)) {
//			            				VM = 0.7;
//									}else if (zTask>=1905) {
//			            				VM = 0;
//									}
                    				/*
                        			 * Distance multiplier expresses the vertical distance the part has to travel. 
                        			 * it takes greater value the closer it is to 0.
                        			 * at a distanc greater than 70 inch = 1778 mm the value distance multiplier takes the value of 0
                        			 */
//                    				double D = Math.abs(basePartZ-zTask);
//			            			if (D<=254){
//			            				DM = 1;
//			            			}else if ((D>254)&&(D<=381)) {
//			            				DM = 0.94;
//									}else if ((D>381)&&(D<=508)) {
//										DM = 0.91;
//									}else if ((D>508)&&(D<=635)) {
//										DM = 0.89;
//									}else if ((D>635)&&(D<=762)) {
//										DM = 0.88;
//									}else if ((D>762)&&(D<=1016)) {
//										DM = 0.87;
//									}else if ((D>1016)&&(D<=1270)) {
//										DM = 0.86;
//									}else if ((D>1270)&&(D<=1778)) {
//										DM = 0.85;
//									}else if (D>1778) {
//										DM = 0;
//									}
                    				double LI = Double.parseDouble(assignment.getTask().getTaskDataModel().getProperty("Weight (Kg)"))/(LC*HM*VM*DM*AM*FM*CM);
                    				partialErgonomyTemp = (LI>=1) ? 0 : (1-LI);
							}
                    	}
                    	else if (assignment.getResource().getResourceDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
							if (firstTask) {
								firstTask=false;
								partialErgonomyTemp = 1;
								basePartX=centerXTask;
                                basePartY=centerYTask;  
								
							}
						}              		
                    	
                        
                    }
                    partialErgonomy += partialErgonomyTemp;

                    logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + Ergonomy.CRITERION_NAME + "=" + partialErgonomy);
                }
                //partialErgonomy = partialErgonomy/path.length;
                ergonomySum += partialErgonomy;
            }
        }
        ergonomySum = ergonomySum / (double) sr;
        logger.trace(msg + Ergonomy.CRITERION_NAME + "=" + ergonomySum + ".");
        return ergonomySum;
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
        return Ergonomy.CRITERION_NAME;
    }

}
