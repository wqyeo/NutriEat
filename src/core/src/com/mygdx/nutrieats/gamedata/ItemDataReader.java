package com.mygdx.nutrieats.gamedata;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.EatEngine;

/**
 * Composition class to read item data from JSON file
 * @author wqyeo
 *
 */
class ItemDataReader {
	
	private static final String SPRITE_BASE_PATH_STRING = "sprites\\food\\";
	private static final String ITEM_DATA_JSON_LOCATION = "item_data.json";

	private GameData gameData;
	
	public ItemDataReader(GameData gameData) {
		this.gameData = gameData;
	}
	
	public Map<Integer, Item> readItemData(EatEngine engine){
		Map<Integer, Item> result = new HashMap<Integer, Item>();
		JsonValue rootJson = gameData.loadJsonValueFromPath(ITEM_DATA_JSON_LOCATION);
		for (JsonValue value : rootJson) {
			try {
			    int id = value.getInt("id");
			    if (result.containsKey(id)) {
			    	System.out.println("INVALID DUPLICATE_ITEM_ID (" + id + ")");
			    	continue;
			    }
			    
			    String name = value.getString("name");
			    String description = value.getString("description");
			    String texture_name = value.getString("texture_name");
			    int cost = value.getInt("cost");
			    FoodTag[] food_tags = readFoodTagsFromValueArray(value, "food_tags");
			    FoodTag[] deny_food_tags = readFoodTagsFromValueArray(value, "deny_food_tags");
			    
			    String texturePath = SPRITE_BASE_PATH_STRING + texture_name;
			    String textureId = engine.createTexutre(texturePath);
			    if (textureId == null) {
			    	System.out.println("INVALID_LOAD_ITEM_TEXTURE :: " + texturePath);
			    	continue;
			    }
			    
			    Item item = new Item(id, name, description, texture_name, cost, food_tags, deny_food_tags, textureId);
			    result.put(id, item);
			} catch (Exception e) {
				System.out.println("INVALID LOAD_ITEM_DATA_ENTRY :: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	private static FoodTag[] readFoodTagsFromValueArray(JsonValue value, String arrayIdentifier) {
		JsonValue foodTags = value.get(arrayIdentifier);
		FoodTag[] result = new FoodTag[foodTags.size];
		
		int i = 0;
		for (JsonValue tag : foodTags) {
			String tagString = "";
			try {
			    tagString = tag.asString().toUpperCase();
			    FoodTag enumValue = FoodTag.valueOf(tagString);
			    
			    result[i] = enumValue;
			} catch (IllegalArgumentException e) {
				System.out.println("INVALID LOAD_ITEM_DATA_ENTRY :: Unknown enum constant of " + tagString);
			} finally {
			    ++i;
			}
		}
		
		return result;
	}
}
