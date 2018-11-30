package planning.model;

import java.util.HashMap;

public class MobileResourceDataModel extends ResourceDataModel {

    MobileResourceTypeDataModel type = null;

    public MobileResourceTypeDataModel getType() {
        return type;
    }

    public MobileResourceDataModel(String resourceId, String resourceName, String description, ResourceAvailabilityDataModel resourceAvailability, SetUpMatrixDataModel setUpMatrixDataModel, InventoryDataModel inventoryDataModel, MobileResourceTypeDataModel type, HashMap<String, String> properties) {
        super(resourceId, resourceName, description, resourceAvailability, setUpMatrixDataModel, inventoryDataModel, properties);
        this.type = type;
    }

}
