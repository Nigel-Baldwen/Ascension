package ascension;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.sun.javafx.print.Units;

import ascension.AbstractUnit.Locomotion;
import ascension.AbstractUnit.UnitType;
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
	Timer turnTimer;
	ArrayList<ArrayList<ArrayList<ArrayList<AbstractUnit>>>> listOfActiveUnitArrays;
	ArrayList<VisibilityState[][]> listOfActiveVisibilityArrays;
	ArrayList<Terrain[][]> listOfActiveTerrainArrays;
	ArrayList<PathFinder> listOfActivePathFinders;
	ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitsP1, unitsP2, unitsP3, unitsP4;
	AbstractUnit focusTarget = null;
	Terrain[][] terrainP1, terrainP2, terrainP3, terrainP4;
	VisibilityState[][] visualModelP1, visualModelP2, visualModelP3, visualModelP4;
	int turnLength, percent, playerCount, waitingState, gridSize;
	PathFinder pathFinderP1, pathFinderP2, pathFinderP3, pathFinderP4;
	ArrayList<ActivityList> activityQueue;
	enum Player { PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4;
		public static final Player playerOrdinals[] = values(); };
	Player activePlayer;
	PrimaryController controller;

	public PrimaryModel(PrimaryController primaryController) {
		controller = primaryController;
	}

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
		turnLength = 10 * 1000 * 10; // Multiply to lengthen the turns; Millisecond units by default
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

		// This ought to help me reinvent some control structures to avoid clunky switch statements.
		listOfActiveUnitArrays = new ArrayList<ArrayList<ArrayList<ArrayList<AbstractUnit>>>>(4);
		listOfActiveVisibilityArrays = new ArrayList<VisibilityState[][]>();
		listOfActiveTerrainArrays = new ArrayList<Terrain[][]>();
		listOfActivePathFinders = new ArrayList<PathFinder>();

		// Creates 2d arrays for terrain, units, and model.
		// Initializes them.
		terrainP1 = new Terrain[size][size];
		listOfActiveTerrainArrays.add(terrainP1);
		generateMap(terrainP1);
		
		// TODO Remove this method
		// It is intended to enable some manual map instantiation
		// Eventually, I will probably replace this with something like
		// loadMapFromFile(String pathToFile);
		
		doManualMapInstantiation();
		
		// We are done initializing the map.
		// TODO End removal directive
		
		// Initialization is essential here since there is no default creation for ArrayList<T> like there is for T[]
		unitsP1  = new ArrayList<ArrayList<ArrayList<AbstractUnit>>>(size);
		listOfActiveUnitArrays.add(unitsP1);
		ArrayList<ArrayList<AbstractUnit>> listOfUnitLists;
		ArrayList<AbstractUnit> unitList;
		for (int i = 0; i < size; i++) {
			listOfUnitLists = new ArrayList<ArrayList<AbstractUnit>>();
			unitsP1.add(listOfUnitLists);
			for (int j = 0; j < size; j++) {
				unitList = new ArrayList<AbstractUnit>();
				listOfUnitLists.add(unitList);
			}
		}
		int row = 1; // (int) (Math.random() * size / 4);
		int column = 1; // (int) (Math.random() * size / 4);
		unitsP1.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_1, row, column));
		unitsP1.get(row).get(column).get(0).setVisible(true);
		unitsP1.get(row).get(column).get(0).setActive(true);
		unitsP1.get(row).get(column).get(0).setMovable(true);
		visualModelP1 = new VisibilityState[size][size];
		listOfActiveVisibilityArrays.add(visualModelP1);
		generateVisualModel(visualModelP1, unitsP1, terrainP1);
		pathFinderP1 = new PathFinder(visualModelP1); // This is a fairly rudimentary solution TODO
		listOfActivePathFinders.add(pathFinderP1);
		/*
		 * Each player uses their own path finder because each player
		 * has a particular view of terrain, enemy units, and other information on any
		 * given turn. It remains to be seen what ought to be done from here.
		 * The problem seems rather larger than I confidently grasp at the moment.
		 */

		terrainP2 = new Terrain[size][size];
		listOfActiveTerrainArrays.add(terrainP2);
		copyMap(terrainP1, terrainP2);
		unitsP2  = new ArrayList<ArrayList<ArrayList<AbstractUnit>>>(size);
		listOfActiveUnitArrays.add(unitsP2);
		for (int i = 0; i < size; i++) {
			listOfUnitLists = new ArrayList<ArrayList<AbstractUnit>>();
			unitsP2.add(listOfUnitLists);
			for (int j = 0; j < size; j++) {
				unitList = new ArrayList<AbstractUnit>();
				listOfUnitLists.add(unitList);
			}
		}
		row = 1; // (int) (Math.random() * size / 4 + size * 3 / 4);
		column = 3; // (int) (Math.random() * size / 4 + size * 3 / 4);
		unitsP2.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_2, row, column));
		unitsP2.get(row).get(column).get(0).setVisible(true);
		unitsP2.get(row).get(column).get(0).setActive(true);
		unitsP2.get(row).get(column).get(0).setMovable(true);
		visualModelP2 = new VisibilityState[size][size];
		listOfActiveVisibilityArrays.add(visualModelP2);
		generateVisualModel(visualModelP2, unitsP2, terrainP2);
		pathFinderP2 = new PathFinder(visualModelP2);
		listOfActivePathFinders.add(pathFinderP2);

		if (playerCount > 2) {
			terrainP3 = new Terrain[size][size];
			listOfActiveTerrainArrays.add(terrainP3);
			copyMap(terrainP1, terrainP3);
			unitsP3  = new ArrayList<ArrayList<ArrayList<AbstractUnit>>>(size);
			listOfActiveUnitArrays.add(unitsP3);
			for (int i = 0; i < size; i++) {
				listOfUnitLists = new ArrayList<ArrayList<AbstractUnit>>();
				unitsP3.add(listOfUnitLists);
				for (int j = 0; j < size; j++) {
					unitList = new ArrayList<AbstractUnit>();
					listOfUnitLists.add(unitList);
				}
			}
			row = 7; // (int) (Math.random() * size / 4);
			column = 7; // (int) (Math.random() * size / 4 + size * 3 / 4);
			unitsP3.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_3, row, column));
			unitsP3.get(row).get(column).get(0).setVisible(true);
			unitsP3.get(row).get(column).get(0).setActive(true);
			unitsP3.get(row).get(column).get(0).setMovable(true);
			visualModelP3 = new VisibilityState[size][size];
			listOfActiveVisibilityArrays.add(visualModelP3);
			generateVisualModel(visualModelP3, unitsP3, terrainP3);
			pathFinderP3 = new PathFinder(visualModelP3);
			listOfActivePathFinders.add(pathFinderP3);
		}

		if (playerCount > 3) {
			terrainP4 = new Terrain[size][size];
			listOfActiveTerrainArrays.add(terrainP4);
			copyMap(terrainP1, terrainP4);
			unitsP4  = new ArrayList<ArrayList<ArrayList<AbstractUnit>>>(size);
			listOfActiveUnitArrays.add(unitsP4);
			for (int i = 0; i < size; i++) {
				listOfUnitLists = new ArrayList<ArrayList<AbstractUnit>>();
				unitsP4.add(listOfUnitLists);
				for (int j = 0; j < size; j++) {
					unitList = new ArrayList<AbstractUnit>();
					listOfUnitLists.add(unitList);
				}
			}
			row = 7; // (int) (Math.random() * size / 4 + size * 3 / 4);
			column = 5; // (int) (Math.random() * size / 4);
			unitsP4.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_4, row, column));
			unitsP4.get(row).get(column).get(0).setVisible(true);
			unitsP4.get(row).get(column).get(0).setActive(true);
			unitsP4.get(row).get(column).get(0).setMovable(true);
			visualModelP4 = new VisibilityState[size][size];
			listOfActiveVisibilityArrays.add(visualModelP4);
			generateVisualModel(visualModelP4, unitsP4, terrainP4);
			pathFinderP4 = new PathFinder(visualModelP4);
			listOfActivePathFinders.add(pathFinderP4);
		}

		activityQueue = new ArrayList<ActivityList>();
		updateVision();
		// TODO Think about moving the start of the turn to somewhere more practical.
		// Maybe a "Start Game" screen or something. Probably something similar to the turn rotations.
		turnTimer.start();
	}

	private void updateVision() {
		// First we flush the vision for each player to offer a clean update slate.
		for (int r = 0; r < gridSize; r++) {
			for (int c = 0; c < gridSize; c++) {
				clearVisionInSquare(r, c); // This also adds back in any units known to the unit arrays
			}
		}

		// Next, we iterate over the available units and structures and provide vision of squares in their range.
		for (int r = 0; r < gridSize; r++) {
			for (int c = 0; c < gridSize; c++) {
				grantVisionInRangeOfSquare(r, c);
			}
		}
		
		// TODO Probably could stand to just iterate over the grid once, but maybe not. Bears thinking though.
		for (int r = 0; r < gridSize; r++) {
			for (int c = 0; c < gridSize; c++) {
				visualizeAllUnitPaths(r, c);
			}
		}
	}

	private void visualizeAllUnitPaths(int r, int c) {
		// Herein, we update the visibility state arrays with intended movements and destinations
		for (int k = 0; k < listOfActiveUnitArrays.size(); k++) { // For each Unit Array...
			if (!listOfActiveUnitArrays.get(k).get(r).get(c).isEmpty()) { // If there is at least one unit in this square...
				for (AbstractUnit unitOfInterest : listOfActiveUnitArrays.get(k).get(r).get(c)) { // Cycle through the units in the square...
					// And update the corresponding visibility state array with their intended paths.
					visualizeIndividualUnitPath(unitOfInterest, k);
				}
			}
		}
	}

	private void visualizeIndividualUnitPath(AbstractUnit unitOfInterest, int playerOrdinal) {
		// We're going to look through this unit's activity list and figure out
		// with what exactly the visibility state array needs to be updated.
		ActivityList activitiesOfInterest = unitOfInterest.getActivityList();
		
		// No need to process empty lists
		if (activitiesOfInterest.size() <= 0) {
			return;
		}
		
		// TODO This method considers only non-conflicted movement as an option
		// Eventually, I will need to add in relevant code for ability and attack
		// displays. (Most likely. Maybe a different method will end up better for it.)
		
		// In motion indicators with 50% transparency
		for (int i = 0; i < activitiesOfInterest.size() - 1; i ++) {
			Activity activityOfInterest = activitiesOfInterest.get(i);
			for (Point pointOfInterest : activityOfInterest.getTarget()) {
				listOfActiveVisibilityArrays.get(playerOrdinal)[pointOfInterest.x][pointOfInterest.y].addInMotionUnit(unitOfInterest.unitType);
			}
		}
		
		// Destination indicator with 75% transparency
		Activity activityOfInterest = activitiesOfInterest.get(activitiesOfInterest.size() - 1);
		for (Point pointOfInterest : activityOfInterest.getTarget()) {
			listOfActiveVisibilityArrays.get(playerOrdinal)[pointOfInterest.x][pointOfInterest.y].setDestinationUnit(unitOfInterest.unitType);
		}
	}

	private void grantVisionInRangeOfSquare(int r, int c) {
		int sightRadius = 0, trueSightRadius = 0; // TODO Eventually, units will sometimes possess true sight
		for (int k = 0; k < listOfActiveUnitArrays.size(); k++) { // For each active square of each Unit Array...
			if (!listOfActiveUnitArrays.get(k).get(r).get(c).isEmpty()) { 
				for (AbstractUnit unitOfInterest : listOfActiveUnitArrays.get(k).get(r).get(c)) { // Find the greatest sight radius originating from that square
					if (unitOfInterest.getSihtRd() > sightRadius) {
						sightRadius = unitOfInterest.getSihtRd();
						// TODO if (unitOfInterest.getTrueSightRadius() > trueSightRadius...
					}
				}

				for (int i = Math.max(0, r - sightRadius); i < Math.min(r + sightRadius + 1, gridSize); i ++) {
					for (int j = Math.max(0, c - sightRadius); j < Math.min(c + sightRadius + 1, gridSize); j++) {
						if (!listOfActiveVisibilityArrays.get(k)[i][j].isInVisionRange) { // TODO need to add an additional check for true sight
							listOfActiveVisibilityArrays.get(k)[i][j].isInVisionRange = true; // Let the square know it is in vision range

							for (ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitArray : listOfActiveUnitArrays) { // Detect enemy presence in the square
								if (!unitArray.equals(listOfActiveUnitArrays.get(k))) {
									if (!unitArray.get(i).get(j).isEmpty()) { // Enemy detected
										// TODO We use only the 0th unit type, but this may need to change (leaning towards probably not).
										listOfActiveVisibilityArrays.get(k)[i][j].setOccupyingUnit(unitArray.get(i).get(j).get(0).unitType); // Now the enemy is noted.
										listOfActiveVisibilityArrays.get(k)[i][j].setControllingPlayer(unitArray.get(i).get(j).get(0).player); // And his controller is noted.
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void clearVisionInSquare(int row, int column) {
		for (int i = 0; i < listOfActiveVisibilityArrays.size(); i++) {
			listOfActiveVisibilityArrays.get(i)[row][column].controllingPlayer = null;
			listOfActiveVisibilityArrays.get(i)[row][column].isInVisionRange = false;
			listOfActiveVisibilityArrays.get(i)[row][column].setOccupyingUnit(UnitType.EMPTY);
			listOfActiveVisibilityArrays.get(i)[row][column].halfTransparencyUnits.clear();
			listOfActiveVisibilityArrays.get(i)[row][column].destinationUnit = null;
			if (!listOfActiveUnitArrays.get(i).get(row).get(column).isEmpty()) { // Ensure that units owned by the player are accounted for in the respective visibility state
				listOfActiveVisibilityArrays.get(i)[row][column].setOccupyingUnit(listOfActiveUnitArrays.get(i).get(row).get(column).get(0).unitType);
				listOfActiveVisibilityArrays.get(i)[row][column].setControllingPlayer(Player.playerOrdinals[i]);
			}
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
	 * @param sourceUnits - the source unit array
	 * @param terrain - the source terrain array
	 */
	private void generateVisualModel(VisibilityState[][] visM, ArrayList<ArrayList<ArrayList<AbstractUnit>>> sourceUnits, Terrain[][] terrain) {
		for (int r = 0; r < sourceUnits.size(); r++) {
			for (int c = 0; c < sourceUnits.size(); c++) {
				visM[r][c] = new VisibilityState();
				if (!sourceUnits.get(r).get(c).isEmpty()) {
					// Should only need to account for the 0th since the map was just initialized
					visM[r][c].setOccupyingUnit(sourceUnits.get(r).get(c).get(0).unitType);
					visM[r][c].setControllingPlayer(sourceUnits.get(r).get(c).get(0).getPlayer());
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
	 * <li> {@link PrimaryController#rotateTurn(String, int) generateNotification(String, int)}
	 * <li> {@link AbstractUnit#getActivityList() getActivityList()}
	 * <li> {@link ActivityQueue#process() process()}
	 * </ul>
	 * </p>
	 */
	void rotateTurn() {
		// First we will terminate all in progress player activities by
		// removing unit focus and interrupting any active mouse activities
		focusTarget = null;
		if (activePlayer.ordinal() < playerCount - 1) {
			activePlayer = Player.values()[activePlayer.ordinal() + 1]; // Select the next player in sequence.
		}
		else {
			/* Now, we'll ensure that all pending activity lists get popped into the processing queue.
			 * The activityQueue is effectively an irregular 2d array where the left-most
			 * activity in each list is prior to all right-hand activities. Thus, all activityList[n]
			 * are prior to any activityList[n+1].
			 */ 
			for (int r = 0; r < unitsP1.size(); r++) {
				for (int c = 0; c < unitsP1.size(); c++) {
					for (ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitArray : listOfActiveUnitArrays) {
						addAllActivityListsAt(r, c, unitArray);
					}	
				}
			}

			/*
			 * The magic starts here. We need a while loop to ensure that we can grind out all activities in
			 * the processing queue. Basically, only once the queue is empty do we stop. Each activity list
			 * loses at least one activity per turn (or so I believe before actually coding it all out) and
			 * so it should reach the empty state reliably. We'll see. We'll see.
			 */

			while (!activityQueue.isEmpty()) {
				/* 
				 * Next, we do a pass over the queue to determine if
				 * there will be any conflicts to resolve.
				 * Notable conflicts include:
				 * Two or more units attempting to occupy the same square as a destination.
				 * Enemy units encounter one another.
				 * The target of an ability is leaving the square where the ability is aimed
				 * TODO Probably need some more notable cases. They will occur to me as time
				 * goes on and as I add more features.
				 */

				// TODO As a proof of concept, I'm going to use a multimap to look for move targets
				// The key is the target list of the 0th activity in the activity list
				// The value is the activity list itself. Since I'm searching for overlaps,
				// the activity lists are put into array lists. Any such list with more than
				// two elements indicates an overlap.
				Map<ArrayList<Point>, ArrayList<ActivityList>> movementDestinations = new HashMap<ArrayList<Point>, ArrayList<ActivityList>>();
				for (ActivityList activityList : activityQueue) {
					// First we handle the case of an already present key
					if (movementDestinations.containsKey(activityList.get(0).getTarget())) {
						movementDestinations.get(activityList.get(0).getTarget()).add(activityList);
					} // Next, we address the case of a newly found key.
					else {
						ArrayList<ActivityList> mapElement = new ArrayList<ActivityList>();
						mapElement.add(activityList);
						movementDestinations.put(activityList.get(0).getTarget(), mapElement);
					}
				}

				// At this point, movementDestinations should account for all activity lists.
				// For any key of movementDestinations with a corresponding array list,
				// if that array list has:
				// zero values - something probably went wrong. oops
				// one value - this activity target is held by exactly one unit
				// two or more values - this target is held by two or more units
				// For the case of two or more values, I must seek to resolve the conflict.
				for (Entry<ArrayList<Point>, ArrayList<ActivityList>> keyValuePair : movementDestinations.entrySet()) {
					// TODO The below if statements are expressive, but probably not optimal. Consider a switch.
					if (keyValuePair.getValue().size() <= 0) {
						System.out.println("THIS IS UNFORTUNATE!!! Why is it so tough to get things right the first time?");
					}
					if (keyValuePair.getValue().size() == 1) {
						processPassiveConflictFreeMovementRequest(keyValuePair.getValue().get(0));
					}
					if (keyValuePair.getValue().size() > 1) {
						System.out.println("It's a Conflict Rick!");
						resolvePassiveMovementConflict(keyValuePair.getValue());
					}
				}
			}
			activePlayer = Player.PLAYER_1;
		}
		controller.rotateTurn("Switching to Player: " + activePlayer.toString(), activePlayer);
		turnTimer.setRepeats(true);
		percent = 0;
		turnTimer.start();
		waitingState = 1;
		updateVision();
	}

	private void resolvePassiveMovementConflict(ArrayList<ActivityList> conflictedActivities) {
		// TODO for now, to get things moving (heh) I'm going to just let these be coin flips
		for (ActivityList activityList : conflictedActivities) {
			activityList.get(0).setCoinFlipValue(Math.random());
		}
		// Sort according to who has the best roll - low roll wins
		Collections.sort(conflictedActivities);
		
		// The winner gets to occupy the square according to the processPassiveConflictFreeMovementRequest rules
		processPassiveConflictFreeMovementRequest(conflictedActivities.remove(0));
		
		// The remaining units need to figure out whether or not to keep trying to move
		for (ActivityList activityList : conflictedActivities) {
			// We'll shove this off to a different method, but the gist of it is that the units should
			// now attempt to see if they can still do what they set out to do at the start of the phase,
			// and if so, to keep on trying. Since they are just trying to passively move, it is simply
			// a matter of seeing whether or not a clear path is available for them to continue towards
			// their destination. If so, the unit should set a new path to try to get there and get as close
			// as possible. If not, the unit should just stop for now, and possibly produce a command prompt
			// for the player. Something like, < retreat, fight, do nothing >.
			
			reattemptPassivePathFindingFor(activityList);			
		}
	}

	private void reattemptPassivePathFindingFor(ActivityList activityList) {
		// TODO This method is designed to re-structure a given passive movement path to account for
		// passive movement conflicts. I'm really not sure where to begin here, but it will minimally
		// call for invoking the pathfinding code.
		
	}

	private void processPassiveConflictFreeMovementRequest(ActivityList activityList) {
		// Exactly one activity list for one target indicates a lack of conflict at this resolution stage.
		// The 0th activity can be removed and executed so long as the intended space is open.
		// In other words, it is possible that although no other activity is currently in conflict
		// with this one, it might be the case that an activity executed in the prior resolution
		// stage left something in the square preventing this unit from moving in.
		Activity toExecute = activityList.remove(0); // Pulling the first activity out
		if (activityList.isEmpty()) {
			//			System.out.println("Removing Activity List");
			activityQueue.remove(activityList);
		}
		//		System.out.println(toExecute);
		ArrayList<Point> executionOrigin = toExecute.getOrigin(),
				executionSquaresOccupied = toExecute.getSquaresOccupied(),
				executionTarget = toExecute.getTarget(); // Getting the location information needed.
		AbstractUnit activityRequestor = toExecute.getActivityRequestor();
		Player controllingPlayer = toExecute.getPlayer();
		int playerOrdinal = controllingPlayer.ordinal();
		if (executionTarget.size() == 1) { // Small unit size, easy case.
			int targetRow = executionTarget.get(0).x, targetCol = executionTarget.get(0).y,
					originRow = executionOrigin.get(0).x, originCol = executionOrigin.get(0).y;
			// Make sure the enemy has nothing in the square
			boolean noEnemyUnitPresent = true;
			for (int i = 0; i < listOfActiveUnitArrays.size(); i++) {
				if (i != playerOrdinal) {
					noEnemyUnitPresent = listOfActiveUnitArrays.get(i).get(targetRow).get(targetCol).isEmpty();
				}
			}

			if (noEnemyUnitPresent) {
				// Make sure that the square is occupied by at most, in-motion, friendly units.
				// TODO on the other hand, I'm really not sure what to make of two friendly units occupying the same space when they happen
				// to encounter an enemy unexpectedly. I suppose I could just auto-link them, but it is problematic considering that the
				// units in question could already be linked.
				if (listOfActiveUnitArrays.get(playerOrdinal).get(targetRow).get(targetCol).isEmpty()) {
					int requestorIndex = listOfActiveUnitArrays.get(playerOrdinal).get(originRow).get(originCol).indexOf(activityRequestor);
					listOfActiveUnitArrays.get(playerOrdinal).get(targetRow).get(targetCol).
					add(listOfActiveUnitArrays.get(playerOrdinal).get(originRow).get(originCol).get(requestorIndex));
					listOfActiveUnitArrays.get(playerOrdinal).get(originRow).get(originCol).remove(requestorIndex);
					listOfActiveVisibilityArrays.get(playerOrdinal)[targetRow][targetCol].setOccupyingUnit(activityRequestor.unitType);
					activityRequestor.curLoc = executionTarget.get(0);
					do {
						Point temp = executionSquaresOccupied.remove(0);
						listOfActiveVisibilityArrays.get(playerOrdinal)[temp.x][temp.y].halfTransparencyUnits.remove(activityRequestor.unitType);
					} while (executionSquaresOccupied.size() > 0);
					if (activityList.isEmpty()) { // We've reached our destination
						listOfActiveVisibilityArrays.get(playerOrdinal)[targetRow][targetCol].destinationUnit = null;
					}
				}
			}
		}
	}

	private void addAllActivityListsAt(int row, int column, ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitArray) {
		if (!unitArray.get(row).get(column).isEmpty()) {
			for (AbstractUnit unitOfInterest : unitArray.get(row).get(column)) {
				if (unitOfInterest.activityList.size() > 0) {
					unitOfInterest.activityList.organize();
					activityQueue.add(unitOfInterest.activityList);
				}
			}
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
	VisibilityState[][] getVisualModel() {
		return listOfActiveVisibilityArrays.get(activePlayer.ordinal());
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
		for (int i = 0; i < playerCount; i++) {
			if (!listOfActiveUnitArrays.get(i).get(r).get(c).isEmpty())
				return listOfActiveUnitArrays.get(i).get(r).get(c).get(0).getDescriptor();
		}
		return listOfActiveTerrainArrays.get(activePlayer.ordinal())[r][c].getDescriptor();
	}

	boolean setUnitFocusTarget(int r, int c) {
		if (!listOfActiveUnitArrays.get(activePlayer.ordinal()).get(r).get(c).isEmpty()) {
			focusTarget = listOfActiveUnitArrays.get(activePlayer.ordinal()).get(r).get(c).get(0);
			return true;
		}
		for (int i = 0; i < playerCount; i++) { // TODO may not actually need to set a focus target at all if I won't be issuing commands to it.
			if (i != activePlayer.ordinal()) {
				if (!listOfActiveUnitArrays.get(i).get(r).get(c).isEmpty()) {
					focusTarget = listOfActiveUnitArrays.get(i).get(r).get(c).get(0);
				}
			}
		}
		return false;
	}

	void requestMoveTo(int row, int column) {
		ArrayList<Point> path;
		if ( listOfActiveTerrainArrays.get(activePlayer.ordinal())[row][column].terrainSubType == TerrainSubType.EIGHT ||
				(listOfActiveTerrainArrays.get(activePlayer.ordinal())[row][column].terrainSubType == TerrainSubType.SEVEN && focusTarget.locomotion != Locomotion.AIR)) {
			System.out.println("This is an invalid move target. Try again.");
			return;
		}
		path = listOfActivePathFinders.get(activePlayer.ordinal()).getPassivePath(focusTarget.curLoc.x, focusTarget.curLoc.y,
				row, column, focusTarget.getMovSpd(), focusTarget.getLocomotion());
		
		if (path == null) {
			System.out.println("I can't reach that spot! Try again.");
			return;
		}

		for (int i = 0; i < path.size() - 1; i++) { // Adding Half Transparency steps to the visual model
			Point step = path.get(i);
			listOfActiveVisibilityArrays.get(activePlayer.ordinal())[step.x][step.y].addInMotionUnit(focusTarget.unitType);
		}

		listOfActiveVisibilityArrays.get(activePlayer.ordinal())[path.get(path.size() -1).x][path.get(path.size() -1).y].setDestinationUnit(focusTarget.unitType);;

		focusTarget.generateMoveActivityWithPath(path);
		focusTarget.setMovable(false);
	}

	public boolean focusTargetCanMove() {
		return focusTarget.canMove();
	}
	
	// TODO Remove or change this method closer to release
	void doManualMapInstantiation() {
		/*
		// Unknown Purpose
		terrainP1[0][1].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[1][1].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[1][0].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[0][0].terrainSubType = TerrainSubType.ZERO;
		*/
		
		// 5 x 3 impassable terrain box for movement conflict testing
		terrainP1[0][0].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[0][1].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[0][2].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[0][3].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[0][4].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[1][0].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[1][1].terrainSubType = TerrainSubType.ZERO;
		terrainP1[1][2].terrainSubType = TerrainSubType.ZERO;
		terrainP1[1][3].terrainSubType = TerrainSubType.ZERO;
		terrainP1[1][4].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[2][0].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[2][1].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[2][2].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[2][3].terrainSubType = TerrainSubType.EIGHT;
		terrainP1[2][4].terrainSubType = TerrainSubType.EIGHT;
	}
}