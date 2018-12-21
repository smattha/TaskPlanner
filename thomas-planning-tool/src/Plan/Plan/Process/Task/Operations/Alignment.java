package Plan.Process.Task.Operations;

import java.util.ArrayList;

import Elements.Tools.ThomasTool;
import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.*;
import Plan.Process.Task.Operations.Actions.Parameters.Position;

import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;


public class Alignment extends Operations {

  
  public Alignment(String nam,ThomasTool tool, WorkingArea w1,TASKS tasks,TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints , Operations opPre, String Description)
  { 
  	this.workingArea=w1;
  	this.tool=tool;
  	this.name=nam;
  	this.idPreviousOperation=opPre;
  	this.theTaskprecedenceconstraints=theTaskprecedenceconstraints;  
  	this.tasks=tasks;
  	this.operationDescription=description;
  	tasks.getTASK().add(this);
  	
  	actions=new ArrayList<Actions>();
  	
  	Position pose=new Position(1.0,2.0,3.0,4.0,5.0,6.0,7.0);
  	
  	genImpactTask();
  	getPrecedenceConstraints();
  	
  	
  	actions.add(new Align());
  }

}