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
 *         &lt;element ref="{}FROM_DATE"/>
 *         &lt;element ref="{}TO_DATE"/>
 *         &lt;element ref="{}REOCCURANCE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fromdate", "todate", "reoccurance" })
@XmlRootElement(name = "PERIOD")
public class PERIOD {

	@XmlElement(name = "FROM_DATE", required = true)
	protected DATE fromdate;
	@XmlElement(name = "TO_DATE", required = true)
	protected DATE todate;
	@XmlElement(name = "REOCCURANCE", required = true)
	protected REOCCURANCE reoccurance;

	/**
	 * Gets the value of the fromdate property.
	 * 
	 * @return possible object is {@link DATE }
	 * 
	 */
	public DATE getFROMDATE() {
		return fromdate;
	}

	/**
	 * Sets the value of the fromdate property.
	 * 
	 * @param value allowed object is {@link DATE }
	 * 
	 */
	public void setFROMDATE(DATE value) {
		this.fromdate = value;
	}

	/**
	 * Gets the value of the todate property.
	 * 
	 * @return possible object is {@link DATE }
	 * 
	 */
	public DATE getTODATE() {
		return todate;
	}

	/**
	 * Sets the value of the todate property.
	 * 
	 * @param value allowed object is {@link DATE }
	 * 
	 */
	public void setTODATE(DATE value) {
		this.todate = value;
	}

	/**
	 * Gets the value of the reoccurance property.
	 * 
	 * @return possible object is {@link REOCCURANCE }
	 * 
	 */
	public REOCCURANCE getREOCCURANCE() {
		return reoccurance;
	}

	/**
	 * Sets the value of the reoccurance property.
	 * 
	 * @param value allowed object is {@link REOCCURANCE }
	 * 
	 */
	public void setREOCCURANCE(REOCCURANCE value) {
		this.reoccurance = value;
	}

}
