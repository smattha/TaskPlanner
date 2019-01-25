package hibernate;
// Generated Jan 25, 2019 4:02:57 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Part generated by hbm2java
 */
public class Part implements java.io.Serializable {

	private int id;
	private Double weight;
	private String type;
	private String name;
	private Set operationsdbs = new HashSet(0);

	public Part() {
	}

	public Part(int id) {
		this.id = id;
	}

	public Part(int id, Double weight, String type, String name, Set operationsdbs) {
		this.id = id;
		this.weight = weight;
		this.type = type;
		this.name = name;
		this.operationsdbs = operationsdbs;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getOperationsdbs() {
		return this.operationsdbs;
	}

	public void setOperationsdbs(Set operationsdbs) {
		this.operationsdbs = operationsdbs;
	}

}