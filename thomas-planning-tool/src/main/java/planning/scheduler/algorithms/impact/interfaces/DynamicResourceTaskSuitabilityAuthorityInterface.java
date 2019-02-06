package planning.scheduler.algorithms.impact.interfaces;

import java.util.Calendar;

import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * Interface to be used for dynamically remove unwanted/infeasible
 * suitabilities. USAGE: // Make the vector with all the authorities
 * Vector<DynamicResourceTaskSuitabilityAuthority>
 * dynamicResourceTaskSuitabilityAuthorities = new
 * Vector<DynamicResourceTaskSuitabilityAuthority>(); // Add the implementors
 * dynamicResourceTaskSuitabilityAuthorities.add(new
 * DynamicResourceFetchingEndEffectorSuitabilityAuthority()); // Notify IMPACT
 * IMPACT.setDynamicResourceTaskSuitabilityAuthorities(dynamicResourceTaskSuitabilityAuthorities);
 */
public interface DynamicResourceTaskSuitabilityAuthorityInterface {
	public abstract boolean isSuitabilityValid(ResourceSimulator resource, TaskSimulator task, Calendar timeNow,
			PlanHelperInterface planHelperInterface);
}
