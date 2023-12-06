package com.mygdx.nutrieats.gamedata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.nutrieats.cookingsystem.CustomerOrder;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.EatEngine;

/**
 * Ideally should have only 1 of this per game instance.
 * Contains all the static data of the game loaded from JSON files.
 * @author wqyeo
 *
 */
public class GameData {
	private static final int[] ITEM_ID_FOR_SALE = new int[] {2, 7, 8, 9};
	
	private final Map<Integer, Item> itemData;
	private final Map<Integer, Recipe> recipeData;
	private final List<CustomerOrder> customerOrderData;
	
	public GameData(EatEngine engine) {
		ItemDataReader itemReader = new ItemDataReader(this);
		itemData = itemReader.readItemData(engine);
		
		RecipeDataReader recipeReader = new RecipeDataReader(this);
		recipeData = recipeReader.readRecipeData(engine);
		
		CustomerOrderDataReader orderReader  = new CustomerOrderDataReader(this);
		customerOrderData = orderReader.readCustomerOrderData(engine);
	}
	
	/**
	 * Given a JSON asset file path, load it as JsonValue. 
	 * @param jsonPath File path from assets folder.
	 * @return
	 */
	JsonValue loadJsonValueFromPath(String jsonPath) {
		FileHandle fileHandle = Gdx.files.internal(jsonPath);
		String jsonString = fileHandle.readString();
		return new JsonReader().parse(jsonString);
	}
	
	public Item getItemById(int id) {
		return itemData.get(id);
	}
	
	public Item[] getItemsById(int[] ids) {
		List<Item> result = new ArrayList<Item>();
		
		for (int itemId: ids) {
			Item found = itemData.getOrDefault(itemId, null);
			if (found != null) {
				result.add(found);
			}
		}
		
		return result.toArray(new Item[result.size()]);
	}
	
	public Item fetchRandomItemByFoodTag(FoodTag foodTag) {
		List<Item> validSelection = new ArrayList<Item>();
		
		for (Item item : itemData.values()) {
			if (item.containFoodTag(foodTag)) {
				validSelection.add(item);
			}
		}
		
	    if (validSelection.isEmpty()) {
	    	System.out.println("(fetchRandomItemByFoodTag) WARN: No item with FoodTag of " + foodTag);
	        return null;
	      }

	      Random random = new Random();
	      int index = random.nextInt(validSelection.size());
	      return validSelection.get(index);
	}
	
	public Collection<Recipe> getAllRecipe(){
		return recipeData.values();
	}
	
	public Collection<Item> getAllItems() {
		return itemData.values();
	}
	
	public CustomerOrder getRandomCustomerOrder() {
		Random random = new Random();
		int index = random.nextInt(customerOrderData.size());
		return customerOrderData.get(index);
	}
	
	public Item[] getItemsForSale(){
		return getItemsById(ITEM_ID_FOR_SALE);
	}
}
