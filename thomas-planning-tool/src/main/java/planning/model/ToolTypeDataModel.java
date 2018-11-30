package planning.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ToolTypeDataModel extends AbstractDataModel {

    private String id = null;

    public String getId() {
        return id;
    }

    private String type = null;

    public String getType() {
        return this.type;
    }

    public ToolTypeDataModel(String id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public Node toXMLNode(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

}
