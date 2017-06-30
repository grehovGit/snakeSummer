package com.example.framework.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.kingsnake.physicsBox2d.PhysicsBox2d;

public class FightingSkillFireAttackForward extends FightingSkillBaseMagicSkills {

	FightingSkillFireAttackForward(DynamicGameObject master) {
		super(Statics.FightingSkills.FIRE_ATTACK_FORWARD, master);
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
		 			 		
		if(dTime >= 0 && dTime <= master.stateHS.strikePeriod) {
			if (!master.stateHS.striked) {
				float lifeTimePeriod = workDistance / Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD_VELOCITY;
				DynamicGameObject localMaster = master.objType == Statics.DynamicGameObject.SNAKE ?	((Snake)master).parts.get(0) : master;
				
				DynamicEffectFire dynEffect = new DynamicEffectFire(localMaster.position.x, localMaster.position.y, 1f, 1f, targetPos.sub(master.position.x, master.position.y).angle(), 
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
		master.stateHS.strikeType = Statics.Render.STRIKE_FIRE;				
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if(isReady) processSensors();		
	}
	
	void processSensors() {
		int objAbstractType = 0;		
		float currentDistance = 0;
		
		for (Entry<Float, Object> item : master.world.world2d.getRaySensorFixturesArray())
		{
			int result = 0;
			Fixture fixture = (Fixture)item.value;
			GameObject gObj = (GameObject) fixture.getBody().getUserData();
			currentDistance = item.key * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RAY_RADIUS;
			
			if(currentDistance > workDistance)
				break;
			else
				objAbstractType = defineObstacleType(master, gObj, fixture);
			
			switch(objAbstractType) {
				case 0:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE:
					result = 1;
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE:
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY:
					master.fSkills.addTarget(Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_FIRE, gObj);
					break;
				case Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND:
					break;	
				default:
					result = 1;				
			}
			
			if(result == 0)
				break;
		}	
	}
	
	public int defineObstacleType(DynamicGameObject dynObj, GameObject gObj, Fixture fixture) {
		if(!gObj.isDynamicObject)
			return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE;
		else {
			DynamicGameObject dObj = (DynamicGameObject) gObj;		
			if (dObj.isCharacter) {
				if (master.world.wProc.isCharFriend(dynObj , dObj))
					return Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND;
				else
					return Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY;				
			} else {
				int material = (int) ((float[])fixture.getUserData())[PhysicsBox2d.FIXTURE_PROPERTY2_INDEX_MATERIAL];
				if(PhysicsBox2d.isInflammableMaterial(material))
					return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE;
				else
					return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE;
			}
		}
	}
}
