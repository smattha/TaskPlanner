package planning.model;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DockingStationDataModel extends AbstractDataModel {
    Vector<MobileResourceTypeDataModel> supportedTypes = null;
    MobileResourceDataModel currentLoad = null;

    public MobileResourceDataModel getCurrentLoad() {
        return currentLoad;
    }

    public DockingStationDataModel(Vector<MobileResourceTypeDataModel> supportedTypes, MobileResourceDataModel currentLoad) {
        this.supportedTypes = supportedTypes;
        this.currentLoad = currentLoad;
    }

    @Override
    public Node toXMLNode(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public Vector<MobileResourceTypeDataModel> getSuportedMobileResourceTypes() {
        return supportedTypes;
    }

    public void setCurrentLoad(MobileResourceDataModel resourceDataModel) {
        this.currentLoad = resourceDataModel;
    }

}
