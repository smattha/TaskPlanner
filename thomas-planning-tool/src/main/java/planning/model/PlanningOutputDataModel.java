package planning.model;

import java.util.Calendar;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import planning.ViewToPlanningOutputDataModelInterface;

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

public class PlanningOutputDataModel extends AbstractDataModel implements ViewToPlanningOutputDataModelInterface {
    private Vector<AssignmentDataModel> assignments = null;
    private String planningOutputDataModelId = null;
    private PlanningInputDataModel planningInputDataModel = null;
    private double meanFlowTime;
    private double meanTardiness;

    public PlanningOutputDataModel() {
        assignments = new Vector<AssignmentDataModel>();
        this.planningOutputDataModelId = "UNSPECIFIED";
    }

    protected void setPlanningInputDataModel(PlanningInputDataModel planningInputDataModel) {
        this.planningInputDataModel = planningInputDataModel;
    }

    public void setAssignments(Vector<AssignmentDataModel> assignments) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.assignments = assignments;
        stateChanged();
    }

    public void setPlanningOutputDataModelId(String planningOutputDataModelId) {
        this.planningOutputDataModelId = planningOutputDataModelId;
    }

    public String getPlanningOutputDataModelId() {
        return planningOutputDataModelId;
    }

    @SuppressWarnings("unchecked")
    public Vector<AssignmentDataModel> getAssignments() {
        return (Vector<AssignmentDataModel>) this.assignments.clone();
    }

    public Vector<AssignmentDataModel> getAssignmentsForTask(String taskid) {
        Vector<AssignmentDataModel> taskAssignments = new Vector<AssignmentDataModel>();
        Vector<AssignmentDataModel> allAssignments = this.getAssignments();
        for (int i = 0; i < allAssignments.size(); i++) {
            if (allAssignments.get(i).getTaskDataModel().getTaskId().equals(taskid)) {
                taskAssignments.add(allAssignments.get(i));
            }
        }
        return taskAssignments;
    }

    public AssignmentDataModel getAssignmentForTask(TaskDataModel taskDataModel) {
        Vector<AssignmentDataModel> allAssignments = this.getAssignments();
        for (int i = 0; i < allAssignments.size(); i++) {
            if (((AssignmentDataModel) allAssignments.get(i)).getTaskDataModel().equals(taskDataModel)) {
                return (AssignmentDataModel) allAssignments.get(i);
            }
        }
        return null;
    }

    public Vector<AssignmentDataModel> getAssignmentsForResource(String resourceid) {
        Vector<AssignmentDataModel> resourceAssignments = new Vector<AssignmentDataModel>();
        Vector<AssignmentDataModel> allAssignments = this.getAssignments();
        for (int i = 0; i < allAssignments.size(); i++) {
            if (allAssignments.get(i).getResourceDataModel().getResourceId().equals(resourceid)) {
                resourceAssignments.add(allAssignments.get(i));
            }
        }
        return resourceAssignments;
    }

    public Node toXMLNode(Document document) {
        Element root = document.getDocumentElement();
        root.setAttribute("id", this.planningOutputDataModelId);
        if (this.planningInputDataModel != null) {
            root.setAttribute("planningInputId", this.planningInputDataModel.getPlanningInputDataModelId());
        } else {
            root.setAttribute("planningInputId", "UNSPECIFIED");
        }

        Node planningOutputNode = document.createDocumentFragment();

        // Making the assignments
        Element assignmentsElement = document.createElement("ASSIGNMENTS");
        planningOutputNode.appendChild(assignmentsElement);

        for (int i = 0; i < assignments.size(); i++) {
            AssignmentDataModel assignment = (AssignmentDataModel) assignments.get(i);
            assignmentsElement.appendChild(assignment.toXMLNode(document));
        }

        return planningOutputNode;
    }

    /********************************************************************************/
    /*************************** SETTERS PUBLIC FUNCTIONS ***************************/
    /********************************************************************************/

    public void setTaskDataModelToAssignmentDataModel(TaskDataModel task, AssignmentDataModel assignment) {
        assignment.setTaskDataModel(task);
        stateChanged();
    }

    public void setResourceDataModelToAssignmentDataModel(ResourceDataModel resource, AssignmentDataModel assignment) {
        assignment.setResourceDataModel(resource);
        stateChanged();
    }

    public void setDurationInMillisecondsToAssignmentDataModel(long newDurationInMilliseconds, AssignmentDataModel assignment) {
        assignment.setDurationInMilliseconds(newDurationInMilliseconds);
        stateChanged();
    }

    public void setTimeOfDispatchToAssignmentDataModel(Calendar newTimeOfDispatch, AssignmentDataModel assignment) {
        assignment.setTimeOfDispatch(newTimeOfDispatch);
        stateChanged();
    }

    public void addAssignmentDataModel(AssignmentDataModel newAssignment) {
        this.assignments.add(newAssignment);
        stateChanged();
    }

    public void removeAssignmentDataModel(AssignmentDataModel assignment) {
        this.assignments.remove(assignment);
        stateChanged();
    }

    public void lock(AssignmentDataModel assignmentDataModel) {
        assignmentDataModel.lock();
    }

    public void unlock(AssignmentDataModel assignmentDataModel) {
        assignmentDataModel.unlock();
    }

    public double getMeanFlowTimeInMilliseconds() {
        return meanFlowTime;
    }

    public double getMeanTardinessInMilliseconds() {
        return meanTardiness;
    }

    private void stateChanged() {
        recalculatePerformanceIndicators();
        notifyObservers();
    }

    // TODO : Check if and when recalculation should be performed upon planningInputDataModel changes
    private void recalculatePerformanceIndicators() {
        if (assignments != null && assignments.size() > 0) {
            int assignmentsCount = assignments.size();
            long assignmentsTardinessSumInMilliseconds = 0;
            long assignmentsFlowTimeSumInMilliseconds = 0;
            for (int i = 0; i < assignmentsCount; i++) {
                AssignmentDataModel assignment = (AssignmentDataModel) assignments.get(i);
                TaskDataModel task = assignment.getTaskDataModel();
                long assignmentCompletion = assignment.getTimeOfDispatch().getTimeInMillis() + assignment.getDurationInMilliseconds();
                // Calculate the assignment flow time and add it to the sum
                long assignmentFlowTimeInMilliseconds = assignmentCompletion - task.getTaskArrivalDate().getTimeInMillis();
                assignmentsFlowTimeSumInMilliseconds += assignmentFlowTimeInMilliseconds;
                // Calculate the assignment tardiness and add it to the sum only
                // if it is positive
                long assignmentTardinesInMilliseconds = assignmentCompletion - task.getTaskDueDate().getTimeInMillis();
                if (assignmentTardinesInMilliseconds > 0) {
                    assignmentsTardinessSumInMilliseconds += assignmentTardinesInMilliseconds;
                }
            }
            meanTardiness = assignmentsTardinessSumInMilliseconds / assignmentsCount;
            meanFlowTime = assignmentsFlowTimeSumInMilliseconds / assignmentsCount;
        } else {
            meanTardiness = 0;
            meanFlowTime = 0;
        }
    }
}
