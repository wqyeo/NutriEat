package com.mygdx.nutrieats.adventurescene.entities.playerstate;

import java.util.Random;

import com.mygdx.nutrieats.adventurescene.AdventureAudioType;
import com.mygdx.nutrieats.adventurescene.TileType;
import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;
import com.mygdx.nutrieats.adventurescene.entities.OnPlayerAnimationDone;
import com.mygdx.nutrieats.adventurescene.entities.PlayerAnimationState;
import com.mygdx.nutrieats.cookingsystem.FoodTag;

/**
 * This state is used by the player to evaluate what to do on the tile they are currently on.
 * @author wqyeo
 *
 */
public class PlayerTileState implements PlayerStateMachine {
	
	private TileType tileType;
	private AdventurePlayer playerRef;
	
	public PlayerTileState(TileType tileType) {
		this.tileType = tileType;
	}

	@Override
	public void onStateEnter(AdventurePlayer playerRef) {
		this.playerRef = playerRef;
		
		// TODO: Other tile actions
		
		if (tileType == TileType.BACK_MOVE) {
			movePlayerTowardsRight(false);
			playerRef.getSceneManager().playSoundByType(AdventureAudioType.BACK_TRACK);
			return;
		}
		
		// For any other tile, make sure the player is facing right first.
		if (playerRef.getAnimator().isFacingRight() == false) {
			playerRef.getAnimator().invokeFlipJumping(new FlipJumpCallback(FlipJumpReason.FLIP_ALIGNMENT));
		} else {
			triggerHandleTileState();
		}
	}
	
	private void triggerHandleTileState() {
		// TODO: Play animation and switch to cooking scene
		// when hit end tile.
		FoodTag foodTag = fetchFoodTagByTileType(tileType);
		
		if (tileType == TileType.STAR){
			triggerCollectCoin();
		} else if (foodTag != null){
			playerRef.doCollectRandomFood(foodTag);
			playerRef.getSceneManager().playSoundByType(AdventureAudioType.ITEM_COLLECT);
		} else if (tileType == TileType.END) {
			triggerEndTile();
			playerRef.getSceneManager().playSoundByType(AdventureAudioType.STAGE_CLEAR);
		} else {
			playerRef.doMoveRoll(true);
		}
	}
	
	private void triggerEndTile() {
		playerRef.triggerEndTile();
	}
	
	private FoodTag fetchFoodTagByTileType(TileType tileType) {
		if (tileType == TileType.MEAT) {
			return FoodTag.MEAT;
		} else if (tileType == TileType.VEGETABLE) {
			return FoodTag.VEGETARIAN;
		} else if (tileType == TileType.WATER) {
			return FoodTag.AQUATIC;
		} else if (tileType == TileType.RANDOM) {
			FoodTag selections[] = {FoodTag.MEAT, FoodTag.VEGETARIAN, FoodTag.AQUATIC};
			
		    Random random = new Random();
		    int index = random.nextInt(selections.length);
		    return selections[index];
		}
		
		return null;
	}
	
	private void triggerCollectCoin() {
		playerRef.doCollectCoinRoll();
	}
	
	private void movePlayerTowardsRight(boolean towardsRight) {
		if (playerRef.getAnimator().isFacingRight() == towardsRight) {
			if (!towardsRight) {
				playerRef.doMoveRollAndStarTile(towardsRight);
			} else {
				playerRef.doMoveRoll(towardsRight);
			}
		} else {
			playerRef.getAnimator().invokeFlipJumping(new FlipJumpCallback());
		}
	}

	@Override
	public void update(float deltaTime, boolean overlappingClickSignal) {}

	@Override
	public void onStateExit() {
		
	}

	@Override
	public PlayerState getPlayerState() {
		return PlayerState.TILE_EVALUATE;
	}
	
	private enum FlipJumpReason {
		BACKSTEP,
		FLIP_ALIGNMENT // Ensure that player is facing right first before doing anything.
	}

	private class FlipJumpCallback implements OnPlayerAnimationDone {
		private FlipJumpReason flipReason;
		
		public FlipJumpCallback() {
			flipReason = FlipJumpReason.BACKSTEP;
		}
		
		public FlipJumpCallback(FlipJumpReason flipReason) {
			this.flipReason = flipReason;
		}
		
		@Override
		public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
			if (flipReason == FlipJumpReason.BACKSTEP) {
				playerRef.doMoveRollAndStarTile(false);
			} else {
				triggerHandleTileState();
			}
		}
		
	}
}
