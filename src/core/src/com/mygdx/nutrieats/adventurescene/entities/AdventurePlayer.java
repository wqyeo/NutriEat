package com.mygdx.nutrieats.adventurescene.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.adventurescene.AdventureSceneManager;
import com.mygdx.nutrieats.adventurescene.Dice;
import com.mygdx.nutrieats.adventurescene.OnDiceRolled;
import com.mygdx.nutrieats.adventurescene.entities.playerstate.PlayerMoveState;
import com.mygdx.nutrieats.adventurescene.entities.playerstate.PlayerRollState;
import com.mygdx.nutrieats.adventurescene.entities.playerstate.PlayerStateMachine;
import com.mygdx.nutrieats.adventurescene.entities.playerstate.PlayerTileState;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.components.SpriteRenderer;
import com.mygdx.nutrieats.adventurescene.AdventureWorld;

public class AdventurePlayer extends GameObject {

	private static final Map<CharacterMood, String> PLAYER_TEXTURE_LOCATIONS_MAPPING;

	static {
		PLAYER_TEXTURE_LOCATIONS_MAPPING = new HashMap<CharacterMood, String>();
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.IDLE, "sprites\\player\\chris\\chrisIdle.png");
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.HAPPY, "sprites\\player\\chris\\chrisHappy.png");
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.ROLL, "sprites\\player\\chris\\chrisRoll.png");
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.HURT, "sprites\\player\\chris\\chrisHurt.png");
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.FALLEN, "sprites\\player\\chris\\chrisFallen.png");
		PLAYER_TEXTURE_LOCATIONS_MAPPING.put(CharacterMood.ATTACK, "sprites\\player\\chris\\chrisAttack.png");
	}

	private static final Vector2 POSITION_OFFSET = new Vector2(-400f, -50f);

	private Dice dice;
	private AdventureSceneManager sceneManagerReference;
	private AdventureWorld worldReference;

	private Map<CharacterMood, String> playerTextureIdMapping;

	/**
		*	Decides what sprite to show on the player.
		*/
	private CharacterMood currentPlayerMood;

	private Vector2 defaultPosition;
	private PlayerStateMachine currentStateMachine;

	private PlayerSpriteAnimator animator;
	private SpriteRenderer spriteRenderer;
	
	private boolean overlappingClickSignal;
	private boolean isViewingInterface;

	public AdventurePlayer(Dice dice, AdventureSceneManager sceneManagerReference, AdventureWorld worldReference) {
		super("Adv-Player");
		this.dice = dice;
		dice.setVisible(false);
		this.sceneManagerReference = sceneManagerReference;
		this.worldReference = worldReference;

		defaultPosition = new Vector2((sceneManagerReference.getScreenWidth() / 2f) + POSITION_OFFSET.x, (sceneManagerReference.getScreenHeight() / 2f) + POSITION_OFFSET.y);
		setPosition(defaultPosition);

		createTextureIds();
		createSpriteRenderer();

		animator = new PlayerSpriteAnimator(this, spriteRenderer);
		this.addComponent(animator);

		doMoveRoll(true);
		
		isViewingInterface = false;
		overlappingClickSignal = false;
	}

	private void createTextureIds() {
		playerTextureIdMapping = new HashMap<CharacterMood, String>();

		for (Entry<CharacterMood, String> entry : PLAYER_TEXTURE_LOCATIONS_MAPPING.entrySet()) {
			String textureId = sceneManagerReference.createTextureId(entry.getValue());
			playerTextureIdMapping.put(entry.getKey(), textureId);
		}
	}

	private void createSpriteRenderer() {
		// Should start at idle.
		Sprite sprite = sceneManagerReference.requestSpriteByTexture(playerTextureIdMapping.get(CharacterMood.IDLE));
		sprite.setScale(-1f,1f);

		spriteRenderer = new SpriteRenderer(sprite);
		// Render above everyone else.
		spriteRenderer.setRenderLayer(1000);
		this.addComponent(spriteRenderer);
	}

	@Override
	public void onUpdate(float deltaTime) {
		if (currentStateMachine != null) {
			currentStateMachine.update(deltaTime, overlappingClickSignal);
		}
	}

	public PlayerSpriteAnimator getAnimator(){
		return animator;
	}

