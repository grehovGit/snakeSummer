package com.example.framework.model;

public class FightingSkillWeaponThrowShield extends FightingSkillBaseWeaponSkills {

	FightingSkillWeaponThrowShield(DynamicGameObject master) {
		super(Statics.FightingSkills.WEAPON_THROW_SHIELD, master);
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
		throwShieldIfBroken();
	}
	
	void throwShieldIfBroken() {
		if(master.fSkills.haveSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD)) {
			FightingSkill fSkillHoldShield = master.fSkills.getSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD);
			
			if (fSkillHoldShield.weapon == null || fSkillHoldShield.weapon.myBody == null) {
				fSkillHoldShield.isReady = false;
				isReady = false;
			} else {
				if (!master.world.world2d.isBodyComplete(fSkillHoldShield.weapon)) {
					if (targetPos != null) {
						weapon = fSkillHoldShield.weapon;
						fSkillHoldShield.releaseWeapon();
						master.world.world2d.tryThrowShield(master, this);
					}
				}
			}
		}
	}
}
