package planning.scheduler.simulation;

import java.util.Calendar;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Interupt
{
    public static final String RESOURCES_IDLE_FROM_RESOURCE_DOWN = "RESOURCES_IDLE_FROM_RESOURCE_DOWN";
    public static final String ASSIGNMENTS_FINISHED = "ASSIGNMENTS_FINISHED";
    public static final String TASKS_ARRIVED = "TASKS_ARRIVED";
    public static final String PLAN_START = "PLAN_START";
    public static final String LOCKED_CONSTRAINTS = "LOCKED_CONSTRAINTS";

    private Calendar dateOfTheInterupt = null;
    private Object source = null;
    private String message = null;

    public Interupt(Calendar date, Object source, String message)
    {
        this.dateOfTheInterupt = date;
        this.source = source;
        this.message = message;
    }

    public Calendar getDate()
    {
        return (Calendar)this.dateOfTheInterupt.clone();
    }

    public Object getSource()
    {
        return this.source;
    }

    public String getMessage()
    {
        return this.message;
    }
}