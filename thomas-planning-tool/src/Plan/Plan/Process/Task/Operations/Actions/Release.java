package Plan.Process.Task.Operations.Actions;

import java.lang.reflect.Parameter;

import Plan.Process.Task.Operations.Actions.Parameters.IO;
import Plan.Process.Task.Operations.Actions.Parameters.Parameters;

public class Release extends Actions {

	Release(IO barcodeParameters) {
		this.parameter = barcodeParameters;
	}

	public Release() {

		this.parameter = new Parameters();
		// TODO Auto-generated constructor stub
	}

	IO getParam() {
		return (IO) parameter;
	};

}