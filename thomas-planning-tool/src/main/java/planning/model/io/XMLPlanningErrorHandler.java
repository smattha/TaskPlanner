package planning.model.io;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

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

public class XMLPlanningErrorHandler extends DefaultHandler {
	public int errors_sum = 0;
	public int warnings_sum = 0;
	public int fatalErrors_sum = 0;

	public XMLPlanningErrorHandler() {
	}

	// Receive notification of a recoverable error.
	public void error(SAXParseException exception) throws SAXException {
		System.out.println("Error Line=" + exception.getLineNumber() + " :" + exception.getMessage());
		errors_sum++;
	}

	// Receive notification of a non-recoverable error.
	public void fatalError(SAXParseException exception) throws SAXException {
		System.out.println("Fatal Error Line=" + exception.getLineNumber() + " :" + exception.getMessage());
		fatalErrors_sum++;
	}

	// Receive notification of a warning.
	public void warning(SAXParseException exception) throws SAXException {
		System.out.println("Warning Line=" + exception.getLineNumber() + " :" + exception.getMessage());
		warnings_sum++;
	}
}