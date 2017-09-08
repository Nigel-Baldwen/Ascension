package ascension;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.Timer;

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
	private Timer turnTimer;
	private ArrayList<ArrayList<ArrayList<ArrayList<AbstractUnit>>>> listOfActiveUnitArrays;
	private ArrayList<VisibilityState[][]> listOfActiveVisibilityArrays;
	private ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitsP1, unitsP2, unitsP3, unitsP4;
	private AbstractUnit focusTarget = null;
	private Terrain[][] terrainP1, terrainP2, terrainP3, terrainP4;
	private VisibilityState[][] visualModelP1, visualModelP2, visualModelP3, visualModelP4;
	private int turnLength, percent, playerCount, waitingState, gridSize;
	private PathFinder pathFinderP1, pathFinderP2, pathFinderP3, pathFinderP4;
	private ArrayList<ActivityList> activityQueue;
	enum Player { PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4 };
	private Player activePlayer;
	private PrimaryController controller;

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
		turnLength = 10 * 1000 * 10; // Multiply to lengthen the turns
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

		// This ought to help me reinvent some control structures to avoid switch statements.
		listOfActiveUnitArrays = new ArrayList<ArrayList<ArrayList<ArrayList<AbstractUnit>>>>(4);
		listOfActiveVisibilityArrays = new ArrayList<VisibilityState[][]>();
		
		// Creates 2d arrays for terrain, units, and model.
		// Initializes them.
		terrainP1 = new Terrain[size][size];
		generateMap(terrainP1);
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
		int row = 0; // (int) (Math.random() * size / 4);
		int column = 0; // (int) (Math.random() * size / 4);
		unitsP1.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_1, row, column));
		unitsP1.get(row).get(column).get(0).setVisible(true);
		unitsP1.get(row).get(column).get(0).setActive(true);
		visualModelP1 = new VisibilityState[size][size];
		listOfActiveVisibilityArrays.add(visualModelP1);
		generateVisualModel(visualModelP1, unitsP1, terrainP1);
		pathFinderP1 = new PathFinder(visualModelP1); // This is a fairly rudimentary solution TODO
		/*
		 * Each player uses their own path finder because each player
		 * has a particular view of terrain, enemy units, and other information on any
		 * given turn. It remains to be seen what ought to be done from here.
		 * The problem seems rather larger than I confidently grasp at the moment.
		 */

		terrainP2 = new Terrain[size][size];
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
		row = 10; // (int) (Math.random() * size / 4 + size * 3 / 4);
		column = 10; // (int) (Math.random() * size / 4 + size * 3 / 4);
		unitsP2.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_2, row, column));
		unitsP2.get(row).get(column).get(0).setVisible(true);
		unitsP2.get(row).get(column).get(0).setActive(true);
		visualModelP2 = new VisibilityState[size][size];
		listOfActiveVisibilityArrays.add(visualModelP2);
		generateVisualModel(visualModelP2, unitsP2, terrainP2);
		pathFinderP2 = new PathFinder(visualModelP2);

		if (playerCount > 2) {
			terrainP3 = new Terrain[size][size];
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
			row = 5; // (int) (Math.random() * size / 4);
			column = 10; // (int) (Math.random() * size / 4 + size * 3 / 4);
			unitsP3.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_3, row, column));
			unitsP3.get(row).get(column).get(0).setVisible(true);
			unitsP3.get(row).get(column).get(0).setActive(true);
			visualModelP3 = new VisibilityState[size][size];
			listOfActiveVisibilityArrays.add(visualModelP3);
			generateVisualModel(visualModelP3, unitsP3, terrainP3);
			pathFinderP3 = new PathFinder(visualModelP3);
		}

		if (playerCount > 3) {
			terrainP4 = new Terrain[size][size];
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
			row = 10; // (int) (Math.random() * size / 4 + size * 3 / 4);
			column = 5; // (int) (Math.random() * size / 4);
			unitsP4.get(row).get(column).add(new PhysicalBuilder(Player.PLAYER_4, row, column));
			unitsP4.get(row).get(column).get(0).setVisible(true);
			unitsP4.get(row).get(column).get(0).setActive(true);
			visualModelP4 = new VisibilityState[size][size];
			listOfActiveVisibilityArrays.add(visualModelP4);
			generateVisualModel(visualModelP4, unitsP4, terrainP4);
			pathFinderP4 = new PathFinder(visualModelP4);
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
				clearVisionInSquare(r, c);
			}
		}
		
		// Next, we iterate over the available units and structures and provide vision of squares in their range.
		for (int r = 0; r < gridSize; r++) {
			for (int c = 0; c < gridSize; c++) {
				grantVisionInRangeOfSquare(r, c);
			}
		}
	}

	private void grantVisionInRangeOfSquare(int r, int c) {
		int sightRadius = 0, trueSightRadius = 0; // TODO Eventually, units will sometimes possess true sight
		for (int k = 0; k < listOfActiveUnitArrays.size(); k++) {
			if (!listOfActiveUnitArrays.get(k).get(r).get(c).isEmpty()) {
				for (AbstractUnit unitOfInterest : listOfActiveUnitArrays.get(k).get(r).get(c)) {
					if (unitOfInterest.getSihtRd() > sightRadius) {
						sightRadius = unitOfInterest.getSihtRd();
						// TODO if (unitOfInterest.getTrueSightRadius() > trueSightRadius...
					}
				}
				
				for (int i = Math.max(0, r - sightRadius); i < Math.min(r + sightRadius + 1, gridSize); i ++) {
					for (int j = Math.max(0, c - sightRadius); j < Math.min(c + sightRadius + 1, gridSize); j++) {
						if (!listOfActiveVisibilityArrays.get(k)[i][j].isInVisionRange) { // TODO need to add an additional check for true sight
							listOfActiveVisibilityArrays.get(k)[i][j].isInVisionRange = true;
							
							for (ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitArray : listOfActiveUnitArrays) {
								if (!unitArray.equals(listOfActiveUnitArrays.get(k))) {
									if (!unitArray.get(i).get(j).isEmpty()) {
										// TODO We use only the 0th unit type, but this may need to change (leaning towards probably not.
										listOfActiveVisibilityArrays.get(k)[i][j].setDestinationUnit(unitArray.get(i).get(j).get(0).unitType);
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
			listOfActiveVisibilityArrays.get(i)[row][column].isInVisionRange = false;
			if (listOfActiveVisibilityArrays.get(i)[row][column].occupyingUnitType != UnitType.EMPTY && listOfActiveUnitArrays.get(i).get(row).get(column).isEmpty()) {
				listOfActiveVisibilityArrays.get(i)[row][column].setOccupyingUnit(UnitType.EMPTY);
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
	 * <li> {@link PrimaryController#generateNotification(String, int) generateNotification(String, int)}
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
			controller.generateNotification("Switching to Player: " + activePlayer.toString(), 0);
			turnTimer.setRepeats(true);
			percent = 0;
			turnTimer.start();
		}
		else {
			/* First, we'll ensure that all pending activity lists get popped into the processing queue.
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
			 * Next, we do a pass over the queue to determine if
			 * there will be any conflicts to resolve.
			 * Notable conflicts include:
			 * Two or more units attempting to occupy the same square as a destination.
			 * Enemy units encounter one another.
			 * The target of an ability is leaving the square where the ability is aimed
			 * TODO Probably need some more notable cases. They will occur to me as time
			 * goes on and as I add more features.
			 * 
			 * In order to do this, I'll need some sort of map between targets and
			 * the activities which spawn the targets. In other words, for all activities
			 * that target <r, c>, I need to know which activities those are such that I can
			 * determine whether or not they are in conflict. I think something like a map,
			 * dictionary, or hash table may be what I need. I'll have to think about it
			 * a while longer. Time to feed Sage lunch. 
			 */

			// TODO As a proof of concept, I'm going to use a multimap to look for move targets
			// The key is the target list of the 0th activity in the activity list
			// The value is the activity list itself. Since I'm searching for overlaps,
			// the activity lists are put into array lists. Any such list with more than
			// two elements indicates an overlap.
			Map<ArrayList<Point>, ArrayList<ActivityList>> movementDestinations = new HashMap<ArrayList<Point>, ArrayList<ActivityList>>();
			for (ActivityList activityList : activityQueue) {
				// First we handle the case of an already present key
				// TODO A possible pitfall is that the targets may be getting compared at the
				// object level rather than the value level. Thus, two target lists with all
				// the same values MAY be treated as non-equivalent since they are technically
				// distinct instances of the same list of points. May seek advisement on this
				// point if trouble becomes apparent. Probably could provide a wrapper for
				// Point that includes a comparator.
				// TODO Delete above todo since Lennon and I talked and it seems not to be an issue atm.
				if (movementDestinations.containsKey(activityList.get(0).getTarget())) {
					movementDestinations.get(activityList.get(0).getTarget()).add(activityList);
				} // Next, we address the case of a newly found key. Same potential issue as above.
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
				if (keyValuePair.getValue().size() <= 0) {
					System.out.println("THIS IS UNFORTUNATE!!! Why is it so tough to get things right the first time?");
				}
				if (keyValuePair.getValue().size() == 1) {
					// Exactly one activity list for one target indicates a lack of conflict at this resolution stage.
					// The 0th activity can be removed and executed so long as the intended space is open.
					// In other words, it is possible that although no other activity is currently in conflict
					// with this one, it might be the case that an activity executed in the prior resolution
					// stage left something in the square preventing this unit from moving in.
					Activity toExecute = keyValuePair.getValue().get(0).remove(0); // Pulling the first activity out
					System.out.println(toExecute);
					ArrayList<Point> executionOrigin = toExecute.getOrigin(),
							//executionSquaresOccupied = toExecute.getSquaresOccupied(),
							executionTarget = toExecute.getTarget(); // Getting the location information needed.
					AbstractUnit activityRequestor = toExecute.getActivityRequestor();
					Player controllingPlayer = toExecute.getPlayer();
					switch (controllingPlayer) {
					case PLAYER_1:
						System.out.println("Executing Action for Player 1:\n"
								+ "executionTarget.size(): " + executionTarget.size());
						if (executionTarget.size() == 1) { // Small unit size, easy case.
							System.out.println("Yup.");
							int targetRow = executionTarget.get(0).x, targetCol = executionTarget.get(0).y;
							// Make sure the enemy has nothing in the square
							if (unitsP2.get(targetRow).get(targetCol).isEmpty() && unitsP3.get(targetRow).get(targetCol).isEmpty() && unitsP4.get(targetRow).get(targetCol).isEmpty()) {
								// Make sure that the square is occupied by at most, in-motion, friendly units.
								// TODO on the other hand, I'm really not sure what to make of two friendly units occupying the same space when they happen
								// to encounter an enemy unexpectedly. I suppose I could just auto-link them, but it is problematic considering that the
								// units in question could already be linked.
								if (unitsP1.get(targetRow).get(targetCol).isEmpty()) {
									System.out.println("Row of Interest: " + executionOrigin.get(0).x + " Column of Interest: " + executionOrigin.get(0).y);
									int requestorIndex = unitsP1.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).indexOf(activityRequestor);
									unitsP1.get(targetRow).get(targetCol).add(unitsP1.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).get(requestorIndex));
									unitsP1.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).remove(requestorIndex);
									}
							}
						}
						break;
					case PLAYER_2:
						if (executionTarget.size() == 1) {
							int targetRow = executionTarget.get(0).x, targetCol = executionTarget.get(0).y;
							if (unitsP1.get(targetRow).get(targetCol).isEmpty() && unitsP3.get(targetRow).get(targetCol).isEmpty() && unitsP4.get(targetRow).get(targetCol).isEmpty()) {
								if (unitsP2.get(targetRow).get(targetCol).isEmpty()) {
									int requestorIndex = unitsP2.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).indexOf(activityRequestor);
									unitsP2.get(targetRow).get(targetCol).add(unitsP2.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).get(requestorIndex));
									unitsP2.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).remove(requestorIndex);
									}
							}
						}
						break;
					case PLAYER_3:
						if (executionTarget.size() == 1) {
							int targetRow = executionTarget.get(0).x, targetCol = executionTarget.get(0).y;
							if (unitsP1.get(targetRow).get(targetCol).isEmpty() && unitsP2.get(targetRow).get(targetCol).isEmpty() && unitsP4.get(targetRow).get(targetCol).isEmpty()) {
								if (unitsP3.get(targetRow).get(targetCol).isEmpty()) {
									int requestorIndex = unitsP3.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).indexOf(activityRequestor);
									unitsP3.get(targetRow).get(targetCol).add(unitsP3.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).get(requestorIndex));
									unitsP3.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).remove(requestorIndex);
									}
							}
						}
						break;
					case PLAYER_4:
						if (executionTarget.size() == 1) {
							int targetRow = executionTarget.get(0).x, targetCol = executionTarget.get(0).y;
							if (unitsP1.get(targetRow).get(targetCol).isEmpty() && unitsP3.get(targetRow).get(targetCol).isEmpty() && unitsP3.get(targetRow).get(targetCol).isEmpty()) {
								if (unitsP4.get(targetRow).get(targetCol).isEmpty()) {
									int requestorIndex = unitsP4.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).indexOf(activityRequestor);
									unitsP4.get(targetRow).get(targetCol).add(unitsP4.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).get(requestorIndex));
									unitsP4.get(executionOrigin.get(0).x).get(executionOrigin.get(0).y).remove(requestorIndex);
									}
							}
						}
						break;
					}

				}
			}

			activePlayer = Player.PLAYER_1;
		}
		waitingState = 1;
	}

	private void addAllActivityListsAt(int row, int column, ArrayList<ArrayList<ArrayList<AbstractUnit>>> unitArray) {
		if (!unitArray.get(row).get(column).isEmpty()) {
			for (AbstractUnit unitOfInterest : unitsP1.get(row).get(column)) {
				unitOfInterest.activityList.organize();
				activityQueue.add(unitOfInterest.activityList);
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
			if (!unitsP1.get(r).get(c).isEmpty())
				return unitsP1.get(r).get(c).get(0).getDescriptor();
			else
				return terrainP1[r][c].getDescriptor();
		case PLAYER_2:
			if (!unitsP2.get(r).get(c).isEmpty())
				return unitsP2.get(r).get(c).get(0).getDescriptor();
			else
				return terrainP2[r][c].getDescriptor();
		case PLAYER_3:
			if (!unitsP3.get(r).get(c).isEmpty())
				return unitsP3.get(r).get(c).get(0).getDescriptor();
			else
				return terrainP3[r][c].getDescriptor();
		default:
			if (!unitsP4.get(r).get(c).isEmpty())
				return unitsP4.get(r).get(c).get(0).getDescriptor();
			else
				return terrainP4[r][c].getDescriptor();
		}
	}

	void setUnitFocusTarget(int r, int c) {
		switch (activePlayer) {
		case PLAYER_1:
			focusTarget = unitsP1.get(r).get(c).get(0);
			break;
		case PLAYER_2:
			focusTarget = unitsP2.get(r).get(c).get(0);
			break;
		case PLAYER_3:
			focusTarget = unitsP3.get(r).get(c).get(0);
			break;
		default:
			focusTarget = unitsP4.get(r).get(c).get(0);
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