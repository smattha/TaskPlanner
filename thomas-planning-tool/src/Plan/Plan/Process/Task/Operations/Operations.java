package Plan.Process.Task.Operations;

import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.POSTCONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PRECONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINT;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
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

	protected TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints ;
  private Parts basepart;

  private Parts matingpart;
  
  protected ThomasTool tool;
  
  public ThomasTool getTool() {return tool;};

  public WorkingArea workingplace;

  public String  description;
  public String name="Pick";
  TASKS tasks;
  
  public WorkingArea workingArea;
  
  ArrayList<Actions> actions;
  
  protected Operations idpre;
  
  //TASKPRECEDENCECONSTRAINTS constraints; 
  
 public Boolean genImpactTask()
 {
	BigInteger id1 = IDGenerator.getNewID();
    String newId = "task_" + id1;
	properties =new PROPERTIES();
	setDESCRIPTION("description");
	setId(newId);
	setNAME(name);
	setPROPERTIES(properties);
 		 
 	properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty("WorkingArea",  workingArea.name+""));
 	
 	properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty("idpre",  idpre +""));
	

	return true;
 }         
 
 
	public  TASKPRECEDENCECONSTRAINTS getPrecedenceConstraints() {


		
			if ( idpre != null ) {
				TASKPRECEDENCECONSTRAINT constraint = new TASKPRECEDENCECONSTRAINT();

				PRECONDITIONTASKREFERENCE pre = new PRECONDITIONTASKREFERENCE();
				POSTCONDITIONTASKREFERENCE post = new POSTCONDITIONTASKREFERENCE();

				pre.setRefid(idpre.getId());
				post.setRefid(this.id);
				constraint.setPRECONDITIONTASKREFERENCE(pre);
				constraint.setPOSTCONDITIONTASKREFERENCE(post);
				theTaskprecedenceconstraints.getTASKPRECEDENCECONSTRAINT().add(constraint);
			}
		
		return theTaskprecedenceconstraints;
	}

}