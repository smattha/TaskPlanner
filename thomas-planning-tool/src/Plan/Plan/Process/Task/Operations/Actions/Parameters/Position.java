package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;
import java.util.Vector;

import org.w3c.dom.Attr;

import gr.upatras.lms.util.Convert;
import xmlParser.Constants;

public class Position extends Parameters {


	 private double	x;
	 private double	y;
	 private double	z;
	 private double	orientationW;
	 private double	orientationX;
	 private double	orientationY;
	 private double	orientationZ;
     



		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public Position(double x, double y, double z,double orientationX,double orientationY,double orientationZ, double orientationW,String target_fame,double vel,double acc) {
			//super();
			this.x = x;
			this.y = y;
			this.z = z;
			this.orientationX=orientationX;
			this.orientationY=orientationY;
			this.orientationZ=orientationZ;
			this.orientationW=orientationW;

    		//Attr attrX=doc.createAttribute(Constants.POSITION_X_VALUE_ATTR, x);
    		//Attr attrY=doc.createAttribute(Constants.POSITION_Y_VALUE_ATTR, y);
    		//Attr attrZ=doc.createAttribute(Constants.POSITION_Z_VALUE_ATTR, z);
    		
			ArrayList<String> inputPosition = new ArrayList<String>();
    		
			ArrayList<String> inputFrame = new ArrayList<String>();
			ArrayList<String> inputVel = new ArrayList<String>();
			ArrayList<String> inputAcc = new ArrayList<String>();
			

    		ArrayList<String> inputX = new ArrayList<String>();
    		ArrayList<String> inputY = new ArrayList<String>();
    		ArrayList<String> inputZ = new ArrayList<String>();
    		
    		
    		ArrayList<String> inputOrientationX = new ArrayList<String>();
    		ArrayList<String> inputOrientationY = new ArrayList<String>();
    		ArrayList<String> inputOrientationZ= new ArrayList<String>();
    		ArrayList<String> inputOrientationW= new ArrayList<String>();
    		
    		//ArrayList<String> input = new ArrayList<String>();
    		
    		//input.add("nameTemp");input.add("valueTemp");
    		
    		inputX.add(Constants.POSITION_X_VALUE_ATTR);inputX.add(Double.toString(x));
    		inputY.add(Constants.POSITION_Y_VALUE_ATTR);inputY.add(Double.toString(y));
    		inputZ.add(Constants.POSITION_Z_VALUE_ATTR);inputZ.add(Double.toString(z));
    		
    		
    		inputX.add(Constants.POSITION_X_VALUE_ATTR);inputX.add(Double.toString(x));
    		
    		inputOrientationX.add(Constants.POSITION_ORIENT_X_VALUE_ATTR);inputOrientationX.add(Double.toString(orientationX));
    		inputOrientationY.add(Constants.POSITION_ORIENT_Y_VALUE_ATTR);inputOrientationY.add(Double.toString(orientationY));
    		inputOrientationZ.add(Constants.POSITION_ORIENT_Z_VALUE_ATTR);inputOrientationZ.add(Double.toString(orientationZ));
    		inputOrientationW.add(Constants.POSITION_ORIENT_W_VALUE_ATTR);inputOrientationW.add(Double.toString(orientationW));
    		
    		String inputPoseString=x+" "+y+" "+z+" "+orientationX+" "+orientationY+" "+orientationZ;
    		
    		inputPosition.add(Constants.MOVE_POSE_VALUE_ATTR);inputPosition.add(inputPoseString);	
    		
    		inputFrame.add(Constants.MOVE_TARGET_VALUE_ATTR);inputFrame.add(target_fame);	
			inputVel.add(Constants.MOVE_VEL_VALUE_ATTR);inputVel.add(Double.toString(vel));
			inputAcc.add(Constants.MOVE_ACC_VALUE_ATTR);inputAcc.add(Double.toString(acc));
			
    		
    		inputs.add(inputPosition);
    		inputs.add(inputFrame);
    		inputs.add(inputVel);
    		inputs.add(inputAcc);
    		
    		//inputs.add(inputX);
    		//inputs.add(inputY);
    		//inputs.add(inputZ);
    		
    		//inputs.add(inputOrientationX);
    		//inputs.add(inputOrientationY);
    		//inputs.add(inputOrientationZ);
    		//inputs.add(inputOrientationW);
    		
		}
		//public Position(double x, double y, double z,double orientationW,double orientationX,double orientationY, double orientationZ,String target_fame,double vel,double acc) {
			
