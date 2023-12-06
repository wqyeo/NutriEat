package com.mygdx.nutrieats.cookingscene.entities;

import com.badlogic.gdx.math.Vector2;

import eatengine.Util;

public class EntityAnimator {
	
	public static final float JUMP_DURATION = 0.125f;
	public static final float JUMP_SPEED = 125f;
	
	public static final float MOVE_SPEED = 250f;
	// How close 
	public static final float DISTANCE_THRESHOLD = 4f;

	private IEntity entity;
	
	private Vector2 currentScale;
	
	private AnimationType currentPlayingAnimation;
	private OnAnimationDone callback;
	private boolean animationPlaying;
	
	private boolean isLanding;
	private float jumpTimer;
	
	private float pauseDuration;
	private float pauseTimer;
	
	private Vector2 movingTowards;
	
	private float flipProgress;
	private Vector2 flipFrom;
	private Vector2 flipTowards;
	
	public EntityAnimator(IEntity entity, Vector2 startingScale) {
		this.entity = entity;
		currentScale = startingScale;
		
		jumpTimer = 0f;
		animationPlaying = false;
	}
	
	void update(float deltaTime) {
		if (!animationPlaying) {
			return;
		}
		
		if (currentPlayingAnimation == AnimationType.HOP_AND_MOVE) {
			handleMoving(deltaTime);
			handleHopping(deltaTime);
		} else if (currentPlayingAnimation == AnimationType.HOP) {
			handleHopping(deltaTime);
		} else if (currentPlayingAnimation == AnimationType.PAUSE) {
			handlePausing(deltaTime);
		} else if (currentPlayingAnimation == AnimationType.FLIP) {
			handleFlipping(deltaTime);
		}
	}
	
	private void handleFlipping(float deltaTime) {
		if (flipProgress >= 1f) {
			currentScale = flipTowards.cpy();
			entity.setEntityScale(flipTowards);
			animationPlaying = false;
			triggerCallback();
			return;
		}
		
		currentScale = Util.lerpVec2(flipFrom, flipTowards, flipProgress);
		entity.setEntityScale(currentScale);
		flipProgress += deltaTime * 2f;
	}
	
	private void handlePausing(float deltaTime) {
		if (pauseTimer >= pauseDuration) {
			animationPlaying = false;
			triggerCallback();
			return;
		}
		
		pauseTimer += deltaTime;
	}
	
	private void handleHopping(float deltaTime) {
		if (jumpTimer >= JUMP_DURATION) {
			if (isLanding) {
				triggerHopSequenceComplete();
			}
			isLanding = !isLanding;
			jumpTimer = 0;
			return;
		}	
		float yDistanceCovered = deltaTime * JUMP_SPEED;
		if (isLanding) {
			yDistanceCovered *= -1f;
		}
		
		Vector2 finalPosition = Util.addVec2(entity.getEntityPosition(), new Vector2(0f, yDistanceCovered));
		entity.setEntityPosition(finalPosition);
		jumpTimer += deltaTime;
	}
	
	private void handleMoving(float deltaTime) {
		if (Util.isClose(entity.getEntityPosition(), movingTowards, DISTANCE_THRESHOLD)) {
			triggerMoveSequenceComplete();
			return;
		}
		
		Vector2 direction = Util.subVec2(movingTowards, entity.getEntityPosition()).nor();
		Vector2 velocity = Util.multiplyVec2(direction, deltaTime * MOVE_SPEED);
		Vector2 finalPosition = Util.addVec2(entity.getEntityPosition(), velocity);
		entity.setEntityPosition(finalPosition);
	}
	
	private void triggerMoveSequenceComplete() {
		if (currentPlayingAnimation == AnimationType.HOP_AND_MOVE) {
			// Let entity finish hopping.
			currentPlayingAnimation = AnimationType.HOP;
		}
	}
	
	private void triggerHopSequenceComplete() {
		if (currentPlayingAnimation == AnimationType.HOP) {
			animationPlaying = false;
			triggerCallback();
		}
	}
	
	private void triggerCallback() {
		if (callback != null) {
			callback.callback(currentPlayingAnimation);
		}
	}
	
//region AnimationInterfaces
	
	public void triggerFlip(OnAnimationDone callback) {
		this.callback = callback;
		currentPlayingAnimation = AnimationType.FLIP;
		
		float x = currentScale.x * -1f;
		flipFrom = new Vector2(currentScale.x, currentScale.y);
		flipTowards = new Vector2(x, currentScale.y);
		
		flipProgress = 0f;
		animationPlaying = true;
	}
	
	public void triggerHopMove(Vector2 moveTowards, OnAnimationDone callback) {
		// TODO: Interrupt currently playing animation.
		movingTowards = moveTowards;
		this.callback = callback;
		currentPlayingAnimation = AnimationType.HOP_AND_MOVE;
		
		isLanding = false;
		jumpTimer = 0f;
		animationPlaying = true;
	}
	
	public void triggerPause(float pauseDuration, OnAnimationDone callback) {
		// TODO: Interrupt
		this.pauseDuration = pauseDuration;
		this.callback = callback;
		currentPlayingAnimation = AnimationType.PAUSE;
		
		pauseTimer = 0f;
		isLanding = false;
		jumpTimer = 0f;
		animationPlaying = true;
	}
	
//endregion
	
	public Vector2 getCurrentScale() {
		return currentScale;
	}
}
