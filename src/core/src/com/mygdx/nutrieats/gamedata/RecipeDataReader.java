package com.mygdx.nutrieats.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.EatEngine;

/**
 * Composition class to read recipe data from JSON file.
 * @author wqyeo
 *
 */
public class RecipeDataReader {
	
	private static final String SPRITE_BASE_PATH_STRING = "sprites\\food\\";
	private static final String RECIPE_DATA_JSON_LOCATION = "recipe_data.json";

	private GameData gameData;
	
	public RecipeDataReader(GameData gameData) {
		this.gameData = gameData;
	}
	
	public Map<Integer, Recipe> readRecipeData(EatEngine engine){
		Map<Integer,  Recipe> result = new HashMap<Integer, Recipe>();
		JsonValue rootJson = gameData.loadJsonValueFromPath(RECIPE_DATA_JSON_LOCATION);
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
			    FoodTag[] required_food_tags = readFoodTagsFromValueArray(value, "required_food_tags");
			    FoodTag[] incompatible_tags = readFoodTagsFromValueArray(value, "incompatible_tags");
			    int[] required_item_id = readIntegersFromValueArray(value, "required_item_id");
			
			    String texturePath = SPRITE_BASE_PATH_STRING + texture_name;
			    String textureId = engine.createTexutre(texturePath);
			    if (textureId == null) {
			    	System.out.println("INVALID_RECIPE_TEXTURE :: " + texturePath);
			    	continue;
			    }
			    
			    Recipe newRecipe = new Recipe(id, name, description, texture_name, cost, food_tags, required_food_tags, incompatible_tags, required_item_id, textureId);
			    result.put(id, newRecipe);
			} catch (Exception e) {
				System.out.println("INVALID LOAD_RECIPE_DATA_ENTRY :: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	private static int[] readIntegersFromValueArray(JsonValue value, String arrayIdentifier) {
		JsonValue integers = value.get(arrayIdentifier);
		int[] result = new int[integers.size];
		
		for (int i = 0; i < integers.size; ++i) {
			try {
			    result[i] = integers.getInt(i);
			} catch (IllegalArgumentException e) {
				System.out.println("INVALID LOAD_RECIPE_DATA_ENTRY :: Failed to fetch an integer value.");
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
				System.out.println("INVALID LOAD_RECIPE_DATA_ENTRY :: Unknown enum constant of " + tagString);
			} finally {
			    ++i;
			}
		}
		
		return result;
	}
}
