package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;

public abstract class FightingSkillBaseWeaponSkills extends FightingSkillBase {
	protected DynamicGameObject weapon;
	protected boolean isJustReleasedWeapon;
	
	FightingSkillBaseWeaponSkills(String type, DynamicGameObject master) {
		super(type, master);
	} 
	
	@Override
	public void update(float actTime, float deltaTime) {			
		super.update(actTime, deltaTime);	
	}
	
	public float getPower() {
		return attackPower;
	}
	
	public Vector2 getTargetPos() {
		return targetPos;
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
	
	public DynamicGameObject getWeapon() {
		return weapon;
	}
	
	public void releaseWeapon()	{
		weapon = null;
		isReady = false;
		startTime = master.world.actTime;
		isJustReleasedWeapon = false;					
	}
}
