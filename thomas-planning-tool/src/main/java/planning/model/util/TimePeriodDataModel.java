package planning.model.util;

import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import planning.model.io.XMLUtil;

// TODO: Think about implementing period re-occurrence in years or in months
public class TimePeriodDataModel
{
    // The static values are in milliseconds of the described period.
    public static final String PERIOD_IN_YEARS = "PERIOD_IN_YEARS";
    public static final String PERIOD_IN_MONTHS = "PERIOD_IN_MONTHS";
    public static final String PERIOD_IN_WEEKS = "PERIOD_IN_WEEKS";
    public static final String PERIOD_IN_DAYS = "PERIOD_IN_DAYS";
    public static final String PERIOD_IN_TIME = "PERIOD_IN_TIME";

    private Calendar fromDate = null;
    private Calendar toDate = null;
    private int ammountOfRecurrence = 0;
    private String recurrencePeriod = null;
    private long hours = 0;
    private long minutes = 0;
    private long seconds = 0;

    public TimePeriodDataModel(Calendar fromDate, Calendar toDate, String recurrencePeriod, int ammountOfRecurrence)
    {
        if (recurrencePeriod.equals(TimePeriodDataModel.PERIOD_IN_DAYS)
                || recurrencePeriod.equals(TimePeriodDataModel.PERIOD_IN_MONTHS)
                || recurrencePeriod.equals(TimePeriodDataModel.PERIOD_IN_WEEKS)
                || recurrencePeriod.equals(TimePeriodDataModel.PERIOD_IN_YEARS))
        {
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.recurrencePeriod = recurrencePeriod;
            this.ammountOfRecurrence = ammountOfRecurrence;
        }
    }

    // The integer values hour, minutes, seconds define the period of the reoccurrence
    public TimePeriodDataModel(Calendar fromDate, Calendar toDate, int hours, int minutes, int seconds)
    {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;
        if (hours == 0 && minutes == 0 && seconds == 0)
        {
            this.ammountOfRecurrence = 0;
        }
        else
            this.ammountOfRecurrence = 1;

    }

    // TODO: If the implementation of year period and month period is decided then update the constructor accordingly
    public TimePeriodDataModel(Calendar fromDate, Calendar toDate, long periodInMilliseconds)
    {
        this.fromDate = fromDate;
        this.toDate = toDate;

        if (periodInMilliseconds == 0)
        {
            this.ammountOfRecurrence = 0;
            this.recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;
        }
        else if (((double) periodInMilliseconds / (double) 604800000) == 1D)
        {
            this.ammountOfRecurrence = 1;
            this.recurrencePeriod = TimePeriodDataModel.PERIOD_IN_WEEKS;
        }
        else if (((double) periodInMilliseconds / (double) 86400000) == 1D)
        {
            this.ammountOfRecurrence = 1;
            this.recurrencePeriod = TimePeriodDataModel.PERIOD_IN_DAYS;
        }
        else// Custom
        {
            this.ammountOfRecurrence = 1;
            this.recurrencePeriod = TimePeriodDataModel.PERIOD_IN_TIME;
            long days = periodInMilliseconds / 86400000;
            periodInMilliseconds = periodInMilliseconds - days * 86400000;
            long hours = periodInMilliseconds / 3600000;
            periodInMilliseconds = periodInMilliseconds - hours * 3600000;
            long minutes = periodInMilliseconds / 60000;
            this.hours = hours + days * 24;
            this.minutes = minutes;
            this.seconds = 0;
        }
    }

    public Calendar getFromDate()
    {
        return this.fromDate;
    }

    public Calendar getToDate()
    {
        return this.toDate;
    }

    public long getPeriodInMilliseconds()
    {
        if (recurrencePeriod == null)
        {
            //TODO: check the algorithm if this is correct.
            return 0;
        }
        else if (TimePeriodDataModel.PERIOD_IN_DAYS.equals(recurrencePeriod))
        {
            return 1000 * 3600 * 24;
        }
        else if (TimePeriodDataModel.PERIOD_IN_WEEKS.equals(recurrencePeriod))
        {
            return 1000 * 3600 * 24 * 7;
        }
        else if (TimePeriodDataModel.PERIOD_IN_YEARS.equals(recurrencePeriod))
        {
            throw new RuntimeException("TimePeriodDataModel: Period in years not implemented yet");
        }
        else if (TimePeriodDataModel.PERIOD_IN_MONTHS.equals(recurrencePeriod))
        {
            throw new RuntimeException("TimePeriodDataModel: Period in months not implemented yet");
        }
        else if (TimePeriodDataModel.PERIOD_IN_TIME.equals(recurrencePeriod))
        {
            return 1000 * ((3600 * hours) + (60 * minutes) + seconds);
        }
        else
        {
            throw new RuntimeException("TimePeriodDataModel instance not initialized properly");
        }
    }

    public Node toXMLNode(Document document)
    {
        Element timePeriodElement = document.createElement("PERIOD");

        Element fromDateElement = document.createElement("FROM_DATE");
        fromDateElement.appendChild(XMLUtil.getNodeFromCalendar(this.fromDate, document));
        timePeriodElement.appendChild(fromDateElement);

        Element toDateElement = document.createElement("TO_DATE");
        toDateElement.appendChild(XMLUtil.getNodeFromCalendar(this.toDate, document));
        timePeriodElement.appendChild(toDateElement);

        Element reoccuranceElement = document.createElement("REOCCURANCE");
        if (this.recurrencePeriod == null)
        {
        }
        else if (TimePeriodDataModel.PERIOD_IN_DAYS.equals(this.recurrencePeriod))
        {
            Element daysElement = document.createElement("DAYS");
            daysElement.appendChild(document.createTextNode("" + this.ammountOfRecurrence));
            reoccuranceElement.appendChild(daysElement);
        }
        else if (TimePeriodDataModel.PERIOD_IN_WEEKS.equals(this.recurrencePeriod))
        {
            Element weeksElement = document.createElement("WEEKS");
            weeksElement.appendChild(document.createTextNode("" + this.ammountOfRecurrence));
            reoccuranceElement.appendChild(weeksElement);
        }
        else if (TimePeriodDataModel.PERIOD_IN_YEARS.equals(this.recurrencePeriod))
        {
            throw new RuntimeException("TimePeriodDataModel: Period in years not implemented yet");
        }
        else if (TimePeriodDataModel.PERIOD_IN_MONTHS.equals(this.recurrencePeriod))
        {
            throw new RuntimeException("TimePeriodDataModel: Period in months not implemented yet");
        }
        else if (TimePeriodDataModel.PERIOD_IN_TIME.equals(this.recurrencePeriod))
        {
            Element hoursElement = document.createElement("HOURS");
            hoursElement.appendChild(document.createTextNode("" + this.hours));
            reoccuranceElement.appendChild(hoursElement);

            Element minutesElement = document.createElement("MINUTES");
            minutesElement.appendChild(document.createTextNode("" + this.minutes));
            reoccuranceElement.appendChild(minutesElement);

            Element secondsElement = document.createElement("SECONDS");
            secondsElement.appendChild(document.createTextNode("" + this.seconds));
            reoccuranceElement.appendChild(secondsElement);
        }
        timePeriodElement.appendChild(reoccuranceElement);

        return timePeriodElement;
    }
}
