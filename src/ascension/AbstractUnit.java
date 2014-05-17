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

/**
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

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

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int toInt() {
		return 0;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public void setVisibleBy(int player) {
		visibleBy[player - 1] = player;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public boolean isVisibleBy(int player) {
		return visibleBy[player - 1] == player;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getIdValu() {
		return idValu;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getIntAtk() {
		return intAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getIntDef() {
		return intDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSolInv() {
		return solInv;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSolAff() {
		return solAff;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSymbio() {
		return symbio;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getHordAp() {
		return hordAp;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPoison() {
		return poison;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getHltReg() {
		return hltReg;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getKenisi() {
		return kenisi;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getCalmAu() {
		return calmAu;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getIllusi() {
		return illusi;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDisIll() {
		return disIll;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getHlyAtk() {
		return hlyAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getHlyDef() {
		return hlyDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDrkAtk() {
		return drkAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDrkDef() {
		return drkDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDispel() {
		return dispel;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSplStr() {
		return splStr;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSumStr() {
		return sumStr;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPlnAff() {
		return plnAff;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getThreat() {
		return threat;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getBravry() {
		return bravry;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getEthAtk() {
		return ethAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getEthDef() {
		return ethDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getWndAtk() {
		return wndAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getWndDef() {
		return wndDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getFirAtk() {
		return firAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getFirDef() {
		return firDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getWtrAtk() {
		return wtrAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getWtrDef() {
		return wtrDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDthBlw() {
		return dthBlw;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getNullif() {
		return nullif;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getUnarmd() {
		return unarmd;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getStrnth() {
		return strnth;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getAglAtk() {
		return aglAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getAglDef() {
		return aglDef;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getCharge() {
		return charge;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getStabil() {
		return stabil;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getBlunts() {
		return blunts;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPaddin() {
		return paddin;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getBlades() {
		return blades;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getShells() {
		return shells;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPierce() {
		return pierce;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getRnfrcm() {
		return rnfrcm;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgMel() {
		return dmgMel;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgRng() {
		return dmgRng;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgOne() {
		return dmgOne;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgTwo() {
		return dmgTwo;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgThr() {
		return dmgThr;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDmgFor() {
		return dmgFor;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getHealth() {
		return health;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getMagPnt() {
		return magPnt;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getMagRgn() {
		return magRgn;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getEngPnt() {
		return engPnt;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getEngRgn() {
		return engRgn;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getAtkSpd() {
		return atkSpd;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getNumAtk() {
		return numAtk;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSihtRd() {
		return sihtRd;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getMovSpd() {
		return movSpd;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPrdCst() {
		return prdCst;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getRanged() {
		return ranged;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getSqrOcu() {
		return sqrOcu;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getLevels() {
		return levels;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getDthEXP() {
		return dthEXP;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getPlayer() {
		return player;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getCurLoc() {
		return curLoc;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getChnTim() {
		return chnTim;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public int getTrnPld() {
		return trnPld;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public boolean isActive() {
		return active;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public void setVisible(boolean b) {
		visible = b;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public void setActive(boolean b) {
		active = b;
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public void setDisabled(boolean b) {
		disabled = b;
	}
}
