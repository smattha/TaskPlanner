package Plan.Process.Task.Operations;

import Plan.Process.Task.Operations.Actions.Move;

import java.util.ArrayList;
import java.util.Vector;

import Elements.Tools.BarcodeScanner;
import Elements.Tools.Gripper;
import Plan.Process.Task.Operations.Actions.ScanBarcode;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Detect;

public class ReadBarcode extends Operations {

	private Gripper tool2;

	public Vector myOperations;

	ReadBarcode() {
		actions = new ArrayList<Actions>();

		actions.add(new Move());
		actions.add(new Detect());
		actions.add(new ScanBarcode());

	}

}