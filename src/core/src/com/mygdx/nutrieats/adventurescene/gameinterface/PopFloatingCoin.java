package com.mygdx.nutrieats.adventurescene.gameinterface;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.gameinterface.Coin;

import eatengine.GameObject;
import eatengine.Util;

/**
 * This coin will:
 * 1. Pop outwards (animation to move very fast to a location)
 * 2. Float itself towards given position
 * 3. then disappear and invoke callback.
 *
 */
public class PopFloatingCoin extends Coin {
	
	private final static float MAX_INITAL_POP_SPEED = 850f;
	private final static float MIN_INITIAL_POP_SPEED = 600f;
	private final static float POP_DURATION = 0.40f;
	// Hover for a awhile after popping
	private final static float HOVER_DURATION = 0.3f;
	private final static float HOVER_SPEED = 1f;
	
	private final static float FLOAT_SPEED = 700f;
	
	private enum AnimationState {
		POP,
		HOVER,
		FLOAT
	}
	
	private final Vector2 floatTowards;
	private final Vector2 popDirection;
	
	private AnimationState currentAnimationState;
	private OnCoinFloatedCallback callback;
	
	// Used to count time before switching states.
	private float timer;
	
	private float popProgress;
	private final float initialPopSpeed;
	
	public PopFloatingCoin(String name, Sprite coinSprite, Vector2 startingPosition, Vector2 coinScale,  Vector2 floatTowards, OnCoinFloatedCallback callback) {
		super(name, coinSprite, coinScale, startingPosition);
		
		this.floatTowards = floatTowards;
		popDirection = generateRandomDirection();
		currentAnimationState = AnimationState.POP;
		this.callback = callback;
		timer = 0f;
		popProgress = 0f;
		
		initialPopSpeed = MIN_INITIAL_POP_SPEED + (float)(Math.random() * (MAX_INITAL_POP_SPEED - MIN_INITIAL_POP_SPEED));
	}
	
	private static Vector2 generateRandomDirection() {
		Random random = new Random();
		float angle = random.nextFloat() * MathUtils.PI2;
		return new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		if (currentAnimationState == AnimationState.POP) {
			handlePopAnimation(deltaTime);
		} else if (currentAnimationState == AnimationState.HOVER) {
			handleHoverAnimation(deltaTime);
		} else if (currentAnimationState == AnimationState.FLOAT) {
			handleFloatAnimation(deltaTime);
		}
		
		timer += deltaTime;
	}
	
	private void handleHoverAnimation(float deltaTime) {
		if (timer >= HOVER_DURATION) {
			currentAnimationState = AnimationState.FLOAT;
			timer = 0f;
			return;
		}
		
		// HOVER
		
		float currDistanceIncrement = deltaTime * HOVER_SPEED;
		Vector2 toTravel = Util.multiplyVec2(popDirection, currDistanceIncrement); 
		Vector2 newPosition = Util.addVec2(getPosition(), toTravel);
		this.setPosition(newPosition);
	}
	
	private void handleFloatAnimation(float deltaTime) {
		if (Util.isClose(floatTowards, this.getWorldPosition(), 10f)) {
			if (callback != null) {
				callback.triggerCallback();
			}
			this.getSceneReference().removeFromRoot(this, true);
			return;
		}
		
		// FLOAT
		
		Vector2 direction = Util.subVec2(floatTowards, getPosition());
		direction.nor();
		Vector2 velocity = Util.multiplyVec2(direction, deltaTime * FLOAT_SPEED);
		setPosition(Util.addVec2(getPosition(), velocity));
	}
	
	private void handlePopAnimation(float deltaTime) {
		if (timer >= POP_DURATION) {
			currentAnimationState = AnimationState.HOVER;
			timer = 0f;
			return;
		}
		
		popProgress = Util.lerp(0f, 1f, timer / POP_DURATION);
		float currentSpeed = Util.lerp(initialPopSpeed, 0f, popProgress);
		
		// POP
		
		float currDistanceIncrement = deltaTime * currentSpeed;
		Vector2 toTravel = Util.multiplyVec2(popDirection, currDistanceIncrement); 
		Vector2 newPosition = Util.addVec2(getPosition(), toTravel);
		this.setPosition(newPosition);
	}
}
