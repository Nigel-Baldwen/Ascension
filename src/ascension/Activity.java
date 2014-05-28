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
	 * @param aP - absolute priority
	 * @param rP - relative priority
	 * @param aT - activity type
	 * @param pI - position index
	 * @param aD - activity destination
	 * @param aR - activity riders and effects
	 */
	
	public Activity (int aP, int rP, int aT, int pI, int aD, int aR) {
		activityKeys[0] = aP;
		activityKeys[1] = rP;
		activityKeys[2] = aT;
		activityKeys[3] = pI;
		activityKeys[4] = aD;
		activityKeys[5] = aR;
	}

	/*
	 * Activity Keys are broken down into the following sections.
	 * activityKeys[0] : absolute priority; this priority is
	 * 		compared to the activities of other units
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
}