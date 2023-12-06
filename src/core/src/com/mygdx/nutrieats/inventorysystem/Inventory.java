package com.mygdx.nutrieats.inventorysystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.mygdx.nutrieats.gamedata.GameData;

public class Inventory {
	
	/**
	 * Key: Item ID; Value: Item Count;
	 */
	private Map<Integer, Integer> itemCountMapping;
	
	public Inventory() {
		itemCountMapping = new HashMap<Integer, Integer>();
	}
	
	public void addItem(Item item) {
		addItem(item, 1);
	}
	
	public void addItem(Item item, int amount) {
		int itemId = item.getId();
		
		int itemCount = amount;
		if (!itemCountMapping.containsKey(itemId)) {
			itemCountMapping.put(itemId, itemCount);
		} else {
			itemCount = itemCountMapping.get(itemId) + amount;
			itemCountMapping.put(itemId, itemCount);
		}
	}
	
	public boolean decrementItemCount(int itemId, int decrementAmount) {
		boolean success = false;
		if (itemCountMapping.containsKey(itemId)) {
			int amount = itemCountMapping.get(itemId);
			
			if (decrementAmount > amount) {
				amount = 0;
				System.out.println("Failed to decrement Item(" + itemId + ") count by + " + decrementAmount + " due to insufficent amount (" + amount + "); Set to 0 instead.");
			} else {
				amount -= decrementAmount;
				itemCountMapping.put(itemId, amount);
				success = true;
			}
		}
		return success;
	}
	
	public int getItemCount(int itemId) {
		if (itemCountMapping.containsKey(itemId)) {
			return itemCountMapping.get(itemId);
		}
		return 0;
	}
	
	public ItemCount[] getAllItemCounts(GameData gameData) {
		List<ItemCount> result = new ArrayList<ItemCount>();
		
		for (int id : itemCountMapping.keySet()) {
			Item item = gameData.getItemById(id);
			int count = itemCountMapping.get(id);
			
			result.add(new ItemCount(item, count));
		}
		
		return result.toArray(new ItemCount[result.size()]);
	}

	public void removeItem(Item item) {
		decrementItemCount(item.getId(), 1);
	}
}
