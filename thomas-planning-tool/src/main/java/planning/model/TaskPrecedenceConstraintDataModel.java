package planning.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class TaskPrecedenceConstraintDataModel extends AbstractDataModel {
	TaskDataModel preconditionTask = null;
	TaskDataModel postconditionTask = null;
	Boolean isConstraintTypeNextTaskInChain;
	Boolean isResourceUnavailableBetweenTasks;

	public TaskPrecedenceConstraintDataModel(TaskDataModel preconditionTask, TaskDataModel postconditionTask,
			Boolean isConstraintTypeNextTaskInChain, Boolean isResourceUnavailableBetweenTasks) {
		this.preconditionTask = preconditionTask;
		this.postconditionTask = postconditionTask;
		this.isConstraintTypeNextTaskInChain = isConstraintTypeNextTaskInChain;
		this.isResourceUnavailableBetweenTasks = isResourceUnavailableBetweenTasks;
	}

	public Node toXMLNode(Document document) {
		Element taskPrecedenceConstraintElement = document.createElement("TASK_PRECEDENCE_CONSTRAINT");

		Element preconditionTaskReferenceElement = document.createElement("PRECONDITION_TASK_REFERENCE");
		preconditionTaskReferenceElement.setAttribute("refid", this.preconditionTask.getTaskId());
		taskPrecedenceConstraintElement.appendChild(preconditionTaskReferenceElement);

		Element postconditionTaskReferenceElement = document.createElement("POSTCONDITION_TASK_REFERENCE");
		postconditionTaskReferenceElement.setAttribute("refid", this.postconditionTask.getTaskId());
		taskPrecedenceConstraintElement.appendChild(postconditionTaskReferenceElement);

		return taskPrecedenceConstraintElement;
	}

	public TaskDataModel getPreconditionTask() {
		return this.preconditionTask;
	}

	public TaskDataModel getPostconditionTask() {
		return this.postconditionTask;
	}

	public Boolean getIsConstraintTypeNextTaskInChain() {
		return isConstraintTypeNextTaskInChain;
	}

	public Boolean getIsResourceUnavailableBetweenTasks() {
		return isResourceUnavailableBetweenTasks;
	}

}