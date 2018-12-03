package Plan;


import java.math.BigInteger;
import java.util.Vector;

import Plan.Process.Task.Operations.Actions.Parameters.Position;
import lms.robopartner.datamodel.map.IDGenerator;

public class WorkingArea extends AssemblyStation{

  public BigInteger id;

  public String name;

  private String description;

  private Position location;

  //public Vector  myAssemblyStation;

  
  public  WorkingArea(String name )
  {
	  id = IDGenerator.getNewID();	  
	  this.name=name;
  }
  
  
}