package ascension;

class Terrain {

	protected enum TerrainType {
		CRYSTAL, DIRT, GRASS, ROCK, SAND;
		public static final TerrainType terrainTypeOrdinals[] = values();
	}
	protected TerrainType terrainType;
	protected enum TerrainSubType {
		ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT;
		public static final TerrainSubType terrainSubTypeOrdinals[] = values();
	}
	protected TerrainSubType terrainSubType;
	
	public Terrain(TerrainType type, TerrainSubType subType) {
		terrainType = type;
		terrainSubType = subType;
	}

	public int getPictureID() {
		return 0; // TODO REMOVE?
	}

	public String getDescriptor() {
		return terrainType.toString();
	}
}
