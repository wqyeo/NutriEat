package com.mygdx.nutrieats.gamedata;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.nutrieats.cookingsystem.CustomerOrder;
import com.mygdx.nutrieats.cookingsystem.FoodTag;

import eatengine.EatEngine;

class CustomerOrderDataReader {
	private static final String CUSTOMER_ORDER_JSON_LOCATION = "customer_order.json";

	private GameData gameData;
	
	public CustomerOrderDataReader(GameData gameData) {
		this.gameData = gameData;
	}
	
	public List<CustomerOrder> readCustomerOrderData(EatEngine engine){
		List<CustomerOrder> result = new ArrayList<CustomerOrder>();
		JsonValue rootJson = gameData.loadJsonValueFromPath(CUSTOMER_ORDER_JSON_LOCATION);
		for (JsonValue value : rootJson) {
			try {
			    String description = value.getString("description");
			    FoodTag[] tags_task = readFoodTagsFromValueArray(value, "tags_task");
			    String info = value.getString("info");
			    
			    
			    CustomerOrder order = new CustomerOrder(description, tags_task, info);
			    result.add(order);
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
