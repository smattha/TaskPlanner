package lms.thomas.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Session;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Elements.Tools.BarcodeScanner;
import Elements.Tools.Gripper;
import Elements.Tools.Screwdriver;
import Elements.Tools.ThomasTool;
import Elements.resources.Arm1;
import Elements.resources.Arm2;
import Elements.resources.ThomasResource;
import Plan.Tools;
import Plan.WorkingArea;
import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Place;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Parameters.Position;
import lms.thomas.*;//ctionsGenerator;
import lms.thomas.actionsGenerations.actionsGenerator;
import lms.thomas.planning.criteria.Utilization;
import eu.robopartner.ps.planner.planninginputmodel.DATE;
import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.RESOURCES;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCE;
import eu.robopartner.ps.planner.planninginputmodel.TASKSUITABLERESOURCES;
import gr.upatras.lms.util.Convert;
import hibernate.Parametersdb;
import hibernate.Part;
import hibernate.Processdb;
import hibernate.Actionsdb;
import hibernate.HibernateUtil;
import hibernate.Operationsdb;
import hibernate.Resourcesdb;
import hibernate.Tasksdb;
import hibernate.Toolparameters;
import hibernate.Toolsdb;
import hibernate.Workingareadb;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import planning.model.AssignmentDataModel;
import planning.model.io.AbstractInputSource;
import planning.model.io.FileInputSource;
import planning.model.io.HttpInputSource;
import planning.scheduler.algorithms.AbstractAlgorithm;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import lms.thomas.*;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.thomas.util.xml.createThomasProgram;

public class initializeFromDb {

	ThomasTool getTool(Toolsdb tooldb) {
		ThomasTool tool = null;

		String toolType = tooldb.getTooltype().getType();
		if (toolType.equals(Constants.TOOL_SCREWDRIVER)) {
			Toolparameters toolParam = (Toolparameters) tooldb.getToolparameterses().iterator().next();
			Screwdriver screw10 = new Screwdriver("screw", Convert.getInteger(toolParam.getValue()));
			tool = screw10;
		} else if (toolType.equals(Constants.TOOL_GRIPPER)) {
			Toolparameters toolParam = (Toolparameters) tooldb.getToolparameterses().iterator().next();

			Gripper g1 = new Gripper(Convert.getDouble(toolParam.getValue()));
			tool = g1;
		}

		return tool;

	}

	// Taskdb tasks;
	// Set operationsdb;
	Operations planningInput;

	boolean loadTask(int id) {

		Session sessionH = HibernateUtil.getSessionFactory().openSession();
		sessionH.beginTransaction();

		// second load() method example
		// tasks = (Taskdb) sessionH.load("hibernate.Taskdb",id);

		// operationsdb=tasks.getOperationsdbs();
		return true;

	}

	Processdb loadProcess(int id) {

		Session sessionH = HibernateUtil.getSessionFactory().openSession();
		sessionH.beginTransaction();

		// second load() method example
		Processdb processData = (Processdb) sessionH.load("hibernate.Processdb", id);

		return processData;

	}

	Actions actionsdb2Action(Actionsdb op) {

		Actions command = null;

		for (Iterator iter = op.getParametersdbs().iterator(); iter.hasNext();) {

			Parametersdb curOp = (Parametersdb) iter.next();

			command = new Move(curOp.getValue());

		}

		return command;
	}

	ThomasResource getResource(Resourcesdb res, RESOURCES resources) {
		ThomasResource resThomas = null;

		String type = res.getType();
		if (type.equals(Constants.RESOURCE_ARM1)) {
			Arm1 robot1 = new Arm1(IDGenerator.getNewID(), resources, res.getName(), res.getMaxWeight(),
					res.getStationControllerId());
			resThomas = robot1;
		} else if (type.equals(Constants.RESOURCE_ARM2)) {
			Arm2 robot1 = new Arm2(IDGenerator.getNewID(), resources, res.getName(), res.getMaxWeight(),
					res.getStationControllerId());
			resThomas = robot1;
		} else if (type.equals(Constants.RESOURCE_MRP)) {
			Arm1 robot1 = new Arm1(IDGenerator.getNewID(), resources, res.getName(), res.getMaxWeight(),
					res.getStationControllerId());
			resThomas = robot1;
		}

		return resThomas;

	}

