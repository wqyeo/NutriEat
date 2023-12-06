package com.mygdx.nutrieats.adventurescene.entities.playerstate;

import com.badlogic.gdx.math.Vector2;

import eatengine.Util;
import com.mygdx.nutrieats.adventurescene.AdventureWorld;
import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;
import com.mygdx.nutrieats.adventurescene.entities.CharacterMood;
import com.mygdx.nutrieats.adventurescene.entities.OnPlayerAnimationDone;
import com.mygdx.nutrieats.adventurescene.entities.PlayerAnimationState;
import com.mygdx.nutrieats.adventurescene.entities.PlayerSpriteAnimator;

/**
 * NOTE: The player doesn't actually moves. The player performs a jump animation and the map moves, giving the illusion of movement.
 * @author wqyeo
 *
 */
public class PlayerMoveState implements PlayerStateMachine {
	private AdventurePlayer playerRef;
	private AdventureWorld worldRef;

	private int moveAmountLeft;

	private boolean movingForward;

	public PlayerMoveState(int moveAmount, AdventureWorld worldReference) {
		moveAmountLeft = moveAmount;
		worldRef = worldReference;
		movingForward = true;
	}

	public PlayerMoveState(int moveAmount, AdventureWorld worldReference, boolean moveForward) {
		moveAmountLeft = moveAmount;
		worldRef = worldReference;
		movingForward = moveForward;
	}

	@Override
	public void onStateEnter(AdventurePlayer playerRef) {
		this.playerRef = playerRef;
		playerRef.changeCharacterMood(CharacterMood.IDLE);
		
		playerRef.getAnimator().invokeJump(new JumpAnimationDone());
		worldRef.stepWorld(PlayerSpriteAnimator.JUMP_SPEED, movingForward);
		--moveAmountLeft;
	}

	@Override
	public void update(float deltaTime, boolean overlappingClickSignal) {}

	@Override
	public void onStateExit() {

	}

	@Override
	public PlayerState getPlayerState() {
		return PlayerState.MOVING;
	}


	private class JumpAnimationDone implements OnPlayerAnimationDone {

		public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean interrupted){
			if (animationState == PlayerAnimationState.JUMPING){
				boolean move = moveAmountLeft > 0;
				if (move){
					move = worldRef.stepWorld(PlayerSpriteAnimator.JUMP_SPEED, movingForward);
				}
				
				if (move) {
					playerRef.getAnimator().invokeJump(new JumpAnimationDone());
				} else {
					// Either hit move quota, or hit end of world.
					playerRef.doTileAction();
				}
				--moveAmountLeft;
			}
		}
	}
}
