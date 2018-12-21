package Plan.Process.Task.Operations;

import Elements.Tools.ThomasTool;

import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Retract;
import Plan.Process.Task.Operations.Actions.Release;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Parameters.Position;

import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Place extends Operations{

   public static int navigatePoseId=0;
   public static int movePoseId=0;
   public static int releasePoseId=0;
   public static int reactPoseId=0;
   
    public Place(String nam,ThomasTool tool, WorkingArea w1,TASKS tasks,TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints , Operations opPre, String Description,Position positionData[])
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

    	
    	actions.add(new Navigate(positionData[navigatePoseId]));
    	actions.add(new Move(positionData[movePoseId]));
    	actions.add(new Release());
    	actions.add(new Retract(positionData[reactPoseId]));
    	
    	genImpactTask();
     	getPrecedenceConstraints();
    	
    }

}