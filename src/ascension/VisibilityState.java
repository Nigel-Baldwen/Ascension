package ascension;

import java.util.ArrayList;
import java.util.HashSet;
import ascension.AbstractUnit.UnitType;
import ascension.PrimaryModel.Player;
import ascension.Terrain.TerrainSubType;
import ascension.Terrain.TerrainType;

/*
 * VisibilityState objects are designed such that they serve to
 * represent the current visual information accessible to a given
 * player about a given square.
 */

class VisibilityState {
	Player controllingPlayer;
	UnitType occupyingUnitType, destinationUnit;
	TerrainType terrainType;
	TerrainSubType terrainSubType;
	ArrayList<UnitType> halfTransparencyUnits;
	boolean isInVisionRange = false;
	
	VisibilityState () {
		occupyingUnitType = UnitType.EMPTY;
		halfTransparencyUnits = new ArrayList<UnitType>();
	}

	void setOccupyingUnit(UnitType unitType) {
		occupyingUnitType = unitType;
	}

	void setTerrainType(TerrainType _terrainType, TerrainSubType _terrainSubType) {
		terrainType = _terrainType;
		terrainSubType = _terrainSubType;
	}

	void addInMotionUnit(UnitType unitType) {
		halfTransparencyUnits.add(unitType);
	}
	
	void setDestinationUnit(UnitType unitType) {
		destinationUnit = unitType;
	}
	
	void setControllingPlayer(Player controller) {
		controllingPlayer = controller;
	}
}