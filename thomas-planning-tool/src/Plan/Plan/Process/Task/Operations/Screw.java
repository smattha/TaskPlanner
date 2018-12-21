package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Release;
import Plan.Process.Task.Operations.Actions.Attach;

import java.util.ArrayList;
import java.util.Vector;

import Elements.Tools.Screwdriver;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.Screwing;
import Plan.Process.Task.Operations.Actions.Parameters.Position;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Align;
import Plan.Process.Task.Operations.Actions.Approach;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Grasp;
import Plan.Process.Task.Operations.Actions.Detach;
import Plan.Process.Task.Operations.Actions.Retract;

public class Screw extends Operations{


  private Screwdriver tool1;

  public Vector  myOperations;
    
    
  Screw()
  {
   actions =new ArrayList<Actions>();
   
   

   
  }

  
  
  public Screw(String nam,ThomasTool tool, WorkingArea w1,TASKS tasks,TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints , Operations opPre, String Description)
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
  	
  	
    actions.add(new Move());
    actions.add(new Detect());
    actions.add(new Approach());
    actions.add(new Attach());
    actions.add(new Screwing());
    actions.add(new Detach());
    actions.add(new Retract());
  	 
  }
  
}