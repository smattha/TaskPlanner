package testingDemo;

public class Simulation{

	
	
public static double simulationDemo1(String res, String Task) {
	
		

double time=-1000;
double weight=-1000;
double idleness=-1000;
int MTTR;
int MTBF;
double Availability;


if (Task.compareTo("pickDamper")==0){
   if (res.compareTo("Human")==0){
   time=2;
   weight=4;
   idleness=2;
   MTBF=480;//8-hour shift
   MTTR=5;//5 mins to change shift
   Availability=MTBF/(MTBF+MTTR);
    
   }else if (res.compareTo("Robot1")==0){
     time=4;
     weight=4;
     idleness=3;
     MTBF=1000;
     MTTR=10;
     Availability=MTBF/(MTBF+MTTR);
   
   
   }else if (res.compareTo("Robot2")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   MTBF=1000;
	   MTTR=10;
	   Availability=MTBF/(MTBF+MTTR);
	   }
	   
   

}

if (Task.compareTo("readBarcode")==0){
	   if (res.compareTo("Human")==0){
	   time=2;
	   weight=1;
	   idleness=2;
	   MTBF=480;//8-hour shift
	   MTTR=5;//5 mins to change shift
	   Availability=MTBF/(MTBF+MTTR);
	    
	   }else if (res.compareTo("Robot1")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   MTBF=1000;
	   MTTR=10;
	   Availability=MTBF/(MTBF+MTTR);
	   }
	   
      else if (res.compareTo("Robot2")==0){
	   time=4;
	   weight=1;
	   idleness=3;
	   MTBF=1000;
	   MTTR=10;	
	   Availability=MTBF/(MTBF+MTTR);
	   

}
}

if (Task.compareTo("placeDamper")==0){
	   if (res.compareTo("Human")==0){
	   time=3;
	   weight=4;
	   idleness=2;
	   MTBF=480;//8-hour shift
	   MTTR=5;//5 mins to change shift
	   Availability=MTBF/(MTBF+MTTR);
	    
       }else if (res.compareTo("Robot1")==0){
	   time=4;
	   weight=4;
	   idleness=3;
	   MTBF=1000;
	   MTTR=10;
	   Availability=MTBF/(MTBF+MTTR);
	   }
	   
       else if (res.compareTo("Robot2")==0){
	    time=4;
	    weight=4;
	    idleness=3;
	    MTBF=1000;
	    MTTR=10;
	    Availability=MTBF/(MTBF+MTTR);
	   
}

}
//if (time==-1000)
if (idleness==-1000)
{

	int i=0;
	i=i+12;
}

//return time;
return idleness;

//if (res.compareTo("Human")==0){
	//return weight;
//}
//else{
//	return 1000;
//}
}
}


