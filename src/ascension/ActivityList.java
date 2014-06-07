package ascension;

import java.util.ArrayList;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class ActivityList extends ArrayList<Activity> {

	/**
	 * Organizes this <code>ActivityList</code>.
	 * 
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link ActivityQueue#process() process()}
	 * </ul>
	 */
	
	public void organize() {
		// Do nothing for now as lists will always be in order
	}
}