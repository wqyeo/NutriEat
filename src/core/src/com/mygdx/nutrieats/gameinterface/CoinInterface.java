package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.adventurescene.AdventureSceneManager;
import com.mygdx.nutrieats.adventurescene.gameinterface.AdventureGameInterfaceManager;

import eatengine.Util;

/**
 * Composition class to create coin count interface on top-right
 *
 */
public class CoinInterface {
	
	private final Vector2 IMAGE_POSITION_OFFSET = new Vector2(-180f, -190f);
	private final Vector2 IMAGE_SCALE = new Vector2(0.25f, 0.25f);
	
	private static final String COIN_TEXTURE_PATH = "sprites\\coin.png";
	private static String coinTextureId;
	
	private final Vector2 TEXT_POSITION_OFFSET = new Vector2(-100, -50f);
	private final float TEXT_SCALE = 1;
	
	private Vector2 imagePosition;
	
	private Coin coinImage;
	private CoinText coinText;
	
	public CoinInterface(Vector2 screenSize, IGameInterfaceManager interfaceManager, int initialCoinCount) {
		if (coinTextureId == null || coinTextureId == "") {
			coinTextureId = interfaceManager.createTextureId(COIN_TEXTURE_PATH);
		}
		
		createCoin(screenSize.x, screenSize.y, interfaceManager);
		createText(screenSize.x, screenSize.y, interfaceManager, initialCoinCount);
	}
	
	public CoinInterface(float screenWidth, float screenHeight, IGameInterfaceManager interfaceManager, int initialCoinCount) {
		if (coinTextureId == null || coinTextureId == "") {
			coinTextureId = interfaceManager.createTextureId(COIN_TEXTURE_PATH);
		}
		
		createCoin(screenWidth, screenHeight, interfaceManager);
		createText(screenWidth, screenHeight, interfaceManager, initialCoinCount);
	}
	
	private void createCoin(float screenWidth, float screenHeight, IGameInterfaceManager interfaceManager) {
		imagePosition = Util.addVec2(IMAGE_POSITION_OFFSET, new Vector2(screenWidth, screenHeight));
		Sprite coinSprite = interfaceManager.getSpriteByTextureId(coinTextureId);
		
		coinImage = new Coin("Interface Coin Image", coinSprite, IMAGE_SCALE, imagePosition);
		
		interfaceManager.getSceneReference().addGameObjectToRoot(coinImage);
	}
	
	private void createText(float screenWidth, float screenHeight, IGameInterfaceManager interfaceManager, int initialCoinCount) {
		Vector2 textPosition = Util.addVec2(TEXT_POSITION_OFFSET, new Vector2(screenWidth, screenHeight));
		coinText = new CoinText(textPosition, TEXT_SCALE, initialCoinCount);
		
		interfaceManager.getSceneReference().addGameObjectToRoot(coinText);
	}
	
	public void incrementCoinCount() {
		coinText.incrementAmount();
	}
	
	public void decrementCoinCount() {
		coinText.decrementAmount();
	}
	
	public Vector2 getCoinImagePosition() {
		return imagePosition;
	}
}
