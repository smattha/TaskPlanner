package hibernate;
// Generated Feb 6, 2019 3:37:35 PM by Hibernate Tools 3.5.0.Final

/**
 * Sensors generated by hbm2java
 */
public class Sensors implements java.io.Serializable {

	private int id;
	private Elements elements;
	private Integer sensorId;
	private String category;
	private Integer weight;
	private Integer arm1Id;
	private Integer arm1MrpId;
	private Integer arm1MrpMobileId;
	private Integer arm1MrpMobileResourecsId;
	private Integer platformId;
	private Integer platformMrpId;
	private Integer platformMrpMobileId;
	private Integer platformMrpMobileResourecsId;
	private Integer arm2Id;
	private Integer arm2MrpId;
	private Integer arm2MrpMobileId;
	private Integer arm2MrpMobileResourecsId;

	public Sensors() {
	}

	public Sensors(int id) {
		this.id = id;
	}

	public Sensors(int id, Elements elements, Integer sensorId, String category, Integer weight, Integer arm1Id,
			Integer arm1MrpId, Integer arm1MrpMobileId, Integer arm1MrpMobileResourecsId, Integer platformId,
			Integer platformMrpId, Integer platformMrpMobileId, Integer platformMrpMobileResourecsId, Integer arm2Id,
			Integer arm2MrpId, Integer arm2MrpMobileId, Integer arm2MrpMobileResourecsId) {
		this.id = id;
		this.elements = elements;
		this.sensorId = sensorId;
		this.category = category;
		this.weight = weight;
		this.arm1Id = arm1Id;
		this.arm1MrpId = arm1MrpId;
		this.arm1MrpMobileId = arm1MrpMobileId;
		this.arm1MrpMobileResourecsId = arm1MrpMobileResourecsId;
		this.platformId = platformId;
		this.platformMrpId = platformMrpId;
		this.platformMrpMobileId = platformMrpMobileId;
		this.platformMrpMobileResourecsId = platformMrpMobileResourecsId;
		this.arm2Id = arm2Id;
		this.arm2MrpId = arm2MrpId;
		this.arm2MrpMobileId = arm2MrpMobileId;
		this.arm2MrpMobileResourecsId = arm2MrpMobileResourecsId;
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

	public Integer getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getArm1Id() {
		return this.arm1Id;
	}

	public void setArm1Id(Integer arm1Id) {
		this.arm1Id = arm1Id;
	}

	public Integer getArm1MrpId() {
		return this.arm1MrpId;
	}

	public void setArm1MrpId(Integer arm1MrpId) {
		this.arm1MrpId = arm1MrpId;
	}

	public Integer getArm1MrpMobileId() {
		return this.arm1MrpMobileId;
	}

	public void setArm1MrpMobileId(Integer arm1MrpMobileId) {
		this.arm1MrpMobileId = arm1MrpMobileId;
	}

	public Integer getArm1MrpMobileResourecsId() {
		return this.arm1MrpMobileResourecsId;
	}

	public void setArm1MrpMobileResourecsId(Integer arm1MrpMobileResourecsId) {
		this.arm1MrpMobileResourecsId = arm1MrpMobileResourecsId;
	}

	public Integer getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getPlatformMrpId() {
		return this.platformMrpId;
	}

	public void setPlatformMrpId(Integer platformMrpId) {
		this.platformMrpId = platformMrpId;
	}

	public Integer getPlatformMrpMobileId() {
		return this.platformMrpMobileId;
	}

	public void setPlatformMrpMobileId(Integer platformMrpMobileId) {
		this.platformMrpMobileId = platformMrpMobileId;
	}

	public Integer getPlatformMrpMobileResourecsId() {
		return this.platformMrpMobileResourecsId;
	}

	public void setPlatformMrpMobileResourecsId(Integer platformMrpMobileResourecsId) {
		this.platformMrpMobileResourecsId = platformMrpMobileResourecsId;
	}

	public Integer getArm2Id() {
		return this.arm2Id;
	}

	public void setArm2Id(Integer arm2Id) {
		this.arm2Id = arm2Id;
	}

	public Integer getArm2MrpId() {
		return this.arm2MrpId;
	}

	public void setArm2MrpId(Integer arm2MrpId) {
		this.arm2MrpId = arm2MrpId;
	}

	public Integer getArm2MrpMobileId() {
		return this.arm2MrpMobileId;
	}

	public void setArm2MrpMobileId(Integer arm2MrpMobileId) {
		this.arm2MrpMobileId = arm2MrpMobileId;
	}

	public Integer getArm2MrpMobileResourecsId() {
		return this.arm2MrpMobileResourecsId;
	}

	public void setArm2MrpMobileResourecsId(Integer arm2MrpMobileResourecsId) {
		this.arm2MrpMobileResourecsId = arm2MrpMobileResourecsId;
	}

}
