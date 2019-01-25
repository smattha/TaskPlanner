package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Move  extends Actions{

	  
	  public Move(Position pose )
	  {
		  this.parameter = pose;
	  }
	  
	  public Move(String pose )
	  {
		  this.parameter =new Position(pose);
	  }
	  public Move() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {return (Position) parameter;};
  
}