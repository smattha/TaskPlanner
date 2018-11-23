package planning.scheduler.simulation;

import java.util.Calendar;

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

public class TaskSimulator {
    public static final String PENDING = "PENDING";
    public static final String ONGOING = "ONGOING";
    public static final String FINISHED = "FINISHED";

    // The task
    private TaskDataModel task = null;

    // The state of the task. It will continues change as simulation goes on
    // private String taskState = null;

    public TaskSimulator(TaskDataModel task) {
        this.task = task;
        // this.taskState = TaskSimulator.PENDING;
    }

    public Calendar getArrivalDate() {
        return this.task.getTaskArrivalDate();
    }

    public Calendar getDueDate() {
        return this.task.getTaskDueDate();
    }

    public TaskDataModel getTaskDataModel() {
        return this.task;
    }

    public String getTaskId() {
        return this.task.getTaskId();
    }
}
