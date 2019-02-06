package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Retract extends Actions {

	public Retract(Position pose) {
		this.parameter = pose;
	}

	public Retract() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {
		return (Position) parameter;
	};

}