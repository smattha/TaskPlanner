package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Release;

import java.util.ArrayList;

import Elements.Tools.Gripper;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Retract;

public class Place extends Operations{


  private Gripper tool1;

    
    
    public Place(String name,ThomasTool tool )
    {
    	this.tool=tool;
    	this.name=name;
    	actions=new ArrayList<Actions>();
    	
    	actions.add(new Navigate());
    	actions.add(new Move());
    	actions.add(new Release());
    	actions.add(new Retract());
    	
    }

}