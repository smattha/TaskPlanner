package hibernate;
// Generated Jan 25, 2019 4:02:57 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Processdb generated by hbm2java
 */
public class Processdb implements java.io.Serializable {

	private int id;
	private String name;
	private String description;
	private Set tasksdbs = new HashSet(0);

	public Processdb() {
	}

	public Processdb(int id) {
		this.id = id;
	}

	public Processdb(int id, String name, String description, Set tasksdbs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.tasksdbs = tasksdbs;
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

	public Set getTasksdbs() {
		return this.tasksdbs;
	}

	public void setTasksdbs(Set tasksdbs) {
		this.tasksdbs = tasksdbs;
	}

}