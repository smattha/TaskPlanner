package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Release;

import java.util.ArrayList;
import java.util.Vector;
import Plan.Process.Task.Operations.Actions.Navigate;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Retract;
import Plan.Process.Task.Operations.Actions.Parameters.Position;

public class InsertPlace extends Operations {

	private Position action5;

	InsertPlace() {
		actions = new ArrayList<Actions>();

		actions.add(new Navigate());
		actions.add(new Move());
		actions.add(new Detect());
		actions.add(new Move());
		actions.add(new Release());
		actions.add(new Retract());

	}
}