package com.example.framework.model;

public class FightingSkillWeaponHoldHelm extends FightingSkillBaseWeaponSkills {

	FightingSkillWeaponHoldHelm(DynamicGameObject master) {
		super(Statics.FightingSkills.WEAPON_HOLD_HELM, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		return 0;
	}

	@Override
	public void strike(float actTime) {	 			 		
	}
}
