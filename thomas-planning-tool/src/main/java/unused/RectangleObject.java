/**
 * 
 */
package unused;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * The Rectangle layout of an object. Wrapper of {@link Rectangle}.
 * 
 * @author Spyros
 *
 */
public class RectangleObject implements RectangleObjectInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2254353200701563601L;

	/**
	 * The current orientation of the {@link RectangleObject}. Always initialized to
	 * {@link RectangleObject#ZERO_DEGREES_ORIENTATION} if not set
	 */
	private double orientation = RectangleObject.ZERO_DEGREES_ORIENTATION;

	/**
	 * 
	 */
	private double reachability = 0;

	/**
	 * 
	 */
	private Rectangle myRectangle = null;
	/**
	 * 
	 */
	protected Rectangle myReachabilityRectangle = null;
	/**
	 * The id of the instance linked to this map
	 */
	private String id;
	public static final Double ZERO_DEGREES_ORIENTATION = 0.0;
	public static final Double NINETY_DEGREES_ORIENTATION = 90.0;

	/**
	 * initializes {@link RectangleObject#orientation} to
	 * {@link RectangleObject#ZERO_DEGREES_ORIENTATION}
	 * 
	 * @param aRectangle defines the location and size of the object
	 * @param id         The id of the instance linked to this map
	 */
	public RectangleObject(Rectangle aRectangle, String id, double reachability) {
		this.setRectangle(aRectangle);
		this.setID(id);
		this.setOrientation0Degrees();
		this.setReachability(reachability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#
	 * isOrientation0Degrees()
	 */
	public boolean isOrientation0Degrees() {

		return Double.compare(this.orientation, RectangleObject.ZERO_DEGREES_ORIENTATION) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#
	 * isOrientation90Degrees()
	 */
	public boolean isOrientation90Degrees() {
		return !this.isOrientation0Degrees();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#
	 * setOrientation0Degrees()
	 */
	public RectangleObjectInterface setOrientation0Degrees() {
		this.orientation = RectangleObject.ZERO_DEGREES_ORIENTATION;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#
	 * setOrientation90Degrees()
	 */
	public RectangleObjectInterface setOrientation90Degrees() {
		this.orientation = RectangleObject.NINETY_DEGREES_ORIENTATION;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#getRectangle ()
	 */
	public Rectangle getRectangle() {

		return this.myRectangle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#setRectangle
	 * (java.awt.Rectangle)
	 */
	public RectangleObjectInterface setRectangle(Rectangle aRectangle) {
		String msg = ".setRectangle() ";
		if (aRectangle == null) {
			throw new RuntimeException(msg + "Rectangle should never be null.");
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#intersects
	 * (lms.robopartner.datamodel.map.RectangleObjectInterface)
	 */
	public boolean intersects(RectangleObjectInterface anotherRectangleObjectInterface) {
		String msg = ".intersects() ";
		if (anotherRectangleObjectInterface == null || anotherRectangleObjectInterface.getRectangle() == null) {
			throw new RuntimeException(msg + "RectangleObjectInterface or its rectangle should never be null.");
		}

		return this.getRectangle().intersects(anotherRectangleObjectInterface.getRectangle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public RectangleObject clone() {
		Rectangle aRectangle = new Rectangle(this.getRectangle().x, this.getRectangle().y, this.getRectangle().width,
				this.getRectangle().height);
		RectangleObject aRectangleObjectClone = new RectangleObject(aRectangle, this.getID(), this.reachability);
		aRectangleObjectClone.orientation = this.orientation;
		return aRectangleObjectClone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#getID()
	 */
	public String getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#setID(java
	 * .lang.String)
	 */
	public RectangleObjectInterface setID(String id) {
		String msg = ".setID() ";
		if (id == null || id.isEmpty()) {
			throw new RuntimeException(msg + "id should never be null.");
		}

		this.id = id;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#
	 * getReachabilityRectangle()
	 */
	public Rectangle getReachabilityRectangle() {

		return this.myReachabilityRectangle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#setReachability
	 * (java.lang.Double)
	 */
	public RectangleObjectInterface setReachability(Double reachability) {
		String msg = ".setReachability() ";
		if (reachability < 0) {
			throw new RuntimeException(msg + "reachability must be positive.");
		}
		this.reachability = reachability;
		this.myRectangle = new Rectangle(1, 3, 1, 2);

		this.myReachabilityRectangle = new Rectangle(this.myRectangle.getLocation(),
				new Dimension((int) this.reachability, (int) this.reachability));
		this.myReachabilityRectangle.translate(this.myRectangle.width / 2, this.myRectangle.height / 2);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.RectangleObjectInterface#getReachability
	 * ()
	 */
	public double getReachability() {
		return this.reachability;
	}

	/**
	 * @param aRectangleObjectInterface
	 * @return
	 */
	public boolean isWithinReach(RectangleObjectInterface aRectangleObjectInterface) {
		String msg = ".isWithinReach() ";
		if (aRectangleObjectInterface == null || aRectangleObjectInterface.getReachabilityRectangle() == null) {
			throw new RuntimeException(
					msg + "RectangleObjectInterface or its getReachabilityRectangle should never be null.");
		}

		return this.myReachabilityRectangle.intersects(aRectangleObjectInterface.getReachabilityRectangle());
	}

}
