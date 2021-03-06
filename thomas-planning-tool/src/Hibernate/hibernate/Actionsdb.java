package hibernate;
// Generated Feb 6, 2019 3:37:35 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Actionsdb generated by hbm2java
 */
public class Actionsdb implements java.io.Serializable {

	private int id;
	private String name;
	private String description;
	private String type;
	private Set parametersdbs = new HashSet(0);
	private Set operationsdbs = new HashSet(0);

	public Actionsdb() {
	}

	public Actionsdb(int id) {
		this.id = id;
	}

	public Actionsdb(int id, String name, String description, String type, Set parametersdbs, Set operationsdbs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.parametersdbs = parametersdbs;
		this.operationsdbs = operationsdbs;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set getParametersdbs() {
		return this.parametersdbs;
	}

	public void setParametersdbs(Set parametersdbs) {
		this.parametersdbs = parametersdbs;
	}

	public Set getOperationsdbs() {
		return this.operationsdbs;
	}

	public void setOperationsdbs(Set operationsdbs) {
		this.operationsdbs = operationsdbs;
	}

}
