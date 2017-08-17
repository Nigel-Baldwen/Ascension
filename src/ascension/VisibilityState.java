package ascension;

import java.util.ArrayList;

import ascension.AbstractUnit.UnitType;
import ascension.Terrain.TerrainSubType;
import ascension.Terrain.TerrainType;

class VisibilityState {
	UnitType occupyingUnitType, destinationUnit;
	TerrainType terrainType;
	TerrainSubType terrainSubType;
	ArrayList<UnitType> halfTransparencyUnits;
	
	
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
}