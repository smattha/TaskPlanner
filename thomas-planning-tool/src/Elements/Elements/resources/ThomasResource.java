package Elements.resources;

import java.awt.List;
import java.math.BigInteger;
import java.util.Vector;

import Elements.Tools.ThomasTool;
import Plan.WorkingArea;
import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Actions.Parameters.Position;
import eu.robopartner.ps.planner.planninginputmodel.NONWORKINGPERIODS;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCEAVAILABILITY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

public class ThomasResource extends RESOURCE {

	private BigInteger resourceID;

	private String status;
	private Position location;
	public Vector myElements;

	public String description;
	public String name = "Robot";
	public String type;
	
	public ThomasResource connectedResource;
	public Boolean connected=false;
	protected RESOURCES resources;
    //public BigInteger id;
	
    protected Vector<ThomasTool> compatibleToolList = new Vector<ThomasTool>();

	public Boolean addCompatibleTool(ThomasTool t) {
		compatibleToolList.add(t);
		return true;
	}

	protected WorkingArea workingArea;
	public ThomasResource(BigInteger id,RESOURCES resources,String name) {
		
		this.name = name;
		properties = new PROPERTIES();
		this.id=id+"";


		this.resources=resources;
		gererateResource();
		
		
		add2Resources();
		
		//fillTasksuitableresources(tasksuitableresources,tasks);
	}

	public ThomasResource() {
		// this.name=name;
	}

	

	public Boolean gererateResource() {

		properties = new PROPERTIES();
		
		properties.getPROPERTY()
		.add(MapToResourcesAndTasks.getProperty("Robot Property", "I workedddddddddddddddddddddddd"));
	
		ObjectFactory myObjectFactory = new ObjectFactory();
		RESOURCEAVAILABILITY resourceavailability = myObjectFactory.createRESOURCEAVAILABILITY();
		NONWORKINGPERIODS nonworkingperiods = myObjectFactory.createNONWORKINGPERIODS();
		resourceavailability.setNONWORKINGPERIODS(nonworkingperiods);

		setRESOURCEAVAILABILITY(resourceavailability);
		setDESCRIPTION(description);
		setId(id + "");
		setNAME(name);	
		
		setPROPERTIES(properties);
		
		String msg="true";
		if (connected==false)
		{
			msg="false";
			
		}
		properties.getPROPERTY()
		.add(MapToResourcesAndTasks.getProperty("Connected", msg));
		
		if (connected==true)
		{
			properties.getPROPERTY()
			.add(MapToResourcesAndTasks.getProperty("ConnectedId", connectedResource.id));
			
		}
		
		
		myObjectFactory = null;

		return true;
	}

	
	public Boolean fillTasksuitableresources(TASKSUITABLERESOURCES tasksuitableresources, TASKS tasks) {

		for (TASK t1 : tasks.getTASK()) {
			Operations t=(Operations)t1;
			if (true == suitableForTask(t)) {
				TASKSUITABLERESOURCE atasksuitableresource = MapToResourcesAndTasks.getTaskSuitableResource(this, t,
						LayoutPlanningInputGenerator.SETUP_CODE,
						LayoutPlanningInputGenerator.OPERATION_TIME_PER_BATCH_SECONDS);
				atasksuitableresource.setPROPERTIES(t.getPROPERTIES());
				tasksuitableresources.getTASKSUITABLERESOURCE().add(atasksuitableresource);
			}
		}
		;
		return true;
	}

	public Boolean suitableForTask(Operations t1) {
		for (ThomasTool t : compatibleToolList) {
			if (t.ToolType == t1.getTool().ToolType) {
				boolean flag = t.isCompatible(t1.getTool());
				if (flag == true) {
					return true;
				}
			}
		}
		return false;
	}

	
	protected void add2Resources()
	{
		resources.getRESOURCE().add(this);
		return;
	}
}