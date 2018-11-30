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

public class PoolOfAssignmentsSimulator {
    private Vector<AssignmentDataModel> assignments = null;

    public PoolOfAssignmentsSimulator(Vector<AssignmentDataModel> assignmentDataModelVector) {
        if (assignmentDataModelVector != null) {
            this.assignments = assignmentDataModelVector;
        } else {
            this.assignments = new Vector<AssignmentDataModel>();
        }
    }

    // Adding an assignment decided by the algorithm
    public void addAssignment(AssignmentDataModel newAssignment) {
        this.assignments.add(newAssignment);
    }

    // Creating interrupt for the most recent finished assignment.
    // Meaning one resource is IDLE again
    public Interupt getNextInterupt(Calendar timeNow) {
        Calendar interuptDate = null;

        Vector<AssignmentDataModel> sources = new Vector<AssignmentDataModel>();

        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            // Check if the assignment has already finish
            if ((assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds()) > timeNow.getTimeInMillis()) {
                if (interuptDate == null) {
                    interuptDate = Calendar.getInstance();
                    interuptDate.setTimeInMillis(assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds());
                    sources.add(assignment);
                } else {
                    if (interuptDate.getTimeInMillis() > (assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds())) {
                        interuptDate = Calendar.getInstance();
                        interuptDate.setTimeInMillis(assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds());
                        sources.removeAllElements();
                        sources.add(assignment);
                    } else if (interuptDate.getTimeInMillis() == (assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds())) {
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
            return new Interupt(interuptDate, sources, Interupt.ASSIGNMENTS_FINISHED);
        }
    }

    public AssignmentDataModel getAssignmentForTask(TaskDataModel taskDataModel) {
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            if (assignment.getTaskDataModel().equals(taskDataModel)) {
                return assignment;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Vector<AssignmentDataModel> getAssignmentDataModelVector() {
        return (Vector<AssignmentDataModel>) this.assignments.clone();
    }

    public void removeAssignment(AssignmentDataModel assignment) {
        this.assignments.remove(assignment);
    }

    public AssignmentDataModel getLastAssignmentOfResource(ResourceDataModel resourceDataModel) {
        AssignmentDataModel lastAssignment = null;
        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = assignments.get(i);
            if (assignment.getResourceDataModel().equals(resourceDataModel)) {
                if (lastAssignment == null) {
                    lastAssignment = assignment;
                } else if (lastAssignment.getTimeOfDispatch().getTimeInMillis() <= assignment.getTimeOfDispatch().getTimeInMillis()) {
                    lastAssignment = assignment;
                }
            }
        }
        return lastAssignment;
    }
}