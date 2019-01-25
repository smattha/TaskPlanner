package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;

import xmlParser.Constants;

public class Vision extends Parameters {

	
	public Vision(String sourceF,String targetF,String detected_part)
	{
		ArrayList<String> VISION_DETECTED_PART = new ArrayList<String>();
		ArrayList<String> VISION_SOURCE_FRAME = new ArrayList<String>();
		ArrayList<String> VISION_TARGET_FRAME = new ArrayList<String>();
		
		VISION_DETECTED_PART.add(Constants.VISION_DETECTED_PART);VISION_DETECTED_PART.add(detected_part);
		
		
		VISION_SOURCE_FRAME.add(Constants.VISION_SOURCE_FRAME);VISION_SOURCE_FRAME.add(sourceF);
		VISION_TARGET_FRAME.add(Constants.VISION_TARGET_FRAME);VISION_TARGET_FRAME.add(targetF);
		
		
		inputs.add(VISION_DETECTED_PART);
		inputs.add(VISION_SOURCE_FRAME);
		inputs.add(VISION_TARGET_FRAME);

	}
}