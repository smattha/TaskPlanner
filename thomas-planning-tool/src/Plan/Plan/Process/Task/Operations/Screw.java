package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Attach;

import java.util.ArrayList;
import java.util.Vector;

import Elements.Tools.Screwdriver;
import Plan.Process.Task.Operations.Actions.Screwing;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Approach;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Detach;
import Plan.Process.Task.Operations.Actions.Retract;

public class Screw extends Operations{


  private Screwdriver tool1;

  public Vector  myOperations;
    
    
  Screw()
  {
   actions =new ArrayList<Actions>();
   
   
   actions.add(new Move());
   actions.add(new Detect());
   actions.add(new Approach());
   actions.add(new Attach());
   actions.add(new Screwing());
   actions.add(new Detach());
   actions.add(new Retract());
   
  }

}