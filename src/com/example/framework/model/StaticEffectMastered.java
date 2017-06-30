package com.example.framework.model;

public class StaticEffectMastered extends StaticEffect implements IUpdatableByMaster {

	public StaticEffectMastered(float x, float y, float width, float height, int type, float angle) {
		super(x, y, width, height, type, angle);
	}

	@Override
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;		
	}

	@Override
	public void setAngle(float angle) {
		this.angle = angle;		
	}

	@Override
	public void setSize(float width, float height) {
		this.bounds.width = width;
		this.bounds.height = height;		
	}

}
