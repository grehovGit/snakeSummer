package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public class FightingSkillImpulseDeffenseLeftSide extends FightingSkillBaseMagicSkills {

	FightingSkillImpulseDeffenseLeftSide(DynamicGameObject master) {
		super(Statics.FightingSkills.IMPULSE_DEFFENSE_LEFTSIDE, master);
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
	 	if(master.objType == Statics.DynamicGameObject.SNAKE) {		 	
			Snake snake = (Snake) master;
		 	int size = snake.parts.size();		 	
		 	for(int i = 1; i < size - 1; ++i) {
		 		SnakePart sPart = snake.parts.get(i);		 			 		
				if(dTime >= 0 && dTime <= master.stateHS.strikePeriod) {
					if (!sPart.stateHS.striked) {
						Vector2 offset = new Vector2(workDistance / 3, 0);
						offset.setAngle(sPart.angle + 90);
						
						master.world.wProc.createStaticEffect(sPart.position.x + offset.x, sPart.position.y + offset.y,
								2 * workDistance, 2 * workDistance, sPart.angle + 90, Statics.StaticEffect.IMPACT,
										Statics.Render.SPECEFFECT_IMPACT_PERIOD, 0, null);
						
						master.world.world2d.fSkillSideImpulse(Statics.FightingSkills.FIGHTSKILL_IMPULSE_POWER_STEP * attackPower,
							 	master.stateHS.attackPower /*TEMPORARY*/, workDistance, sPart, true);
						
					 	sPart.stateHS.isStriking = true;
					 	sPart.stateHS.strikeTime = dTime;
					 	sPart.stateHS.striked = true;
					}
				} else if (dTime > master.stateHS.strikePeriod)	{
					sPart.stateHS.isStriking = false;					
					strikeFinish();		
				}		
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
