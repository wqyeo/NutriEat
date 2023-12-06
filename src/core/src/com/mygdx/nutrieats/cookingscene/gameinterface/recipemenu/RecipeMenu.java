package com.mygdx.nutrieats.cookingscene.gameinterface.recipemenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.gamedata.GameData;

import eatengine.Util;

public class RecipeMenu {

	// TODO: Maybe different page for each limit?
	private static final int RECIPE_DISPLAY_LIMIT = 8;
	private static final Vector2 RECIPE_POSITION_OFFSET_VECTOR2 = new Vector2(-850f, -80f);
	private static final float RECIPE_ROW_LENGTH = -110f;
	private static final int MAX_ROW_COUNT = 4;
	
	private static final float RECIPE_COLUMN_LENGTH = 300f;
	
	private List<RecipeItem> recipesDisplayList;
	
	private CookingGameInterfaceManager interfaceManager;
	private RecipeMenuBackground background;
	
	public RecipeMenu(CookingGameInterfaceManager interfaceManager) {
		this.interfaceManager = interfaceManager;
		
		background = new RecipeMenuBackground(interfaceManager);
		createRecipes();
	}
	
	private void createRecipes() {
		recipesDisplayList = new ArrayList<RecipeItem>();
		GameData gameData = interfaceManager.getGameData();
		
		Collection<Recipe> recipes = gameData.getAllRecipe();
		Vector2 startPosition = Util.addVec2(RECIPE_POSITION_OFFSET_VECTOR2, interfaceManager.getScreenSize());
		
		int row = 0;
		int column = 0;
		for (Recipe recipe : recipes) {
			if (recipesDisplayList.size() >= RECIPE_DISPLAY_LIMIT) {
				break;
			}
			if (row >= MAX_ROW_COUNT) {
				row = 0;
				++column;
			}
			
			Vector2 position = Util.addVec2(startPosition, new Vector2(RECIPE_COLUMN_LENGTH * column, RECIPE_ROW_LENGTH * row));
			RecipeItem recipeItem = new RecipeItem(recipe, position, gameData, interfaceManager);
			interfaceManager.addObjectToScene(recipeItem);
			recipesDisplayList.add(recipeItem);
			++row;
		}
	}
	
	public void show() {
		background.setActive(true);
		for (RecipeItem item: recipesDisplayList) {
			item.setActive(true);
		}
	}
	
	public void hide() {
		background.setActive(false);
		for (RecipeItem item: recipesDisplayList) {
			item.setActive(false);
		}
	}
}
