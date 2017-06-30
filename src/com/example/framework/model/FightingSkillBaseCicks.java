package com.example.framework.model;


import com.badlogic.gdx.math.Vector2;

public abstract class FightingSkillBaseCicks extends FightingSkillBase {
	
	FightingSkillBaseCicks(String type, DynamicGameObject master) {
		super(type, master);
	}	
	
	@Override
	public void update(float actTime, float deltaTime) {	
		super.update(actTime, deltaTime);
		if(!isReady && Statics.FightingSkills.CHARGING_DEFAULT_PERIOD * attackPower / chargePower < actTime - startTime)
			isReady = true;					
	}
	
	void strikeInit(float forcePeriod) {
		master.stateHS.strikeType = Statics.Render.STRIKE_JUMP;		
		if (master.objType == Statics.DynamicGameObject.SNAKE) {
			Snake snake = (Snake) master;
			SnakePart sPart = null;
		 	int size = snake.parts.size();
		 	master.stateHS.strikePeriod =  2 * size *  Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD + forcePeriod;
		 	
		 	for (int i = size - 1; i >= 0; --i) {
		 		sPart = snake.parts.get(i);
		 		sPart.stateHS.strikePower = attackPower;
				sPart.stateHS.strikeType = Statics.Render.STRIKE_JUMP;
				sPart.myBody.setAngularDamping(Statics.PhysicsBox2D.CHAR_ANGUALR_DAMPING_JUMP);
				sPart.myBody.setLinearDamping(Statics.PhysicsBox2D.CHAR_LINEAR_DAMPING_JUMP);
				sPart.stateHS.strikePeriod = forcePeriod + (i + 1) * 2 * Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD;
		 	}
		} else {
		 	master.stateHS.strikePeriod =  2 * Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD + forcePeriod;
			master.myBody.setAngularDamping(Statics.PhysicsBox2D.CHAR_ANGUALR_DAMPING_JUMP);
			master.myBody.setLinearDamping(Statics.PhysicsBox2D.CHAR_LINEAR_DAMPING_JUMP);
		}		
	}
	
	void strikeFinish() {
		IS_STRIKING = isStriking = false;
		master.stateHS.isStriking = false;
		master.stateHS.strikeStartTime = 0;
		targetPos.set(0, 0);
		master.fSkills.startRelaxing();
		
		if(master.objType == Statics.DynamicGameObject.SNAKE) {
			Snake snake = (Snake) master;
			SnakePart sPart = null;
		 	int size = snake.parts.size();	
		 	
		 	for(int i = size - 1; i >= 0; --i) {
		 		sPart = snake.parts.get(i);
		 		sPart.stateHS.striked = false;
				sPart.stateHS.strikeType = Statics.Render.STRIKE_NO;	 		
		 		if(i == 0) {
					sPart.myBody.setLinearDamping(Statics.PhysicsBox2D.CHAR_LINEAR_DAMPING);	
					sPart.myBody.setAngularDamping(Statics.PhysicsBox2D.CHAR_ANGUALR_DAMPING);	
		 		} else {
					sPart.myBody.setLinearDamping(Statics.PhysicsBox2D.DYNOBJ_LINEAR_DAMPING);	
					sPart.myBody.setAngularDamping(Statics.PhysicsBox2D.DYNOBJ_ANGUALR_DAMPING);			 			
		 		}
		 	}
		} else {
			master.myBody.setLinearDamping(Statics.PhysicsBox2D.CHAR_LINEAR_DAMPING);	
			master.myBody.setAngularDamping(Statics.PhysicsBox2D.CHAR_ANGUALR_DAMPING);		
		}
	}
	
	public float getPower() {
		return attackPower;
	}
	
	public Vector2 getTargetPos() {
		return targetPos;
	}
	
	void renderSnakeWave(float dTime) {
		Snake snake = (Snake) master;
	 	int size = snake.parts.size();
	 	
	 	for(int i = size - 1; i >= 0; --i) {
	 		SnakePart sPart = snake.parts.get(i);
	 		float sPartTime = dTime - (size - 1 - i) * Statics.Render.SNAKE_GOLD_STRIKE_SEGMENT_CHANGING_PERIOD;		 			 		
		 	if(sPartTime > 0 && sPartTime <= master.stateHS.strikePeriod) {
			 	sPart.stateHS.isStriking = true;
			 	sPart.sPartGoldWaveTime = sPartTime;
		 	} else if (sPartTime > master.stateHS.strikePeriod)	{
			 	sPart.stateHS.isStriking = false;		 		
		 		if(i == 0) strikeFinish();			 		
		 	}
	 	}
	}
	
	@Override
	protected float getObstacleKoefToFSkill(DynamicGameObject target) {
		float obWeight = 0;
		if(type == Statics.FightingSkills.FORWARD_ATTACK)
			obWeight = master.world.wProc.getObstacleKoefForForwardAttack(master, target);
		else if (type == Statics.FightingSkills.FORWARD_LEFT_HOOK)
			obWeight = master.world.wProc.getObstacleKoefForHook(master, target, true);	
		else if (type == Statics.FightingSkills.FORWARD_RIGHT_HOOK)
			obWeight = master.world.wProc.getObstacleKoefForHook(master, target, false);				
		else if (type == Statics.FightingSkills.FORWARD_LEFT_TURN)
			obWeight = master.world.wProc.getObstacleKoefForHook(master, target, false);	
		else if(type == Statics.FightingSkills.FORWARD_RIGHT_TURN)
			obWeight = master.world.wProc.getObstacleKoefForHook(master, target, true);	
		else if (type == Statics.FightingSkills.BACK_LEFT_HOOK)
			obWeight = master.world.wProc.getObstacleKoefForBackHook(master, target, true);				
		else if(type == Statics.FightingSkills.BACK_RIGHT_HOOK)
			obWeight = master.world.wProc.getObstacleKoefForBackHook(master, target, false);	
		else if (type == Statics.FightingSkills.BACK_ATTACK)
			obWeight = master.world.wProc.getObstacleKoefForBackAttack(master, target);
		else
			return 1; 
		
		obWeight = 1 - obWeight / Statics.AI.MAX_WEIGHT;		
		return obWeight <= 0 ? 0 : obWeight;
	}
}
