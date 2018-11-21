package Plan.Process.Task.Operations;

import java.util.ArrayList;


import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Release;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Retract;

public class Insert extends Operations{
 
 
  Insert()
  {
	  actions=new ArrayList<Actions>(); 
	  actions.add(new Navigate());
	  actions.add(new Move());
	  actions.add(new Detect());
	  actions.add(new Move());
	  actions.add(new Release());
	  actions.add(new Retract());
	 
  }
}