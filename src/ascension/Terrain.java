package ascension;

public class Terrain {

	private int type, pictureID;
	
	public Terrain(int id, int picID) {
		type = id;
		pictureID = picID; // Think about whether or not pictureID should be mutable.
	}

	public int getPictureID() {
		return pictureID;
	}

	public String getDescriptor() {
		switch (type) {
		case 0:
			return "Crystal";
		case 1:
			return "Dirt";
		case 2:
			return "Grass";
		case 3:
			return "Rock";
		default:
			return "Sand";
		}
	}
}
