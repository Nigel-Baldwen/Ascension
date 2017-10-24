package ascension;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import ascension.Activity.ActivityType;
import ascension.PrimaryModel.Player;

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

class AbstractUnit {

	// @formatter:off
	// All atk/def stats are paired left to right.
	// values of -1 indicate that the given stat
	// is not readily applicable to the given unit.
	protected int intelligenceAttack, intelligenceDefense,
	soulInvestment, soulAffinity,
	symbiosis, hoardingAptitude,
	poison, healthRegeneration,
	kenisis, calmingAura,
	illusion, disillusion,
	holyAttack, holyDefense,
	darkAttack, darkDefense,
	dispel, spellStrength,
	summonStrength, planarAffinity,
	threat, bravery,
	earthAttack, earthDefense,
	windAttack, windDefense,
	fireAttack, fireDefense,
	waterAttack, waterDefense,
	deathBlow, nullification,
	unarmed, strength,
	agilityAttack, agilityDefense,
	charge, stability,
	bluntAttack, paddingDefense,
	bladeAttack, shellDefense,
	pierceAttack, reinforcementDefense,
	// All Utility & Damage Stats are listed singly.
	damageMelee,
	damageRanged,
	damageAbilityOne,
	damageAbilityTwo,
	damageAbilityThree,
	damageAbilityFour,
	healthPoints,
	magicPoints,
	magicRegeneration,
	energyPoints,
	energyRegeneration,
	attackSpeed,
	numberOfAttacks,
	sightRadius,
	moveSpeed,
	productionCost,
	range,
	squaresOccupied,
	level,
	deathEXP,
	curLocR,
	curLocC,
	channelTime,
	turnsPlayed,
	idValue,
	// commandCount indicates how many commands have been issued to the unit.
	// This assists with prioritizing issued commands per unit.
	commandCount;

	Player player; 
	enum Locomotion { GROUND, AIR } // Potentially burrowing, tree-walking, etc.
	Locomotion locomotion;
	enum UnitType { EMPTY, PHYSICAL_BUILDER };
	UnitType unitType;
	ActivityList activityList = new ActivityList();
	private int[] visibleBy = new int[4];
	boolean visible = false, active = false, canMove = false, disabled;
	Point curLoc; //  TODO Probably need to shift curLoc to an ArrayList<Point> itself

	// @formatter:on

