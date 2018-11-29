package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Attach extends Actions {

	Attach(Position pose) {
		this.parameter = pose;
	}

	public Attach() {
		// TODO Auto-generated constructor stub
	}

	Position getPose() {
		return (Position) parameter;
	};

}