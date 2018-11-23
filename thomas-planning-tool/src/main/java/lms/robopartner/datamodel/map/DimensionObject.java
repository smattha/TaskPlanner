/**
 * 
 */
package lms.robopartner.datamodel.map;


import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Spyros
 *
 */
public class DimensionObject implements DimensionObjectInterface {

	private Map<String, String>	propertyMap	= new HashMap<String, String>();

	/**
	 * @param dimension
	 * @param id
	 * @param reachability
	 * @param weight
	 */
	public DimensionObject(Dimension dimension, String id, Double reachability) {
		this.dimension = dimension;
		this.id = id;
		this.reachability = reachability;
	}

	/**
	 * @param dimension
	 * @param height
	 * @param id
	 * @param reachability
	 */
	public DimensionObject(Dimension dimension, Double height, String id, Double reachability) {
		this(dimension, id, reachability);
		this.setHeight(height);
	}

	/**
	 * 
	 * @param dimension
	 * @param height
	 * @param id
	 * @param reachability
	 * @param shape
	 */
	public DimensionObject(Dimension dimension, Double height, String id, Double reachability, String shape) {
		this(dimension, id, reachability);
		this.setHeight(height);
		this.setShape(shape);
	}

	/**
	 * @param dimension
	 * @param id
	 * @param reachability
	 * @param weight
	 */
	public DimensionObject(Dimension dimension, String id, Double reachability, Double weight) {
		this.dimension = dimension;
		this.id = id;
		this.reachability = reachability;
		this.weight = weight;
	}

	/**
	 * @param dimension
	 * @param height
	 * @param id
	 * @param reachability
	 * @param weight
	 */
	public DimensionObject(Dimension dimension, Double height, String id, Double reachability, Double weight) {
		this(dimension, id, reachability, weight);
		this.setHeight(height);
	}

	public DimensionObject(int virtualIMAU, String id) {
		this.virtualIMAU = virtualIMAU;
		this.id = id;
	}

	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= 1024009830999690241L;
	private Dimension			dimension			= new Dimension();
	private String				id					= null;
	private Double				reachability		= 0d;
	private Double				weight				= 0d;
	private Double				height				= 0d;
	private int					virtualIMAU;
	private String				shape				= "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#getDimension
	 * ()
	 */
	public Dimension getDimension() {
		return this.dimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#setDimension
	 * (java.awt.Dimension)
	 */
	public DimensionObjectInterface setDimension(Dimension aDimension) {
		this.dimension = aDimension;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#getID()
	 */
	public String getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#setID(java
	 * .lang.String)
	 */
	public DimensionObjectInterface setVirtualIMAU(int virtualIMAU) {
		this.virtualIMAU = virtualIMAU;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#getID()
	 */
	public int getVirtualIMAU() {
		return this.virtualIMAU;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#setID(java
	 * .lang.String)
	 */
	public DimensionObjectInterface setID(String id) {
		this.id = id;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#setReachability
	 * (java.lang.Double)
	 */
	public DimensionObjectInterface setReachability(Double reachability) {
		this.reachability = reachability;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lms.robopartner.datamodel.map.DimensionObjectInterface#getReachability
	 * ()
	 */
	public double getReachability() {
		return this.reachability;
	}

	/* (non-Javadoc)
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#getWeight()
	 */
	public double getWeight() {
		return this.weight;
	}

	/* (non-Javadoc)
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String key, String value) {
		this.propertyMap.put(key, value);

	}

	/* (non-Javadoc)
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return this.propertyMap.get(key);
	}

	/* (non-Javadoc)
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#setHeight(double)
	 */
	public void setHeight(double height) {
		this.height = height;

	}

	/* (non-Javadoc)
	 * @see lms.robopartner.datamodel.map.DimensionObjectInterface#getHeight()
	 */
	public double getHeight() {
		return this.height;
	}

	public String getShape() {
		return this.shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

}
