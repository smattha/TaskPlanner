package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Detach extends Actions {

	  Detach(Position pose )
	  {
		  this.parameter = pose;
	  }
	  
	  public Detach() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {return (Position) parameter;};

}