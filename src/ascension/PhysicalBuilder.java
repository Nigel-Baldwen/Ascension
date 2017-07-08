package ascension;

import java.awt.Point;
import java.util.ArrayList;

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
	public PhysicalBuilder(int player, int locR, int locC) {
		locomotion = Locomotion.GROUND;
		intelligenceAttack = 3; intelligenceDefense = 6;
		soulInvestment = -1; soulAffinity = 3;
		symbiosis = -1; hoardingAptitude = 3;
		poison = -1; healthRegeneration = 3;
		kenisis = -1; calmingAura = 3;
		illusion = -1; disillusion = 4;
		holyAttack = -1; holyDefense = 5;
		darkAttack = -1; darkDefense = 4;
		dispel = -1; spellStrength = 4;
		summonStrength = -1; planarAffinity = 3;
		threat = -1; bravery = 3;
		earthAttack = -1; earthDefense = 3;
		windAttack = -1; windDefense = 3;
		fireAttack = -1; fireDefense = 4;
		waterAttack = -1; waterDefense = 5;
		deathBlow = -1; nullification = 4;
		unarmed = -1; strength = 3;
		agilityAttack = -1; agilityDefense = 3;
		charge = -1; stability = 3;
		bluntAttack = -1; paddingDefense = 3;
		bladeAttack = -1; shellDefense = 4;
		pierceAttack = 6; reinforcementDefense = 5;
		damageMelee = 3; 
		damageRanged = -1;
		damageAbilityOne = 0;
		damageAbilityTwo = 0;
		damageAbilityThree = 0;
		damageAbilityFour = 0;
		healthPoints = 12;
		magicPoints = 6;
		magicRegeneration = 3;
		energyPoints = -1;
		energyRegeneration = -1;
		attackSpeed = 0;
		numberOfAttacks = 4;
		sightRadius = 6;
		moveSpeed = 12;
		productionCost = 4;
		range = -1;
		squaresOccupied = 1;
		level = 0;
		deathEXP = 6;
		this.player = player;
		curLocR = locR;
		curLocC = locC;
		curLoc = new Point(locR, locC);
		channelTime = 0;
		turnsPlayed = 0;
		idValue = 0;
		commandCount = 0;
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
		String descriptor = healthPoints + ":" + magicPoints + ":" + energyPoints + ":" + 
				moveSpeed + ":" + sightRadius + ":" + numberOfAttacks + ":" + attackSpeed + ":" + 
				range + ":" + squaresOccupied + ":" + deathEXP + ":" + level + ":" + 
				// X coordinate for current location.
				curLocR + ":" + 
				player + ":" +
				productionCost + ":" + turnsPlayed + ":" + 
				// Y coordinate for current location.
				curLocC + ":" +
				intelligenceAttack + ":" + intelligenceDefense + ":" + soulInvestment + ":" + soulAffinity + ":" + 
				symbiosis + ":" + hoardingAptitude + ":" + poison + ":" + healthRegeneration + ":" + 
				kenisis + ":" + calmingAura + ":" + illusion + ":" + disillusion + ":" + 
				holyAttack + ":" + holyDefense + ":" + darkAttack + ":" + darkDefense + ":" + 
				dispel + ":" + spellStrength + ":" + summonStrength + ":" + planarAffinity + ":" + 
				threat + ":" + bravery + ":" + deathBlow + ":" + nullification + ":" +
				earthAttack + ":" + earthDefense + ":" + windAttack + ":" + windDefense + ":" + 
				fireAttack + ":" + fireDefense + ":" + waterAttack + ":" + waterDefense + ":" + 
				charge + ":" + stability + ":" + agilityAttack + ":" + agilityDefense + ":" + 
				unarmed + ":" + strength + ":" + bluntAttack + ":" + paddingDefense + ":" + 
				bladeAttack + ":" + shellDefense + ":" + pierceAttack + ":" + reinforcementDefense;
		return descriptor;
	}
}
