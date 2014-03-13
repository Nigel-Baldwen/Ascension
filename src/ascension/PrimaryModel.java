package ascension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class PrimaryModel {

	// @formatter:off
	/* The game state model is comprised of the integration
	 * of two separate 2D arrays, one for units and one for terrain,
	 * into a third 2D array of ints which abstracts the minute
	 * detail of the former two arrays into a collection of readily
	 * processed numbers corresponding to various image
	 * representations. e.g. health bars, unit portraits,
	 * and transparent unit portraits.
	 */
	// @formatter:on
	
	private AbstractUnit[][] units;
	private int[][] terrain, visualModel;
	private int turnLength, percent, currentTurn, playerCount;
	private ActivityQueue actvivityQueue;
	private Timer turnTimer = new Timer(0, new ActionListener() {
		
		// At every tick of the clock the turn advances a bit closer
		// to completion. The view takes the current value of percent
		// for use in representing a clock face.

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (percent == 9) {
				turnTimer.setRepeats(false);
			}
			if (percent == 10) {
				percent = -1;
				rotateTurn();
			}
			percent++;
		}
	});
	
	public void loadInitialModelState(int size, int playerCount) {
		currentTurn = 1;
		this.playerCount = playerCount;
		
		// Clock instantiation
		percent = 0;
		turnLength = 10 * 1000;		
		turnTimer.setDelay(turnLength / 10);
		turnTimer.setRepeats(true);
		turnTimer.start();
		
		// Creates 2d arrays for terrain, units, and model.
		// Initializes them.
		terrain = new int[size][size];
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				terrain[r][c] = (int) (45 * Math.random() + 45);
			}
		}
		
		units  = new AbstractUnit[size][size];
		visualModel = new int[size][size * 2];
		
		// Initialize up to four players on the map
		units[0][0] = new RaeclarianManus(1, 0);
		units[0][0].setVisible(true);
		units[0][0].setActive(true);
		visualModel[0][0] = units[0][0].toInt();
		
		units[size - 1][size - 1] = new RaeclarianManus(2, ((size - 1) * size) + (size - 1)); // Converting row and column to an int
		visualModel[size - 1][size - 1] = units[size - 1][size - 1].toInt();
		
		
		if (playerCount > 2) {
			units[size - 1][0] = new RaeclarianManus(3, ((size - 1) * size));
			visualModel[size - 1][0] = units[size - 1][0].toInt();
		}
		
		if (playerCount > 3) {
			units[0][size - 1] = new RaeclarianManus(4, (size - 1));
			visualModel[0][size - 1] = units[0][size - 1].toInt();
		}
		
		for (int r = 0; r < size; r++) {
			for (int c = size; c < size * 2; c++) {
				visualModel[r][c] = terrain[r][c - size];
			}
		}
	}

	protected void rotateTurn() {
		if (currentTurn < playerCount) {
			currentTurn += 1;
			
			for (int r = 0; r < units.length; r++) {
				for (int c = 0; c < units.length; c++) {
					if (units[r][c] != null) {
						AbstractUnit temp = units[r][c];
						if (temp.getPlayer() == currentTurn) {
							temp.setVisible(true);
							if (temp.isDisabled()) {
								temp.setActive(false);
							}
							else {
								temp.setActive(true);
							}
						}
						else {
							if (temp.isVisibleBy(currentTurn)) {
								temp.setVisible(true);
							}
							else {
								temp.setVisible(false);
							}
							temp.setActive(false);
						}
						visualModel[r][c] = units[r][c].toInt();
					}
				}
			}
			turnTimer.setRepeats(true);
			turnTimer.start();
		}
		else {
			currentTurn = 1;
		}
	}

	public int[][] getVisualModel() {
		return visualModel;
	}

	public int getClockFace() {
		return percent;
	}

	public void transferUnit(int srcR, int srcC, int destR, int destC) {
		AbstractUnit temp = units[srcR][srcC];
		units[srcR][srcC] = units[destR][destC];
		units[destR][destC] = temp;
		visualModel[srcR][srcC] = 0;
		visualModel[destR][destC] = units[destR][destC].toInt();
	}
}
