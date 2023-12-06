package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

/**
 * When the 'cook' button  is press. An animation will play where all the ingredients gets launched into the cooking pot.
 * This class helps with the animation.
 * @author syaf
 *
 */
public class LaunchedItem extends GameObject {
	private static final float GRAVITY = -160;
	private static final float SPEED = 400;
	
	private OnItemLaunchedComplete callback;
	private Vector2 initialVelocity;
	
	private Vector2 endPosition;
	private Vector2 startPosition;
	private float currentTime;
	
	public LaunchedItem(Vector2 startPosition, Vector2 endPosition, Sprite sprite, OnItemLaunchedComplete callback, Vector2 imageTargetSize) {
		super("Launching Ingredient");
		this.endPosition = endPosition;
		this.callback = callback;
		this.startPosition = startPosition;
		
        float distance = startPosition.dst(endPosition);;
        float time = distance / SPEED;
        currentTime = 0f;

        float velocityX = (endPosition.y - startPosition.y + 0.5f * GRAVITY * time * time) / time;
        float velocityY = (endPosition.x - startPosition.x) / time;
        initialVelocity = new Vector2(velocityX, velocityY);
        
        createImageComponent(sprite, imageTargetSize);
        this.setPosition(startPosition);
	}
	
	private void createImageComponent(Sprite sprite, Vector2 imageTargetSize) {
		Image image = new Image(sprite);
		image.scaleSpriteToTargetSize(imageTargetSize);
		image.setRenderLayer(1);
		this.addComponent(image);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		if (Util.isClose(getPosition(), endPosition, 7f)) {
			
			if (callback != null) {
				callback.callback(this);
			}
			return;
		}
		
		Vector2 position = this.getPosition();
        float distance = (float) position.dst(endPosition);
        float time = distance / SPEED;
        float vx = (endPosition.x - position.x) / time;
        float vy = (endPosition.y - position.y - 0.5f * GRAVITY * time * time) / time;

        position.x += vx * deltaTime;
        position.y += vy * deltaTime - 0.5f * GRAVITY * deltaTime * deltaTime;
        this.setPosition(position);
	}
	
}
