package planning.model;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ToolDataModel extends ResourceDataModel {
	ToolTypeDataModel type = null;

	public ToolTypeDataModel getType() {
		return type;
	}

	public ToolDataModel(String resourceId, String resourceName, String description,
			ResourceAvailabilityDataModel resourceAvailability, SetUpMatrixDataModel setUpMatrixDataModel,
			ToolTypeDataModel type, HashMap<String, String> properties) {
		super(resourceId, resourceName, description, resourceAvailability, setUpMatrixDataModel, null, properties);
		this.type = type;
	}

	@Override
	public Node toXMLNode(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

}
