package com.mygdx.nutrieats.mainmenuscene;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

public class MenuButton extends GameObject {

	private static final String NEW_GAME_BUTTON_TEXTURE_PATH = "sprites\\interface\\NewGameBtn.png";
	private static final String CONTINUE_BUTTON_TEXTURE_PATH = "sprites\\interface\\ContinueBtn.png";
	private static final String EXIT_BUTTON_TEXTURE_PATH = "sprites\\interface\\ExitBtn.png";

	private static final Vector2 CLICK_OFFSET = new Vector2(245f, 125f);
	private static final Vector2 CLICK_SIZE = new Vector2(168f, 84f);

	private MenuButtonType buttonType;
	private MainMenuSceneManager sceneManagerRef;

	public MenuButton(String name, MainMenuSceneManager sceneManagerRef, MenuButtonType buttonType) {
		super(name);
		this.sceneManagerRef = sceneManagerRef;
		this.buttonType = buttonType;

		createClickableComponent();
		createImageComponent();
	}

	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICK_SIZE);
		clickable.addOnClickListener(new OnMenuButtonClicked());;
		clickable.setOffset(CLICK_OFFSET);
		this.addComponent(clickable);
	}
	
	private void createImageComponent() {
		String textureIdString = sceneManagerRef.createTextureId(getButtonTexturePathByButtonType(buttonType));
		Sprite buttonSprite = sceneManagerRef.requestSpriteByTexture(textureIdString);

		Image buttonImage = new Image(buttonSprite);
		buttonImage.setSpriteScale(0.25f);
		
		this.addComponent(buttonImage);
	}
	
	private static String getButtonTexturePathByButtonType(MenuButtonType buttonType) {
		if (buttonType == MenuButtonType.CONTINUE) {
			return CONTINUE_BUTTON_TEXTURE_PATH;
		} else if (buttonType == MenuButtonType.EXIT) {
			return EXIT_BUTTON_TEXTURE_PATH;
		} else {
			return NEW_GAME_BUTTON_TEXTURE_PATH;
		}
	}
	
	private class OnMenuButtonClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			if (buttonType == MenuButtonType.NEW_GAME) {
				sceneManagerRef.triggerNewGame();
			} else if (buttonType == MenuButtonType.CONTINUE) {
				// TODO
			} else if (buttonType == MenuButtonType.EXIT) {
				sceneManagerRef.triggerExitGame();
			}
		}
		
		
	}
}