	/**
	 * Adds an <code>Activity</code> to the <code>AbstractUnit</code>'s
	 * <code>ActivityList</code>.
	 * 
	 * @param activity - the <code>Activity</code> to be added
	 */
	void addActivity(Activity activity) {
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
	ActivityList getActivityList() {
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
	int toInt() {
		return 0;
	}

	/**
	 * Allows an individual unit to track who sees it.
	 * 
	 * @param player - the player who sees the unit
	 */
	void setVisibleBy(int player) {
		visibleBy[player - 1] = player;
	}

	/**
	 * Check to see if a unit is visible to a given player.
	 * 
	 * @param player - the player checking for vision
	 * @return true if the player can see the unit
	 */
	boolean isVisibleBy(int player) {
		return visibleBy[player - 1] == player;
	}

	/**
	 * Returns the unit's ID value which corresponds to its type.
	 * 
	 * @return the unit's type
	 */
	int getIdValu() {
		return idValue;
	}

	/**
	 * Returns the unit's Intelligence Attack value.
	 * 
	 * @return the unit's Intelligence Attack value.
	 */
	int getIntAtk() {
		return intelligenceAttack;
	}

	/**
	 * Returns the unit's Intelligence Defense value.
	 * 
	 * @return the unit's Intelligence Defense value.
	 */
	int getIntDef() {
		return intelligenceDefense;
	}

	/**
	 * Returns the unit's Soul Investment Attack value.
	 * 
	 * @return the unit's Soul Investment Attack value.
	 */
	int getSolInv() {
		return soulInvestment;
	}

	/**
	 * Returns the unit's Soul Affinity Defense value.
	 * 
	 * @return the unit's Soul Affinity Defense value.
	 */
	int getSolAff() {
		return soulAffinity;
	}

	/**
	 * Returns the unit's Symbiosis Attack value.
	 * 
	 * @return the unit's Symbiosis Attack value.
	 */
	int getSymbio() {
		return symbiosis;
	}

	/**
	 * Returns the unit's Hoarding Aptitude Defense value.
	 * 
	 * @return the unit's Hoarding Aptitude Defense value.
	 */
	int getHordAp() {
		return hoardingAptitude;
	}

	/**
	 * Returns the unit's Poison Attack value.
	 * 
	 * @return the unit's Poison Attack value.
	 */
	int getPoison() {
		return poison;
	}

	/**
	 * Returns the unit's Health Regeneration Defense value.
	 * 
	 * @return the unit's Health Regeneration Defense value.
	 */
	int getHltReg() {
		return healthRegeneration;
	}

	/**
	 * Returns the unit's Kinesis Attack value.
	 * 
	 * @return the unit's Kinesis Attack value.
	 */
	int getKenisi() {
		return kenisis;
	}

	/**
	 * Returns the unit's Calming Aura Defense value.
	 * 
	 * @return the unit's Calming Aura Defense value.
	 */
	int getCalmAu() {
		return calmingAura;
	}

	/**
	 * Returns the unit's Illusion Attack value.
	 * 
	 * @return the unit's Illusion Attack value.
	 */
	int getIllusi() {
		return illusion;
	}

	/**
	 * Returns the unit's Disillusionment Defense value.
	 * 
	 * @return the unit's Disillusionment Defense value.
	 */
	int getDisIll() {
		return disillusion;
	}

	/**
	 * Returns the unit's Holy Attack value.
	 * 
	 * @return the unit's Holy Attack value.
	 */
	int getHlyAtk() {
		return holyAttack;
	}

	/**
	 * Returns the unit's Holy Defense value.
	 * 
	 * @return the unit's Holy Defense value.
	 */
	int getHlyDef() {
		return holyDefense;
	}

	/**
	 * Returns the unit's Dark Attack value.
	 * 
	 * @return the unit's Dark Attack value.
	 */
	int getDrkAtk() {
		return darkAttack;
	}

	/**
	 * Returns the unit's Dark Defense value.
	 * 
	 * @return the unit's Dark Defense value.
	 */
	int getDrkDef() {
		return darkDefense;
	}

	/**
	 * Returns the unit's Dispel Attack value.
	 * 
	 * @return the unit's Dispel Attack value.
	 */
	int getDispel() {
		return dispel;
	}

	/**
	 * Returns the unit's Spell Strength Defense value.
	 * 
	 * @return the unit's Spell Strength Defense value.
	 */
	int getSplStr() {
		return spellStrength;
	}

	/**
	 * Returns the unit's Summon Strength Attack value.
	 * 
	 * @return the unit's Summon Strength Attack value.
	 */
	int getSumStr() {
		return summonStrength;
	}

	/**
	 * Returns the unit's Planar Affinity Defense value.
	 * 
	 * @return the unit's Planar Affinity Defense value.
	 */
	int getPlnAff() {
		return planarAffinity;
	}

	/**
	 * Returns the unit's Threat Attack value.
	 * 
	 * @return the unit's Threat Attack value.
	 */
	int getThreat() {
		return threat;
	}

	/**
	 * Returns the unit's Bravery Defense value.
	 * 
	 * @return the unit's Bravery Defense value.
	 */
	int getBravry() {
		return bravery;
	}

	/**
	 * Returns the unit's Earth Attack value.
	 * 
	 * @return the unit's Earth Attack value.
	 */
	int getEthAtk() {
		return earthAttack;
	}

	/**
	 * Returns the unit's Earth Defense value.
	 * 
	 * @return the unit's Earth Defense value.
	 */
	int getEthDef() {
		return earthDefense;
	}

	/**
	 * Returns the unit's Wind Attack value.
	 * 
	 * @return the unit's Wind Attack value.
	 */
	int getWndAtk() {
		return windAttack;
	}

	/**
	 * Returns the unit's Wind Defense value.
	 * 
	 * @return the unit's Wind Defense value.
	 */
	int getWndDef() {
		return windDefense;
	}

	/**
	 * Returns the unit's Fire Attack value.
	 * 
	 * @return the unit's Fire Attack value.
	 */
	int getFirAtk() {
		return fireAttack;
	}

	/**
	 * Returns the unit's Fire Defense value.
	 * 
	 * @return the unit's Fire Defense value.
	 */
	int getFirDef() {
		return fireDefense;
	}

	/**
	 * Returns the unit's Water Attack value.
	 * 
	 * @return the unit's Water Attack value.
	 */
	int getWtrAtk() {
		return waterAttack;
	}

	/**
	 * Returns the unit's Water Defense value.
	 * 
	 * @return the unit's Water Defense value.
	 */
	int getWtrDef() {
		return waterDefense;
	}

	/**
	 * Returns the unit's Death Blow Attack value.
	 * 
	 * @return the unit's Death Blow Attack value.
	 */
	int getDthBlw() {
		return deathBlow;
	}

	/**
	 * Returns the unit's Nullification Defense value.
	 * 
	 * @return the unit's Nullification Defense value.
	 */
	int getNullif() {
		return nullification;
	}

	/**
	 * Returns the unit's Unarmed Attack value.
	 * 
	 * @return the unit's Unarmed Attack value.
	 */
	int getUnarmd() {
		return unarmed;
	}

	/**
	 * Returns the unit's Strength Defense value.
	 * 
	 * @return the unit's Strength Defense value.
	 */
	int getStrnth() {
		return strength;
	}

	/**
	 * Returns the unit's Agility Attack value.
	 * 
	 * @return the unit's Agility Attack value.
	 */
	int getAglAtk() {
		return agilityAttack;
	}

	/**
	 * Returns the unit's Agility Defense value.
	 * 
	 * @return the unit's Agility Defense value.
	 */
	int getAglDef() {
		return agilityDefense;
	}

	/**
	 * Returns the unit's Charge value.
	 * 
	 * @return the unit's Charge value.
	 */
	int getCharge() {
		return charge;
	}

	/**
	 * Returns the unit's Stability.
	 * 
	 * @return the unit's Stability.
	 */
	int getStabil() {
		return stability;
	}

	/**
	 * Returns the unit's Blunt Attack value.
	 * 
	 * @return the unit's Blunt Attack value.
	 */
	int getBlunts() {
		return bluntAttack;
	}

	/**
	 * Returns the unit's Padding Defense value.
	 * 
	 * @return the unit's Padding Defense value.
	 */
	int getPaddin() {
		return paddingDefense;
	}

	/**
	 * Returns the unit's Blade Attack value.
	 * 
	 * @return the unit's Blade Attack value.
	 */
	int getBlades() {
		return bladeAttack;
	}

	/**
	 * Returns the unit's Shell Defense value.
	 * 
	 * @return the unit's Shell Defense value.
	 */
	int getShells() {
		return shellDefense;
	}

	/**
	 * Returns the unit's Pierce Attack value.
	 * 
	 * @return the unit's Pierce Attack value.
	 */
	int getPierce() {
		return pierceAttack;
	}

	/**
	 * Returns the unit's Reinforcement Defense value.
	 * 
	 * @return the unit's Reinforcement Defense value.
	 */
	int getRnfrcm() {
		return reinforcementDefense;
	}

	/**
	 * Returns the unit's Melee Attack Damage.
	 * 
	 * @return the unit's Melee Attack Damage.
	 */
	int getDmgMel() {
		return damageMelee;
	}

	/**
	 * Returns the unit's Ranged Attack Damage.
	 * 
	 * @return the unit's Ranged Attack Damage.
	 */
	int getDmgRng() {
		return damageRanged;
	}

	/**
	 * Returns the unit's Damage for Special Attack One.
	 * 
	 * @return the unit's Damage for Special Attack One.
	 */
	int getDmgOne() {
		return damageAbilityOne;
	}

	/**
	 * Returns the unit's Damage for Special Attack Two.
	 * 
	 * @return the unit's Damage for Special Attack Two.
	 */
	int getDmgTwo() {
		return damageAbilityTwo;
	}

	/**
	 * Returns the unit's Damage for Special Attack Three.
	 * 
	 * @return the unit's Damage for Special Attack Three.
	 */
	int getDmgThr() {
		return damageAbilityThree;
	}

	/**
	 * Returns the unit's Damage for Special Attack Four.
	 * 
	 * @return the unit's Damage for Special Attack Four.
	 */
	int getDmgFor() {
		return damageAbilityFour;
	}

	/**
	 * Returns the unit's Health Capacity.
	 * 
	 * @return the unit's Health Capacity.
	 */
	int getHealth() {
		return healthPoints;
	}

	/**
	 * Returns the unit's Mana Capacity.
	 * 
	 * @return the unit's Mana Capacity.
	 */
	int getMagPnt() {
		return magicPoints;
	}

	/**
	 * Returns the unit's Mana Regeneration Rate.
	 * 
	 * @return the unit's Mana Regeneration Rate.
	 */
	int getMagRgn() {
		return magicRegeneration;
	}

	/**
	 * Returns the unit's Energy Capacity.
	 * 
	 * @return the unit's Energy Capacity.
	 */
	int getEngPnt() {
		return energyPoints;
	}

	/**
	 * Returns the unit's Energy Regeneration Rate.
	 * 
	 * @return the unit's Energy Regeneration Rate.
	 */
	int getEngRgn() {
		return energyRegeneration;
	}

	/**
	 * Returns the unit's Attack Speed.
	 * 
	 * @return the unit's Attack Speed.
	 */
	int getAtkSpd() {
		return attackSpeed;
	}

	/**
	 * Returns the unit's Attack Per Turn.
	 * 
	 * @return the unit's Attacks Per Turn.
	 */
	int getNumAtk() {
		return numberOfAttacks;
	}

	/**
	 * Returns the unit's Sight Radius.
	 * 
	 * @return the unit's Sight Radius.
	 */
	int getSihtRd() {
		return sightRadius;
	}

	/**
	 * Returns the unit's Movement Speed.
	 * 
	 * @return the unit's Movement Speed.
	 */
	int getMovSpd() {
		return moveSpeed;
	}

	/**
	 * Returns the unit's Production Cost.
	 * 
	 * @return the unit's Production Cost.
	 */
	int getPrdCst() {
		return productionCost;
	}

	/**
	 * Returns the unit's Attack Range.
	 * 
	 * @return the unit's Attack Range.
	 */
	int getRanged() {
		return range;
	}

	/**
	 * Returns the unit's Size In Squares.
	 * 
	 * @return the unit's Size In Squares.
	 */
	int getSqrOcu() {
		return squaresOccupied;
	}

	/**
	 * Returns the unit's Levels.
	 * 
	 * @return the unit's Levels.
	 */
	int getLevels() {
		return level;
	}

	/**
	 * Returns the unit's Death Experience Reward.
	 * 
	 * @return the unit's Death Experience Reward.
	 */
	int getDthEXP() {
		return deathEXP;
	}

	/**
	 * Returns the unit's Owner.
	 * 
	 * @return the unit's Owner.
	 */
	Player getPlayer() {
		return player;
	}

	/**
	 * Returns the unit's current row.
	 * 
	 * @return the unit's current row.
	 */
	int getCurLocR() {
		return curLocR;
	}

	/**
	 * Returns the unit's current column.
	 * 
	 * @return the unit's current column.
	 */
	int getCurLocC() {
		return curLocC;
	}

	/**
	 * Returns the unit's Channel Time.
	 * 
	 * @return the unit's Channel Time.
	 */
	int getChnTim() {
		return channelTime;
	}

	/**
	 * Returns the number of turns the unit has been in the game.
	 * 
	 * @return the number of turns the unit has been in the game.
	 */
	int getTrnPld() {
		return turnsPlayed;
	}

	/**
	 * Indicates whether or not the unit is visible.
	 * 
	 * @return true if the unit is visible
	 */
	boolean isVisible() {
		return visible;
	}

	/**
	 * Indicates whether or not the unit is active.
	 * 
	 * @return true if the unit is active
	 */
	boolean isActive() {
		return active;
	}

	/**
	 * Indicates whether or not the unit is disabled.
	 * 
	 * @return true if the unit is disabled
	 */
	boolean isDisabled() {
		return disabled;
	}

	/**
	 * Sets the unit to be visible or invisible.
	 * 
	 * @param b - if true, set visible
	 */
	void setVisible(boolean b) {
		visible = b;
	}

	/**
	 * Sets the unit to be active or inactive.
	 * 
	 * @param b - if true, set active
	 */
	void setActive(boolean b) {
		active = b;
	}

	/**
	 * Sets the unit to be disabled or enabled.
	 * 
	 * @param b - if true, set disabled
	 */
	void setDisabled(boolean b) {
		disabled = b;
	}

	String getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	Locomotion getLocomotion() {
		return locomotion;
	}

	/**
	 * This method seeks to generate a series of <code>Activities</code> such that
	 * the unit will occupy the correct frames of the queue processing. For more info,
	 * visit the Activity Queue class.
	 * 
	 * @param path - the path the unit will try to take
	 */
	void generateMoveActivityWithPath(ArrayList<Point> path) {
//				System.out.println("In the generateMoveActivityWithPath method now.");
		// First, we find out how long we've got to travel.
		double squaresTraveled = path.size();
//				System.out.println("Squares Traveled: " + squaresTraveled);
		// Now we find out how fast we're going to get there.
		// This is added to each step along the path.
		double travelModifier = 1 - (squaresTraveled / moveSpeed); // TODO keep in mind rational numbers for future implementation since you may lose functionality to double prec
		// Maybe Math.fractions
//				System.out.println("Travel Modifier: " + travelModifier);
		// We'll keep another variable to track the stacks of the travel modifier.
		// For a step in which this rolling number is over 1, the unit takes a double step.
		double modSum = 0;
		// Activities expect to see ArrayList<Point> for the origin, target, and squares occupied
		ArrayList<Point> origin, target, squaresOccupied;
		origin = new ArrayList<Point>();
		target = new ArrayList<Point>();
		squaresOccupied = new ArrayList<Point>();
		origin.add(curLoc); // First origin always comes from the unit's current location
		for (Iterator<Point> i = path.iterator(); i.hasNext();) {
			Point step = i.next();
			modSum += travelModifier;
			if (modSum < 1) {
				// Since this is a normal step, we'll just move along to the next square.
//								System.out.println("\n= Taking a Normal Step =");
				target.add(step);
				squaresOccupied.add(step);
//								System.out.println("This is a move TO: < " + target.get(0).x + " , " + target.get(0).y + " >");
//								System.out.println("This move originates FROM: < " + origin.get(0).x + " , " + origin.get(0).y + " >");
//								System.out.println("Squares occupied during this move: < " + squaresOccupied.get(0).x + " , " + squaresOccupied.get(0).y + " >");
				this.addActivity(new Activity(player, this, commandCount++, ActivityType.MOVEMENT, 0, origin, target, squaresOccupied));
				origin = new ArrayList<Point>();
				origin.add(step);
				target = new ArrayList<Point>();
				squaresOccupied = new ArrayList<Point>();
			} else {
				// We're going to take a double step now and scale down the travelMultiplier
				modSum -= 1;
				if (i.hasNext()) {
//										System.out.println("\n== Taking a Double Step ==");
					squaresOccupied.add(step); // Thinking. Want to test for a bit on the other cases.
					step = i.next();
					target.add(step);
					squaresOccupied.add(step);
//										System.out.println("This is a move TO: < " + target.get(0).x + " , " + target.get(0).y + " >");
//										System.out.println("This move originates FROM: < " + origin.get(0).x + " , " + origin.get(0).y + " >");
//										System.out.println("Squares occupied during this move: < " + squaresOccupied.get(0).x + " , " + squaresOccupied.get(0).y + " >,"
//												+ " < " + squaresOccupied.get(1).x + " , " + squaresOccupied.get(1).y	+ " >");
					this.addActivity(new Activity(player, this, commandCount++, ActivityType.MOVEMENT, 0, origin, target, squaresOccupied));
					origin= new ArrayList<Point>();
					origin.add(step);
					target= new ArrayList<Point>();
					squaresOccupied = new ArrayList<Point>();
				} else { // Last step came before double step bonus
//										System.out.println("\n=== Out of Squares ===");
					target.add(step);
					squaresOccupied.add(step);
					this.addActivity(new Activity(player, this, commandCount++, ActivityType.MOVEMENT, 0, origin, target, squaresOccupied));
				}
			}
		}
//		System.out.println();
	}

	void setMovable(boolean isMovable) {
		canMove = isMovable;
	}
	
	boolean canMove() {
		return canMove;
	}
}
