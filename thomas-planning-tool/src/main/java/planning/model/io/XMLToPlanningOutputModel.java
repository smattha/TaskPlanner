package planning.model.io;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import planning.model.AssignmentDataModel;
import planning.model.PlanningInputDataModel;
import planning.model.PlanningOutputDataModel;
import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class XMLToPlanningOutputModel {
	private Document xmlPlanningOutputDocument = null;
	private PlanningOutputDataModel outputModel = null;
	private PlanningInputDataModel inputModel = null;

	public XMLToPlanningOutputModel(Document xmlPlanningOutputDocument, PlanningInputDataModel inputModel) {
		this.xmlPlanningOutputDocument = xmlPlanningOutputDocument;
		this.inputModel = inputModel;
	}

	public PlanningOutputDataModel getPlanningOutputDataModel() {
		if (outputModel == null) {
			outputModel = new PlanningOutputDataModel();
			outputModel.setPlanningOutputDataModelId(getPlanningOutputDataModelId());
			outputModel.setAssignments(getAssignments());
		}
		return outputModel;
	}

	private Vector<AssignmentDataModel> getAssignments() {
		Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();
		NodeList assignmentsNodes = xmlPlanningOutputDocument.getElementsByTagName("ASSIGNMENT");

		for (int i = 0; i < assignmentsNodes.getLength(); i++) {
			NodeList assignmentDetails = assignmentsNodes.item(i).getChildNodes();
			TaskDataModel task = null;
			ResourceDataModel resource = null;
			Calendar timeOfDispatch = null;
			long durationInMilliseconds = 0;
			HashMap<String, String> assignmentProperties = new HashMap<String, String>();
			for (int j = 0; j < assignmentDetails.getLength(); j++) {
				if (!assignmentDetails.item(j).getNodeName().equals("#text")) {
					if (assignmentDetails.item(j).getNodeName().equals("TASK")) {
						NamedNodeMap attributes = assignmentDetails.item(j).getAttributes();
						task = inputModel.getTaskDataModel(attributes.getNamedItem("id").getNodeValue());
					} else if (assignmentDetails.item(j).getNodeName().equals("RESOURCE")) {
						NamedNodeMap attributes = assignmentDetails.item(j).getAttributes();
						resource = inputModel.getResourceDataModel(attributes.getNamedItem("id").getNodeValue());
					} else if (assignmentDetails.item(j).getNodeName().equals("TIME_OF_DISPATCH")) {
						timeOfDispatch = XMLUtil
								.getCalendarDateFromXMLNodeList(assignmentDetails.item(j).getChildNodes());
					} else if (assignmentDetails.item(j).getNodeName().equals("PROPERTIES")) {
						NodeList propertiesDetailsNodes = assignmentDetails.item(j).getChildNodes();
						// Looping for the resource availability nodes (up to now 10/2/20004 only non
						// working periods are defined and no working periods)
						for (int k = 0; k < propertiesDetailsNodes.getLength(); k++) {
							if (!propertiesDetailsNodes.item(k).getNodeName().equals("#text")) {
								if (propertiesDetailsNodes.item(k).getNodeName().equals("PROPERTY")) {
									String propertyName = null;
									String propertyValue = null;
									NodeList propertyDetailsNodeList = propertiesDetailsNodes.item(k).getChildNodes();
									// Looping among the periods (non_working_periods element child nodes)
									for (int l = 0; l < propertyDetailsNodeList.getLength(); l++) {
										if (!propertyDetailsNodeList.item(l).getNodeName().equals("#text")) {
											if (propertyDetailsNodeList.item(l).getNodeName().equals("NAME")) {
												propertyName = propertyDetailsNodeList.item(l).getLastChild()
														.getNodeValue();
											} else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
												propertyValue = propertyDetailsNodeList.item(l).getLastChild()
														.getNodeValue();
											}
										}
									}
									assignmentProperties.put(propertyName, propertyValue);
								}
							}
						}
					} else if (assignmentDetails.item(j).getNodeName().equals("DURATION_IN_MILLISECONDS")) {
						String stringValue = assignmentDetails.item(j).getLastChild().getNodeValue();
						try {
							durationInMilliseconds = Long.parseLong(stringValue);
						} catch (NumberFormatException nex) {
							// Do nothing just keep the default value (zero)
						}
					} else if (assignmentDetails.item(j).getNodeName().equals("START_DATE")) {
					} else if (assignmentDetails.item(j).getNodeName().equals("END_DATE")) {
					}
				}
			}
			try {
				AssignmentDataModel assignment = null;

				if ("true".equals(assignmentsNodes.item(i).getAttributes().getNamedItem("locked").getNodeValue())) {
					assignment = new AssignmentDataModel(task, resource, timeOfDispatch, durationInMilliseconds, true,
							assignmentProperties);
				} else {
					assignment = new AssignmentDataModel(task, resource, timeOfDispatch, durationInMilliseconds, false,
							assignmentProperties);
				}

				assignments.add(assignment);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return assignments;
	}

	public boolean isInitilized() {
		if ((xmlPlanningOutputDocument != null) && (outputModel != null) && (inputModel != null)) {
			return true;
		}
		// else
		{
			return false;
		}
	}

	private String getPlanningOutputDataModelId() {
		Node planOutputNode = xmlPlanningOutputDocument.getDocumentElement();
		String id = planOutputNode.getAttributes().getNamedItem("id").getNodeValue();
		return id;
	}
}