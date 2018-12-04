package Elements.resources;



import java.math.BigInteger;

import Elements.Sensors.Sensors;
import Elements.Tools.Gripper;
import Elements.Tools.Screwdriver;
import Plan.WorkingArea;
import eu.robopartner.ps.planner.planninginputmodel.NONWORKINGPERIODS;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCEAVAILABILITY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

public class Arm1 extends ThomasResource {

  private Gripper tool1;

  private Screwdriver tool2;

  private Integer armpayload;

  private Integer armvelocity;

  private Sensors sensor;

  //public BigInteger id;
  
  private Arm2 arm2=null;
  
  public Arm1(BigInteger id1, String name, WorkingArea workingArea)
  {
	  this.id="Resources"+id1;
	  this.name=name;
	  this.workingArea=workingArea;
	  gererateResource();
  }

  
  public Arm1(BigInteger id1,RESOURCES resource, String name)
  {
	  this.id="Resource"+id1;
	  this.name=name;
	  this.workingArea=null;
	  this.resources=resource;
	  
	  gererateResource();
	  
	  
	  add2Resources();
	  
  }
  
  public void connectedArm(Arm2 arm2) {
	  
	  connectedResource=arm2;
	  
	  this.arm2=arm2;
	  connected=true;
  }
  

  
}
