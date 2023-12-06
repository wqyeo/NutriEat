package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Item;
import com.mygdx.nutrieats.inventorysystem.ItemCount;

import eatengine.Scene;
import eatengine.Util;

class IngredientsMenu {
	private static final int COLUMN_COUNT = 6;
	
	private static final Vector2 START_PLACEMENT_OFFSET = new Vector2(-800f, -75f);
	private static final float COLUMN_GAP = 55f;
	private static final float ROW_GAP = -70f;
	
	private CookingSceneManager interfaceManager;
	private List<IngredientItem> ingredientItems;
	
	private boolean isShowing;
	
	private int currentColumn, currentRow;
	/**
	 * Position to start placing the items from.
	 * (Offset factored in)
	 */
	private Vector2 startPosition;
	private CookingMenu cookingMenu;
	
	IngredientsMenu(float screenWidth, float screenHeight, CookingSceneManager sceneManager, CookingMenu cookingMenu) {
		this.interfaceManager = sceneManager;
	
		startPosition = Util.addVec2(START_PLACEMENT_OFFSET, new Vector2(screenWidth, screenHeight));
		placeInventoryItems();
		
		this.cookingMenu = cookingMenu;
		isShowing = true;
	}
	
	
	public void incrementIngredientCount(Item item) {
		for (IngredientItem ingredient: ingredientItems) {
			if (ingredient.getItemId() == item.getId()) {
				ingredient.incrementCount();
				return;
			}
		}
		
		// Doesnt exist in inventory yet, add new entry.
		Vector2 finalPosition = calculateNewPlacementLocation();
		createNewItem(finalPosition, item, 1);
		++currentColumn;
	}
	
	private void placeInventoryItems() {
		ingredientItems = new ArrayList<IngredientItem>();
		currentColumn = 0;
		currentRow = 0;
		
		ItemCount[] itemsToDisplay = interfaceManager.getPlayerData().getAllItemCount();
		for (ItemCount itemCount: itemsToDisplay) {
			// Counting system used to display the inventory in a
			// grid-like system (row/column).
			if (currentColumn >= COLUMN_COUNT) {
				currentColumn = 0;
				++currentRow;
			}
			
			Vector2 finalPosition = calculateNewPlacementLocation();
			createNewItem(finalPosition, itemCount.getItem(), itemCount.getCount());
			++currentColumn;
		}
	}
	
	public void updateInventoryCount () {
		ItemCount[] itemsToDisplay = interfaceManager.getPlayerData().getAllItemCount();
		Set<ItemCount> hashSet = new HashSet<>(Arrays.asList(itemsToDisplay));
		
		
		for (IngredientItem item : ingredientItems) {
			for (ItemCount checkAgainst: itemsToDisplay) {
				if (item.getItemId() == checkAgainst.getItem().getId()) {
					item.setItemCount(checkAgainst.getCount());
					hashSet.remove(checkAgainst);
					break;
				}	
			}
		}
		
		for (ItemCount unaddedItem : hashSet) {
			if (currentColumn >= COLUMN_COUNT) {
				currentColumn = 0;
				++currentRow;
			}
			
			Vector2 finalPosition = calculateNewPlacementLocation();
			createNewItem(finalPosition, unaddedItem.getItem(), unaddedItem.getCount());
			++currentColumn;
		}
	}
	
	/**
	 * Based on the 'startPosition', currentColumn, currentRow; Determine where to place the next item.
	 * @return
	 */
	private Vector2 calculateNewPlacementLocation() {
		Vector2 currentOffset = new Vector2(currentColumn * COLUMN_GAP, currentRow * ROW_GAP);
		return Util.addVec2(startPosition, currentOffset);
	}
	
	private void createNewItem(Vector2 position, Item item, int itemCount) {
		Sprite itemSprite = interfaceManager.createSpriteByTextureId(item.getTextureId());
		IngredientItem newItem = new IngredientItem(position, item, itemSprite, itemCount, this);
		ingredientItems.add(newItem);
	}
	
	public void hideIngredients() {
		for (IngredientItem item : ingredientItems) {
			item.setActive(false);
		}
		isShowing = false;
	}
	
	public void showIngredients() {
		for (IngredientItem item: ingredientItems) {
			item.setActive(true);
		}
		isShowing = true;
	}
	
	boolean isShowing() {
		return isShowing;
	}
	
	Scene getSceneReference() {
		return interfaceManager.getScene();
	}
	
	void triggerIngredientSelected(int itemId) {
		GameData gameData = interfaceManager.getGameData();
		Item itemClicked = gameData.getItemById(itemId);
		
		cookingMenu.triggerIngredientsAdded(itemClicked);
	}
}
