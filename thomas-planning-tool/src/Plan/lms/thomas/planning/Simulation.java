package lms.thomas.planning;

public class Simulation {

	public static double[] simulationDemo1(String res, String Task) {

		double time = -1000;
		double weight = -1000;
		double idleness = -1000;
		double distance = -1000;
		double MTTR;
		double MTBF;
		double Availability = -1000;
		double Utilization = -1000;
		double Return[] = new double[2];

		if (Task.compareTo("pickDamper") == 0) {
			if (res.compareTo("Human") == 0) {
				time = 2;
				weight = 4;
				idleness = 2;
				distance = 0;
				MTBF = 480;// 8-hour shift
				MTTR = 5;// 5 mins to change shift
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			} else if (res.compareTo("Robot1") == 0) {
				time = 4;
				weight = 4;
				idleness = 3;
				distance = 0;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			} else if (res.compareTo("Robot2") == 0) {
				time = 4;
				weight = 4;
				idleness = 3;
				distance = 0;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			}

		}

		if (Task.compareTo("readBarcode") == 0) {
			if (res.compareTo("Human") == 0) {
				time = 2;
				weight = 1;
				idleness = 2;
				distance = 2;
				MTBF = 480;// 8-hour shift
				MTTR = 5;// 5 mins to change shift
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			} else if (res.compareTo("Robot1") == 0) {
				time = 4;
				weight = 1;
				idleness = 3;
				distance = 2;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			}

			else if (res.compareTo("Robot2") == 0) {
				time = 4;
				weight = 1;
				idleness = 3;
				distance = 2;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			}
		}

		if (Task.compareTo("placeDamper") == 0) {
			if (res.compareTo("Human") == 0) {
				time = 3;
				weight = 4;
				idleness = 2;
				distance = 5;
				MTBF = 480;// 8-hour shift
				MTTR = 5;// 5 mins to change shift
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			} else if (res.compareTo("Robot1") == 0) {
				time = 4;
				weight = 4;
				idleness = 3;
				distance = 5;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			}

			else if (res.compareTo("Robot2") == 0) {
				time = 4;
				weight = 4;
				idleness = 3;
				distance = 5;
				MTBF = 1000;
				MTTR = 10;
				Availability = MTBF / (MTBF + MTTR);
				Utilization = time / Availability;

			}
		}
//if (time==-1000){
//if (idleness==-1000){
//if (weight==-1000){
		if (Utilization == -1000) {

			int i = 0;
			i = i + 12;
		}
//Flowtime
//Return[0]=time+idleness;
//Return[1]=0;

//Idleness
//Return[0]=idleness;
//Return[1]=0;

//Utilization human
		if (res.equals("Human")) {
			Return[0] = time + idleness;
			Return[1] = Utilization;
		} else {
			Return[0] = 1000;
			Return[1] = 1000;
		}

//Utilization Robot1
//if (res.equals("Robot1")){
//Return[0]=time+idleness;
//Return[1]=Utilization;
//}
//else {
		// Return[0]=1000;
		// Return[1]=1000;
//}

//Utilization Robot2
//if (res.equals("Robot2")){
//Return[0]=time+idleness;
//Return[1]=Utilization;
//}
//else {
		// Return[0]=1000;
		// Return[1]=1000;
//}

//Payload
//if (res.equals("Human")){
		// Return[0]= weight;
		// Return[1]= 0;
//}
//else{
		// Return[0]= 1000;
		// Return[1]= 0;
//}

//DistanceCovered human
//if (res.equals("Human")){
//Return[0]=distance;
//Return[1]=0;
//}
//else {
//	Return[0]=0.1;
//	Return[1]=0.1;
//}

//DistanceCovered Robot1
//if (res.equals("Robot1")){
//Return[0]=distance;
//Return[1]=0;
//}
//else {
//	Return[0]=0.1;
//	Return[1]=0.1;
//}

//DistanceCovered Robot2
//if (res.equals("Robot2")){
//Return[0]=distance;
//Return[1]=0;
//}
//else {
//	Return[0]=0.1;
//	Return[1]=0.1;
//}

		return Return;
	}
}