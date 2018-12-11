package testingDemo;

import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import org.w3c.dom.Document;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
//rt testingDemo.DemoPlanningGenerator3D;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import planning.model.AssignmentDataModel;
import planning.model.JobDataModel;
import planning.model.MainDataModel;
import planning.model.PlanningInputDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.WorkcenterDataModel;
import planning.model.io.AbstractInputSource;
import planning.model.io.FileInputSource;
import planning.model.io.HttpInputSource;
import planning.model.io.XMLToPlanningInputModel;
import planning.scheduler.algorithms.AlgorithmFactory;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;

import planning.scheduler.simulation.PlanEndRule;
import planning.scheduler.simulation.PlanSimulator;
import planning.scheduler.simulation.WorkloadAllocationUntilDateEndRule;
import planning.scheduler.simulation.interfaces.OperationTimeCalculatorInterface;
import testingDemo.criteria.FlowTime;
import testingDemo.criteria.Idleness;
import testingDemo.criteria.Utilization;

public class MainPlanningTool {

	private double[] cumulatiCriteriaKpis = null;

	public double[] getKpis() {
		// System.err.println("AD HOC IMPLEMENTATION OF KPIS");
		return cumulatiCriteriaKpis;
	}

	// The input/output data model
	MainDataModel datamodel = null;
	// The simulator
	PlanSimulator simulator = null;
	// The results
	private Vector<AssignmentDataModel> assignments = null;

	// public MainPlanningTool(String webserverurl, String port, String
	// uriPlanningInputLocation, Calendar startDate)
	public MainPlanningTool(String webserverurl, String port, String uriPlanningInputLocation, String protocol) {
		// SETTING THE DEFAULT LOCALES AND TIME ZONES
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		java.util.TimeZone.setDefault(new java.util.SimpleTimeZone(java.util.TimeZone.getDefault().getRawOffset(),
				"DEFAULT APPLICATION TIME ZONE"));

		String parametersForInputXML = "";

		/* Building and initializing the data model */
		// Creating the input sources that must be translated into data model
		HttpInputSource inputsource = new HttpInputSource(webserverurl, port, uriPlanningInputLocation,
				parametersForInputXML, null, null, protocol);
		// Creating and attaching to a data model the input source
		this.datamodel = new MainDataModel(inputsource);
	}

	public MainPlanningTool(Document xmlPlanningInputDocument) {
		// SETTING THE DEFAULT LOCALES AND TIME ZONES
		Locale.setDefault(java.util.Locale.ENGLISH);
		TimeZone.setDefault(new java.util.SimpleTimeZone(java.util.TimeZone.getDefault().getRawOffset(),
				"DEFAULT APPLICATION TIME ZONE"));

		XMLToPlanningInputModel xmlToPlanningInputModel = new XMLToPlanningInputModel(xmlPlanningInputDocument);
		this.datamodel = new MainDataModel(xmlToPlanningInputModel.getPlanningInputDataModel(), null);
	}

	public MainPlanningTool(AbstractInputSource inputSource) {
		// SETTING THE DEFAULT LOCALES AND TIME ZONES
		Locale.setDefault(java.util.Locale.ENGLISH);
		TimeZone.setDefault(new java.util.SimpleTimeZone(java.util.TimeZone.getDefault().getRawOffset(),
				"DEFAULT APPLICATION TIME ZONE"));

		this.datamodel = new MainDataModel(inputSource);
	}

	public MainPlanningTool(MainDataModel mainDataModel) {
		// SETTING THE DEFAULT LOCALES AND TIME ZONES
		Locale.setDefault(java.util.Locale.ENGLISH);
		TimeZone.setDefault(new java.util.SimpleTimeZone(java.util.TimeZone.getDefault().getRawOffset(),
				"DEFAULT APPLICATION TIME ZONE"));

		this.datamodel = mainDataModel;
	}

	public void initializeSimulator() {
		// Get the input planning parameters
		PlanningInputDataModel planningInputDataModel = datamodel.getPlanningInputDataModel();

		Vector<WorkcenterDataModel> workcenters = planningInputDataModel.getWorkcenterDataModelVector();
		Vector<JobDataModel> jobs = planningInputDataModel.getJobDataModelVector();
		Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel
				.getTaskPrecedenceConstraintDataModelVector();
		Vector<TaskSuitableResourceDataModel> suitableResources = planningInputDataModel
				.getTaskSuitableResourceDataModelVector();

		// Construct the planning problem with the input parameters
		this.simulator = new PlanSimulator(workcenters, jobs, constraints, suitableResources,
				planningInputDataModel.getPlanStartDate());
	}

	public void initializeSimulator(Vector<AssignmentDataModel> lockedAssignments) {
		// Get the input planning parameters
		PlanningInputDataModel planningInputDataModel = datamodel.getPlanningInputDataModel();

		Vector<WorkcenterDataModel> workcenters = planningInputDataModel.getWorkcenterDataModelVector();
		Vector<JobDataModel> jobs = planningInputDataModel.getJobDataModelVector();
		Vector<TaskPrecedenceConstraintDataModel> constraints = planningInputDataModel
				.getTaskPrecedenceConstraintDataModelVector();
		Vector<TaskSuitableResourceDataModel> suitableResources = planningInputDataModel
				.getTaskSuitableResourceDataModelVector();

		// Construct the planning problem with the input parameters
		this.simulator = new PlanSimulator(workcenters, jobs, lockedAssignments, constraints, suitableResources,
				planningInputDataModel.getPlanStartDate());
	}

