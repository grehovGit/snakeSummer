package com.example.framework.model;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.kingsnake.physicsBox2d.PhysicsBox2d;


public class TreeHurt extends DynamicGameObject {

	public final static float HEART_BEAT_PERIOD_NORMAL = 2;
	public final static float HEART_BEAT_PERIOD_EXCITED = 1;
	public final static int MAX_BRANCHES_COUNT = 5;
	
	private final float branchAngle;
	private float branchCurAngle;
	private final Builder builder;
	private final List<Branch> branches = new ArrayList<Branch>();
	private final int branchesCount;
	boolean isExcited;
	
	public TreeHurt(final float x, final float y, final float angle, final int level, final WorldKingSnake world) {
    	super(x, y, 2 * Statics.Render.STANDART_SIZE_1,	2 * Statics.Render.STANDART_SIZE_1,	angle, 
    			Statics.DynamicGameObject.TREE_HURT, level, world);	
    	PhysicsBox2d.setBodyVertical(myBody);
    	PhysicsBox2d.setBodyMass(myBody, Statics.PhysicsBox2D.TREEHURT_BODY_MASS);
    	branchesCount = level + 1 > MAX_BRANCHES_COUNT ? MAX_BRANCHES_COUNT : level + 1;
    	branchAngle = 360 / branchesCount;
    	branchCurAngle = 0;  
    	fixtureGroupFilterId = (short) world.world2d.addCharacterFilterGroup(myBody);
    	builder = new Builder(this);
    	isExcited = false;
    	builder.buildClawTree();
	}
	
	void turnBranchAngle() {
		branchCurAngle += branchAngle;
	}
	
	private float getBranchAngle() {
		return branchCurAngle;
	}
	
	public boolean getState() {
		return isExcited;
	}
	
	public float getHurtBeatPeriod() {
		return isExcited ? HEART_BEAT_PERIOD_EXCITED : HEART_BEAT_PERIOD_NORMAL;
	}
	
	private void setRotation(boolean isExcited) {
		if(isExcited) {
			RevoluteJoint rootJoint = null;
			float rotationSpeed = (stateHS.level == HealthScore.LEVEL_PINK ?
					Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_EXCITED_SPEED_START / 1.5f :
					Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_EXCITED_SPEED_START +
					stateHS.level * stateHS.level * Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_EXCITED_SPEED_STEP);
			for (Branch branch : branches) {
				if ((rootJoint = branch.getRootJoint()) == null) continue;
				branch.relax();
				rootJoint.setMotorSpeed(-rotationSpeed);
				rootJoint.setMaxMotorTorque(rotationSpeed);
			}
		} else {
			RevoluteJoint rootJoint = null;
			for (Branch branch : branches) {
				if ((rootJoint = branch.getRootJoint()) == null) continue;
				rootJoint.setMotorSpeed(0);
				rootJoint.setMaxMotorTorque(Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_NORMAL_TORQUE);
			}			
		}
	}
	
	private void provideWaving() {
		for (Branch branch : branches)
			branch.provideWaving();	
	}
	
	private int getTreeGroupFilter() {
		return fixtureGroupFilterId;
	}
	
    public void update(float deltaTime) {    	
    	super.update(deltaTime);
    	setState(world.world2d.isEnemyInRegion(this, 2 * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_TREEHURT_RADIUS, 
    			2 * Statics.PhysicsBox2D.SENSOR_FIGHT_SKILL_TREEHURT_RADIUS));   				
    }
    
    private void setState(final boolean isExcited) {  
    	if (this.isExcited != isExcited) setRotation(this.isExcited = isExcited);
    	if (!this.isExcited) provideWaving();
    }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	interface BranchItem {
		void setJoint(RevoluteJoint joint);
		RevoluteJoint getJoint();
	}
	
