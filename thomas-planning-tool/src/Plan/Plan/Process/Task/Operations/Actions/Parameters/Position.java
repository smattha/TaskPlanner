package Plan.Process.Task.Operations.Actions.Parameters;

import java.util.Vector;

public class Position extends Parameters {


	 private double	x;
	 private double	y;
	 private double	z;
	 private double	roll;
	 private double	pitch;
	 private double	yaw;
     
	/**
	 * @return the x- length
	 */
	 
	public double getX() {
		return x;
	}


		
		/**
		 * @param x the x to set
		 */
		public void setX(double x) {
			this.x = x;
		}


		
		/**
		 * @return the y - height
		 */
		public double getY() {
			return y;
		}

		
		/**
		 * @param y the y to set
		 */
		public void setY(double y) {
			this.y = y;
		}



		
		/**
		 * @return the z - width
		 */
		public double getZ() {
			return z;
		}



		
		/**
		 * @param z the z to set
		 */
		public void setZ(double z) {
			this.z = z;
		}



		
		/**
		 * @return the roll
		 */
		public double getRoll() {
			return roll;
		}



		
		/**
		 * @param roll the roll to set
		 */
		public void setRoll(double roll) {
			this.roll = roll;
		}

		
		/**
		 * @return the pitch
		 */
		public double getPitch() {
			return pitch;
		}



		/**
		 * @param pitch the pitch to set
		 */
		public void setPitch(double pitch) {
			this.pitch = pitch;
		}

		
		/**
		 * @return the yaw
		 */
		public double getYaw() {
			return yaw;
		}


		
		/**
		 * @param yaw the yaw to set
		 */
		public void setYaw(double yaw) {
			this.yaw = yaw;
		}

       

		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public Position(double x, double y, double z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
			this.roll=0;
			this.pitch=0;
			this.yaw=0;
		}

		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public Position(double x, double y, double z,double roll,double pitch, double yaw) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
			this.roll=roll;
			this.pitch=pitch;
			this.yaw=yaw;
		}



		public Position() {
			// TODO Auto-generated constructor stub
		}

		


}