	public void initializeSimulator(OperationTimeCalculatorInterface operationTimeCalculator) {
		initializeSimulator();
		this.simulator.setOperationTimeCalculator(operationTimeCalculator);
	}

	public void initializeSimulator(OperationTimeCalculatorInterface operationTimeCalculator,
			Vector<AssignmentDataModel> lockedAssignments) {
		initializeSimulator(lockedAssignments);
		this.simulator.setOperationTimeCalculator(operationTimeCalculator);
	}

	/**
	 * The references are as follows {@link MainPlanningTool} has a
	 * {@link PlanSimulator} which has a {@link AlgorithmFactory} which provides a
	 * single {@link IMPACT} instance every time.
	 * 
	 * @return the instance of {@link AlgorithmFactory} that will be used so that it
	 *         can be configured.
	 * 
	 */
	public AlgorithmFactory getAlgorithmFactoryforConfiguration() {
		return this.simulator.getAlgorithmFactoryInstance();
	}

	public void simulate() {
		PlanningInputDataModel planningInputDataModel = datamodel.getPlanningInputDataModel();

		if (planningInputDataModel.continueAssignmentsAfterPlanEndDate()) {
			this.assignments = simulator.simulate(null);
			// System.err.println("AD HOC IMPLEMENTATION OF KPIS");
			this.cumulatiCriteriaKpis = simulator.getKpis();
		} else {
			this.assignments = simulator
					.simulate(new WorkloadAllocationUntilDateEndRule(planningInputDataModel.getPlanEndDate()));
			// System.err.println("AD HOC IMPLEMENTATION OF KPIS");
			this.cumulatiCriteriaKpis = simulator.getKpis();
		}
	}

	public void simulate(PlanEndRule planEndRule) {
		this.assignments = simulator.simulate(planEndRule);
		//System.err.println("AD HOC IMPLEMENTATION OF KPIS");
		this.cumulatiCriteriaKpis = simulator.getKpis();
	}

	@SuppressWarnings("unchecked")
	public Vector<AssignmentDataModel> getAssignmentDataModelVector() {
		return (Vector<AssignmentDataModel>) this.assignments.clone();
	}

	public static void main(String[] args) {
		boolean autosaveOutput = false;
		// Parameters used for identifying the input source
		if (args.length != 7 && args.length != 2) {
			System.out.println("Usage:");
			System.out.println(
					"java.exe planning.MainPlanningTool <webserverurl> <port> <uriPlanningInputLocation> <planStartDate> <planStartMonth> <planStartYear> <protocol>");
			System.out.println("java.exe planning.MainPlanningTool <planningInputFilePath> <planningOutputFilePath> ");
			// return;
		}

		/* Building and initializing the data model */
		// Creating the input sources that must be translated into data model
		AbstractInputSource inputsource = null;
		if (args.length == 7) {
			String webserverurl = args[0];
			String port = args[1];
			String uriPlanningInputLocation = args[2];
			String protocol = args[6];
			String parametersForInputXML = "";
			inputsource = new HttpInputSource(webserverurl, port, uriPlanningInputLocation, parametersForInputXML, null,
					null, protocol);
		}
		if (args.length == 2) {
			String planningInputFilePath = args[0];
			String planningOutputFilePath = args[1];
			inputsource = new FileInputSource(planningInputFilePath, planningOutputFilePath);
			// Auto save output
			autosaveOutput = true;
		}
		Document document = null;
		if (args.length == 0) {

			ThomasDemoEvaluation1 demo = new ThomasDemoEvaluation1();
			PLANNINGINPUT layoutPlanningInput = demo.generatePlanningInput();
			try {
				document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
			} catch (Exception e) {

			}
		}

		MainPlanningTool tool = new MainPlanningTool(document);
		tool.initializeSimulator();

		IMPACT mptIMPACT = (IMPACT) tool.getAlgorithmFactoryforConfiguration()
				.getAlgorithmInstance(IMPACT.MULTICRITERIA);

		//mptIMPACT.setCriteria(new AbstractCriterion[] { new FlowTime() });
		
		//mptIMPACT.setCriteria(new AbstractCriterion[] { new Idleness() });
		
		mptIMPACT.setCriteria(new AbstractCriterion[] { new Utilization("test") });
		mptIMPACT.setCriteria(new AbstractCriterion[] { new Utilization("test1") });		
		
		//mptIMPACT.setCriteria(new AbstractCriterion[] { new Payload() });
		
		
		int dh = 2;
		int mna = 100;
			
		int sr = 2;

		mptIMPACT.setDH(dh);
		mptIMPACT.setMNA(mna);
		mptIMPACT.setSR(sr);

		tool.simulate();

		Vector<AssignmentDataModel> assignments = tool.getAssignmentDataModelVector();

		System.out.println("");System.out.println("");System.out.println("");
		for (AssignmentDataModel ass:assignments) {

			System.out.println(ass.getTaskDataModel().getTaskName() + "   "
					+ ass.getResourceDataModel().getResourceName()  +" "+  ass.getTaskDataModel().getProperty("WorkingArea" ));

		}
		
	}
}
