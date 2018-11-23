package planning.scheduler.algorithms.impact.interfaces;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * Interface to be used for dynamically drop infeasible assignments.
 * USAGE:
 * // Make the vector with all the authorities
 * Vector<DynamicAssignmentsAuthorityInterface> dynamicAssignmentsAuthorities = new Vector<DynamicAssignmentsAuthorityInterface>();
 * // Add the implementors
 * dynamicAssignmentsAuthorities.add(new DynamicAssignmentsAuthority());
 * // Notify IMPACT
 * IMPACT.setDynamicAssignmentsAuthorities(dynamicAssignmentsAuthorities);
 */
public interface DynamicAssignmentsAuthorityInterface {
    public abstract boolean areAssignmentsValid(Vector<AssignmentDataModel> currentAssignemnts, Calendar timeNow, PlanHelperInterface planHelperInterface);
}