	class BranchClawItem extends DynamicGameObject implements BranchItem {
		private RevoluteJoint myJoint;
		BranchClawItem (final float x, final float y, final float angle, final int level, final WorldKingSnake world) {
	    	super(x, y, Statics.Render.STANDART_SIZE_1,	Statics.Render.STANDART_SIZE_1,	angle, 
	    			Statics.DynamicGameObject.TREE_HURT_ORDYNARY_SEGMENT, level, world);
	    	myJoint = null;
	    	int filter = getTreeGroupFilter();
	    	PhysicsBox2d.setBodyFilter(filter, myBody);
		}
		@Override
		public void setJoint(RevoluteJoint joint) {
			myJoint = joint;
		}
		
		@Override
		public RevoluteJoint getJoint() {
			return myJoint;
		}
	}
	
	class Branch {
		private List<BranchItem> items = new ArrayList<BranchItem>(); 
		
		public void addItem(BranchItem item) {
			items.add(item);
		}
		public BranchItem getItem(int index) {
			return items.get(index);
		}
		RevoluteJoint getRootJoint() {
			return items.get(0).getJoint();
		}
		void provideWaving() {
			int index = 0;
			RevoluteJoint itemJoint = null;
			for (BranchItem item : items) {								
				if((itemJoint = item.getJoint()) == null) break;											
				itemJoint.setMaxMotorTorque(Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_NORMAL_TORQUE);										
				itemJoint.setMotorSpeed((float) Math.sin((world.actTime * Math.PI - index++ * Math.PI / 2) / 4));
			}			
		}
		void relax() {
			RevoluteJoint itemJoint = null;
			for (BranchItem item : items) {
				if ((itemJoint = item.getJoint()) == null) break;
				itemJoint.setMotorSpeed(0);
				itemJoint.setMaxMotorTorque(Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT_NORMAL_TORQUE);
			}
		}
	}
	
	class Builder {
		TreeHurt master;
		public Builder (TreeHurt treeHurt) {
			this.master = treeHurt;
		}
		
		public void addClawBranches() {
			for (int i = 0; i < branchesCount; i++)
				addClawBranch();
		}
		
		public void addClawBranch() {
			Branch branch = new Branch();
			for (int i=0; i < Statics.PhysicsBox2D.TREEHURT_ORDYNARY_SEGMENT_COUNT; i++) {
				branch.addItem(getClawItem(i, (DynamicGameObject) (i == 0 ? master : branch.getItem(i - 1))));
			}
			turnBranchAngle();
			branches.add(branch);
		}
		
		private BranchItem getClawItem(final int index, DynamicGameObject previousItem) {
			Vector2 offset = new Vector2(index * Statics.PhysicsBox2D.TREEHURT_FIRST_SEGMENT_OFFSET 
					+ Statics.PhysicsBox2D.TREEHURT_STEP_SEGMENT_OFFSET, 0);
			Vector2 anchorPos = new Vector2(offset);
			offset.setAngle(getBranchAngle());
			offset.add(master.position.x, master.position.y);			
			BranchClawItem newItem =  new BranchClawItem(offset.x, offset.y, getBranchAngle(), master.stateHS.level, master.world);

			anchorPos.add(Statics.PhysicsBox2D.TREEHURT_SEGMENT_ANCHOR_OFFSET, 0);
			anchorPos.setAngle(getBranchAngle());
			anchorPos.add(master.position.x, master.position.y);	
			RevoluteJoint joint = (RevoluteJoint) world.world2d.createJoint(Statics.PhysicsBox2D.TREEHURT_BRANCHITEM_JOINT, (JointDef)Statics.PhysicsBox2D.TREEHURT_BRANCH_ITEM_JOINT_DEF,
					(int)((master.stateHS.level + 1) * Statics.PhysicsBox2D.JOINT_FORCE_BASIC), index, previousItem.myBody, newItem.myBody, anchorPos);
			if (index == 0) joint.enableLimit(false);			
			newItem.setJoint(joint);
			world.dynObjects.add(newItem);
			return newItem;
		}
		public void buildClawTree() {
			addClawBranches();
		}
	}
	
	

}
