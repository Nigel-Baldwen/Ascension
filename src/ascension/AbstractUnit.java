package ascension;

/**
 * <code>AbstractUnit</code> serves as the basis for all units.
 * 
 * <p>
 * All units possess several stats represented by named int fields.
 * They additionally possess at least one basic attack and four
 * special abilities.
 * </p>
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class AbstractUnit {

	// @formatter:off
	// All atk/def stats are paired left to right.
	// values of -1 indicate that the given stat
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
	protected ActivityList activityList = new ActivityList();
	private int[] visibleBy = new int[4];
	protected boolean visible = false, active = false, disabled;
	// @formatter:on

	/**
	 * Adds an <code>Activity</code> to the <code>AbstractUnit</code>'s
	 * <code>ActivityList</code>.
	 * 
	 * @param activity - the <code>Activity</code> to be added
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	/**
	 * Returns the unit's <code>ActivityList</code>.
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#rotateTurn() rotateTurn()}
	 * </ul>
	 * <p>
	 * @return the unit's <code>ActivityList</code>
	 */
	public ActivityList getActivityList() {
		return activityList;
	}
	
	/**
	 * Returns an int which encapsulates the visual state of the unit.
	 * 
	 * <p>
	 * Only <code>AbstractUnit</code> objects return a value of 0.
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryModel#generateVisualModel(int[][], AbstractUnit[][], int[][]) generateVisualModel(int[][], AbstractUnit[][], int[][])}
	 * <li> {@link PrimaryModel#transferUnit(int, int, int, int) transferUnit(int, int, int, int)}
	 * </ul>
	 * <p>
	 * 
	 * @return the unit's visual state
	 */
	public int toInt() {
		return 0;
	}

	/**
	 * Allows an individual unit to track who sees it.
	 * 
	 * @param player - the player who sees the unit
	 */
	public void setVisibleBy(int player) {
		visibleBy[player - 1] = player;
	}

	/**
	 * Check to see if a unit is visible to a given player.
	 * 
	 * @param player - the player checking for vision
	 * @return true if the player can see the unit
	 */
	public boolean isVisibleBy(int player) {
		return visibleBy[player - 1] == player;
	}

	/**
	 * Returns the unit's ID value which corresponds to its type.
	 * 
	 * @return the unit's type
	 */
	public int getIdValu() {
		return idValu;
	}

	/**
	 * Returns the unit's Intelligence Attack value.
	 * 
	 * @return the unit's Intelligence Attack value.
	 */
	public int getIntAtk() {
		return intAtk;
	}

	/**
	 * Returns the unit's Intelligence Defense value.
	 * 
	 * @return the unit's Intelligence Defense value.
	 */
	public int getIntDef() {
		return intDef;
	}

	/**
	 * Returns the unit's Soul Investment Attack value.
	 * 
	 * @return the unit's Soul Investment Attack value.
	 */
	public int getSolInv() {
		return solInv;
	}

	/**
	 * Returns the unit's Soul Affinity Defense value.
	 * 
	 * @return the unit's Soul Affinity Defense value.
	 */
	public int getSolAff() {
		return solAff;
	}

	/**
	 * Returns the unit's Symbiosis Attack value.
	 * 
	 * @return the unit's Symbiosis Attack value.
	 */
	public int getSymbio() {
		return symbio;
	}

	/**
	 * Returns the unit's Hoarding Aptitude Defense value.
	 * 
	 * @return the unit's Hoarding Aptitude Defense value.
	 */
	public int getHordAp() {
		return hordAp;
	}

	/**
	 * Returns the unit's Poison Attack value.
	 * 
	 * @return the unit's Poison Attack value.
	 */
	public int getPoison() {
		return poison;
	}

	/**
	 * Returns the unit's Health Regeneration Defense value.
	 * 
	 * @return the unit's Health Regeneration Defense value.
	 */
	public int getHltReg() {
		return hltReg;
	}

	/**
	 * Returns the unit's Kinesis Attack value.
	 * 
	 * @return the unit's Kinesis Attack value.
	 */
	public int getKenisi() {
		return kenisi;
	}

	/**
	 * Returns the unit's Calming Aura Defense value.
	 * 
	 * @return the unit's Calming Aura Defense value.
	 */
	public int getCalmAu() {
		return calmAu;
	}

	/**
	 * Returns the unit's Illusion Attack value.
	 * 
	 * @return the unit's Illusion Attack value.
	 */
	public int getIllusi() {
		return illusi;
	}

	/**
	 * Returns the unit's Disillusionment Defense value.
	 * 
	 * @return the unit's Disillusionment Defense value.
	 */
	public int getDisIll() {
		return disIll;
	}

	/**
	 * Returns the unit's Holy Attack value.
	 * 
	 * @return the unit's Holy Attack value.
	 */
	public int getHlyAtk() {
		return hlyAtk;
	}

	/**
	 * Returns the unit's Holy Defense value.
	 * 
	 * @return the unit's Holy Defense value.
	 */
	public int getHlyDef() {
		return hlyDef;
	}

	/**
	 * Returns the unit's Dark Attack value.
	 * 
	 * @return the unit's Dark Attack value.
	 */
	public int getDrkAtk() {
		return drkAtk;
	}

	/**
	 * Returns the unit's Dark Defense value.
	 * 
	 * @return the unit's Dark Defense value.
	 */
	public int getDrkDef() {
		return drkDef;
	}

	/**
	 * Returns the unit's Dispel Attack value.
	 * 
	 * @return the unit's Dispel Attack value.
	 */
	public int getDispel() {
		return dispel;
	}

	/**
	 * Returns the unit's Spell Strength Defense value.
	 * 
	 * @return the unit's Spell Strength Defense value.
	 */
	public int getSplStr() {
		return splStr;
	}

	/**
	 * Returns the unit's Summon Strength Attack value.
	 * 
	 * @return the unit's Summon Strength Attack value.
	 */
	public int getSumStr() {
		return sumStr;
	}

	/**
	 * Returns the unit's Planar Affinity Defense value.
	 * 
	 * @return the unit's Planar Affinity Defense value.
	 */
	public int getPlnAff() {
		return plnAff;
	}

	/**
	 * Returns the unit's Threat Attack value.
	 * 
	 * @return the unit's Threat Attack value.
	 */
	public int getThreat() {
		return threat;
	}

	/**
	 * Returns the unit's Bravery Defense value.
	 * 
	 * @return the unit's Bravery Defense value.
	 */
	public int getBravry() {
		return bravry;
	}

	/**
	 * Returns the unit's Earth Attack value.
	 * 
	 * @return the unit's Earth Attack value.
	 */
	public int getEthAtk() {
		return ethAtk;
	}

	/**
	 * Returns the unit's Earth Defense value.
	 * 
	 * @return the unit's Earth Defense value.
	 */
	public int getEthDef() {
		return ethDef;
	}

	/**
	 * Returns the unit's Wind Attack value.
	 * 
	 * @return the unit's Wind Attack value.
	 */
	public int getWndAtk() {
		return wndAtk;
	}

	/**
	 * Returns the unit's Wind Defense value.
	 * 
	 * @return the unit's Wind Defense value.
	 */
	public int getWndDef() {
		return wndDef;
	}

	/**
	 * Returns the unit's Fire Attack value.
	 * 
	 * @return the unit's Fire Attack value.
	 */
	public int getFirAtk() {
		return firAtk;
	}

	/**
	 * Returns the unit's Fire Defense value.
	 * 
	 * @return the unit's Fire Defense value.
	 */
	public int getFirDef() {
		return firDef;
	}

	/**
	 * Returns the unit's Water Attack value.
	 * 
	 * @return the unit's Water Attack value.
	 */
	public int getWtrAtk() {
		return wtrAtk;
	}

	/**
	 * Returns the unit's Water Defense value.
	 * 
	 * @return the unit's Water Defense value.
	 */
	public int getWtrDef() {
		return wtrDef;
	}

	/**
	 * Returns the unit's Death Blow Attack value.
	 * 
	 * @return the unit's Death Blow Attack value.
	 */
	public int getDthBlw() {
		return dthBlw;
	}

	/**
	 * Returns the unit's Nullification Defense value.
	 * 
	 * @return the unit's Nullification Defense value.
	 */
	public int getNullif() {
		return nullif;
	}

	/**
	 * Returns the unit's Unarmed Attack value.
	 * 
	 * @return the unit's Unarmed Attack value.
	 */
	public int getUnarmd() {
		return unarmd;
	}

	/**
	 * Returns the unit's Strength Defense value.
	 * 
	 * @return the unit's Strength Defense value.
	 */
	public int getStrnth() {
		return strnth;
	}

	/**
	 * Returns the unit's Agility Attack value.
	 * 
	 * @return the unit's Agility Attack value.
	 */
	public int getAglAtk() {
		return aglAtk;
	}

	/**
	 * Returns the unit's Agility Defense value.
	 * 
	 * @return the unit's Agility Defense value.
	 */
	public int getAglDef() {
		return aglDef;
	}

	/**
	 * Returns the unit's Charge value.
	 * 
	 * @return the unit's Charge value.
	 */
	public int getCharge() {
		return charge;
	}

	/**
	 * Returns the unit's Stability.
	 * 
	 * @return the unit's Stability.
	 */
	public int getStabil() {
		return stabil;
	}

	/**
	 * Returns the unit's Blunt Attack value.
	 * 
	 * @return the unit's Blunt Attack value.
	 */
	public int getBlunts() {
		return blunts;
	}

	/**
	 * Returns the unit's Padding Defense value.
	 * 
	 * @return the unit's Padding Defense value.
	 */
	public int getPaddin() {
		return paddin;
	}

	/**
	 * Returns the unit's Blade Attack value.
	 * 
	 * @return the unit's Blade Attack value.
	 */
	public int getBlades() {
		return blades;
	}

	/**
	 * Returns the unit's Shell Defense value.
	 * 
	 * @return the unit's Shell Defense value.
	 */
	public int getShells() {
		return shells;
	}

	/**
	 * Returns the unit's Pierce Attack value.
	 * 
	 * @return the unit's Pierce Attack value.
	 */
	public int getPierce() {
		return pierce;
	}

	/**
	 * Returns the unit's Reinforcement Defense value.
	 * 
	 * @return the unit's Reinforcement Defense value.
	 */
	public int getRnfrcm() {
		return rnfrcm;
	}

	/**
	 * Returns the unit's Melee Attack Damage.
	 * 
	 * @return the unit's Melee Attack Damage.
	 */
	public int getDmgMel() {
		return dmgMel;
	}

	/**
	 * Returns the unit's Ranged Attack Damage.
	 * 
	 * @return the unit's Ranged Attack Damage.
	 */
	public int getDmgRng() {
		return dmgRng;
	}

	/**
	 * Returns the unit's Damage for Special Attack One.
	 * 
	 * @return the unit's Damage for Special Attack One.
	 */
	public int getDmgOne() {
		return dmgOne;
	}

	/**
	 * Returns the unit's Damage for Special Attack Two.
	 * 
	 * @return the unit's Damage for Special Attack Two.
	 */
	public int getDmgTwo() {
		return dmgTwo;
	}

	/**
	 * Returns the unit's Damage for Special Attack Three.
	 * 
	 * @return the unit's Damage for Special Attack Three.
	 */
	public int getDmgThr() {
		return dmgThr;
	}

	/**
	 * Returns the unit's Damage for Special Attack Four.
	 * 
	 * @return the unit's Damage for Special Attack Four.
	 */
	public int getDmgFor() {
		return dmgFor;
	}

	/**
	 * Returns the unit's Health Capacity.
	 * 
	 * @return the unit's Health Capacity.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Returns the unit's Mana Capacity.
	 * 
	 * @return the unit's Mana Capacity.
	 */
	public int getMagPnt() {
		return magPnt;
	}

	/**
	 * Returns the unit's Mana Regeneration Rate.
	 * 
	 * @return the unit's Mana Regeneration Rate.
	 */
	public int getMagRgn() {
		return magRgn;
	}

	/**
	 * Returns the unit's Energy Capacity.
	 * 
	 * @return the unit's Energy Capacity.
	 */
	public int getEngPnt() {
		return engPnt;
	}

	/**
	 * Returns the unit's Energy Regeneration Rate.
	 * 
	 * @return the unit's Energy Regeneration Rate.
	 */
	public int getEngRgn() {
		return engRgn;
	}

	/**
	 * Returns the unit's Attack Speed.
	 * 
	 * @return the unit's Attack Speed.
	 */
	public int getAtkSpd() {
		return atkSpd;
	}

	/**
	 * Returns the unit's Attack Per Turn.
	 * 
	 * @return the unit's Attacks Per Turn.
	 */
	public int getNumAtk() {
		return numAtk;
	}

	/**
	 * Returns the unit's Sight Radius.
	 * 
	 * @return the unit's Sight Radius.
	 */
	public int getSihtRd() {
		return sihtRd;
	}

	/**
	 * Returns the unit's Movement Speed.
	 * 
	 * @return the unit's Movement Speed.
	 */
	public int getMovSpd() {
		return movSpd;
	}

	/**
	 * Returns the unit's Production Cost.
	 * 
	 * @return the unit's Production Cost.
	 */
	public int getPrdCst() {
		return prdCst;
	}

	/**
	 * Returns the unit's Attack Range.
	 * 
	 * @return the unit's Attack Range.
	 */
	public int getRanged() {
		return ranged;
	}

	/**
	 * Returns the unit's Size In Squares.
	 * 
	 * @return the unit's Size In Squares.
	 */
	public int getSqrOcu() {
		return sqrOcu;
	}

	/**
	 * Returns the unit's Levels.
	 * 
	 * @return the unit's Levels.
	 */
	public int getLevels() {
		return levels;
	}

	/**
	 * Returns the unit's Death Experience Reward.
	 * 
	 * @return the unit's Death Experience Reward.
	 */
	public int getDthEXP() {
		return dthEXP;
	}

	/**
	 * Returns the unit's Owner.
	 * 
	 * @return the unit's Owner.
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * Returns the unit's Current Location.
	 * 
	 * @return the unit's Current Location.
	 */
	public int getCurLoc() {
		return curLoc;
	}

	/**
	 * Returns the unit's Channel Time.
	 * 
	 * @return the unit's Channel Time.
	 */
	public int getChnTim() {
		return chnTim;
	}

	/**
	 * Returns the number of turns the unit has been in the game.
	 * 
	 * @return the number of turns the unit has been in the game.
	 */
	public int getTrnPld() {
		return trnPld;
	}

	/**
	 * Indicates whether or not the unit is visible.
	 * 
	 * @return true if the unit is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Indicates whether or not the unit is active.
	 * 
	 * @return true if the unit is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Indicates whether or not the unit is disabled.
	 * 
	 * @return true if the unit is disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Sets the unit to be visible or invisible.
	 * 
	 * @param b - if true, set visible
	 */
	public void setVisible(boolean b) {
		visible = b;
	}

	/**
	 * Sets the unit to be active or inactive.
	 * 
	 * @param b - if true, set active
	 */
	public void setActive(boolean b) {
		active = b;
	}

	/**
	 * Sets the unit to be disabled or enabled.
	 * 
	 * @param b - if true, set disabled
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}

	public String getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}
}
