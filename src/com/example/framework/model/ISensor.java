package com.example.framework.model;

import java.util.List;

import com.badlogic.gdx.utils.ObjectMap.Entry;

/**
 * Represents all types sensors 
 * @author Grehov
 *
 */
public interface ISensor {
	
	/**
	 * Produce targets list
	 * @return
	 */
	List<Entry<DynamicGameObject, Float>> getTargets(List<Entry<String,Entry<DynamicGameObject, Float>>> targets);

}
