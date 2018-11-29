package planning.scheduler.simulation.interfaces;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;

public interface ManualPlanHelperInterface extends PlanHelperInterface {
	public void addManualAssignment(AssignmentDataModel assignment);

	public void addManualAssignments(Vector<AssignmentDataModel> assignments);

	public void setManualTimeNow(Calendar timeNow);

	// public void removeAssignment(AssignmentDataModel assignment);
	// public void removeAssignments(AssignmentDataModelVector assignments);

}
