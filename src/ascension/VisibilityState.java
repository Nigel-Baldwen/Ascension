package ascension;

import java.util.ArrayList;

import ascension.AbstractUnit.UnitType;
import ascension.Terrain.TerrainSubType;
import ascension.Terrain.TerrainType;

class VisibilityState {
	protected UnitType occupyingUnitType, destinationUnit;
	protected TerrainType terrainType;
	protected TerrainSubType terrainSubType;
	protected ArrayList<UnitType> halfTransparencyUnits;
	
	public VisibilityState () {
		occupyingUnitType = UnitType.EMPTY;
		halfTransparencyUnits = new ArrayList<UnitType>();
	}

	public void setOccupyingUnit(UnitType unitType) {
		occupyingUnitType = unitType;
	}

	public void setTerrainType(TerrainType _terrainType, TerrainSubType _terrainSubType) {
		terrainType = _terrainType;
		terrainSubType = _terrainSubType;
	}

	public void addInMotionUnit(UnitType unitType) {
		halfTransparencyUnits.add(unitType);
	}
	
	public void setDestinationUnit(UnitType unitType) {
		destinationUnit = unitType;
	}
}