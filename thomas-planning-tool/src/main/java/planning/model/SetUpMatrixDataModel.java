package planning.model;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SetUpMatrixDataModel extends AbstractDataModel {
    public static final String FROM_CODE = "FROM_CODE";
    public static final String TO_CODE = "TO_CODE";
    public static final String TIME_IN_MILLISECONDS = "TIME_IN_MILLISECONDS";
    private String id = null;
    private Vector<HashMap<String, Object>> setUpCodesAndTimes = new Vector<HashMap<String, Object>>();

    public SetUpMatrixDataModel(String id) {
        this.id = id;
    }

    public Node toXMLNode(Document document) {
        Element setUpMatrixElement = document.createElement("SET_UP_MATRIX");
        setUpMatrixElement.setAttribute("id", this.id);

        for (int i = 0; i < setUpCodesAndTimes.size(); i++) {
            HashMap<String, Object> details = setUpCodesAndTimes.get(i);

            Element setUpElement = document.createElement("SET_UP");

            Element fromCodeElement = document.createElement("FROM_CODE");
            fromCodeElement.appendChild(document.createTextNode((String) details.get(FROM_CODE)));
            setUpElement.appendChild(fromCodeElement);

            Element toCodeElement = document.createElement("TO_CODE");
            toCodeElement.appendChild(document.createTextNode((String) details.get(TO_CODE)));
            setUpElement.appendChild(toCodeElement);

            Element timeInSecondsElement = document.createElement("TIME_IN_SECONDS");
            timeInSecondsElement.appendChild(document.createTextNode("" + (((Long) details.get(TIME_IN_MILLISECONDS)).longValue() / 1000)));
            setUpElement.appendChild(timeInSecondsElement);

            setUpMatrixElement.appendChild(setUpElement);
        }

        return setUpMatrixElement;
    }

    public void addSetUp(String fromSetUpCode, String toSetUpCode, long timeInMilliseconds) {
        HashMap<String, Object> details = new HashMap<String, Object>();
        details.put(FROM_CODE, fromSetUpCode);
        details.put(TO_CODE, toSetUpCode);
        details.put(TIME_IN_MILLISECONDS, new Long(timeInMilliseconds));

        setUpCodesAndTimes.add(details);
    }

    public long getSetUpTimeInMilliseconds(String fromSetUpCode, String toSetUpCode) {
        if (fromSetUpCode == null || toSetUpCode == null) {
            // throw new RuntimeException("Function argument cannot be null");
            return 0;
        }
        for (int i = 0; i < setUpCodesAndTimes.size(); i++) {
            HashMap<String, Object> details = setUpCodesAndTimes.get(i);
            if (fromSetUpCode.equals(details.get(FROM_CODE)) && toSetUpCode.equals(details.get(TO_CODE))) {
                return ((Long) details.get(TIME_IN_MILLISECONDS)).longValue();
            }
        }
        // throw new
        // RuntimeException("Set up for codes FROM : "+fromSetUpCode+" TO : "+toSetUpCode+" cannot be found");
        return 0;
    }

    public String[] getSetUpCodes() {
        Vector<String> setUpCodesVector = new Vector<String>();
        for (int i = 0; i < setUpCodesAndTimes.size(); i++) {
            HashMap<String, Object> details = setUpCodesAndTimes.get(i);

            String fromCode = (String) details.get(FROM_CODE);
            if (setUpCodesVector.indexOf(fromCode) == -1) {
                setUpCodesVector.add(fromCode);
            }

            String toCode = (String) details.get(TO_CODE);
            if (setUpCodesVector.indexOf(toCode) == -1) {
                setUpCodesVector.add(toCode);
            }
        }

        String[] setUpCodes = new String[setUpCodesVector.size()];
        for (int i = 0; i < setUpCodes.length; i++) {
            setUpCodes[i] = setUpCodesVector.get(i);
        }
        return setUpCodes;
    }

    public String getId() {
        return this.id;
    }
}
