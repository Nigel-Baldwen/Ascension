package ascension;

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class PhysicalBuilder extends AbstractUnit {

	/**
	 * Creates a new PhysicalBuilder.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#loadInitialModelState(int, int) loadInitialModelState(int, int)}
	 * </ul>
	 * </p>
	 * 
	 * @param player - the owner
	 * @param location - the location in the grid
	 */
	public PhysicalBuilder(int player, int location) {
		intAtk = 3; intDef = 6;
		solInv = -1; solAff = 3;
		symbio = -1; hordAp = 3;
		poison = -1; hltReg = 3;
		kenisi = -1; calmAu = 3;
		illusi = -1; disIll = 4;
		hlyAtk = -1; hlyDef = 5;
		drkAtk = -1; drkDef = 4;
		dispel = -1; splStr = 4;
		sumStr = -1; plnAff = 3;
		threat = -1; bravry = 3;
		ethAtk = -1; ethDef = 3;
		wndAtk = -1; wndDef = 3;
		firAtk = -1; firDef = 4;
		wtrAtk = -1; wtrDef = 5;
		dthBlw = -1; nullif = 4;
		unarmd = -1; strnth = 3;
		aglAtk = -1; aglDef = 3;
		charge = -1; stabil = 3;
		blunts = -1; paddin = 3;
		blades = -1; shells = 4;
		pierce = 6; rnfrcm = 5;
		dmgMel = 3; 
		dmgRng = -1;
		dmgOne = 0;
		dmgTwo = 0;
		dmgThr = 0;
		dmgFor = 0;
		health = 12;
		magPnt = 6;
		magRgn = 3;
		engPnt = -1;
		engRgn = -1;
		atkSpd = 0;
		numAtk = 4;
		sihtRd = 6;
		movSpd = 6;
		prdCst = 4;
		ranged = -1;
		sqrOcu = 1;
		levels = 0;
		dthEXP = 6;
		this.player = player;
		curLoc = location;
		chnTim = 0;
		trnPld = 0;
		idValu = 0;
	}

	/**
	 * Returns the current visual state of this <code>PhysicalBuilder</code>.
	 * 
	 * @return - the current visual state
	 */
	public int toInt() {
		if (visible) {
			return 0;
		}
		else {
			return - 1;
		}
	}

	/**
	 * Returns the stats of the unit plus potentially some misc info.
	 */
	@Override
	public String getDescriptor() {
		String descriptor = health + ":" + magPnt + ":" + engPnt + ":" + 
				movSpd + ":" + sihtRd + ":" + numAtk + ":" + atkSpd + ":" + 
				ranged + ":" + sqrOcu + ":" + dthEXP + ":" + levels + ":" + 
				curLoc + ":" + player + ":" +
				/* Null value for the spacer */
				-1 + ":" +
				prdCst + ":" + trnPld + ":" + 
				intAtk + ":" + intDef + ":" + solInv + ":" + solAff + ":" + 
				symbio + ":" + hordAp + ":" + poison + ":" + hltReg + ":" + 
				kenisi + ":" + calmAu + ":" + illusi + ":" + disIll + ":" + 
				hlyAtk + ":" + hlyDef + ":" + drkAtk + ":" + drkDef + ":" + 
				dispel + ":" + splStr + ":" + sumStr + ":" + plnAff + ":" + 
				threat + ":" + bravry + ":" + dthBlw + ":" + nullif + ":" +
				ethAtk + ":" + ethDef + ":" + wndAtk + ":" + wndDef + ":" + 
				firAtk + ":" + firDef + ":" + wtrAtk + ":" + wtrDef + ":" + 
				charge + ":" + stabil + ":" + aglAtk + ":" + aglDef + ":" + 
				unarmd + ":" + strnth + ":" + blunts + ":" + paddin + ":" + 
				blades + ":" + shells + ":" + pierce + ":" + rnfrcm;
		return descriptor;
	}
}
