package testingDemo;

public class Simulation{

	
	
public static double simulationDemo1(String res, String Task) {
	
		

int time=-10000;
int weight;
int idleness;

if (Task.compareTo("Pick Damper")==0){
   if (res.compareTo("Human")==0){
   time=2;
   weight=4;
   idleness=2;
    
   }else if (res.compareTo("Robot")==0){
   time=2;
   weight=4;
   idleness=2;
   }
   

}else if (Task.compareTo("Read Barcode")==0){
	   if (res.compareTo("Human")==0){
	   time=4;
	   weight=1;
	   idleness=2;
	    
	   }else if (res.compareTo("Robot")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   }

}else if (Task.compareTo("Place Damper")==0){
	   if (res.compareTo("Human")==0){
	   time=3;
	   weight=4;
	   idleness=2;
	    
       }else if (res.compareTo("Robot")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   }
	   
}else {
	time=-1;
}

return time;
}
}








