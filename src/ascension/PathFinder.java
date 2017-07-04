package ascension;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ascension.AbstractUnit.Locomotion;

/**
 * <p>
 * <code>Pathfinder</code> handles the automatic path generation
 * for units.
 * </p>
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class PathFinder {

	/* Values of zero indicate impassable terrain.
	 * Values of one indicate enemies.
	 * Values of two indicate allied structures.
	 * Values of three indicate allied units.
	 * Values of four indicate ground passable terrain.
	 * Values of five indicate flight passable terrain.
	 * Values of X indicate other passable terrain.
	 * Maybe eventually there will be burrowing and the like.
	 * Tree walking, that sort of stuff. Might end up needing a
	 * G-cost in light of that in addition to the F-cost.
	 */

	int[][] navigableTerrain;

	/**
	 * Creates a new <code>PathFinder</code>.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#loadInitialModelState(int, int) loadInitialModelState(int, int)}
	 * </ul>
	 * </p>
	 * 
	 * @param navigableTerrain
	 */

	public PathFinder(int[][] navigableTerrain) {
		this.navigableTerrain = navigableTerrain;
	}

	public ArrayList<Point> getPassivePath(int sRow, int sColumn, int dRow, int dColumn, int speed, Locomotion locomotion) {
		// Create a nodeMap, then fill it with default nodes.
		Node[][] nodeMap = new Node[speed * 2 + 1][speed * 2 + 1];
		for (int r = 0; r < nodeMap.length; r++) {
			for (int c = 0; c < nodeMap.length; c++) {
				nodeMap[r][c] = new Node(r, c);
			}
		}

		Node start = nodeMap[speed][speed]; // Taking stock of naught-based indexing

		// I know this seems a bit odd, but I did it out on paper...SO...yeah...
		Node destination = nodeMap[(speed - (sRow - dRow))][(speed - (sColumn - dColumn))];

		// Since default initialization is to 0, 1 represents a closed node.
		// Additionally, impassable terrain counts as a closed node because
		// the path could not possibly consider it.
		int[][] closedSet = new int[speed * 2 + 1][speed * 2 + 1];

		// We'll iterate over the closed set array to account for impassable terrain,
		// enemies, and other relevant path finding concerns.
		for (int r = 0; r < speed * 2 + 1; r++) {
			for (int c = 0; c < speed * 2 + 1; c++) {
				if (sRow - speed + r < 0 || sRow - speed + r >= navigableTerrain.length
						|| sColumn - speed + c < 0 || sColumn - speed + c >= navigableTerrain.length) {
					continue;
				}
				switch (navigableTerrain[sRow - speed + r][sColumn - speed + c]) {
				case 1: // Flying only
					if (locomotion != Locomotion.AIR) { // Unit can't fly
						closedSet[r][c] = 1;
					}
					break;
				case 2: // Completely impassable terrain
					closedSet[r][c] = 1;
					break;
				default: // Must be open
					break;
				}
			}
		}

		PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		openSet.add(start);
		start.setGCost(0);
		start.setFCost(calculateHCost(start, destination));

		// We're going to assume the longest travel.
		// This merely sets the initial size of the container though.
		// We'll trim it down to size once we return it.
		ArrayList<Point> bestPath = new ArrayList<Point>(speed);

		while (!openSet.isEmpty()) {
			Node current = openSet.poll();

			// We found the shortest path!
			if (current.equals(destination)) {
				// We'll add the points in last to first
				// We start at the back of the list so that the recipient can iterate easily.
				int index = speed;
				// Loop backwards through the parents until you hit the start.
				// The points are added according to the locations under consideration by the caller.
				while (!current.equals(start)) {
					int rMovement = current.getRow() - current.getParent().getRow();
					int cMovement = current.getColumn() - current.getParent().getColumn();
					dRow -= rMovement;
					dColumn -= cMovement;
					bestPath.add(--index, new Point(dRow, dColumn));
				}

				bestPath.trimToSize();
				System.out.println("WE DID IT TEAM!!!");
				return bestPath;
			}

			closedSet[current.getRow()][current.getColumn()] = 1;

			// Looking at the neighbors of the current node for info.
			for (int r = current.getRow() - 1; r < current.getRow() + 2; r++) {
				for (int c = current.getColumn() - 1; c < current.getColumn() + 2; c++) {
					if (r < 0 || r == nodeMap.length
							|| c < 0 || c == nodeMap.length
							|| closedSet[r][c] == 1) {
						continue;		// Ignore the neighbor which is already evaluated or nodes off of the map.
					}

					if (!openSet.contains(current))	// Discover a new node
						openSet.add(nodeMap[r][c]);

					// The distance from start to a neighbor
					int tempGCost = current.getGCost() + 1;
					if (tempGCost >= nodeMap[r][c].getGCost()) {
						continue;		// This is not a better path.
					}
					// This path is the best until now. Record it!
					nodeMap[r][c].setParent(current);
					nodeMap[r][c].setGCost(tempGCost);
					nodeMap[r][c].setFCost(nodeMap[r][c].getGCost() + calculateHCost(nodeMap[r][c], destination));
				}
			}
		}
		return null; // Exiting the while loop signifies failure.
	}

			private int calculateHCost(Node start, Node destination) {
				return Math.max(Math.abs(start.getRow() - destination.getRow()), Math.abs(start.getColumn() - destination.getColumn()));
			}

			/*
			 * Nodes track their most efficient ancestor as well as various costs.
			 */
			private class Node  implements Comparable<Node>{
				private int gCost, // Cost of getting from start to this. 
				fCost, // Cost, partly heuristic, of getting to goal through this node. f = h + g.
				row, column; // The location of the node on the grid relative to other nodes.
				Node parent; // Most efficient prior node.

				public Node(int _row, int _column) {
					fCost = 9999;
					gCost = 9999;
					row = _row;
					column = _column;
					parent = null;
				}

				public void setParent(Node _parent) {
					parent = _parent;
				}

				public int getGCost() {
					return gCost;
				}

				public void setGCost(int _gCost) {
					gCost = _gCost;
				}

				public void setFCost(int _fCost) {
					fCost = _fCost;
				}

				public int getRow() {
					return row;
				}

				public int getColumn() {
					return column;
				}

				public Node getParent() {
					return parent;
				}

				/**
				 * This particular implementation returns negative iff
				 * this F cost is less than that F cost, zero iff equal,
				 * positive iff greater than.
				 * 
				 * @see java.lang.Comparable#compareTo(java.lang.Object)
				 */
				@Override
				public int compareTo(Node other) {
					return this.fCost - other.fCost;
				}
			}
		}