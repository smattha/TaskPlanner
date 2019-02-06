package lms.thomas.util.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class createThomasProgram {

	private DocumentBuilderFactory documentFactory;

	private Document document;

	Element root;

	public createThomasProgram() {
		try {

			documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			document = documentBuilder.newDocument();
			createRoot();

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}

	}

	private void createRoot() {
		root = document.createElement("program");
		document.appendChild(root);
		// return root;
	}

	public boolean addElement(Element newElement) {

		root.appendChild(newElement);
		return true;
	}

	public Attr createAttribute(String id, String value) {
		Attr attr = document.createAttribute(id);
		attr.setValue(value);
		return attr;
	}

	public Element createElement(String name) {

		return document.createElement(name);

	}

	public boolean store(String xmlFilePath) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();

			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));

			try {
				transformer.transform(domSource, streamResult);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("XML File Stored");

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public Text createTextNode(String value) {
		return document.createTextNode(value);
	}

	public static final String xmlFilePath = "C:\\Users\\smatt\\Desktop\\thomas\\xmlfile.xml";

	public static void main(String argv[]) {

		createThomasProgram doc = new createThomasProgram();

		Element employee = doc.createElement("employer");
		Attr attr = doc.createAttribute("Attr", "10");
		employee.setAttributeNode(attr);
		// doc.addElement(employee);

		Element employeeInside = doc.createElement("employerInside");
		Attr attr2 = doc.createAttribute("Attr", "10");
		employeeInside.setAttributeNode(attr2);
		employeeInside.appendChild(doc.createTextNode("It works"));
		employee.appendChild(employeeInside);

		doc.addElement(employee);

		doc.store(xmlFilePath);

	}

}