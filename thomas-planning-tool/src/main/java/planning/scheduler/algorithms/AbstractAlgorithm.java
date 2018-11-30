package planning.scheduler.algorithms;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public abstract class AbstractAlgorithm {
//  public static final String EDD = "Earliest Due Date";
//  public static final String FIFO = "First In First Out";
//  public static final String SPT = "Shortest Proccessing Time";
	 public static final String EDD = "EDD";
	 public static final String FIFO = "FIFO";
	public static final String SPT = "SPT";
	public static final String MULTICRITERIA = "MULTICRITERIA";
    public abstract Vector<AssignmentDataModel> solve(Vector<TaskSimulator> pendingTaskSimulators, Vector<ResourceSimulator> idleResourceSimulators, Calendar timeNow, PlanHelperInterface helper);
}
