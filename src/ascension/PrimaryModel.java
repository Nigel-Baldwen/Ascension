package ascension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * <p>
 * <code>PrimaryModel</code> is the <i>model</i> component of the
 * <b>Ascension</b> project. (Model-View-Controller paradigm)
 * </p>
 * 
 * <p>
 * <code>PrimaryModel</code> is comprised of the integration
 * of up to eight 2D arrays, 4 for units and 4 for terrain,
 * (one per player) into four 2D arrays of ints which
 * abstract the details of the former eight arrays into a 
 * collection of readily processed numbers corresponding to 
 * various image representations. e.g. health bars, 
 * unit portraits, and transparent unit portraits.
 * 
 * *A separate triple of arrays in necessary for each player
 * in order to reflect the various vision and control rules.
 * This requirement applies to the off-line/single-machine variant.
 * </p>
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class PrimaryModel {

	/**
	 * At every tick of the clock the turn advances a bit closer
	 * to completion.
	 * 
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#rotateTurn() rotateTurn()}
	 * </ul>
	 * </p>
	 */
	private Timer turnTimer;
	private AbstractUnit[][] unitsP1, unitsP2, unitsP3, unitsP4;
	private int[][] terrainP1, terrainP2, terrainP3, terrainP4, visualModelP1, visualModelP2, visualModelP3, visualModelP4;
	private int turnLength, percent, currentTurn, playerCount;
	private ActivityQueue activityQueue;

	/**
	 * Takes in a set of values from the <i>model</i> in order
	 * to instantiate various things such as the clock and arrays.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#loadInitialGameState() loadInitialGameState()}
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link PhysicalBuilder RaeclarianManus}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#generateMap(int[][]) generateMap(int[][])}
	 * <li> {@link PrimaryModel#copyMap(int[][], int[][]) copyMap(int[][], int[][])}
	 * <li> {@link PrimaryModel#generateVisualModel(int[][], AbstractUnit[][], int[][]) generateVisualModel(int[][], AbstractUnit[][], int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param size - the number of squares in the square map
	 * @param playerCount - the number of players in the match
	 */
	public void loadInitialModelState(int size, int playerCount) {
		currentTurn = 1;
		this.playerCount = playerCount;

		// Clock instantiation
		percent = 0;
		turnLength = 10 * 1000;
		turnTimer = new Timer(0, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (percent == 9) {
					turnTimer.setRepeats(false);
				}
				if (percent == 10) {
					percent = 0;
					rotateTurn();
				}
				percent++;
			}
		});
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
		unitsP1[x][y] = new PhysicalBuilder(1, size * x + y);
		unitsP1[x][y].setVisible(true);
		unitsP1[x][y].setActive(true);
		visualModelP1 = new int[size][size * 2];
		generateVisualModel(visualModelP1, unitsP1, terrainP1);

		terrainP2 = new int[size][size];
		copyMap(terrainP1, terrainP2);
		unitsP2  = new AbstractUnit[size][size];
		x = (int) (Math.random() * size / 4 + size * 3 / 4);
		y = (int) (Math.random() * size / 4 + size * 3 / 4);
		unitsP2[x][y] = new PhysicalBuilder(2, size * x + y);
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
			unitsP3[x][y] = new PhysicalBuilder(3, size * x + y);
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
			unitsP4[x][y] = new PhysicalBuilder(4, size * x + y);
			unitsP4[x][y].setVisible(true);
			unitsP4[x][y].setActive(true);
			visualModelP4 = new int[size][size * 2];
			generateVisualModel(visualModelP4, unitsP4, terrainP4);
		}
	}

	/**
	 * Populates the visual model array using the unit and terrain arrays.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#loadInitialModelState(int, int) loadInitialModelState(int, int)}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link AbstractUnit#toInt() toInt()}
	 * </ul>
	 * </p>
	 * 
	 * @param visM - the target visual model array
	 * @param units - the source unit array
	 * @param terrain - the source terrain array
	 */
	private void generateVisualModel(int[][] visM, AbstractUnit[][] units, int[][] terrain) {
		for (int r = 0; r < units.length; r++) {
			for (int c = 0; c < units.length; c++) {
				visM[r][c] = units[r][c] != null ? units[r][c].toInt() : -1;
				visM[r][c + units.length] = terrain[r][c];
			}
		}
	}

	/**
	 * Copies one map over to another map.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#loadInitialModelState(int, int) loadInitialModelState(int, int)}
	 * </ul>
	 * </p>
	 * 
	 * @param origin - the map to be copied
	 * @param dest - the map to receive the copy
	 */
	private void copyMap(int[][] origin, int[][] dest) {
		for (int r = 0; r < origin.length; r++) {
			for (int c = 0; c < origin.length; c++) {
				dest[r][c] = origin[r][c];
			}
		}
	}

	/**
	 * Generates a randomized map.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#loadInitialModelState(int, int) loadInitialModelState(int, int)}
	 * </ul>
	 * </p>
	 * 
	 * @param dest - the terrain array that will receive the map
	 */
	private void generateMap(int[][] dest) {
		for (int r = 0; r < dest.length; r++) {
			for (int c = 0; c < dest.length; c++) {
				int tileID = (int) (45 * Math.random() + 45);
				dest[r][c] = tileID;
			}
		}
	}

	/**
	 * Rotates the turn to the next player in sequence.
	 * 
	 * <p>
	 * At the end of a round, processes the <code>ActivityQueue</code>
	 * and resolves all pending <code>Activities</code>.
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#turnTimer turnTimer}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link PrimaryController#generateNotification(String, int) generateNotification(String, int)}
	 * <li> {@link AbstractUnit#getActivityList() getActivityList()}
	 * <li> {@link ActivityQueue#process() process()}
	 * </ul>
	 * </p>
	 */
	protected void rotateTurn() {
		if (currentTurn < playerCount) {
			currentTurn += 1;
			PrimaryController.generateNotification("Switching to Player " + currentTurn, 0);
			turnTimer.setRepeats(true);
			turnTimer.start();
		}
		else {
			for (int r = 0; r < unitsP1.length; r++) {
				for (int c = 0; c < unitsP1.length; c++) {
					if (unitsP1[r][c].toInt() != 0) {
						activityQueue.add(unitsP1[r][c].getActivityList());
					}
					if (unitsP2[r][c].toInt() != 0) {
						activityQueue.add(unitsP2[r][c].getActivityList());
					}
					if (unitsP3[r][c].toInt() != 0) {
						activityQueue.add(unitsP3[r][c].getActivityList());
					}
					if (unitsP4[r][c].toInt() != 0) {
						activityQueue.add(unitsP4[r][c].getActivityList());
					}
				}
			}
			
			activityQueue.process();
			// Consider how to implement the chain of activities
			currentTurn = 1;
		}
	}

	/**
	 * Returns the visual model associated with the current turn.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#render(java.awt.Graphics) render(java.awt.Graphics)}
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the current visual model
	 */
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

	/**
	 * Returns the current clock face.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 * </p>
	 * 
	 * @return represents the current clock face
	 */
	public int getClockFace() {
		return percent;
	}

	/**
	 * Moves a unit from one square to another.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link AbstractUnit#toInt() toInt()}
	 * </ul>
	 * </p>
	 * 
	 * @param srcR - the unit's original row
	 * @param srcC - the unit's original column
	 * @param destR - the unit's destination row
	 * @param destC - the unit's destination column
	 */
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