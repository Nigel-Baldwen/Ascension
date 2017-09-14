package ascension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

class ActivityList extends ArrayList<Activity> {

	/**
	 * Organizes this <code>ActivityList</code>.
	 * 
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link ActivityQueue#process() process()}
	 * </ul>
	 */
	void organize() {
		if(size() < 2)
			return;

		Collections.sort((List) this);
	}
}