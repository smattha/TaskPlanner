/**
 * 
 */
package lms.robopartner.datamodel.map.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import lms.robopartner.datamodel.map.utilities.DataModelToAWTHelper;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

/**
 * @author Spyros This class makes the following assumptions: + Objects that are
 *         modeled by Tasks of given size are place into a floor which is
 *         modeled as a grid. Grid squares are the resources. + All objects and
 *         floor space are orthogonal (width*height) + Location are always
 *         modeled as the upper-left corner of objects and map. + (0,0) is the
 *         upper-left corner of the grid. + Objects cannot overlap. For this
 *         reason this class checks assignments and considers the resources
 *         (grid squares) that are occupied by an object as unavailable.
 * 
 */
public class LayoutEvaluator {
    private static org.slf4j.Logger logger = LoggerFactory
        .getLogger(LayoutEvaluator.class);

    public static boolean isSuitabilityValid(Point resourcePoint, Dimension taskDimension, int taskReachability, Vector<AssignmentDataModel> existingAssignements) {
        String msg = ".isSuitabilityValid(): ";
        Rectangle assignementRectangle = new Rectangle(resourcePoint, taskDimension);

        // if the area defined by the task-object size and the resource's
        // location is occupied by the union of the areas defined by the
        // dimensions of the task and location of resources in previous
        // assignments
        Rectangle mapRectangle = new Rectangle(new Dimension(MapParameters.MAP_WIDTH, MapParameters.MAP_HEIGHT));

        boolean isSuitable = true;
        int assignmentsWithinReachability = 0;
        int assignmentshumanPositionNotBetweenRobotAndParts = 0;
        if (mapRectangle.contains(assignementRectangle)) {
            // the boundaries of the tasks-object fall inside the maps area.
            // All previous assignments must be checked.
            Rectangle currentTaskReachabilityRectangle = LayoutEvaluator.getReachabilityRectangle(assignementRectangle, taskReachability);

            for (AssignmentDataModel anAssignement : existingAssignements) {
                Rectangle theOtherRectangle = DataModelToAWTHelper.createRectangle(anAssignement.getResourceDataModel(), anAssignement.getTaskDataModel());

                // Rectangles are not one on another
                isSuitable = !assignementRectangle.intersects(theOtherRectangle);

                // if it is not suitable break
                if (!isSuitable) {
                    logger.trace(msg + "Rectangle=" + assignementRectangle + " isSuitable=" + isSuitable + " [intersects Renctangle:" + theOtherRectangle + "]");
                    break;
                }
                else {
                    // else check reachability

                    Rectangle anotherReachabilityRectangle = LayoutEvaluator.getReachabilityRectangle(theOtherRectangle, Integer.parseInt(anAssignement.getTaskDataModel().getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)));
                    if (LayoutEvaluator.areWithinReachability(currentTaskReachabilityRectangle, anotherReachabilityRectangle)) {
                        assignmentsWithinReachability++;
                    }
                    if (LayoutEvaluator.humanPositionNotBetweenRobotAndParts(existingAssignements)) {
                        assignmentshumanPositionNotBetweenRobotAndParts++;
                    }
                }
            }

            // after all existing assignments have been checked
            // and no collisions have been detected, there should be at least
            // one task within reachability
            isSuitable = isSuitable && (existingAssignements.size() == 0 || (assignmentsWithinReachability > 0 && assignmentshumanPositionNotBetweenRobotAndParts > 0));

        }
        else {
            // the boundaries of the tasks-object fall outside the maps area.
            isSuitable = false;
            logger.trace(msg + "Rectangle=" + assignementRectangle
                + " isSuitable=" + isSuitable + " [out of bounds]");
        }
        if (isSuitable) {
            logger.trace(msg + "Rectangle=" + assignementRectangle
                + " isSuitable=" + isSuitable);
        }
        return isSuitable;
    }

    /**
     * @param resourcePoint
     * @param taskDimension
     * @param existingAssignements
     * @param taskReachability
     * @return
     */
    public static boolean isSuitabilityValid(Point resourcePoint, Dimension taskDimension, Vector<Assignment> existingAssignements, int taskReachability) {
        
    	String msg = ".isSuitabilityValid(): ";
        Rectangle assignementRectangle = new Rectangle(resourcePoint, taskDimension);

        // if the area defined by the task-object size and the resource's
        // location is occupied by the union of the areas defined by the
        // dimensions of the task and location of resources in previous
        // assignments
        Rectangle mapRectangle = new Rectangle(new Dimension(MapParameters.MAP_WIDTH, MapParameters.MAP_HEIGHT));
        Rectangle currentTaskReachabilityRectangle = LayoutEvaluator.getReachabilityRectangle(assignementRectangle, taskReachability);

        boolean isSuitable = true;
        int assignmentsWithinReachability = 0;
        if (mapRectangle.contains(assignementRectangle)) {
            // the boundaries of the tasks-object fall inside the maps area.
            // All previous assignments must be checked.
            for (Assignment anAssignement : existingAssignements) {
                Rectangle theOtherRectangle = DataModelToAWTHelper.createRectangle(anAssignement.getResource().getResourceDataModel(), anAssignement.getTask().getTaskDataModel());

                isSuitable = !assignementRectangle.intersects(theOtherRectangle);

                // if it is not suitable brake
                if (!isSuitable) {
                    logger.trace(msg + "Rectangle=" + assignementRectangle + " isSuitable=" + isSuitable + " [intersects Renctangle:" + theOtherRectangle + "]");
                    break;
                }
                else {
                    // else check reachability

                    Rectangle anotherReachabilityRectangle = LayoutEvaluator.getReachabilityRectangle(theOtherRectangle, Integer.parseInt(anAssignement.getTask().getTaskDataModel().getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)));
                    if (LayoutEvaluator.areWithinReachability(currentTaskReachabilityRectangle, anotherReachabilityRectangle)) {
                        assignmentsWithinReachability++;
                    }
                }
            }

            // after all existing assignments have been checked
            // and no collisions have been detected, there should be at least
            // one task within reachability
            isSuitable = isSuitable && (existingAssignements.size() == 0 || assignmentsWithinReachability > 0);

        }
        else {
            // the boundaries of the tasks-object fall outside the maps area.
            isSuitable = false;
            logger.trace(msg + "Rectangle=" + assignementRectangle
                + " isSuitable=" + isSuitable + " [out of bounds]");
        }
        if (isSuitable) {
            logger.trace(msg + "Rectangle=" + assignementRectangle
                + " isSuitable=" + isSuitable);
        }
        return isSuitable;
    }

    /**
     * @param resource
     * @param task
     * @param existingAssignements
     * @return
     */
    public static boolean isSuitabilityValid(ResourceSimulator resource, TaskSimulator task, Vector<AssignmentDataModel> existingAssignements) {

        Point resourcePoint = DataModelToAWTHelper.getPointFromResource(resource);
        boolean isXYIndependentAndReachabilityMatching = LayoutEvaluator.isSuitabilityValid(resourcePoint, DataModelToAWTHelper.getDimensionFromTask(task), Integer.parseInt(task.getTaskDataModel().getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME)), existingAssignements);

        // Relationships and restriction between parts

        return isXYIndependentAndReachabilityMatching;
    }

    public static boolean reachabilityOfRectangles() {
        boolean areReachable = false;
        return areReachable;
    }

    /**
     * Returns a Rectangle defining the reachability bounding box for a Task
     * 
     * @param aTaskRectangle
     * @param reachability
     * @return
     */
    public static Rectangle getReachabilityRectangle(Rectangle aTaskRectangle, int reachability) {
        String msg = ".getReachability() ";
        if (reachability < 0) {
            throw new RuntimeException(msg + "reachability must be positive.");
        }
        int reachabilityWidth = Math.max(reachability, aTaskRectangle.width);
        int reachabilityHeight = Math.max(reachability, aTaskRectangle.height);
        Rectangle aReachabilityRectanle = new Rectangle(aTaskRectangle.getLocation(), new Dimension(reachabilityWidth, reachabilityHeight));

        aReachabilityRectanle.setLocation(aTaskRectangle.x + (int) ((aTaskRectangle.getCenterX() - aReachabilityRectanle.getCenterX())), aTaskRectangle.y + (int) ((aTaskRectangle.getCenterY() - aReachabilityRectanle.getCenterY())));
        return aReachabilityRectanle;
    }

    /**
     * Should be transitive. Returns true if two reachabilities intersect
     * 
     * @param aRectangleObjectInterface
     * @return
     */
    public static boolean areWithinReachability(Rectangle aReachabilityRectangle, Rectangle anotherReachabilityRectangle) {
        String msg = ".areWithinReach() ";
        if (aReachabilityRectangle == null || anotherReachabilityRectangle == null) {
            throw new RuntimeException(msg + "aReachabilityRectangle or  anotherReachabilityRectangle should never be null.");
        }

        boolean areWithinReach = aReachabilityRectangle.intersects(anotherReachabilityRectangle) || aReachabilityRectangle.contains(anotherReachabilityRectangle) || anotherReachabilityRectangle.contains(aReachabilityRectangle);
        logger.trace(msg + " rectangle=" + aReachabilityRectangle + " and rectangle=" + anotherReachabilityRectangle + " areWithinReach=" + areWithinReach);
        return areWithinReach;
    }

    /**
     * Returns true if the human is not placed between the robot and the parts
     * 
     * @param existingAssignements
     * @return
     */
    public static boolean humanPositionNotBetweenRobotAndParts(Vector<AssignmentDataModel> existingAssignements) {
        String msg = ".humanPositionNotBetweenRobotAndParts() ";
        boolean humanPositionNotBetweenRobotAndParts = true;
        ArrayList<Rectangle> RobotPositions = new ArrayList<Rectangle>();
        ArrayList<Rectangle> HumanPositions = new ArrayList<Rectangle>();
        ArrayList<Rectangle> PartsPositions = new ArrayList<Rectangle>();
        for (AssignmentDataModel anAssignement : existingAssignements) {
            // if the task is a robot create a robotpositions rectangle
            if (anAssignement.getTaskDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_ROBOT)) {
                Rectangle RobotRectangle = DataModelToAWTHelper.createRectangle(anAssignement.getResourceDataModel(), anAssignement.getTaskDataModel());
                RobotPositions.add(RobotRectangle);
            }
            // if the task is a human create a humanpositions rectangle
            else
                if (anAssignement.getTaskDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_HUMAN)) {
                    Rectangle HumanRectangle = DataModelToAWTHelper.createRectangle(anAssignement.getResourceDataModel(), anAssignement.getTaskDataModel());
                    HumanPositions.add(HumanRectangle);
                }
                // if the task is a task create a taskpositions rectangle
                else
                    if (anAssignement.getTaskDataModel().getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME).equals(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_TASK)) {
                        Rectangle PartRectangle = DataModelToAWTHelper.createRectangle(anAssignement.getResourceDataModel(), anAssignement.getTaskDataModel());
                        PartsPositions.add(PartRectangle);
                    }
        }

        if (!HumanPositions.isEmpty() && !RobotPositions.isEmpty() && !PartsPositions.isEmpty()) {
            for (Rectangle HumanPosition : HumanPositions) {
                for (Rectangle RobotPosition : RobotPositions) {
                    for (Rectangle PartsPosition : PartsPositions) {
                        // check whether the part is positioned right from the robot in a height same as the robot's
                        if ((PartsPosition.x >= RobotPosition.x + RobotPosition.width) && ((PartsPosition.y >= RobotPosition.y) || (PartsPosition.y + PartsPosition.height <= RobotPosition.y + RobotPosition.height))) {
                            // check whether the human is positioned right from the robot in a height same as the robot's
                            if ((HumanPosition.x >= RobotPosition.x + RobotPosition.width) && ((HumanPosition.y >= RobotPosition.y) || (HumanPosition.y + HumanPosition.height <= RobotPosition.y + RobotPosition.height))) {
                                // whether the human is closer to the robot than the part
                                if (HumanPosition.x + HumanPosition.width <= PartsPosition.x) {
                                    humanPositionNotBetweenRobotAndParts = false;
                                    logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                    return humanPositionNotBetweenRobotAndParts;
                                }
                            }
                            // check whether the part is positioned right from the robot in a height same as the robot's
                        }
                        else
                            if ((PartsPosition.x + PartsPosition.width <= RobotPosition.x) && ((PartsPosition.y >= RobotPosition.y) || (PartsPosition.y + PartsPosition.height <= RobotPosition.y + RobotPosition.height))) {
                                // check whether the human is positioned right from the robot in a height same as the robot's
                                if ((HumanPosition.x + HumanPosition.width <= RobotPosition.x) && ((HumanPosition.y >= RobotPosition.y) || (HumanPosition.y + HumanPosition.height <= RobotPosition.y + RobotPosition.height))) {
                                    // whether the human is closer to the robot than the part
                                    if (HumanPosition.x >= PartsPosition.x + PartsPosition.width) {
                                        humanPositionNotBetweenRobotAndParts = false;
                                        logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                        return humanPositionNotBetweenRobotAndParts;
                                    }
                                }
                                // check whether the part is positioned downwards from the robot in a horizontal position same as the robot's
                            }
                            else
                                if ((PartsPosition.y >= RobotPosition.y + RobotPosition.height) && ((PartsPosition.x >= RobotPosition.x) || (PartsPosition.x + PartsPosition.width <= RobotPosition.x + RobotPosition.width))) {
                                    // check whether the human is positioned downwards from the robot in a horizontal position same as the robot's
                                    if ((HumanPosition.y >= RobotPosition.y + RobotPosition.height) && ((HumanPosition.x >= RobotPosition.x) || (HumanPosition.x + HumanPosition.width <= RobotPosition.x + RobotPosition.width))) {
                                        // whether the human is closer to the robot than the part
                                        if (HumanPosition.y <= PartsPosition.y) {
                                            humanPositionNotBetweenRobotAndParts = false;
                                            logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                            return humanPositionNotBetweenRobotAndParts;
                                        }
                                    }
                                    // check whether the part is positioned upwards from the robot in a horizontal position same as the robot's
                                }
                                else
                                    if ((PartsPosition.y + PartsPosition.height <= RobotPosition.y) && ((PartsPosition.x >= RobotPosition.x) || (PartsPosition.x + PartsPosition.width <= RobotPosition.x + RobotPosition.width))) {
                                        // check whether the human is positioned upwards from the robot in a horizontal position same as the robot's
                                        if ((HumanPosition.y + HumanPosition.height <= RobotPosition.y) && ((HumanPosition.x >= RobotPosition.x) || (HumanPosition.x + HumanPosition.width <= RobotPosition.x + RobotPosition.width))) {
                                            // whether the human is closer to the robot than the part
                                            if (HumanPosition.y >= PartsPosition.y) {
                                                humanPositionNotBetweenRobotAndParts = false;
                                                logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                                return humanPositionNotBetweenRobotAndParts;
                                            }
                                        }
                                        // check whether the part is positioned upwards and right from the robot
                                    }
                                    else
                                        if ((PartsPosition.x >= RobotPosition.x + RobotPosition.width) && (PartsPosition.y + PartsPosition.height <= RobotPosition.y)) {
                                            // check whether the human is positioned upwards and right from the robot
                                            if ((HumanPosition.x >= RobotPosition.x + RobotPosition.width) && (HumanPosition.y + HumanPosition.height <= RobotPosition.y)) {
                                                double PartsDistanceFromRobot = Math.sqrt(Math.pow((PartsPosition.x - RobotPosition.x), 2) + Math.pow((PartsPosition.y - RobotPosition.y), 2));
                                                double HumanDistanceFromRobot = Math.sqrt(Math.pow((HumanPosition.x - RobotPosition.x), 2) + Math.pow((HumanPosition.y - RobotPosition.y), 2));
                                                // whether the human is closer to the robot than the part
                                                if (HumanDistanceFromRobot <= PartsDistanceFromRobot) {
                                                    humanPositionNotBetweenRobotAndParts = false;
                                                    logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                                    return humanPositionNotBetweenRobotAndParts;
                                                }
                                            }
                                            // check whether the part is positioned downwards and right from the robot
                                        }
                                        else
                                            if ((PartsPosition.x >= RobotPosition.x + RobotPosition.width) && (PartsPosition.y >= RobotPosition.y + RobotPosition.height)) {
                                                // check whether the human is positioned downwards and right from the robot
                                                if ((HumanPosition.x >= RobotPosition.x + RobotPosition.width) && (HumanPosition.y >= RobotPosition.y + RobotPosition.height)) {
                                                    double PartsDistanceFromRobot = Math.sqrt(Math.pow((PartsPosition.x - RobotPosition.x), 2) + Math.pow((PartsPosition.y - RobotPosition.y), 2));
                                                    double HumanDistanceFromRobot = Math.sqrt(Math.pow((HumanPosition.x - RobotPosition.x), 2) + Math.pow((HumanPosition.y - RobotPosition.y), 2));
                                                    // whether the human is closer to the robot than the part
                                                    if (HumanDistanceFromRobot <= PartsDistanceFromRobot) {
                                                        humanPositionNotBetweenRobotAndParts = false;
                                                        logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                                        return humanPositionNotBetweenRobotAndParts;
                                                    }
                                                }
                                                // check whether the part is positioned upwards and left from the robot
                                            }
                                            else
                                                if ((PartsPosition.x + PartsPosition.width <= RobotPosition.x) && (PartsPosition.y + PartsPosition.height <= RobotPosition.y)) {
                                                    // check whether the human is positioned upwards and left from the robot
                                                    if ((HumanPosition.x + HumanPosition.width <= RobotPosition.x) && (HumanPosition.y + HumanPosition.height <= RobotPosition.y)) {
                                                        double PartsDistanceFromRobot = Math.sqrt(Math.pow((PartsPosition.x - RobotPosition.x), 2) + Math.pow((PartsPosition.y - RobotPosition.y), 2));
                                                        double HumanDistanceFromRobot = Math.sqrt(Math.pow((HumanPosition.x - RobotPosition.x), 2) + Math.pow((HumanPosition.y - RobotPosition.y), 2));
                                                        // whether the human is closer to the robot than the part
                                                        if (HumanDistanceFromRobot <= PartsDistanceFromRobot) {
                                                            humanPositionNotBetweenRobotAndParts = false;
                                                            logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                                            return humanPositionNotBetweenRobotAndParts;
                                                        }
                                                    }
                                                    // check whether the part is positioned downwards and left from the robot
                                                }
                                                else
                                                    if ((PartsPosition.x + PartsPosition.width <= RobotPosition.x) && (PartsPosition.y >= RobotPosition.y + RobotPosition.height)) {
                                                        // check whether the human is positioned downwards and left from the robot
                                                        if ((HumanPosition.x + HumanPosition.width <= RobotPosition.x) && (HumanPosition.y >= RobotPosition.y + RobotPosition.height)) {
                                                            double PartsDistanceFromRobot = Math.sqrt(Math.pow((PartsPosition.x - RobotPosition.x), 2) + Math.pow((PartsPosition.y - RobotPosition.y), 2));
                                                            double HumanDistanceFromRobot = Math.sqrt(Math.pow((HumanPosition.x - RobotPosition.x), 2) + Math.pow((HumanPosition.y - RobotPosition.y), 2));
                                                            // whether the human is closer to the robot than the part
                                                            if (HumanDistanceFromRobot <= PartsDistanceFromRobot) {
                                                                humanPositionNotBetweenRobotAndParts = false;
                                                                logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
                                                                return humanPositionNotBetweenRobotAndParts;
                                                            }
                                                        }
                                                    }
                    }
                }
            }
        }
        // boolean humanPositionNotBetweenRobotAndParts = aReachabilityRectangle.intersects(anotherReachabilityRectangle) || aReachabilityRectangle.contains(anotherReachabilityRectangle) || anotherReachabilityRectangle.contains(aReachabilityRectangle);
        logger.trace(msg + " humanPositionNotBetweenRobotAndParts=" + humanPositionNotBetweenRobotAndParts);
        return humanPositionNotBetweenRobotAndParts;
    }

    /**
     * Tries to get a Point where the task can be placed, given the
     * existingAssignements.
     * Tries using three different algorithms. The first solution found is
     * returned.
     * 
     * @param task
     * @param existingAssignements
     * @return
     */
    public static Point getAValidPointForResource(TaskDataModel task, Vector<Assignment> existingAssignements, int maxTries) {
        // try heuristic then try random
        String msg = ".getAValidPointForResource: ";
        Point aPoint = LayoutEvaluator.getAValidPointForResourceHeuristic(task, existingAssignements);
        if (aPoint == null) {
            aPoint = LayoutEvaluator.getAValidPointForResourceExtensive(task, existingAssignements, maxTries);
        }
        if (aPoint == null) {
            aPoint = LayoutEvaluator.getAValidPointForResourceRandom(task, existingAssignements, maxTries);
        }
        logger.trace(msg + " aPoint=" + aPoint);
        return aPoint;
    }

    /**
     * Randomly selects and checks maxTries squares in the space of the map.
     * Returns a point if it is valid, null if no point is found.
     * 
     * @param task
     * @param existingAssignements
     * @param maxTries
     * @return
     */
    private static Point getAValidPointForResourceRandom(TaskDataModel task, Vector<Assignment> existingAssignements, int maxTries) {
        Point returnPoint = new Point(0, 0);

        Dimension taskDimensions = DataModelToAWTHelper.getDimensionFromTask(task);
        int taskReachability = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME));

        for (int i = 0; i < maxTries; i++) {

            Random aRandom = new Random();
            returnPoint.setLocation(aRandom.nextInt(MapParameters.MAP_WIDTH + 1), aRandom.nextInt(MapParameters.MAP_HEIGHT + 1));

            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                return returnPoint;
            }

        }

        return null;
    }

    /**
     * Equally distributes maxTries squares in the space of the map.
     * Returns a point if it is valid, null if no point is found.
     * 
     * @param task
     * @param existingAssignements
     * @param maxTries
     * @return
     */
    private static Point getAValidPointForResourceExtensive(TaskDataModel task, Vector<Assignment> existingAssignements, int maxTries) {
        Point returnPoint = new Point(0, 0);
        int stepX = MapParameters.MAP_WIDTH / (int) Math.sqrt((int) maxTries);
        int stepY = MapParameters.MAP_HEIGHT / (int) Math.sqrt((int) maxTries);
        Dimension taskDimensions = DataModelToAWTHelper.getDimensionFromTask(task);
        int taskReachability = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME));
        for (int x = 0; stepX < MapParameters.MAP_WIDTH; x += stepX) {
            for (int y = 0; stepY < MapParameters.MAP_HEIGHT; y += stepY) {
                returnPoint.setLocation(x, y);
                if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
                {
                    return returnPoint;
                }
            }
        }

        return null;
    }

    /**
     * Uses a heyristic to check the available positions of a Task.
     * If the heuristic succeeds it returns a Point.
     * Will search 8 locations per existing assignment.
     * 
     * @param task
     * @param existingAssignements
     * @return
     */
    private static Point getAValidPointForResourceHeuristic(TaskDataModel task, Vector<Assignment> existingAssignements) {
        String msg = ".getAValidPointForResourceHeuristic: ";
        Point returnPoint = null;
        Dimension taskDimensions = DataModelToAWTHelper.getDimensionFromTask(task);
        int taskReachability = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.REACHABILTY_PROPERTY_NAME));
        if (existingAssignements == null || existingAssignements.size() == 0) {
            returnPoint = new Point();
            logger.trace(msg + " no existing assignement return:" + returnPoint);
            return returnPoint;
        }
        String reason = "null";
        for (Assignment anAssignment : existingAssignements) {
            Rectangle assignementRectangle = DataModelToAWTHelper.createRectangle(anAssignment.getResource(), anAssignment.getTask());

            reason = "up left";
            returnPoint = new Point(assignementRectangle.x, assignementRectangle.y - taskDimensions.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "right up";
            returnPoint = new Point(assignementRectangle.x + assignementRectangle.width, assignementRectangle.y);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "down right";
            returnPoint = new Point(assignementRectangle.x + assignementRectangle.width - taskDimensions.width, assignementRectangle.y + assignementRectangle.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "left down";
            returnPoint = new Point(assignementRectangle.x - taskDimensions.width, assignementRectangle.y + assignementRectangle.height - taskDimensions.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }

            reason = "down left";
            returnPoint = new Point(assignementRectangle.x, assignementRectangle.y + assignementRectangle.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "left up";
            returnPoint = new Point(assignementRectangle.x - taskDimensions.width, assignementRectangle.y);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "up right";
            returnPoint = new Point(assignementRectangle.x + assignementRectangle.width - taskDimensions.width, assignementRectangle.y - taskDimensions.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "right down";
            returnPoint = new Point(assignementRectangle.x + assignementRectangle.width, assignementRectangle.y + assignementRectangle.height - taskDimensions.height);
            if (LayoutEvaluator.isSuitabilityValid(returnPoint, taskDimensions, existingAssignements, taskReachability))
            {
                break;
            }
            reason = "null";
            returnPoint = null;
        }
        logger.trace(msg + "reason:" + reason + " return:" + returnPoint);
        return returnPoint;
    }
}
