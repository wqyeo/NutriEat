package com.mygdx.nutrieats.inventorysystem;

import java.util.Arrays;

import com.mygdx.nutrieats.cookingsystem.FoodTag;

/**
 * Represent a item (Mostly ingredient used for cooking)
 * @author wqyeo
 *
 */
public class Item {

	private int id;
	private String name;
	private String description;
	private String texture_name;
	private int cost;
	private FoodTag[] food_tags;
	
	private String textureId;
	
	/**
	 * If this item is used in any recipe, the resulting food will never attain these tags.
	 */
	private FoodTag[] deny_food_tags;
	
	public Item(int id, String name, String description, String texture_name, int cost, FoodTag[] food_tags,
			FoodTag[] deny_food_tags, String textureId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.texture_name = texture_name;
		this.cost = cost;
		this.food_tags = food_tags;
		this.deny_food_tags = deny_food_tags;
		this.textureId = textureId;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getTexture_name() {
		return texture_name;
	}

	public int getCost() {
		return cost;
	}

	public FoodTag[] getFoodTags() {
		return Arrays.copyOf(food_tags, food_tags.length);
	}

	/**
	 * If this item is used in any recipe, the resulting food will never attain these tags.
	 */
	public FoodTag[] getDenyFoodTags() {
		return Arrays.copyOf(deny_food_tags, deny_food_tags.length);
	}
	
	public boolean containFoodTag(FoodTag foodTag) {
		return Arrays.asList(food_tags).contains(foodTag);
	}
 
	
	public String getTextureId() {
		return textureId;
	}
}
