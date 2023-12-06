package com.mygdx.nutrieats.cookingscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.GameSceneManager;
import com.mygdx.nutrieats.NutriEats;
import com.mygdx.nutrieats.PlayerData;
import com.mygdx.nutrieats.SceneManagerType;
import com.mygdx.nutrieats.adventurescene.AdventureAudioType;
import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;
import com.mygdx.nutrieats.adventurescene.gameinterface.AdventureGameInterfaceManager;
import com.mygdx.nutrieats.cookingscene.entities.Chef;
import com.mygdx.nutrieats.cookingscene.entities.Waiter;
import com.mygdx.nutrieats.cookingscene.gameinterface.CookingGameInterfaceManager;
import com.mygdx.nutrieats.cookingsystem.CustomerOrder;
import com.mygdx.nutrieats.gamedata.GameData;

import eatengine.EatEngine;
import eatengine.Scene;
import eatengine.components.SpriteRenderer;

public class CookingSceneManager extends GameSceneManager {
	private final float screenWidth;
	private final float screenHeight;

	private NutriEats coreRef;
	private EatEngine engineRef;

	private Scene cookingScene;
	
	private Chef chef;
	private Waiter waiter;
	private CookingGameInterfaceManager interfaceManager;
	private CookingAudioManager soundManager;

	public CookingSceneManager(NutriEats coreRef, EatEngine engineRef) {
		this.coreRef = coreRef;
		this.engineRef = engineRef;

		screenWidth = Gdx.graphics.getWidth() / 1.2f;
		screenHeight = Gdx.graphics.getHeight() / 1.2f;
		
		cookingScene = new Scene("Cooking Scene", screenWidth, screenHeight);
		createChef();
		createWaiter();
		
		interfaceManager = new CookingGameInterfaceManager(this);
		
		soundManager = new CookingAudioManager();
		cookingScene.addGameObjectToRoot(soundManager);
	}
	
	public void playSoundByType(CookingAudioType audioType) {
		soundManager.playSoundByType(audioType);
	}
	
	private void createWaiter() {
		waiter = new Waiter(this);
		cookingScene.addGameObjectToRoot(waiter);
	}
	
	private void createChef() {
		chef = new Chef(this);
		cookingScene.addGameObjectToRoot(chef);
	}
	
	@Override
	public Scene getScene() {
		return cookingScene;
	}
	
	public Vector2 getScreenSize() {
		return new Vector2(screenWidth, screenHeight);
	}
	
	public String createTexture(String texturePath) {
		return engineRef.createTexutre(texturePath);
	}
	
	public Sprite createSpriteByTextureId(String textureId) {
		return engineRef.createSpriteFromTextureId(textureId);
	}
	
	public PlayerData getPlayerData() {
		return coreRef.getPlayerData();
	}

	public GameData getGameData() {
		return coreRef.getGameData();
	}
	
	public void setOrderForChef() {
		GameData gameData = getGameData();
		CustomerOrder customerOrder = gameData.getRandomCustomerOrder();
		interfaceManager.setCustomerOrder(customerOrder);
	}
	
	public Waiter getWaiter() {
		return waiter;
	}
	
	public Chef getChef() {
		return chef;
	}
	
	public void changeToAdventureScene() {
		coreRef.shiftToScene(SceneManagerType.ADVENTURE);
	}
	
	public void notifyFoodServed() {
		interfaceManager.getCoinRewardFromOrder();
	}
}
