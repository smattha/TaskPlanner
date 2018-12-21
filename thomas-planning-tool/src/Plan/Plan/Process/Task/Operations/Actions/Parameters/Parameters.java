package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import xmlParser.Constants;
import xmlParser.CreateXmlFileDemo;



public class Parameters {

	
	String name="Parameters";


	protected ArrayList<ArrayList<String>> inputs = new ArrayList<ArrayList<String>>();
	
	protected ArrayList<ArrayList<String>> outputs = new ArrayList<ArrayList<String>>();
	
	public Parameters()
	{
		
		//ArrayList<String> input = new ArrayList<String>();
		
		//input.add("nameTemp");input.add("valueTemp");
		
		//inputs.add(input);
		
		//outputs.add("output");
	}
	
	public void convert2XmlElement(CreateXmlFileDemo doc,Element root)
	{
		
		Element e2=doc.createElement(Constants.OUTPUT_SECTION_ATTR);
		  
    	for(ArrayList<String> input:inputs)
    	{
    		Element el=doc.createElement(Constants.INPUT_SECTION_ATTR);
    		
    		Attr attr=doc.createAttribute(Constants.INPUT_ELEMENT_NAME_ATTR, input.get(0));
        	Attr attr1=doc.createAttribute(Constants.INPUT_ELEMENT_VALUE_ATTR,  input.get(1));
        	
    		el.setAttributeNode(attr);
    		el.setAttributeNode(attr1);
    		root.appendChild(el);
    	}
    	
    	
    	for(ArrayList<String> output:outputs)
    	{
    		Element el=doc.createElement(Constants.INPUT_SECTION_ATTR);
    		
    		Attr attr=doc.createAttribute(Constants.INPUT_ELEMENT_NAME_ATTR, output.get(0));
        	Attr attr1=doc.createAttribute(Constants.INPUT_ELEMENT_VALUE_ATTR,  output.get(1));
        	
    		el.setAttributeNode(attr);
    		el.setAttributeNode(attr1);
    		root.appendChild(el);
    	}
    	

  

		root.appendChild(e2);
		  
		return ;

	}
        
}