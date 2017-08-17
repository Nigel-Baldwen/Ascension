package ascension;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import ascension.AbstractUnit.Locomotion;
import ascension.Terrain.TerrainSubType;
import ascension.Terrain.TerrainType;

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
 * *A separate triple of arrays is necessary for each player
 * in order to reflect the various vision and control rules.
 * This requirement applies to the off-line/single-machine variant.
 * </p>
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

class PrimaryModel {

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
	private AbstractUnit focusTarget = null;
	private Terrain[][] terrainP1, terrainP2, terrainP3, terrainP4;
	private VisibilityState[][] visualModelP1, visualModelP2, visualModelP3, visualModelP4;
	private int turnLength, percent, playerCount, waitingState, gridSize;
	private PathFinder pathFinderP1, pathFinderP2, pathFinderP3, pathFinderP4;
	private ActivityQueue activityQueue;
	enum Player { PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4 };
	private Player activePlayer;

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
	void loadInitialModelState(int size, int playerCount) {
		gridSize = size;
		waitingState = 0;
		activePlayer = Player.PLAYER_1;
		this.playerCount = playerCount;

		// Clock instantiation
		percent = -1;
		turnLength = 10 * 1000;
		turnTimer = new Timer(0, new ActionListener() {

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
		turnTimer.setDelay(turnLength / 10);
		turnTimer.setRepeats(true);

		// Creates 2d arrays for terrain, units, and model.
		// Initializes them.
		terrainP1 = new Terrain[size][size];
		generateMap(terrainP1);
		unitsP1  = new AbstractUnit[size][size];
		int c = 10; // (int) (Math.random() * size / 4);
		int r = 10; // (int) (Math.random() * size / 4);
		unitsP1[c][r] = new PhysicalBuilder(Player.PLAYER_1, r, c);
		unitsP1[c][r].setVisible(true);
		unitsP1[c][r].setActive(true);
		visualModelP1 = new VisibilityState[size][size];
		generateVisualModel(visualModelP1, unitsP1, terrainP1);
		pathFinderP1 = new PathFinder(visualModelP1); // This is a fairly rudimentary solution TODO
		/*
		 * Each player uses their own path finder because each player
		 * has a particular view of terrain, enemy, and other information on any
		 * given turn. It remains to be seen what ought to be done from here.
		 * The problem seems rather larger than I confidently grasp at the moment.
		 */

		terrainP2 = new Terrain[size][size];
		copyMap(terrainP1, terrainP2);
		unitsP2  = new AbstractUnit[size][size];
		c = (int) (Math.random() * size / 4 + size * 3 / 4);
		r = (int) (Math.random() * size / 4 + size * 3 / 4);
		unitsP2[c][r] = new PhysicalBuilder(Player.PLAYER_2, r, c);
		unitsP2[c][r].setVisible(true);
		unitsP2[c][r].setActive(true);
		visualModelP2 = new VisibilityState[size][size];
		generateVisualModel(visualModelP2, unitsP2, terrainP2);
		pathFinderP2 = new PathFinder(visualModelP2);

		if (playerCount > 2) {
			terrainP3 = new Terrain[size][size];
			copyMap(terrainP1, terrainP3);
			unitsP3  = new AbstractUnit[size][size];
			c = (int) (Math.random() * size / 4);
			r = (int) (Math.random() * size / 4 + size * 3 / 4);
			unitsP3[c][r] = new PhysicalBuilder(Player.PLAYER_3, r, c);
			unitsP3[c][r].setVisible(true);
			unitsP3[c][r].setActive(true);
			visualModelP3 = new VisibilityState[size][size];
			generateVisualModel(visualModelP3, unitsP3, terrainP3);
			pathFinderP3 = new PathFinder(visualModelP3);
		}

		if (playerCount > 3) {
			terrainP4 = new Terrain[size][size];
			copyMap(terrainP1, terrainP4);
			unitsP4  = new AbstractUnit[size][size];
			c = (int) (Math.random() * size / 4 + size * 3 / 4);
			r = (int) (Math.random() * size / 4);
			unitsP4[c][r] = new PhysicalBuilder(Player.PLAYER_4, r, c);
			unitsP4[c][r].setVisible(true);
			unitsP4[c][r].setActive(true);
			visualModelP4 = new VisibilityState[size][size];
			generateVisualModel(visualModelP4, unitsP4, terrainP4);
			pathFinderP4 = new PathFinder(visualModelP4);
		}

		// TODO Think about moving the start of the turn to somewhere more practical.
		// Maybe a "Start Game" screen or something. Probably something similar to the turn rotations.
		turnTimer.start();
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
	private void generateVisualModel(VisibilityState[][] visM, AbstractUnit[][] units, Terrain[][] terrain) {
		for (int r = 0; r < units.length; r++) {
			for (int c = 0; c < units.length; c++) {
				visM[r][c] = new VisibilityState();
				if (units[r][c] != null) {
					visM[r][c].setOccupyingUnit(units[r][c].unitType);
				}
				visM[r][c].setTerrainType(terrain[r][c].terrainType, terrain[r][c].terrainSubType);
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
	private void copyMap(Terrain[][] origin, Terrain[][] dest) {
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
	private void generateMap(Terrain[][] dest) {
		// Divide the map into 10-20% width and length seed zones.
		int zonePercent = ((int) (11 * Math.random())) + 10, zoneLength = gridSize / zonePercent;
		TerrainType seedType;
		TerrainSubType seedSubType;

		for (int x = 0; x < zonePercent; x++) {
			for (int y = 0; y < zonePercent; y++) {
				int seedC = zoneLength * x + zoneLength / 2;
				int seedR = zoneLength * y + zoneLength / 2;
				seedType = TerrainType.terrainTypeOrdinals[(int) (5 * Math.random())];
				dest[seedR][seedC] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);

				for (int i = 1; i <= zoneLength / 2; i++) {
					double chanceTarget = (double) i / (zoneLength / 2) - (double) 1 / (zoneLength / 2);

					for (int c = seedC - i; c <= seedC + i; c++) {
						if (Math.random() >= chanceTarget && seedR - i > -1 && c > -1 && c < gridSize)
							dest[seedR - i][c] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);
						if (Math.random() >= chanceTarget && seedR + i < gridSize && c > -1 && c < gridSize)
							dest[seedR + i][c] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);
					}
					for (int r = seedR - i + 1; r <= seedR + i - 1; r++) {
						if (Math.random() >= chanceTarget && seedC - i > -1 && r > -1 && r < gridSize)
							dest[r][seedC - i] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);
						if (Math.random() >= chanceTarget && seedC + i < gridSize && r > -1 && r < gridSize)
							dest[r][seedC + i] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);
					}
				}
			}
		}

		for (int r = 0; r < gridSize; r++) {
			for (int c = 0; c < gridSize; c++) {
				if (dest[r][c] == null) {
					seedType = TerrainType.terrainTypeOrdinals[(int) (5 * Math.random())];
					dest[r][c] = new Terrain(seedType, TerrainSubType.terrainSubTypeOrdinals[(int) (9 * Math.random())]);
				}
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
		if (activePlayer.ordinal() < playerCount - 1) {
			activePlayer = Player.values()[activePlayer.ordinal() + 1]; // Select the next player in sequence.
			PrimaryController.generateNotification("Switching to Player: " + activePlayer.toString(), 0);
			turnTimer.setRepeats(true);
			percent = 0;
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
			activePlayer = Player.PLAYER_1;
		}
		waitingState = 1;
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
	VisibilityState[][] getVisualModel() {
		switch (activePlayer) {
		case PLAYER_1:
			return visualModelP1;

		case PLAYER_2:
			return visualModelP2;

		case PLAYER_3:
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
	int getClockFace() {
		return percent;
	}

	String getDescriptor(int r, int c) {
		switch (activePlayer) {
		case PLAYER_1:
			if (c < unitsP1.length )
				return unitsP1[r][c].getDescriptor();
			else
				return terrainP1[r][c - unitsP1.length].getDescriptor();
		case PLAYER_2:
			if (c < unitsP2.length )
				return unitsP2[r][c].getDescriptor();
			else
				return terrainP2[r][c - unitsP2.length].getDescriptor();
		case PLAYER_3:
			if (c < unitsP3.length )
				return unitsP3[r][c].getDescriptor();
			else
				return terrainP3[r][c - unitsP3.length].getDescriptor();
		default:
			if (c < unitsP4.length )
				return unitsP4[r][c].getDescriptor();
			else
				return terrainP4[r][c - unitsP4.length].getDescriptor();
		}
	}

	void setUnitFocusTarget(int r, int c) {
		switch (activePlayer) {
		case PLAYER_1:
			focusTarget = unitsP1[r][c];
			break;
		case PLAYER_2:
			focusTarget = unitsP2[r][c];
			break;
		case PLAYER_3:
			focusTarget = unitsP3[r][c];
			break;
		default:
			focusTarget = unitsP4[r][c];
			break;
		}
	}

	void requestMoveTo(int row, int column) {
		ArrayList<Point> path;
		switch (activePlayer) {
		case PLAYER_1:
			if ( terrainP1[row][column].terrainSubType == TerrainSubType.EIGHT ||
			(terrainP1[row][column].terrainSubType == TerrainSubType.SEVEN && focusTarget.locomotion != Locomotion.AIR)) {
				System.out.println("This is an invalid move target. Try again.");
				break;
			}
			path = pathFinderP1.getPassivePath(focusTarget.getCurLocR(), focusTarget.getCurLocC(),
					row, column, focusTarget.getMovSpd(), focusTarget.getLocomotion());

			for (int i = 0; i < path.size() - 1; i++) { // Adding Half Transparency steps to the visual model
				Point step = path.get(i);
				visualModelP1[step.x][step.y].addInMotionUnit(focusTarget.unitType);
			}

			visualModelP1[path.get(path.size() -1).x][path.get(path.size() -1).y].setDestinationUnit(focusTarget.unitType);;

			focusTarget.generateMoveActivityWithPath(path);
			break;
		case PLAYER_2:
			if ( terrainP2[row][column].terrainSubType == TerrainSubType.EIGHT ||
			(terrainP2[row][column].terrainSubType == TerrainSubType.SEVEN && focusTarget.locomotion != Locomotion.AIR)) {
				System.out.println("This is an invalid move target. Try again.");
				break;
			}
			path = pathFinderP2.getPassivePath(focusTarget.getCurLocR(), focusTarget.getCurLocC(),
					row, column, focusTarget.getMovSpd(), focusTarget.getLocomotion());

			for (int i = 0; i < path.size() - 1; i++) { // Adding Half Transparency steps to the visual model
				Point step = path.get(i);
				visualModelP2[step.x][step.y].addInMotionUnit(focusTarget.unitType);
			}

			visualModelP2[path.get(path.size() -1).x][path.get(path.size() -1).y].setDestinationUnit(focusTarget.unitType);;

			focusTarget.generateMoveActivityWithPath(path);
			break;
		case PLAYER_3:
			if ( terrainP3[row][column].terrainSubType == TerrainSubType.EIGHT ||
			(terrainP3[row][column].terrainSubType == TerrainSubType.SEVEN && focusTarget.locomotion != Locomotion.AIR)) {
				System.out.println("This is an invalid move target. Try again.");
				break;
			}
			path = pathFinderP3.getPassivePath(focusTarget.getCurLocR(), focusTarget.getCurLocC(),
					row, column, focusTarget.getMovSpd(), focusTarget.getLocomotion());

			for (int i = 0; i < path.size() - 1; i++) { // Adding Half Transparency steps to the visual model
				Point step = path.get(i);
				visualModelP3[step.x][step.y].addInMotionUnit(focusTarget.unitType);
			}

			visualModelP3[path.get(path.size() -1).x][path.get(path.size() -1).y].setDestinationUnit(focusTarget.unitType);;

			focusTarget.generateMoveActivityWithPath(path);
			break;
		default:
			if ( terrainP4[row][column].terrainSubType == TerrainSubType.EIGHT ||
			(terrainP4[row][column].terrainSubType == TerrainSubType.SEVEN && focusTarget.locomotion != Locomotion.AIR)) {
				System.out.println("This is an invalid move target. Try again.");
				break;
			}
			path = pathFinderP4.getPassivePath(focusTarget.getCurLocR(), focusTarget.getCurLocC(),
					row, column, focusTarget.getMovSpd(), focusTarget.getLocomotion());

			for (int i = 0; i < path.size() - 1; i++) { // Adding Half Transparency steps to the visual model
				Point step = path.get(i);
				visualModelP4[step.x][step.y].addInMotionUnit(focusTarget.unitType);
			}

			visualModelP4[path.get(path.size() -1).x][path.get(path.size() -1).y].setDestinationUnit(focusTarget.unitType);;

			focusTarget.generateMoveActivityWithPath(path);
			break;
		}

	}
}