package com.mygdx.nutrieats.adventurescene.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

class InventoryBag extends GameObject {
	private static final float IMAGE_SCALE = 0.37f;
	private static final Vector2 INVENTORY_BAG_PLACEMENT_OFFSET = new Vector2(-950f, -600f);
	
	public InventoryBag(float screenWidth, float screenHeight, Sprite sprite) {
		super("Inventory Bag");
		
		createImageComponent(sprite);
		Vector2 finalPosition = Util.addVec2(INVENTORY_BAG_PLACEMENT_OFFSET, new Vector2(screenWidth, screenHeight));
		this.setPosition(finalPosition);
	}
	
	private void createImageComponent(Sprite sprite) {
		Image image = new Image(sprite);
		image.setSpriteScale(IMAGE_SCALE);
		
		// Items should overlay over the bag
		image.setRenderLayer(-100);
		this.addComponent(image);
	}
}
