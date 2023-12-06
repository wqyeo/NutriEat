package com.mygdx.nutrieats.cookingscene.gameinterface.shoppingmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;
import eatengine.components.gameinterface.Text;

class ShopItem extends GameObject {
	
	private static final int IMAGE_RENDER_LAYER = 7;
	private static final int TEXT_RENDER_LAYER = 8;
	
	private static final Vector2 IMAGE_TARGET_SIZE = new Vector2(32f, 32f);
	private static final Vector2 PRODUCT_IMAGE_OFFSET = new Vector2(2f, 2f);
	
	private static final Vector2 TITLE_TEXT_OFFSET = new Vector2(-0f, 45f);
	private static final float TITLE_TEXT_SCALE = 0.55f;
	
	private static final Vector2 TAGS_TEXT_OFFSET = new Vector2(0f, -6f);
	private static final float TAGS_TEXT_SCALE = 0.45f;
	
	private static final Vector2 CLICKABLE_SIZE = new Vector2(170f, 70f);
	private static final Vector2 CLICKABLE_OFFSET = new Vector2(0f, -25f);
	
	private ShoppingMenu shopMenu;
	private Item item;
	
	public ShopItem(Item item, Vector2 startPosition, ShoppingMenu shopMenu) {
		super("Purchase_Item_" + item.getId());
		this.shopMenu = shopMenu;
		this.item = item;
		
		// Title text.
		createTextComponent(item.getName(), TITLE_TEXT_OFFSET, TITLE_TEXT_SCALE);
		
		// Display cost
		String content = "Cost: " + String.valueOf(item.getCost());
		createTextComponent(content, TAGS_TEXT_OFFSET, TAGS_TEXT_SCALE);
		
		createProductFoodImage();
		createClickableComponent();
		
		this.setPosition(startPosition);
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICKABLE_SIZE);
		clickable.setOffset(CLICKABLE_OFFSET);
		clickable.addOnClickListener(new ClickableClicked());
		this.addComponent(clickable);
	}
	
	private void createProductFoodImage() {
		Sprite sprite = shopMenu.getSpriteByTextureId(item.getTextureId());
		scaleSpriteToTargetSize(sprite);
		Image image = new Image(sprite);
		image.setOffset(PRODUCT_IMAGE_OFFSET);
		image.setRenderLayer(IMAGE_RENDER_LAYER);
		
		this.addComponent(image);
	}
	

	private static void scaleSpriteToTargetSize(Sprite sprite) {
		Texture texture = sprite.getTexture();
		float scaleX = IMAGE_TARGET_SIZE.x / texture.getWidth();
		float scaleY = IMAGE_TARGET_SIZE.y / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
	
	private void createTextComponent(String content, Vector2 offset, float scale) {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		Text text = new Text(font, content);
		text.setOffset(offset);
		text.setScale(scale);
		text.setRenderLayer(TEXT_RENDER_LAYER);
		text.setAlignment(Align.left);
		this.addComponent(text);
	}
	
	private class ClickableClicked implements ClickEventListener {
		@Override
		public void onClickEvent(Clickable clickedObject) {
			shopMenu.purchaseItem(item);
		}
	}
}
