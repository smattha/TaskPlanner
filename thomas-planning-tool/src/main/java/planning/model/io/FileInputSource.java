package planning.model.io;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class FileInputSource extends AbstractInputSource {
	private String fileInPath = "";
	private String fileOutPath = "";

	FileOutputSource aFileOutputSource = null;

	public FileInputSource(String fileInPath, String fileOutPath) {
		this.fileInPath = fileInPath;
		this.fileOutPath = fileOutPath;
	}

	public Document fetchPlanningOutputXMLDocument() throws java.net.MalformedURLException, java.io.IOException,
			javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
		File outFile = new File(fileOutPath);
		FileInputStream instream = new FileInputStream(outFile);

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

	public Document fetchPlanningInputXMLDocument() throws java.net.MalformedURLException, java.io.IOException,
			javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException {
		File inFile = new File(fileInPath);
		FileInputStream instream = new FileInputStream(inFile);

		// TODO : REMOVE THE NEXT LINE AS SOON AS POSSIBLE !!!!!!!!!!!!!!
		// THIS COMMENT HAVE BEEN ADDED AT 27/05/2004
		// REVALIDATE THE COMMENT ... ABOVE 22/02/2016
		// REVALIDATE THE COMMENT ... ABOVE 21/12/2017
		aFileOutputSource = new FileOutputSource();
		aFileOutputSource.setUrls(fileInPath, fileOutPath);

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
		return this.aFileOutputSource;
	}
}
