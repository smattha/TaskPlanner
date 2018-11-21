package Plan.Process.Task.Operations.Actions;



import Plan.Process.Task.Operations.Actions.Parameters.IO;

public class Release extends Actions {

	Release(IO barcodeParameters)
	  {
	 	  this.parameter = barcodeParameters;
	  }
	  
	public Release() {
		// TODO Auto-generated constructor stub
	}

	IO getParam() {return (IO) parameter;};
	
}