package ascension;

import java.util.ArrayList;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class ActivityQueue extends ArrayList<ActivityList> {

	/**
	 * Processes the Queue and returns an ordered list.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#rotateTurn() rotateTurn()}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link ActivityList#organize() organize()}
	 * </ul>
	 * <p>
	 * 
	 * @return the processed list
	 */
	
	public ActivityList process() {

		ActivityList result = new ActivityList();

		for (int i = 0; i < size(); i++) {
			get(i).organize();
			for (int j = 0; j < get(i).size(); j++) {
				result.add(get(i).get(j));
			}
		}

		return result;
	}
}
