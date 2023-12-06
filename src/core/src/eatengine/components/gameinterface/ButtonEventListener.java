package eatengine.components.gameinterface;

public interface ButtonEventListener {
	public void onButtonClicked(Button button);
	
	/**
	 * Triggered when the button is hovered over
	 * @param button
	 * @param enterHover True if the button is hovered over. False if button is hovered off.
	 */
	public void onButtonHover(Button button, boolean enterHover);
}
