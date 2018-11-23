package planning.model.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class FileOutputSource extends AbstractOutputSource
{
    private  String planningInputFilePath = null;
    private  String planningOutputFilePath = null;

    protected  void setUrls(String newPlanningInputFilePath, String newPlanningOutputFilePath)
    {
        this.planningInputFilePath = newPlanningInputFilePath;
        this.planningOutputFilePath = newPlanningOutputFilePath;
    }

    public void savePlanningInputXMLDocument(Node planningInputXMLDocument) throws java.net.MalformedURLException, java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException
    {
        File planningInputFile = new File(planningInputFilePath);
        FileOutputStream fos = new FileOutputStream(planningInputFile);
        Writer documentWriter = new Writer();
        documentWriter.setOutput(fos, "UTF-8");
        documentWriter.write(planningInputXMLDocument);
        fos.flush();
        fos.close();
    }

    public void savePlanningOutputXMLDocument(Document planningOutputXMLDocument) throws java.net.MalformedURLException, java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException
    {
        File planningOutputFile = new File(planningOutputFilePath);
//        FileOutputStream fos = new FileOutputStream(planningOutputFile);

//        Writer documentWriter = new Writer();
//        documentWriter.setOutput(fos, "UTF-8");
//        documentWriter.setCanonical(true);
//        documentWriter.write(planningOutputXMLDocument);

        // Pretty-prints a DOM document to XML using DOM Load and Save's LSSerializer.
        // Note that the "format-pretty-print" DOM configuration parameter can only be set in JDK 1.6+.
        DOMImplementation domImplementation = planningOutputXMLDocument.getImplementation();
        if (domImplementation.hasFeature("LS", "3.0") && domImplementation.hasFeature("Core", "2.0")) {
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS", "3.0");
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
            if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
                lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
                LSOutput lsOutput = domImplementationLS.createLSOutput();
                lsOutput.setEncoding("UTF-8");
//                StringWriter stringWriter = new StringWriter();
                FileWriter fw = new FileWriter(planningOutputFile);
                lsOutput.setCharacterStream(fw);
                lsSerializer.write(planningOutputXMLDocument, lsOutput);
                fw.flush();
                fw.close();
//                return stringWriter.toString();
            } else {
                throw new RuntimeException("DOMConfiguration 'format-pretty-print' parameter isn't settable.");
            }
        } else {
            throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
        }

//        fos.flush();
//        fos.close();
    }
}
