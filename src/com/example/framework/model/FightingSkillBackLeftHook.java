package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public class FightingSkillBackLeftHook extends FightingSkillBaseCicks {

	FightingSkillBackLeftHook(DynamicGameObject master) {
		super(Statics.FightingSkills.BACK_LEFT_HOOK, master);
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	void strike(float actTime) {
	 	float dTime = actTime - master.stateHS.strikeStartTime;
	 	
		if(master.objType == Statics.DynamicGameObject.SNAKE) {
			Snake snake = (Snake) master;
		 	int size = snake.parts.size();
		 	renderSnakeWave(dTime);	
		 	if(dTime > size *  Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD / 3) {
		 		master.world.world2d.snakeForwardHook(snake.parts.get(size-1), startPos, targetPos, true, maxPath, false);		 		

		 		if(!master.stateHS.striked) {
			 		master.world.world2d.snakeForwardHookImpulse(snake.parts.get(size-1), targetPos, true, maxPath);
			 		master.stateHS.striked = true;
		 		}
		 	}
		} else {		
		 	if(dTime > 0 && dTime <= master.stateHS.strikePeriod)
			 	master.stateHS.isStriking = true;
		 	else if (dTime > master.stateHS.strikePeriod)	
		 		 strikeFinish();
		 	
		 	if(dTime > 0) {
		 		master.world.world2d.forwardHook(master, startPos, targetPos, true, maxPath);		 		
		 		if(!master.stateHS.striked) {
			 		master.world.world2d.forwardHookImpulse(master, targetPos, true, maxPath);
			 		master.stateHS.striked = true;
		 		}
		 	}		 	
		}	 	 		
	}

	@Override
	public void strikeStart(float actTime, DynamicGameObject target,  boolean isBot) {
		super.strikeStart(actTime, target, isBot);
		if(master.objType == Statics.DynamicGameObject.SNAKE) {
			SnakePart sTale = ((Snake)master).parts.get(((Snake)master).parts.size() - 1);
			startPos.set(sTale.position.x, sTale.position.y);
		}		
	 	curPath = (float) (Math.PI * Vector2.dst(startPos.x, startPos.y, targetPos.x, targetPos.y) / 2);
	 	maxPath = 2 * (float) (Math.PI * (Statics.FightingSkills.SENSOR_SIDE_SENS_START_DISTANCE + 
	 			Statics.FightingSkills.SENSOR_SIDE_SENS_STEP_DISTANCE * attackPower) / 2);
	 	
	 	float forcePeriod = Statics.FightingSkills.FORWARD_TURN_PERIOD * curPath / maxPath;
	 	strikeInit(forcePeriod);
	}

	@Override
	void setAvatarTargetPoint() {
		super.setAvatarTargetPoint();
		targetPos.rotate(90);
		targetPos.add(startPos);		
	}

}
