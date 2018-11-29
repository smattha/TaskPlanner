/**
 * 
 */
package lms.robopartner.mfg_planning_tool;

import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;
import lms.robopartner.mfg_planning_tool.impact.criteria.AreaCost;
import lms.robopartner.mfg_planning_tool.impact.interfaces.AverageRawCriteriaValuesDecisionPointAssignmentsConsumer;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import planning.MainPlanningTool;
import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
//import unused.UnifiedCriterion;

/**
 * @author Spyros
 *
 */
public class TaskSchedulerForLayout {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(TaskSchedulerForLayout.class);
	private Map<String, Point3D> resourcesAndPartsMapingForSR = null;
	private Vector<AssignmentDataModel> theResultAssignments = null;
	private AverageRawCriteriaValuesDecisionPointAssignmentsConsumer criteriaConsumer = null;
	private int internalDH = 1;
	private int internalMNA = 100;
	private int internalSR = 10;

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public TaskSchedulerForLayout(Map<String, Point3D> resourcesAndPartsMapingForSR, int internalDH, int internalMNA,
			int internalSR) {
		super();
		this.setResourcesAndPartsMapingForSR(resourcesAndPartsMapingForSR);
	}

	/**
	 * @return the theResultAssignments
	 */
	public Vector<AssignmentDataModel> getTheResultAssignments() {
		return theResultAssignments;
	}

	/**
	 * @param resourcesAndPartsMapingForSR the resourcesAndPartsMapingForSR to set
	 */
	private final void setResourcesAndPartsMapingForSR(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
	}

	/**
	 * @param theResultAssignments the theResultAssignments to set
	 */
	private final void setTheResultAssignments(Vector<AssignmentDataModel> theResultAssignments) {
		this.theResultAssignments = theResultAssignments;
	}

	/**
	 * @param criteriaConsumer the criteriaConsumer to set
	 */
	private final void setCriteriaConsumer(AverageRawCriteriaValuesDecisionPointAssignmentsConsumer criteriaConsumer) {
		this.criteriaConsumer = criteriaConsumer;
	}

	/**
	 * @return the resourcesAndPartsMapingForSR
	 */
	public final Map<String, Point3D> getResourcesAndPartsMapingForSR() {
		return resourcesAndPartsMapingForSR;
	}

	/**
	 * @return the criteriaConsumer
	 */
	public final AverageRawCriteriaValuesDecisionPointAssignmentsConsumer getCriteriaConsumer() {
		return criteriaConsumer;
	}

	/**
	 * @return the internalDH
	 */
	public final int getInternalDH() {
		return internalDH;
	}

	/**
	 * @param internalDH the internalDH to set
	 */
	public final void setInternalDH(int internalDH) {
		this.internalDH = internalDH;
	}

	/**
	 * @return the internalMNA
	 */
	public final int getInternalMNA() {
		return internalMNA;
	}

	/**
	 * @param internalMNA the internalMNA to set
	 */
	public final void setInternalMNA(int internalMNA) {
		this.internalMNA = internalMNA;
	}

	/**
	 * @return the internalSR
	 */
	public final int getInternalSR() {
		return internalSR;
	}

	/**
	 * @param internalSR the internalSR to set
	 */
	public final void setInternalSR(int internalSR) {
		this.internalSR = internalSR;
	}

	/**
	 * @param planningInputDocument
	 * @return time needed for evaluation in milliseconds
	 */
	public double evaluate(Document planningInputDocument) {
		String msg = ".evaluate():";
		double startTime = System.nanoTime();

		// Get the input to run Scheduling Impact
		MainPlanningTool mpt = new MainPlanningTool(planningInputDocument);

		// MAKE THE RESULTS
		mpt.initializeSimulator();

		// configure impact
		IMPACT mptIMPACT = (IMPACT) mpt.getAlgorithmFactoryforConfiguration()
				.getAlgorithmInstance(IMPACT.MULTICRITERIA);
		mptIMPACT.setDH(internalDH);
		mptIMPACT.setMNA(internalMNA);
		mptIMPACT.setSR(internalSR);
		AverageRawCriteriaValuesDecisionPointAssignmentsConsumer criteriaValueConsumer = new AverageRawCriteriaValuesDecisionPointAssignmentsConsumer();
		mptIMPACT.setDecisionPointAssignmentsConsumer(criteriaValueConsumer);

		// new AreaCost(resourcesAndPartsMapingForSR)

		mptIMPACT.setCriteria(new AbstractCriterion[] { new AreaCost((PLANNINGINPUT) resourcesAndPartsMapingForSR) });

//        new AbstractCriterion[] {  new Ergonomy(resourcesAndPartsMapingForSR),new Fatigue(resourcesAndPartsMapingForSR),new FloorSpace(resourcesAndPartsMapingForSR),new InvestmentCost(resourcesAndPartsMapingForSR),new Payload(resourcesAndPartsMapingForSR),new SaturationLevel(resourcesAndPartsMapingForSR),new TimeToComplete(resourcesAndPartsMapingForSR) });

		mpt.simulate();
		this.setTheResultAssignments(mpt.getAssignmentDataModelVector());
		this.setCriteriaConsumer(criteriaValueConsumer);
		double endTime = (System.nanoTime() - startTime) * 1000;
		logger.trace(msg + " Evaluation Duration: " + endTime + " milliseconds");
		return endTime;
	}

}
