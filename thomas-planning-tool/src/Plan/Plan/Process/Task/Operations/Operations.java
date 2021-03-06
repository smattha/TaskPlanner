package Plan.Process.Task.Operations;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import eu.robopartner.ps.planner.planninginputmodel.ObjectFactory;
import eu.robopartner.ps.planner.planninginputmodel.POSTCONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PRECONDITIONTASKREFERENCE;
import eu.robopartner.ps.planner.planninginputmodel.PROPERTIES;
import eu.robopartner.ps.planner.planninginputmodel.TASK;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINT;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import lms.robopartner.datamodel.map.IDGenerator;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.thomas.util.xml.createThomasProgram;
import Plan.WorkingArea;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.tools.Tool;

import Elements.Elements;
import Elements.Parts.Parts;
import Elements.Tools.ThomasTool;
import Plan.Process.Task.Operations.Actions.*;

public class Operations extends TASK {

	static public HashMap<String, Operations> mapOperationsThomas2Id = new HashMap<String, Operations>();

	protected TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints;

	private Parts basepart;
	private Parts matingpart;

	protected ThomasTool tool;

	public ThomasTool getTool() {
		return tool;
	};

	public WorkingArea workingplace;
	public String description;
	public String name = "Pick";
	public String assigned = "";
	public double weightPart;

	TASKS tasks;
	public WorkingArea workingArea;
	ArrayList<Actions> actions;

	protected Operations idPreviousOperation;

	public void setAction(Actions newAction) {
		actions.add(newAction);
	}

	public String operationDescription = "Description";
	public String operationType = "TASK";
	// TASKPRECEDENCECONSTRAINTS constraints;

	public Element convert2XmlElement(createThomasProgram doc) {
		Element el = doc.createElement("action");

		Attr attr = doc.createAttribute("name", name);
		Attr attr1 = doc.createAttribute("description", operationDescription);
		Attr attr2 = doc.createAttribute("type", operationType);
		Attr attr3 = doc.createAttribute("assigned", assigned);

		el.setAttributeNode(attr);
		el.setAttributeNode(attr1);
		el.setAttributeNode(attr2);
		el.setAttributeNode(attr3);
		System.out.println("......................................... " + name + " " + actions.size());
		for (Actions actionCurrent : actions) {
			System.out.println("\\......................................... " + name + " " + actions.size());

			try {
				System.out.println("                    " + actionCurrent.type);

			} catch (Exception e1) {
				System.out.println(e1.getMessage());
				continue;

			}

			Element newChild = actionCurrent.convert2XmlElement(doc);
			el.appendChild(newChild);
		}
		doc.addElement(el);
		return el;

	}

	public Boolean genImpactTask() {
		BigInteger id1 = IDGenerator.getNewID();
		String newId = "task_" + id1;
		properties = new PROPERTIES();
		setDESCRIPTION("description");
		setId(newId);
		setNAME(name);
		setPROPERTIES(properties);

		properties.getPROPERTY().add(MapToResourcesAndTasks.getProperty("name", this.name + ""));
		Operations.mapOperationsThomas2Id.put(newId, this);

		return true;
	}

	public TASKPRECEDENCECONSTRAINTS getPrecedenceConstraints() {

		if (idPreviousOperation != null) {
			TASKPRECEDENCECONSTRAINT constraint = new TASKPRECEDENCECONSTRAINT();

			PRECONDITIONTASKREFERENCE pre = new PRECONDITIONTASKREFERENCE();
			POSTCONDITIONTASKREFERENCE post = new POSTCONDITIONTASKREFERENCE();

			pre.setRefid(idPreviousOperation.getId());
			post.setRefid(this.id);
			constraint.setPRECONDITIONTASKREFERENCE(pre);
			constraint.setPOSTCONDITIONTASKREFERENCE(post);
			theTaskprecedenceconstraints.getTASKPRECEDENCECONSTRAINT().add(constraint);

		}

		return theTaskprecedenceconstraints;
	}

}