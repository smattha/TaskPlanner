package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.awt.Point;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.TreeNode;

//import lms.robopartner.mfg_planning_tool.DemoPlanningGenerator ;

import org.slf4j.LoggerFactory;
import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * 
 * Old scheduling cost. This class has been deprecated. In this class, the
 * robot's and human's costs where just taken fromt he user input
 * 
 * @author Jason
 *
 */
public class InvestmentCostold extends AbstractCriterion {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(InvestmentCostold.class);
	private static final String CRITERION_NAME = "INVESTMENT COST";
	private Map<String, Point> resourcesAndPartsMapingForSR;

	/**
	 * Hide constructor
	 */
	@SuppressWarnings("unused")
	private InvestmentCostold() {
		super();
	}

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public InvestmentCostold(Map<String, Point> resourcesAndPartsMapingForSR) {
		super();
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;

	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		String msg = ".getValue(): ";
		// Each path is a solution. There are SR paths provided each time.
		int sr = paths.size();
		double investmentCostSum = 0;
		double partialInvestmentCost = 0;

		for (int i = 0; i < sr; i++) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);

			// path is solution
			for (int j = 0; j < path.length; j++) {
				LayerNode node = (LayerNode) path[j];
				for (Assignment assignment : node.getNodeAssignments()) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();

					// calculate
					boolean missingTask = !resourcesAndPartsMapingForSR.containsKey(taskId);
					boolean missingResource = !resourcesAndPartsMapingForSR.containsKey(resourceId);
					if (missingTask) {
						String msg2 = msg + "No task mapping found for task with id=" + taskId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}
					if (missingResource) {
						String msg2 = msg + "No resource mapping found for resource with id=" + resourceId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}
					double partialInvestmentCostTemp = 0;
					if (missingTask || missingResource) {
//                        // If a resource or task is missing, this solution is
//                        // totally bad.
//                        partialAreaCost = Double.MAX_VALUE;
						logger.debug(msg + "Missing Task Or Resource");
					} else {
						double InvestmentCost = Double.parseDouble(
								assignment.getResource().getResourceDataModel().getProperty("Cost (euro)"));
						// partialInvestmentCostTemp =
						// ((DemoPlanningGenerator.MAX_INVESTMENT_COST-InvestmentCost)/(DemoPlanningGenerator.MAX_INVESTMENT_COST-DemoPlanningGenerator.MIN_INVESTMENT_COST));

					}
					partialInvestmentCost += partialInvestmentCostTemp;

					logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId
							+ InvestmentCostold.CRITERION_NAME + "=" + partialInvestmentCost);
				}
				// partialInvestmentCost = partialInvestmentCost/path.length;
				investmentCostSum += partialInvestmentCost;
			}
		}
		investmentCostSum = investmentCostSum / (double) sr;
		logger.trace(msg + InvestmentCostold.CRITERION_NAME + "=" + investmentCostSum + ".");
		return investmentCostSum;
	}

	@Override
	public double getWeight() {
		return 1;
	}

	@Override
	public boolean isBenefit() {
		return true;
	}

	@Override
	public String getCriterionName() {
		return InvestmentCostold.CRITERION_NAME;
	}

}
