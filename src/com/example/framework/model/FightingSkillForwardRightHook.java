package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public class FightingSkillForwardRightHook extends FightingSkillBaseCicks {

	FightingSkillForwardRightHook(DynamicGameObject master) {
		super(Statics.FightingSkills.FORWARD_RIGHT_HOOK, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void strike(float actTime) {
	 	float dTime = actTime - master.stateHS.strikeStartTime;
	 	
		if(master.objType == Statics.DynamicGameObject.SNAKE) {
			Snake snake = (Snake) master;
		 	int size = snake.parts.size();
		 	renderSnakeWave(dTime);	
		 	if(dTime > size *  Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD / 3) {
		 		master.world.world2d.snakeForwardHook(snake.parts.get(0), startPos, targetPos, false, maxPath, true);		 		

		 		if(!master.stateHS.striked) {
			 		master.world.world2d.snakeForwardHookImpulse(snake.parts.get(0), targetPos, false, maxPath);
			 		master.stateHS.striked = true;
		 		}
		 	}
		} else {		
		 	if(dTime > 0 && dTime <= master.stateHS.strikePeriod)
			 	master.stateHS.isStriking = true;
		 	else if (dTime > master.stateHS.strikePeriod)	
		 		 strikeFinish();
		 	
		 	if(dTime > 0) {
		 		master.world.world2d.forwardHook(master, startPos, targetPos, false, maxPath);		 		
		 		if(!master.stateHS.striked) {
			 		master.world.world2d.forwardHookImpulse(master, targetPos, false, maxPath);
			 		master.stateHS.striked = true;
		 		}
		 	}		 	
		}	 	 		
	}

	@Override
	public void strikeStart(float actTime, DynamicGameObject target,  boolean isBot) {
		super.strikeStart(actTime, target, isBot);
	 	curPath = (float) (Math.PI * Vector2.dst(startPos.x, startPos.y, targetPos.x, targetPos.y) / 2);
	 	maxPath = (float) (Math.PI * (Statics.FightingSkills.SENSOR_FORWARD_SENS_START_DISTANCE + 
	 			Statics.FightingSkills.SENSOR_FORWARD_SENS_STEP_DISTANCE * attackPower) / 2);		 	

	 	float forcePeriod = Statics.FightingSkills.FORWARD_HOOK_PERIOD * curPath / maxPath;
	 	strikeInit(forcePeriod);
	}

	@Override
	void setAvatarTargetPoint() {
		super.setAvatarTargetPoint();
		targetPos.add(startPos);		
	}

}
