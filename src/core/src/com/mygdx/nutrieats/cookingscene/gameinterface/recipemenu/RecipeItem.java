package com.mygdx.nutrieats.cookingscene.gameinterface.recipemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;
import eatengine.components.gameinterface.Text;

class RecipeItem extends GameObject {
	
	private static final int IMAGE_RENDER_LAYER = 7;
	private static final int TEXT_RENDER_LAYER = 8;
	
	private static final Vector2 IMAGE_TARGET_SIZE = new Vector2(32f, 32f);
	private static final Vector2 PRODUCT_IMAGE_OFFSET = new Vector2(2f, 2f);
	private static final float COLUMN_OFFSET = 50f;
	
	private static final Vector2 TITLE_TEXT_OFFSET = new Vector2(-0f, 45f);
	private static final float TITLE_TEXT_SCALE = 0.55f;
	
	private static final Vector2 TAGS_TEXT_OFFSET = new Vector2(0f, -6f);
	private static final float TAGS_TEXT_SCALE = 0.45f;
	private static final Vector2 DENY_TAGS_TEXT_OFFSET = new Vector2(0f, -20f);
	
	private static final Vector2 CLICKABLE_SIZE = new Vector2(170f, 70f);
	private static final Vector2 CLICKABLE_OFFSET = new Vector2(0f, -25f);
	
	private CookingGameInterfaceManager interfaceManager;
	private Recipe recipe;
	
	public RecipeItem(Recipe recipe, Vector2 startPosition, GameData gameData, CookingGameInterfaceManager interfaceManager) {
		super("Recipe_" + recipe.getId());
		this.interfaceManager = interfaceManager;
		this.recipe = recipe;
		
		// Title text.
		createTextComponent(recipe.getName(), TITLE_TEXT_OFFSET, TITLE_TEXT_SCALE);
		// Food Tags
		createTextComponent("Requires: " + parseToString(recipe.getRequired_food_tags()), TAGS_TEXT_OFFSET, TAGS_TEXT_SCALE);
		
		FoodTag[] incompatibles = recipe.getIncompatible_tags();
		if (incompatibles.length > 0) {
			createTextComponent("Can't mix with: " + parseToString(incompatibles), DENY_TAGS_TEXT_OFFSET, TAGS_TEXT_SCALE, Color.RED);
		}
		createRequiredFoodsIcons(gameData);
		createProductFoodImage();
		createClickableComponent();
		
		this.setPosition(startPosition);
	}
	
	private String parseToString(FoodTag[] foodTags) {
		return Util.enumArrayToString(foodTags);
	}
	
	private void createClickableComponent() {
		Clickable clickable = new Clickable(CLICKABLE_SIZE);
		clickable.setOffset(CLICKABLE_OFFSET);
		clickable.addOnClickListener(new ClickableClicked());
		this.addComponent(clickable);
	}
	
	private void createProductFoodImage() {
		Sprite sprite = interfaceManager.getSpriteByTextureId(recipe.getTextureId());
		scaleSpriteToTargetSize(sprite);
		Image image = new Image(sprite);
		image.setOffset(PRODUCT_IMAGE_OFFSET);
		image.setRenderLayer(IMAGE_RENDER_LAYER);
		
		this.addComponent(image);
	}
	
	private void createRequiredFoodsIcons(GameData gameData) {
		int[] requriedItemsId = recipe.getRequired_item_id();
		
		Item[] requiredItems = gameData.getItemsById(requriedItemsId);
		int column = 2;
		for (Item item : requiredItems) {
			Sprite itemSprite = interfaceManager.getSpriteByTextureId(item.getTextureId());
			scaleSpriteToTargetSize(itemSprite);
			
			Vector2 offset = Util.addVec2(PRODUCT_IMAGE_OFFSET, new Vector2(column * COLUMN_OFFSET, 0f));
			Image image = new Image(itemSprite);
			image.setOffset(offset);
			image.setRenderLayer(IMAGE_RENDER_LAYER);
			
			this.addComponent(image);
			++column;
		}
	}
	

	private static void scaleSpriteToTargetSize(Sprite sprite) {
		Texture texture = sprite.getTexture();
		float scaleX = IMAGE_TARGET_SIZE.x / texture.getWidth();
		float scaleY = IMAGE_TARGET_SIZE.y / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
	
	private void createTextComponent(String content, Vector2 offset, float scale, Color color) {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		Text text = new Text(font, content);
		text.setOffset(offset);
		text.setScale(scale);
		text.setRenderLayer(TEXT_RENDER_LAYER);
		text.setColor(color);
		text.setAlignment(Align.left);
		this.addComponent(text);
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
			interfaceManager.setRecipeOnPot(recipe);
		}
	}
}
