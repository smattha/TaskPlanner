package Plan.Process.Task.Operations;

import java.util.ArrayList;

import Elements.Tools.ThomasTool;

import Plan.WorkingArea;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Retract;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Release;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Parameters.Position;
import eu.robopartner.ps.planner.planninginputmodel.TASKS;
import eu.robopartner.ps.planner.planninginputmodel.TASKPRECEDENCECONSTRAINTS;

public class InsertPlace extends Operations {

	public InsertPlace(String nam, ThomasTool tool, WorkingArea w1, TASKS tasks,
			TASKPRECEDENCECONSTRAINTS theTaskprecedenceconstraints, Operations opPre, String Description) {
		this.workingArea = w1;
		this.tool = tool;
		this.name = nam;
		this.idPreviousOperation = opPre;
		this.theTaskprecedenceconstraints = theTaskprecedenceconstraints;
		this.tasks = tasks;
		this.operationDescription = description;
		tasks.getTASK().add(this);

		actions = new ArrayList<Actions>();

		Position pose = new Position(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

		// actions.add(new Navigate(pose));
		actions.add(new Move(pose));
		actions.add(new Release());
		actions.add(new Retract(pose));
		genImpactTask();
		getPrecedenceConstraints();

		actions.add(new Navigate());
		actions.add(new Move());
		actions.add(new Detect());
		actions.add(new Move());
		actions.add(new Release());
		actions.add(new Retract());

	}

}