		 public Position(double x, double y, double z,double orientationW,double orientationX,double orientationY, double orientationZ) {
			 this(x, y, z, orientationW, orientationX, orientationY, orientationZ, "a", 0.04,  0.001 );
 
		 }


		public Position(String str)
		{
			
			String[] data = str.split(" ");
			
			
			
			
			
			this.x = Convert.getDouble(data[0]);
			this.y = Convert.getDouble(data[1]);
			this.z = Convert.getDouble(data[2]);
			this.orientationX=Convert.getDouble(data[3]);
			this.orientationY=Convert.getDouble(data[4]);
			this.orientationZ=Convert.getDouble(data[5]);
			this.orientationW=Convert.getDouble(data[6]);

    		//Attr attrX=doc.createAttribute(Constants.POSITION_X_VALUE_ATTR, x);
    		//Attr attrY=doc.createAttribute(Constants.POSITION_Y_VALUE_ATTR, y);
    		//Attr attrZ=doc.createAttribute(Constants.POSITION_Z_VALUE_ATTR, z);
    		
			ArrayList<String> inputPosition = new ArrayList<String>();
    		
			ArrayList<String> inputFrame = new ArrayList<String>();
			ArrayList<String> inputVel = new ArrayList<String>();
			ArrayList<String> inputAcc = new ArrayList<String>();
			

    		ArrayList<String> inputX = new ArrayList<String>();
    		ArrayList<String> inputY = new ArrayList<String>();
    		ArrayList<String> inputZ = new ArrayList<String>();
    		
    		
    		ArrayList<String> inputOrientationX = new ArrayList<String>();
    		ArrayList<String> inputOrientationY = new ArrayList<String>();
    		ArrayList<String> inputOrientationZ= new ArrayList<String>();
    		ArrayList<String> inputOrientationW= new ArrayList<String>();
    		
    		//ArrayList<String> input = new ArrayList<String>();
    		
    		//input.add("nameTemp");input.add("valueTemp");
    		
    		inputX.add(Constants.POSITION_X_VALUE_ATTR);inputX.add(Double.toString(x));
    		inputY.add(Constants.POSITION_Y_VALUE_ATTR);inputY.add(Double.toString(y));
    		inputZ.add(Constants.POSITION_Z_VALUE_ATTR);inputZ.add(Double.toString(z));
    		
    		
    		//inputX.add(Constants.POSITION_X_VALUE_ATTR);inputX.add(Double.toString(x));
    		
    		inputOrientationX.add(Constants.POSITION_ORIENT_X_VALUE_ATTR);inputOrientationX.add(Double.toString(orientationX));
    		inputOrientationY.add(Constants.POSITION_ORIENT_Y_VALUE_ATTR);inputOrientationY.add(Double.toString(orientationY));
    		inputOrientationZ.add(Constants.POSITION_ORIENT_Z_VALUE_ATTR);inputOrientationZ.add(Double.toString(orientationZ));
    		inputOrientationW.add(Constants.POSITION_ORIENT_W_VALUE_ATTR);inputOrientationW.add(Double.toString(orientationW));
    		
    		String inputPoseString=x+" "+y+" "+z+" "+orientationX+" "+orientationY+" "+orientationZ;
    		
    		inputPosition.add(Constants.MOVE_POSE_VALUE_ATTR);inputPosition.add(inputPoseString);	
    		
    		inputFrame.add(Constants.MOVE_TARGET_VALUE_ATTR);inputFrame.add(data[7]);	
			inputVel.add(Constants.MOVE_VEL_VALUE_ATTR);inputVel.add(data[8]);
			inputAcc.add(Constants.MOVE_ACC_VALUE_ATTR);inputAcc.add(data[9]);
			
    		
    		inputs.add(inputPosition);
    		inputs.add(inputFrame);
    		inputs.add(inputVel);
    		inputs.add(inputAcc);
    		
    		//inputs.add(inputX);
    		//inputs.add(inputY);
    		//inputs.add(inputZ);
    		
    		//inputs.add(inputOrientationX);
    		//inputs.add(inputOrientationY);
    		//inputs.add(inputOrientationZ);
    		//inputs.add(inputOrientationW);
    		
			
		}
		public Position() {
			// TODO Auto-generated constructor stub
		}

		


}