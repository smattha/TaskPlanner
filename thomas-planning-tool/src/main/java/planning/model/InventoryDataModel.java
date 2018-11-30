package planning.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class InventoryDataModel extends AbstractDataModel {

    HashMap<ToolTypeDataModel, ToolDataModel[]> storages = new HashMap<ToolTypeDataModel, ToolDataModel[]>();

    public InventoryDataModel(HashMap<ToolTypeDataModel, Integer> maximumToolTypesCapacities, Vector<ToolDataModel> currentLoad) {
        for (Entry<ToolTypeDataModel, Integer> entry : maximumToolTypesCapacities.entrySet()) {
            ToolTypeDataModel toolType = entry.getKey();
            Integer capacity = entry.getValue();
            ToolDataModel[] storage = new ToolDataModel[capacity.intValue()];
            storages.put(toolType, storage);
        }
        for (ToolDataModel toolDataModel : currentLoad) {
            addTool(toolDataModel);
        }
    }

    public Vector<ToolDataModel> getTools() {
        Vector<ToolDataModel> tools = new Vector<ToolDataModel>();
        for (Map.Entry<ToolTypeDataModel, ToolDataModel[]> entry : storages.entrySet()) {
            ToolDataModel[] currentLoad = entry.getValue();
            for (int i = 0; i < currentLoad.length; i++) {
                ToolDataModel tool = currentLoad[i];
                if (tool != null) {
                    tools.add(tool);
                }
            }
        }
        return tools;
    }

    public Vector<ToolTypeDataModel> getSuportedToolTypes() {
        Vector<ToolTypeDataModel> supportedToolTypes = new Vector<ToolTypeDataModel>();
        for (Map.Entry<ToolTypeDataModel, ToolDataModel[]> entry : storages.entrySet()) {
            ToolTypeDataModel supportedType = entry.getKey();
            supportedToolTypes.add(supportedType);
        }
        return supportedToolTypes;
    }

    @Override
    public Node toXMLNode(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public void addTool(ToolDataModel tool) {
        ToolDataModel[] currentTools = storages.get(tool.getType());
        boolean attached = false;
        for (int i = 0; i < currentTools.length; i++) {
            ToolDataModel currentTool = currentTools[i];
            if (currentTool == null) {
                currentTools[i] = tool;
                attached = true;
                break;
            }
        }
        if (!attached) {
            throw new RuntimeException("MUST FIRST UNLOAD. NO ROOM FOR TOOL LOADING.");
        }
    }

    public void removeTool(ToolDataModel tool) {
        ToolDataModel[] currentTools = storages.get(tool.getType());
        for (int i = 0; i < currentTools.length; i++) {
            ToolDataModel currentTool = currentTools[i];
            if (currentTool.getResourceId().equals(tool.getResourceId())) {
                currentTools[i] = null;
                break;
            }
        }
    }

    public int getFreeSlotsForToolType(ToolTypeDataModel toolTypeDataModel) {
        int freeSlots = 0;
        ToolDataModel[] currentTools = storages.get(toolTypeDataModel);
        for (int i = 0; i < currentTools.length; i++) {
            ToolDataModel currentTool = currentTools[i];
            if (currentTool == null) {
                freeSlots++;
            }
        }
        return freeSlots;
    }
}
