package com.mygdx.nutrieats.adventurescene.entities;

public interface OnPlayerAnimationDone {

	/**
	 * Callback for when player animation is done.
	 * @param animationState The animation is was performing.
	 * @param isInterrupted True if this callback is invoked because the animation is interrupted, not because it was done.
	 */
	public void onPlayerAnimationDoneCallback(PlayerAnimationState animationState, boolean isInterrupted);
}
