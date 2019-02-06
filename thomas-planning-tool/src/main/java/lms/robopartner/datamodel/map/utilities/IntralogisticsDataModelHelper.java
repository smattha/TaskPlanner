/**
 * 
 */
package lms.robopartner.datamodel.map.utilities;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

/**
 * @author Spyros Helper class to provide conversion functionality for
 *         INTRALOGISTICS
 */
public abstract class IntralogisticsDataModelHelper {
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(IntralogisticsDataModelHelper.class);

	/**
	 * @param resource
	 * @return
	 */
	public final static boolean isResourceIMAU(ResourceSimulator resource) {
		return IntralogisticsDataModelHelper.isResourceIMAU(resource.getResourceDataModel());
	}

	/**
	 * @param resource
	 * @return
	 */
	public final static boolean isResourceIMAU(ResourceDataModel resource) {
		boolean virtualIMAUId = resource.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
				.equalsIgnoreCase(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_IMAU);
		return virtualIMAUId;
	}

	/**
	 * @param task
	 * @return
	 */
	public final static Integer getVirtualIMAUAssignment(TaskSimulator task) {
		return IntralogisticsDataModelHelper.getVirtualIMAUAssignment(task.getTaskDataModel());
	}

	/**
	 * @param task
	 * @return
	 */
	public final static Integer getVirtualIMAUAssignment(TaskDataModel task) {
		String msg = ".getVirtualIMAUAssignment(): ";
		Integer virtualIMAUId = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.VIRTUAL_IMAU_PROPERTY_NAME));
		logger.trace(msg + "TaskId=" + task.getTaskId() + MapToResourcesAndTasks.VIRTUAL_IMAU_PROPERTY_NAME + "="
				+ virtualIMAUId);
		return virtualIMAUId;
	}

	/**
	 * @param task
	 * @return the difference in the current number of boxes (that is the boxes that
	 *         the IMAU is carrying) after performing the task. This is +1 if it is
	 *         loading a box and -1 if it is unloading a box.
	 */
	public final static Integer getDifferenceInCurrentNumberOfBoxesForTask(TaskDataModel task) {
		Integer differenceInCurrentNumberOfBoxesForTask = 0;
		if (task.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
				.equalsIgnoreCase(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_LOADING)) {
			differenceInCurrentNumberOfBoxesForTask = +1;
		} else if (task.getProperty(MapToResourcesAndTasks.TYPE_PROPERTY_NAME)
				.equalsIgnoreCase(MapToResourcesAndTasks.TYPE_PROPERTY_VALUE_UNLOADING)) {
			differenceInCurrentNumberOfBoxesForTask = -1;
		}

		return differenceInCurrentNumberOfBoxesForTask;
	}

	/**
	 * Get the value of MapToResourcesAndTasks.IMAU_MAX_NUMBER_OF_BOXES
	 * 
	 * @param resource
	 * @return
	 */
	public final static Integer getIMAUMaxNumberOfBoxes(ResourceDataModel resource) {
		Integer imauMaxNumberOfBoxes = (int) Double
				.parseDouble(resource.getProperty(MapToResourcesAndTasks.IMAU_MAX_NUMBER_OF_BOXES));
		return imauMaxNumberOfBoxes;
	}

	/**
	 * Get the value of MapToResourcesAndTasks.IMAU_INITIAL_NUMBER_OF_BOXES
	 * 
	 * @param resource
	 * @return
	 */
	public final static Integer getIMAUInitialNumberOfBoxes(ResourceDataModel resource) {
		Integer imauInitialNumberOfBoxes = (int) Double
				.parseDouble(resource.getProperty(MapToResourcesAndTasks.IMAU_INITIAL_NUMBER_OF_BOXES));
		return imauInitialNumberOfBoxes;
	}

}
