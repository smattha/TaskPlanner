package planning.scheduler.simulation;

import java.util.Vector;

import planning.scheduler.simulation.interfaces.PlanHelperInterface;


/**
 * A interface that a class should implement in order to trigger the end of a schedule.
 * Predefined such classes are the schedule end time in this package.
 */
public interface PlanEndRule
{
    public abstract boolean continuePlan(Vector<Interupt> interupt, PlanHelperInterface planHelperInterface);
    public abstract String getHumanReadableDescription();
}
