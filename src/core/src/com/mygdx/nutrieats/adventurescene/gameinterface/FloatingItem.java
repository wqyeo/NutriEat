package com.mygdx.nutrieats.adventurescene.gameinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

/**
 * Used as animation when player collects an item.
 * Will float upwards and go to transparent.s
 * @author wqyeo
 *
 */
class FloatingItem extends GameObject {
	
	private static final Vector2 SPRITE_TARGET_SIZE = new Vector2(32f, 32f);
	
	private static final float FLOAT_SPEED = 125f;
	private static final float EXIST_DURATION = 1f;
	
	/**
	 * How long this item has existed.
	 */
	private float timer;
	
	private Image itemImage;
	
	public FloatingItem(String name, Sprite itemSprite, Vector2 position) {
		super(name);
		
		timer = 0f;
		createImageComponent(itemSprite);
		this.setPosition(position);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		if (timer >= EXIST_DURATION) {
			this.getSceneReference().removeFromRoot(this, true);
			this.isActive = false;
			return;
		}
		
		float alphaProgress = Util.lerp(1, 0, timer / EXIST_DURATION);
		itemImage.setAlpha(alphaProgress);
		
		Vector2 newPosition = Util.addVec2(getPosition(), new Vector2(0f, FLOAT_SPEED * deltaTime));
		this.setPosition(newPosition);
		
		timer += deltaTime;
	}
	
	private void createImageComponent(Sprite sprite) {
		scaleSpriteToTargetSize(sprite);
		itemImage = new Image(sprite);
		
		this.addComponent(itemImage);
	}
	
	public static void scaleSpriteToTargetSize(Sprite sprite) {
		Texture texture = sprite.getTexture();
		float scaleX = SPRITE_TARGET_SIZE.x / texture.getWidth();
		float scaleY = SPRITE_TARGET_SIZE.y / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
}
