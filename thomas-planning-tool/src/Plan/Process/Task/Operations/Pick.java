package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Align;
import Plan.Process.Task.Operations.Actions.Move;

import java.util.ArrayList;
import java.util.Vector;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Approach;
import Plan.Gripper;
import Plan.Process.Task.Operations.Actions.Grasp;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Retract;

public class Pick extends Operations{

  
  Pick()
  {
   actions=new ArrayList<Actions>();
   
   actions.add(new Navigate());
   actions.add(new Move());
   actions.add(new Detect());
   actions.add(new Approach());
   actions.add(new Align());
   actions.add(new Grasp());
   actions.add(new Retract());
   	  
  }

}