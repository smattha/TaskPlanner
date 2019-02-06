package lms.thomas.planning;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.w3c.dom.Document;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import Plan.Process.Task.Operations.Operations;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import lms.thomas.Constants;
import lms.thomas.planning.criteria.Utilization;
import lms.thomas.util.initializeFromDb;
import lms.thomas.util.xml.createThomasProgram;
import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;

public class planner {

	private PLANNINGINPUT layoutPlanningInput;
	Vector<AssignmentDataModel> assignments;
	private initializeFromDb d1 = new initializeFromDb();

	private int dh = 2;
	private int mna = 100;
	private int sr = 2;

	public PLANNINGINPUT getLayoutPlanningInput() {
		return layoutPlanningInput;
	}

	public void setLayoutPlanningInput(PLANNINGINPUT layoutPlanningInput) {
		this.layoutPlanningInput = layoutPlanningInput;
	}

	public void getCompResource(String processID) {
		layoutPlanningInput = d1.loadPlanningInput(processID);
	}

	public void generateTemplates() {
		d1.generateTemplates();
	}

	public boolean generateThomasProgram() {
		createThomasProgram doc = new createThomasProgram();

		System.out.println("");
		System.out.println("");
		System.out.println("");
		for (AssignmentDataModel ass : assignments) {

			// d1.storeXML(d1.planningInput);

			String taskID = ass.getTaskDataModel().getTaskId();
			Operations op1 = Operations.mapOperationsThomas2Id.get(taskID);

			System.out.println(ass.getTaskDataModel().getTaskName() + "   "
					+ ass.getResourceDataModel().getResourceName() + " " + op1.name);// .getProperty("WorkingArea" ));
			op1.assigned = ass.getResourceDataModel().getResourceName();

			op1.convert2XmlElement(doc);

		}

		doc.store(Constants.OUTPUT_FILE_PATH_IMPACT_OUT);

		return true;
	}

	public void executePlanner() {

		Document document = null;
		try {
			document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
		} catch (Exception e) {

		}

		MainPlanningTool tool = new MainPlanningTool(document);
		tool.initializeSimulator();

		IMPACT mptIMPACT = (IMPACT) tool.getAlgorithmFactoryforConfiguration()
				.getAlgorithmInstance(IMPACT.MULTICRITERIA);

		mptIMPACT.setCriteria(new AbstractCriterion[] { new Utilization("test") });

		mptIMPACT.setDH(dh);
		mptIMPACT.setMNA(mna);
		mptIMPACT.setSR(sr);
		tool.simulate();

		assignments = tool.getAssignmentDataModelVector();

		return;

	}

}
