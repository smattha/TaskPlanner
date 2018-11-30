package planning.scheduler.simulation.interfaces;

import java.util.Calendar;
import java.util.Vector;

import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

public interface OperationTimeCalculatorInterface {
    public long getOperationTimeInMillisecondsForTaskOnResource(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator, Calendar timeNow, PlanHelperInterface helperInterface, Vector<Assignment> assignments);
}
