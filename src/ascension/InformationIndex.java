package ascension;

// Intended as a sort of Library reference class
// Currently incomplete

/**
 * Intended as a sort of library/reference class.
 * May or may not see actual use.
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class InformationIndex {

	/**
	 * Returns a unit's sight radius.
	 * 
	 * @param idTag - the unit's type
	 * @return the unit's sight radius
	 */
	public static int getSightRadius(int idTag) {
		// For now, only one SR - 6
		return 6;
	}

	/**
	 * Returns a unit's movement radius.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * </ul>
	 * 
	 * @param idTag - the unit's type
	 * @return the unit's movement radius
	 */
	public static int getMovementRadius(int idTag) {
		// For now, only one MR - 6
		return 6;
	}

	/**
	 * Returns a unit's name.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * </ul>
	 * 
	 * @param idTag - the unit's type
	 * @return the unit's name
	 */
	public static String getName(int idTag) {
		// For now, only one name - Raeclarian Manus
		return "Raeclarian Manus";
	}

}
