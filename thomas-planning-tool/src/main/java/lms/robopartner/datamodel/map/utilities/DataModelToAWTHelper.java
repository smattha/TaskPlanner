/**
 * 
 */
package lms.robopartner.datamodel.map.utilities;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javafx.geometry.Point3D;
import javafx.scene.shape.Box;
import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

//import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import planning.model.ResourceDataModel;
import planning.model.TaskDataModel;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;

/**
 * @author Spyros Helper class to provide conversion functionality
 */
@SuppressWarnings("restriction")
public abstract class DataModelToAWTHelper {

	/**
	 * @param resource
	 * @param simulator
	 * @return
	 */
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DataModelToAWTHelper.class);

	public final static Rectangle createRectangle(ResourceSimulator resource, TaskSimulator task) {
		Dimension aDimension = DataModelToAWTHelper.getDimensionFromTask(task);
		Point aPoint = DataModelToAWTHelper.getPointFromResource(resource);
		return new Rectangle(aPoint, aDimension);
	}

	/**
	 * @param resource
	 * @param simulator
	 * @return
	 */
	public final static Rectangle createRectangle(ResourceDataModel resource, TaskDataModel task) {
		Dimension aDimension = DataModelToAWTHelper.getDimensionFromTask(task);
		Point aPoint = DataModelToAWTHelper.getPointFromResource(resource);
		return new Rectangle(aPoint, aDimension);
	}

	/**
	 * @param resource
	 * @param simulator
	 * @return
	 */
	public final static Rectangle createRectangle(ResourceDataModel resource, TaskDataModel task, int resolution) {
		Dimension aDimension = DataModelToAWTHelper.getDimensionFromTask(task, resolution);
		Point aPoint = DataModelToAWTHelper.getPointFromResource(resource, resolution);
		return new Rectangle(aPoint, aDimension);
	}

	/**
	 * @param task
	 * @return
	 */
	public final static Dimension getDimensionFromTask(TaskSimulator task) {
		// TODO add Z
		return DataModelToAWTHelper.getDimensionFromTask(task.getTaskDataModel());
	}

	/**
	 * @param task
	 * @return
	 */
	public final static Dimension getDimensionFromTask(TaskDataModel task) {
		// TODO add Z
		int height = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
		int width = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
		return new Dimension(width, height);
	}

	/**
	 * 
	 * @param shape
	 * @param width
	 * @param height
	 * @param resource
	 * @return
	 */
	public final static Shape createShape(String shape, int width, int height, ResourceDataModel resource) {
		String msg = ".createShape(): ";
		Shape createdShape = null;
		Point point = DataModelToAWTHelper.getPointFromResource(resource);
		double x = point.x;
		double y = point.y;
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			createdShape = new Ellipse2D.Double(x, y, width, width);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			createdShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			createdShape = new Rectangle((int) x, (int) y, width, height);
			break;
		default:
			RuntimeException runtimeException = new RuntimeException(msg + "shape not determined");
			// DataModelToAWTHelper.LOGGER.error(ExceptionUtils.getStackTrace(runtimeException));
		}
		return createdShape;
	}

	public final static Shape createShape(String shape, int x, int y, int width, int height) {
		String msg = ".createShape(): ";
		Shape createdShape = null;
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			createdShape = new Ellipse2D.Double(x, y, width, width);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			createdShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			createdShape = new Rectangle((int) x, (int) y, width, height);
			break;
		default:
			RuntimeException runtimeException = new RuntimeException(msg + "shape not determined");
			// DataModelToAWTHelper.LOGGER.error(ExceptionUtils.getStackTrace(runtimeException));
		}
		return createdShape;
	}

	/**
	 * 
	 * @param shape
	 * @param task
	 * @param resource
	 * @return
	 */
	public final static Shape createShape(String shape, TaskDataModel task, ResourceDataModel resource) {
		String msg = ".createShape(): ";
		Shape createdShape = null;
		Point point = DataModelToAWTHelper.getPointFromResource(resource);
		double x = point.x;
		double y = point.y;
		int width = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
		int height = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			createdShape = new Ellipse2D.Double(x, y, width, width);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			createdShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			createdShape = new Rectangle((int) x, (int) y, width, height);
			break;
		default:
			RuntimeException runtimeException = new RuntimeException(msg + "shape not determined");
			// DataModelToAWTHelper.LOGGER.error(ExceptionUtils.getStackTrace(runtimeException));
		}
		return createdShape;
	}

	/**
	 * 
	 * @param taskSimulator
	 * @param resource
	 * @return
	 */
	public final static Shape createShape(TaskSimulator taskSimulator, ResourceSimulator resourceSimulator) {
		String msg = ".createShape(): ";
		ResourceDataModel resource = resourceSimulator.getResourceDataModel();
		TaskDataModel task = taskSimulator.getTaskDataModel();
		String shape = task.getProperty(MapToResourcesAndTasks.SHAPE_TYPE_NAME);
		Shape createdShape = null;
		Point point = DataModelToAWTHelper.getPointFromResource(resource);
		double x = point.x;
		double y = point.y;
		int width = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
		int height = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			createdShape = new Ellipse2D.Double(x, y, width, width);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			createdShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			createdShape = new Rectangle((int) x, (int) y, width, height);
			break;
		default:
			RuntimeException runtimeException = new RuntimeException(msg + "shape not determined");
			// DataModelToAWTHelper.LOGGER.error(ExceptionUtils.getStackTrace(runtimeException));
		}
		return createdShape;
	}

	/**
	 * 
	 * @param point
	 * @param dimension
	 * @param shape
	 * @return
	 */
	public final static Shape createShape(Point point, Dimension dimension, String shape) {
		String msg = ".createShape(): ";
		Shape createdShape = null;
		double x = point.x;
		double y = point.y;
		int width = dimension.width;
		int height = dimension.height;
		switch (shape) {
		case MapToResourcesAndTasks.SHAPE_CIRCLE:
			createdShape = new Ellipse2D.Double(x, y, width, width);
			break;
		case MapToResourcesAndTasks.SHAPE_OVAL:
			createdShape = new Ellipse2D.Double(x, y, width, height);
			break;
		case MapToResourcesAndTasks.SHAPE_REACTANGLE:
			createdShape = new Rectangle((int) x, (int) y, width, height);
			break;
		default:
			RuntimeException runtimeException = new RuntimeException(msg + "shape not determined");
			// DataModelToAWTHelper.LOGGER.error(ExceptionUtils.getStackTrace(runtimeException));
		}
		return createdShape;
	}

	public static Shape rotateShape(int angle, Shape shape, double x, double y, String kindOfShape) {
		double deegreToRadians = angle * (Math.PI / 180);
		int initialX = (int) x;
		int initialY = (int) y;
		AffineTransform rotate = AffineTransform.getRotateInstance(deegreToRadians, x, y);
		Shape tempRotatedShape = rotate.createTransformedShape(shape);
		Shape rotatedShape = DataModelToAWTHelper.createShape(kindOfShape, initialX, initialY,
				tempRotatedShape.getBounds().width, tempRotatedShape.getBounds().height);
		LOGGER.debug("\nx: {} y: {} newX: {} newY:{}", x, y, rotatedShape.getBounds().x, rotatedShape.getBounds().y);
		return rotatedShape;
	}

	/**
	 * Does not return correct box dimensions TODO find how to fix the return
	 * argument of the second called method
	 * 
	 * @param task
	 * @return
	 */
	@Deprecated
	public final static Box getBoxFromTask(TaskSimulator task) {
		// TODO add Z
		return DataModelToAWTHelper.getBoxFromTask(task.getTaskDataModel());
	}

	/**
	 * @param resource
	 * @return
	 */
	@Deprecated
	public final static Box getBoxFromTask(TaskDataModel task) {
		// TODO add Z
		int height = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME));
		int length = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.HEIGHT_PROPERTY_NAME));
		int width = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME));
		return new Box(width, length, height);
	}

	public final static Point getPointFromResource(ResourceSimulator resource) {
		return DataModelToAWTHelper.getPointFromResource(resource.getResourceDataModel());
	}

	/**
	 * @param resource
	 * @return
	 */
	public final static Point getPointFromResource(ResourceDataModel resource) {
		int x = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME));
		int y = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME));
		return new Point(x, y);
	}

	public final static Point3D getPoint3DFromResource(ResourceSimulator resource) {
		return DataModelToAWTHelper.getPoint3DFromResource(resource.getResourceDataModel());
	}

	/**
	 * @param resource
	 * @return
	 */
	public final static Point3D getPoint3DFromResource(ResourceDataModel resource) {
		int x = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME));
		int y = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME));
		int z = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_Z_PROPERTY_NAME));
		return new Point3D(x, y, z);
	}

	/**
	 * @param task
	 * @return
	 */
	public final static Dimension getDimensionFromTask(TaskDataModel task, int resolution) {
		int height = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME)) * resolution;
		int width = Integer.parseInt(task.getProperty(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)) * resolution;
		return new Dimension(width, height);
	}

	/**
	 * @param resource
	 * @return
	 */
	public final static Point getPointFromResource(ResourceDataModel resource, int resolution) {
		int x = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_X_PROPERTY_NAME)) * resolution;
		int y = Integer.parseInt(resource.getProperty(MapToResourcesAndTasks.LOCATION_Y_PROPERTY_NAME)) * resolution;
		return new Point(x, y);
	}

	public final Shape createShape(ResourceDataModel resource, TaskDataModel task) {
		Shape newShape = null;

		return newShape;
	}
}
