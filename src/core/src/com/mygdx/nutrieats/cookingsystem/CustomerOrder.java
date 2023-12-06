package com.mygdx.nutrieats.cookingsystem;

public class CustomerOrder {
	private String description;
	private FoodTag[] tags_task;
	private String info;
	
	public CustomerOrder(String description, FoodTag[] tags_task, String info) {
		this.description = description;
		this.tags_task = tags_task;
		this.info = info;
	}
	
	public String getDescription() {
		return description;
	}
	
	public FoodTag[] getTagsTask() {
		return tags_task.clone();
	}
	
	public String getInfo() {
		return info;
	}
}
