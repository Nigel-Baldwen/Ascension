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
		generateMap(terrainP1);
		unitsP1  = new AbstractUnit[size][size];
		int x = (int) (Math.random() * size / 4);
		int y = (int) (Math.random() * size / 4);
		unitsP1[x][y] = new RaeclarianManus(1, size * x + y);
		unitsP1[x][y].setVisible(true);
		unitsP1[x][y].setActive(true);
		visualModelP1 = new int[size][size * 2];
		generateVisualModel(visualModelP1, unitsP1, terrainP1);
		
		terrainP2 = new int[size][size];
		copyMap(terrainP1, terrainP2);
		unitsP2  = new AbstractUnit[size][size];
		x = (int) (Math.random() * size / 4 + size * 3 / 4);
		y = (int) (Math.random() * size / 4 + size * 3 / 4);
		unitsP2[x][y] = new RaeclarianManus(2, size * x + y);
		unitsP2[x][y].setVisible(true);
		unitsP2[x][y].setActive(true);
		visualModelP2 = new int[size][size * 2];
		generateVisualModel(visualModelP2, unitsP2, terrainP2);
		
		if (playerCount > 2) {
			terrainP3 = new int[size][size];
			copyMap(terrainP1, terrainP3);
			unitsP3  = new AbstractUnit[size][size];
			x = (int) (Math.random() * size / 4);
			y = (int) (Math.random() * size / 4 + size * 3 / 4);
			unitsP3[x][y] = new RaeclarianManus(3, size * x + y);
			unitsP3[x][y].setVisible(true);
			unitsP3[x][y].setActive(true);
			visualModelP3 = new int[size][size * 2];
			generateVisualModel(visualModelP3, unitsP3, terrainP3);
		}
		
		if (playerCount > 3) {
			terrainP4 = new int[size][size];
			copyMap(terrainP1, terrainP4);
			unitsP4  = new AbstractUnit[size][size];
			x = (int) (Math.random() * size / 4 + size * 3 / 4);
			y = (int) (Math.random() * size / 4);
			unitsP4[x][y] = new RaeclarianManus(4, size * x + y);
			unitsP4[x][y].setVisible(true);
			unitsP4[x][y].setActive(true);
			visualModelP4 = new int[size][size * 2];
			generateVisualModel(visualModelP4, unitsP4, terrainP4);
		}
	}

	private void generateVisualModel(int[][] visM, AbstractUnit[][] units, int[][] terrain) {
		for (int r = 0; r < units.length; r++) {
			for (int c = 0; c < units.length; c++) {
				visM[r][c] = units[r][c] != null ? units[r][c].toInt() : 0;
				visM[r][c + units.length] = terrain[r][c];
			}
		}
	}

	private void copyMap(int[][] origin, int[][] dest) {
		for (int r = 0; r < origin.length; r++) {
			for (int c = 0; c < origin.length; c++) {
				dest[r][c] = origin[r][c];
			}
		}
	}

	private void generateMap(int[][] dest) {
		for (int r = 0; r < dest.length; r++) {
			for (int c = 0; c < dest.length; c++) {
				int tileID = (int) (45 * Math.random() + 45);
				dest[r][c] = tileID;
			}
		}
	}

	protected void rotateTurn() {
		if (currentTurn < playerCount) {
			currentTurn += 1;
			turnTimer.setRepeats(true);
			turnTimer.start();
		}
		else {
			currentTurn = 1;
		}
	}

	public int[][] getVisualModel() {
		switch (currentTurn) {
		case 1:
			return visualModelP1;
		
		case 2:
			return visualModelP2;
			
		case 3:
			return visualModelP3;
			
		default:
			return visualModelP4;
		}
	}

	public int getClockFace() {
		return percent;
	}

	public void transferUnit(int srcR, int srcC, int destR, int destC) {
		AbstractUnit temp;
		
		switch (currentTurn) {
		case 1:
			temp = unitsP1[srcR][srcC];
			unitsP1[srcR][srcC] = unitsP1[destR][destC];
			unitsP1[destR][destC] = temp;
			visualModelP1[srcR][srcC] = 0;
			visualModelP1[destR][destC] = unitsP1[destR][destC].toInt();
			break;
			
		case 2:
			temp = unitsP2[srcR][srcC];
			unitsP2[srcR][srcC] = unitsP2[destR][destC];
			unitsP2[destR][destC] = temp;
			visualModelP2[srcR][srcC] = 0;
			visualModelP2[destR][destC] = unitsP2[destR][destC].toInt();
			break;
			
		case 3:
			temp = unitsP3[srcR][srcC];
			unitsP3[srcR][srcC] = unitsP3[destR][destC];
			unitsP3[destR][destC] = temp;
			visualModelP3[srcR][srcC] = 0;
			visualModelP3[destR][destC] = unitsP3[destR][destC].toInt();
			break;

		default:
			temp = unitsP4[srcR][srcC];
			unitsP4[srcR][srcC] = unitsP4[destR][destC];
			unitsP4[destR][destC] = temp;
			visualModelP4[srcR][srcC] = 0;
			visualModelP4[destR][destC] = unitsP4[destR][destC].toInt();
			break;
		}
	}
}