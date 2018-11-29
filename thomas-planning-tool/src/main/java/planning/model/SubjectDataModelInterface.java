package planning.model;

import planning.view.ViewInterface;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public interface SubjectDataModelInterface {
	public void attachObserver(ViewInterface objerver);

	public void detachObserver(ViewInterface objerver);

	public void notifyObservers();
}