package planning.model.io;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import planning.model.DockingStationDataModel;
import planning.model.InventoryDataModel;
import planning.model.JobDataModel;
import planning.model.MobileResourceDataModel;
import planning.model.MobileResourceTypeDataModel;
import planning.model.PlanningInputDataModel;
import planning.model.ResourceAvailabilityDataModel;
import planning.model.ResourceDataModel;
import planning.model.SetUpMatrixDataModel;
import planning.model.TaskDataModel;
import planning.model.TaskPrecedenceConstraintDataModel;
import planning.model.TaskSuitableResourceDataModel;
import planning.model.ToolDataModel;
import planning.model.ToolTypeDataModel;
import planning.model.WorkcenterDataModel;
import planning.model.util.TimePeriodDataModel;

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

public class XMLToPlanningInputModel {
    private Document xmlPlanningInputDocument = null;
    private PlanningInputDataModel inputModel = null;

    public XMLToPlanningInputModel(Document xmlPlanningInputDocument) {
        this.xmlPlanningInputDocument = xmlPlanningInputDocument;
    }

    public PlanningInputDataModel getPlanningInputDataModel() {
        if (inputModel == null) {
            // NOTE: The order is the important
            inputModel = new PlanningInputDataModel();
            inputModel.setPlanningInputDataModelId(getPlanningInputDataModelId());
            inputModel.setPlanStartDate(getPlanningInputDataModelStartDate());
            inputModel.setPlanEndDate(getPlanningInputDataModelEndDate());
            inputModel.setContinueAssignmentsAfterPlanEndDate(getContinueAssignmentsAfterPlanEndDate());
            inputModel.setToolTypes(getToolTypes());
            inputModel.setMobileResourceTypes(getMobileResourceTypes());
            inputModel.setTools(getTools());
            inputModel.setMobileResources(getMobileResources());
            inputModel.setResourceDataModelVector(getResources());
            inputModel.setWorkcenterDataModelVector(getWorkcenters());
            inputModel.setTaskDataModelVector(getTasks());
            inputModel.setJobDataModelVector(getJobs());
            inputModel.setSetUpMatrices(getAllSetUpMatrices());
            inputModel.setTaskPrecedenceConstraintsDataModelVector(getPrecedenceConstraints());
            inputModel.setTaskSuitableResourceDataModelVector(getTasksSuitableResources());
            // Done with the model data, perform some initialisation procedure in order to calculate task due dates
            inputModel.calculateTaskArrivalAndDueDates();
        }
        return inputModel;
    }

