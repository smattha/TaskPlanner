package hibernate;
// Generated Jan 25, 2019 4:02:57 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Resourcesdb generated by hbm2java
 */
public class Resourcesdb implements java.io.Serializable {

	private int id;
	private Elements elements;
	private String name;
	private String status;
	private String type;
	private Double maxWeight;
	private String stationControllerId;
	private Set workingareadbs = new HashSet(0);
	private Set toolsdbs = new HashSet(0);

	public Resourcesdb() {
	}

	public Resourcesdb(int id) {
		this.id = id;
	}

	public Resourcesdb(int id, Elements elements, String name, String status, String type, Double maxWeight,
			String stationControllerId, Set workingareadbs, Set toolsdbs) {
		this.id = id;
		this.elements = elements;
		this.name = name;
		this.status = status;
		this.type = type;
		this.maxWeight = maxWeight;
		this.stationControllerId = stationControllerId;
		this.workingareadbs = workingareadbs;
		this.toolsdbs = toolsdbs;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getMaxWeight() {
		return this.maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getStationControllerId() {
		return this.stationControllerId;
	}

	public void setStationControllerId(String stationControllerId) {
		this.stationControllerId = stationControllerId;
	}

	public Set getWorkingareadbs() {
		return this.workingareadbs;
	}

	public void setWorkingareadbs(Set workingareadbs) {
		this.workingareadbs = workingareadbs;
	}

	public Set getToolsdbs() {
		return this.toolsdbs;
	}

	public void setToolsdbs(Set toolsdbs) {
		this.toolsdbs = toolsdbs;
	}

}