	boolean getResources(RESOURCES resources, TASKSUITABLERESOURCES tasksuitableresources, TASKS tasks) {

		Session sessionH = HibernateUtil.getSessionFactory().openSession();
		sessionH.beginTransaction();

		// second load() method example
		List<Resourcesdb> res = (List<Resourcesdb>) sessionH.createCriteria(Resourcesdb.class).list();

		for (Iterator iter = res.iterator(); iter.hasNext();) {
			Resourcesdb resourcedb = (Resourcesdb) iter.next();

			// Arm1 robot1 = new Arm1(IDGenerator.getNewID(),resources,element.getName());

			ThomasResource resourceT = getResource(resourcedb, resources);

			for (Iterator iterTOOLS = resourcedb.getToolsdbs().iterator(); iterTOOLS.hasNext();) {
				Toolsdb tool = (Toolsdb) iterTOOLS.next();

				resourceT.addCompatibleTool(getTool(tool));

			}

			resourceT.fillTasksuitableresources(tasksuitableresources, tasks);

		}

		printSuitable(tasksuitableresources, tasks, resources);

		return true;
	}

	void printSuitable(TASKSUITABLERESOURCES tasksuitableresources, TASKS tasks, RESOURCES resources) {

		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		System.out.println("RESOURCES");
		for (Iterator iter = resources.getRESOURCE().iterator(); iter.hasNext();) {
			ThomasResource tr = (ThomasResource) iter.next();
			String param = null;
			for (Iterator iter2 = tr.compatibleToolList.iterator(); iter2.hasNext();) {
				ThomasTool toolCur = (ThomasTool) iter2.next();
				if (toolCur.ToolType.equals(Constants.TOOL_SCREWDRIVER)) {
					param = Convert.getString(((Screwdriver) toolCur).torque);
				} else if (toolCur.ToolType.equals(Constants.TOOL_GRIPPER)) {
					param = Convert.getString(((Gripper) toolCur).maxForce);
				}

				System.out.println(
						"Resource " + tr.name + " MaxWeight " + tr.maxWeight + "    " + toolCur.ToolType + " " + param);
			}
		}

		System.out.println("\n\n");
		System.out.println("TASK");
		for (Iterator iter = tasks.getTASK().iterator(); iter.hasNext();) {
			Operations tr = (Operations) iter.next();
			String param = null;
			if (tr.getTool().ToolType.equals(Constants.TOOL_SCREWDRIVER)) {
				param = Convert.getString(((Screwdriver) tr.getTool()).torque);
			} else if (tr.getTool().ToolType.equals(Constants.TOOL_GRIPPER)) {
				param = Convert.getString(((Gripper) tr.getTool()).maxForce);
			}

			System.out.println(
					"Task " + tr.name + "  Weight " + tr.weightPart + "    " + tr.getTool().ToolType + " " + param);
		}

		System.out.println("\n\n");
		System.out.println("RESOURCE TASK COMP");
		createThomasProgram doc = new createThomasProgram();

		for (Iterator iter = tasksuitableresources.getTASKSUITABLERESOURCE().iterator(); iter.hasNext();) {
			TASKSUITABLERESOURCE tr = (TASKSUITABLERESOURCE) iter.next();
			Iterator iter1 = tr.getPROPERTIES().getPROPERTY().iterator();
			PROPERTY p1 = (PROPERTY) iter1.next();
			PROPERTY p2 = (PROPERTY) iter1.next();
			System.out.println("Resource " + p1.getVALUE() + "   TASK " + p2.getVALUE());

			String msg = "Resource " + p1.getVALUE() + "   TASK " + p2.getVALUE();

			Element el = doc.createElement("action");

			Attr attr = doc.createAttribute("COM", msg);
			el.setAttributeNode(attr);
			doc.addElement(el);
		}

		doc.store(Constants.OUTPUT_FILE_PATH_RESOURCE_COMP);
	}

