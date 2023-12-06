package com.mygdx.nutrieats;

import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Inventory;
import com.mygdx.nutrieats.inventorysystem.Item;
import com.mygdx.nutrieats.inventorysystem.ItemCount;

public class PlayerData {
	private Inventory inventory;
	private int coinCount;
	
	/**
	 * For reference to items/recipe when needed..
	 */
	private GameData gameData;
	
	public PlayerData(GameData gameData) {
		this.gameData = gameData;
		inventory = new Inventory();
		coinCount = 0;
	}
	
	public void addCoins(int amount) {
		coinCount += amount;
	}
	
	public void removeCoins(int amount) {
		coinCount -= amount;
	}
	
	public int getCoinCount() {
		return coinCount;
	}
	
	public void useItem(Item item) {
		inventory.removeItem(item);
	}
	
	public void insertItem(Item item) {
		inventory.addItem(item);
	}
	
	public ItemCount[] getAllItemCount() {
		return inventory.getAllItemCounts(gameData);
	}
	
	/**
	 * For debug.
	 * @param amount
	 */
	void addAllItemByAmount(int amount) {
		for (Item item: gameData.getAllItems()) {
			inventory.addItem(item, amount);
		}
		coinCount += 20;
	}
}
