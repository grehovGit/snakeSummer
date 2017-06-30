package com.example.framework.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightingSkillHypnoseAttackForward extends FightingSkillBaseMagicSkills {

	FightingSkillHypnoseAttackForward(DynamicGameObject master) {
		super(Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD, master);
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
	 	
	 	if (master.objType == Statics.DynamicGameObject.SNAKE)
	 		((Snake)master).parts.get(0).stateHS.isStriking = true;
	 	
		if (dTime >= 0 && dTime <= master.stateHS.strikePeriod) {
			if (!master.stateHS.striked) {
				float lifeTimePeriod = workDistance / Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD_VELOCITY;
				DynamicGameObject localMaster = master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0) : master;
				
				DynamicEffectHypnose dynEffect = new DynamicEffectHypnose(localMaster.position.x, localMaster.position.y, 1f, 1f, targetPos.sub(master.position.x, master.position.y).angle(), 
								lifeTimePeriod, attackPower, master);				
				master.world.dynEffectsAboveObjects.add(dynEffect);	
				master.stateHS.striked = true;
			}
		} else if (dTime > master.stateHS.strikePeriod)	{
			if (master.objType == Statics.DynamicGameObject.SNAKE)
		 		((Snake)master).parts.get(0).stateHS.isStriking = false;			
			strikeFinish();		
		}		 	 			 		
	}

	@Override
	void setAvatarTargetPoint() {
		super.setAvatarTargetPoint();
		targetPos.add(startPos);
	}

	@Override
	public void strikeStart(float actTime, DynamicGameObject target,  boolean isBot) {
		super.strikeStart(actTime, target, isBot);
		 master.stateHS.strikeType = Statics.Render.STRIKE_HYPNOSE;
		 master.stateHS.strikePeriod = Statics.Render.SNAKE_BLUE_STRIKE_PERIOD;					
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if(isReady) processSensors();		
	}
	
	void processSensors() {
		float currentDistance = 0;		
		for (Entry<Float, Object> item : master.world.world2d.getRaySensorFixturesArray()) {
			Fixture fixture = (Fixture)item.value;
			GameObject gObj = (GameObject) fixture.getBody().getUserData();
			currentDistance = item.key * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RAY_RADIUS;
			
			if (currentDistance > workDistance)
				break;
			else if (defineObstacleType(master, gObj) == Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY)
					master.fSkills.addTarget(Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_HYPNOSE, gObj);
		}	
	}

}
