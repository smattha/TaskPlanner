package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.ArrayList;
import java.util.Vector;

import org.w3c.dom.Attr;

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
		public Position(double x, double y, double z,double orientationW,double orientationX,double orientationY, double orientationZ) {
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
    		
    		inputOrientationX.add(Constants.POSITION_ORIENT_X_VALUE_ATTR);inputOrientationX.add(Double.toString(orientationX));
    		inputOrientationY.add(Constants.POSITION_ORIENT_Y_VALUE_ATTR);inputOrientationY.add(Double.toString(orientationY));
    		inputOrientationZ.add(Constants.POSITION_ORIENT_Z_VALUE_ATTR);inputOrientationZ.add(Double.toString(orientationZ));
    		inputOrientationW.add(Constants.POSITION_ORIENT_W_VALUE_ATTR);inputOrientationW.add(Double.toString(orientationW));
    		
    		
    		inputs.add(inputX);
    		inputs.add(inputY);
    		inputs.add(inputZ);
    		
    		inputs.add(inputOrientationX);
    		inputs.add(inputOrientationY);
    		inputs.add(inputOrientationZ);
    		inputs.add(inputOrientationW);
    		
		}



		public Position() {
			// TODO Auto-generated constructor stub
		}

		


}