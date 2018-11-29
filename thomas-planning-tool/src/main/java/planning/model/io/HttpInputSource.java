package planning.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

public class HttpInputSource extends AbstractInputSource {
	private String webserverurl = "";
	private String port = "";
	private String uriPlanningInputLocation = "";
	private String parametersForInputXML = "";
	private String uriPlanningOutputLocation = "";
	private String parametersForOutputXML = "";
	private String protocol = "";

	public HttpInputSource(String webserverurl, String port, String uriPlanningInputLocation,
			String parametersForInputXML, String uriPlanningOutputLocation, String parametersForOutputXML,
			String protocol) {
		this.webserverurl = webserverurl;
		this.port = port;
		this.uriPlanningInputLocation = uriPlanningInputLocation;
		this.parametersForInputXML = parametersForInputXML;
		this.uriPlanningOutputLocation = uriPlanningOutputLocation;
		this.parametersForOutputXML = parametersForOutputXML;
		this.protocol = protocol;
	}

	public Document fetchPlanningInputXMLDocument()
			throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
		String connectionRequest = webserverurl + ":" + port + "" + uriPlanningInputLocation + "?"
				+ parametersForInputXML;
		// TODO : REMOVE THE NEXT LINE AS SOON AS POSSIBLE !!!!!!!!!!!!!!
		// THIS COMMENT HAVE BEEN ADDED AT 27/05/2004
		HttpOutputSource aHttpOutputSource = new HttpOutputSource();
		aHttpOutputSource.setSaveUrl(webserverurl + ":" + port + "" + uriPlanningInputLocation);
		// TODO : REMOVE THE NEXT LINE ALSO
		// THIS COMMENT HAS BEEN ADDED 03/12/2004
		aHttpOutputSource.setProtocol(protocol);

		URL url = new java.net.URL(protocol + "://" + connectionRequest);

		URLConnection conn = url.openConnection();
		System.out.println("Planning Input Request : " + connectionRequest);

		InputStream instream;
		System.out.println("Getting input stream ...");

		instream = conn.getInputStream(); // connect
		System.out.println("done");

		Document document;
		// To obtain an instance of a factory that can give us a document builder:
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);

		// Get a instance of a builder, and use it to parse the stream:
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new XMLPlanningEntityResolver());
		builder.setErrorHandler(new XMLPlanningErrorHandler());
		document = builder.parse(instream);

		return document;
	}

	public Document fetchPlanningOutputXMLDocument()
			throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
		String connectionRequest = webserverurl + ":" + port + "" + uriPlanningOutputLocation + "?"
				+ parametersForOutputXML;

		URL url = new java.net.URL(protocol + "://" + connectionRequest);

		URLConnection conn = url.openConnection();
		System.out.println("Planning Output Request : " + connectionRequest);

		InputStream instream;
		System.out.println("Getting input stream ...");
		instream = conn.getInputStream(); // connect
		System.out.println("done");

		Document document;
		// To obtain an instance of a factory that can give us a document builder:
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		// Get a instance of a builder, and use it to parse the stream:
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new XMLPlanningEntityResolver());
		// Parser parser = ParserFactory.makeParser();
		document = builder.parse(instream);

		return document;
	}

	public AbstractOutputSource getOutputSource() {
		return new HttpOutputSource();
	}
}
