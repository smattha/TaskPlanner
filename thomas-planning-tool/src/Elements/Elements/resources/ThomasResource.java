package Elements.resources;

import java.awt.List;
import java.math.BigInteger;
import java.util.Vector;

import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Actions.Parameters.Position;
import eu.robopartner.ps.planner.planninginputmodel.NONWORKINGPERIODS;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCEAVAILABILITY;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;

public class ThomasResource extends RESOURCE {

	private Integer resourceID;

	private String status;

	private Position location;

	public Vector myElements;

	public String description;
	public String name = "Robot";

	protected Vector<ThomasTool> compatibleToolList = new Vector<ThomasTool>();

	public Boolean addCompatibleTool(ThomasTool t) {
		compatibleToolList.add(t);
		return true;

	}

	public ThomasResource(String name) {
		this.name = name;
	}

	public ThomasResource() {
		// this.name=name;
	}

	public Boolean gererateResource() {
		BigInteger id = IDGenerator.getNewID();

		properties = new PROPERTIES();
		ObjectFactory myObjectFactory = new ObjectFactory();
		RESOURCEAVAILABILITY resourceavailability = myObjectFactory.createRESOURCEAVAILABILITY();
		NONWORKINGPERIODS nonworkingperiods = myObjectFactory.createNONWORKINGPERIODS();
		resourceavailability.setNONWORKINGPERIODS(nonworkingperiods);

		setRESOURCEAVAILABILITY(resourceavailability);
		setDESCRIPTION(description);
		setId(id + "");
		setNAME(name);
		setPROPERTIES(properties);
		myObjectFactory = null;

		properties.getPROPERTY()
				.add(MapToResourcesAndTasks.getProperty("Robot Property", "I workedddddddddddddddddddddddd"));

		return true;
	}

	;

	public Boolean fillTasksuitableresources(TASKSUITABLERESOURCES tasksuitableresources, Vector<Operations> ops) {

		for (Operations t : ops) {
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
				boolean flag = ((Operations) t1).getTool().isCompatible(t);
				if (flag == true) {
					return true;
				}
			}
		}

		return false;
	}

}