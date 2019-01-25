package Plan.Process.Task.Operations.Actions;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import Plan.Process.Task.Operations.Actions.Parameters.Parameters;
import xmlParser.CreateXmlFileDemo;

public class Actions {

protected Parameters parameter;
public String name="ActionName";
public String descr="Description";
public String type="type";

public Actions(){}


public Element convert2XmlElement(CreateXmlFileDemo doc)
{
	
	//parameter=new Parameters();
	Element el=doc.createElement("action");

	Attr attr=doc.createAttribute("name", name);
	Attr attr1=doc.createAttribute("description", descr);
	Attr attr2=doc.createAttribute("type", type);
	
	el.setAttributeNode(attr);
	el.setAttributeNode(attr1);
	el.setAttributeNode(attr2);
	
		
	parameter.convert2XmlElement(doc, el);
	
	//doc.addElement(el);
	  
	return el;

}

}