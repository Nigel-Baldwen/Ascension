package ascension;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class Activity {

	private int[] activityKeys = new int[6];

	/**
	 * Makes a new activity.
	 * 
	 * @param tier - absolute priority
	 * @param rP - relative priority
	 * @param aT - activity type
	 * @param pI - position index
	 * @param aD - activity destination
	 * @param aR - activity riders and effects
	 */
	public Activity (int tier, int rP, int aT, int pI, int aD, int aR) {
		activityKeys[0] = tier;
		activityKeys[1] = rP;
		activityKeys[2] = aT;
		activityKeys[3] = pI;
		activityKeys[4] = aD;
		activityKeys[5] = aR;
	}

	/*
	 * Activity Keys are broken down into the following sections.
	 * activityKeys[0] : tier; this is a reflection of the placement
	 * 		of the unit in relation to other classes of units
	 * 		it determines which bracket of activity this unit acts in.
	 * activityKeys[1] : relative priority; this priority is
	 * 		compared to activities within the same activity list
	 * activityKeys[2] : type of action
	 * 		Possible Values :
	 * 			0 : Movement
	 * 			1 : Attack
	 * 			3 : Use Ability
	 * activityKeys[3] : position index of calling unit
	 * activityKeys[4] : movement destination of calling unit
	 * 		|| attack target of calling unit
	 * activityKeys[5] : special riders or effects to be applied
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