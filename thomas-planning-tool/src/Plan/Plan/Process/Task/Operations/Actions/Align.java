package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Align extends Actions {

	Align(Position pose) {
		this.parameter = pose;
	}

	public Align() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {
		return (Position) parameter;
	};

}