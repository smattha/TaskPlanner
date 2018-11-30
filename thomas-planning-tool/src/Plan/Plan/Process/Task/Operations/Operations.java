package Plan.Process.Task.Operations;

import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import Plan.WorkingArea;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.tools.Tool;

import Elements.Elements;
import Elements.Parts.Parts;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.*;

public class Operations extends TASK {

  private Parts basepart;

  private Parts matingpart;
  
  protected ThomasTool tool;
  
  public ThomasTool getTool() {return tool;};

  public WorkingArea workingplace;

  public String  description;
  public String name="Pick";
  
  
  ArrayList<Actions> actions;
  
 public Boolean genImpactTask()
 {
	BigInteger id1 = IDGenerator.getNewID();
    String newId = "thomas_" + id1;
	properties =new PROPERTIES();
	setDESCRIPTION("description");
	setId(newId);
	setNAME(name);
	setPROPERTIES(properties);
 		 
 	properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty("Testing",  "I workedddddddddddddddddddddddd"));

	return true;
 }            
}