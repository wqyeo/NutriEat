package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.cookingscene.entities.Waiter;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.gameinterface.LaunchedItem;
import com.mygdx.nutrieats.gameinterface.OnItemLaunchedComplete;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.Util;

public class CookingMenu {

	private static final int COOKING_INGREDIENT_COUNT = 5;
	static final Vector2 COOKING_INGREDIENT_POSITION = new Vector2(-780f, -360f);
	private static final float COLUMN_SIZE = 90f;
	
	private CookingIngredient[] cookingIngredients;
	private int ingredientsToCook;
	
	private IngredientsMenu ingredientsMenu;
	private CookingPot cookingPot;
	private Recipe currentSetRecipe;
	private CookButton cookButton;
	private CookingTagsText cookingTagsText;
	
	private boolean isCooking;
	
	private CookingSceneManager sceneManagerRef;
	private CookingGameInterfaceManager interfaceManager;
	public CookingMenu(CookingSceneManager sceneRef, CookingGameInterfaceManager interfaceManager) {
		this.sceneManagerRef = sceneRef;
		this.interfaceManager = interfaceManager;
		
		Vector2 screenSize = sceneRef.getScreenSize();
		ingredientsMenu = new IngredientsMenu(screenSize.x, screenSize.y, sceneRef, this);
		cookingPot = new CookingPot(screenSize, sceneRef);
		createCookingIngredients();
		cookButton = new CookButton(this);
		
		isCooking = false;
		
		cookingTagsText = new CookingTagsText(this);
		this.getSceneManager().getScene().addGameObjectToRoot(cookingTagsText);
	}
	
	public void updateIngredientList() {
		ingredientsMenu.updateInventoryCount();
	}
	
