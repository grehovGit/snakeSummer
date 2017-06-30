package com.example.framework.model;

public class FightingSkillWeaponHoldRightArm extends FightingSkillBaseWeaponSkills {

	FightingSkillWeaponHoldRightArm(DynamicGameObject master) {
		super(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		return 0;
	}

	@Override
	public void strike(float actTime) {	 			 		
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if (weapon == null || weapon.myBody == null) {
			isReady = false;			
			if (!isJustReleasedWeapon) {
				if(master.world.actTime - startTime > Statics.FightingSkills.WEAPON_HOLD_ARM_NO_ACT_PERIOD_AFTER_WEAPON_RELEASE)
					isJustReleasedWeapon = true;
			}
		} else if (Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SWORD
				|| Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_AXE)
			master.world.world2d.processFencing(master, weapon, targetPos, deltaTime, this, Statics.FightingSkills.WEAPON_HOLD_ARM_FENCING_TYPE_CHOP);
		else if (Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			master.world.world2d.processFencing(master, weapon, targetPos, deltaTime, this, Statics.FightingSkills.WEAPON_HOLD_ARM_FENCING_TYPE_STAB);
	}
}
