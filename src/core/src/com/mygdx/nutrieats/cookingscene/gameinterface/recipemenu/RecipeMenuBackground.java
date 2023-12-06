package com.mygdx.nutrieats.cookingscene.gameinterface.recipemenu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

public class RecipeMenuBackground extends GameObject {
	
	private static final int RENDER_LAYER = 6;
	private static final float IMAGE_SCALE = 0.7f;
	private static final String SPRITE_TEXTURE_LOCATION = "sprites\\interface\\InventoryBag.png";
	private static final Vector2 POSITION_OFFSET = new Vector2(-1050f, -570f);
	
	private static String textureId;
	
	public RecipeMenuBackground(CookingGameInterfaceManager interfaceManager) {
		super("Recipe Menu Background");
		
		if (textureId == null) {
			textureId = interfaceManager.createTextureId(SPRITE_TEXTURE_LOCATION);
		}
		createImageComponent(interfaceManager);
		
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, interfaceManager.getScreenSize());
		this.setPosition(finalPosition);
		interfaceManager.addObjectToScene(this);
	}
	
	private void createImageComponent(CookingGameInterfaceManager interfaceManager) {
		Sprite sprite = interfaceManager.getSpriteByTextureId(textureId);
		Image image = new Image(sprite);
		image.setSpriteScale(IMAGE_SCALE);
		image.setRenderLayer(RENDER_LAYER);
		this.addComponent(image);
	}
	
}
