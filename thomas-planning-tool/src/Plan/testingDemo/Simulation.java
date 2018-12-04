package testingDemo;

public class Simulation{

	
	
public static double simulationDemo1(String res, String Task) {
	
		

int time=0;
int weight;
int idleness;

if (Task.compareTo("pick damper")==0){
   if (res.compareTo("Human")==0){
   time=2;
   weight=4;
   idleness=2;
    
   }else if (res.compareTo("Robot1")==0){
     time=4;
     weight=4;
     idleness=3;
   
   
   }else if (res.compareTo("Robot2")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   }
	   
   

}else if (Task.compareTo("read barcode")==0){
	   if (res.compareTo("Human")==0){
	   time=2;
	   weight=1;
	   idleness=2;
	    
	   }else if (res.compareTo("Robot1")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   }
	   
       }else if (res.compareTo("Robot2")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   

}else if (Task.compareTo("place damper")==0){
	   if (res.compareTo("Human")==0){
	   time=3;
	   weight=4;
	   idleness=2;
	    
       }else if (res.compareTo("Robot1")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   }
	   
       }else if (res.compareTo("Robot2")==0){
	    time=4;
	    weight=4;
	    idleness=3;
	   
}else {
	  time=-1;
	  //idleness=-1;
}


return time;
//return idleness;
}
}
