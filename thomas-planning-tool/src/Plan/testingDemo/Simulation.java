package testingDemo;

public class Simulation{

	
	
public static double simulationDemo1(String res, String Task) {
	
		

int time=-1000;
int weight;
int idleness;

if (Task.compareTo("pickDamper")==0){
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
	   
   

}

if (Task.compareTo("readBarcode")==0){
	   if (res.compareTo("Human")==0){
	   time=2;
	   weight=1;
	   idleness=2;
	    
	   }else if (res.compareTo("Robot1")==0){
	   time=4       ;
	   weight=1;
	   idleness=3;
	   }
	   
      else if (res.compareTo("Robot2")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   
	   

}
}

if (Task.compareTo("placeDamper")==0){
	   if (res.compareTo("Human")==0){
	   time=3;
	   weight=4;
	   idleness=2;
	    
       }else if (res.compareTo("Robot1")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   }
	   
       else if (res.compareTo("Robot2")==0){
	    time=4;
	    weight=4;
	    idleness=3;
	   
}

}
if (time==-1000)
{

	int i=0;
	i=i+12;
}

return time;
//return idleness;
}
}
