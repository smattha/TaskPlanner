/**
 * 
 */
package lms.robopartner.datamodel.map.controller;

import gr.upatras.lms.util.Convert;

import java.awt.Point;

import javafx.geometry.Point3D;
import planning.model.ResourceDataModel;

/**
 * @author Spyros Holds the properties of the map. Only one map supported per
 *         VM. Properties could be inserted in the workcenter if properties in
 *         workcenters are implemented.
 */
public class MapParameters {

	private static final String RESOURCE_ID_PREFIX = "R";
	private static final String RESOURCE_ID_XY_SEPERATOR = "_";
	private static final String RESOURCE_ID_YZ_SEPERATOR = "!";
	private static final String RESOURCE_ID_YRZ_SEPERATOR = "#";
	public static final double AXIS_Z_DISCRETIZATION = 0.5;

	// TODO the below commented line(s) are the 3D criteria changes
	// public static int MAP_Z = (int) Math.ceil(2000/500);
	// TODO could be inserted in the workcenter if properties in workcenters are
	// implemented.

	public static int MAP_HEIGHT = (int) Math.ceil(5000 / 500);
	// TODO could be inserted in the workcenter if properties in workcenters are
	// implemented.
	public static int MAP_LENGTH = (int) Math.ceil(5000 / 500);

	public static int MAP_WIDTH = (int) Math.ceil(5000 / 500);
	// TODO could be inserted in the workcenter if properties in workcenters are
	// implemented.

	public static int speed = 250; // (mm/s)

	public static final String getIDfromLocation(Point point) {
		return MapParameters.getIDfromLocation(point.x, point.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public static final String getIDfromLocation(int x, int y) {
		return MapParameters.RESOURCE_ID_PREFIX + x + MapParameters.RESOURCE_ID_XY_SEPERATOR + y
				+ MapParameters.RESOURCE_ID_YZ_SEPERATOR + 0;
	}

	public static final String getIDFromLocation(double z) {
		return MapParameters.RESOURCE_ID_PREFIX + z;
	}

	/**
	 * 
	 * @param point
	 * @return
	 */
	public static final String getIDfromLocation(Point3D point) {
		return MapParameters.getIDfromLocation(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public static final String getIDfromLocation(double x, double y, double z) {
		return MapParameters.RESOURCE_ID_PREFIX + x + MapParameters.RESOURCE_ID_XY_SEPERATOR + y
				+ MapParameters.RESOURCE_ID_YZ_SEPERATOR + z;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param rZ
	 * @return
	 */
	public static final String getIDfromLocationAndRotation(double x, double y, double rZ) {
		return MapParameters.RESOURCE_ID_PREFIX + x + MapParameters.RESOURCE_ID_XY_SEPERATOR + y
				+ MapParameters.RESOURCE_ID_YRZ_SEPERATOR + rZ;
	}

	/**
	 * @param resourceId
	 * @return
	 */
	public static final Point3D getLocationFromID(String resourceId) {

		String xSubstring = resourceId.substring(MapParameters.RESOURCE_ID_PREFIX.length(),
				resourceId.indexOf(MapParameters.RESOURCE_ID_XY_SEPERATOR));
		String ySubstring = resourceId.substring(
				MapParameters.RESOURCE_ID_XY_SEPERATOR.length()
						+ resourceId.indexOf(MapParameters.RESOURCE_ID_XY_SEPERATOR),
				resourceId.indexOf(MapParameters.RESOURCE_ID_YZ_SEPERATOR));
		String zSubstring = resourceId.substring(MapParameters.RESOURCE_ID_YZ_SEPERATOR.length()
				+ resourceId.indexOf(MapParameters.RESOURCE_ID_YZ_SEPERATOR));
		int x = (int) Double.parseDouble(xSubstring);
		int y = (int) Double.parseDouble(ySubstring);
		int z = (int) Double.parseDouble(ySubstring);
		return new Point3D(x, y, z);
	}

	public static final Point getLocationFromResourceDataModel(ResourceDataModel resourceDataModel) {
		int x = Convert.getInteger(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME));
		int y = Convert.getInteger(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME));

		return new Point(x, y);
	}

	public static final Point3D getLocationFromResourceDataModel3D(ResourceDataModel resourceDataModel) {
		int x = Convert.getInteger(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME));
		int y = Convert.getInteger(resourceDataModel.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME));
		int z = 0;
		return new Point3D(x, y, z);
	}

}
