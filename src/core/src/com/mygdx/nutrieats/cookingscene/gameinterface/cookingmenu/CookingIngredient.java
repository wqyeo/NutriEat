package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

class CookingIngredient extends GameObject {
	
	private static final Vector2 IMAGE_TARGET_SIZE = new Vector2(32f, 32f);
	
	private static final Vector2 CLICKABLE_SIZE = new Vector2(35f, 35f);
	private static final Vector2 CLICKABLE_OFFSET = new Vector2(3f, 3f);

	private Item ingredient;
	
	private CookingSceneManager sceneRef;
	private CookingMenu cookingMenu;

	private Image ingredientImage;
	
	public CookingIngredient(Vector2 position, CookingSceneManager sceneRef, CookingMenu cookingMenu) {
		super("Cooking Ingredient Display");
		this.sceneRef = sceneRef;
		this.cookingMenu = cookingMenu;
		createClickable();
		
		this.setPosition(position);
	}
	
	private void createClickable() {
		Clickable clickable = new Clickable(CLICKABLE_SIZE);
		clickable.setOffset(CLICKABLE_OFFSET);
		clickable.addOnClickListener(new CookingIngredientClicked());
		this.addComponent(clickable);
	}
	
	public Item getIngredient() {
		return ingredient;
	}
	
	void clearIngredient() {
		ingredient = null;
		
		if (ingredientImage != null) {
			ingredientImage.detachFromGameObject();
			ingredientImage.dispose();
			ingredientImage = null;
		}
	}
	
	void setIngredient(Item ingredient) {
		this.ingredient = ingredient;
		
		if (ingredientImage != null) {
			ingredientImage.changeTexture(ingredient.getTextureId());
		} else {
			ingredientImage = new Image(sceneRef.createSpriteByTextureId(ingredient.getTextureId()));
			this.addComponent(ingredientImage);
		}
		
		ingredientImage.scaleSpriteToTargetSize(IMAGE_TARGET_SIZE);
	}
	
	private class CookingIngredientClicked implements ClickEventListener {

		@Override
		public void onClickEvent(Clickable clickedObject) {
			if (ingredient == null) {
				return;
			}
			
			clearIngredient();
			cookingMenu.updateTagsState();
		}
		
	}
}
