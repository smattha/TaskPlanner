package Plan.Process.Task.Operations.Actions;

import Plan.Process.Task.Operations.Actions.Parameters.BarcodeScannerON;

public class ScanBarcode extends Actions {

	ScanBarcode(BarcodeScannerON barcodeParameters) {
		this.parameter = barcodeParameters;
	}

	public ScanBarcode() {
		// TODO Auto-generated constructor stub
	}

	BarcodeScannerON getParam() {
		return (BarcodeScannerON) parameter;
	};

}