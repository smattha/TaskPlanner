package Plan.Process.Task.Operations;

import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Release;

import java.util.ArrayList;

import Elements.Tools.Gripper;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Retract;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;

public class Place extends Operations{


  private Gripper tool1;

    
    
    public Place(String name,ThomasTool tool, WorkingArea w1,TASKS tasks,TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints , Operations opPre)
    { 
    	this.workingArea=w1;
    	this.tool=tool;
    	this.name=name;
    	this.idpre=opPre;
    	this.theTaskprecedenceconstraints=theTaskprecedenceconstraints;  
    	this.tasks=tasks;
    	tasks.getTASK().add(this);
    	
    	actions=new ArrayList<Actions>();
    	
    	actions.add(new Navigate());
    	actions.add(new Move());
    	actions.add(new Release());
    	actions.add(new Retract());
    	genImpactTask();
    	getPrecedenceConstraints();
    	
    }

}