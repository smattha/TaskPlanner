//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 01:40:46 PM EEST 
//

package planning.model.impact.jaxb.planning.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}DAY"/>
 *         &lt;element ref="{}MONTH"/>
 *         &lt;element ref="{}YEAR"/>
 *         &lt;element ref="{}HOUR" minOccurs="0"/>
 *         &lt;element ref="{}MINUTES" minOccurs="0"/>
 *         &lt;element ref="{}SECONDS" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "day", "month", "year", "hour", "minutes", "seconds" })
@XmlRootElement(name = "STARTDATE")
public class STARTDATE {

	@XmlElement(name = "DAY")
	protected int day;
	@XmlElement(name = "MONTH")
	protected int month;
	@XmlElement(name = "YEAR")
	protected int year;
	@XmlElement(name = "HOUR")
	protected Integer hour;
	@XmlElement(name = "MINUTES")
	protected Integer minutes;
	@XmlElement(name = "SECONDS")
	protected Integer seconds;

	/**
	 * Gets the value of the day property.
	 * 
	 */
	public int getDAY() {
		return day;
	}

	/**
	 * Sets the value of the day property.
	 * 
	 */
	public void setDAY(int value) {
		this.day = value;
	}

	/**
	 * Gets the value of the month property.
	 * 
	 */
	public int getMONTH() {
		return month;
	}

	/**
	 * Sets the value of the month property.
	 * 
	 */
	public void setMONTH(int value) {
		this.month = value;
	}

	/**
	 * Gets the value of the year property.
	 * 
	 */
	public int getYEAR() {
		return year;
	}

	/**
	 * Sets the value of the year property.
	 * 
	 */
	public void setYEAR(int value) {
		this.year = value;
	}

	/**
	 * Gets the value of the hour property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getHOUR() {
		return hour;
	}

	/**
	 * Sets the value of the hour property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setHOUR(Integer value) {
		this.hour = value;
	}

	/**
	 * Gets the value of the minutes property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getMINUTES() {
		return minutes;
	}

	/**
	 * Sets the value of the minutes property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setMINUTES(Integer value) {
		this.minutes = value;
	}

	/**
	 * Gets the value of the seconds property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getSECONDS() {
		return seconds;
	}

	/**
	 * Sets the value of the seconds property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setSECONDS(Integer value) {
		this.seconds = value;
	}

}
