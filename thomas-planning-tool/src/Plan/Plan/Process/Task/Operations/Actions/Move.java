package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Move  extends Actions{

	  
	  Move(Position pose )
	  {
		  this.parameter = pose;
	  }
	  
	  public Move() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {return (Position) parameter;};
  
}