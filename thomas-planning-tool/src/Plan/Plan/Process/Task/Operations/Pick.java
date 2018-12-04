package Plan.Process.Task.Operations;

import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Align;
import Plan.Process.Task.Operations.Actions.Move;

import java.util.ArrayList;
import java.util.Vector;

import Elements.Tools.Gripper;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Release;
import Plan.Process.Task.Operations.Actions.Approach;
import Plan.Process.Task.Operations.Actions.Grasp;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Retract;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;

public class Pick extends Operations{

  
	
    public Pick(String name,ThomasTool tool, WorkingArea w1,TASKS tasks,TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints , Operations opPre)
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
    	   actions.add(new Detect());
    	   actions.add(new Approach());
    	   actions.add(new Align());
    	   actions.add(new Grasp());
    	   actions.add(new Retract());
    	   
    	genImpactTask();
    	getPrecedenceConstraints();
    	
    }

    public Pick(TASK t) {
	 
	 this.description=t.getDESCRIPTION();
	 this.id=t.getId();
	 this.name=t.getNAME();
	 this.properties=t.getPROPERTIES();
	 
 }

}