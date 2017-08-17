package ascension;

class Terrain {

	enum TerrainType {
		CRYSTAL, DIRT, GRASS, ROCK, SAND;
		public static final TerrainType terrainTypeOrdinals[] = values();
	}
	TerrainType terrainType;
	enum TerrainSubType {
		ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT;
		public static final TerrainSubType terrainSubTypeOrdinals[] = values();
	}
	protected TerrainSubType terrainSubType;
	
	Terrain(TerrainType type, TerrainSubType subType) {
		terrainType = type;
		terrainSubType = subType;
	}

	int getPictureID() {
		return 0; // TODO REMOVE?
	}

	String getDescriptor() {
		return terrainType.toString();
	}
}
