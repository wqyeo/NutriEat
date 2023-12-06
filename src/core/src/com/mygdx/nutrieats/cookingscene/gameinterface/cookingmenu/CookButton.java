package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

/**
 * To execute cooking.
 *
 */
class CookButton extends GameObject {
	private static final Vector2 POSITION_OFFSET = new Vector2(-400f, -350f);
	private static final float BUTTON_IMAGE_SCALE = 0.3f;
	
	private static final Vector2 CLICKABLE_SIZE = new Vector2(102.5f, 52.5f);
	private static final Vector2 CLICKABLE_OFFSET = new Vector2(117.5f, 52f);
	
	private static final String ENABLED_BUTTON_TEXTURE_PATH = "sprites\\interface\\CookBtn.png";
	private static String enabledButtonTextureId;
	
	private static final String DISABLED_BUTTON_TEXTURE_PATH = "sprites\\interface\\CookBtn_Disabled.png";
	private static String disabledButtonTextureId;
	
	private CookingMenu cookingMenu;
	private Image buttonImage;
	
	private boolean enabled;
	
	public CookButton(CookingMenu cookingMenu) {
		super("Start cooking button");
		this.cookingMenu = cookingMenu;
		
		if (enabledButtonTextureId == null) {
			enabledButtonTextureId = cookingMenu.getSceneManager().createTexture(ENABLED_BUTTON_TEXTURE_PATH);
		}
		if (disabledButtonTextureId == null) {
			disabledButtonTextureId = cookingMenu.getSceneManager().createTexture(DISABLED_BUTTON_TEXTURE_PATH);
		}
		
		createButtonImage();
		createClickableComponent();
		
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, cookingMenu.getSceneManager().getScreenSize());
		this.setPosition(finalPosition);
		cookingMenu.getSceneManager().getScene().addGameObjectToRoot(this);
	}
	
	private void createButtonImage() {
		Sprite btnSprite = cookingMenu.getSceneManager().createSpriteByTextureId(disabledButtonTextureId);
		buttonImage = new Image(btnSprite);
		buttonImage.setSpriteScale(BUTTON_IMAGE_SCALE);
		this.addComponent(buttonImage);
		enabled = true;
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICKABLE_SIZE);
		clickable.setOffset(CLICKABLE_OFFSET);
		clickable.addOnClickListener(new OnButtonClicked());
		this.addComponent(clickable);
	}
	
	public void setState(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			buttonImage.changeTexture(enabledButtonTextureId);
		} else {
			buttonImage.changeTexture(disabledButtonTextureId);
		}
	}
	
	private class OnButtonClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			if (!enabled) {
				return;
			}
			
			// TODO
			cookingMenu.startCooking();
		}
		
	}
}
