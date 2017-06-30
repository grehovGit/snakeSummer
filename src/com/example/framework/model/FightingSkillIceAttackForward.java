package com.example.framework.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightingSkillIceAttackForward extends FightingSkillBaseMagicSkills {

	FightingSkillIceAttackForward(DynamicGameObject master) {
		super(Statics.FightingSkills.ICE_ATTACK_FORWARD, master);
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
				float lifeTimePeriod = workDistance / Statics.FightingSkills.ICE_ATTACK_FORWARD_VELOCITY;
				DynamicGameObject localMaster = master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0) : master;
				
				DynamicEffect dynEffect = new DynamicEffect(localMaster.position.x, localMaster.position.y, 1f, 1f, targetPos.sub(master.position.x, master.position.y).angle(), 
								lifeTimePeriod, Statics.DynamicGameObject.FSKILL_FORWARD_ATTACK_ICE_EFFECT, master);
				dynEffect.setDataForwardAttackPower(attackPower);				
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
		master.stateHS.strikeType = Statics.Render.STRIKE_ICE;
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
		float workIceballRadius = Statics.FightingSkills.FIGHTSKILL_ICE_FORWARD_START_RADIUS 
				+ attackPower * Statics.FightingSkills.FIGHTSKILL_ICE_FORWARD_STEP_RADIUS;
		
		for (Entry<Float, Object> item : master.world.world2d.getRaySensorFixturesArray()) {
			int result = 0;
			Fixture fixture = (Fixture)item.value;
			GameObject gObj = (GameObject) fixture.getBody().getUserData();
			currentDistance = item.key * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RAY_RADIUS;
			
			if (currentDistance > workDistance)
				break;
			else if(i == 0) {
				objAbstractType = defineObstacleType(master, gObj);
				firstDistance = currentDistance;
			} else {			
				if (currentDistance - firstDistance > workIceballRadius)
					break;
				else
					objAbstractType = defineObstacleType(master, gObj);
			}
			
			switch (objAbstractType) {
				case 0:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE:
					result = 1;
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY:
					if(currentDistance > Statics.FightingSkills.SENSOR_ICE_MININIMAL_WORK_DISTANCE)
						master.fSkills.addTarget(Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_ICE, gObj);
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND:
					break;	
				default:
					result = 1;				
			}			
			if(result == 0)
				break;
			++i;
		}	
	}
}
