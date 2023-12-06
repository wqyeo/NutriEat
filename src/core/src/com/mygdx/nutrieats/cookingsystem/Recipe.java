package com.mygdx.nutrieats.cookingsystem;

import java.util.Arrays;

public class Recipe {

	private int id;
	private String name;
	private String description;
	private String texture_name;
	private int cost;
	
	private FoodTag[] food_tags;
	
	/**
	 * All the collective FoodTag of items used to make this recipe must fill up all the tags in the array.
	 * (All tags must be filled else we cant cook it)
	 */
	private FoodTag[] required_food_tags;
	
	/**
	 * FoodTags present in any of the item cannot be present in the array as well.
	 * (Incompatibility of FoodTags with item used, recipe will fail)
	 */
	private FoodTag[] incompatible_tags;
	
	/**
	 * These specific items must be present to cook this recipe.
	 */
	private int[] required_item_id;
	
	private String textureId;
	
	public Recipe(int id, String name, String description, String texture_nameString, int cost, FoodTag[] food_tags,
			FoodTag[] required_food_tags, FoodTag[] incompatible_tags, int[] required_item_id, String textureId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.texture_name = texture_nameString;
		this.cost = cost;
		this.food_tags = food_tags;
		this.required_food_tags = required_food_tags;
		this.incompatible_tags = incompatible_tags;
		this.required_item_id = required_item_id;
		this.textureId = textureId;
	}
	
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}

	public String getTextureName(){
		return texture_name;
	}

	public int getCost() {
		return cost;
	}

	public FoodTag[] getFood_tags() {
		return Arrays.copyOf(food_tags, food_tags.length);
	}

	/**
	 * All the collective FoodTag of items used to make this recipe must fill up all the tags in the array.
	 * (All tags must be filled else we cant cook it)
	 */
	public FoodTag[] getRequired_food_tags() {
		return Arrays.copyOf(required_food_tags, required_food_tags.length);
	}

	/**
	 * FoodTags present in any of the item cannot be present in the array as well.
	 * (Incompatibility of FoodTags with item used, recipe will fail)
	 */
	public FoodTag[] getIncompatible_tags() {
		return Arrays.copyOf(incompatible_tags, incompatible_tags.length);
	}

	/**
	 * These specific items must be present to cook this recipe.
	 */
	public int[] getRequired_item_id() {
		return Arrays.copyOf(required_item_id, required_item_id.length);
	}
	
	public String getTextureId() {
		return textureId;
	}
}
