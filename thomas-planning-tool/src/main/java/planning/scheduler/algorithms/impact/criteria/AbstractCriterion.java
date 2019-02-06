package planning.scheduler.algorithms.impact.criteria;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.tree.TreeNode;

//import planning.scheduler.algorithms.impact.LayerNode;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public abstract class AbstractCriterion {
	public abstract double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow);

	public abstract double getWeight();

	public abstract boolean isBenefit();

	public abstract String getCriterionName();
}
