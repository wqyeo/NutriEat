package com.mygdx.nutrieats.cookingscene.gameinterface.shoppingmenu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

class ShopMenuBackground extends GameObject {
	private static final int RENDER_LAYER = 6;
	private static final float IMAGE_SCALE = 0.7f;
	private static final String SPRITE_TEXTURE_LOCATION = "sprites\\interface\\shopBg.png";
	private static final Vector2 POSITION_OFFSET = new Vector2(-850f, -570f);
	
	private static String textureId;
	
	public ShopMenuBackground(ShoppingMenu shopMenu) {
		super("Shop Menu Background");
		
		if (textureId == null) {
			textureId = shopMenu.createTextureId(SPRITE_TEXTURE_LOCATION);
		}
		createImageComponent(shopMenu);
		
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, shopMenu.getInterfaceManager().getScreenSize());
		this.setPosition(finalPosition);
		shopMenu.getScene().addGameObjectToRoot(this);
	}
	
	private void createImageComponent(ShoppingMenu shopMenu) {
		Sprite sprite = shopMenu.getSpriteByTextureId(textureId);
		Image image = new Image(sprite);
		image.setSpriteScale(IMAGE_SCALE);
		image.setRenderLayer(RENDER_LAYER);
		this.addComponent(image);
	}
}
