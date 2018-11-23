package planning.model;

import java.util.Calendar;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import planning.model.io.XMLUtil;

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

public class JobDataModel extends AbstractDataModel {
    private String jobId = null;
    private String name = null;
    private String description = null;
    private Vector<TaskDataModel> taskDataModelVector = null;
    private Calendar arrivalDate = null;
    private Calendar dueDate = null;
    private WorkcenterDataModel workcenter = null;

    public WorkcenterDataModel getWorkcenterDataModel() {
        return workcenter;
    }

    public JobDataModel(String jobId, String name, String description, Calendar arrivalDate, Calendar dueDate, Vector<TaskDataModel> tasks, WorkcenterDataModel workcenter) {
        this.jobId = jobId;
        this.description = description;
        this.taskDataModelVector = tasks;
        this.arrivalDate = arrivalDate;
        this.dueDate = dueDate;
        this.name = name;
        this.workcenter = workcenter;
    }

    public Node toXMLNode(Document document) {
        Element jobElement = document.createElement("JOB");
        jobElement.setAttribute("id", this.jobId);

        Element nameElement = document.createElement("NAME");
        nameElement.appendChild(document.createTextNode(this.name));
        jobElement.appendChild(nameElement);

        Element descriptionElement = document.createElement("DESCRIPTION");
        descriptionElement.appendChild(document.createTextNode(this.description));
        jobElement.appendChild(descriptionElement);

        Element arrivalDateElement = document.createElement("ARRIVAL_DATE");
        arrivalDateElement.appendChild(XMLUtil.getNodeFromCalendar(this.arrivalDate, document));
        jobElement.appendChild(arrivalDateElement);

        Element dueDateElement = document.createElement("DUE_DATE");
        dueDateElement.appendChild(XMLUtil.getNodeFromCalendar(this.dueDate, document));
        jobElement.appendChild(dueDateElement);

        for (int i = 0; i < this.taskDataModelVector.size(); i++) {
            Element taskReferenceElement = document.createElement("JOB_TASK_REFERENCE");
            taskReferenceElement.setAttribute("refid", ((TaskDataModel) this.taskDataModelVector.get(i)).getTaskId());
            jobElement.appendChild(taskReferenceElement);
        }

        return jobElement;
    }

    @SuppressWarnings("unchecked")
    public Vector<TaskDataModel> getTasks() {
        return (Vector<TaskDataModel>) this.taskDataModelVector.clone();
    }

    protected void setTasks(Vector<TaskDataModel> newTaskDataModelVector) {
        this.taskDataModelVector = newTaskDataModelVector;
    }

    public boolean contains(TaskDataModel taskDataModel) {
        return this.taskDataModelVector.contains(taskDataModel);
    }

    public Calendar getArrivalDate() {
        return this.arrivalDate;
    }

    public Calendar getDueDate() {
        return this.dueDate;
    }

    protected void addTask(TaskDataModel newTask) {
        if (this.taskDataModelVector == null) {
            this.taskDataModelVector = new Vector<TaskDataModel>();
        }
        this.taskDataModelVector.add(newTask);
    }

    protected void removeTask(TaskDataModel task) {
        if (this.taskDataModelVector == null) {
            this.taskDataModelVector = new Vector<TaskDataModel>();
        }
        this.taskDataModelVector.remove(task);
    }

    protected void setName(String newName) {
        this.name = newName;
    }

    protected void setArrivalDate(Calendar newArrivalDate) {
        this.arrivalDate = newArrivalDate;
    }

    protected void setDueDate(Calendar newDueDate) {
        this.dueDate = newDueDate;
    }

    public String getJobId() {
        return jobId;
    }

    public String getName() {
        return name;
    }

    // public String toString()
    // {
    // return name;
    // }
}
