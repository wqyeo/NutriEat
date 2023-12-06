package com.mygdx.nutrieats.cookingscene.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

class EndDayButton extends GameObject {
	private static final Vector2 POSITION_OFFSET = new Vector2(-300f, -650f);
	private static final float BUTTON_IMAGE_SCALE = 0.3f;
	
	private static final Vector2 CLICKABLE_SIZE = new Vector2(102.5f, 52.5f);
	private static final Vector2 CLICKABLE_OFFSET = new Vector2(117.5f, 52f);
	
	private static final String BUTTON_TEXTURE_PATH = "sprites\\interface\\EndDayBtn.png";
	private static String buttonTextureId;
	
	private CookingGameInterfaceManager interfaceManager;
	private Image buttonImage;
	
	public EndDayButton(CookingGameInterfaceManager interfaceManager) {
		super("Start cooking button");
		this.interfaceManager = interfaceManager;
		
		if (buttonTextureId == null) {
			buttonTextureId = interfaceManager.createTextureId(BUTTON_TEXTURE_PATH);
		}
		createButtonImage();
		createClickableComponent();
		
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, interfaceManager.getScreenSize());
		this.setPosition(finalPosition);
		interfaceManager.getSceneReference().addGameObjectToRoot(this);
	}
	
	private void createButtonImage() {
		Sprite btnSprite = interfaceManager.getSpriteByTextureId(buttonTextureId);
		buttonImage = new Image(btnSprite);
		buttonImage.setSpriteScale(BUTTON_IMAGE_SCALE);
		this.addComponent(buttonImage);
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICKABLE_SIZE);
		clickable.setOffset(CLICKABLE_OFFSET);
		clickable.addOnClickListener(new OnButtonClicked());
		this.addComponent(clickable);
	}
	
	private class OnButtonClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			interfaceManager.endDay();
		}
	}
}
