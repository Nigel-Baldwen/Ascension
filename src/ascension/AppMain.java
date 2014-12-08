package ascension;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class AppMain {
	
	/**
	 * Creates and runs a new game.
	 * 
	 * <p>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link PrimaryController PrimaryController}
     * </ul>
	 * <b>Calls</b> - 
	 * <ul>
	 * <li> {@link PrimaryController#loadInitialGameState() loadInitialGameState()}
	 * <li> {@link PrimaryController#startGame() startGame()}
     * </ul>
	 * </p>
	 * 
	 */
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PrimaryController p = new PrimaryController();
				p.loadInitialGameState();
				p.startGame();
			}
		});
	}
}
