package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.IO;

public class Screwing extends Actions {

	Screwing(IO ioParameters) {
		this.parameter = ioParameters;
	}

	public Screwing() {
		// TODO Auto-generated constructor stub
	}

	IO getParam() {
		return (IO) parameter;
	};

}