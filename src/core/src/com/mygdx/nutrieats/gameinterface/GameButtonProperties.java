package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class GameButtonProperties {
	private Vector2 position;
	private Sprite sprite;
	private float spriteScale;
	private Vector2 clickArea;
	private Vector2 clickAreaOffset;
	private GameButtonClickedCallback callback;
	
	public GameButtonProperties(Vector2 position, Sprite sprite, float spriteScale, Vector2 clickArea, Vector2 clickAreaOffset,
			GameButtonClickedCallback callback) {
		this.sprite = sprite;
		this.clickArea = clickArea;
		this.clickAreaOffset = clickAreaOffset;
		this.callback = callback;
		this.spriteScale = spriteScale;
		this.position = position;
	}
	public Vector2 getPosition() {
		return position;
	}
	public float getSpriteScale() {
		return spriteScale;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public Vector2 getClickArea() {
		return clickArea;
	}
	public Vector2 getClickAreaOffset() {
		return clickAreaOffset;
	}
	public GameButtonClickedCallback getCallback() {
		return callback;
	}
}
