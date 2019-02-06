package Elements.resources;

import java.math.BigInteger;
import java.util.Vector;

import Elements.Sensors.Sensors;
import Elements.Tools.Gripper;
import Elements.Tools.Screwdriver;
import Plan.WorkingArea;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;

public class Arm2 extends ThomasResource {

	private Gripper tool1;

	private Screwdriver tool2;

	private Integer armpayload;

	private Integer armvelocity;

	private Sensors sensor;

	private Arm1 arm1;

	public Vector myMRP;

	public Arm2(BigInteger id1, String name, WorkingArea workingArea) {
		this.id = "Resources" + id1;
		this.name = name;
		this.workingArea = workingArea;
		gererateResource();
	}

	public Arm2(BigInteger id1, RESOURCES resource, String name) {
		this.id = "Resources" + id1;
		this.name = name;
		this.workingArea = null;
		this.resources = resource;
		gererateResource();
		add2Resources();
	}

	public Arm2(BigInteger id1, RESOURCES resource, String name, Double maxWeight, String stationID) {
		this.id = "Resource" + id1;
		this.name = name;
		this.workingArea = null;
		this.resources = resource;
		this.maxWeight = maxWeight;
		this.stationID = stationID;

		gererateResource();

		add2Resources();

	}

	public void connectedArm(Arm1 arm1) {

		connectedResource = arm1;
		this.arm1 = arm1;
		connected = true;

	}

}