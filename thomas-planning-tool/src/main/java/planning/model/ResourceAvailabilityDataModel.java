package planning.model;

import java.util.Calendar;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

public class ResourceAvailabilityDataModel extends AbstractDataModel {
    private Vector<TimePeriodDataModel> nonWorkingPeriods = new Vector<TimePeriodDataModel>();

    private Vector<TimePeriodDataModel> exceptionsNonWorkingPeriods = new Vector<TimePeriodDataModel>();// Forced
                                                                                                        // working
                                                                                                        // days

    private String id = null;

    public ResourceAvailabilityDataModel(Vector<TimePeriodDataModel> nonWorkingPeriodDataModelVector, Vector<TimePeriodDataModel> forcedWorkingPeriodDataModelVector, String id) {
        this.nonWorkingPeriods = nonWorkingPeriodDataModelVector;
        this.exceptionsNonWorkingPeriods = forcedWorkingPeriodDataModelVector;
        this.id = id;
    }

    // TODO : this constructor should be deprecated so the software uses the other constructor
    @Deprecated
    public ResourceAvailabilityDataModel(Vector<TimePeriodDataModel> nonWorkingPeriodDataModelVector, Vector<TimePeriodDataModel> forcedWorkingPeriodDataModelVector) {
        this.nonWorkingPeriods = nonWorkingPeriodDataModelVector;
        this.exceptionsNonWorkingPeriods = forcedWorkingPeriodDataModelVector;
        this.id = "Default Calendar";
    }

    public Node toXMLNode(Document document) {
        Element resourceAvailabilityElement = document.createElement("RESOURCE_AVAILABILITY");

        Element nonWorkingPeriodsElement = document.createElement("NON_WORKING_PERIODS");
        for (int i = 0; i < this.nonWorkingPeriods.size(); i++) {
            nonWorkingPeriodsElement.appendChild(((TimePeriodDataModel) this.nonWorkingPeriods.get(i)).toXMLNode(document));
        }
        resourceAvailabilityElement.appendChild(nonWorkingPeriodsElement);

        Element forcedWorkingPeriodsElement = document.createElement("FORCED_WORKING_PERIODS");
        for (int i = 0; i < this.exceptionsNonWorkingPeriods.size(); i++) {
            forcedWorkingPeriodsElement.appendChild(((TimePeriodDataModel) this.exceptionsNonWorkingPeriods.get(i)).toXMLNode(document));
        }
        resourceAvailabilityElement.appendChild(forcedWorkingPeriodsElement);

        return resourceAvailabilityElement;
    }

    @SuppressWarnings("unchecked")
    public Vector<TimePeriodDataModel> getNonWorkingPeriods() {
        return (Vector<TimePeriodDataModel>) this.nonWorkingPeriods.clone();
    }

    protected void setNonWorkingPeriods(Vector<TimePeriodDataModel> newNonWorkingPeriods) {
        this.nonWorkingPeriods = newNonWorkingPeriods;
    }

    protected void addNonWorkingPeriod(TimePeriodDataModel newPeriod) {
        this.nonWorkingPeriods.add(newPeriod);
    }

    public String getResourceAvailabilityId() {
        return this.id;
    }

    protected void addWorkingPeriod(TimePeriodDataModel newPeriod) {
        this.exceptionsNonWorkingPeriods.add(newPeriod);
    }

    @SuppressWarnings("unchecked")
    public Vector<TimePeriodDataModel> getWorkingPeriods() {
        return (Vector<TimePeriodDataModel>) this.exceptionsNonWorkingPeriods.clone();
    }

    /**
     * getNextNonWorkingDate
     * 
     * @param date
     *            Calendar
     * @return Calendar
     * @deprecated
     */
    public Calendar getNextNonWorkingDate(Calendar date) {
        return null;
    }

    /**
     * getNextWorkingDate
     * 
     * @param date
     *            Calendar
     * @return Calendar
     * @deprecated
     */
    public Calendar getNextWorkingDate(Calendar date) {
        return null;
    }
}
