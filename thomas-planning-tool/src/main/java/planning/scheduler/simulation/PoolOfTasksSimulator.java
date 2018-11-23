package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.model.JobDataModel;
import planning.model.TaskDataModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class PoolOfTasksSimulator {
    private Vector<TaskSimulator> pendingTaskSimulatorVector = new Vector<TaskSimulator>();
    private Vector<TaskSimulator> ongoingTaskSimulatorVector = new Vector<TaskSimulator>();
    private Vector<TaskSimulator> finishedTaskSimulatorVector = new Vector<TaskSimulator>();
    private Vector<JobDataModel> jobDataModelVector = null;

    public PoolOfTasksSimulator(Vector<JobDataModel> jobDataModelVector) {
        this.jobDataModelVector = jobDataModelVector;
        for (int i = 0; i < this.jobDataModelVector.size(); i++) {
            JobDataModel job = (JobDataModel) jobDataModelVector.get(i);
            Vector<TaskDataModel> tasks = job.getTasks();
            for (int j = 0; j < tasks.size(); j++) {
                TaskDataModel taskDataModel = tasks.get(j);
                pendingTaskSimulatorVector.add(new TaskSimulator(taskDataModel));
            }
        }
    }

    protected void addTask(TaskDataModel newTask) {
        // For future development when tasks can be added real time ...
        throw new UnsupportedOperationException("Function not implented yet");
    }

    // Creates interrupt for the pending task with the smallest arrival time.
    // TODO: Check if the task has preconditions??
    // TODO: What about locked tasks???
    protected Interupt getNextInterupt(Calendar timeNow) {
        Calendar interuptDate = null;
        Vector<TaskSimulator> sources = new Vector<TaskSimulator>();
        for (int i = 0; i < pendingTaskSimulatorVector.size(); i++) {
            TaskSimulator task = pendingTaskSimulatorVector.get(i);
            Calendar taskArrivalDate = task.getArrivalDate();
            if (taskArrivalDate.getTimeInMillis() > timeNow.getTimeInMillis()) {
                if (interuptDate == null) {
                    interuptDate = taskArrivalDate;
                    sources.add(task);
                } else {
                    if (taskArrivalDate.getTimeInMillis() < interuptDate.getTimeInMillis()) {
                        interuptDate = taskArrivalDate;
                        sources.removeAllElements();
                        sources.add(task);
                    } else if (taskArrivalDate.getTimeInMillis() == interuptDate.getTimeInMillis()) {
                        interuptDate = taskArrivalDate;
                        sources.add(task);
                    }
                }
            }
        }

        if (interuptDate == null) {
            return null;
        }

        return new Interupt(interuptDate, sources, Interupt.TASKS_ARRIVED);
    }

    @SuppressWarnings("unchecked")
    protected Vector<TaskSimulator> getPendingTaskSimulatorVector() {
        return (Vector<TaskSimulator>) this.pendingTaskSimulatorVector.clone();
    }

    protected Vector<TaskSimulator> getPendingTaskSimulatorVector(Calendar timeNow) {
        Vector<TaskSimulator> pendingAssignmentsUpToGivenTime = new Vector<TaskSimulator>();
        for (int i = 0; i < this.pendingTaskSimulatorVector.size(); i++) {
            TaskSimulator taskSimulator = this.pendingTaskSimulatorVector.get(i);
            if (taskSimulator.getArrivalDate().getTimeInMillis() <= timeNow.getTimeInMillis()) {
                pendingAssignmentsUpToGivenTime.add(taskSimulator);
            }
        }
        return pendingAssignmentsUpToGivenTime;
    }

    protected void notifyForNewAssignment(AssignmentDataModel assignment) {
        TaskSimulator taskSimulator = getTaskSimulator(assignment.getTaskDataModel());
        pendingTaskSimulatorVector.remove(taskSimulator);
        ongoingTaskSimulatorVector.add(taskSimulator);
    }

    protected TaskSimulator getTaskSimulator(TaskDataModel task) {
        for (int i = 0; i < pendingTaskSimulatorVector.size(); i++) {
            TaskSimulator taskSimulator = pendingTaskSimulatorVector.get(i);
            if (taskSimulator.getTaskDataModel().equals(task)) {
                return taskSimulator;
            }
        }
        for (int i = 0; i < ongoingTaskSimulatorVector.size(); i++) {
            TaskSimulator taskSimulator = ongoingTaskSimulatorVector.get(i);
            if (taskSimulator.getTaskDataModel().equals(task)) {
                return taskSimulator;
            }
        }
        for (int i = 0; i < finishedTaskSimulatorVector.size(); i++) {
            TaskSimulator taskSimulator = finishedTaskSimulatorVector.get(i);
            if (taskSimulator.getTaskDataModel().equals(task)) {
                return taskSimulator;
            }
        }
        return null;
    }

    protected void notifyForAssignmentFinished(AssignmentDataModel assignmentDataModel) {
        TaskSimulator taskSimulator = getTaskSimulator(assignmentDataModel.getTaskDataModel());
        ongoingTaskSimulatorVector.remove(taskSimulator);
        finishedTaskSimulatorVector.add(taskSimulator);
    }
}
