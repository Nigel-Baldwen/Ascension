package ascension;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class Activity {

	private int[] activityKeys = new int[6];

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
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
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int[] provideActivityKeys() {
		return activityKeys;
	}
}