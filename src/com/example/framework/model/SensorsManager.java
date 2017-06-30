package com.example.framework.model;

import java.util.HashMap;

/**
 * Sensors manager
 * @author Grehov
 *
 */
public class SensorsManager {
	private HashMap<String, ISensor> sensors = new HashMap<String, ISensor>();
	
	ISensor get(String sensor) {
		ISensor sens = sensors.get(sensor);
		return sens != null ? sens : getSensor (sensor);
	}
	
	/**
	 * Factory method
	 * @param sensor
	 * @return sensor object
	 * @throws InvalidArgumentException if undefined argument sensor passed
	 */
	private ISensor getSensor (String sensor) {
		return null;
	}

}
