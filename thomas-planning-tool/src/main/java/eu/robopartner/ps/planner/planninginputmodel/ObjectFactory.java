//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.12 at 02:45:59 PM EEST 
//


package eu.robopartner.ps.planner.planninginputmodel;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.robopartner.ps.planner.planninginputmodel package. 
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

    private final static QName _FROMCODE_QNAME = new QName("", "FROM_CODE");
    private final static QName _FROMDATE_QNAME = new QName("", "FROM_DATE");
    private final static QName _MONTH_QNAME = new QName("", "MONTH");
    private final static QName _TOCODE_QNAME = new QName("", "TO_CODE");
    private final static QName _HOUR_QNAME = new QName("", "HOUR");
    private final static QName _TODATE_QNAME = new QName("", "TO_DATE");
    private final static QName _ALGORITHM_QNAME = new QName("", "ALGORITHM");
    private final static QName _DAYS_QNAME = new QName("", "DAYS");
    private final static QName _RESOURCEUNAVAILABLEUNTILNEXTTASK_QNAME = new QName("", "RESOURCE_UNAVAILABLE_UNTIL_NEXT_TASK");
    private final static QName _VALUE_QNAME = new QName("", "VALUE");
    private final static QName _TIMEINSECONDS_QNAME = new QName("", "TIME_IN_SECONDS");
    private final static QName _MONTHS_QNAME = new QName("", "MONTHS");
    private final static QName _SETUPCODE_QNAME = new QName("", "SET_UP_CODE");
    private final static QName _YEAR_QNAME = new QName("", "YEAR");
    private final static QName _DESCRIPTION_QNAME = new QName("", "DESCRIPTION");
    private final static QName _MINUTE_QNAME = new QName("", "MINUTE");
    private final static QName _ARRIVALDATE_QNAME = new QName("", "ARRIVAL_DATE");
    private final static QName _DUEDATE_QNAME = new QName("", "DUE_DATE");
    private final static QName _OPERATIONTIMEPERBATCHINSECONDS_QNAME = new QName("", "OPERATION_TIME_PER_BATCH_IN_SECONDS");
    private final static QName _SECONDS_QNAME = new QName("", "SECONDS");
    private final static QName _NEXTTASKINCHAIN_QNAME = new QName("", "NEXT_TASK_IN_CHAIN");
    private final static QName _NAME_QNAME = new QName("", "NAME");
    private final static QName _YEARS_QNAME = new QName("", "YEARS");
    private final static QName _HOURS_QNAME = new QName("", "HOURS");
    private final static QName _WEEKS_QNAME = new QName("", "WEEKS");
    private final static QName _SECOND_QNAME = new QName("", "SECOND");
    private final static QName _MINUTES_QNAME = new QName("", "MINUTES");
    private final static QName _DAY_QNAME = new QName("", "DAY");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.robopartner.ps.planner.planninginputmodel
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DOCKINGSTATION }
     * 
     */
    public DOCKINGSTATION createDOCKINGSTATION() {
        return new DOCKINGSTATION();
    }

    /**
     * Create an instance of {@link TOOLPLACEMENT }
     * 
     */
    public TOOLPLACEMENT createTOOLPLACEMENT() {
        return new TOOLPLACEMENT();
    }

    /**
     * Create an instance of {@link TOOLPLACEMENT.SUPPORTEDTOOLTYPES }
     * 
     */
    public TOOLPLACEMENT.SUPPORTEDTOOLTYPES createTOOLPLACEMENTSUPPORTEDTOOLTYPES() {
        return new TOOLPLACEMENT.SUPPORTEDTOOLTYPES();
    }

    /**
     * Create an instance of {@link DATE }
     * 
     */
    public DATE createDATE() {
        return new DATE();
    }

    /**
     * Create an instance of {@link TOOLTYPES }
     * 
     */
    public TOOLTYPES createTOOLTYPES() {
        return new TOOLTYPES();
    }

    /**
     * Create an instance of {@link TOOLTYPE }
     * 
     */
    public TOOLTYPE createTOOLTYPE() {
        return new TOOLTYPE();
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
     * Create an instance of {@link SETUPMATRIXREFERENCE }
     * 
     */
    public SETUPMATRIXREFERENCE createSETUPMATRIXREFERENCE() {
        return new SETUPMATRIXREFERENCE();
    }

    /**
     * Create an instance of {@link MOBILERESOURCES }
     * 
     */
    public MOBILERESOURCES createMOBILERESOURCES() {
        return new MOBILERESOURCES();
    }

    /**
     * Create an instance of {@link MOBILERESOURCE }
     * 
     */
    public MOBILERESOURCE createMOBILERESOURCE() {
        return new MOBILERESOURCE();
    }

    /**
     * Create an instance of {@link RESOURCEAVAILABILITY }
     * 
     */
    public RESOURCEAVAILABILITY createRESOURCEAVAILABILITY() {
        return new RESOURCEAVAILABILITY();
    }

    /**
     * Create an instance of {@link NONWORKINGPERIODS }
     * 
     */
    public NONWORKINGPERIODS createNONWORKINGPERIODS() {
        return new NONWORKINGPERIODS();
    }

    /**
     * Create an instance of {@link PERIOD }
     * 
     */
    public PERIOD createPERIOD() {
        return new PERIOD();
    }

    /**
     * Create an instance of {@link REOCCURANCE }
     * 
     */
    public REOCCURANCE createREOCCURANCE() {
        return new REOCCURANCE();
    }

    /**
     * Create an instance of {@link FORCEDWORKINGPERIODS }
     * 
     */
    public FORCEDWORKINGPERIODS createFORCEDWORKINGPERIODS() {
        return new FORCEDWORKINGPERIODS();
    }

    /**
     * Create an instance of {@link MOBILERESOURCETYPEREFERENCE }
     * 
     */
    public MOBILERESOURCETYPEREFERENCE createMOBILERESOURCETYPEREFERENCE() {
        return new MOBILERESOURCETYPEREFERENCE();
    }

    /**
     * Create an instance of {@link TASKPRECEDENCECONSTRAINTS }
     * 
     */
    public TASKPRECEDENCECONSTRAINTS createTASKPRECEDENCECONSTRAINTS() {
        return new TASKPRECEDENCECONSTRAINTS();
    }

    /**
     * Create an instance of {@link TASKPRECEDENCECONSTRAINT }
     * 
     */
    public TASKPRECEDENCECONSTRAINT createTASKPRECEDENCECONSTRAINT() {
        return new TASKPRECEDENCECONSTRAINT();
    }

    /**
     * Create an instance of {@link PRECONDITIONTASKREFERENCE }
     * 
     */
    public PRECONDITIONTASKREFERENCE createPRECONDITIONTASKREFERENCE() {
        return new PRECONDITIONTASKREFERENCE();
    }

    /**
     * Create an instance of {@link POSTCONDITIONTASKREFERENCE }
     * 
     */
    public POSTCONDITIONTASKREFERENCE createPOSTCONDITIONTASKREFERENCE() {
        return new POSTCONDITIONTASKREFERENCE();
    }

    /**
     * Create an instance of {@link JOB }
     * 
     */
    public JOB createJOB() {
        return new JOB();
    }

    /**
     * Create an instance of {@link JOBTASKREFERENCE }
     * 
     */
    public JOBTASKREFERENCE createJOBTASKREFERENCE() {
        return new JOBTASKREFERENCE();
    }

    /**
     * Create an instance of {@link JOBWORKCENTERREFERENCE }
     * 
     */
    public JOBWORKCENTERREFERENCE createJOBWORKCENTERREFERENCE() {
        return new JOBWORKCENTERREFERENCE();
    }

    /**
     * Create an instance of {@link MOBILERESOURCETYPE }
     * 
     */
    public MOBILERESOURCETYPE createMOBILERESOURCETYPE() {
        return new MOBILERESOURCETYPE();
    }

    /**
     * Create an instance of {@link TASK }
     * 
     */
    public TASK createTASK() {
        return new TASK();
    }

    /**
     * Create an instance of {@link TASKSUITABLERESOURCE }
     * 
     */
    public TASKSUITABLERESOURCE createTASKSUITABLERESOURCE() {
        return new TASKSUITABLERESOURCE();
    }

    /**
     * Create an instance of {@link RESOURCEREFERENCE }
     * 
     */
    public RESOURCEREFERENCE createRESOURCEREFERENCE() {
        return new RESOURCEREFERENCE();
    }

    /**
     * Create an instance of {@link TOOLREFERENCE }
     * 
     */
    public TOOLREFERENCE createTOOLREFERENCE() {
        return new TOOLREFERENCE();
    }

    /**
     * Create an instance of {@link MOBILERESOURCEREFERENCE }
     * 
     */
    public MOBILERESOURCEREFERENCE createMOBILERESOURCEREFERENCE() {
        return new MOBILERESOURCEREFERENCE();
    }

    /**
     * Create an instance of {@link TASKREFERENCE }
     * 
     */
    public TASKREFERENCE createTASKREFERENCE() {
        return new TASKREFERENCE();
    }

    /**
     * Create an instance of {@link SETUP }
     * 
     */
    public SETUP createSETUP() {
        return new SETUP();
    }

    /**
     * Create an instance of {@link TOOLTYPEREFERENCE }
     * 
     */
    public TOOLTYPEREFERENCE createTOOLTYPEREFERENCE() {
        return new TOOLTYPEREFERENCE();
    }

    /**
     * Create an instance of {@link TOOL }
     * 
     */
    public TOOL createTOOL() {
        return new TOOL();
    }

    /**
     * Create an instance of {@link SETUPMATRICES }
     * 
     */
    public SETUPMATRICES createSETUPMATRICES() {
        return new SETUPMATRICES();
    }

    /**
     * Create an instance of {@link SETUPMATRIX }
     * 
     */
    public SETUPMATRIX createSETUPMATRIX() {
        return new SETUPMATRIX();
    }

    /**
     * Create an instance of {@link WORKCENTER }
     * 
     */
    public WORKCENTER createWORKCENTER() {
        return new WORKCENTER();
    }

    /**
     * Create an instance of {@link WORKCENTERRESOURCEREFERENCE }
     * 
     */
    public WORKCENTERRESOURCEREFERENCE createWORKCENTERRESOURCEREFERENCE() {
        return new WORKCENTERRESOURCEREFERENCE();
    }

    /**
     * Create an instance of {@link DOCKINGSTATIONS }
     * 
     */
    public DOCKINGSTATIONS createDOCKINGSTATIONS() {
        return new DOCKINGSTATIONS();
    }

    /**
     * Create an instance of {@link DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES }
     * 
     */
    public DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES createDOCKINGSTATIONSUPPORTEDMOBILERESOURCESTYPES() {
        return new DOCKINGSTATION.SUPPORTEDMOBILERESOURCESTYPES();
    }

    /**
     * Create an instance of {@link DOCKINGSTATION.CURRENTLOAD }
     * 
     */
    public DOCKINGSTATION.CURRENTLOAD createDOCKINGSTATIONCURRENTLOAD() {
        return new DOCKINGSTATION.CURRENTLOAD();
    }

    /**
     * Create an instance of {@link TASKS }
     * 
     */
    public TASKS createTASKS() {
        return new TASKS();
    }

    /**
     * Create an instance of {@link WORKCENTERS }
     * 
     */
    public WORKCENTERS createWORKCENTERS() {
        return new WORKCENTERS();
    }

    /**
     * Create an instance of {@link PLANNINGINPUT }
     * 
     */
    public PLANNINGINPUT createPLANNINGINPUT() {
        return new PLANNINGINPUT();
    }

    /**
     * Create an instance of {@link RESOURCES }
     * 
     */
    public RESOURCES createRESOURCES() {
        return new RESOURCES();
    }

    /**
     * Create an instance of {@link RESOURCE }
     * 
     */
    public RESOURCE createRESOURCE() {
        return new RESOURCE();
    }

    /**
     * Create an instance of {@link JOBS }
     * 
     */
    public JOBS createJOBS() {
        return new JOBS();
    }

    /**
     * Create an instance of {@link TASKSUITABLERESOURCES }
     * 
     */
    public TASKSUITABLERESOURCES createTASKSUITABLERESOURCES() {
        return new TASKSUITABLERESOURCES();
    }

    /**
     * Create an instance of {@link TOOLS }
     * 
     */
    public TOOLS createTOOLS() {
        return new TOOLS();
    }

    /**
     * Create an instance of {@link MOBILERESOURCETYPES }
     * 
     */
    public MOBILERESOURCETYPES createMOBILERESOURCETYPES() {
        return new MOBILERESOURCETYPES();
    }

    /**
     * Create an instance of {@link TOOLPLACEMENT.CURRENTLOAD }
     * 
     */
    public TOOLPLACEMENT.CURRENTLOAD createTOOLPLACEMENTCURRENTLOAD() {
        return new TOOLPLACEMENT.CURRENTLOAD();
    }

    /**
     * Create an instance of {@link TOOLPLACEMENT.SUPPORTEDTOOLTYPES.SUPPORTEDTOOLTYPE }
     * 
     */
    public TOOLPLACEMENT.SUPPORTEDTOOLTYPES.SUPPORTEDTOOLTYPE createTOOLPLACEMENTSUPPORTEDTOOLTYPESSUPPORTEDTOOLTYPE() {
        return new TOOLPLACEMENT.SUPPORTEDTOOLTYPES.SUPPORTEDTOOLTYPE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FROM_CODE")
    public JAXBElement<String> createFROMCODE(String value) {
        return new JAXBElement<String>(_FROMCODE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DATE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FROM_DATE")
    public JAXBElement<DATE> createFROMDATE(DATE value) {
        return new JAXBElement<DATE>(_FROMDATE_QNAME, DATE.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TO_CODE")
    public JAXBElement<String> createTOCODE(String value) {
        return new JAXBElement<String>(_TOCODE_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link DATE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TO_DATE")
    public JAXBElement<DATE> createTODATE(DATE value) {
        return new JAXBElement<DATE>(_TODATE_QNAME, DATE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ALGORITHM")
    public JAXBElement<String> createALGORITHM(String value) {
        return new JAXBElement<String>(_ALGORITHM_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DAYS")
    public JAXBElement<Integer> createDAYS(Integer value) {
        return new JAXBElement<Integer>(_DAYS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RESOURCE_UNAVAILABLE_UNTIL_NEXT_TASK")
    public JAXBElement<Boolean> createRESOURCEUNAVAILABLEUNTILNEXTTASK(Boolean value) {
        return new JAXBElement<Boolean>(_RESOURCEUNAVAILABLEUNTILNEXTTASK_QNAME, Boolean.class, null, value);
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
    @XmlElementDecl(namespace = "", name = "TIME_IN_SECONDS")
    public JAXBElement<Integer> createTIMEINSECONDS(Integer value) {
        return new JAXBElement<Integer>(_TIMEINSECONDS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MONTHS")
    public JAXBElement<Integer> createMONTHS(Integer value) {
        return new JAXBElement<Integer>(_MONTHS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SET_UP_CODE")
    public JAXBElement<String> createSETUPCODE(String value) {
        return new JAXBElement<String>(_SETUPCODE_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DESCRIPTION")
    public JAXBElement<String> createDESCRIPTION(String value) {
        return new JAXBElement<String>(_DESCRIPTION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MINUTE")
    public JAXBElement<Integer> createMINUTE(Integer value) {
        return new JAXBElement<Integer>(_MINUTE_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DATE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ARRIVAL_DATE")
    public JAXBElement<DATE> createARRIVALDATE(DATE value) {
        return new JAXBElement<DATE>(_ARRIVALDATE_QNAME, DATE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DATE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DUE_DATE")
    public JAXBElement<DATE> createDUEDATE(DATE value) {
        return new JAXBElement<DATE>(_DUEDATE_QNAME, DATE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "OPERATION_TIME_PER_BATCH_IN_SECONDS")
    public JAXBElement<Integer> createOPERATIONTIMEPERBATCHINSECONDS(Integer value) {
        return new JAXBElement<Integer>(_OPERATIONTIMEPERBATCHINSECONDS_QNAME, Integer.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "NEXT_TASK_IN_CHAIN")
    public JAXBElement<Boolean> createNEXTTASKINCHAIN(Boolean value) {
        return new JAXBElement<Boolean>(_NEXTTASKINCHAIN_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "NAME")
    public JAXBElement<String> createNAME(String value) {
        return new JAXBElement<String>(_NAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "YEARS")
    public JAXBElement<Integer> createYEARS(Integer value) {
        return new JAXBElement<Integer>(_YEARS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "HOURS")
    public JAXBElement<Integer> createHOURS(Integer value) {
        return new JAXBElement<Integer>(_HOURS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "WEEKS")
    public JAXBElement<Integer> createWEEKS(Integer value) {
        return new JAXBElement<Integer>(_WEEKS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SECOND")
    public JAXBElement<Integer> createSECOND(Integer value) {
        return new JAXBElement<Integer>(_SECOND_QNAME, Integer.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DAY")
    public JAXBElement<Integer> createDAY(Integer value) {
        return new JAXBElement<Integer>(_DAY_QNAME, Integer.class, null, value);
    }

}