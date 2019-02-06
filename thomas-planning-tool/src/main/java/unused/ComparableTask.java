/**
 * 
 */
package unused;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;

import org.apache.commons.lang3.builder.EqualsBuilder;

import eu.robopartner.ps.planner.planninginputmodel.PROPERTY;
import eu.robopartner.ps.planner.planninginputmodel.TASK;

/**
 * @author Spyros Created to implement comparator by size
 */
public class ComparableTask extends TASK implements Comparable<TASK> {

	/**
	 * 
	 */
	public ComparableTask(TASK aTask) {
		super();
		this.setDESCRIPTION(aTask.getDESCRIPTION());
		this.setNAME(aTask.getNAME());
		this.setId(aTask.getId());
		this.setPROPERTIES(aTask.getPROPERTIES());
	}

	/**
	 * @return this as a Task
	 */
	public final TASK getTask() {
		return (TASK) this;
	}

	/**
	 * @param obj
	 * @return
	 */
	public boolean equals(TASK obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ComparableTask rhs = (ComparableTask) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(this.getDESCRIPTION(), rhs.getDESCRIPTION())
				.append(this.getId(), rhs.getId()).append(this.getNAME(), rhs.getNAME())
				.append(this.getPROPERTIES(), rhs.getPROPERTIES()).isEquals();
	}

	/**
	 * @return
	 */
	public int getSquareSize() {
		return ComparableTask.getSquareSize(this);
	}

	/**
	 * @return
	 */
	public static int getSquareSize(TASK aTask) {
		int width = 0;
		int height = 0;
		if (aTask != null && aTask.getPROPERTIES() != null && aTask.getPROPERTIES().getPROPERTY() != null) {
			for (PROPERTY aProperty : aTask.getPROPERTIES().getPROPERTY()) {
				if (aProperty.getNAME() != null
						&& aProperty.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME)) {
					try {
						height = Integer.parseInt(aProperty.getVALUE());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				if (aProperty.getNAME() != null
						&& aProperty.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)) {
					try {
						width = Integer.parseInt(aProperty.getVALUE());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return width * height;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return ComparableTask.getWidth(this);
	}

	/**
	 * @return
	 */
	public static int getWidth(TASK aTask) {
		int width = 0;

		if (aTask != null && aTask.getPROPERTIES() != null && aTask.getPROPERTIES().getPROPERTY() != null) {
			for (PROPERTY aProperty : aTask.getPROPERTIES().getPROPERTY()) {

				if (aProperty.getNAME() != null
						&& aProperty.getNAME().equals(MapToResourcesAndTasks.WIDTH_PROPERTY_NAME)) {
					try {
						width = Integer.parseInt(aProperty.getVALUE());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return width;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return ComparableTask.getHeight(this);
	}

	/**
	 * @return
	 */
	public static int getHeight(TASK aTask) {
		int height = 0;

		if (aTask != null && aTask.getPROPERTIES() != null && aTask.getPROPERTIES().getPROPERTY() != null) {
			for (PROPERTY aProperty : aTask.getPROPERTIES().getPROPERTY()) {

				if (aProperty.getNAME() != null
						&& aProperty.getNAME().equals(MapToResourcesAndTasks.LENGTH_PROPERTY_NAME)) {
					try {
						height = Integer.parseInt(aProperty.getVALUE());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return height;
	}

	/*
	 * Compares first dimensions (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TASK obj) {
		int lastCmp = Integer.compare(this.getSquareSize(), ComparableTask.getSquareSize(obj));
		lastCmp = (lastCmp != 0 ? lastCmp : Integer.compare(this.getWidth(), ComparableTask.getWidth(obj)));
		lastCmp = (lastCmp != 0 ? lastCmp : Integer.compare(this.getHeight(), ComparableTask.getHeight(obj)));
		if (obj != null) {
			if (obj.getId() != null) {
				lastCmp = (lastCmp != 0 ? lastCmp : this.getId().compareTo(obj.getId()));
			}
			if (obj.getNAME() != null) {
				lastCmp = (lastCmp != 0 ? lastCmp : this.getNAME().compareTo(obj.getNAME()));
			}
			if (obj.getDESCRIPTION() != null) {
				lastCmp = (lastCmp != 0 ? lastCmp : this.getDESCRIPTION().compareTo(obj.getDESCRIPTION()));
			}
		}

		return lastCmp;
	}

}
