package com.mygdx.nutrieats.adventurescene.entities.playerstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.mygdx.nutrieats.adventurescene.OnDiceRolled;
import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;
import com.mygdx.nutrieats.adventurescene.entities.CharacterMood;

public class PlayerRollState implements PlayerStateMachine {

	private enum DiceState {
		WAITING_ROLL, // wait for user to roll
		ROLLING, // is currently rolling
		CONFIRM_ROLL // roll is done, waiting for user to confirm.
	}

	/**
	 * Used to constantly change the mood between idle/roll, to indicate to the player to roll.
	 */
	private static final float MOOD_CHANGE_INTERVAL = 0.5f;
	private float moodChangeTimer;

	private int rolledAmount;
	private OnDiceRolled diceRollCallback;

	private DiceState currentDiceState;

	private AdventurePlayer playerRef;
	
	private OnDiceRolled afterDiceRolledConfirmation;
	private boolean actionRegister;

	public PlayerRollState(OnDiceRolled afterRollConfirmationCallback) {
		diceRollCallback = new OnDiceRolledCallback(this);
		moodChangeTimer = 0f;
		currentDiceState = DiceState.WAITING_ROLL;
		rolledAmount = 0;
		afterDiceRolledConfirmation= afterRollConfirmationCallback;
		actionRegister = false;
	}

	@Override
	public void onStateEnter(AdventurePlayer playerRef) {
		this.playerRef = playerRef;
	}

	@Override
	public void update(float deltaTime, boolean overlappingClickSignal) {
		// Waiting for dice's callback.
		if (currentDiceState == DiceState.ROLLING) {
			return;
		}

		if (currentDiceState == DiceState.WAITING_ROLL){
			handleAwaitingRoll(deltaTime, overlappingClickSignal);
    	}

		if (currentDiceState == DiceState.CONFIRM_ROLL) {
			awaitRollConfirmation(overlappingClickSignal);
		}
	}

	private void awaitRollConfirmation(boolean overlappingClickSignal) {
    	if (tryEatActionRegister(overlappingClickSignal)) {
			if (afterDiceRolledConfirmation == null) {
				System.out.println("Player Rolling State: Forgot to set callback after roll confirmation!");
			} else {
				afterDiceRolledConfirmation.onDiceRolledCallback(rolledAmount);
			}
    		return;
    	}
    	
    	if (playerLeftClicked(overlappingClickSignal)) {
    		actionRegister = true;
    	}
	}

	private void handleAwaitingRoll(float deltaTime, boolean overlappingClickSignal){
	  moodChangeTimer += deltaTime;

	  	// Used to show the animation of the character about to perform a roll.
    	if (moodChangeTimer >= MOOD_CHANGE_INTERVAL){
    		CharacterMood currentMood = playerRef.getCharacterMood();
    		if (currentMood == CharacterMood.IDLE) {
    			playerRef.changeCharacterMood(CharacterMood.ROLL);
    		} else {
    			playerRef.changeCharacterMood(CharacterMood.IDLE);
    		}
    		moodChangeTimer = 0f;
    	}
    	
    	if (tryEatActionRegister(overlappingClickSignal)) {
    		currentDiceState = DiceState.ROLLING;
    		playerRef.changeCharacterMood(CharacterMood.ROLL);
    		playerRef.requestRollDice(diceRollCallback);
    		return;
    	}
    	
    	// Player input roll button, lock animation and wait for dice's callback.
    	if (playerLeftClicked(overlappingClickSignal)) {
    		actionRegister = true;
    	}
    
	}
	
	private boolean tryEatActionRegister(boolean overlappingClickSignal) {
    	if (overlappingClickSignal) {
    		playerRef.signalOverlapResolve();
    		return false;
    	}
		
    	if (actionRegister && !overlappingClickSignal) {
    		actionRegister = false;
    		return true;
    	}
    	
    	return false;
	}
	
	private boolean playerLeftClicked(boolean overlappingClickSignal) {
    	if (playerRef.isViewingInterface()) {
    		return false;
    	}
    	
    	if (overlappingClickSignal) {
    		return false;
    	}
    	
    	if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
    		return true; 
    	}
    	
    	return false;
	}

	@Override
	public void onStateExit() {

	}


	private class OnDiceRolledCallback extends OnDiceRolled {

		private PlayerRollState rollStateRef;

		public OnDiceRolledCallback(PlayerRollState rollState) {
			rollStateRef = rollState;
		}

		@Override
		public void onDiceRolledCallback(int diceValue) {
			rollStateRef.currentDiceState = DiceState.CONFIRM_ROLL;
			rollStateRef.rolledAmount = diceValue;
		}

	}


	@Override
	public PlayerState getPlayerState() {
		return PlayerState.ROLLING;
	}
}
