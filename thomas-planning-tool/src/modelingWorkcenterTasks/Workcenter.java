package modelingWorkcenterTasks;

import java.util.Vector;

public class Workcenter {

	
	
	private String workcenterId="wc1";
	private String name="Station1";
	private String Description="testing";
	private String  algorithName="EDD";
	private Vector resourceName=new Vector();
	
    public Boolean addResource(String Name){
    	
    	resourceName.add(Name);
    
    	return true;
    }
	
}
