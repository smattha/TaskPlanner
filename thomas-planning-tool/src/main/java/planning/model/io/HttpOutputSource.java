package planning.model.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

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

public class HttpOutputSource extends AbstractOutputSource {
	private String saveUrl = null;
	private String protocol;

	protected void setProtocol(String theprotocol) {
		this.protocol = theprotocol;
	}

	protected void setSaveUrl(String newUrl) {
		this.saveUrl = newUrl;
	}

	public HttpOutputSource() {
	}

	public void savePlanningInputXMLDocument(Node planningInputXMLDocument) {
		try {
			System.out.println("Saving Planning Input XML Document");
			URL url = new URL(protocol + "://" + saveUrl);
			// create a boundary string
			String boundary = MultiPartFormOutputStream.createBoundary();
			URLConnection urlConn = MultiPartFormOutputStream.createConnection(url);
			urlConn.setRequestProperty("Accept", "*/*");
//System.out.println("TEST : "+MultiPartFormOutputStream.getContentType(boundary));
			urlConn.setRequestProperty("Content-Type", MultiPartFormOutputStream.getContentType(boundary));
			// set some other request headers...
			urlConn.setRequestProperty("Connection", "Keep-Alive");
			urlConn.setRequestProperty("Cache-Control", "no-cache");
			// no need to connect cuz getOutputStream() does it
			MultiPartFormOutputStream out = new MultiPartFormOutputStream(urlConn.getOutputStream(), boundary);
			// write a text field element
			out.writeField("dbaction", "putinputxmldocument");

			// upload as a file
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.out.println("Saving Planning Input XML Document : Making the writer");
			Writer documentWriter = new Writer();
			documentWriter.setOutput(baos, "UTF-8");
			documentWriter.write(planningInputXMLDocument);
			baos.flush();
			baos.close();
			String xmlContent = baos.toString();
			System.out.println("Saving Planning Input XML Document : Making the multipart request");
			out.writeFile("theFile", "text/xml", "xmlPlanning.xml", xmlContent.getBytes());
			System.out.println("Saving Planning Input XML Document : Done making the multipart request");
			out.close();
			// read response from server
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String line = "";
			System.out.print("Server Responded:");
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void savePlanningOutputXMLDocument(Document planningOutputXMLDocument) {
		try {
			System.out.println("Saving Planning Output XML Document");
			URL url = new URL(protocol + "://" + saveUrl);
			// create a boundary string
			String boundary = MultiPartFormOutputStream.createBoundary();
			URLConnection urlConn = MultiPartFormOutputStream.createConnection(url);
			urlConn.setRequestProperty("Accept", "*/*");
			urlConn.setRequestProperty("Content-Type", MultiPartFormOutputStream.getContentType(boundary));
			// set some other request headers...
			urlConn.setRequestProperty("Connection", "Keep-Alive");
			urlConn.setRequestProperty("Cache-Control", "no-cache");
			// no need to connect cuz getOutputStream() does it
			MultiPartFormOutputStream out = new MultiPartFormOutputStream(urlConn.getOutputStream(), boundary);
			// write a text field element
			out.writeField("dbaction", "putoutputxmldocument");

			// upload as a file
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.out.println("Saving Planning Output XML Document : Making the writer");
			Writer documentWriter = new Writer();
			documentWriter.setOutput(baos, "UTF-8");
			documentWriter.write(planningOutputXMLDocument);
			baos.flush();
			baos.close();
			String xmlContent = baos.toString();
			System.out.println("Saving Planning Output XML Document : Making the multipart request");
			out.writeFile("theFile", "text/xml", "xmlPlanning.xml", xmlContent.getBytes());
			System.out.println("Saving Planning Output XML Document : Done making the multipart request");
			out.close();
			// read response from server
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String line = "";
			System.out.print("Server Responded:");
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
