package com.example.framework.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public abstract class FightingSkillBase implements IFightingSkill {
	
	static boolean IS_STRIKING;
	protected String type;
	protected boolean isReady;
	protected boolean isStriking;
	protected boolean isStartStriking;
	protected float startTime;
	protected float rateByDistanceClass;
	protected float fSkillRate;
	protected DynamicGameObject master;
	protected Vector2 targetPos;
	protected Vector2 startPos;
	protected float workDistance;
	protected float curPath;
	protected float maxPath;	
	protected int attackPower;
	protected int chargePower;
	protected Set<ISensor> mySensors = new HashSet<ISensor>(); 
	
	FightingSkillBase (String type, DynamicGameObject master) {
		this.type = type;
		this.master = master;
		targetPos = new Vector2();
		startPos = new Vector2();
		isReady = false;
		isStriking = false;
		isStartStriking = true;
		rateByDistanceClass = Statics.FightingSkills.getFSkillRateByDistanceClass(type);
		fSkillRate = Statics.FightingSkills.getFSkillRate(type);
		workDistance = Statics.FightingSkills.getFSkillWorkDistance(type, Statics.FightingSkills.FIGHTSKILL_LEVEL_PINK);
		attackPower = Statics.FightingSkills.FIGHTSKILL_LEVEL_PINK;
		chargePower = Statics.FightingSkills.FIGHTSKILL_LEVEL_PINK;
		fillSensors();
	}

	protected void fillSensors() {
		switch (type) {
			case Statics.FightingSkills.FORWARD_ATTACK:
			case Statics.FightingSkills.FORWARD_LEFT_HOOK:
			case Statics.FightingSkills.FORWARD_RIGHT_HOOK:
			case Statics.FightingSkills.IMPULSE_DEFFENSE_FORWARD:
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_FORWARD));			
			case Statics.FightingSkills.BACK_LEFT_HOOK:
			case Statics.FightingSkills.FORWARD_LEFT_TURN:
			case Statics.FightingSkills.IMPULSE_DEFFENSE_LEFTSIDE:
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_LEFT_SIDE));					
			case Statics.FightingSkills.BACK_RIGHT_HOOK:
			case Statics.FightingSkills.FORWARD_RIGHT_TURN:
			case Statics.FightingSkills.IMPULSE_DEFFENSE_RIGHTSIDE:
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_RIGHT_SIDE));		
			case Statics.FightingSkills.BACK_ATTACK:
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_BACK));		
			case Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM:
			case Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM:
			case Statics.FightingSkills.WEAPON_HOLD_SHIELD:
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_FORWARD));
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_LEFT_SIDE));
				mySensors.add(master.fSkills.getSensorManager().get(Statics.FightingSkills.SENSOR_RIGHT_SIDE));	
			default:
				assert false: "Invalid skill";			
		}
	}
	
	public void update(float actTime, float deltaTime) {	
		if (isStriking) strike(actTime);	
	}
	
	public float getStrikeRate(DynamicGameObject target, final float targetForceRate) {
		return 0;
	}
	
	@Override
	public void strikeStart(float actTime, DynamicGameObject target,  boolean isBot) {
		isReady = false;
		IS_STRIKING = isStriking = true;
		startTime = actTime;
		master.stateHS.isStriking = true;
		isStartStriking = true;
		master.stateHS.striked = false;
		master.stateHS.strikeStartTime = actTime;
		master.stateHS.strikePower = attackPower;
		startPos.set(master.position.x, master.position.y);
		
		if(isBot)		
			targetPos.set(target.position.x, target.position.y);
		else
			setAvatarTargetPoint();	
	}

	abstract void strike(float actTime);
	
	void setAvatarTargetPoint() {
		targetPos.set(workDistance, 0);
		targetPos.setAngle(master.angle);
	}
	
	protected List<Entry<DynamicGameObject, Float>> getTargets(List<Entry<String, Entry<DynamicGameObject, Float>>> targets) {
		LinkedList<Entry<DynamicGameObject, Float>> myTargets = new LinkedList<>();
		for (ISensor sensor : mySensors)
			myTargets.addAll(sensor.getTargets(targets));
		return myTargets;
	}
	
	protected float rateTargets(List<Entry<String, Entry<DynamicGameObject, Float>>> targets) {
		List<Entry<DynamicGameObject, Float>> myTargets = getTargets(targets);
		float curRate = 0, maxRate = 0;
		for (Entry<DynamicGameObject, Float> target : myTargets) {
			if ((curRate = rateTarget(target.key, target.value)) > maxRate)
					maxRate = curRate;
		}
		return maxRate;
	}
	
	float rateTarget(DynamicGameObject target, float forceRate) {		
		float dst = Vector2.dst(master.position.x, master.position.y, target.position.x, target.position.y);					
		if(dst > workDistance) return 0;			
		
		float result = fSkillRate;												// 1 - 1.8
		result *= rateByDistanceClass; 							   				// 1 - 8
		result *= getObstacleKoefToFSkill(target);			    				// 0 - 1
		return result *= getFSkillOptimalRateForTarget(target, forceRate);  	// 0.01 - 1
	}
	
	protected float getObstacleKoefToFSkill(DynamicGameObject target) {	
		return 1;
	}
	
	protected float getFSkillOptimalRateForTarget(DynamicGameObject target, float forceRate) {
		//optimal fSkillPower to forceRate formula: 
		//power >= FIGHTSKILL_POWER_START && power <= FIGHTSKILL_POWER_FINISH: F(power) / (F(power) +  DeltaF(power))

		//F(power): forceRate = DeadlyForceRate / (1 + force) ^ 2
		//DeltaF(power): [OurForceRate] - [forceRate = F(power) = DeadlyForceRate / (1 + force) ^ 2]		
		float power = attackPower;	
		float optimalForceRate = (float) (Statics.FightingSkills.FIGHTSKILL_DEADLY_FORCE_RATE / Math.pow((1 + power), 2));
		float optimalFSkillRate = optimalForceRate / (Math.abs(forceRate - optimalForceRate) + optimalForceRate);
		return optimalFSkillRate;
	}
	
	public static IFightingSkill getSkill(String type, DynamicGameObject master) {
		switch (type) {
			case Statics.FightingSkills.FORWARD_ATTACK:
				return new FightingSkillForwardAttack(master);
			case Statics.FightingSkills.FORWARD_LEFT_HOOK:
				return new FightingSkillForwardLeftHook(master);
			case Statics.FightingSkills.FORWARD_RIGHT_HOOK:
				return new FightingSkillForwardRightHook(master);
			case Statics.FightingSkills.FORWARD_LEFT_TURN:
				return new FightingSkillForwardLeftTurn(master);
			case Statics.FightingSkills.FORWARD_RIGHT_TURN:
				return new FightingSkillForwardRightTurn(master);
			case Statics.FightingSkills.BACK_LEFT_HOOK:
				return new FightingSkillBackLeftHook(master);
			case Statics.FightingSkills.BACK_RIGHT_HOOK:
				return new FightingSkillBackRightHook(master);
			case Statics.FightingSkills.BACK_ATTACK:
				return new FightingSkillBackAttack(master);	
			case Statics.FightingSkills.IMPULSE_ATTACK_FORWARD:
				return new FightingSkillImpulseAttackForward(master);				
			case Statics.FightingSkills.IMPULSE_DEFFENSE_FORWARD:
				return new FightingSkillImpulseDeffenseForward(master);
			case Statics.FightingSkills.IMPULSE_DEFFENSE_LEFTSIDE:
				return new FightingSkillImpulseDeffenseLeftSide(master);							
			case Statics.FightingSkills.IMPULSE_DEFFENSE_RIGHTSIDE:
				return new FightingSkillImpulseDeffenseRightSide(master);	
			case Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD:
				return new FightingSkillHypnoseAttackForward(master);
			case Statics.FightingSkills.ICE_ATTACK_FORWARD:
				return new FightingSkillIceAttackForward(master);
			case Statics.FightingSkills.FIRE_ATTACK_FORWARD:
				return new FightingSkillFireAttackForward(master);				
			case Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM:
				return new FightingSkillWeaponHoldLeftArm(master);
			case Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM:
				return new FightingSkillWeaponHoldRightArm(master);
			case Statics.FightingSkills.WEAPON_HOLD_SHIELD:
				return new FightingSkillWeaponHoldShield(master);
			case Statics.FightingSkills.WEAPON_HOLD_HELM:
				return new FightingSkillWeaponHoldHelm(master);				
			case Statics.FightingSkills.WEAPON_THROW:
				return new FightingSkillWeaponThrow(master);
			case Statics.FightingSkills.WEAPON_THROW_SPEAR:
				return new FightingSkillWeaponThrowSpear(master);
			case Statics.FightingSkills.WEAPON_THROW_SHIELD:
				return new FightingSkillWeaponThrowShield(master);
			default:
				assert false: "Invalid skill";	
				return null;
		}
	}
	
	@Override
	public void setAttackLevel(int level) {
		attackPower = level;
	}
	@Override
	public int getAttackLevel(int level) {
		return attackPower;
	}
	@Override
	public void setChargeLevel(int level) {
		chargePower = level;
	}
	@Override
	public int getChargeLevel(int level) {
		return chargePower;
	}
	@Override
	public
	void setWorkDistance(float dist) {
		workDistance = dist;
	}

}
