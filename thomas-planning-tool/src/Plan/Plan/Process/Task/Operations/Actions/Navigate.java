package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.navigationGoal;;

public class Navigate extends Actions {

	public Navigate() {

	}

	public Navigate(String name, String description, String working_area) {
		this.parameter = new navigationGoal(working_area);
	}

	// Position getPose() {return (Position) parameter;};

}