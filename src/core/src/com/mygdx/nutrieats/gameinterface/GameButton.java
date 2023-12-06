package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

public class GameButton extends GameObject {
	
	private GameButtonClickedCallback callback;
	
	public GameButton(String name, GameButtonProperties properties) {
		super(name);
		this.callback = properties.getCallback();
		
		createImageComponent(properties.getSprite(), properties.getSpriteScale());
		createClickableComponent(properties.getClickArea(), properties.getClickAreaOffset());
		this.setPosition(properties.getPosition());
	}
	
	private void createImageComponent(Sprite sprite, float spriteScale) {
		Image image = new Image(sprite);
		image.setSpriteScale(spriteScale);
		this.addComponent(image);
	}
	
	private void createClickableComponent(Vector2 clickSize, Vector2 clickOffset) {
		Clickable clickable = new Clickable(clickSize);
		clickable.setOffset(clickOffset);
		clickable.addOnClickListener(new ClickableCallback(this));
		this.addComponent(clickable);
	}
	
	private class ClickableCallback implements ClickEventListener{
		
		private GameButton gameButton;
		
		private ClickableCallback(GameButton gameButton) {
			this.gameButton = gameButton;
		}

		@Override
		public void onClickEvent(Clickable clickedObject) {
			callback.invoke(gameButton);
		}
		
	}
	
}
