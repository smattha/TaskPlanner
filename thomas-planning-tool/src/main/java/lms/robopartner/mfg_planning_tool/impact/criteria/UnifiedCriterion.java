package lms.robopartner.mfg_planning_tool.impact.criteria;

import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point3D;

import javax.swing.tree.TreeNode;

import org.slf4j.LoggerFactory;

import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * This criterion is used in order to call all others and calculate a final weighted factor for the plan. This calls the
 * below criteria:
 * Ergonomy, FloorSpace, InvestmentCost, Payload, SaturationLevel, TimeToComplete, Fatigue
 * 
 * @author Jason
 *
 */
public class UnifiedCriterion extends AbstractCriterion {

	private static org.slf4j.Logger	logger			= LoggerFactory.getLogger(UnifiedCriterion.class);
	private static final String		CRITERION_NAME	= "UNIFIED";
	private Map<String, Point3D>	resourcesAndPartsMapingForSR;

	/**
	 * Hide constructor
	 */
	@SuppressWarnings ("unused")
	private UnifiedCriterion() {
		super();
	}

	/**
	 * @param resourcesAndPartsMapingForSR
	 */
	public UnifiedCriterion(Map<String, Point3D> resourcesAndPartsMapingForSR) {
		super();
		this.resourcesAndPartsMapingForSR = resourcesAndPartsMapingForSR;
	}

	@Override
	public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
		// TODO Possibly here insert the call for Process Simulate simulation execution.

		String msg = ".getValue(): ";
		// Each path is a solution. There are SR paths provided each time.
		int sr = paths.size();
		double areaCostSum = 0;
		double partialAreaCost = 0;

		for ( int i = 0; i < sr; i++ ) {
			logger.trace(msg + "Sample " + (i + 1) + " of " + sr + " sequence :");
			TreeNode[] path = paths.get(i);

			// path is solution
			for ( int j = 0; j < path.length; j++ ) {
				LayerNode node = (LayerNode) path[j];
				for ( Assignment assignment : node.getNodeAssignments() ) {
					String taskId = assignment.getTask().getTaskId();
					String resourceId = assignment.getResource().getResourceId();

					// calculate
					boolean missingTask = !resourcesAndPartsMapingForSR.containsKey(taskId);
					boolean missingResource = !resourcesAndPartsMapingForSR.containsKey(resourceId);

					if ( missingTask ) {
						String msg2 = msg + "No task mapping found for task with id=" + taskId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}

					if ( missingResource ) {
						String msg2 = msg + "No resource mapping found for resource with id=" + resourceId;
						RuntimeException runtimeException = new RuntimeException(msg2);
						logger.error(msg2, runtimeException);
						logger.error(msg2);
						throw runtimeException;
					}

					double partialAreaCostTemp = 0.0;
					if ( missingTask || missingResource ) {
						// If a resource or task is missing, this solution is
						// totally bad.
						partialAreaCost = Double.MAX_VALUE;
						logger.debug(msg + "Missing Task Or Resource");
					}

					double[] Abstract = { new InvestmentCost(resourcesAndPartsMapingForSR).getValue(paths, helper, timeNow),
							new Payload().getValue(paths, helper, timeNow),
							new TimeToComplete(resourcesAndPartsMapingForSR).getValue(paths, helper, timeNow), };
					for ( double abstractDouble : Abstract )
						partialAreaCostTemp += abstractDouble;
					partialAreaCost += partialAreaCostTemp;

					logger.trace(msg + " taskId=" + taskId + " resourceId=" + resourceId + "- partial" + UnifiedCriterion.CRITERION_NAME + "="
							+ partialAreaCost);
				}
				areaCostSum += partialAreaCost;
			}
		}
		areaCostSum = areaCostSum / (double) sr;
		logger.trace(msg + UnifiedCriterion.CRITERION_NAME + "=" + areaCostSum + ".");
		return areaCostSum;
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
		return UnifiedCriterion.CRITERION_NAME;
	}

}
