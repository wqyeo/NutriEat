package com.mygdx.nutrieats.adventurescene.gameinterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Item;
import com.mygdx.nutrieats.inventorysystem.ItemCount;

import eatengine.Scene;
import eatengine.Util;

/**
 * Composition class to create the interface for showing player's inventory.
 *
 */
class InventoryInterface {

	private static final String INVENTORY_BAG_TEXTURE_LOCATION = "sprites\\interface\\InventoryBag.png";
	
	private static final int COLUMN_COUNT = 6;
	
	private static final Vector2 START_PLACEMENT_OFFSET = new Vector2(-600f, -200f);
	private static final float COLUMN_GAP = 55f;
	private static final float ROW_GAP = -70f;
	
	private AdventureGameInterfaceManager interfaceManager;
	private List<InventoryItem> inventoryItems;
	
	private boolean isShowing;
	private InventoryBag inventoryBag;
	
	private int currentColumn, currentRow;
	/**
	 * Position to start placing the items from.
	 * (Offset factored in)
	 */
	private Vector2 startPosition;
	
	InventoryInterface(float screenHeight, float screenWidth, AdventureGameInterfaceManager interfaceManager) {
		this.interfaceManager = interfaceManager;
	
		startPosition = Util.addVec2(START_PLACEMENT_OFFSET, new Vector2(screenWidth, screenHeight));
		placeInventoryItems();
		
		createInventoryBag(screenHeight, screenWidth);
		
		isShowing = false;
	}
	
	private void createInventoryBag(float screenHeight, float screenWidth) {
		Sprite inventoryBagSprite = createInventoryBagSprite();
		inventoryBag = new InventoryBag(screenWidth, screenHeight, inventoryBagSprite);
		interfaceManager.getSceneReference().addGameObjectToRoot(inventoryBag);
		
		inventoryBag.setActive(false);
	}
	
	private Sprite createInventoryBagSprite() {
		String textureId = interfaceManager.createTextureId(INVENTORY_BAG_TEXTURE_LOCATION);
		return interfaceManager.getSpriteByTextureId(textureId);
	}
	
	public void incrementItemToInventory(Item item) {
		for (InventoryItem inventoryItem: inventoryItems) {
			if (inventoryItem.getItemId() == item.getId()) {
				inventoryItem.incrementCount();
				return;
			}
		}
		
		// Doesnt exist in inventory yet, add new entry.
		Vector2 finalPosition = calculateNewPlacementLocation();
		createNewItem(finalPosition, item, 1);
		++currentColumn;
	}
	
	private void placeInventoryItems() {
		inventoryItems = new ArrayList<InventoryItem>();
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
	
	/**
	 * Based on the 'startPosition', currentColumn, currentRow; Determine where to place the next item.
	 * @return
	 */
	private Vector2 calculateNewPlacementLocation() {
		Vector2 currentOffset = new Vector2(currentColumn * COLUMN_GAP, currentRow * ROW_GAP);
		return Util.addVec2(startPosition, currentOffset);
	}
	
	private void createNewItem(Vector2 position, Item item, int itemCount) {
		Sprite itemSprite = interfaceManager.getSpriteByTextureId(item.getTextureId());
		InventoryItem newItem = new InventoryItem(position, item, itemSprite, itemCount, this);
		inventoryItems.add(newItem);
		newItem.setActive(false);
	}
	
	public void hideInventory() {
		for (InventoryItem item : inventoryItems) {
			item.setActive(false);
		}
		inventoryBag.setActive(false);
		isShowing = false;
	}
	
	public void showInventory() {
		for (InventoryItem item: inventoryItems) {
			item.setActive(true);
		}
		inventoryBag.setActive(true);
		isShowing = true;
	}
	
	boolean isShowing() {
		return isShowing;
	}
	
	Scene getSceneReference() {
		return interfaceManager.getSceneReference();
	}
	
	void triggerInventoryItemClicked(int itemId) {
		GameData gameData = interfaceManager.getGameData();
		Item itemClicked = gameData.getItemById(itemId);
		
		interfaceManager.showItemDescription(itemClicked);
	}
}
