package com.example.framework.model;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.ObjectMap.Entry;

/**
 * Represents base sensor
 * @author Grehov
 *
 */
abstract public class SensorBase implements ISensor {
	protected String type;
	DynamicGameObject master;
	
	SensorBase(DynamicGameObject master, String type) {
		this.master = master;
		this.type = type;
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
	
	@Override
	public List<Entry<DynamicGameObject, Float>> getTargets(List<Entry<String,Entry<DynamicGameObject, Float>>> targets) {
		List<Entry<DynamicGameObject, Float>> myTargets = new LinkedList<>();
		 for (Entry<String,Entry<DynamicGameObject, Float>> target : targets) {
			 if (target.key.equals(type)) 
				 myTargets.add(target.value);
		 }
		 return myTargets;
	}
}