//region StateMachine interfaces
	
	public int getTileIndex() {
		return worldReference.getCurrentWorldStep();
	}
	
	public AdventureSceneManager getSceneManager() {
		return sceneManagerReference;
	}
	
	public void convertTileToStar(int index) {
		worldReference.convertTileToStar(index);
	}
	
	public void doCollectRandomFood(FoodTag randomFoodType) {
		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		currentStateMachine = null;
		
		Item selectedItem = sceneManagerReference.appendRandomItemToInventoryByTag(randomFoodType);
		// Callback: When the 'happy' animation is done playing, move.
		final OnPlayerAnimationDone onSpritePauseAnimationDone = new OnPlayerAnimationDone() {
			@Override
			public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
				doMoveRoll(true);
			}
		};
		
		animator.invokeSpritePause(CharacterMood.HAPPY, 1.2f, onSpritePauseAnimationDone);
		// TODO: Invoke UI change to show the item player gotten
	}
	
	/**
	 * Requests the player to perform a roll to collect coins.
	 */
	public void doCollectCoinRoll() {
		dice.setVisible(false);

		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		
		// The player will roll for coin amount,
		// then play coin collecting animation
		// after coin collecting animation finishes, move the player
		
		// Callback: When the 'happy' animation is done playing, move.
		final OnPlayerAnimationDone onSpritePauseAnimationDone = new OnPlayerAnimationDone() {
			@Override
			public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
				doMoveRoll(true);
			}
		};
		
		// Callback: When the dice roll is confirmed, collect coins by given amount.
		OnDiceRolled onDiceRollConfirmeDiceRolled = new OnDiceRolled() {
			@Override
			public void onDiceRolledCallback(int diceValue) {
				sceneManagerReference.triggerAddCoins(diceValue);
				animator.invokeSpritePause(CharacterMood.HAPPY, 1.2f, onSpritePauseAnimationDone);
				currentStateMachine = null;
			}
		};
		
		currentStateMachine = new PlayerRollState(onDiceRollConfirmeDiceRolled);
		currentStateMachine.onStateEnter(this);
	}
	
	/**
	 * Requests the player to start performing dice roll for movement.
	 * 
	 */
	public void doMoveRollAndStarTile(final boolean forwardMovement) {
		dice.setVisible(false);
		final int cachedIndex = worldReference.getCurrentWorldStep();

		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		
		// Callback: When the dice roll is confirmed, move the player by the amount.
		OnDiceRolled onDiceRollConfirmeDiceRolled = new OnDiceRolled() {
			@Override
			public void onDiceRolledCallback(int diceValue) {
				moveBy(diceValue, forwardMovement);
				worldReference.convertTileToStar(cachedIndex);
			}
		};
		// Set to roll, state with given callback.
		currentStateMachine = new PlayerRollState(onDiceRollConfirmeDiceRolled);
		currentStateMachine.onStateEnter(this);
	}


	/**
	 * Requests the player to start performing dice roll for movement.
	 */
	public void doMoveRoll(final boolean forwardMovement) {
		dice.setVisible(false);

		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		
		// Callback: When the dice roll is confirmed, move the player by the amount.
		OnDiceRolled onDiceRollConfirmeDiceRolled = new OnDiceRolled() {
			@Override
			public void onDiceRolledCallback(int diceValue) {
				moveBy(diceValue, forwardMovement);
			}
		};
		// Set to roll, state with given callback.
		currentStateMachine = new PlayerRollState(onDiceRollConfirmeDiceRolled);
		currentStateMachine.onStateEnter(this);
	}

	
	/**
	 * Requests the player to move by the given amount
	 * @param moveAmount
	 */
	public void moveBy(int moveAmount, boolean forwardMovement) {
		dice.setVisible(false);
		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		currentStateMachine = new PlayerMoveState(moveAmount, worldReference, forwardMovement);
		currentStateMachine.onStateEnter(this);
	}


	public Vector2 getDefaultPosition() {
		return new Vector2(defaultPosition.x, defaultPosition.y);
	}

	/*
	* Do the respective action based on the current tile the player is currently on.
	*/
	public void doTileAction() {
		if (currentStateMachine != null) {
			currentStateMachine.onStateExit();
		}
		
		currentStateMachine = new PlayerTileState(worldReference.getCurrentWorldTile());
		currentStateMachine.onStateEnter(this);
	}

	/**
	 * Change the mood of the character, affects the sprite.
	 * @param newMood
	 */
	public void changeCharacterMood(CharacterMood newMood) {
		spriteRenderer.changeTexture(playerTextureIdMapping.get(newMood));
		currentPlayerMood = newMood;
		spriteRenderer.setSpriteScale(animator.getSpriteOrientation().x, animator.getSpriteOrientation().y);
	}

	public CharacterMood getCharacterMood() {
		return currentPlayerMood;
	}

	public void requestRollDice(OnDiceRolled callback) {
		dice.rollDice(callback);
	}

	/**
	 * Signal that the overlapping click has been resolved.
	 */
	public void signalOverlapResolve() {
		overlappingClickSignal = false;
	}
	
	public void triggerEndTile() {
		final OnPlayerAnimationDone onPauseDone = new OnPlayerAnimationDone() {
			@Override
			public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
				sceneManagerReference.triggerToCookingScene();
			}
		};
		
		final OnPlayerAnimationDone onFlipDone2 = new OnPlayerAnimationDone() {
			@Override
			public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
				animator.invokeSpritePause(CharacterMood.HAPPY, 1f, onPauseDone);
			}
		};
		
		final OnPlayerAnimationDone onFlipDone1 = new OnPlayerAnimationDone() {
			@Override
			public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted) {
				animator.invokeSpriteFlip(CharacterMood.HAPPY, onFlipDone2);
			}
		};
		
		animator.invokeSpriteFlip(CharacterMood.HAPPY, onFlipDone1);
	}
	
//endregion
	
	public void signalButtonClicked() {
		overlappingClickSignal = true;
	}
	
	public void signalInInterface(boolean inInterface) {
		isViewingInterface = inInterface;
	}
	
	public boolean isViewingInterface() {
		return isViewingInterface;
	}
}
