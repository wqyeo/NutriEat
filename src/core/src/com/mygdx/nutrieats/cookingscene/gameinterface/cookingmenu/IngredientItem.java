package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

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

class IngredientItem extends GameObject {
	
	private static final int RENDER_LAYER = 4;
	private static final int TEXT_RENDER_LAYER = 5;
	private static final Vector2 SPRITE_TARGET_SIZE = new Vector2(32f, 32f); 
	private static final Vector2 TEXT_OFFSET = new Vector2(1f, 1f);
	
	private static final Vector2 CLICKABLE_POSITION_OFFSET = new Vector2(-2f, -2f);
	
	private int itemId;
	private int itemCount;
	
	private Text itemCountText;
	private IngredientsMenu ingredientMenu;
	
	
	IngredientItem(Vector2 position, Item itemToLoad, Sprite itemSprite, int itemCount, IngredientsMenu ingredientMenu){
		super(itemToLoad.getName() + "_INGREDIENT_ELEMENT");
		itemId = itemToLoad.getId();
		this.itemCount = itemCount;
		this.ingredientMenu = ingredientMenu;
		
		createImageComponent(itemSprite);
		createTextComponent();
		createClickableComponent();
		
		ingredientMenu.getSceneReference().addGameObjectToRoot(this);
		this.setPosition(position);
	}
	
	private void createImageComponent(Sprite sprite) {
		scaleSpriteToTargetSize(sprite);
		Image image = new Image(sprite);
		image.setRenderLayer(RENDER_LAYER);
		
		this.addComponent(image);
	} 
	
	private static void scaleSpriteToTargetSize(Sprite sprite) {
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
		itemCountText.layer = TEXT_RENDER_LAYER;
		
		this.addComponent(itemCountText);
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(SPRITE_TARGET_SIZE);
		clickable.setOffset(CLICKABLE_POSITION_OFFSET);
		clickable.addOnClickListener(new OnIngredientClicked());
		
		this.addComponent(clickable);
	}
	
	public void setItemCount(int newCount) {
		itemCount = newCount;
		itemCountText.setText(String.valueOf(itemCount));
	}
	
	public void decrementCount() {
		--itemCount;
		itemCountText.setText(String.valueOf(itemCount));
	}
	
	public void incrementCount() {
		++itemCount;
		itemCountText.setText(String.valueOf(itemCount));
	}
	
	public int getItemId() {
		return itemId;
	}
	
	private class OnIngredientClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			if (itemCount <= 0) {return;}
			ingredientMenu.triggerIngredientSelected(itemId);
		}
		
	}

}
