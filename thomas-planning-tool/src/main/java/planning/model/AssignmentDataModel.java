package planning.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

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

public class AssignmentDataModel extends AbstractDataModel {
	private TaskDataModel task = null;
	private ResourceDataModel resource = null;
	private Calendar timeOfDispatch = null;
	private long durationInMilliseconds = 0;
	private boolean locked = false;
	private HashMap<String, String> properties = new HashMap<String, String>();

//    public AssignmentDataModel(TaskDataModel task, ResourceDataModel resource, Calendar timeOfDispatch, long durationInMilliseconds, boolean locked)
//    {
//    	this(task, resource, timeOfDispatch, durationInMilliseconds, locked, null);
//    }
//    
	public AssignmentDataModel(TaskDataModel task, ResourceDataModel resource, Calendar timeOfDispatch,
			long durationInMilliseconds, boolean locked, HashMap<String, String> properties) {
		if (resource == null || task == null || timeOfDispatch == null) {
			throw new RuntimeException("Assignment cannot be created with null values");
		}
		this.resource = resource;
		this.task = task;
		this.timeOfDispatch = timeOfDispatch;
		if (durationInMilliseconds > 0) {
			this.durationInMilliseconds = durationInMilliseconds;
		} else {// CANNOT HAVE TASK WITH DURATION EQUALS TO ZERO
			this.durationInMilliseconds = 1l;
		}
		this.locked = locked;
		if (properties != null)
			this.properties = properties;
	}

	public Node toXMLNode(Document document) {
		Element assignmentElement = document.createElement("ASSIGNMENT");
		assignmentElement.setAttribute("locked", "" + this.locked);

//        Element taskElement = document.createElement("TASK");
//        taskElement.setAttribute("id", this.task.getTaskId());
//        assignmentElement.appendChild(taskElement);
//
//        Element resourceElement = document.createElement("RESOURCE");
//        resourceElement.setAttribute("id", this.resource.getResourceId());
//        assignmentElement.appendChild(resourceElement);

		Node taskNode = this.task.toXMLNode(document);
		assignmentElement.appendChild(taskNode);

		Node resourceNode = this.resource.toXMLNode(document);
		assignmentElement.appendChild(resourceNode);

		Element timeOfDispatchElement = document.createElement("TIME_OF_DISPATCH");
		timeOfDispatchElement.appendChild(XMLUtil.getNodeFromCalendar(this.timeOfDispatch, document));
		assignmentElement.appendChild(timeOfDispatchElement);

		Element durationInMilisecondsElement = document.createElement("DURATION_IN_MILLISECONDS");
		durationInMilisecondsElement.appendChild(document.createTextNode("" + this.durationInMilliseconds));
		assignmentElement.appendChild(durationInMilisecondsElement);

		Element propertiesElement = document.createElement("PROPERTIES");
		assignmentElement.appendChild(propertiesElement);

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

		return assignmentElement;
	}

	public TaskDataModel getTaskDataModel() {
		return this.task;
	}

	public ResourceDataModel getResourceDataModel() {
		return this.resource;
	}

	public Calendar getTimeOfDispatch() {
		return this.timeOfDispatch;
	}

	/**
	 * The duration time is calculated by adding the operational time, the setup
	 * time, and the resource down period.
	 *
	 * @return The duration in milliseconds
	 */
	public long getDurationInMilliseconds() {
		return this.durationInMilliseconds;
	}

	protected void setTimeOfDispatch(Calendar newTimeOfDispatch) {
		this.timeOfDispatch = newTimeOfDispatch;
	}

	protected void setDurationInMilliseconds(long newDurationInMilliseconds) {
		this.durationInMilliseconds = newDurationInMilliseconds;
	}

	protected void setResourceDataModel(ResourceDataModel newResourceDataModel) {
		this.resource = newResourceDataModel;
	}

	protected void setTaskDataModel(TaskDataModel newTaskDataModel) {
		this.task = newTaskDataModel;
	}

	public boolean isLocked() {
		return this.locked;
	}

	protected void lock() {
		this.locked = true;
	}

	protected void unlock() {
		this.locked = false;
	}

	protected void setProperty(String propertyName, String propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public HashMap<String, String> getProperties() {
		return (HashMap<String, String>) this.properties.clone();
	}
}
