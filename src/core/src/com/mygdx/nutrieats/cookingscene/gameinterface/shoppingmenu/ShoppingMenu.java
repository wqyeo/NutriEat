package com.mygdx.nutrieats.cookingscene.gameinterface.shoppingmenu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.PlayerData;
import com.mygdx.nutrieats.adventurescene.gameinterface.OnCoinFloatedCallback;
import com.mygdx.nutrieats.adventurescene.gameinterface.PopFloatingCoin;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.Scene;
import eatengine.Util;

public class ShoppingMenu {
	
	private static final String COIN_TEXTURE_PATH = "sprites\\coin.png";
	private final Vector2 FLOATING_COIN_SCALE = new Vector2(0.20f, 0.20f);
	
	private final String coinTextureId;
	
	// TODO: Maybe different page for each limit?
	private static final int RECIPE_DISPLAY_LIMIT = 8;
	private static final Vector2 RECIPE_POSITION_OFFSET_VECTOR2 = new Vector2(-650f, -80f);
	private static final float RECIPE_ROW_LENGTH = -110f;
	
	private List<ShopItem> shopDisplayList;
	
	private CookingGameInterfaceManager interfaceManager;
	private ShopMenuBackground background;
	
	private boolean isShowing;
	
	public ShoppingMenu(CookingGameInterfaceManager interfaceManager) {
		this.interfaceManager = interfaceManager;
		coinTextureId = interfaceManager.createTextureId(COIN_TEXTURE_PATH);
		
		background = new ShopMenuBackground(this);
		background.setActive(false);
		setupShoppingList();
		isShowing = false;
	}
	
	private void setupShoppingList() {
		shopDisplayList = new ArrayList<ShopItem>();
		GameData gameData = interfaceManager.getGameData();
		
		Item[] itemForSale = gameData.getItemsForSale();
		Vector2 startPosition = Util.addVec2(RECIPE_POSITION_OFFSET_VECTOR2, interfaceManager.getScreenSize());
		
		int row = 0;
		for (Item item : itemForSale) {
			if (shopDisplayList.size() >= RECIPE_DISPLAY_LIMIT) {
				break;
			}
			
			Vector2 position = Util.addVec2(startPosition, new Vector2(0f, RECIPE_ROW_LENGTH * row));
			ShopItem recipeItem = new ShopItem(item, position, this);
			interfaceManager.addObjectToScene(recipeItem);
			recipeItem.setActive(false);
			shopDisplayList.add(recipeItem);
			++row;
		}
	}
	
	public void show() {
		background.setActive(true);
		for (ShopItem item: shopDisplayList) {
			item.setActive(true);
		}
		isShowing = true;
	}
	
	public void hide() {
		background.setActive(false);
		for (ShopItem item: shopDisplayList) {
			item.setActive(false);
		}
		isShowing = false;
	}
	
	public boolean isShowing() {
		return isShowing;
	}
	
	
	public CookingGameInterfaceManager getInterfaceManager() {
		return interfaceManager;
	}
	
	public Scene getScene(){
		return interfaceManager.getSceneReference();
	}
	
	public Sprite getSpriteByTextureId(String textureId) {
		return interfaceManager.getSpriteByTextureId(textureId);
	}
	
	public String createTextureId(String texturePath) {
		return interfaceManager.createTextureId(texturePath);
	}
	
	public void purchaseItem(Item item) {
		int cost = item.getCost();
		PlayerData playerData = interfaceManager.getPlayerData();
		
		// Insufficent funds
		if (playerData.getCoinCount() < cost) {
			return;
		}
		
		playerData.removeCoins(cost);
		playerData.insertItem(item);
		
		Vector2 floatTowards = interfaceManager.getScreenSize();
		Vector2 startPosition = interfaceManager.getCoinInterface().getCoinImagePosition();
		
		OnCoinFloatedCallback onCoinFloatDone = new OnCoinFloatedCallback() {
			@Override
			public void triggerCallback() {
				interfaceManager.playSoundByType(CookingAudioType.COIN_GET);
			}
		};
		
		for (int i = 0; i < cost; ++i) {
			createFloatingCoin(floatTowards, startPosition, onCoinFloatDone);
			interfaceManager.getCoinInterface().decrementCoinCount();
		}
		
		interfaceManager.playSoundByType(CookingAudioType.ITEM_COLLECT);
	}
	
	public void createFloatingCoin(Vector2 floatTowards, Vector2 startPosition, OnCoinFloatedCallback callback) {
		Sprite coinSprite = interfaceManager.getSpriteByTextureId(coinTextureId);

		
		PopFloatingCoin newFloatingCoin = new PopFloatingCoin("Floating Coin", coinSprite, startPosition, FLOATING_COIN_SCALE, floatTowards, callback);
		interfaceManager.getSceneReference().addGameObjectToRoot(newFloatingCoin);
	}

}
