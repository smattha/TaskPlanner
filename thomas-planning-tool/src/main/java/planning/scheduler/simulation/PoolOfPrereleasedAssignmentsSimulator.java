package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.model.ResourceDataModel;
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

public class PoolOfPrereleasedAssignmentsSimulator {
    private Vector<AssignmentDataModel> assignments = null;

    public PoolOfPrereleasedAssignmentsSimulator(Vector<AssignmentDataModel> assignmentDataModelVector) {
        if (assignmentDataModelVector != null) {
            this.assignments = assignmentDataModelVector;
        } else {
            this.assignments = new Vector<AssignmentDataModel>();
        }
    }

    // Creating interrupt for the most recent locked assignment.
    // Meaning one task MUST be assigned
    public Interupt getNextInterupt(Calendar timeNow) {
        Calendar interuptDate = null;

        Vector<AssignmentDataModel> sources = new Vector<AssignmentDataModel>();

        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            // Check if the assignment has already finish
            if (assignment.getTimeOfDispatch().getTimeInMillis() > timeNow.getTimeInMillis()) {
                if (interuptDate == null) {
                    interuptDate = Calendar.getInstance();
                    interuptDate.setTimeInMillis(assignment.getTimeOfDispatch().getTimeInMillis());
                    sources.add(assignment);
                } else {
                    if (interuptDate.getTimeInMillis() > assignment.getTimeOfDispatch().getTimeInMillis()) {
                        interuptDate = Calendar.getInstance();
                        interuptDate.setTimeInMillis(assignment.getTimeOfDispatch().getTimeInMillis());
                        sources.removeAllElements();
                        sources.add(assignment);
                    } else if (interuptDate.getTimeInMillis() == assignment.getTimeOfDispatch().getTimeInMillis()) {
                        sources.add(assignment);
                    }
                }
            }
        }

        if (interuptDate == null) {
            return null;
        }
        // else
        {
            return new Interupt(interuptDate, sources, Interupt.LOCKED_CONSTRAINTS);
        }
    }

    public AssignmentDataModel getPrereleaseAssignmentForTask(TaskDataModel taskDataModel) {
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            if (assignment.getTaskDataModel().equals(taskDataModel)) {
                return assignment;
            }
        }
        return null;
    }

    public AssignmentDataModel getNextPrereleaseAssignment(ResourceDataModel resource, Calendar currentTime) {
        AssignmentDataModel nextPrereleaseAssignment = null;
        for (int i = 0; i < this.assignments.size(); i++) {
            AssignmentDataModel currentPrereleaseAssignment = this.assignments.get(i);
            if (currentPrereleaseAssignment.getResourceDataModel().equals(resource))// Check for the resource assignments
            {
                if (currentTime.getTimeInMillis() <= currentPrereleaseAssignment.getTimeOfDispatch().getTimeInMillis())// Check for the time of the pre-release assignment
                {
                    if (nextPrereleaseAssignment == null) {
                        nextPrereleaseAssignment = currentPrereleaseAssignment;
                    } else if (nextPrereleaseAssignment.getTimeOfDispatch().getTimeInMillis() > currentPrereleaseAssignment.getTimeOfDispatch().getTimeInMillis()) {
                        nextPrereleaseAssignment = currentPrereleaseAssignment;
                    }
                }
            }
        }

        return nextPrereleaseAssignment;
    }

    @SuppressWarnings("unchecked")
    public Vector<AssignmentDataModel> getPrereleaseAssignmentDataModelVector() {
        return (Vector<AssignmentDataModel>) this.assignments.clone();
    }
}
