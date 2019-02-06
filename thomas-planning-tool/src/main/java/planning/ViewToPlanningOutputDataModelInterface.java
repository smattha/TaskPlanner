package planning;

import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.model.SubjectDataModelInterface;

// This interfaced is implemented by the data models and is used by the the views
public interface ViewToPlanningOutputDataModelInterface extends SubjectDataModelInterface {
	public Vector<AssignmentDataModel> getAssignments();

	public Vector<AssignmentDataModel> getAssignmentsForTask(String taskid);

	public Vector<AssignmentDataModel> getAssignmentsForResource(String resourceid);

	public double getMeanTardinessInMilliseconds();

	public double getMeanFlowTimeInMilliseconds();
}
