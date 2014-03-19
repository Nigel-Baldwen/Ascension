package ascension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class PrimaryModel {

	// @formatter:off
	/* The game state model is comprised of the integration
	 * of up to eight 2D arrays, 4 for units and 4 for terrain,
	 * (one per player) into four 2D arrays of ints which
	 * abstract the details of the former eight arrays into a 
	 * collection of readily processed numbers corresponding to 
	 * various image representations. e.g. health bars, 
	 * unit portraits, and transparent unit portraits.
	 * 
	 * *A separate triple of arrays in necessary for each player
	 * in order to reflect the various vision and control rules.
	 * 
	 * *Requirement applies to off-line/single-machine variant.
	 */
	// @formatter:on
	
	private AbstractUnit[][] unitsP1, unitsP2, unitsP3, unitsP4;
	private int[][] terrainP1, terrainP2, terrainP3, terrainP4, visualModelP1, visualModelP2, visualModelP3, visualModelP4;
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
		terrainP1 = new int[size][size];
		terrainP2 = new int[size][size];
		terrainP3 = new int[size][size];
		terrainP4 = new int[size][size];
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				int tileID = (int) (45 * Math.random() + 45);
				terrainP1[r][c] = tileID;
				terrainP2[r][c] = tileID;
				terrainP3[r][c] = tileID;
				terrainP4[r][c] = tileID;
			}
		}
		
		unitsP1  = new AbstractUnit[size][size];
		unitsP2  = new AbstractUnit[size][size];
		unitsP3  = new AbstractUnit[size][size];
		unitsP4  = new AbstractUnit[size][size];
		visualModelP1 = new int[size][size * 2];
		visualModelP2 = new int[size][size * 2];
		visualModelP3 = new int[size][size * 2];
		visualModelP4 = new int[size][size * 2];
		
		// Initialize up to four players on the map
		// For testing purposes, all four players are grouped in upper left corner
		unitsP1[0][0] = new RaeclarianManus(1, 0);
		unitsP1[0][0].setVisible(true);
		unitsP1[0][0].setActive(true);
		visualModelP1[0][0] = unitsP1[0][0].toInt();
		
		// units[size - 1][size - 1] = new RaeclarianManus(2, ((size - 1) * size) + (size - 1)); // Converting row and column to an int
		// visualModel[size - 1][size - 1] = units[size - 1][size - 1].toInt();
		
		unitsP2[0][1] = new RaeclarianManus(2, 1); // Converting row and column to an int
		visualModelP2[0][1] = unitsP2[0][1].toInt();
		
		if (playerCount > 2) {
			// units[size - 1][0] = new RaeclarianManus(3, ((size - 1) * size));
			// visualModel[size - 1][0] = units[size - 1][0].toInt();
			
			unitsP3[1][0] = new RaeclarianManus(3, size);
			visualModelP3[1][0] = unitsP3[1][0].toInt();
		}
		
		if (playerCount > 3) {
			// units[0][size - 1] = new RaeclarianManus(4, (size - 1));
			// visualModel[0][size - 1] = units[0][size - 1].toInt();
			
			unitsP4[1][1] = new RaeclarianManus(4, size + 1);
			visualModelP4[1][1] = unitsP4[1][1].toInt();
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
