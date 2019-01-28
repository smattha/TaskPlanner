package hibernate;
// Generated Jan 25, 2019 5:59:11 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Tasksdb generated by hbm2java
 */
public class Tasksdb implements java.io.Serializable {

	private int id;
	private String name;
	private String description;
	private String assigedResource;
	private Set operationsdbs = new HashSet(0);
	private Set processdbs = new HashSet(0);

	public Tasksdb() {
	}

	public Tasksdb(int id) {
		this.id = id;
	}

	public Tasksdb(int id, String name, String description, String assigedResource, Set operationsdbs, Set processdbs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.assigedResource = assigedResource;
		this.operationsdbs = operationsdbs;
		this.processdbs = processdbs;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssigedResource() {
		return this.assigedResource;
	}

	public void setAssigedResource(String assigedResource) {
		this.assigedResource = assigedResource;
	}

	public Set getOperationsdbs() {
		return this.operationsdbs;
	}

	public void setOperationsdbs(Set operationsdbs) {
		this.operationsdbs = operationsdbs;
	}

	public Set getProcessdbs() {
		return this.processdbs;
	}

	public void setProcessdbs(Set processdbs) {
		this.processdbs = processdbs;
	}

}
