package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Align;

import java.util.ArrayList;
import Plan.Process.Task.Operations.Actions.*;

public class Alignment extends Operations {



  boolean setAction()
  {
	  actions=new ArrayList<Actions>();
	  
	  actions.add(new Align());
	  //this.actions
	  return true;
  }
  

}