package com.example.framework.model;

public class FightingSkillWeaponThrowSpear extends FightingSkillWeaponThrow {

	FightingSkillWeaponThrowSpear(DynamicGameObject master) {
		super(master);
		this.type = Statics.FightingSkills.WEAPON_THROW_SPEAR;
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	boolean isReady() {		
		boolean ready = false;		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM)) {
			FightingSkill leftArm = master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM);
			if (leftArm.isReady) {
				if (leftArm.weapon != null) {
					if (Statics.DynamicGameObject.getWeaponClass(leftArm.weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
						ready = true;							
				}
				else 
					leftArm.isReady = false;
			}
		}		
		if (master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM)) {
			FightingSkill rightArm = master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM);
			if (rightArm.isReady) {
				if (rightArm.weapon != null) {
					if (Statics.DynamicGameObject.getWeaponClass(rightArm.weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
						ready = true;							
				}
				else 
					rightArm.isReady = false;
			}
		}
		return ready;
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
		else
			return null;			
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
		else
			return null;			
	}
}
