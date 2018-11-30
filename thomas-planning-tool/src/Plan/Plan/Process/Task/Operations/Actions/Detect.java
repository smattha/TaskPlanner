package Plan.Process.Task.Operations.Actions;


import Plan.Process.Task.Operations.Actions.Parameters.Vision;

public class Detect extends Actions{

  
  Detect(Vision parameter)
  {
 	  this.parameter = parameter;
  }
  
public Detect() {
	// TODO Auto-generated constructor stub
}

Vision getParam() {return (Vision) parameter;};

}