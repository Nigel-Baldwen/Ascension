package ascension;

// @formatter:off
/* AbstractUnit, as may be guessed, serves as
 * the basic structure for all units. All units
 * possess several stats, represented by named
 * int fields. They additionally possess at
 * least one basic, melee attack and four
 * special abilities. (The builder units give
 * one of their "ability" slots to their building
 * menus)
 */
// @formatter:on

public class AbstractUnit {

	// @formatter:off
	// All atk/def stats are paired left to right.
	// Values of -1 indicate that the given stat
	// is not readily applicable to the given unit.
	protected int intAtk, intDef,
				solInv, solAff,
				symbio, hordAp,
				poison, hltReg,
				kenisi, calmAu,
				illusi, disIll,
				hlyAtk, hlyDef,
				drkAtk, drkDef,
				dispel, splStr,
				sumStr, plnAff,
				threat, bravry,
				ethAtk, ethDef,
				wndAtk, wndDef,
				firAtk, firDef,
				wtrAtk, wtrDef,
				dthBlw, nullif,
				unarmd, strnth,
				aglAtk, aglDef,
				charge, stabil,
				blunts, paddin,
				blades, shells,
				pierce, rnfrcm,
	// All Utility & Damage Stats are listed singly.
				dmgMel,
				dmgRng,
				dmgOne,
				dmgTwo,
				dmgThr,
				dmgFor,
				health,
				magPnt,
				magRgn,
				engPnt,
				engRgn,
				atkSpd,
				numAtk,
				sihtRd,
				movSpd,
				prdCst,
				ranged,
				sqrOcu,
				levels,
				dthEXP,
				player,
				curLoc,
				chnTim,
				trnPld,
				idValu;
	private int[] visibleBy = new int[4];
	protected boolean visible = false, active = false, disabled;
	// @formatter:on

	// Returns an int which encapsulates the visual state of the unit
	public int toInt() {
		return 0;
	}
	
	public void setVisibleBy(int player) {
		visibleBy[player - 1] = player;
	}
	
	public boolean isVisibleBy(int player) {
		return visibleBy[player - 1] == player;
	}
	public int getIdValu() {
		return idValu;
	}

	public int getIntAtk() {
		return intAtk;
	}

	public int getIntDef() {
		return intDef;
	}

	public int getSolInv() {
		return solInv;
	}

	public int getSolAff() {
		return solAff;
	}

	public int getSymbio() {
		return symbio;
	}

	public int getHordAp() {
		return hordAp;
	}

	public int getPoison() {
		return poison;
	}

	public int getHltReg() {
		return hltReg;
	}

	public int getKenisi() {
		return kenisi;
	}

	public int getCalmAu() {
		return calmAu;
	}

	public int getIllusi() {
		return illusi;
	}

	public int getDisIll() {
		return disIll;
	}

	public int getHlyAtk() {
		return hlyAtk;
	}

	public int getHlyDef() {
		return hlyDef;
	}

	public int getDrkAtk() {
		return drkAtk;
	}

	public int getDrkDef() {
		return drkDef;
	}

	public int getDispel() {
		return dispel;
	}

	public int getSplStr() {
		return splStr;
	}

	public int getSumStr() {
		return sumStr;
	}

	public int getPlnAff() {
		return plnAff;
	}

	public int getThreat() {
		return threat;
	}

	public int getBravry() {
		return bravry;
	}

	public int getEthAtk() {
		return ethAtk;
	}

	public int getEthDef() {
		return ethDef;
	}

	public int getWndAtk() {
		return wndAtk;
	}

	public int getWndDef() {
		return wndDef;
	}

	public int getFirAtk() {
		return firAtk;
	}

	public int getFirDef() {
		return firDef;
	}

	public int getWtrAtk() {
		return wtrAtk;
	}

	public int getWtrDef() {
		return wtrDef;
	}

	public int getDthBlw() {
		return dthBlw;
	}

	public int getNullif() {
		return nullif;
	}

	public int getUnarmd() {
		return unarmd;
	}

	public int getStrnth() {
		return strnth;
	}

	public int getAglAtk() {
		return aglAtk;
	}

	public int getAglDef() {
		return aglDef;
	}

	public int getCharge() {
		return charge;
	}

	public int getStabil() {
		return stabil;
	}

	public int getBlunts() {
		return blunts;
	}

	public int getPaddin() {
		return paddin;
	}

	public int getBlades() {
		return blades;
	}

	public int getShells() {
		return shells;
	}

	public int getPierce() {
		return pierce;
	}

	public int getRnfrcm() {
		return rnfrcm;
	}

	public int getDmgMel() {
		return dmgMel;
	}

	public int getDmgRng() {
		return dmgRng;
	}

	public int getDmgOne() {
		return dmgOne;
	}

	public int getDmgTwo() {
		return dmgTwo;
	}

	public int getDmgThr() {
		return dmgThr;
	}

	public int getDmgFor() {
		return dmgFor;
	}

	public int getHealth() {
		return health;
	}

	public int getMagPnt() {
		return magPnt;
	}

	public int getMagRgn() {
		return magRgn;
	}

	public int getEngPnt() {
		return engPnt;
	}

	public int getEngRgn() {
		return engRgn;
	}

	public int getAtkSpd() {
		return atkSpd;
	}

	public int getNumAtk() {
		return numAtk;
	}

	public int getSihtRd() {
		return sihtRd;
	}

	public int getMovSpd() {
		return movSpd;
	}

	public int getPrdCst() {
		return prdCst;
	}

	public int getRanged() {
		return ranged;
	}

	public int getSqrOcu() {
		return sqrOcu;
	}

	public int getLevels() {
		return levels;
	}

	public int getDthEXP() {
		return dthEXP;
	}

	public int getPlayer() {
		return player;
	}

	public int getCurLoc() {
		return curLoc;
	}

	public int getChnTim() {
		return chnTim;
	}

	public int getTrnPld() {
		return trnPld;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isDisabled() {
		return disabled;
	}
	
	public void setVisible(boolean b) {
		visible = b;
	}
	
	public void setActive(boolean b) {
		active = b;
	}
	
	public void setDisabled(boolean b) {
		disabled = b;
	}
}
