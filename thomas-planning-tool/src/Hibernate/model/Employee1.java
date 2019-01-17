package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="parts", 
	   uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class Employee1 {

	@Id
	@Column(name="ID", nullable=false, unique=true, length=11)
	private int id;
	
	@Column(name="weight", length=45, nullable=true)
	private int weight;
	
	@Column(name="material", length=20, nullable=true)
	private String material;
	
	public String getPartscol() {
		return partscol;
	}
	public void setPartscol(String partscol) {
		this.partscol = partscol;
	}
	@Column(name="partscol", length=20, nullable=true)
	private String partscol;
	
	@Column(name="stiffness", nullable=true)
	private int stifness;
	
	
	@Column(name="maxDimension",length=20, nullable=true)
	private int maxDimension;
	
	@Column(name="Elements_id", length=20, nullable=true)
	private int el_id;
	

	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getStifness() {
		return stifness;
	}
	public void setStifness(int stifness) {
		this.stifness = stifness;
	}
	public int getMaxDimension() {
		return maxDimension;
	}
	public void setMaxDimension(int maxDimension) {
		this.maxDimension = maxDimension;
	}
	public int getEl_id() {
		return el_id;
	}
	public void setEl_id(int el_id) {
		this.el_id = el_id;
	}
	public int getOperations_id() {
		return Operations_id;
	}
	public void setOperations_id(int operations_id) {
		Operations_id = operations_id;
	}
	@Column(name="Operations_id", nullable=true)
	private int Operations_id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
