package ascension;

import java.util.ArrayList;
import java.util.Collections;

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

		for(int i = 0; i < size() - 1; i++) {
			for (int j = i + 1; j < size(); j++) {
				if (!get(i).comparePriority(get(j))); {
					Collections.swap(this, i, j);
				}
			}
		}
	}
}