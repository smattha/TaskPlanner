package planning.model.io;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public abstract class AbstractInputSource {
	public abstract Document fetchPlanningInputXMLDocument()
			throws MalformedURLException, IOException, ParserConfigurationException, SAXException;

	public abstract Document fetchPlanningOutputXMLDocument()
			throws MalformedURLException, IOException, ParserConfigurationException, SAXException;

	public abstract AbstractOutputSource getOutputSource();
}
