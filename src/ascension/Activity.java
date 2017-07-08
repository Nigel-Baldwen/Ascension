package ascension;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class Activity {

	private int[] activityKeys = new int[8];
	private ArrayList<Point> squaresOccupied, origin, target;
	
	/** TODO Fix this comment
	 * Makes a new activity.
	 * 
	 * @param tier - absolute priority
	 * @param relativePriority - relative priority
	 * @param activityType - activity type
	 * @param rowS - the row of the source unit
	 * @param colS - the column of the source unit
	 * @param rowT - the row of the target
	 * @param colT - the column of the target
	 * @param activityRiders - activity riders and effects
	 */
	public Activity (int tier, int relativePriority, int activityType, int activityRiders, ArrayList<Point> _origin, ArrayList<Point> _target, ArrayList<Point> _squaresOccupied) {
		activityKeys[0] = tier;
		activityKeys[1] = relativePriority;
		activityKeys[2] = activityType;
		activityKeys[7] = activityRiders;
		origin = _origin; // The squares the activity originates from
		target = _target; // The squares affected by the effect of this activity
		squaresOccupied = _squaresOccupied; // The squares the unit is considered to occupy during the activity
	}

	/*
	 * Activity Keys are broken down into the following sections.
	 * activityKeys[0] : tier; this is a reflection of the placement
	 * 		of the activity in relation to the phases
	 * activityKeys[1] : relative priority; this priority is
	 * 		compared to activities within the same activity list
	 * activityKeys[2] : type of action
	 * 		Possible Values :
	 * 			0 : Movement
	 * 			1 : Attack
	 * 			3 : Use Ability
	 * activityKeys[3] : row of source unit
	 * activityKeys[4] : column of source unit
	 * activityKeys[5] : row of target
	 * activityKeys[6] : column of target
	 * activityKeys[7] : special riders or effects to be applied
	 * 		by an ability
	 */

	/**
	 * Returns the activity keys of this activity.
	 * 
	 * @return the activity keys
	 */
	public int[] provideActivityKeys() {
		return activityKeys;
	}

	public boolean comparePriority(Activity otherAct) {
		// TODO Implement a reasonable comparison for two activities.
		// Keep in mind key-frame divisions as well as order of entry
		// into the queue. Perhaps add some keys for those ideas.
		// If the otherAct should occur after thisAct, then return true.
		return false;
	}
}