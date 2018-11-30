package planning.scheduler.algorithms;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

// Short processing time considers as i) task on resource proccessing time (the opration time provided by the user) PLUS ii) the setup time of the resource for the task
public class SPT extends AbstractAlgorithm {
    protected SPT() {
    }

    public Vector<AssignmentDataModel> solve(Vector<TaskSimulator> pendingTaskSimulators, Vector<ResourceSimulator> idleResourceSimulators, Calendar timeNow, PlanHelperInterface helper) {
        // Steps :
        // 1) For each pending task do :
        //    1.1) calculate the potential resources
        //    1.2) if potential resources exist for these resource calculate the processingTime = opperationalTime + setUpTime
        //    1.3) select the smallest processingTime for a specified resource
        // 2) Make an assignment for the task with the smallest processingTime
        // 3) Back to 1)

        // TODO : If i had a collection (vector) with all the task suitable resources it would be quicker
        // Check times and decide ...

        // While there are pending tasks with potential resources

        Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();

        while (!pendingTaskSimulators.isEmpty()) {
            TaskSimulator potentialAssignmentTaskSimulator = null;
            ResourceSimulator potentialAssignmentResourceSimulator = null;
            long potentialAssignmentProcessingTime = 0;
            for (int i = 0; i < pendingTaskSimulators.size(); i++) {
                TaskSimulator currentTaskSimulator = (TaskSimulator) pendingTaskSimulators.get(i);
                // Check if the tasks has pending obligations ...
                Vector<ResourceSimulator> suitableResources = helper.getSuitableResourcesSimulatorForTask(currentTaskSimulator);
                ResourceSimulator potentialSuitableResourceSimulator = null;
                long potentialSuitableResourceSimulatorProcessingTime = 0;
                // Loop to get rid of the unwanted resources.
                // If a resource is found then calculate it's processing time and store the minimum values
                for (int j = 0; j < suitableResources.size(); j++) {
                    ResourceSimulator resourceSimulator = suitableResources.get(j);
                    if (!helper.areAnyPendingPresendenceConstraintsForTaskOnResource(resourceSimulator, currentTaskSimulator, timeNow)) {
                        // Checks if the suitable resource is idle, is currently available and if it's lock constrains are fulfilled
                        if ((resourceSimulator.getResourceState().equals(ResourceSimulator.IDLE)) && (idleResourceSimulators.indexOf(resourceSimulator) != -1) && (helper.canResourceFullfilTheTaskUniterrupted(resourceSimulator, currentTaskSimulator, timeNow))) {
                            // Checks if this suitable resource has the smallest processing time and replace if applicable the
                            // potential suitable resource
                            if (potentialSuitableResourceSimulator == null) {
                                potentialSuitableResourceSimulator = resourceSimulator;
                                potentialSuitableResourceSimulatorProcessingTime = helper.getSetUpTimeInMillisecondsForTaskOnResource(currentTaskSimulator, potentialSuitableResourceSimulator, timeNow, null)
                                        + helper.getOperationTimeInMillisecondsForTaskOnResource(currentTaskSimulator, potentialSuitableResourceSimulator, timeNow, null);
                            } else {// Perform a check 
                                long currentSuitableResourceSimulatorProcessingTime = helper.getSetUpTimeInMillisecondsForTaskOnResource(currentTaskSimulator, potentialSuitableResourceSimulator, timeNow, null)
                                        + helper.getOperationTimeInMillisecondsForTaskOnResource(currentTaskSimulator, potentialSuitableResourceSimulator, timeNow, null);
                                // TODO: what if the times are the same ? One thought is to check the resources ... or
                                //    to choose randomly, and check of the overall value of the plan from the performance indicators.
                                if (potentialSuitableResourceSimulatorProcessingTime > currentSuitableResourceSimulatorProcessingTime) {
                                    potentialSuitableResourceSimulator = resourceSimulator;
                                    potentialSuitableResourceSimulatorProcessingTime = currentSuitableResourceSimulatorProcessingTime;
                                }
                            }
                        }
                    }
                }
                // Check if this task has a potential resource.
                if (potentialSuitableResourceSimulator != null) {
                    // If it has a potential resource check if it has the smallest proccessing time between al the tasks and replace if aplicable the
                    // potential assignment task
                    if (potentialAssignmentTaskSimulator == null) {
                        potentialAssignmentTaskSimulator = currentTaskSimulator;
                        potentialAssignmentResourceSimulator = potentialSuitableResourceSimulator;
                        potentialAssignmentProcessingTime = potentialSuitableResourceSimulatorProcessingTime;
                    } else // Check
                    {
                        // TODO: what if the times are the same ? One thought is to check the two tasks times to other resources
                        // and select the one that is most profitable. i.e. the one that has the bigger difference between the task processing time
                        // to this resource with the smallest processing time to other resources. I thought the bigger because if we choose this then the loss will
                        // be minimised. Another thought is to choose randomly, and check of the overall value of the plan from the performance indicators.
                        if (potentialAssignmentProcessingTime > potentialSuitableResourceSimulatorProcessingTime) {
                            potentialAssignmentTaskSimulator = currentTaskSimulator;
                            potentialAssignmentResourceSimulator = potentialSuitableResourceSimulator;
                            potentialAssignmentProcessingTime = potentialSuitableResourceSimulatorProcessingTime;
                        }
                    }
                }
            }

            if (potentialAssignmentTaskSimulator != null) {
                // Make the assignment
                // Creating the assignment
                AssignmentDataModel assignment = new AssignmentDataModel(potentialAssignmentTaskSimulator.getTaskDataModel(), potentialAssignmentResourceSimulator.getResourceDataModel(), timeNow,
                        potentialAssignmentProcessingTime // operation time + set up time
                                + helper.getResourceDownTimeInMillisecondsForTaskOnResource(potentialAssignmentTaskSimulator, potentialAssignmentResourceSimulator, timeNow, null), false, null);
                // Adding it to the assignment vector for return it
                assignments.add(assignment);
                // Remove the resource from the available resources vector (not available any more an assignment has been made)
                idleResourceSimulators.remove(potentialAssignmentResourceSimulator);
                // Remove the task from the pending tasks vector (not pending any more an assignment has been made)
                pendingTaskSimulators.remove(potentialAssignmentTaskSimulator);
            } else {
                break;
            }
        }

        return assignments;
    }

}
