package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;

import xmlParser.Constants;

public class Vision extends Parameters {

	
	public Vision(double distance)
	{
		ArrayList<String> inputDistance = new ArrayList<String>();
		
		inputDistance.add(Constants.VISION_DISTANCE_VALUE_ATTR);inputDistance.add(Double.toString(distance));
		
		
		inputs.add(inputDistance);

	}
}