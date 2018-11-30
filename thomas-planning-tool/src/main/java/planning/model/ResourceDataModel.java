package planning.model;

import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ResourceDataModel extends AbstractDataModel {
    private ResourceAvailabilityDataModel resourceAvailability = null;
    private String resourceId = null;
    private String resourceName = null;
    private String description = null;
    private SetUpMatrixDataModel setUpMatrixDataModel = null;
    private HashMap<String, String> properties = new HashMap<String, String>();
    private InventoryDataModel endEffectorsDataModel = null;

    public InventoryDataModel getEndEffectorsDataModel() {
        return endEffectorsDataModel;
    }

    public ResourceDataModel(String resourceId, String resourceName, String description, ResourceAvailabilityDataModel resourceAvailability, SetUpMatrixDataModel setUpMatrixDataModel, InventoryDataModel endEffectorsDataModel, HashMap<String, String> properties) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.description = description;
        this.resourceAvailability = resourceAvailability;
        this.setUpMatrixDataModel = setUpMatrixDataModel;
        if (properties != null)
            this.properties = properties;
        this.endEffectorsDataModel = endEffectorsDataModel;
    }

    public Node toXMLNode(Document document) {
        Element resourceElement = document.createElement("RESOURCE");
        resourceElement.setAttribute("id", this.resourceId);

        Element nameElement = document.createElement("NAME");
        nameElement.appendChild(document.createTextNode(this.resourceName));
        resourceElement.appendChild(nameElement);

        Element descriptionElement = document.createElement("DESCRIPTION");
        descriptionElement.appendChild(document.createTextNode(this.description));
        resourceElement.appendChild(descriptionElement);

        Element setUpMatrixReferenceElement = document.createElement("SET_UP_MATRIX_REFERENCE");
        if (this.setUpMatrixDataModel != null) {
            setUpMatrixReferenceElement.setAttribute("refid", this.setUpMatrixDataModel.getId());
        }
        resourceElement.appendChild(setUpMatrixReferenceElement);

        resourceElement.appendChild(this.resourceAvailability.toXMLNode(document));

        Element propertiesElement = document.createElement("PROPERTIES");
        resourceElement.appendChild(propertiesElement);

        if(this.properties!=null) {
	        for (Entry<String,String> entry : properties.entrySet()) {
	            Element propertyElement = document.createElement("PROPERTY");
	            Element propertyNameElement = document.createElement("NAME");
	            propertyNameElement.appendChild(document.createTextNode(entry.getKey()));
	            propertyElement.appendChild(propertyNameElement);
	            Element propertyValueElement = document.createElement("VALUE");
	            propertyValueElement.appendChild(document.createTextNode(entry.getValue()));
	            propertyElement.appendChild(propertyValueElement);
	            propertiesElement.appendChild(propertyElement);
			}
        }
        
        return resourceElement;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public ResourceAvailabilityDataModel getResourceAvailabilityDataModel() {
        return this.resourceAvailability;
    }

    protected void setResourceName(String newName) {
        this.resourceName = newName;
    }

    protected void setResourceAvailabilityDataModel(ResourceAvailabilityDataModel newResourceAvailabilityDataModel) {
        this.resourceAvailability = newResourceAvailabilityDataModel;
    }

    public SetUpMatrixDataModel getSetUpMatrixDataModel() {
        return this.setUpMatrixDataModel;
    }

    protected void setProperty(String propertyName, String propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
}
