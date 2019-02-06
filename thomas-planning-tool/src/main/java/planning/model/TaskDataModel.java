package planning.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

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

public class TaskDataModel extends AbstractDataModel {
	private String taskId = null;
	private String taskName = null;
	private String description = null;
	private Calendar arrivalDate = null;
	private Calendar dueDate = null;
	private HashMap<String, String> properties = new HashMap<String, String>();

	public TaskDataModel(String taskId, String taskName,
			String description/* , Calendar arrivalDate, Calendar dueDate */, HashMap<String, String> properties) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.description = description;
		if (properties != null)
			this.properties = properties;
	}

	public Node toXMLNode(Document document) {
		Element taskElement = document.createElement("TASK");
		taskElement.setAttribute("id", this.taskId);

		Element nameElement = document.createElement("NAME");
		nameElement.appendChild(document.createTextNode(this.getTaskName()));
		taskElement.appendChild(nameElement);

		Element descriptionElement = document.createElement("DESCRIPTION");
		descriptionElement.appendChild(document.createTextNode(this.description));
		taskElement.appendChild(descriptionElement);

		Element propertiesElement = document.createElement("PROPERTIES");
		taskElement.appendChild(propertiesElement);

		if (this.properties != null) {
			for (Entry<String, String> entry : properties.entrySet()) {
				Element propertyElement = document.createElement("PROPERTY");
				Element propertyNameElement = document.createElement("NAME");
				propertyNameElement.appendChild(document.createTextNode(entry.getKey()));
				propertyElement.appendChild(propertyNameElement);
				Element propertyValueElement = document.createElement("VALUE");
				propertyValueElement.appendChild(document.createTextNode(entry.getValue()));
				propertyElement.appendChild(propertyValueElement);
				propertiesElement.appendChild(propertyElement);
			}
		}

		return taskElement;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	protected void setTaskName(String newTaskName) {
		this.taskName = newTaskName;
	}

	public Calendar getTaskDueDate() {
		return this.dueDate;
	}

	public void setTaskDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public Calendar getTaskArrivalDate() {
		return this.arrivalDate;
	}

	/*
	 * TODO : Must make sure that the values do not change after the start of the
	 * schedule ... by mistake ... maybe protected ???
	 */
	public void setTaskArrrivalDate(Calendar arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	protected void setProperty(String propertyName, String propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}
}
