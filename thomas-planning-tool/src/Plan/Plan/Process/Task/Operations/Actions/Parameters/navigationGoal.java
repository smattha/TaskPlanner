


package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;

import xmlParser.Constants;

public class navigationGoal extends Parameters {

	
	public navigationGoal(String workingArea)
	{
		ArrayList<String> NAvIGATION_ID = new ArrayList<String>();
		
		NAvIGATION_ID.add(Constants.NAVIGATION_ID);NAvIGATION_ID.add(workingArea);



		inputs.add(NAvIGATION_ID);

	}
}