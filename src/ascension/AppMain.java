package ascension;

// Creates new instance of game and initializes.
// Starts game.

public class AppMain {
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
