package com.example.framework.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightingSkillImpulseAttackForward extends FightingSkillBaseMagicSkills {

	FightingSkillImpulseAttackForward(DynamicGameObject master) {
		super(Statics.FightingSkills.IMPULSE_ATTACK_FORWARD, master);
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
			if(!master.stateHS.striked) {
				float lifeTimePeriod = workDistance / Statics.FightingSkills.IMPULSE_ATTACK_FORWARD_VELOCITY;
				DynamicGameObject localMaster = master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0) : master;				
				DynamicEffect dynEffect = new DynamicEffect(localMaster.position.x, localMaster.position.y, 1f, 1f, targetPos.sub(master.position.x, master.position.y).angle(), 
								lifeTimePeriod, Statics.DynamicGameObject.FSKILL_FORWARD_ATTACK_IMPULSE_EFFECT, master);
				
				dynEffect.setDataForwardAttackImpulse(workDistance / Statics.FightingSkills.SENSOR_IMPULSE_FORWARD_ATTACK_KOEF_TO_FORWARD_DEFENSE_SENSE,
						Statics.FightingSkills.FIGHTSKILL_IMPULSE_POWER_STEP * attackPower,
						master.stateHS.attackPower /*TEMPORARY*/);				
				master.world.dynEffectsAboveObjects.add(dynEffect);	
				master.stateHS.striked = true;
			}
		} else if (dTime > master.stateHS.strikePeriod)	{
			if(master.objType == Statics.DynamicGameObject.SNAKE)
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
		 master.stateHS.strikeType = Statics.Render.STRIKE_IMPULSE;
		 master.stateHS.strikePeriod = Statics.Render.SNAKE_BLUE_STRIKE_PERIOD;				
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if(isReady) processSensors();		
	}
	
	void processSensors() {
		int objAbstractType = 0;	
		int i = 0;
		float currentDistance = 0;
		float firstDistance = 0;
		float workRadius = workDistance / Statics.FightingSkills.SENSOR_IMPULSE_FORWARD_ATTACK_KOEF_TO_FORWARD_DEFENSE_SENSE;
		
		for (Entry<Float, Object> item : master.world.world2d.getRaySensorFixturesArray()) {
			int result = 0;
			Fixture fixture = (Fixture)item.value;
			GameObject gObj = (GameObject) fixture.getBody().getUserData();
			currentDistance = item.key * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RAY_RADIUS;
			
			if(currentDistance > workDistance)
				break;
			else if(i == 0) {
				objAbstractType = defineObstacleType(master, gObj);
				firstDistance = currentDistance;
			} else {
				currentDistance = currentDistance - firstDistance;				
				if(currentDistance > workRadius)
					break;
				else
					objAbstractType = defineObstacleType(master, gObj);
			}
			
			switch(objAbstractType) {
				case 0:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE:
					result = 1;
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY:
					master.fSkills.addTarget(Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_IMPULSE, gObj);
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND:
					break;	
				default:
					result = 1;				
			}	
			if(result == 0)
				break;
			i++;
		}	
	}

}
