package com.mygdx.nutrieats.adventurescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.GameSceneManager;
import com.mygdx.nutrieats.NutriEats;
import com.mygdx.nutrieats.PlayerData;
import com.mygdx.nutrieats.SceneManagerType;
import com.mygdx.nutrieats.adventurescene.entities.AdventurePlayer;
import com.mygdx.nutrieats.adventurescene.gameinterface.AdventureGameInterfaceManager;
import com.mygdx.nutrieats.cookingsystem.FoodTag;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.EatEngine;
import eatengine.Scene;

public class AdventureSceneManager extends GameSceneManager  {

	private final float screenWidth;
	private final float screenHeight;

	private NutriEats coreRef;
	private EatEngine engineRef;

	private Scene adventureScene;
	private AdventureGameInterfaceManager gameIntefaceManager;
	private AdventurePlayer player;
	
	private AdventureSoundManager soundManager;

	public AdventureSceneManager(NutriEats coreRef, EatEngine engineRef) {
		this.coreRef = coreRef;
		this.engineRef = engineRef;

		screenWidth = Gdx.graphics.getWidth() / 1.2f;
		screenHeight = Gdx.graphics.getHeight() / 1.2f;
		
		adventureScene = new Scene("Adventure Scene", screenWidth, screenHeight);
		gameIntefaceManager = new AdventureGameInterfaceManager(this);
		createAdventureScene();
		
		soundManager = new AdventureSoundManager();
		adventureScene.addGameObjectToRoot(soundManager);
	}
	
	public void playSoundByType(AdventureAudioType audioType) {
		soundManager.playSoundByType(audioType);
	}

	private void createAdventureScene() {

		AdventureWorld adventureWorld = new AdventureWorld(this);
		adventureScene.addGameObjectToRoot(adventureWorld);

		Dice dice = createDiceObject();
		adventureScene.addGameObjectToRoot(dice);

		player = new AdventurePlayer(dice, this, adventureWorld);
		adventureScene.addGameObjectToRoot(player);
	}

	private Dice createDiceObject() {
		Dice diceObject = new Dice(this);

		return diceObject;
	}

	@Override
	public Scene getScene() {
		return adventureScene;
	}

	public float getScreenWidth() {return screenWidth;}

	public float getScreenHeight() {return screenHeight;}

	public String createTextureId(String texturePath) {
		return engineRef.createTexutre(texturePath);
	}

	public Sprite requestSpriteByTexture(String textureId) {
		return engineRef.createSpriteFromTextureId(textureId);
	}
	
	public Vector2 getPlayerPosition() {
		return player.getWorldPosition();
	}
	
	public void triggerAddCoins(int amount) {
		// TODO: Animations/UI Change
		coreRef.getPlayerData().addCoins(amount);
		gameIntefaceManager.triggerCollectCoin(amount);
	}
	
	/**
	 * Use it to block global button inputs for the player.
	 * (Player shouldn't eat clicks that was used for buttons)
	 */
	public void signalButtonClicked() {
		player.signalButtonClicked();
	}
	
	/**
	 * Use it to block global button inputs for player.
	 * (Player shouldn't be able to do anything when viewing menus or inventory, etc)
	 * @param isViewing
	 */
	public void signalViewingInterface(boolean isViewing) {
		player.signalInInterface(isViewing);
	}
	
	/**
	 * Append a random item to the current game data's inventory by given food tag.
	 * @param foodTag
	 * @return The random item appended.
	 */
	public Item appendRandomItemToInventoryByTag(FoodTag foodTag) {
		GameData gameData = coreRef.getGameData();
		PlayerData playerData = coreRef.getPlayerData();
		
		Item itemAppenededItem = gameData.fetchRandomItemByFoodTag(foodTag);
		playerData.insertItem(itemAppenededItem);
		
		gameIntefaceManager.triggerShowItemCollected(itemAppenededItem);
		return itemAppenededItem;
	}

	
	public GameData getGameData() {
		return coreRef.getGameData();
	}
	
	public PlayerData getPlayerData() {
		return coreRef.getPlayerData();
	}
	
	public Vector2 getScreenSize() {
		return new Vector2(screenWidth, screenHeight);
	}
	
	public void triggerToCookingScene() {
		coreRef.shiftToScene(SceneManagerType.COOKING);
	}
}
