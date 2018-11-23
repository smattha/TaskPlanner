package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.ResourceAvailabilityDataModel;
import planning.model.ResourceDataModel;
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

public class ResourceSimulator {
    public static final String IDLE = "IDLE";
    public static final String BUSY = "BUSY";
    public static final String DOWN = "DOWN";

    private ResourceDataModel resource = null;
    private String resourceState = null;

    private Vector<ResourceSimulator> tools = new Vector<ResourceSimulator>();

    public void holdTool(ResourceSimulator tool) throws Exception {
        tools.add(tool);
    }

    public void releaseTool(ResourceSimulator tool) throws Exception {
        tools.remove(tool);
    }

    public ResourceSimulator(ResourceDataModel resource) {
        this.resource = resource;
    }

    public String getResourceState() {
        return this.resourceState;
    }

    public ResourceDataModel getResourceDataModel() {
        return this.resource;
    }

    public void setResourceState(String newState) {
        this.resourceState = newState;
    }

    public String getResourceId() {
        return this.resource.getResourceId();
    }

    public boolean isResourceDown(Calendar currentTime) {
        // Checking first the exception dates that the resource is surely up
        Vector<TimePeriodDataModel> exceptionPeriods = resource.getResourceAvailabilityDataModel().getWorkingPeriods();
        for (int i = 0; i < exceptionPeriods.size(); i++) {
            TimePeriodDataModel period = exceptionPeriods.get(i);
            Calendar fromDate = period.getFromDate();
            long fromDateInMilliseconds = fromDate.getTimeInMillis();
            Calendar toDate = period.getToDate();
            long toDateInMilliseconds = toDate.getTimeInMillis();
            long periodInMilliseconds = period.getPeriodInMilliseconds();
            long currentTimeInMilliseconds = currentTime.getTimeInMillis();
            if (currentTimeInMilliseconds > fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = currentTimeInMilliseconds - fromDateInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds;
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - ((quotient * periodInMilliseconds) + fromDateInMilliseconds);
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        return false;
                    }
                } else {
                    if (fromDate.getTimeInMillis() < currentTime.getTimeInMillis() && currentTime.getTimeInMillis() <= toDate.getTimeInMillis()) {
                        return false;
                    }
                }
            } else if (currentTimeInMilliseconds < fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = fromDateInMilliseconds - currentTimeInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds + 1;// We add one because we want the first period start BEFORE the current time (Rounded up)
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - (fromDateInMilliseconds - (quotient * periodInMilliseconds));
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        return false;
                    }
                } else {
                    if (fromDate.getTimeInMillis() < currentTime.getTimeInMillis() && currentTime.getTimeInMillis() <= toDate.getTimeInMillis()) {
                        return false;
                    }
                }
            } else // currentTimeInMilliseconds == fromDateInMilliseconds thus the resource is about to get down
            {
                return false;
            }
        }

        // Checking the rest of the resource calendar
        Vector<TimePeriodDataModel> periods = resource.getResourceAvailabilityDataModel().getNonWorkingPeriods();
        boolean isResourceDown = false;
        for (int i = 0; i < periods.size(); i++) {
            TimePeriodDataModel period = periods.get(i);
            Calendar fromDate = period.getFromDate();
            long fromDateInMilliseconds = fromDate.getTimeInMillis();
            Calendar toDate = period.getToDate();
            long toDateInMilliseconds = toDate.getTimeInMillis();
            long periodInMilliseconds = period.getPeriodInMilliseconds();
            long currentTimeInMilliseconds = currentTime.getTimeInMillis();
            if (currentTimeInMilliseconds > fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = currentTimeInMilliseconds - fromDateInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds;
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - ((quotient * periodInMilliseconds) + fromDateInMilliseconds);
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        isResourceDown = true;
                        break;
                    }
                } else {
                    if (fromDate.getTimeInMillis() <= currentTime.getTimeInMillis() && currentTime.getTimeInMillis() < toDate.getTimeInMillis()) {
                        isResourceDown = true;
                        break;
                    }
                }
            } else if (currentTimeInMilliseconds < fromDateInMilliseconds) {
                long differenceFromDateToCurrentDateInMilliseconds = fromDateInMilliseconds - currentTimeInMilliseconds;
                if (periodInMilliseconds != 0) {
                    long quotient = differenceFromDateToCurrentDateInMilliseconds / periodInMilliseconds + 1; // We add one because we want the first period start BEFORE the current time (Rounded up)
                    long timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds = currentTimeInMilliseconds - (fromDateInMilliseconds - (quotient * periodInMilliseconds));
                    if (timeDifferenceFromTheStartOfThePeriodUntilTheCurrentDateInMilliseconds < (toDateInMilliseconds - fromDateInMilliseconds)) {
                        isResourceDown = true;
                        break;
                    }
                } else {
                    if (fromDate.getTimeInMillis() <= currentTime.getTimeInMillis() && currentTime.getTimeInMillis() < toDate.getTimeInMillis()) {
                        isResourceDown = true;
                        break;
                    }
                }
            } else // currentTimeInMilliseconds == fromDateInMilliseconds thus the resource is about to get down
            {
                isResourceDown = true;
                break;
            }
        }
        return isResourceDown;
    }

    protected Calendar getNextNonWorkingDate(Calendar date) {
        if (isResourceDown(date)) {
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(date);
            Calendar nextNonWorkingDateDueToForcedWorkingPeriods = getNextNonWorkingDateFromForcedWorkingDates(date);
            if (nextNonWorkingDateDueToForcedWorkingPeriods!=null && nextWorkingDateDueToNonWorkingPeriods!=null 
            		&& nextNonWorkingDateDueToForcedWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis()) {
                return nextNonWorkingDateDueToForcedWorkingPeriods;
            } else if(nextNonWorkingDateDueToForcedWorkingPeriods==null) {
            	// Means that there is no forced working periods
            	// so next non working is the next non working from non working periods
            	// ... no check for non working periods means that here there might be bug in the future
                Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(date);
                if(nextNonWorkingDateDueToNonWorkingPeriods!=null) {
                	return nextNonWorkingDateDueToNonWorkingPeriods;
                } else {
                	return null;
                }
            }
            // else
            {
                Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(date);
                Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(nextNonWorkingDateDueToNonWorkingPeriods);
                return nextNonWorkingDateDueToForcedWorkingPeriods2;
            }
        }
        // else
        {
            if (isDateInsideTheResoureNonWorkingPeriods(date)) {
                // Must check if the next day the resource will go down due to forced working periods
                // is smallest from the day that the resource will be up due to non workingperiod.
                // This means that the next date the resource will go down due to forced working periods is allready up
                // so next non working period is the next time the resource will be down also checking the exceptions
                Calendar nextNonWorkingDateDueToForcedWorkingPeriods = getNextNonWorkingDateFromForcedWorkingDates(date);
                Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(date);
                if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() > nextNonWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                    return nextNonWorkingDateDueToForcedWorkingPeriods;
                }
                // else
                {
                    Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(date);
                    Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(nextNonWorkingDateDueToNonWorkingPeriods);
                    return nextNonWorkingDateDueToForcedWorkingPeriods2;
                }
            }
            // else
            {
                Calendar nextNonWorkingDateDueToNonWorkingPeriods = getNextNonWorkingDateFromNonWorkingDates(date);
                if (nextNonWorkingDateDueToNonWorkingPeriods == null) {
                    return null;
                }
                if (isDateInsideTheResoureForcedWorkingPeriods(nextNonWorkingDateDueToNonWorkingPeriods))// Checking if the next working date due to non working periods is inside a forced working period
                {
                    Calendar nextNonWorkingDateDueToForcedWorkingPeriods2 = getNextNonWorkingDateFromForcedWorkingDates(nextNonWorkingDateDueToNonWorkingPeriods);
                    if (nextNonWorkingDateDueToForcedWorkingPeriods2 == null) {
                        return nextNonWorkingDateDueToNonWorkingPeriods;
                    }
                    return nextNonWorkingDateDueToForcedWorkingPeriods2;
                }
                // else
                {
                    return nextNonWorkingDateDueToNonWorkingPeriods;
                }
            }
        }
    }

    private Calendar getNextNonWorkingDateFromNonWorkingDates(Calendar date) {
        Calendar copyOfDateToCheck = (Calendar) date.clone();// this is done in order not to mess with the schedule date

        Calendar nextNonWorkingDate = null;

        int loopTimes = 0;
        while (isDateInsideTheResoureNonWorkingPeriods(copyOfDateToCheck)) {
            // The current copyOfDateToCheck is inside non working period ...
            // So must check when this period ends and then find the next non working copyOfDateToCheck
            ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
            Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
            for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
                TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
                // Checking if is actually non working period for this down time period
                Calendar startPeriodDate = timePeriod.getFromDate();
                long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
                long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
                if (copyOfDateToCheck.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                    if (periodInMilliseconds != 0) {
                        long periodTimes = (copyOfDateToCheck.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                        if (startPeriodDate.getTimeInMillis() + (periodTimes * periodInMilliseconds) + durationInMilliseconds > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(startPeriodDate.getTimeInMillis() + (periodTimes * periodInMilliseconds) + durationInMilliseconds);
                        }
                    } else {
                        if (timePeriod.getToDate().getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(timePeriod.getToDate().getTimeInMillis());
                        }
                    }
                } else {
                    if (periodInMilliseconds != 0) {
                        // TODO: check if start period copyOfDateToCheck is after the copyOfDateToCheck assuming not and continue...
                        throw new RuntimeException("Start period copyOfDateToCheck is after the copyOfDateToCheck assuming not implemented yet");
                    }
                    // else
                    {
                        if (timePeriod.getToDate().getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                            copyOfDateToCheck.setTimeInMillis(timePeriod.getToDate().getTimeInMillis());
                        }
                    }
                }
            }
            // TODO : Find a condition in order to end the loop if the resource is completely DOWN.
            System.out.println("LOOP : " + loopTimes);
        }

        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();

            if (copyOfDateToCheck.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextNonWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (copyOfDateToCheck.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;

                    periodTimes++; // Adding one more period makes the next non working start copyOfDateToCheck

                    nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds;

                    Calendar temp = Calendar.getInstance();
                    temp.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                } else {
                    if (startPeriodDate.getTimeInMillis() > copyOfDateToCheck.getTimeInMillis()) {
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis();
                    }
                }

                if (nextNonWorkingDate == null) {
                    if (nextNonWorkingDateInMilliseconds != 0) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                } else {
                    // check for the smallest copyOfDateToCheck
                    if (nextNonWorkingDate.getTimeInMillis() > nextNonWorkingDateInMilliseconds) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds == 0) {
                    if (nextNonWorkingDate == null) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(startPeriodDate.getTimeInMillis());
                    } else {
                        // check for the smallest copyOfDateToCheck
                        if (nextNonWorkingDate.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                            nextNonWorkingDate = Calendar.getInstance();
                            nextNonWorkingDate.setTimeInMillis(startPeriodDate.getTimeInMillis());
                        }
                    }
                } else {
                    // TODO: check if start period copyOfDateToCheck is after the copyOfDateToCheck assuming not and continue...
                    throw new RuntimeException("Start period copyOfDateToCheck is after the copyOfDateToCheck assuming not implemented yet");
                }
            }
        }
        return nextNonWorkingDate;
    }

    private Calendar getNextNonWorkingDateFromForcedWorkingDates(Calendar date) {
        Calendar nextNonWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextNonWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (date.getTimeInMillis() <= startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds) {
                        // Means we are inside a forced working period so the end might be the next non working date
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    } else {
                        // We are outside the non working period so we must add one period and then is the next period's end date
                        periodTimes++;
                        nextNonWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    }
                } else {
                    if (date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        nextNonWorkingDateInMilliseconds = timePeriod.getToDate().getTimeInMillis();
                    }
                }
                // Make a check ...
                if (nextNonWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextNonWorkingDate == null) {
                    if (nextNonWorkingDateInMilliseconds != 0) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                } else // check for the smallest
                {
                    if (nextNonWorkingDate.getTimeInMillis() > nextNonWorkingDateInMilliseconds) {
                        nextNonWorkingDate = Calendar.getInstance();
                        nextNonWorkingDate.setTimeInMillis(nextNonWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("NotImpmented Yet");
                }
                // else
                {
                    if (nextNonWorkingDate == null) {
                        nextNonWorkingDate = (Calendar) timePeriod.getToDate().clone();
                    } else // check for the smallest
                    {
                        if (nextNonWorkingDate.getTimeInMillis() > timePeriod.getToDate().getTimeInMillis()) {
                            nextNonWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        }
                    }
                }
            }
        }
        return nextNonWorkingDate;
    }

    // NOTICE: the resource must be DOWN
    // If the resource is idle or busy then this time reflect the next time the resource will be up from down
    protected Calendar getNextWorkingDate(Calendar date) {
        if (isResourceDown(date)) {
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(date);
            Calendar nextWorkingDateDueToForcedWorkingPeriods = getNextWorkingDateFromForcedWorkingDates(date);
            if (nextWorkingDateDueToNonWorkingPeriods == null) {
                return null;
            } else if (nextWorkingDateDueToForcedWorkingPeriods == null) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else {
                return nextWorkingDateDueToForcedWorkingPeriods;
            }
        }
        // else
        {
            // For this date calculate the next working date as before ...
            Calendar nextWorkingDateDueToNonWorkingPeriods = getNextWorkingDateFromNonWorkingDates(date);
            Calendar nextWorkingDateDueToForcedWorkingPeriods = getNextWorkingDateFromForcedWorkingDates(date);

            if (nextWorkingDateDueToNonWorkingPeriods == null) {
                return null;
            } else if (nextWorkingDateDueToForcedWorkingPeriods == null) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else if (nextWorkingDateDueToNonWorkingPeriods.getTimeInMillis() < nextWorkingDateDueToForcedWorkingPeriods.getTimeInMillis()) {
                return nextWorkingDateDueToNonWorkingPeriods;
            } else {
                return nextWorkingDateDueToForcedWorkingPeriods;
            }
        }
    }

    private Calendar getNextWorkingDateFromNonWorkingDates(Calendar date) {
        Calendar nextWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceNonWorkingPeriods.get(i);
            // Checking if is actually non working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    nextWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    while (nextWorkingDateInMilliseconds <= date.getTimeInMillis()) {
                        periodTimes++; // Adding one more period makes the NEXT working start date
                        nextWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds + durationInMilliseconds;
                    }
                } else {
                    if (timePeriod.getToDate().getTimeInMillis() > date.getTimeInMillis()) {
                        nextWorkingDateInMilliseconds = timePeriod.getToDate().getTimeInMillis();
                    }
                }

                if (nextWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextWorkingDate == null) {
                    if (nextWorkingDateInMilliseconds != 0) {
                        nextWorkingDate = Calendar.getInstance();
                        nextWorkingDate.setTimeInMillis(nextWorkingDateInMilliseconds);
                    }
                } else if (nextWorkingDate.getTimeInMillis() > nextWorkingDateInMilliseconds) {
                    nextWorkingDate = Calendar.getInstance();
                    nextWorkingDate.setTimeInMillis(nextWorkingDateInMilliseconds);
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("NotImpmented Yet");
                }
                // else
                {
                    if (timePeriod.getToDate().getTimeInMillis() > date.getTimeInMillis()) {
                        if (nextWorkingDate == null) {
                            nextWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        } else if (nextWorkingDate.getTimeInMillis() > timePeriod.getToDate().getTimeInMillis()) {
                            nextWorkingDate = (Calendar) timePeriod.getToDate().clone();
                        }
                    }
                }
            }
        }

        if (nextWorkingDate == null) {
            return null;
        }

        if (isDateInsideTheResoureNonWorkingPeriods(nextWorkingDate)) {
            return getNextWorkingDateFromNonWorkingDates(nextWorkingDate);
        }
        return nextWorkingDate;
    }

    private Calendar getNextWorkingDateFromForcedWorkingDates(Calendar date) {
        Calendar nextForcedWorkingDate = null;
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();

            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                long nextForcedWorkingDateInMilliseconds = 0;
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    periodTimes++; // Adding one more period makes the next working start date
                    nextForcedWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis() + periodTimes * periodInMilliseconds;
                } else {
                    if (startPeriodDate.getTimeInMillis() > date.getTimeInMillis()) {
                        nextForcedWorkingDateInMilliseconds = startPeriodDate.getTimeInMillis();
                    }
                }
                if (nextForcedWorkingDateInMilliseconds == 0) {
                    // do nothing
                } else if (nextForcedWorkingDate == null) {
                    if (nextForcedWorkingDateInMilliseconds != 0) {
                        nextForcedWorkingDate = Calendar.getInstance();
                        nextForcedWorkingDate.setTimeInMillis(nextForcedWorkingDateInMilliseconds);
                    }
                } else // check for the smallest date
                {
                    if (nextForcedWorkingDate.getTimeInMillis() > nextForcedWorkingDateInMilliseconds) {
                        nextForcedWorkingDate = Calendar.getInstance();
                        nextForcedWorkingDate.setTimeInMillis(nextForcedWorkingDateInMilliseconds);
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() > date.getTimeInMillis()) {
                        if (nextForcedWorkingDate == null) {
                            nextForcedWorkingDate = (Calendar) startPeriodDate.clone();
                        } else // check for the smallest date
                        {
                            if (nextForcedWorkingDate.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                                nextForcedWorkingDate = (Calendar) startPeriodDate.clone();
                            }
                        }
                    }
                }
            }
        }
        return nextForcedWorkingDate;
    }

    // BE CAREFULL this function does not take into account the forced working periods
    private boolean isDateInsideTheResoureNonWorkingPeriods(Calendar date) {
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceNonWorkingPeriods = resourceAvailability.getNonWorkingPeriods();
        for (int i = 0; i < resourceNonWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = (TimePeriodDataModel) resourceNonWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) <= date.getTimeInMillis() && date.getTimeInMillis() < startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) + durationInMilliseconds) {
                        return true;
                    }
                } else {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDateInsideTheResoureForcedWorkingPeriods(Calendar date) {
        ResourceAvailabilityDataModel resourceAvailability = resource.getResourceAvailabilityDataModel();
        Vector<TimePeriodDataModel> resourceForcedWorkingPeriods = resourceAvailability.getWorkingPeriods();
        for (int i = 0; i < resourceForcedWorkingPeriods.size(); i++) {
            TimePeriodDataModel timePeriod = resourceForcedWorkingPeriods.get(i);
            // Checking if is actually working period for this down time period
            Calendar startPeriodDate = timePeriod.getFromDate();
            long periodInMilliseconds = timePeriod.getPeriodInMilliseconds();
            long durationInMilliseconds = timePeriod.getToDate().getTimeInMillis() - startPeriodDate.getTimeInMillis();
            if (date.getTimeInMillis() > startPeriodDate.getTimeInMillis()) {
                if (periodInMilliseconds != 0) {
                    long periodTimes = (date.getTimeInMillis() - startPeriodDate.getTimeInMillis()) / periodInMilliseconds;
                    if (startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) <= date.getTimeInMillis() && date.getTimeInMillis() < startPeriodDate.getTimeInMillis() + (periodInMilliseconds * periodTimes) + durationInMilliseconds) {
                        return true;
                    }
                } else {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            } else {
                if (periodInMilliseconds != 0) {
                    // TODO: check if start period date is after the date assuming not and continue...
                    throw new RuntimeException("Start period date is after the date assuming not implemented yet");
                }
                // else
                {
                    if (startPeriodDate.getTimeInMillis() <= date.getTimeInMillis() && date.getTimeInMillis() < timePeriod.getToDate().getTimeInMillis()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
