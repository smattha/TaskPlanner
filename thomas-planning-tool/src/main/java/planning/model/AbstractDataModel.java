package planning.model;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import planning.view.ViewInterface;

abstract public class AbstractDataModel implements SubjectDataModelInterface {
    private Vector<ViewInterface> objervers = new Vector<ViewInterface>();

    public AbstractDataModel() {
    }

    public void attachObserver(ViewInterface objerver) {
        if (objervers.indexOf(objerver) == -1) {
            objervers.add(objerver);
            objerver.update(this);
        }
    }

    public void detachObserver(ViewInterface objerver) {
        if (objervers.indexOf(objerver) != -1) {
            objervers.remove(objerver);
        }
    }

    public void notifyObservers() {
        for (int i = 0; i < objervers.size(); i++) {
            ((ViewInterface) objervers.get(i)).update(this);
        }
    }

    public abstract Node toXMLNode(Document document);
}
