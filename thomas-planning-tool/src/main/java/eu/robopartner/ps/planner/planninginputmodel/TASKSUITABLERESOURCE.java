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
 *         &lt;choice>
 *           &lt;element ref="{}RESOURCE_REFERENCE"/>
 *           &lt;element ref="{}TOOL_REFERENCE"/>
 *           &lt;element ref="{}MOBILE_RESOURCE_REFERENCE"/>
 *         &lt;/choice>
 *         &lt;element ref="{}TASK_REFERENCE"/>
 *         &lt;element ref="{}OPERATION_TIME_PER_BATCH_IN_SECONDS"/>
 *         &lt;element ref="{}SET_UP_CODE"/>
 *         &lt;element ref="{}PROPERTIES" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "resourcereference", "toolreference", "mobileresourcereference", "taskreference",
		"operationtimeperbatchinseconds", "setupcode", "properties" })
@XmlRootElement(name = "TASK_SUITABLE_RESOURCE")
public class TASKSUITABLERESOURCE {

	@XmlElement(name = "RESOURCE_REFERENCE")
	protected RESOURCEREFERENCE resourcereference;
	@XmlElement(name = "TOOL_REFERENCE")
	protected TOOLREFERENCE toolreference;
	@XmlElement(name = "MOBILE_RESOURCE_REFERENCE")
	protected MOBILERESOURCEREFERENCE mobileresourcereference;
	@XmlElement(name = "TASK_REFERENCE", required = true)
	protected TASKREFERENCE taskreference;
	@XmlElement(name = "OPERATION_TIME_PER_BATCH_IN_SECONDS")
	protected int operationtimeperbatchinseconds;
	@XmlElement(name = "SET_UP_CODE", required = true)
	protected String setupcode;
	@XmlElement(name = "PROPERTIES")
	protected PROPERTIES properties;

	/**
	 * Gets the value of the resourcereference property.
	 * 
	 * @return possible object is {@link RESOURCEREFERENCE }
	 * 
	 */
	public RESOURCEREFERENCE getRESOURCEREFERENCE() {
		return resourcereference;
	}

	/**
	 * Sets the value of the resourcereference property.
	 * 
	 * @param value allowed object is {@link RESOURCEREFERENCE }
	 * 
	 */
	public void setRESOURCEREFERENCE(RESOURCEREFERENCE value) {
		this.resourcereference = value;
	}

	/**
	 * Gets the value of the toolreference property.
	 * 
	 * @return possible object is {@link TOOLREFERENCE }
	 * 
	 */
	public TOOLREFERENCE getTOOLREFERENCE() {
		return toolreference;
	}

	/**
	 * Sets the value of the toolreference property.
	 * 
	 * @param value allowed object is {@link TOOLREFERENCE }
	 * 
	 */
	public void setTOOLREFERENCE(TOOLREFERENCE value) {
		this.toolreference = value;
	}

	/**
	 * Gets the value of the mobileresourcereference property.
	 * 
	 * @return possible object is {@link MOBILERESOURCEREFERENCE }
	 * 
	 */
	public MOBILERESOURCEREFERENCE getMOBILERESOURCEREFERENCE() {
		return mobileresourcereference;
	}

	/**
	 * Sets the value of the mobileresourcereference property.
	 * 
	 * @param value allowed object is {@link MOBILERESOURCEREFERENCE }
	 * 
	 */
	public void setMOBILERESOURCEREFERENCE(MOBILERESOURCEREFERENCE value) {
		this.mobileresourcereference = value;
	}

	/**
	 * Gets the value of the taskreference property.
	 * 
	 * @return possible object is {@link TASKREFERENCE }
	 * 
	 */
	public TASKREFERENCE getTASKREFERENCE() {
		return taskreference;
	}

	/**
	 * Sets the value of the taskreference property.
	 * 
	 * @param value allowed object is {@link TASKREFERENCE }
	 * 
	 */
	public void setTASKREFERENCE(TASKREFERENCE value) {
		this.taskreference = value;
	}

	/**
	 * Gets the value of the operationtimeperbatchinseconds property.
	 * 
	 */
	public int getOPERATIONTIMEPERBATCHINSECONDS() {
		return operationtimeperbatchinseconds;
	}

	/**
	 * Sets the value of the operationtimeperbatchinseconds property.
	 * 
	 */
	public void setOPERATIONTIMEPERBATCHINSECONDS(int value) {
		this.operationtimeperbatchinseconds = value;
	}

	/**
	 * Gets the value of the setupcode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSETUPCODE() {
		return setupcode;
	}

	/**
	 * Sets the value of the setupcode property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSETUPCODE(String value) {
		this.setupcode = value;
	}

	/**
	 * Gets the value of the properties property.
	 * 
	 * @return possible object is {@link PROPERTIES }
	 * 
	 */
	public PROPERTIES getPROPERTIES() {
		return properties;
	}

	/**
	 * Sets the value of the properties property.
	 * 
	 * @param value allowed object is {@link PROPERTIES }
	 * 
	 */
	public void setPROPERTIES(PROPERTIES value) {
		this.properties = value;
	}

}
