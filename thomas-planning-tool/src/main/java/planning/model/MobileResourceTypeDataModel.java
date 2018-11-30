package planning.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MobileResourceTypeDataModel extends AbstractDataModel {

    private String id;

    private String type = null;

    public String getType() {
        return this.type;
    }

    public MobileResourceTypeDataModel(String id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public Node toXMLNode(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getId() {
        return this.id;
    }
}
