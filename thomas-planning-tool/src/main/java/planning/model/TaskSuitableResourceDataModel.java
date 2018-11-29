package planning.model;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TaskSuitableResourceDataModel extends AbstractDataModel {
	private TaskDataModel task = null;
	private ResourceDataModel resource = null;
	private long operationTimeInMilliseconds = 1;
	private String setupCode;
	private HashMap<String, String> properties = new HashMap<String, String>();

	public TaskSuitableResourceDataModel(TaskDataModel task, ResourceDataModel resource,
			long operationTimeInMilliseconds, String setupCode, HashMap<String, String> properties) {
		this.task = task;
		this.resource = resource;
		if (operationTimeInMilliseconds > 0) {
			this.operationTimeInMilliseconds = operationTimeInMilliseconds;
		}
		this.setupCode = setupCode;
		if (properties != null)
			this.properties = properties;
	}

	public Node toXMLNode(Document document) {
		Element taskSuitableResourceElement = document.createElement("TASK_SUITABLE_RESOURCE");

		Element resourceReferenceElement = document.createElement("RESOURCE_REFERENCE");
		resourceReferenceElement.setAttribute("refid", this.resource.getResourceId());
		taskSuitableResourceElement.appendChild(resourceReferenceElement);

		Element taskReferenceElement = document.createElement("TASK_REFERENCE");
		taskReferenceElement.setAttribute("refid", this.task.getTaskId());
		taskSuitableResourceElement.appendChild(taskReferenceElement);

		Element operationTimePerBatchInSecondsElement = document.createElement("OPERATION_TIME_PER_BATCH_IN_SECONDS");
		operationTimePerBatchInSecondsElement
				.appendChild(document.createTextNode("" + (this.operationTimeInMilliseconds / 1000)));
		taskSuitableResourceElement.appendChild(operationTimePerBatchInSecondsElement);

		Element setupCodeElement = document.createElement("SET_UP_CODE");
		setupCodeElement.appendChild(document.createTextNode(this.setupCode));
		taskSuitableResourceElement.appendChild(setupCodeElement);

		return taskSuitableResourceElement;
	}

	public TaskDataModel getTaskDataModel() {
		return this.task;
	}

	public ResourceDataModel getResourceDataModel() {
		return this.resource;
	}

	public long getOperationTimeInMilliseconds() {
		return this.operationTimeInMilliseconds;
	}

	public String getSetUpCode() {
		return this.setupCode;
	}

	protected void setTaskDataModel(TaskDataModel newTaskDataModel) {
		this.task = newTaskDataModel;
	}

	protected void setResourceDataModel(ResourceDataModel newResourceDataModel) {
		this.resource = newResourceDataModel;
	}

	protected void setOperationTimeInMilliseconds(long newOperationTimeInMilliseconds) {
		this.operationTimeInMilliseconds = newOperationTimeInMilliseconds;
	}

	protected void setSetUpCode(String newSetUpCode) {
		this.setupCode = newSetUpCode;
	}

	protected void setProperty(String propertyName, String propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}
}
