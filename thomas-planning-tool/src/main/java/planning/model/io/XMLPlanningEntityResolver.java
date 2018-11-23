package planning.model.io;

import java.io.InputStream;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class XMLPlanningEntityResolver implements EntityResolver {
    public InputSource resolveEntity(String publicId, String systemId) {
        // TODO : MIGRATE TO JAXB
        if ((systemId != null) && (true)) {
            // return a special input source
            try {
                if (systemId.endsWith("planninginput.dtd")) {
                    URL url = this.getClass().getResource("dtd\\planninginput.dtd");
                    InputStream stream = url.openStream();
                    InputSource is = new InputSource(stream);
                    return is;
                } else if (systemId.endsWith("planningoutput.dtd")) {
                    URL url = this.getClass().getResource("dtd\\planningoutput.dtd");
                    InputStream stream = url.openStream();
                    InputSource is = new InputSource(stream);
                    return is;
                } else {
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        // else
        {
            // use the default behavior
            return null;
        }
    }
}