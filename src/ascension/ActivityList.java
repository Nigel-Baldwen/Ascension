package ascension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

class ActivityList extends ArrayList<Activity> implements Comparable<ActivityList> {

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

	@Override
	public int compareTo(ActivityList o) {
		// This activity serves to help sort out which activity ought to go first in case of conflicts
		// Currently, an activity list is considered prior to another iff its zeroth activity has
		// a lower coinFlipValue than the other activity's coinFlipValue
		if (this.get(0).getCoinFlipValue() < o.get(0).getCoinFlipValue())
			return -1;
		if (this.get(0).getCoinFlipValue() > o.get(0).getCoinFlipValue())
			return 1;
		return 0;
	}
}