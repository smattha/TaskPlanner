package hibernate;
// Generated Jan 24, 2019 3:44:32 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Resources generated by hbm2java
 */
public class Resources implements java.io.Serializable {

	private int id;
	private Elements elements;
	private Integer resourceId;
	private String status;
	private Set workingareadbs = new HashSet(0);

	public Resources() {
	}

	public Resources(int id) {
		this.id = id;
	}

	public Resources(int id, Elements elements, Integer resourceId, String status, Set workingareadbs) {
		this.id = id;
		this.elements = elements;
		this.resourceId = resourceId;
		this.status = status;
		this.workingareadbs = workingareadbs;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Elements getElements() {
		return this.elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

	public Integer getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set getWorkingareadbs() {
		return this.workingareadbs;
	}

	public void setWorkingareadbs(Set workingareadbs) {
		this.workingareadbs = workingareadbs;
	}

}