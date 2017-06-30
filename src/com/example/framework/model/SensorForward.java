package com.example.framework.model;

import java.util.List;

import com.badlogic.gdx.utils.ObjectMap.Entry;

/**
 * Represents forward sensor
 * @author Grehov
 *
 */
public class SensorForward extends SensorBase {
	public static final float RADIUS = 2f;
	public static final float OFFSET_X = 2.3f;
	public static final float OFFSET_Y = 0;
	
	DynamicGameObject master;
	
	SensorForward(DynamicGameObject master, String type) {
		super(master, type);
		master.world.world2d.addFSkillSensor(master, RADIUS, OFFSET_X, OFFSET_Y, type);
	}
	
	public int defineObstacleType(DynamicGameObject dynObj, GameObject gObj) {
		if(!gObj.isDynamicObject)
			return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE_INSUPERABLE;
		else {
			DynamicGameObject dObj = (DynamicGameObject) gObj;
			
			if(dObj.isCharacter) {
				if(master.world.wProc.isCharFriend(master , dObj))
					return Statics.DynamicGameObject.ABSTRACT_TYPE_FRIEND;
				else
					return Statics.DynamicGameObject.ABSTRACT_TYPE_ENEMY;				
			} else 
				return Statics.DynamicGameObject.ABSTRACT_TYPE_OBSTACLE;
		}
	}


}
