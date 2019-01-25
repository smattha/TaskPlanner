package hibernate;
// Generated Jan 25, 2019 4:02:57 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Tooltype generated by hbm2java
 */
public class Tooltype implements java.io.Serializable {

	private int id;
	private String type;
	private Set toolsdbs = new HashSet(0);

	public Tooltype() {
	}

	public Tooltype(int id) {
		this.id = id;
	}

	public Tooltype(int id, String type, Set toolsdbs) {
		this.id = id;
		this.type = type;
		this.toolsdbs = toolsdbs;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set getToolsdbs() {
		return this.toolsdbs;
	}

	public void setToolsdbs(Set toolsdbs) {
		this.toolsdbs = toolsdbs;
	}

}
