package com.mygdx.nutrieats.adventurescene.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.PlayerData;
import com.mygdx.nutrieats.adventurescene.AdventureAudioType;
import com.mygdx.nutrieats.adventurescene.AdventureSceneManager;
import com.mygdx.nutrieats.adventurescene.OnDiceRolled;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.gameinterface.CoinInterface;
import com.mygdx.nutrieats.gameinterface.DescriptionBox;
import com.mygdx.nutrieats.gameinterface.IGameInterfaceManager;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.Scene;
import eatengine.Util;
import eatengine.components.gameinterface.Text;

public class AdventureGameInterfaceManager implements IGameInterfaceManager {

	private final Vector2 FLOATING_ITEM_POSITION_OFFSET = new Vector2(-800, -200f);
	
	private static final String COIN_TEXTURE_PATH = "sprites\\coin.png";
	private final Vector2 FLOATING_COIN_SCALE = new Vector2(0.20f, 0.20f);
	
	private static final String INVENTORY_BUTTON_TEXTURE_PATH = "sprites\\interface\\InventoryBtn.png";
	
	private final String coinTextureId;
	private final String inventoryBtnTextureId;
	
	private AdventureSceneManager sceneManager;
	
	private CoinInterface coinInterface;
	private HotbarButton inventoryBtn;
	private InventoryInterface inventoryInterface;
	private DescriptionBox descriptionBox;
	
	public AdventureGameInterfaceManager(AdventureSceneManager sceneManager) {
		this.sceneManager = sceneManager;
		
		coinTextureId = sceneManager.createTextureId(COIN_TEXTURE_PATH);
		inventoryBtnTextureId = sceneManager.createTextureId(INVENTORY_BUTTON_TEXTURE_PATH);
		
		float screenWidth = sceneManager.getScreenWidth();
		float screenHeight = sceneManager.getScreenHeight();
		
		coinInterface = new CoinInterface(screenWidth, screenHeight, this, sceneManager.getPlayerData().getCoinCount());
		inventoryBtn = new HotbarButton("Inventory Btn", screenWidth, screenHeight, this, HotbarButtonType.INVENTORY_DISPLAY);
		inventoryInterface = new InventoryInterface(screenHeight, screenWidth, this);
		
		descriptionBox = new DescriptionBox(screenWidth, screenHeight, this);
		sceneManager.getScene().addGameObjectToRoot(descriptionBox);
		descriptionBox.setActive(false);
	}
	
	public void triggerShowItemCollected(Item item) {
		inventoryInterface.incrementItemToInventory(item);
		Sprite itemSprite = sceneManager.requestSpriteByTexture(item.getTextureId());
		Vector2 itemPosition = Util.addVec2(FLOATING_ITEM_POSITION_OFFSET, sceneManager.getScreenSize());
		
		FloatingItem floatingItem = new FloatingItem("FLOATING_" + item.getName(), itemSprite, itemPosition);
		sceneManager.getScene().addGameObjectToRoot(floatingItem);
	}
	
	public void triggerCollectCoin(int amount) {
		while (amount > 0) {
			createCollectedCoin();
			--amount;
		}
	}
	
	private void createCollectedCoin() {
		Vector2 floatTowards = coinInterface.getCoinImagePosition();
		Sprite coinSprite = getCoinSprite();
		Vector2 startPosition = sceneManager.getPlayerPosition();
		
		// Increment coin count after done floating.
		OnCoinFloatedCallback onCoinFloatDone = new OnCoinFloatedCallback() {
			@Override
			public void triggerCallback() {
				coinInterface.incrementCoinCount();
				sceneManager.playSoundByType(AdventureAudioType.COIN_GET);
			}
		};
		
		PopFloatingCoin newFloatingCoin = new PopFloatingCoin("Floating Coin", coinSprite, startPosition, FLOATING_COIN_SCALE, floatTowards, onCoinFloatDone);
		sceneManager.getScene().addGameObjectToRoot(newFloatingCoin);
	}
	
	void triggerShowInventory() {
		sceneManager.playSoundByType(AdventureAudioType.BUTTON_CLICK);
		sceneManager.signalButtonClicked();
		
		if (inventoryInterface.isShowing()) {
			inventoryInterface.hideInventory();
			sceneManager.signalViewingInterface(false);
			descriptionBox.setActive(false);
		} else {
			inventoryInterface.showInventory();
			sceneManager.signalViewingInterface(true);
		}
	}
	
	void showItemDescription(Item item) {
		sceneManager.playSoundByType(AdventureAudioType.BUTTON_CLICK);
		descriptionBox.setActive(true);
		descriptionBox.fillDescriptionWithItem(item);
		System.out.println("Showing " + item.getName());
	}
	
	@Override
	public String createTextureId(String texturePath) {
		return sceneManager.createTextureId(texturePath);
	}
	
	Sprite getCoinSprite() {
		return sceneManager.requestSpriteByTexture(coinTextureId);
	}
	
	Sprite getInventoryBtnSprite() {
		return sceneManager.requestSpriteByTexture(inventoryBtnTextureId);
	}
	
	@Override
	public Sprite getSpriteByTextureId(String textureId) {
		return sceneManager.requestSpriteByTexture(textureId);
	}
	
	@Override
	public Scene getSceneReference() {
		return sceneManager.getScene();
	}

	GameData getGameData() {
		return sceneManager.getGameData();
	}
	
	@Override
	public PlayerData getPlayerData() {
		return sceneManager.getPlayerData();
	}
}
