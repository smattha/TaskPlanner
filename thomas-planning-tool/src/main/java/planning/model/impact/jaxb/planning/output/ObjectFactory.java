//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.11 at 11:06:49 AM EEST 
//


package planning.model.impact.jaxb.planning.output;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MONTH_QNAME = new QName("", "MONTH");
    private final static QName _YEAR_QNAME = new QName("", "YEAR");
    private final static QName _HOUR_QNAME = new QName("", "HOUR");
    private final static QName _SECONDS_QNAME = new QName("", "SECONDS");
    private final static QName _MINUTES_QNAME = new QName("", "MINUTES");
    private final static QName _VALUE_QNAME = new QName("", "VALUE");
    private final static QName _DAY_QNAME = new QName("", "DAY");
    private final static QName _DURATIONINMILLISECONDS_QNAME = new QName("", "DURATION_IN_MILLISECONDS");
    private final static QName _NAME_QNAME = new QName("", "NAME");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PLANNINGOUTPUT }
     * 
     */
    public PLANNINGOUTPUT createPLANNINGOUTPUT() {
        return new PLANNINGOUTPUT();
    }

    /**
     * Create an instance of {@link ASSIGNMENTS }
     * 
     */
    public ASSIGNMENTS createASSIGNMENTS() {
        return new ASSIGNMENTS();
    }

    /**
     * Create an instance of {@link ASSIGNMENT }
     * 
     */
    public ASSIGNMENT createASSIGNMENT() {
        return new ASSIGNMENT();
    }

    /**
     * Create an instance of {@link TASK }
     * 
     */
    public TASK createTASK() {
        return new TASK();
    }

    /**
     * Create an instance of {@link RESOURCE }
     * 
     */
    public RESOURCE createRESOURCE() {
        return new RESOURCE();
    }

    /**
     * Create an instance of {@link TIMEOFDISPATCH }
     * 
     */
    public TIMEOFDISPATCH createTIMEOFDISPATCH() {
        return new TIMEOFDISPATCH();
    }

    /**
     * Create an instance of {@link PROPERTIES }
     * 
     */
    public PROPERTIES createPROPERTIES() {
        return new PROPERTIES();
    }

    /**
     * Create an instance of {@link PROPERTY }
     * 
     */
    public PROPERTY createPROPERTY() {
        return new PROPERTY();
    }

    /**
     * Create an instance of {@link STARTDATE }
     * 
     */
    public STARTDATE createSTARTDATE() {
        return new STARTDATE();
    }

    /**
     * Create an instance of {@link ENDDATE }
     * 
     */
    public ENDDATE createENDDATE() {
        return new ENDDATE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MONTH")
    public JAXBElement<Integer> createMONTH(Integer value) {
        return new JAXBElement<Integer>(_MONTH_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "YEAR")
    public JAXBElement<Integer> createYEAR(Integer value) {
        return new JAXBElement<Integer>(_YEAR_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "HOUR")
    public JAXBElement<Integer> createHOUR(Integer value) {
        return new JAXBElement<Integer>(_HOUR_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SECONDS")
    public JAXBElement<Integer> createSECONDS(Integer value) {
        return new JAXBElement<Integer>(_SECONDS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MINUTES")
    public JAXBElement<Integer> createMINUTES(Integer value) {
        return new JAXBElement<Integer>(_MINUTES_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "VALUE")
    public JAXBElement<String> createVALUE(String value) {
        return new JAXBElement<String>(_VALUE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DAY")
    public JAXBElement<Integer> createDAY(Integer value) {
        return new JAXBElement<Integer>(_DAY_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DURATION_IN_MILLISECONDS")
    public JAXBElement<Long> createDURATIONINMILLISECONDS(Long value) {
        return new JAXBElement<Long>(_DURATIONINMILLISECONDS_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "NAME")
    public JAXBElement<String> createNAME(String value) {
        return new JAXBElement<String>(_NAME_QNAME, String.class, null, value);
    }

}
