package com.mygdx.nutrieats.adventurescene.entities.playerstate;

import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;

public interface PlayerStateMachine {
	public void onStateEnter(AdventurePlayer playerRef);
	
	/**
	 * 
	 * @param deltaTime
	 * @param overlappingClickSignal True if a click was detected for other buttons; (True if states should accept button inputs)
	 */
	public void update(float deltaTime, boolean overlappingClickSignal);
	
	public void onStateExit();
	
	public PlayerState getPlayerState();
}
