package planning.model.io;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class AbstractOutputSource
{
    public AbstractOutputSource()
    {
    }

    public abstract void savePlanningInputXMLDocument(Node planningInputXMLDocument) throws MalformedURLException, IOException, ParserConfigurationException, SAXException;

    public abstract void savePlanningOutputXMLDocument(Document planningOutputXMLDocument) throws MalformedURLException, IOException, ParserConfigurationException, SAXException;
}