package planning.model.io;

import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

public class XMLUtil {
	private XMLUtil() {
	}

	/**
	 * Reads the parameter nodelist and returns a Calendar object containing the
	 * date read in the nodelist.
	 *
	 * @param nodeList
	 * @return Calendar
	 */
	public static Calendar getCalendarDateFromXMLNodeList(NodeList nodeList) {
		int year = 0;
		int day = 0;
		int month = 0;
		int minutes = 0;
		int hours = 0;
		int seconds = 0;

//System.out.println("--------------------------------");
		for (int j = 0; j < nodeList.getLength(); j++) {
			if (!nodeList.item(j).getNodeName().equals("#text")) {
//System.out.println("\tNode : ->"+nodeList.item(j).getNodeName()+"<- contains value : ->"+nodeList.item(j).getLastChild().getNodeValue()+"<-");
				if (nodeList.item(j).getNodeName().equals("DAY")) {
					day = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				} else if (nodeList.item(j).getNodeName().equals("MONTH")) {
					month = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				} else if (nodeList.item(j).getNodeName().equals("YEAR")) {
					year = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				} else if (nodeList.item(j).getNodeName().equals("HOUR")) {
					hours = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				} else if (nodeList.item(j).getNodeName().equals("MINUTE")) {
					minutes = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				} else if (nodeList.item(j).getNodeName().equals("SECOND")) {
					seconds = Integer.parseInt(nodeList.item(j).getLastChild().getNodeValue());
				}
			}
		}
		Calendar date = getCalendar(month, day, year);
		date.set(Calendar.HOUR_OF_DAY, hours);
		date.set(Calendar.MINUTE, minutes);
		date.set(Calendar.SECOND, seconds);
		return date;
	}

	/***/
	private static Calendar initCalendar = Calendar.getInstance();

	/**
	 * Little helper function to make it easier to create calendars.
	 *
	 * @param month
	 * @param day
	 * @param year
	 */
	private static Calendar getCalendar(int month, int day, int year) {
		Calendar c = (Calendar) initCalendar.clone();
		c.set(Calendar.MILLISECOND, 0);
		c.set(year, month - 1, day, 0, 0, 0);
		return c;
	}

	public static Node getNodeFromCalendar(Calendar calendarDate, Document document) {
		DocumentFragment containerDocumentFragment = document.createDocumentFragment();

		Element day = document.createElement("DAY");
		day.appendChild(document.createTextNode("" + calendarDate.get(Calendar.DAY_OF_MONTH)));
		containerDocumentFragment.appendChild(day);

		Element month = document.createElement("MONTH");
		month.appendChild(document.createTextNode("" + (calendarDate.get(Calendar.MONTH) + 1)));
		containerDocumentFragment.appendChild(month);

		Element year = document.createElement("YEAR");
		year.appendChild(document.createTextNode("" + calendarDate.get(Calendar.YEAR)));
		containerDocumentFragment.appendChild(year);

		Element hour = document.createElement("HOUR");
		hour.appendChild(document.createTextNode("" + calendarDate.get(Calendar.HOUR_OF_DAY)));
		containerDocumentFragment.appendChild(hour);

		Element minutes = document.createElement("MINUTE");
		minutes.appendChild(document.createTextNode("" + calendarDate.get(Calendar.MINUTE)));
		containerDocumentFragment.appendChild(minutes);

		Element seconds = document.createElement("SECOND");
		seconds.appendChild(document.createTextNode("" + calendarDate.get(Calendar.SECOND)));
		containerDocumentFragment.appendChild(seconds);

		return containerDocumentFragment;
	}
}