package com.example.framework.model;
/**
 * Represents character's fighting skill
 * @author Grehov
 *
 */
public interface IFightingSkill {
	/**
	 * Calculates strike rate for target. The rate depends on:
	 * 		is fSkill ready
	 * 		is fSkill reaches the target by distance
	 * 		fSkillRate = curFSkill.fSkillRate;										   // 1 - 1.8
	 * 		fSkillRate *= curFSkill.rateByDistanceClass; 							   // 1 - 8
	 * 		fSkillRate *= getObstacleKoefToFSkill(curFSkill.type, target);			   // 0 - 1
	 * 		fSkillRate *= getFSkillOptimalRateForTarget(curFSkill, target, forceRate); // 0.01 - 1
	 * @param target
	 * @param targetForceRate
	 * @return
	 */
	float getStrikeRate(DynamicGameObject target, final float targetForceRate);
	
	/**
	 * Updates skill
	 * @param actTime
	 */
	void update(float actTime, float deltaTime);
	
	/**
	 * Starts actual striking
	 * @param actTime
	 */
	void strikeStart(float actTime, DynamicGameObject target,  boolean isBot);
	
	void setAttackLevel(int level);
	int getAttackLevel(int level);
	void setChargeLevel(int level);
	int getChargeLevel(int level);
	void setWorkDistance(float dist);
}
