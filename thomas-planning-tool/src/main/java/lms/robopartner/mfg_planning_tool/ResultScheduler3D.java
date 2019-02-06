package lms.robopartner.mfg_planning_tool;

import java.awt.Point;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;

public class ResultScheduler3D {

	public ResultScheduler3D() {
	}

	private Vector<String> results;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ResultScheduler3D.class);

	public Vector<String> resultScheduler(Vector<AssignmentDataModel> assignments,
			Map<String, Point> resourcesAndPartsMapingForSR) {

		results = new Vector<String>();

		HashMap<String, Vector<AssignmentDataModel>> timeOrderedAssignments = new HashMap<String, Vector<AssignmentDataModel>>();
		List<Date> allDispathesDates = new Vector<Date>();

		for (AssignmentDataModel assignment : assignments) {
			Date dateOfDispatch = assignment.getTimeOfDispatch().getTime();
			String dateOfDispatchStringRepresentation = "" + dateOfDispatch.getTime();
			if (!allDispathesDates.contains(dateOfDispatch)) {
				allDispathesDates.add(dateOfDispatch);
			}
			if (timeOrderedAssignments.get(dateOfDispatchStringRepresentation) == null) {
				Vector<AssignmentDataModel> timeAssignments = new Vector<AssignmentDataModel>();
				timeAssignments.add(assignment);
				timeOrderedAssignments.put(dateOfDispatchStringRepresentation, timeAssignments);
			} else {
				Vector<AssignmentDataModel> timeAssignments = timeOrderedAssignments
						.get(dateOfDispatchStringRepresentation);
				timeAssignments.add(assignment);
			}

		}
		String output;
		Collections.sort(allDispathesDates);
		for (Date date : allDispathesDates) {
			String dateOfDispatchStringRepresentation = "" + date.getTime();
			Vector<AssignmentDataModel> dateAssignments = timeOrderedAssignments
					.get(dateOfDispatchStringRepresentation);
			String msg2 = "->" + date.toString() + (" milisec(" + date.getTime() + ")");
			logger.trace(msg2);
			for (AssignmentDataModel assignment : dateAssignments) {
				output = "result@" + assignment.getTaskDataModel().getTaskName() + "@"
						+ assignment.getResourceDataModel().getResourceName() + "@"
						+ Long.toString(assignment.getDurationInMilliseconds()) + "@"
						+ resourcesAndPartsMapingForSR.get(assignment.getResourceDataModel().getResourceId()).toString()
						+ "@" + resourcesAndPartsMapingForSR.get(assignment.getTaskDataModel().getTaskId()).toString();
				results.add(output);
				logger.trace(output);

			}

		}

		return results;
	}
}