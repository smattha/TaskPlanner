package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Approach extends Actions {

	  Approach(Position pose )
	  {
		  this.parameter = pose;
	  }
	  
	  public Approach() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {return (Position) parameter;};

}