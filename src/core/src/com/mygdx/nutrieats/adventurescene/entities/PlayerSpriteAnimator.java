package com.mygdx.nutrieats.adventurescene.entities;

import eatengine.Component;
import eatengine.components.SpriteRenderer;
import com.badlogic.gdx.math.Vector2;
import eatengine.Util;

/**
 * Component to help animate the sprite on the player
 * 
 * @author wqyeo
 *
 */
public class PlayerSpriteAnimator extends Component {

	public static final float JUMP_MAX_HEIGHT = 25f;
	public static final float JUMP_SPEED = 6f;

	private boolean isLanding;
	private float jumpProgress;

	private float jumpHeightLimit;
	private Vector2 defaultPosition;

	private float flipProgress;
	private Vector2 flipTowards;
	private Vector2 defaultOrientation;
	private boolean flippingTowardsDefault;

	private PlayerAnimationState currentAnimationState;

	private AdventurePlayer playerRef;
	private SpriteRenderer rendererRef;

	private OnPlayerAnimationDone callback;
	// Ensure a assigned instance of callback is ever called only once.
	private boolean instanceCallbackInvoked;
	
	private float timerDuration;
	private float timer;
	
	private CharacterMood flipToMood;
	private CharacterMood originalMood;

	public PlayerSpriteAnimator(AdventurePlayer playerRef, SpriteRenderer rendererRef) {
		this.playerRef = playerRef;
		this.rendererRef = rendererRef;
		flipToMood = null;
		
		isLanding = false;
		jumpProgress = 0f;
		currentAnimationState = PlayerAnimationState.NONE;

		flipProgress = 0f;
		flipTowards = new Vector2(0f, 0f);
		defaultOrientation = new Vector2(-1f, 1f);
		
		timer = 0f;
		timerDuration = 0f;
	}

	@Override
	public void update(float deltaTime) {
		if (currentAnimationState == PlayerAnimationState.NONE) {
			return;
		}

		if (currentAnimationState == PlayerAnimationState.JUMPING
				|| currentAnimationState == PlayerAnimationState.FLIP_JUMPING) {
			handleJumpingState(deltaTime);
		}

		if (currentAnimationState == PlayerAnimationState.FLIP_JUMPING || currentAnimationState == PlayerAnimationState.FLIP) {
			handleFlipState(deltaTime);
		}
		
		if (currentAnimationState == PlayerAnimationState.SPRITE_PAUSE) {
			handlePauseState(deltaTime);
		}
	}
	
	private void handlePauseState(float deltaTime) {
		if (timer >= timerDuration) {
			playerRef.changeCharacterMood(originalMood);
			setNoneAnimation(true);
			return;
		}
		timer += deltaTime;
	}

	private void handleFlipState(float deltaTime) {
		if (flipProgress >= 1f) {
			rendererRef.setSpriteScale(flipTowards);
			defaultOrientation = new Vector2(flipTowards.x, flipTowards.y);
			setNoneAnimation(true);
			return;
		}
		
		if (flipProgress >= 0.5f && flipToMood != null && originalMood != flipToMood) {
			playerRef.changeCharacterMood(flipToMood);
			originalMood = flipToMood;
		}

		Vector2 currentScale;
		currentScale = Util.lerpVec2(defaultOrientation, flipTowards, flipProgress);

		rendererRef.setSpriteScale(currentScale);
		flipProgress += (deltaTime * (JUMP_SPEED / 2f));
	}

	private void handleJumpingState(float deltaTime) {
		// Finished the current jump/land progress.
		if (jumpProgress >= 1f) {
			// Landing, end animation.
			if (isLanding) {
				playerRef.setPosition(defaultPosition);
				setNoneAnimation(true);
				return;
			}
			// Was going up, switch to landing.
			isLanding = !isLanding;
			jumpProgress = 0f;
		}

		// If landing, lerp to move down, else upwards.
		float yPosition;
		if (isLanding) {
			yPosition = Util.lerp(jumpHeightLimit, defaultPosition.y, jumpProgress);
		} else {
			yPosition = Util.lerp(defaultPosition.y, jumpHeightLimit, jumpProgress);
		}

		Vector2 playerNewPosition = new Vector2(defaultPosition.x, yPosition);
		playerRef.setPosition(playerNewPosition);

		jumpProgress += (JUMP_SPEED * deltaTime);
	}

