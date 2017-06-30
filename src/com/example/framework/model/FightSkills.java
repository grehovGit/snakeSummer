package com.example.framework.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ArrayMap.Values;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class FightSkills {
	
 	public static final int WORLD_WIDTH = Statics.WKS.WORLD_WIDTH;
    public static final int WORLD_HEIGHT = Statics.WKS.WORLD_HEIGHT;
    public static final int CELL_DIM = Statics.WKS.CELL_DIM;
    public static final float CELL_SIZE = Statics.WKS.CELL_SIZE;
	
	ArrayMap <String, IFightingSkill>  fSkills;
	SensorsManager sManager;
	Set<Entry<String, GameObject>> targets;
	DynamicGameObject master;
	Set <DynamicGameObject>  weaponQueue;
	
	boolean isRelaxing;
	float startRelaxTime; 
	
	//data for fast rendering
	public boolean forwardAttack = false;
	public boolean forwardLeftHook = false;
	public boolean forwardRightHook = false;
	public boolean forwardLeftTurn = false;
	public boolean forwardRightTurn = false;
	public boolean backLeftHook = false;
	public boolean backRightHook = false;
	public boolean backAttack = false;
	public boolean block = false;
	public boolean impulseForwardDef = false;
	public boolean impulseForwardAttack = false;
	public boolean impulseLeftSideDef = false;
	public boolean impulseRightSideDef = false;	
	public boolean iceForwardAttack = false;
	public boolean hypnoseForwardAttack = false;
	public boolean fireForwardAttack = false;
	
	FightSkills (DynamicGameObject dynObj) {
		fSkills = new ArrayMap <String, IFightingSkill>();
		targets = new HashSet<Entry<String, GameObject>>();
		weaponQueue = new HashSet <DynamicGameObject>();
		sManager = new SensorsManager();
		master = dynObj;
		isRelaxing = false;
		startRelaxTime = 0;
	}
	
	public IFightingSkill addSkill(String type) {
		IFightingSkill fSkill = FightingSkillBase.getSkill(type, master);
		fSkills.put(type, fSkill);
		udpateRenderVariables(type, true);
		return  fSkill;
	}
	
	public SensorsManager getSensorManager() {
		return sManager;
	}
	
	void udpateRenderVariables(String type, boolean add_remove) {
		if(add_remove) {
			if(type == Statics.FightingSkills.FORWARD_ATTACK)
				forwardAttack = true;
			else if (type == Statics.FightingSkills.FORWARD_LEFT_HOOK)
				forwardLeftHook = true;			
			else if (type == Statics.FightingSkills.FORWARD_RIGHT_HOOK)
				forwardRightHook = true;
			else if (type == Statics.FightingSkills.FORWARD_LEFT_TURN)
				forwardLeftTurn = true;
			else if (type == Statics.FightingSkills.FORWARD_RIGHT_TURN)
				forwardRightTurn = true;
			else if (type == Statics.FightingSkills.BACK_LEFT_HOOK)
				backLeftHook = true;
			else if (type == Statics.FightingSkills.BACK_RIGHT_HOOK)
				backRightHook = true;
			else if (type == Statics.FightingSkills.BACK_ATTACK)
				backAttack = true;
			else if (type == Statics.FightingSkills.BLOCK)
				block = true;	
			else if (type == Statics.FightingSkills.IMPULSE_ATTACK_FORWARD)
				impulseForwardAttack = true;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_FORWARD)
				impulseForwardDef = true;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_LEFTSIDE)
				impulseLeftSideDef = true;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_RIGHTSIDE)
				impulseRightSideDef = true;					
			else if (type == Statics.FightingSkills.ICE_ATTACK_FORWARD)
				iceForwardAttack = true;	
			else if (type == Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD)
				hypnoseForwardAttack = true;	
			else if (type == Statics.FightingSkills.FIRE_ATTACK_FORWARD)
				fireForwardAttack = true;	
		} else {
			if(type == Statics.FightingSkills.FORWARD_ATTACK)
				forwardAttack = false;
			else if (type == Statics.FightingSkills.FORWARD_LEFT_HOOK)
				forwardLeftHook = false;			
			else if (type == Statics.FightingSkills.FORWARD_RIGHT_HOOK)
				forwardRightHook = false;
			else if (type == Statics.FightingSkills.FORWARD_LEFT_TURN)
				forwardLeftTurn = false;
			else if (type == Statics.FightingSkills.FORWARD_RIGHT_TURN)
				forwardRightTurn = false;
			else if (type == Statics.FightingSkills.BACK_LEFT_HOOK)
				backLeftHook = false;
			else if (type == Statics.FightingSkills.BACK_RIGHT_HOOK)
				backRightHook = false;
			else if (type == Statics.FightingSkills.BACK_ATTACK)
				backAttack = false;
			else if (type == Statics.FightingSkills.BLOCK)
				block = false;
			else if (type == Statics.FightingSkills.IMPULSE_ATTACK_FORWARD)
				impulseForwardAttack = false;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_FORWARD)
				impulseForwardDef = false;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_LEFTSIDE)
				impulseLeftSideDef = false;	
			else if (type == Statics.FightingSkills.IMPULSE_DEFFENSE_RIGHTSIDE)
				impulseRightSideDef = false;
			else if (type == Statics.FightingSkills.ICE_ATTACK_FORWARD)
				iceForwardAttack = false;	
			else if (type == Statics.FightingSkills.HYPNOSE_ATTACK_FORWARD)
				hypnoseForwardAttack = false;	
			else if (type == Statics.FightingSkills.FIRE_ATTACK_FORWARD)
				fireForwardAttack = false;			
		}			
	}
	
	public void removeSkill(String type) {
		fSkills.removeKey(type);
		udpateRenderVariables(type, false);
	}
	
	public boolean haveSkill(String type) {
		return fSkills.containsKey(type);
	}
	
	public void setSkillLevel(String type, int newLevel) {
		IFightingSkill fSkill = fSkills.get(type);	
		if(fSkill != null) {
			fSkill.setAttackLevel(newLevel);
			fSkill.setWorkDistance(Statics.FightingSkills.getFSkillWorkDistance(type, newLevel));
		}
	}
	
	public void setSkillChargeLevel(String type, int newLevel) {
		IFightingSkill fSkill = fSkills.get(type);		
		if(fSkill != null)
			fSkill.setChargeLevel(newLevel);
	}
	
	public IFightingSkill getSkill(String type) {
		return fSkills.get(type);
	}
	
	public void addTarget(String sensor, GameObject target) {
		if(haveTarget(sensor, target))
			return;
		
		Entry<String, GameObject> entry = new Entry<String, GameObject>();
		entry.key = sensor;
		entry.value = target;
		targets.add(entry);
	}
	
	public void removeTarget(Entry<String, GameObject> target) {
		targets.remove(target);
	}
	
	public void clearTargets() {	
		Iterator <Entry<String, GameObject>> iter = targets.iterator();	
		while(iter.hasNext()) {
			Entry <String, GameObject> target = iter.next();	
			if (target.key == null || target.value == null || target.value.myBody == null 
				|| target.key == Statics.FightingSkills.SENSOR_FORWARD_RAY
				||target.key == Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_IMPULSE
				||target.key == Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_ICE
				||target.key == Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_HYPNOSE
				||target.key == Statics.FightingSkills.SENSOR_VIRTUAL_FORWARD_RAY_FIRE
				|| master.position.dist(target.value.position) > 2.5f * Statics.FightingSkills.getSensorRadius(target.key)) {
				iter.remove();
			}
		}		
	}	
	
	public boolean haveTarget(String sensor, GameObject targetObj) {
		Iterator <Entry<String, GameObject>> targetsIterator = targets.iterator();	
		while(targets.iterator().hasNext()) {
			Entry <String, GameObject> target = targetsIterator.next();			
			if(target.key == sensor && target.value == targetObj)
				return true;
		}		
		return false;
	}
	
	public boolean haveTargets() {
		return targets.size() > 0;
	}
	
	public void update(float actTime, double deltaTime) {
		clearTargets();			
		if(isRelaxing) processRelaxing(actTime);		
		for (Entry<String, IFightingSkill> skill : fSkills)		
			skill.value.update(actTime, (float) deltaTime);			
		processWeaponQueue();
	}
	
	void processRelaxing(float actTime) {
		if(actTime - startRelaxTime >= Statics.FightingSkills.FIGHTSKILL_RELAX_PERIOD) {
			isRelaxing  = false;
			startRelaxTime = 0;
		}		
	}
	
	public void startRelaxing() {
		isRelaxing = true;
		startRelaxTime = master.world.actTime;
	}
	
	public void strike(float actTime, DynamicGameObject target, FightingSkill fSkill, boolean isBot) {
		if(!isRelaxing && fSkill.isReady && !master.stateHS.isStriking && !master.stateHS.isHeated && !master.stateHS.isHypnosed)
			fSkill.strikeStart(actTime, target, isBot);
	}
	
	public void strikeAvatar(float actTime, String skill) {
		if(!master.stateHS.isStriking && !master.stateHS.isHeated && !master.stateHS.isHypnosed) {
			FightingSkillBase fSkill = (FightingSkillBase) getSkill(skill);

			if(fSkill != null && fSkill.isReady)
				fSkill.strikeStart(actTime, null, false);
		}
	}
	
	public Values<IFightingSkill> getFSkills() {
		return fSkills.values();
	}
	
	 public boolean processNewWeaponQueue(DynamicGameObject weapon) {
		 
		 	boolean need = false;
		 	int type = weapon.objType;
		 	int weaponClass = Statics.DynamicGameObject.getWeaponClass(type);
		 	FightingSkillBase fSkill = null;
		 		
			if (weaponClass == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_HELM && haveSkill(Statics.FightingSkills.WEAPON_HOLD_HELM)) {
				fSkill = (FightingSkillBase) getSkill(Statics.FightingSkills.WEAPON_HOLD_HELM);				
				if (!fSkill.isReady && fSkill.isStartStriking == true)
					need = true;
			} else if (weaponClass == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SHIELD && haveSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD)) {
				fSkill = (FightingSkillBase) getSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD);				
				if (!fSkill.isReady && fSkill.isStartStriking == true)
					need = true;		
			} else if ((weaponClass == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SWORD
						|| weaponClass == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_AXE
						|| weaponClass == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)) {	
				
				if (haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM)) {
					fSkill = (FightingSkillBase) getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM);				
					if(!fSkill.isReady && fSkill.isStartStriking == true)
						need = true;
				}
				if(haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM)) {
					fSkill = (FightingSkillBase) getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM);					
					if(!fSkill.isReady && fSkill.isStartStriking == true)
						need = true;
				}
			}		
			if(need) {								
				return weaponQueue.add(weapon);
			}			
			return false;			
		}
	 
	 public void processWeaponQueue() {
		for (DynamicGameObject weapon : weaponQueue)
			processWeaponHolding(weapon);	 
		 weaponQueue.clear();		 
	 }
	
	 public void processWeaponHolding(DynamicGameObject weapon) {		 
		 	int type = weapon.objType;
		 	FightingSkillBaseWeaponSkills fSkill = null;
			if (Statics.DynamicGameObject.getWeaponClass(type) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_HELM 
					&& haveSkill(Statics.FightingSkills.WEAPON_HOLD_HELM)) {
				if (!(fSkill = (FightingSkillBaseWeaponSkills) getSkill(Statics.FightingSkills.WEAPON_HOLD_HELM)).isReady) {
					master.world.world2d.processPhysicalHelmHolding(master, weapon.myBody);
					fSkill.isReady = true;					
				}
			} else if (Statics.DynamicGameObject.getWeaponClass(type) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SHIELD 
					&& haveSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD)) {
				if (!(fSkill = (FightingSkillBaseWeaponSkills) getSkill(Statics.FightingSkills.WEAPON_HOLD_SHIELD)).isReady) {
					master.world.world2d.processPhysicalShieldHolding(master, weapon.myBody);
					fSkill.isReady = true;
					fSkill.weapon = weapon;
				}		
			} else if ((Statics.DynamicGameObject.getWeaponClass(type) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SWORD
					|| Statics.DynamicGameObject.getWeaponClass(type) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_AXE
					|| Statics.DynamicGameObject.getWeaponClass(type) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)) {		
				if (haveSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM) 
						&& !(fSkill = (FightingSkillBaseWeaponSkills) getSkill(Statics.FightingSkills.WEAPON_HOLD_LEFT_ARM)).isReady) {
					processArmWeaponHolding(weapon, fSkill.getPower(), true);	
					fSkill.isReady = true;
					fSkill.weapon = weapon;
				} else if (haveSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM) 
						&& !(fSkill = (FightingSkillBaseWeaponSkills) getSkill(Statics.FightingSkills.WEAPON_HOLD_RIGHT_ARM)).isReady) {
					processArmWeaponHolding(weapon, fSkill.getPower(), false);	
					fSkill.isReady = true;
					fSkill.weapon = weapon;
				}
			}		
		}
  
	 public void processArmWeaponHolding(DynamicGameObject weapon, float power, boolean isLeft) {		 
		 if (Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SWORD)
			 master.world.world2d.processArmSwordHolding(master, weapon, isLeft, (int) power);
		 else if (Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_AXE)
			 master.world.world2d.processArmAxeHolding(master, weapon, isLeft, (int) power);	 
		 else if (Statics.DynamicGameObject.getWeaponClass(weapon.objType) == Statics.DynamicGameObject.ABSTRACT_TYPE_WEAPON_SPEAR)
			 master.world.world2d.processArmSpearHolding(master, weapon, isLeft, (int) power);
	 }		
}
