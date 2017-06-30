package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public class FightingSkillBackAttack extends FightingSkillBaseCicks {

	FightingSkillBackAttack(DynamicGameObject master) {
		super(Statics.FightingSkills.BACK_ATTACK, master);
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
		 		master.world.world2d.snakeForwardJump(snake.parts.get(size-1), targetPos, false);		 		
		 		if(!master.stateHS.striked) {
			 		master.world.world2d.snakeForwardJumpImpulse(snake.parts.get(size-1), targetPos);
			 		master.stateHS.striked = true;
		 		}
		 	}
		} else {		
		 	if(dTime > 0 && dTime <= master.stateHS.strikePeriod)
			 	master.stateHS.isStriking = true;
		 	else if (dTime > master.stateHS.strikePeriod)	
		 		 strikeFinish();
		 	
		 	if(dTime > 0) {
		 		master.world.world2d.forwardJump(master, targetPos); 		
		 		if(!master.stateHS.striked) {
			 		master.world.world2d.forwardJumpImpulse(master, targetPos);
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
	 	curPath = Vector2.dst(startPos.x, startPos.y, targetPos.x, targetPos.y);
	 	maxPath =  (Statics.FightingSkills.SENSOR_BACK_SENS_START_DISTANCE + 
	 			Statics.FightingSkills.SENSOR_BACK_SENS_STEP_DISTANCE * attackPower);		 	
	 	
	 	float forcePeriod = Statics.FightingSkills.FORWARD_ATTACK_PERIOD * curPath / maxPath;		
	 	strikeInit(forcePeriod);
	}

	@Override
	void setAvatarTargetPoint() {
		super.setAvatarTargetPoint();
		SnakePart sTale = ((Snake)master).parts.get(((Snake)master).parts.size() - 1);
		startPos.set(sTale.position.x, sTale.position.y);
		targetPos.set(workDistance, 0);
		targetPos.setAngle(sTale.angle);
		targetPos.rotate(180);
		targetPos.add(startPos);		
	}
}
