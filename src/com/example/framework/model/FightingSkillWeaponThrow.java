package com.example.framework.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightingSkillWeaponThrow extends FightingSkillBaseWeaponSkills {

	FightingSkillWeaponThrow(DynamicGameObject master) {
		super(Statics.FightingSkills.WEAPON_THROW, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void strike(float actTime) {	 			 		
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if (isReady())processSensors(getThrowingWeapon());
	}
	
	boolean isReady() {
		int weaponCounter = 0;		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM)) {
			FightingSkill leftArm =  master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM);
			if (leftArm.isReady) {
				if (leftArm.weapon != null)
					++weaponCounter;
				else 
					leftArm.isReady = false;
			}			
		}		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM)) {
			FightingSkill rightArm =  master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM);
			if (rightArm.isReady) {
				if (rightArm.weapon != null)
					++weaponCounter;
				else 
					rightArm.isReady = false;
			}			
		}	
		if (weaponCounter > 1)
			return true;
		else
			return false;
	}
	
	void processSensors(DynamicGameObject weapon) {
		if (weapon == null) return;
		float currentDistance = 0;
		float firstDistance = 0;
		int objAbstractType = 0;				
		float penetrationDistance = 100;	
		float weaponContactWidth = weapon.weaponPenetrationWidth;
		int weaponContactMaterial = Statics.PhysicsBox2D.getWeaponStrikeMaterial(weapon.objType);
		int weaponLevel = weapon.stateHS.level;
		float weaponImpulse = Statics.PhysicsBox2D.WEAPON_ARM_THROW_IMPULSE_START + 
				attackPower * Statics.PhysicsBox2D.WEAPON_ARM_THROW_IMPULSE_STEP;
		
		int i = 0;
		
		for (Entry<Float, Object> item : master.world.world2d.getRaySensorFixturesArray()) {
			int result = 0;
			Fixture fixture = (Fixture)item.value;
			GameObject gObj = (GameObject) fixture.getBody().getUserData();
			currentDistance = item.key * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RAY_RADIUS;
			
			if(currentDistance > workDistance)
				break;
			else if(i == 0) {
				objAbstractType = defineObstacleType(master, gObj);
				firstDistance = currentDistance;
				i++;
			} else {
				currentDistance = currentDistance - firstDistance;				
				if(currentDistance > penetrationDistance)
					break;
				else
					objAbstractType = defineObstacleType(master, gObj);
			}
			
			switch(objAbstractType)
			{
				case 0:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE:
					penetrationDistance = master.world.world2d.getPenetrationDistance(weaponImpulse, weaponContactWidth,
							weaponContactMaterial, weaponLevel, fixture) - currentDistance;
					if (penetrationDistance > 0) result = 1;
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY:
					if (currentDistance + firstDistance >= Statics.FightingSkills.SENSOR_WEAPON_THROW_START_DISTANCE)
						tryThrowWeapon();
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND:
					break;	
				default:
					result = 1;				
			}		
			if(result == 0)
				break;
		}		
	}
	
	public void tryThrowWeapon() {
		FightingSkillBaseWeaponSkills arm = getArmThrowingWeapon();		
		if(arm != null && arm.weapon != null)
			master.world.world2d.tryThrowWeapon(master, arm);
	}
	
	FightingSkillBaseWeaponSkills getArmThrowingWeapon() {
		FightingSkillBaseWeaponSkills leftArm = master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM) ?
				(FightingSkillBaseWeaponSkills) master.fSkills.getFSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM) : null;	
				
		FightingSkillBaseWeaponSkills rightArm = master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM) ?
				(FightingSkillBaseWeaponSkills) master.fSkills.getFSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM) : null;		

		if (leftArm != null && leftArm.isReady && Statics.DynamicGameObject.getWeaponClass(leftArm.weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			return leftArm;
		else if (rightArm != null && rightArm.isReady && Statics.DynamicGameObject.getWeaponClass(rightArm.weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			return rightArm;
		else if (leftArm != null && leftArm.isReady )
			return leftArm;
		else
			return rightArm;			
	}
	
	DynamicGameObject getThrowingWeapon() {
		DynamicGameObject weaponLeft = null;
		DynamicGameObject weaponRight = null;
		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM) && master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM).isReady)
			weaponLeft  = master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM).weapon;
		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM) && master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM).isReady)
			weaponRight  = master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM).weapon;
			
		if (weaponLeft != null && Statics.DynamicGameObject.getWeaponClass(weaponLeft.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			return weaponLeft;
		else if (weaponRight != null && Statics.DynamicGameObject.getWeaponClass(weaponRight.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			return weaponRight;
		else if (weaponLeft != null)
			return weaponLeft;
		else
			return weaponRight;			
	}
}
