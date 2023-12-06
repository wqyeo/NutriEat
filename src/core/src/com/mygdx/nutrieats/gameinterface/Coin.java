package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.components.gameinterface.Image;

public class Coin extends GameObject {

	public Coin(String name, Sprite coinSprite, Vector2 scale, Vector2 position) {
		super(name);
		
		createCoinImage(coinSprite, scale);
		this.setPosition(position);
	}
	
	private void createCoinImage(Sprite coinSprite, Vector2 scale) {
		coinSprite.setScale(scale.x, scale.y);
		Image image = new Image(coinSprite);
		image.setRenderLayer(999);
		this.addComponent(image);
	}
	
}
