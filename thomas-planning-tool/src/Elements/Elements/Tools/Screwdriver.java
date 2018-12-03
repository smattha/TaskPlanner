package Elements.Tools;

import java.util.Vector;

public class Screwdriver extends ThomasTool {

	private Integer weight;

	private Integer length;

	private String material;

	private int torque;

	private String headType;

	public Vector myTools;

	public Boolean isCompatible(ThomasTool t1) {
		if (((Screwdriver) t1).torque <= this.torque) {
			return true;
		} else {
			return false;
		}

	}

	public Screwdriver(String type, int torque) {

		this.headType = type;
		this.torque = torque;

	}
}