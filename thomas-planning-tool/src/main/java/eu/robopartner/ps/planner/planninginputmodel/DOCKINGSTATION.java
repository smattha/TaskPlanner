//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.12 at 02:45:59 PM EEST 
//

package eu.robopartner.ps.planner.planninginputmodel;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="SUPPORTED_MOBILE_RESOURCES_TYPES">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element ref="{}MOBILE_RESOURCE_TYPE_REFERENCE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CURRENT_LOAD">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence minOccurs="0">
 *                   &lt;element ref="{}MOBILE_RESOURCE_REFERENCE" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "supportedmobileresourcestypes", "currentload" })
@XmlRootElement(name = "DOCKING_STATION")
public class DOCKINGSTATION {

	@XmlElement(name = "SUPPORTED_MOBILE_RESOURCES_TYPES", required = true)
	protected DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES supportedmobileresourcestypes;
	@XmlElement(name = "CURRENT_LOAD", required = true)
	protected DOCKINGSTATION.CURRENTLOAD currentload;

	/**
	 * Gets the value of the supportedmobileresourcestypes property.
	 * 
	 * @return possible object is
	 *         {@link DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES }
	 * 
	 */
	public DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES getSUPPORTEDMOBILERESOURCESTYPES() {
		return supportedmobileresourcestypes;
	}

	/**
	 * Sets the value of the supportedmobileresourcestypes property.
	 * 
	 * @param value allowed object is
	 *              {@link DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES }
	 * 
	 */
	public void setSUPPORTEDMOBILERESOURCESTYPES(DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES value) {
		this.supportedmobileresourcestypes = value;
	}

	/**
	 * Gets the value of the currentload property.
	 * 
	 * @return possible object is {@link DOCKINGSTATION.CURRENTLOAD }
	 * 
	 */
	public DOCKINGSTATION.CURRENTLOAD getCURRENTLOAD() {
		return currentload;
	}

	/**
	 * Sets the value of the currentload property.
	 * 
	 * @param value allowed object is {@link DOCKINGSTATION.CURRENTLOAD }
	 * 
	 */
	public void setCURRENTLOAD(DOCKINGSTATION.CURRENTLOAD value) {
		this.currentload = value;
	}

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
	 *       &lt;sequence minOccurs="0">
	 *         &lt;element ref="{}MOBILE_RESOURCE_REFERENCE" minOccurs="0"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "mobileresourcereference" })
	public static class CURRENTLOAD {

		@XmlElement(name = "MOBILE_RESOURCE_REFERENCE")
		protected MOBILERESOURCEREFERENCE mobileresourcereference;

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

	}

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
	 *       &lt;sequence maxOccurs="unbounded">
	 *         &lt;element ref="{}MOBILE_RESOURCE_TYPE_REFERENCE" maxOccurs="unbounded"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "mobileresourcetypereference" })
	public static class SUPPORTEDMOBILERESOURCESTYPES {

		@XmlElement(name = "MOBILE_RESOURCE_TYPE_REFERENCE", required = true)
		protected List<MOBILERESOURCETYPEREFERENCE> mobileresourcetypereference;

		/**
		 * Gets the value of the mobileresourcetypereference property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a snapshot.
		 * Therefore any modification you make to the returned list will be present
		 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
		 * for the mobileresourcetypereference property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getMOBILERESOURCETYPEREFERENCE().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link MOBILERESOURCETYPEREFERENCE }
		 * 
		 * 
		 */
		public List<MOBILERESOURCETYPEREFERENCE> getMOBILERESOURCETYPEREFERENCE() {
			if (mobileresourcetypereference == null) {
				mobileresourcetypereference = new ArrayList<MOBILERESOURCETYPEREFERENCE>();
			}
			return this.mobileresourcetypereference;
		}

	}

}
