package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class Navigate extends Actions {

	public Navigate(Position pose) {
		this.parameter = pose;
	}

	public Navigate() {

	}

	Position getPose() {
		return (Position) parameter;
	};

}