	private void setNoneAnimation(boolean invokeCallback) {
		setNoneAnimation(invokeCallback, false);
	}

	private void setNoneAnimation(boolean invokeCallback, boolean interruptFlag) {
		PlayerAnimationState cachedState = currentAnimationState;
		currentAnimationState = PlayerAnimationState.NONE;
		if (invokeCallback && !instanceCallbackInvoked) {
			instanceCallbackInvoked = true;
			
			if (callback != null) {
				callback.onPlayerAnimationDoneCallback(cachedState, interruptFlag);
			}
		}
	}

//region AnimationInterfaces
	
	public void invokeSpriteFlip(CharacterMood flipToMood, OnPlayerAnimationDone callback) {
		if (currentAnimationState != PlayerAnimationState.NONE) {
			playerRef.setPosition(defaultPosition);
			setNoneAnimation(true, true);
		}
		this.flipToMood = flipToMood;
		currentAnimationState = PlayerAnimationState.FLIP;
		originalMood = playerRef.getCharacterMood();

		invokeStateReset(callback);
	}
	
	public void invokeSpritePause(CharacterMood pauseInMood, float pauseDuration, OnPlayerAnimationDone callback) {
		if (currentAnimationState != PlayerAnimationState.NONE) {
			playerRef.setPosition(defaultPosition);
			setNoneAnimation(true, true);
		}
		
		currentAnimationState = PlayerAnimationState.SPRITE_PAUSE;
		invokeStateReset(callback);
		
		timerDuration = pauseDuration;
		originalMood = playerRef.getCharacterMood();
		playerRef.changeCharacterMood(pauseInMood);
	}

	public void invokeFlipJumping(OnPlayerAnimationDone callback) {
		if (currentAnimationState != PlayerAnimationState.NONE) {
			playerRef.setPosition(defaultPosition);
			setNoneAnimation(true, true);
		}
		flipToMood = null;
		currentAnimationState = PlayerAnimationState.FLIP_JUMPING;

		invokeStateReset(callback);
	}
	
	public void invokeFlipJumping(CharacterMood flipToMood, OnPlayerAnimationDone callback) {
		if (currentAnimationState != PlayerAnimationState.NONE) {
			playerRef.setPosition(defaultPosition);
			setNoneAnimation(true, true);
		}
		this.flipToMood = flipToMood;
		currentAnimationState = PlayerAnimationState.FLIP_JUMPING;

		invokeStateReset(callback);
	}

	public void invokeJump(OnPlayerAnimationDone callback) {
		if (currentAnimationState != PlayerAnimationState.NONE) {
			// end animation by interrupting
			playerRef.setPosition(defaultPosition);
			setNoneAnimation(true, true);
		}

		currentAnimationState = PlayerAnimationState.JUMPING;
		invokeStateReset(callback);
	}
	
	private void interruptCurrentAnimation() {
		if (currentAnimationState == PlayerAnimationState.JUMPING || currentAnimationState == PlayerAnimationState.FLIP_JUMPING) {
			playerRef.setPosition(defaultPosition);
		}
		
		if (currentAnimationState == PlayerAnimationState.FLIP_JUMPING) {
			/// TODO
		}
		
		if (currentAnimationState != PlayerAnimationState.NONE) {
			setNoneAnimation(true, true);
		}

	}

	private void invokeStateReset(OnPlayerAnimationDone callback) {
		jumpProgress = 0f;
		isLanding = false;
		defaultPosition = playerRef.getDefaultPosition();
		jumpHeightLimit = defaultPosition.y + JUMP_MAX_HEIGHT;
		
		this.callback = callback;
		instanceCallbackInvoked = false;
	
		flippingTowardsDefault = false;	
		defaultOrientation = rendererRef.getSpriteScale();
		float xTowards = defaultOrientation.x * -1f;
		flipTowards = new Vector2(xTowards, defaultOrientation.y);
		flipProgress = 0f;
		
		timer = 0f;
		timerDuration = 0f;
	}
//endregion

	public Vector2 getSpriteOrientation() {
		return rendererRef.getSpriteScale();
	}
	
	public boolean isFacingRight() {
		return rendererRef.getSpriteScale().x <= -0.9f;
	}
}
