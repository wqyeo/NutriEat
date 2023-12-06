package com.mygdx.nutrieats.cookingscene.gameinterface;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.PlayerData;
import com.mygdx.nutrieats.adventurescene.AdventureAudioType;
import com.mygdx.nutrieats.adventurescene.gameinterface.OnCoinFloatedCallback;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu.CookingMenu;
import com.mygdx.nutrieats.cookingscene.gameinterface.recipemenu.RecipeMenu;
import com.mygdx.nutrieats.cookingscene.gameinterface.shoppingmenu.ShoppingMenu;
import com.mygdx.nutrieats.cookingsystem.CustomerOrder;
import com.mygdx.nutrieats.cookingsystem.Recipe;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.gameinterface.CoinInterface;
import com.mygdx.nutrieats.gameinterface.DescriptionBox;
import com.mygdx.nutrieats.gameinterface.IGameInterfaceManager;

import eatengine.GameObject;
import eatengine.Scene;

public class CookingGameInterfaceManager implements IGameInterfaceManager {
	
	private CookingSceneManager sceneManager;
	
	private DescriptionBox descriptionBox;
	private SidebarMenu sidebarMenu;
	private CoinInterface coinInterface;
	private CookingMenu cookingMenu;
	private RecipeMenu recipeMenu;
	private EndDayButton endDayButton;
	private ShoppingMenu shoppingMenu;
	
	private CustomerOrder currentCustomerOrder;

	
	public CookingGameInterfaceManager(CookingSceneManager sceneManager) {
		this.sceneManager = sceneManager;
		
		descriptionBox = new DescriptionBox(sceneManager.getScreenSize(), this);
		sceneManager.getScene().addGameObjectToRoot(descriptionBox);
		descriptionBox.setActive(false);
		
		sidebarMenu = new SidebarMenu(this);
		createCoinInterface();
		
		cookingMenu = new CookingMenu(sceneManager, this);
		recipeMenu = new RecipeMenu(this);
		recipeMenu.hide();
		
		endDayButton = new EndDayButton(this);
		shoppingMenu = new ShoppingMenu(this);
	}
	
	public void fillOrderInfo() {
		descriptionBox.fillDescriptionBox("", currentCustomerOrder.getInfo());
	}
	
	private void createCoinInterface() {
		int coinCount = sceneManager.getPlayerData().getCoinCount();
		coinInterface = new CoinInterface(getScreenSize(), this, coinCount);
	}
	
	public void getCoinRewardFromOrder() {
		Random rand = new Random();		
		int generatedCoinsCount = rand.nextInt(8) + 2;
		Vector2 startingPosition = new Vector2(-200f, 200f);
		
		OnCoinFloatedCallback onCoinFloatDone = new OnCoinFloatedCallback() {
			@Override
			public void triggerCallback() {
				sceneManager.getPlayerData().addCoins(1);
				coinInterface.incrementCoinCount();
				playSoundByType(CookingAudioType.COIN_GET);
			}
		};
		
		for (int i = 0; i < generatedCoinsCount; ++i) {
			shoppingMenu.createFloatingCoin(coinInterface.getCoinImagePosition(), startingPosition, onCoinFloatDone);	
		}
	}
	
	public void triggerShowRecipeList() {
		if (cookingMenu.isCooking()) {return;}
		
		recipeMenu.show();
		cookingMenu.hide();
		shoppingMenu.hide();
		playSoundByType(CookingAudioType.BUTTON_CLICK);
	}
	
	public void triggerShowShop() {
		if (cookingMenu.isCooking()) {return;}
		
		if (shoppingMenu.isShowing()) {
			cookingMenu.show();
			shoppingMenu.hide();
			recipeMenu.hide();
		} else {
			cookingMenu.hide();
			recipeMenu.hide();
			shoppingMenu.show();
		}
		playSoundByType(CookingAudioType.BUTTON_CLICK);
	}
	
	public void setRecipeOnPot(Recipe recipe) {
		cookingMenu.setRecipe(recipe);
		cookingMenu.clearIngredients();
		
		cookingMenu.show();
		shoppingMenu.hide();
		recipeMenu.hide();
		playSoundByType(CookingAudioType.BUTTON_CLICK);
	}
	
	@Override
	public String createTextureId(String texturePath) {
		return sceneManager.createTexture(texturePath);
	}

	@Override 
	public Sprite getSpriteByTextureId(String textureId) {
		return sceneManager.createSpriteByTextureId(textureId);
	}
	
	public void addObjectToScene(GameObject object) {
		sceneManager.getScene().addGameObjectToRoot(object);
	}
	
	public Vector2 getScreenSize() {
		return sceneManager.getScreenSize();
	}

	@Override
	public Scene getSceneReference() {
		return sceneManager.getScene();
	}


	@Override
	public PlayerData getPlayerData() {
		return sceneManager.getPlayerData();
	}
	
	public GameData getGameData() {
		return sceneManager.getGameData();
	}
	
	public CoinInterface getCoinInterface() {
		return coinInterface;
	}
	
	public void endDay() {
		if (cookingMenu.isCooking()) {return;}
		
		sceneManager.changeToAdventureScene();
	}
	
	public void setCustomerOrder(CustomerOrder customerOrder) {
		playSoundByType(CookingAudioType.NEW_ORDER);
		currentCustomerOrder = customerOrder;
		descriptionBox.fillDescriptionBox("", customerOrder.getDescription());
		descriptionBox.setActive(true);
		cookingMenu.setDoneCooking();
	}
	
	public void playSoundByType(CookingAudioType audioType) {
		sceneManager.playSoundByType(audioType);
	}
}