	boolean getTasksdb(Processdb processdb, TASKS tasks, TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints) {

		for (Iterator iter = processdb.getTasksdbs().iterator(); iter.hasNext();) {

			Tasksdb curOp = (Tasksdb) iter.next();
			getTask(curOp, tasks, theTaskprecedenceconstraints);
		}

		System.out.println("Load Tasks from db completed ");
		return true;
	}

	boolean getTask(Tasksdb opdb, TASKS tasks, TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints) {

		System.out.println("Load Task ");

		for (Iterator iter = opdb.getOperationsdbs().iterator(); iter.hasNext();) {
			Operationsdb curOp = (Operationsdb) iter.next();
			getOperation(curOp, tasks, theTaskprecedenceconstraints);

		}

		return true;
	}

	boolean templateFill(Operationsdb opdb, Operations operation) {

		for (Iterator iter = opdb.getActionsdbs().iterator(); iter.hasNext();) {

			Actionsdb curAction = (Actionsdb) iter.next();
			Actions command = null;

			if (curAction.getType().equals(Constants.ACTIONS_LOCALIZE)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_NAVIGATE)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_APPROACH)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_DETECT)) {
				((Part) opdb.getParts().iterator().next()).getId();
				command = new Detect(curAction.getName(), curAction.getDescription(), "3", "4",
						((Part) opdb.getParts().iterator().next()).getName());
			} else if (curAction.getType().equals(Constants.ACTIONS_ALIGN)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_ATTACH)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_GRASP)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_PRE_REACT)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_RETRACT)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_MOVE)) {

				// for (Iterator iter=op.getParametersdbs().iterator();iter.hasNext();) {
				Iterator iterParams = curAction.getParametersdbs().iterator();
				Parametersdb curOp = (Parametersdb) iterParams.next();

				command = new Move(curOp.getValue());

				// }
			} else if (curAction.getType().equals(Constants.ACTIONS_POST_ATTACH)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_RELEASE)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_DETECT_BARCODE)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_DETECT_SCREWING_POSE)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_SCREW)) {

			} else if (curAction.getType().equals(Constants.ACTIONS_READ_BARCODE)) {

			} else {
				Iterator iterParams = curAction.getParametersdbs().iterator();
				Parametersdb curOp = (Parametersdb) iterParams.next();

				command = new Move(curOp.getValue());

			}
			if (operation.equals(null)) {
			} else {
				operation.setAction(command);
			}
		}

		return true;
	}

	List<Operationsdb> ops = new ArrayList<Operationsdb>();
	List<Operations> op = new ArrayList<Operations>();;

	Operations getOperation(Operationsdb opdb, TASKS tasks, TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints) {

		Workingareadb working = opdb.getWorkingareadb();
		Toolsdb tool = (Toolsdb) opdb.getToolsdb();

		Toolparameters toolParam = (Toolparameters) tool.getToolparameterses().iterator().next();
		WorkingArea w1 = new WorkingArea(working.getName());
		Operations operation = null;

		if (opdb.getType().equals(Constants.OPERATIONS_PLACE)) {
			Part part = (Part) opdb.getParts().iterator().next();
			operation = new Place(opdb.getName(), getTool(tool), w1, tasks, theTaskprecedenceconstraints, null,
					opdb.getDescription(), part.getWeight());

		}
		// else if (opdb.getType().equals(Constants.OPERATIONS_PICK))
		else {

			operation = new Place(opdb.getName(), getTool(tool), w1, tasks, theTaskprecedenceconstraints, null,
					opdb.getDescription());

			for (Iterator iter = opdb.getActionsdbs().iterator(); iter.hasNext();) {

				Actionsdb curOp = (Actionsdb) iter.next();
				operation.setAction(actionsdb2Action(curOp));

			}

		}
		ops.add(opdb);
		op.add(operation);

		// System.out.println("It worked");
		return operation;
	}

	public PLANNINGINPUT loadPlanningInput(String processId) {
		String id = IDGenerator.getNewID() + "";
		PLANNINGINPUT aPlanninginput = MapToResourcesAndTasks.getPlanningInput(id, 1, 1, 2015, 1, 2, 2018, true);
		ObjectFactory myObjectFactory = new ObjectFactory();
		TASKS tasks = myObjectFactory.createTASKS();
		TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints = myObjectFactory.createTASKPRECEDENCECONSTRAINTS();
		RESOURCES resources = myObjectFactory.createRESOURCES();
		TASKSUITABLERESOURCES tasksuitableresources = myObjectFactory.createTASKSUITABLERESOURCES();
		Processdb processData = this.loadProcess(Convert.getInteger(processId));

		getTasksdb(processData, tasks, theTaskprecedenceconstraints);
		getResources(resources, tasksuitableresources, tasks);

		aPlanninginput.setTASKPRECEDENCECONSTRAINTS(theTaskprecedenceconstraints);
		aPlanninginput.setTASKS(tasks);
		aPlanninginput.setRESOURCES(resources);
		aPlanninginput.setTASKSUITABLERESOURCES(tasksuitableresources);

		LayoutPlanningInputGenerator.addWorkcenters(aPlanninginput, resources, AbstractAlgorithm.MULTICRITERIA);

		DATE arrivalDate = MapToResourcesAndTasks.getDate(1, 1, 2014, 0, 0, 0);
		DATE dueDate = MapToResourcesAndTasks.getDate(1, 1, 2018, 0, 0, 0);
		LayoutPlanningInputGenerator.addJobs(aPlanninginput, tasks,
				aPlanninginput.getWORKCENTERS().getWORKCENTER().get(0), arrivalDate, dueDate);

		return aPlanninginput;
	}

	boolean storeXML(Operations t1) {
		createThomasProgram doc = new createThomasProgram();

		t1.convert2XmlElement(doc);
		t1.convert2XmlElement(doc);
		doc.store("C:\\Users\\smatt\\Desktop\\xml\\xmlfile1.xml");
		return true;
	}

	public boolean generateTemplates() {
		Iterator iterdb = ops.iterator();
		for (Iterator iter = op.iterator(); iter.hasNext();) {
			Operationsdb opdbC = (Operationsdb) iterdb.next();
			Operations op = (Operations) iter.next();
			actionsGenerator.templateFill(opdbC, op);
			actionsGenerator.storeXML(op, Constants.OUTPUT_FOLDER_OPERATIONS_GENERATED);
		}
		return true;
	}

	public static void main(String[] args) {

		initializeFromDb d1 = new initializeFromDb();

		Document document = null;
		if (args.length == 0) {

			PLANNINGINPUT layoutPlanningInput = d1.loadPlanningInput("1");
			d1.generateTemplates();

			if (1 == 1) {
				// return ;
			}

			try {
				document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
			} catch (Exception e) {

			}
		}

		lms.thomas.planning.MainPlanningTool tool = new lms.thomas.planning.MainPlanningTool(document);
		tool.initializeSimulator();

		IMPACT mptIMPACT = (IMPACT) tool.getAlgorithmFactoryforConfiguration()
				.getAlgorithmInstance(IMPACT.MULTICRITERIA);

		// mptIMPACT.setCriteria(new AbstractCriterion[] { new FlowTime() });

		// mptIMPACT.setCriteria(new AbstractCriterion[] { new Idleness() });

		mptIMPACT.setCriteria(new AbstractCriterion[] { new Utilization("test") });

		int dh = 2;
		int mna = 100;
		int sr = 2;

		mptIMPACT.setDH(dh);
		mptIMPACT.setMNA(mna);
		mptIMPACT.setSR(sr);

		tool.simulate();

		Vector<AssignmentDataModel> assignments = tool.getAssignmentDataModelVector();
		createThomasProgram doc = new createThomasProgram();

		System.out.println("");
		System.out.println("");
		System.out.println("");
		for (AssignmentDataModel ass : assignments) {

			String taskID = ass.getTaskDataModel().getTaskId();
			Operations op1 = Operations.mapOperationsThomas2Id.get(taskID);
			System.out.println(ass.getTaskDataModel().getTaskName() + "   "
					+ ass.getResourceDataModel().getResourceName() + " " + op1.name);
			op1.assigned = ass.getResourceDataModel().getResourceName();
			op1.convert2XmlElement(doc);
		}

		doc.store(Constants.OUTPUT_FILE_PATH_IMPACT_OUT);

		return;
	}

}
