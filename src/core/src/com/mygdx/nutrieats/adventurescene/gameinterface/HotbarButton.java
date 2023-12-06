package com.mygdx.nutrieats.adventurescene.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

/**
 * Button at bottom right of the game
 *
 */
class HotbarButton extends GameObject {

	private static final Vector2 CLICK_OFFSET = new Vector2(245f, 125f);
	private static final Vector2 CLICK_SIZE = new Vector2(168f, 84f);
	
	// TODO: If we have more hotbar buttons in the future,
	// can allow user to dynamically set indexed position.
	private static final Vector2 POSITION_OFFSET = new Vector2(110f, -460f);

	private AdventureGameInterfaceManager interfaceManager;
	private HotbarButtonType buttonType;

	HotbarButton(String name, float screenWidth, float screenHeight,  AdventureGameInterfaceManager interfaceManager, HotbarButtonType buttonType) {
		super(name);
		this.interfaceManager = interfaceManager;
		this.buttonType = buttonType;

		createClickableComponent();
		createImageComponent();
		
		Vector2 position = Util.addVec2(POSITION_OFFSET, new Vector2(screenWidth /2f, screenHeight /2f));
		this.setPosition(position);
		interfaceManager.getSceneReference().addGameObjectToRoot(this);
	}

	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICK_SIZE);
		clickable.addOnClickListener(new OnMenuButtonClicked());;
		clickable.setOffset(CLICK_OFFSET);
		this.addComponent(clickable);
	}
	
	private void createImageComponent() {
		Sprite buttonSprite = getSpriteByButtonType();

		Image buttonImage = new Image(buttonSprite);
		buttonImage.setSpriteScale(0.25f);
		this.addComponent(buttonImage);
	}
	
	private Sprite getSpriteByButtonType() {
		if (buttonType == HotbarButtonType.INVENTORY_DISPLAY) {
			return interfaceManager.getInventoryBtnSprite();
		}
		
		System.out.println("WARN: Forgot to implement 'getSpriteByButtonType' for hotbar");
		return null;
	}
	
	private class OnMenuButtonClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			if (buttonType == HotbarButtonType.INVENTORY_DISPLAY) {
				interfaceManager.triggerShowInventory();
			}
		}
		
		
	}
}