    private Vector<MobileResourceDataModel> getMobileResources() {
        Vector<MobileResourceDataModel> mobileResources = new Vector<MobileResourceDataModel>();
        NodeList mobileResourceNodes = xmlPlanningInputDocument.getElementsByTagName("MOBILE_RESOURCE");
        // Looping for all resources
        for (int i = 0; i < mobileResourceNodes.getLength(); i++) {
            String mobileResourceId = new String();
            String mobileResourceName = new String();
            String mobileResourceDescription = new String();
            SetUpMatrixDataModel mobileResourceSetUpMatrixDataModel = null;
            HashMap<String, String> mobileResourceProperties = new HashMap<String, String>();
            Vector<TimePeriodDataModel> mobileResourceNonWorkingPeriods = new Vector<TimePeriodDataModel>();
            Vector<TimePeriodDataModel> mobileResourceForcedWorkingPeriods = new Vector<TimePeriodDataModel>();
            ResourceAvailabilityDataModel mobileResourceAvailability = null;
            InventoryDataModel mobileResourceEndEffectorDataModel = null;
            MobileResourceTypeDataModel mobileResourceType = null;

            Node resourceNode = mobileResourceNodes.item(i);
            NamedNodeMap attributes = resourceNode.getAttributes();
            mobileResourceId = attributes.getNamedItem("id").getNodeValue();
            // System.out.println("RESOURCE ID : "+resourceId);
            NodeList resourceDetailsNodes = resourceNode.getChildNodes();
            // Reading resource's children nodes and looping among them
            for (int j = 0; j < resourceDetailsNodes.getLength(); j++) {
                if (!resourceDetailsNodes.item(j).getNodeName().equals("#text")) {
                    if (resourceDetailsNodes.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            mobileResourceDescription = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("NAME")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            mobileResourceName = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("RESOURCE_AVAILABILITY")) {
                        NodeList resourceAvailabilityDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
                        for (int k = 0; k < resourceAvailabilityDetailsNodes.getLength(); k++) {
                            if (!resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("#text")) {
                                if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("NON_WORKING_PERIODS")) {
                                    NodeList nonWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < nonWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!nonWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (nonWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel nonWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList nonWorkingPeriodDetailsNodeList = nonWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < nonWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = nonWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                mobileResourceNonWorkingPeriods.add(nonWorkingPeriod);
                                            }
                                        }
                                    }
                                } else if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("FORCED_WORKING_PERIODS")) {
                                    NodeList forcedWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < forcedWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel forcedWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList forcedWorkingPeriodDetailsNodeList = forcedWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < forcedWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                mobileResourceForcedWorkingPeriods.add(forcedWorkingPeriod);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("SET_UP_MATRIX_REFERENCE")) {
                        String setUpMatrixReference = null;
                        NamedNodeMap referenceAttributes = resourceDetailsNodes.item(j).getAttributes();
                        if (referenceAttributes.getNamedItem("refid") != null) {
                            setUpMatrixReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                        }
                        mobileResourceSetUpMatrixDataModel = inputModel.getSetUpMatrixDataModel(setUpMatrixReference);
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("PROPERTIES")) {
                        NodeList propertiesDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
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
                                                propertyName = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            } else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
                                                propertyValue = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            }
                                        }
                                    }
                                    mobileResourceProperties.put(propertyName, propertyValue);
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("END_EFFECTORS")) {
                        mobileResourceEndEffectorDataModel = getInventoryDataModel(resourceDetailsNodes.item(j));
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("MOBILE_RESOURCE_TYPE_REFERENCE")) {
                        String mobileResourceTypeReference = null;
                        NamedNodeMap referenceAttributes = resourceDetailsNodes.item(j).getAttributes();
                        if (referenceAttributes.getNamedItem("refid") != null) {
                            mobileResourceTypeReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                        }
                        mobileResourceType = inputModel.getMobileResourceTypeDataModel(mobileResourceTypeReference);
                    }
                }
            }
            mobileResourceAvailability = new ResourceAvailabilityDataModel(mobileResourceNonWorkingPeriods, mobileResourceForcedWorkingPeriods);

            MobileResourceDataModel mobileResource = new MobileResourceDataModel(mobileResourceId, mobileResourceName, mobileResourceDescription, mobileResourceAvailability, mobileResourceSetUpMatrixDataModel, mobileResourceEndEffectorDataModel, mobileResourceType, mobileResourceProperties);
            mobileResources.add(mobileResource);
        }
        return mobileResources;
    }

    private Vector<MobileResourceTypeDataModel> getMobileResourceTypes() {
        Vector<MobileResourceTypeDataModel> mobileResourceTypes = new Vector<MobileResourceTypeDataModel>();
        NodeList mobileResourceTypeNodes = xmlPlanningInputDocument.getElementsByTagName("MOBILE_RESOURCE_TYPE");
        for (int i = 0; i < mobileResourceTypeNodes.getLength(); i++) {
            String mobileResourceTypeId = new String();
            String mobileResourceTypeType = new String();

            Node mobileResourceTypeNode = mobileResourceTypeNodes.item(i);
            NamedNodeMap attributes = mobileResourceTypeNode.getAttributes();
            mobileResourceTypeId = attributes.getNamedItem("id").getNodeValue();

            NodeList mobileResourceTypeDetailsNodes = mobileResourceTypeNode.getChildNodes();

            for (int j = 0; j < mobileResourceTypeDetailsNodes.getLength(); j++) {
                if (!mobileResourceTypeDetailsNodes.item(j).getNodeName().equals("#text")) {
                    if (mobileResourceTypeDetailsNodes.item(j).getNodeName().equals("TYPE_NAME")) {
                        if (mobileResourceTypeDetailsNodes.item(j).getLastChild() != null)
                            mobileResourceTypeType = mobileResourceTypeDetailsNodes.item(j).getLastChild().getNodeValue();
                    }
                }
            }
            MobileResourceTypeDataModel mobileResourceType = new MobileResourceTypeDataModel(mobileResourceTypeId, mobileResourceTypeType);
            mobileResourceTypes.add(mobileResourceType);
        }
        return mobileResourceTypes;
    }

    private Vector<ToolDataModel> getTools() {
        Vector<ToolDataModel> tools = new Vector<ToolDataModel>();
        NodeList toolNodes = xmlPlanningInputDocument.getElementsByTagName("TOOL");
        // Looping for all resources
        for (int i = 0; i < toolNodes.getLength(); i++) {
            String toolId = new String();
            String toolName = new String();
            String toolDescription = new String();
            SetUpMatrixDataModel toolSetUpMatrixDataModel = null;
            HashMap<String, String> toolProperties = new HashMap<String, String>();
            Vector<TimePeriodDataModel> toolNonWorkingPeriods = new Vector<TimePeriodDataModel>();
            Vector<TimePeriodDataModel> toolForcedWorkingPeriods = new Vector<TimePeriodDataModel>();
            ResourceAvailabilityDataModel toolAvailability = null;
            ToolTypeDataModel toolType = null;

            Node toolNode = toolNodes.item(i);
            NamedNodeMap attributes = toolNode.getAttributes();
            toolId = attributes.getNamedItem("id").getNodeValue();
            // System.out.println("RESOURCE ID : "+resourceId);
            NodeList resourceDetailsNodes = toolNode.getChildNodes();
            // Reading resource's children nodes and looping among them
            for (int j = 0; j < resourceDetailsNodes.getLength(); j++) {
                if (!resourceDetailsNodes.item(j).getNodeName().equals("#text")) {
                    if (resourceDetailsNodes.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            toolDescription = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("NAME")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            toolName = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("RESOURCE_AVAILABILITY")) {
                        NodeList resourceAvailabilityDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
                        for (int k = 0; k < resourceAvailabilityDetailsNodes.getLength(); k++) {
                            if (!resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("#text")) {
                                if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("NON_WORKING_PERIODS")) {
                                    NodeList nonWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < nonWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!nonWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (nonWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel nonWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList nonWorkingPeriodDetailsNodeList = nonWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < nonWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = nonWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                toolNonWorkingPeriods.add(nonWorkingPeriod);
                                            }
                                        }
                                    }
                                } else if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("FORCED_WORKING_PERIODS")) {
                                    NodeList forcedWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < forcedWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel forcedWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList forcedWorkingPeriodDetailsNodeList = forcedWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < forcedWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                toolForcedWorkingPeriods.add(forcedWorkingPeriod);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("SET_UP_MATRIX_REFERENCE")) {
                        String setUpMatrixReference = null;
                        NamedNodeMap referenceAttributes = resourceDetailsNodes.item(j).getAttributes();
                        if (referenceAttributes.getNamedItem("refid") != null) {
                            setUpMatrixReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                        }
                        toolSetUpMatrixDataModel = inputModel.getSetUpMatrixDataModel(setUpMatrixReference);
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("PROPERTIES")) {
                        NodeList propertiesDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
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
                                                propertyName = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            } else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
                                                propertyValue = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            }
                                        }
                                    }
                                    toolProperties.put(propertyName, propertyValue);
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("TOOL_TYPE_REFERENCE")) {
                        String toolTypeReference = null;
                        NamedNodeMap referenceAttributes = resourceDetailsNodes.item(j).getAttributes();
                        if (referenceAttributes.getNamedItem("refid") != null) {
                            toolTypeReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                        }
                        toolType = inputModel.getToolTypeDataModel(toolTypeReference);
                    }
                }
            }
            toolAvailability = new ResourceAvailabilityDataModel(toolNonWorkingPeriods, toolForcedWorkingPeriods);

            ToolDataModel mobileResource = new ToolDataModel(toolId, toolName, toolDescription, toolAvailability, toolSetUpMatrixDataModel, toolType, toolProperties);
            tools.add(mobileResource);
        }
        return tools;
    }

    private Vector<ToolTypeDataModel> getToolTypes() {
        Vector<ToolTypeDataModel> toolTypes = new Vector<ToolTypeDataModel>();
        NodeList toolTypeNodes = xmlPlanningInputDocument.getElementsByTagName("TOOL_TYPE");
        for (int i = 0; i < toolTypeNodes.getLength(); i++) {
            String toolTypeId = new String();
            String toolTypeType = new String();

            Node toolTypeNode = toolTypeNodes.item(i);
            NamedNodeMap attributes = toolTypeNode.getAttributes();
            toolTypeId = attributes.getNamedItem("id").getNodeValue();

            NodeList toolTypeDetailsNodes = toolTypeNode.getChildNodes();

            for (int j = 0; j < toolTypeDetailsNodes.getLength(); j++) {
                if (!toolTypeDetailsNodes.item(j).getNodeName().equals("#text")) {
                    if (toolTypeDetailsNodes.item(j).getNodeName().equals("TYPE_NAME")) {
                        if (toolTypeDetailsNodes.item(j).getLastChild() != null)
                            toolTypeType = toolTypeDetailsNodes.item(j).getLastChild().getNodeValue();
                    }
                }
            }
            ToolTypeDataModel mobileResourceType = new ToolTypeDataModel(toolTypeId, toolTypeType);
            toolTypes.add(mobileResourceType);
        }
        return toolTypes;
    }

    private Vector<TaskDataModel> getTasks() {
        Vector<TaskDataModel> tasks = new Vector<TaskDataModel>();
        NodeList tasksNodes = xmlPlanningInputDocument.getElementsByTagName("TASK");

        for (int i = 0; i < tasksNodes.getLength(); i++) {
            String taskName = null;
            String description = null;
            HashMap<String, String> properties = new HashMap<String, String>();

            Node taskNode = tasksNodes.item(i);
            NamedNodeMap attributes = taskNode.getAttributes();
            String taskId = attributes.getNamedItem("id").getNodeValue();
            NodeList taskDetailsNodeList = taskNode.getChildNodes();
            for (int j = 0; j < taskDetailsNodeList.getLength(); j++) {
                if (!taskDetailsNodeList.item(j).getNodeName().equals("#text")) {
                    if (taskDetailsNodeList.item(j).getNodeName().equals("NAME")) {
                        if (taskDetailsNodeList.item(j).getLastChild() != null)
                            taskName = taskDetailsNodeList.item(j).getLastChild().getNodeValue();
                    } else if (taskDetailsNodeList.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (taskDetailsNodeList.item(j).getLastChild() != null)
                            description = taskDetailsNodeList.item(j).getLastChild().getNodeValue();
                    } else if (taskDetailsNodeList.item(j).getNodeName().equals("PROPERTIES")) {
                        NodeList propertiesDetailsNodes = taskDetailsNodeList.item(j).getChildNodes();
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
                                                propertyName = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            } else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
                                                propertyValue = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            }
                                        }
                                    }
                                    properties.put(propertyName, propertyValue);
                                }
                            }
                        }
                    }
                }
            }

            TaskDataModel task = new TaskDataModel(taskId, taskName, description, properties);
            tasks.add(task);
        }
        return tasks;
    }

    private Vector<TaskPrecedenceConstraintDataModel> getPrecedenceConstraints() {
        Vector<TaskPrecedenceConstraintDataModel> constraints = new Vector<TaskPrecedenceConstraintDataModel>();
        NodeList constraintsNodes = xmlPlanningInputDocument.getElementsByTagName("TASK_PRECEDENCE_CONSTRAINT");

        for (int i = 0; i < constraintsNodes.getLength(); i++) {
            NodeList constraintDetails = constraintsNodes.item(i).getChildNodes();
            TaskDataModel preconditiontask = null;
            TaskDataModel postconditiontask = null;
            Boolean isConstraintTypeNextTaskInChain = Boolean.FALSE;
            Boolean isTasksPreemptionAllowed = Boolean.FALSE;
            for (int j = 0; j < constraintDetails.getLength(); j++) {
                if (!constraintDetails.item(j).getNodeName().equals("#text")) {
                    if (constraintDetails.item(j).getNodeName().equals("PRECONDITION_TASK_REFERENCE")) {
                        NamedNodeMap attributes = constraintDetails.item(j).getAttributes();
                        preconditiontask = inputModel.getTaskDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (constraintDetails.item(j).getNodeName().equals("POSTCONDITION_TASK_REFERENCE")) {
                        NamedNodeMap attributes = constraintDetails.item(j).getAttributes();
                        postconditiontask = inputModel.getTaskDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (constraintDetails.item(j).getNodeName().equals("NEXT_TASK_IN_CHAIN")) {
                        if (constraintDetails.item(j).getLastChild() != null)
                            isConstraintTypeNextTaskInChain = Boolean.parseBoolean(constraintDetails.item(j).getLastChild().getNodeValue());
                    } else if (constraintDetails.item(j).getNodeName().equals("RESOURCE_UNAVAILABLE_UNTIL_NEXT_TASK")) {
                        if (constraintDetails.item(j).getLastChild() != null)
                            isTasksPreemptionAllowed = Boolean.parseBoolean(constraintDetails.item(j).getLastChild().getNodeValue());
                    }
                }
            }
            TaskPrecedenceConstraintDataModel constraint = new TaskPrecedenceConstraintDataModel(preconditiontask, postconditiontask, isConstraintTypeNextTaskInChain, isTasksPreemptionAllowed);
            constraints.add(constraint);
        }
        return constraints;
    }

    private Vector<TaskSuitableResourceDataModel> getTasksSuitableResources() {
        Vector<TaskSuitableResourceDataModel> tasksSuitableResources = new Vector<TaskSuitableResourceDataModel>();
        NodeList taskSuitableResourcesNodes = xmlPlanningInputDocument.getElementsByTagName("TASK_SUITABLE_RESOURCE");

        for (int i = 0; i < taskSuitableResourcesNodes.getLength(); i++) {
            NodeList taskSuitableResourceDetails = taskSuitableResourcesNodes.item(i).getChildNodes();
            TaskDataModel task = null;
            ResourceDataModel resource = null;
            String setUpCode = "";
            HashMap<String, String> properties = new HashMap<String, String>();
            double operationTime = 0;
            for (int j = 0; j < taskSuitableResourceDetails.getLength(); j++) {
                if (!taskSuitableResourceDetails.item(j).getNodeName().equals("#text")) {
                    if (taskSuitableResourceDetails.item(j).getNodeName().equals("TASK_REFERENCE")) {
                        NamedNodeMap attributes = taskSuitableResourceDetails.item(j).getAttributes();
                        task = inputModel.getTaskDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("RESOURCE_REFERENCE")) {
                        NamedNodeMap attributes = taskSuitableResourceDetails.item(j).getAttributes();
                        // Search for resource
                        resource = inputModel.getResourceDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("TOOL_REFERENCE")) {
                        NamedNodeMap attributes = taskSuitableResourceDetails.item(j).getAttributes();
                        // Search for tools
                        resource = inputModel.getToolDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("MOBILE_RESOURCE_REFERENCE")) {
                        NamedNodeMap attributes = taskSuitableResourceDetails.item(j).getAttributes();
                        // Search for mobile resource
                        resource = inputModel.getMobileResourceDataModel(attributes.getNamedItem("refid").getNodeValue());
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("OPERATION_TIME_PER_BATCH_IN_SECONDS")) {
                        String stringValue = taskSuitableResourceDetails.item(j).getLastChild().getNodeValue();
                        try {
                            operationTime = Double.parseDouble(stringValue.trim());
                        } catch (NumberFormatException nex) {
                            // Do nothing just keep the default value (zero)
                        }
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("SET_UP_CODE")) {
                        if (taskSuitableResourceDetails.item(j).getLastChild() != null)
                            setUpCode = taskSuitableResourceDetails.item(j).getLastChild().getNodeValue();
                    } else if (taskSuitableResourceDetails.item(j).getNodeName().equals("PROPERTIES")) {
                        NodeList propertiesDetailsNodes = taskSuitableResourceDetails.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
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
                                                propertyName = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            } else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
                                                propertyValue = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            }
                                        }
                                    }
                                    properties.put(propertyName, propertyValue);
                                }
                            }
                        }
                    }
                }
            }
            TaskSuitableResourceDataModel taskSuitableResource = new TaskSuitableResourceDataModel(task, resource, (long) (operationTime * 1000d), setUpCode, properties);
            tasksSuitableResources.add(taskSuitableResource);
        }
        return tasksSuitableResources;
    }

    private Vector<WorkcenterDataModel> getWorkcenters() {
        Vector<WorkcenterDataModel> workcenters = new Vector<WorkcenterDataModel>();
        NodeList workcenterNodes = xmlPlanningInputDocument.getElementsByTagName("WORKCENTER");

        for (int i = 0; i < workcenterNodes.getLength(); i++) {
            String workcenterId = null;
            Vector<ResourceDataModel> resources = new Vector<ResourceDataModel>();
            String description = null;
            String name = "Unspecified";
            String algorithm = null;
            InventoryDataModel inventoryDataModel = null;
            Vector<DockingStationDataModel> dockingStationDataModels = null;

            Node workcenterNode = workcenterNodes.item(i);
            workcenterId = workcenterNode.getAttributes().getNamedItem("id").getNodeValue();

            NodeList workcenterDetails = workcenterNodes.item(i).getChildNodes();
            for (int j = 0; j < workcenterDetails.getLength(); j++) {
                if (!workcenterDetails.item(j).getNodeName().equals("#text")) {
                    if (workcenterDetails.item(j).getNodeName().equals("WORKCENTER_RESOURCE_REFERENCE")) {
                        NamedNodeMap attributes = workcenterDetails.item(j).getAttributes();
                        ResourceDataModel resource = inputModel.getResourceDataModel(attributes.getNamedItem("refid").getNodeValue());
                        resources.add(resource);
                    } else if (workcenterDetails.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (workcenterDetails.item(j).getLastChild() != null)
                            description = workcenterDetails.item(j).getLastChild().getNodeValue();
                    } else if (workcenterDetails.item(j).getNodeName().equals("NAME")) {
                        if (workcenterDetails.item(j).getLastChild() != null)
                            name = workcenterDetails.item(j).getLastChild().getNodeValue();
                    } else if (workcenterDetails.item(j).getNodeName().equals("ALGORITHM")) {
                        if (workcenterDetails.item(j).getLastChild() != null)
                            algorithm = workcenterDetails.item(j).getLastChild().getNodeValue();
                    } else if (workcenterDetails.item(j).getNodeName().equals("TOOL_REPOSITORY")) {
                        inventoryDataModel = getInventoryDataModel(workcenterDetails.item(j));
                    } else if (workcenterDetails.item(j).getNodeName().equals("DOCKING_STATIONS")) {
                        dockingStationDataModels = new Vector<DockingStationDataModel>();
                        NodeList dockingStationsDetail = workcenterDetails.item(j).getChildNodes();
                        for (int k = 0; k < dockingStationsDetail.getLength(); k++) {
                            Node dockingStationNode = dockingStationsDetail.item(k);
                            if (!dockingStationNode.getNodeName().equals("#text")) {
                                if (dockingStationNode.getNodeName().equals("DOCKING_STATION")) {
                                    dockingStationDataModels.add(getDockingStationDataModel(dockingStationNode));
                                }
                            }
                        }
                    }
                }
            }
            WorkcenterDataModel workcenter = new WorkcenterDataModel(workcenterId, name, description, algorithm, resources, inventoryDataModel, dockingStationDataModels);
            workcenters.add(workcenter);
        }
        return workcenters;
    }

    private DockingStationDataModel getDockingStationDataModel(Node dockingStationNode) {
        Vector<MobileResourceTypeDataModel> supportedTypes = new Vector<MobileResourceTypeDataModel>();
        MobileResourceDataModel currentLoad = null;

        NodeList dockingStationDetails = dockingStationNode.getChildNodes();
        for (int i = 0; i < dockingStationDetails.getLength(); i++) {
            Node dockingStationDetailNode = dockingStationDetails.item(i);
            if (!dockingStationDetailNode.getNodeName().equals("#text")) {
                if (dockingStationDetailNode.getNodeName().equals("SUPPORTED_MOBILE_RESOURCES_TYPES")) {
                    NodeList supportedMobileResourceTypes = dockingStationDetailNode.getChildNodes();
                    for (int j = 0; j < supportedMobileResourceTypes.getLength(); j++) {
                        Node supportedMobileResourceType = supportedMobileResourceTypes.item(j);
                        if (!supportedMobileResourceType.getNodeName().equals("#text")) {
                            if (supportedMobileResourceType.getNodeName().equals("MOBILE_RESOURCE_TYPE_REFERENCE")) {
                                String mobileResourceTypeReference = null;
                                NamedNodeMap referenceAttributes = supportedMobileResourceType.getAttributes();
                                if (referenceAttributes.getNamedItem("refid") != null) {
                                    mobileResourceTypeReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                                }
                                MobileResourceTypeDataModel mobileResourceTypeDataModel = inputModel.getMobileResourceTypeDataModel(mobileResourceTypeReference);
                                if (mobileResourceTypeDataModel != null) {
                                    supportedTypes.add(mobileResourceTypeDataModel);
                                }
                            }
                        }
                    }
                } else if (dockingStationDetailNode.getNodeName().equals("CURRENT_LOAD")) {
                    NodeList currentLoadNodeList = dockingStationDetailNode.getChildNodes();
                    for (int k = 0; k < currentLoadNodeList.getLength(); k++) {
                        Node currentLoadDetailNode = currentLoadNodeList.item(k);
                        if (!currentLoadDetailNode.getNodeName().equals("#text")) {
                            if (currentLoadDetailNode.getNodeName().equals("MOBILE_RESOURCE_REFERENCE")) {
                                NamedNodeMap attributes = currentLoadDetailNode.getAttributes();
                                MobileResourceDataModel mobileResource = inputModel.getMobileResourceDataModel(attributes.getNamedItem("refid").getNodeValue());
                                if (mobileResource != null) {
                                    currentLoad = mobileResource;
                                }
                            }
                        }
                    }
                }
            }
        }

        return new DockingStationDataModel(supportedTypes, currentLoad);
    }

    private InventoryDataModel getInventoryDataModel(Node inventory) {
        HashMap<ToolTypeDataModel, Integer> maximumToolTypesCapacities = new HashMap<ToolTypeDataModel, Integer>();
        Vector<ToolDataModel> currentLoad = new Vector<ToolDataModel>();
        NodeList inventoryDetails = inventory.getChildNodes();
        for (int i = 0; i < inventoryDetails.getLength(); i++) {
            Node inventoryDetailNode = inventoryDetails.item(i);
            // NamedNodeMap inventoryDetailNodeAttributes = inventoryDetailNodeName.getAttributes();
            if (!inventoryDetailNode.getNodeName().equals("#text")) {
                if (inventoryDetailNode.getNodeName().equals("SUPPORTED_TOOL_TYPES")) {
                    NodeList supportedToolTypes = inventoryDetailNode.getChildNodes();
                    for (int j = 0; j < supportedToolTypes.getLength(); j++) {
                        Node supportedToolType = supportedToolTypes.item(j);
                        if (!supportedToolType.getNodeName().equals("#text")) {
                            if (supportedToolType.getNodeName().equals("SUPPORTED_TOOL_TYPE")) {
                                ToolTypeDataModel toolTypeDataModel = null;
                                Integer maximumCapacity = null;
                                NodeList supportedToolTypeDetails = supportedToolType.getChildNodes();
                                for (int k = 0; k < supportedToolTypeDetails.getLength(); k++) {
                                    Node supportedToolTypeDetailNode = supportedToolTypeDetails.item(k);
                                    if (!supportedToolTypeDetailNode.getNodeName().equals("#text")) {
                                        if (supportedToolTypeDetailNode.getNodeName().equals("TOOL_TYPE_REFERENCE")) {
                                            String toolTypeReference = null;
                                            NamedNodeMap referenceAttributes = supportedToolTypeDetailNode.getAttributes();
                                            if (referenceAttributes.getNamedItem("refid") != null) {
                                                toolTypeReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                                            }
                                            toolTypeDataModel = inputModel.getToolTypeDataModel(toolTypeReference);
                                        } else if (supportedToolTypeDetailNode.getNodeName().equals("MAXIMUM_CAPACITY")) {
                                            if (supportedToolTypeDetailNode.getLastChild() != null) {
                                                String capacity = supportedToolTypeDetailNode.getLastChild().getNodeValue();
                                                maximumCapacity = Integer.parseInt(capacity);
                                            }
                                        }
                                    }
                                }

                                if (toolTypeDataModel != null && maximumCapacity != null) {
                                    maximumToolTypesCapacities.put(toolTypeDataModel, maximumCapacity);
                                }
                            }
                        }
                    }
                } else if (inventoryDetailNode.getNodeName().equals("CURRENT_LOAD")) {
                    NodeList currentLoadNodeList = inventoryDetailNode.getChildNodes();
                    for (int k = 0; k < currentLoadNodeList.getLength(); k++) {
                        Node currentLoadDetailNode = currentLoadNodeList.item(k);
                        if (!currentLoadDetailNode.getNodeName().equals("#text")) {
                            if (currentLoadDetailNode.getNodeName().equals("TOOL_REFERENCE")) {
                                NamedNodeMap attributes = currentLoadDetailNode.getAttributes();
                                ToolDataModel tool = inputModel.getToolDataModel(attributes.getNamedItem("refid").getNodeValue());
                                if (tool != null) {
                                    currentLoad.add(tool);
                                }
                            }
                        }
                    }
                }
            }
        }
        InventoryDataModel inventoryDataModel = new InventoryDataModel(maximumToolTypesCapacities, currentLoad);
        return inventoryDataModel;
    }

    private Vector<JobDataModel> getJobs() {
        Vector<JobDataModel> jobs = new Vector<JobDataModel>();
        NodeList jobsNodes = xmlPlanningInputDocument.getElementsByTagName("JOB");

        for (int i = 0; i < jobsNodes.getLength(); i++) {
            String jobId = null;
            String name = null;
            String description = null;
            Calendar arrivalDate = null;
            Calendar dueDate = null;
            Vector<TaskDataModel> tasks = new Vector<TaskDataModel>();
            WorkcenterDataModel workcenter = null;

            Node jobNode = jobsNodes.item(i);
            NamedNodeMap attributes = jobNode.getAttributes();
            jobId = attributes.getNamedItem("id").getNodeValue();
            NodeList jobDetailsNodeList = jobNode.getChildNodes();
            for (int j = 0; j < jobDetailsNodeList.getLength(); j++) {
                if (!jobDetailsNodeList.item(j).getNodeName().equals("#text")) {
                    if (jobDetailsNodeList.item(j).getNodeName().equals("ARRIVAL_DATE")) {
                        arrivalDate = XMLUtil.getCalendarDateFromXMLNodeList(jobDetailsNodeList.item(j).getChildNodes());
                    } else if (jobDetailsNodeList.item(j).getNodeName().equals("DUE_DATE")) {
                        dueDate = XMLUtil.getCalendarDateFromXMLNodeList(jobDetailsNodeList.item(j).getChildNodes());
                    } else if (jobDetailsNodeList.item(j).getNodeName().equals("JOB_TASK_REFERENCE")) {
                        NamedNodeMap taskAttributes = jobDetailsNodeList.item(j).getAttributes();
                        TaskDataModel task = inputModel.getTaskDataModel(taskAttributes.getNamedItem("refid").getNodeValue());
                        tasks.add(task);
                    } else if (jobDetailsNodeList.item(j).getNodeName().equals("JOB_WORKCENTER_REFERENCE")) {
                        NamedNodeMap workcenterAttributes = jobDetailsNodeList.item(j).getAttributes();
                        workcenter = inputModel.getWorkcenterDataModel(workcenterAttributes.getNamedItem("refid").getNodeValue());
                    } else if (jobDetailsNodeList.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (jobDetailsNodeList.item(j).getLastChild() != null)
                            description = jobDetailsNodeList.item(j).getLastChild().getNodeValue();
                    } else if (jobDetailsNodeList.item(j).getNodeName().equals("NAME")) {
                        if (jobDetailsNodeList.item(j).getLastChild() != null)
                            name = jobDetailsNodeList.item(j).getLastChild().getNodeValue();
                    }
                }
            }

            JobDataModel job = new JobDataModel(jobId, name, description, arrivalDate, dueDate, tasks, workcenter);
            jobs.add(job);
        }
        return jobs;
    }

    private Vector<ResourceDataModel> getResources() {
        Vector<ResourceDataModel> resources = new Vector<ResourceDataModel>();
        NodeList resourcesNodes = xmlPlanningInputDocument.getElementsByTagName("RESOURCE");
        // Looping for all resources
        for (int i = 0; i < resourcesNodes.getLength(); i++) {
            String resourceId = new String();
            String resourceName = new String();
            String description = new String();
            SetUpMatrixDataModel setUpMatrixDataModel = null;
            HashMap<String, String> properties = new HashMap<String, String>();
            Vector<TimePeriodDataModel> nonWorkingPeriods = new Vector<TimePeriodDataModel>();
            Vector<TimePeriodDataModel> forcedWorkingPeriods = new Vector<TimePeriodDataModel>();
            ResourceAvailabilityDataModel resourceAvailability = null;
            InventoryDataModel endEffectorDataModel = null;

            Node resourceNode = resourcesNodes.item(i);
            NamedNodeMap attributes = resourceNode.getAttributes();
            resourceId = attributes.getNamedItem("id").getNodeValue();
            // System.out.println("RESOURCE ID : "+resourceId);
            NodeList resourceDetailsNodes = resourceNode.getChildNodes();
            // Reading resource's children nodes and looping among them
            for (int j = 0; j < resourceDetailsNodes.getLength(); j++) {
                if (!resourceDetailsNodes.item(j).getNodeName().equals("#text")) {
                    if (resourceDetailsNodes.item(j).getNodeName().equals("DESCRIPTION")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            description = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("NAME")) {
                        if (resourceDetailsNodes.item(j).getLastChild() != null)
                            resourceName = resourceDetailsNodes.item(j).getLastChild().getNodeValue();
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("RESOURCE_AVAILABILITY")) {
                        NodeList resourceAvailabilityDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
                        for (int k = 0; k < resourceAvailabilityDetailsNodes.getLength(); k++) {
                            if (!resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("#text")) {
                                if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("NON_WORKING_PERIODS")) {
                                    NodeList nonWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < nonWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!nonWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (nonWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel nonWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList nonWorkingPeriodDetailsNodeList = nonWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < nonWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(nonWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (nonWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = nonWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    nonWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                nonWorkingPeriods.add(nonWorkingPeriod);
                                            }
                                        }
                                    }
                                } else if (resourceAvailabilityDetailsNodes.item(k).getNodeName().equals("FORCED_WORKING_PERIODS")) {
                                    NodeList forcedWorkingPeriodsNodeList = resourceAvailabilityDetailsNodes.item(k).getChildNodes();
                                    // Looping among the periods (non_working_periods element child nodes)
                                    for (int l = 0; l < forcedWorkingPeriodsNodeList.getLength(); l++) {
                                        if (!forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("#text")) {
                                            if (forcedWorkingPeriodsNodeList.item(l).getNodeName().equals("PERIOD")) {
                                                TimePeriodDataModel forcedWorkingPeriod = null;
                                                Calendar fromDate = null;
                                                Calendar toDate = null;
                                                int ammountOfRecurrence = 0;
                                                String recurrencePeriod = null;
                                                int hours = 0;
                                                int minutes = 0;
                                                int seconds = 0;

                                                // Looping among the period's data
                                                NodeList forcedWorkingPeriodDetailsNodeList = forcedWorkingPeriodsNodeList.item(l).getChildNodes();
                                                for (int m = 0; m < forcedWorkingPeriodDetailsNodeList.getLength(); m++) {
                                                    if (!forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("#text")) {
                                                        if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("FROM_DATE")) {
                                                            fromDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML fromDate : "+fromDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("TO_DATE")) {
                                                            toDate = XMLUtil.getCalendarDateFromXMLNodeList(forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes());
                                                            // System.out.println("FROM XML toDate : "+toDate.getTime());
                                                        } else if (forcedWorkingPeriodDetailsNodeList.item(m).getNodeName().equals("REOCCURANCE")) {
                                                            // Getting the children elements (it should only be one exept if it defines time)
                                                            NodeList reoccuranceChildsNideList = forcedWorkingPeriodDetailsNodeList.item(m).getChildNodes();
                                                            // Looping among them. It should be one exept if it defines time, that's wh the loop is essential
                                                            for (int n = 0; n < reoccuranceChildsNideList.getLength(); n++) {
                                                                if (!reoccuranceChildsNideList.item(n).getNodeName().equals("#text")) {
                                                                    if (reoccuranceChildsNideList.item(n).getNodeName().equals("YEARS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_YEARS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MONTHS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_MONTHS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("WEEKS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("DAYS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            ammountOfRecurrence = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("HOURS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            hours = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("MINUTES")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            minutes = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    } else if (reoccuranceChildsNideList.item(n).getNodeName().equals("SECONDS")) {
                                                                        // Setting the timePeriod
                                                                        recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;

                                                                        // Setting the ammountOfRecurrence
                                                                        String ammountOfRecurrenceString = reoccuranceChildsNideList.item(n).getLastChild().getNodeValue();
                                                                        try {
                                                                            seconds = Integer.parseInt(ammountOfRecurrenceString);
                                                                        } catch (Exception ex) {
                                                                            // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Puting the values alltogether constracting a period object
                                                if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod)) {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, hours, minutes, seconds);
                                                } else {
                                                    forcedWorkingPeriod = new TimePeriodDataModel(fromDate, toDate, recurrencePeriod, ammountOfRecurrence);
                                                }

                                                forcedWorkingPeriods.add(forcedWorkingPeriod);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("SET_UP_MATRIX_REFERENCE")) {
                        String setUpMatrixReference = null;
                        NamedNodeMap referenceAttributes = resourceDetailsNodes.item(j).getAttributes();
                        if (referenceAttributes.getNamedItem("refid") != null) {
                            setUpMatrixReference = referenceAttributes.getNamedItem("refid").getNodeValue();
                        }
                        setUpMatrixDataModel = inputModel.getSetUpMatrixDataModel(setUpMatrixReference);
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("PROPERTIES")) {
                        NodeList propertiesDetailsNodes = resourceDetailsNodes.item(j).getChildNodes();
                        // Looping for the resource availability nodes (up to now 10/2/20004 only non working periods are defined and no working periods)
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
                                                propertyName = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            } else if (propertyDetailsNodeList.item(l).getNodeName().equals("VALUE")) {
                                                propertyValue = propertyDetailsNodeList.item(l).getLastChild().getNodeValue();
                                            }
                                        }
                                    }
                                    properties.put(propertyName, propertyValue);
                                }
                            }
                        }
                    } else if (resourceDetailsNodes.item(j).getNodeName().equals("END_EFFECTORS")) {
                        endEffectorDataModel = getInventoryDataModel(resourceDetailsNodes.item(j));
                    }
                }
            }
            resourceAvailability = new ResourceAvailabilityDataModel(nonWorkingPeriods, forcedWorkingPeriods);

            ResourceDataModel resource = new ResourceDataModel(resourceId, resourceName, description, resourceAvailability, setUpMatrixDataModel, endEffectorDataModel, properties);
            resources.add(resource);
        }
        return resources;
    }

    private String getPlanningInputDataModelId() {
        Node planNode = xmlPlanningInputDocument.getDocumentElement();
        String id = planNode.getAttributes().getNamedItem("id").getNodeValue();
        return id;
    }

    private Calendar getPlanningInputDataModelStartDate() {
        Node planNode = xmlPlanningInputDocument.getDocumentElement();
        String planStartDay = planNode.getAttributes().getNamedItem("planStartDate_day").getNodeValue();
        planStartDay.replaceFirst("", "");
        String planStartMonth = planNode.getAttributes().getNamedItem("planStartDate_month").getNodeValue();
        String planStartYear = planNode.getAttributes().getNamedItem("planStartDate_year").getNodeValue();
        Calendar planStartCalendarDate = Calendar.getInstance();
        planStartCalendarDate.set(Calendar.MILLISECOND, 0);
        planStartCalendarDate.set(Integer.parseInt(planStartYear), Integer.parseInt(planStartMonth) - 1, Integer.parseInt(planStartDay), 0, 0, 0);
        return planStartCalendarDate;
    }

    private Calendar getPlanningInputDataModelEndDate() {
        Node planNode = xmlPlanningInputDocument.getDocumentElement();
        String planEndDay = planNode.getAttributes().getNamedItem("planEndDate_day").getNodeValue();
        String planEndMonth = planNode.getAttributes().getNamedItem("planEndDate_month").getNodeValue();
        String planEndYear = planNode.getAttributes().getNamedItem("planEndDate_year").getNodeValue();
        Calendar planEndCalendarDate = Calendar.getInstance();
        planEndCalendarDate.set(Calendar.MILLISECOND, 0);
        planEndCalendarDate.set(Integer.parseInt(planEndYear), Integer.parseInt(planEndMonth) - 1, Integer.parseInt(planEndDay), 0, 0, 0);
        return planEndCalendarDate;
    }

    private boolean getContinueAssignmentsAfterPlanEndDate() {
        Node planNode = xmlPlanningInputDocument.getDocumentElement();
        Node continueAssignmentsAfterPlanEndDateAttribute = planNode.getAttributes().getNamedItem("continueAssignmentsAfterPlanEndDate");
        String continueAssignmentsAfterPlanEndDateString = null;
        if (continueAssignmentsAfterPlanEndDateAttribute != null) {
            continueAssignmentsAfterPlanEndDateString = continueAssignmentsAfterPlanEndDateAttribute.getNodeValue();
        }
        boolean continueAssignmentsAfterPlanEndDate = true;// Default value
        try {
            continueAssignmentsAfterPlanEndDate = Boolean.valueOf(continueAssignmentsAfterPlanEndDateString).booleanValue();
        } catch (Exception ex) {
            // Format exception or attribute does not exists so leave the default value
        }
        return continueAssignmentsAfterPlanEndDate;
    }

    private HashMap<String, SetUpMatrixDataModel> getAllSetUpMatrices() {
        HashMap<String, SetUpMatrixDataModel> setUpMatrices = new HashMap<String, SetUpMatrixDataModel>();

        NodeList setUpMatricesNodes = xmlPlanningInputDocument.getElementsByTagName("SET_UP_MATRIX");
        // Looping for all set up matrices
        for (int i = 0; i < setUpMatricesNodes.getLength(); i++) {
            String setUpMatrixId = null;
            Node setUpMatrixNode = setUpMatricesNodes.item(i);
            NamedNodeMap attributes = setUpMatrixNode.getAttributes();
            setUpMatrixId = attributes.getNamedItem("id").getNodeValue();

            SetUpMatrixDataModel setUpMatrix = new SetUpMatrixDataModel(setUpMatrixId);

            NodeList setUpMatrixDetailsNodes = setUpMatrixNode.getChildNodes();
            // Reading resource's children nodes and looping among them
            for (int j = 0; j < setUpMatrixDetailsNodes.getLength(); j++) {
                if (!setUpMatrixDetailsNodes.item(j).getNodeName().equals("#text")) {
                    String fromCode = null;
                    String toCode = null;
                    long timeInMilliseconds = 0;

                    Node setUpNode = setUpMatrixDetailsNodes.item(j);
                    NodeList setUpDetailsNodes = setUpNode.getChildNodes();
                    for (int k = 0; k < setUpDetailsNodes.getLength(); k++) {
                        Node detailNode = setUpDetailsNodes.item(k);
                        if (!detailNode.getNodeName().equals("#text")) {
                            if (detailNode.getNodeName().equals("FROM_CODE")) {
                                if (detailNode.getLastChild() != null)
                                    fromCode = detailNode.getLastChild().getNodeValue();
                            } else if (detailNode.getNodeName().equals("TO_CODE")) {
                                if (detailNode.getLastChild() != null)
                                    toCode = detailNode.getLastChild().getNodeValue();
                            } else if (detailNode.getNodeName().equals("TIME_IN_SECONDS")) {
                                if (detailNode.getLastChild() != null) {
                                    String timeInSecondsString = detailNode.getLastChild().getNodeValue();
                                    long timeInSeconds = 0;
                                    try {
                                        timeInSeconds = Long.parseLong(timeInSecondsString);
                                    } catch (Exception ex) {
                                        // Do not print anything this exception occured to wrong xml values. Continue with defaults.
                                    }
                                    timeInMilliseconds = timeInSeconds * 1000L;
                                }
                            }
                        }
                    }

                    setUpMatrix.addSetUp(fromCode, toCode, timeInMilliseconds);
                }
            }

            setUpMatrices.put(setUpMatrixId, setUpMatrix);
        }
        return setUpMatrices;
    }
}