	private void createCookingIngredients() {
		if (isCooking ) {
			return;
		}
		
		Vector2 basePosition = Util.addVec2(COOKING_INGREDIENT_POSITION, sceneManagerRef.getScreenSize());
		cookingIngredients = new CookingIngredient[COOKING_INGREDIENT_COUNT];
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			Vector2 position = Util.addVec2(basePosition, new Vector2(i * COLUMN_SIZE, 0f));
			cookingIngredients[i] = new CookingIngredient(position, sceneManagerRef, this);
			sceneManagerRef.getScene().addGameObjectToRoot(cookingIngredients[i]);
		}
	}
	
	public void clearIngredients() {
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			cookingIngredients[i].clearIngredient();
		}
		updateTagsState();
	}
	
	void triggerIngredientsAdded(Item ingredient) {
		if (currentSetRecipe == null || isCooking) {
			// TODO: Warn player to set a recipe first?
			return;
		}
		
		// First the first empty slot to place the new ingredient in,
		// additionally, ensure that the ingredient isn't already part of the cooking. (If so, ignore this request)
		int firstEmptySlotIndex = -1;
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			if (cookingIngredients[i].getIngredient() == ingredient) {
				// TODO: Maybe warn player that ingredient is already inside pot?
				return;
			}
			if (firstEmptySlotIndex == -1 && cookingIngredients[i].getIngredient() == null) {
				firstEmptySlotIndex = i;
			}
		}
		
		if (firstEmptySlotIndex == -1) {
			return;
		}
		
		sceneManagerRef.playSoundByType(CookingAudioType.BUTTON_CLICK);
		cookingIngredients[firstEmptySlotIndex].setIngredient(ingredient);
		updateTagsState();
	}
	
	void updateTagsState() {
		Collection<FoodTag> currentTags = getAllIngredientTags();
		cookingTagsText.fillTextWithTags(currentTags);
		checkCookAvability(currentTags);
	}
	
	/**
	 * Check if the user can cook. (All the required food, and food tags are present)
	 * @param currentTags
	 */
	private void checkCookAvability(Collection<FoodTag> currentTags) {
		HashSet<FoodTag> required = new HashSet<>(Arrays.asList(currentSetRecipe.getRequired_food_tags()));
		HashSet<FoodTag> deny = new HashSet<>(Arrays.asList(currentSetRecipe.getIncompatible_tags()));
		
		boolean canCook = true;
		for (FoodTag tag: currentTags) {
			// Tags that are incompatible are added!
			if (deny.contains(tag)) {
				canCook = false;
				break;
			}
			
			required.remove(tag);
		}
		
		// Some required tags are not fullfilled.
		if (required.size() > 0) {
			canCook = false;
		}
		
		if (!canCook) {
			cookButton.setState(canCook);
			return;
		}
		
		HashSet<Integer> ingredientsNeeded = new HashSet<>();
		for (int i : currentSetRecipe.getRequired_item_id()) {
			ingredientsNeeded.add(i);
		}
		
		// Check for required ingredients
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			Item ingredient = cookingIngredients[i].getIngredient();
			if (ingredient == null) {
				continue;
			}
			
			ingredientsNeeded.remove(ingredient.getId());
		}
		
		// Ingredients required is not fullfilled.
		if (ingredientsNeeded.size() > 0) {
			canCook = false;
		}
		
		cookButton.setState(canCook);
	}
	
	void startCooking() {
		sceneManagerRef.playSoundByType(CookingAudioType.BUTTON_CLICK);
		isCooking = true;
		ingredientsToCook = 0;
		Vector2 destination = Util.addVec2(CookingPot.RECIPE_IMAGE_OFFSET, cookingPot.getWorldPosition());
		
		OnItemLaunchedComplete callback = new OnItemLaunchedComplete() {
			
			@Override
			public void callback(LaunchedItem launchedIngredient) {
				ingredientLaunchedDone(launchedIngredient);
			}
		};
		
		// For each ingredient used, launch into the cooking pot
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			Item ingredient = cookingIngredients[i].getIngredient();
			if (ingredient == null) {
				continue;
			}
			
			Sprite sprite = sceneManagerRef.createSpriteByTextureId(ingredient.getTextureId());
			
			Vector2 position = cookingIngredients[i].getWorldPosition();
			LaunchedItem launchedIngredient = new LaunchedItem(position, destination,  sprite, callback, new Vector2(32f, 32f));
			sceneManagerRef.getScene().addGameObjectToRoot(launchedIngredient);
			++ingredientsToCook;
			
			// Additionally, remove from player's inventory
			sceneManagerRef.getPlayerData().useItem(ingredient);
		}
		
		this.clearIngredients();
		updateTagsState();
		interfaceManager.fillOrderInfo();
		updateIngredientList();
	}
	
	void ingredientLaunchedDone(LaunchedItem launchedIngredient) {
		--ingredientsToCook;
		sceneManagerRef.getScene().removeFromRoot(launchedIngredient, true);
		sceneManagerRef.playSoundByType(CookingAudioType.INGREDIENT_LAND);
		
		if (ingredientsToCook > 0) {
			return;
		}
		
		OnItemLaunchedComplete callback = new OnItemLaunchedComplete() {
			@Override
			public void callback(LaunchedItem launchedIngredient) {
				sceneManagerRef.getWaiter().invokeServeFood(currentSetRecipe);
				sceneManagerRef.getScene().removeFromRoot(launchedIngredient, true);
			}
		};
		Waiter waiter = sceneManagerRef.getWaiter();
		Sprite foodSprite = sceneManagerRef.createSpriteByTextureId(currentSetRecipe.getTextureId());
		
		// Calculate where to launch food towards, and from.
		Vector2 offset = new Vector2(110f, 60f);
		Vector2 destination = Util.addVec2(offset, waiter.getWorldPosition());
		Vector2 position = Util.addVec2(CookingPot.RECIPE_IMAGE_OFFSET, cookingPot.getWorldPosition());
		
		LaunchedItem food = new LaunchedItem(position, destination, foodSprite, callback, new Vector2(64f, 64f));
		this.sceneManagerRef.getScene().addGameObjectToRoot(food);
		
		// Free cooking pot and play cook animation
		sceneManagerRef.getChef().doCookAnimation();
		cookingPot.clearRecipeDisplay();
	}
	
	
	public Collection<FoodTag> getAllIngredientTags(){
		Collection<FoodTag> result = new HashSet<FoodTag>();
		
		// Add all food tags.
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			Item ingredient = cookingIngredients[i].getIngredient();
			if (ingredient == null) {continue;}
			
			for (FoodTag tags: ingredient.getFoodTags()) {
				result.add(tags);
			}
		}
		
		// Remove food tags based on denied.
		for (int i = 0; i < COOKING_INGREDIENT_COUNT; ++i) {
			Item ingredient = cookingIngredients[i].getIngredient();
			if (ingredient == null) {continue;}
			
			for (FoodTag tags: ingredient.getDenyFoodTags()) {
				result.remove(tags);
			}
		}
		
		return result;
	}
	
	public void hide() {
		cookingPot.setActive(false);
		ingredientsMenu.hideIngredients();
		cookButton.setActive(false);
	}
	
	public void show() {
		cookingPot.setActive(true);
		ingredientsMenu.showIngredients();
		cookButton.setActive(true);
		updateIngredientList();
	}
	
	public void setRecipe(Recipe recipe) {
		currentSetRecipe = recipe;
		cookingPot.setRecipeDisplay(recipe.getTextureId());
		
		sceneManagerRef.playSoundByType(CookingAudioType.BUTTON_CLICK);
	}
	
	CookingSceneManager getSceneManager() {
		return sceneManagerRef;
	}
	
	public void setDoneCooking() {
		isCooking = false;
		currentSetRecipe = null;
	}
	
	public boolean isCooking() {
		return isCooking;
	}
}
