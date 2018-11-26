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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}PRECONDITION_TASK_REFERENCE"/>
 *         &lt;element ref="{}POSTCONDITION_TASK_REFERENCE"/>
 *         &lt;element ref="{}NEXT_TASK_IN_CHAIN" minOccurs="0"/>
 *         &lt;element ref="{}RESOURCE_UNAVAILABLE_UNTIL_NEXT_TASK" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "preconditiontaskreference",
    "postconditiontaskreference",
    "nexttaskinchain",
    "resourceunavailableuntilnexttask"
})
@XmlRootElement(name = "TASK_PRECEDENCE_CONSTRAINT")
public class TASKPRECEDENCECONSTRAINT {

    @XmlElement(name = "PRECONDITION_TASK_REFERENCE", required = true)
    protected PRECONDITIONTASKREFERENCE preconditiontaskreference;
    @XmlElement(name = "POSTCONDITION_TASK_REFERENCE", required = true)
    protected POSTCONDITIONTASKREFERENCE postconditiontaskreference;
    @XmlElement(name = "NEXT_TASK_IN_CHAIN")
    protected Boolean nexttaskinchain;
    @XmlElement(name = "RESOURCE_UNAVAILABLE_UNTIL_NEXT_TASK")
    protected Boolean resourceunavailableuntilnexttask;

    /**
     * Gets the value of the preconditiontaskreference property.
     * 
     * @return
     *     possible object is
     *     {@link PRECONDITIONTASKREFERENCE }
     *     
     */
    public PRECONDITIONTASKREFERENCE getPRECONDITIONTASKREFERENCE() {
        return preconditiontaskreference;
    }

    /**
     * Sets the value of the preconditiontaskreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRECONDITIONTASKREFERENCE }
     *     
     */
    public void setPRECONDITIONTASKREFERENCE(PRECONDITIONTASKREFERENCE value) {
        this.preconditiontaskreference = value;
    }

    /**
     * Gets the value of the postconditiontaskreference property.
     * 
     * @return
     *     possible object is
     *     {@link POSTCONDITIONTASKREFERENCE }
     *     
     */
    public POSTCONDITIONTASKREFERENCE getPOSTCONDITIONTASKREFERENCE() {
        return postconditiontaskreference;
    }

    /**
     * Sets the value of the postconditiontaskreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link POSTCONDITIONTASKREFERENCE }
     *     
     */
    public void setPOSTCONDITIONTASKREFERENCE(POSTCONDITIONTASKREFERENCE value) {
        this.postconditiontaskreference = value;
    }

    /**
     * Gets the value of the nexttaskinchain property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNEXTTASKINCHAIN() {
        return nexttaskinchain;
    }

    /**
     * Sets the value of the nexttaskinchain property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNEXTTASKINCHAIN(Boolean value) {
        this.nexttaskinchain = value;
    }

    /**
     * Gets the value of the resourceunavailableuntilnexttask property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRESOURCEUNAVAILABLEUNTILNEXTTASK() {
        return resourceunavailableuntilnexttask;
    }

    /**
     * Sets the value of the resourceunavailableuntilnexttask property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRESOURCEUNAVAILABLEUNTILNEXTTASK(Boolean value) {
        this.resourceunavailableuntilnexttask = value;
    }

}