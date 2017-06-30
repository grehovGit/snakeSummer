package com.example.framework.model;

public abstract class FightingSkillBaseMagicSkills extends FightingSkillBase{
	
	FightingSkillBaseMagicSkills(String type, DynamicGameObject master) {
		super(type, master);
	} 
	
	@Override
	public
	void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		if(!isReady && Statics.FightingSkills.CHARGING_DEFAULT_PERIOD * attackPower / chargePower < actTime - startTime)
			isReady = true;				
	}
	
	void strikeFinish() {
		IS_STRIKING = isStriking = false;
		master.stateHS.isStriking = false;
		master.stateHS.strikeStartTime = 0;
		targetPos.set(0, 0);
		master.fSkills.startRelaxing();
	}
	
	void setAvatarTargetPoint() {
		targetPos.set(workDistance, 0);
		targetPos.setAngle(master.angle);
	}
	
	void renderSnake(float dTime) {
		//Snake snake = (Snake) master;
	}
	
	public int defineObstacleType(DynamicGameObject dynObj, GameObject gObj) {
		if(!gObj.isDynamicObject)
			return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE;
		else {
			DynamicGameObject dObj = (DynamicGameObject) gObj;		
			if(dObj.isCharacter) {
				if(master.world.wProc.isCharFriend(dynObj , dObj))
					return Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND;
				else
					return Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY;				
			}
			else
				return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE;
		}
	}
}
