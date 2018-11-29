package planning.model;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WorkcenterDataModel extends AbstractDataModel {
	private Vector<ResourceDataModel> resources = null;
	private String workcenterId = null;
	private String description = null;
	private String algorithm = null;
	private String name;
	private InventoryDataModel inventoryDataModel = null;

	public InventoryDataModel getInventoryDataModel() {
		return inventoryDataModel;
	}

	public Vector<DockingStationDataModel> getDockingStationDataModelVector() {
		return dockingStations;
	}

	private Vector<DockingStationDataModel> dockingStations = null;

	public WorkcenterDataModel(String workcenterId, String name, String description, String algorithm,
			Vector<ResourceDataModel> resources, InventoryDataModel inventoryDataModel,
			Vector<DockingStationDataModel> dockingStations) {
		this.workcenterId = workcenterId;
		this.description = description;
		this.algorithm = algorithm;
		this.resources = resources;
		this.name = name;
		this.inventoryDataModel = inventoryDataModel;
		this.dockingStations = dockingStations;
	}

	public Node toXMLNode(Document document) {
		Element workcenterElement = document.createElement("WORKCENTER");
		workcenterElement.setAttribute("id", this.workcenterId);

		Element nameElement = document.createElement("NAME");
		nameElement.appendChild(document.createTextNode(this.name));
		workcenterElement.appendChild(nameElement);

		Element descriptionElement = document.createElement("DESCRIPTION");
		descriptionElement.appendChild(document.createTextNode(this.description));
		workcenterElement.appendChild(descriptionElement);

		Element algorithmElement = document.createElement("ALGORITHM");
		algorithmElement.appendChild(document.createTextNode(this.algorithm));
		workcenterElement.appendChild(algorithmElement);

		for (int i = 0; i < this.resources.size(); i++) {
			Element resourceReferenceElement = document.createElement("WORKCENTER_RESOURCE_REFERENCE");
			resourceReferenceElement.setAttribute("refid", ((ResourceDataModel) this.resources.get(i)).getResourceId());
			workcenterElement.appendChild(resourceReferenceElement);
		}

		return workcenterElement;
	}

	public String getWorkcenterId() {
		return this.workcenterId;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public Vector<ResourceDataModel> getResourceDataModelVector() {
		return (Vector<ResourceDataModel>) this.resources.clone();
	}

	public ResourceDataModel getResourceDataModel(String resourceId) {
		for (int i = 0; i < resources.size(); i++) {
			ResourceDataModel resource = (ResourceDataModel) resources.get(i);
			if (resource.getResourceId().equals(resourceId))
				return resource;
		}
		return null;
	}

	public boolean containsResourceDataModel(ResourceDataModel resourceDataModel) {
		return resources.contains(resourceDataModel);
	}

	protected void setAlgorithm(String newAlgorithm) {
		this.algorithm = newAlgorithm;
	}

	protected void setDescription(String newDescription) {
		this.description = newDescription;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void addResourceDataModel(ResourceDataModel newResource) {
		this.resources.add(newResource);
	}

	protected void removeResourceDataModel(ResourceDataModel resource) {
		this.resources.remove(resource);
	}
}
