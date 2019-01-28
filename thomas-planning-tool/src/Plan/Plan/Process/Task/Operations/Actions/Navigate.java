package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;
import Plan.Process.Task.Operations.Actions.Parameters.navigationGoal;

public class Navigate extends Actions{


	  public Navigate( navigationGoal goal)
	  {
		  this.parameter = goal;
	  }
	  
	  public Navigate (String name,String description,String goal)
	  {
		  this.name=name;
		  this.descr=description;
		  navigationGoal g1=new navigationGoal(goal);
		  this.parameter = g1;
	  }
	  public Navigate()
	  {
		  
	  }
	  	
	  public Navigate( Position pose)
	  {
		  this.parameter = pose;
	  }
	  
	  Position getPose() {return (Position) parameter;};

}