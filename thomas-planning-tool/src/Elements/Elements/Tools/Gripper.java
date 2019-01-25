package Elements.Tools;

import java.util.Vector;

import xmlParser.Constants;

public class Gripper  extends ThomasTool{

    public Vector  myTools;
    
	public Boolean isCompatible(ThomasTool t1) {
		
		if(t1.ToolType==Constants.TOOL_GRIPPER)
		  {
			  
		if (((Gripper) t1).maxForce<= this.maxForce) {
			return true;
		} else {
			return false;
		}

		  }
		else 
		{return false;}
	}
public double maxForce;
	public Gripper(double maxForce) {

		//this.headType = type;
		this.maxForce = maxForce;
		ToolType=Constants.TOOL_GRIPPER;

	}

}