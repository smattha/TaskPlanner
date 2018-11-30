package planning.scheduler.algorithms.impact.criteria;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class DummyCriterion extends AbstractCriterion {

    @Override
    public double getValue(Vector<TreeNode[]> paths, PlanHelperInterface helper, Calendar timeNow) {
        return 0;
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
        return "Dummy Criterion";
    }

}
