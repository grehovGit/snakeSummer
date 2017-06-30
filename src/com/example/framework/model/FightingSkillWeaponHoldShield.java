package com.example.framework.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightingSkillWeaponHoldShield extends FightingSkillBaseWeaponSkills {
	
	//targets in sensors
	private int forward;
	private int left;
	private int right;
	private boolean isModyfied;	

	FightingSkillWeaponHoldShield(DynamicGameObject master) {
		super(Statics.FightingSkills.WEAPON_HOLD_SHIELD, master);		
	}

	@Override
	public float getStrikeRate(DynamicGameObject target, float targetForceRate) {
		return 0;
	}

	@Override
	public void strike(float actTime) { 			 		
	}
	
	@Override
	public void update(float actTime, float deltaTime) {
		super.update(actTime, deltaTime);
		processSensors();
		if(isReady) updateShieldPos();	
	}
	
	void processSensors() {
		int forward = 0;				
		int left = 0;					
		int right = 0;					
		String sensor = null;
		GameObject  target = null;
		Vector2 sensorCenterPos = new Vector2();
		
		for (Entry<String, GameObject> targetEntry : master.fSkills.fTargets) {
			sensor = targetEntry.key;
			target = targetEntry.value; 
			
			if (sensor == Statics.FightingSkills.SENSOR_FORWARD) {
				sensorCenterPos.set(Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_CENTER_POS_X, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_CENTER_POS_Y);
				forward = setSensorState(forward, target, sensorCenterPos, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_FORWARD_RADIUS);
			} else if (sensor == Statics.FightingSkills.SENSOR_LEFT_SIDE) {
				sensorCenterPos.set(Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_LEFT_CENTER_POS_X, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_LEFT_CENTER_POS_Y);
				left = setSensorState(left, target, sensorCenterPos, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_SIDE_RADIUS);
			} else if (sensor == Statics.FightingSkills.SENSOR_RIGHT_SIDE) {
				sensorCenterPos.set(Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_RIGHT_CENTER_POS_X, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_RIGHT_CENTER_POS_Y);
				right = setSensorState(right, target, sensorCenterPos, Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_SIDE_RADIUS);
			}
		}		
		
		if ((forward == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS ? forward - 1 : forward) != this.forward) {
			if (forward == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS) {
				this.forward = forward - 1;				
				isModyfied = true;				
			} else if (forward == 0) {
				this.forward = forward;				
				isModyfied = true;
			}
		}

		if ((left == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS ? left - 1 : left) != this.left) {
			if (left == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS)	{
				this.left = left - 1;				
				isModyfied = true;				
			} else if (left == 0) {
				this.left = left;				
				isModyfied = true;
			}
		}
		
		if ((right == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS ? right - 1 : right) != this.right) {
			if (right == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS)	{
				this.right = right - 1;				
				isModyfied = true;				
			} else if (right == 0) {
				this.right = right;				
				isModyfied = true;
			}
		}	
	}
	
	int setSensorState (int sensorState, GameObject gObj, Vector2 sensorCenterPos, float sensorRadius) {	
		sensorCenterPos.rotate(master.angle);
		sensorCenterPos.add(master.position.x, master.position.y);
		float distance = sensorCenterPos.dst(gObj.position.x, gObj.position.y);
		
		if (distance < sensorRadius || sensorState == Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS)
			return (int) Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_WORKRADIUS;
		else
			return (int) Statics.FightingSkills.WEAPON_HOLD_SHIELD_TARGET_IN_SENSOR;
	}
		
	void updateShieldPos()
	{
		if(isModyfied) {
			int shieldMode = Statics.FightingSkills.WEAPON_HOLD_SHIELD_MODE_NORMAL;
			
			if (left == 1 && right == 1)
				shieldMode = Statics.FightingSkills.WEAPON_HOLD_SHIELD_MODE_FORWARD;	
			else if (forward == 1)
				shieldMode = Statics.FightingSkills.WEAPON_HOLD_SHIELD_MODE_FORWARD;
			else if (left == 1)
				shieldMode = Statics.FightingSkills.WEAPON_HOLD_SHIELD_MODE_LEFT;
			else if (right == 1)
				shieldMode = Statics.FightingSkills.WEAPON_HOLD_SHIELD_MODE_RIGHT;				
			master.world.world2d.changeShieldPos(master.myBody, shieldMode);			
			isModyfied = false;
		}
	}
}
