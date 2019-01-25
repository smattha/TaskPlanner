package Elements.Tools;

import java.util.Vector;

import xmlParser.Constants;

public class Screwdriver extends ThomasTool {

	private Integer weight;

	private Integer length;

	private String material;

	public int torque;

	private String headType;

	public Vector myTools;

	public Boolean isCompatible(ThomasTool t1) {
		
		if(t1.ToolType==Constants.TOOL_SCREWDRIVER)
		  {
			  
		if (((Screwdriver) t1).torque <= this.torque) {
			return true;
		} else {
			return false;
		}

		  }
		else 
		{return false;}
	}

	public Screwdriver(String type, int torque) {

		this.headType = type;
		this.torque = torque;
		ToolType=Constants.TOOL_SCREWDRIVER;

	}
}