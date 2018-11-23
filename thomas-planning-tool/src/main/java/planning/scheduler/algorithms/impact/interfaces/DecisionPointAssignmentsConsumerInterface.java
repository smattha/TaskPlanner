package planning.scheduler.algorithms.impact.interfaces;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public interface DecisionPointAssignmentsConsumerInterface {

    public Vector<AssignmentDataModel> consumeDecisionPointAssignments(Calendar timeNow, PlanHelperInterface helper, Vector<Assignment> decisionPointAssignments, double[] criteriaValues);

}
