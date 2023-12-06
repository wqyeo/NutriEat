package com.mygdx.nutrieats.adventurescene.gameinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;
import eatengine.components.gameinterface.Text;

class InventoryItem extends GameObject {
	
	private static final Vector2 SPRITE_TARGET_SIZE = new Vector2(32f, 32f); 
	private static final Vector2 TEXT_OFFSET = new Vector2(1f, 1f);
	
	private static final Vector2 CLICKABLE_POSITION_OFFSET = new Vector2(-2f, -2f);
	
	private int itemId;
	private int itemCount;
	
	private Text itemCountText;
	private InventoryInterface inventoryInterface;
	
	
	InventoryItem(Vector2 position, Item itemToLoad, Sprite itemSprite, int itemCount, InventoryInterface inventoryInterface){
		super(itemToLoad.getName() + "_INV_ELEMENT");
		itemId = itemToLoad.getId();
		this.itemCount = itemCount;
		this.inventoryInterface = inventoryInterface;
		
		createImageComponent(itemSprite);
		createTextComponent();
		createClickableComponent();
		
		inventoryInterface.getSceneReference().addGameObjectToRoot(this);
		this.setPosition(position);
	}
	
	private void createImageComponent(Sprite sprite) {
		scaleSpriteToTargetSize(sprite);
		Image image = new Image(sprite);
		
		this.addComponent(image);
	} 
	
	public static void scaleSpriteToTargetSize(Sprite sprite) {
		Texture texture = sprite.getTexture();
		float scaleX = SPRITE_TARGET_SIZE.x / texture.getWidth();
		float scaleY = SPRITE_TARGET_SIZE.y / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
	
	private void createTextComponent() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		itemCountText = new Text(font, String.valueOf(itemCount));
		itemCountText.setOffset(TEXT_OFFSET);
		itemCountText.setScale(0.5f);
		itemCountText.layer = 100;
		
		this.addComponent(itemCountText);
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(SPRITE_TARGET_SIZE);
		clickable.setOffset(CLICKABLE_POSITION_OFFSET);
		clickable.addOnClickListener(new OnInventoryItemClicked());
		
		this.addComponent(clickable);
	}
	
	public void incrementCount() {
		++itemCount;
		itemCountText.setText(String.valueOf(itemCount));
	}
	
	public int getItemId() {
		return itemId;
	}
	
	private class OnInventoryItemClicked implements ClickEventListener{

		@Override
		public void onClickEvent(Clickable clickedObject) {
			inventoryInterface.triggerInventoryItemClicked(itemId);
		}
		
	}

}
