//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.12 at 02:45:59 PM EEST 
//


package eu.robopartner.ps.planner.planninginputmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DATE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DATE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}DAY"/>
 *         &lt;element ref="{}MONTH"/>
 *         &lt;element ref="{}YEAR"/>
 *         &lt;element ref="{}HOUR"/>
 *         &lt;element ref="{}MINUTE"/>
 *         &lt;element ref="{}SECOND"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATE", propOrder = {
    "day",
    "month",
    "year",
    "hour",
    "minute",
    "second"
})
public class DATE {

    @XmlElement(name = "DAY")
    protected int day;
    @XmlElement(name = "MONTH")
    protected int month;
    @XmlElement(name = "YEAR")
    protected int year;
    @XmlElement(name = "HOUR")
    protected int hour;
    @XmlElement(name = "MINUTE")
    protected int minute;
    @XmlElement(name = "SECOND")
    protected int second;

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
     */
    public int getHOUR() {
        return hour;
    }

    /**
     * Sets the value of the hour property.
     * 
     */
    public void setHOUR(int value) {
        this.hour = value;
    }

    /**
     * Gets the value of the minute property.
     * 
     */
    public int getMINUTE() {
        return minute;
    }

    /**
     * Sets the value of the minute property.
     * 
     */
    public void setMINUTE(int value) {
        this.minute = value;
    }

    /**
     * Gets the value of the second property.
     * 
     */
    public int getSECOND() {
        return second;
    }

    /**
     * Sets the value of the second property.
     * 
     */
    public void setSECOND(int value) {
        this.second = value;
    }

}