package hibernate;
// Generated Jan 25, 2019 5:59:11 PM by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Operationsdb generated by hbm2java
 */
public class Operationsdb implements java.io.Serializable {

	private int id;
	private Toolsdb toolsdb;
	private Workingareadb workingareadb;
	private String name;
	private String description;
	private String type;
	private Set actionsdbs = new HashSet(0);
	private Set parts = new HashSet(0);
	private Set tasksdbs = new HashSet(0);

	public Operationsdb() {
	}

	public Operationsdb(int id, Toolsdb toolsdb, Workingareadb workingareadb) {
		this.id = id;
		this.toolsdb = toolsdb;
		this.workingareadb = workingareadb;
	}

	public Operationsdb(int id, Toolsdb toolsdb, Workingareadb workingareadb, String name, String description,
			String type, Set actionsdbs, Set parts, Set tasksdbs) {
		this.id = id;
		this.toolsdb = toolsdb;
		this.workingareadb = workingareadb;
		this.name = name;
		this.description = description;
		this.type = type;
		this.actionsdbs = actionsdbs;
		this.parts = parts;
		this.tasksdbs = tasksdbs;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Toolsdb getToolsdb() {
		return this.toolsdb;
	}

	public void setToolsdb(Toolsdb toolsdb) {
		this.toolsdb = toolsdb;
	}

	public Workingareadb getWorkingareadb() {
		return this.workingareadb;
	}

	public void setWorkingareadb(Workingareadb workingareadb) {
		this.workingareadb = workingareadb;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set getActionsdbs() {
		return this.actionsdbs;
	}

	public void setActionsdbs(Set actionsdbs) {
		this.actionsdbs = actionsdbs;
	}

	public Set getParts() {
		return this.parts;
	}

	public void setParts(Set parts) {
		this.parts = parts;
	}

	public Set getTasksdbs() {
		return this.tasksdbs;
	}

	public void setTasksdbs(Set tasksdbs) {
		this.tasksdbs = tasksdbs;
	}

}
