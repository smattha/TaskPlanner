package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.Vision;
import lms.thomas.*;

public class Detect extends Actions {

	public Detect(Vision parameter) {
		this.parameter = parameter;
	}

	public Detect(String name, String description, String sourceframe, String targetF, String detected_part) {
		Vision parameter = new Vision(sourceframe, targetF, detected_part);

		this.parameter = parameter;
		this.name = name;
		this.descr = description;
		this.type = Constants.VISION_TYPE;

	}

	public Detect() {
		// TODO Auto-generated constructor stub
	}

	Vision getParam() {
		return (Vision) parameter;
	};

}