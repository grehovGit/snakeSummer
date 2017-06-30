package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public class FightingSkillImpulseDeffenseForward extends FightingSkillBaseMagicSkills {

	FightingSkillImpulseDeffenseForward(DynamicGameObject master) {
		super(Statics.FightingSkills.IMPULSE_DEFFENSE_FORWARD, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void strike(float actTime) {
	 	float dTime = actTime - master.stateHS.strikeStartTime;
	 	master.stateHS.strikeTime = dTime;
	 	
	 	if(master.objType == Statics.DynamicGameObject.SNAKE)
	 		((Snake)master).parts.get(0).stateHS.isStriking = true;
		 			 		
		if(dTime >= 0 && dTime <= master.stateHS.strikePeriod) {
			if (isStartStriking) {
				Vector2 offset = new Vector2(workDistance / 2, 0);
				offset.setAngle(master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0).angle : master.angle);
				
				master.world.wProc.createStaticEffect (master.position.x + offset.x, master.position.y + offset.y, 2 * workDistance, 2 * workDistance, 
						master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0).angle : master.angle,
						Statics.StaticEffect.IMPACT, Statics.Render.SPECEFFECT_IMPACT_PERIOD, 0, null);
				isStartStriking = false;
			}
		} else if (dTime > master.stateHS.strikePeriod)	{
			if(master.objType == Statics.DynamicGameObject.SNAKE)
		 		((Snake)master).parts.get(0).stateHS.isStriking = false;			
			strikeFinish();		
		}
		 	
		if(dTime > Statics.Render.SPECEFFECT_IMPACT_PERIOD / 4) {		 		
			if(!master.stateHS.striked) {
				master.world.world2d.fSkillImpulse(Statics.FightingSkills.FIGHTSKILL_IMPULSE_POWER_STEP * attackPower,
				 	master.stateHS.attackPower /*TEMPORARY*/, workDistance, master.objType == Statics.DynamicGameObject.SNAKE ?
				 			((Snake)master).parts.get(0) : master);
				master.stateHS.striked = true;
			}
		} 			 		
	}

	@Override
	public void strikeStart(float actTime, DynamicGameObject target,  boolean isBot) {
		 super.strikeStart(actTime, target, isBot);
		 master.stateHS.strikeType = Statics.Render.STRIKE_IMPULSE;
		 master.stateHS.strikePeriod = Statics.Render.SNAKE_BLUE_STRIKE_PERIOD;				
	}
}
