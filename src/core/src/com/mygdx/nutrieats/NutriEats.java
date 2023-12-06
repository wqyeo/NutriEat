package com.mygdx.nutrieats;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.nutrieats.adventurescene.AdventureSceneManager;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.gamedata.GameData;
import com.mygdx.nutrieats.mainmenuscene.MainMenuSceneManager;
import com.mygdx.nutrieats.samplescene.SampleSceneManager;

import eatengine.EatEngine;

public class NutriEats extends ApplicationAdapter {
	
	private EatEngine eatEngine;
	private GameSceneManager sceneManager;
	
	private PlayerData currentPlayerData;
	private GameData gameData;
	
	@Override
	public void create() {
		
		SpriteBatch batch = new SpriteBatch();
		eatEngine = new EatEngine(batch);
		gameData = new GameData(eatEngine);
		currentPlayerData = new PlayerData(gameData);
		
		// DEBUG
		currentPlayerData.addAllItemByAmount(1);
		
		sceneManager = new MainMenuSceneManager(this, eatEngine);
		
		eatEngine.loadScene(sceneManager.getScene());
		eatEngine.start();
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		eatEngine.update(deltaTime);
	}

	@Override
	public void dispose() {
		eatEngine.dispose();
	}
	
	public void notifySceneChange() {
		eatEngine.loadScene(sceneManager.getScene());
	}

	public void shiftToScene(SceneManagerType managerType) {
		eatEngine.disposeScene();
	
		GameSceneManager newScene;
		if (SceneManagerType.ADVENTURE == managerType) {
			newScene = new AdventureSceneManager(this, eatEngine);
		} else if (SceneManagerType.COOKING == managerType) {
			newScene = new CookingSceneManager(this, eatEngine);
		} else {
			newScene = new MainMenuSceneManager(this, eatEngine);
		}
		
		eatEngine.loadScene(newScene.getScene());
	}
	
	public PlayerData getPlayerData() {
		return currentPlayerData;
	}
	
	public GameData getGameData() {
		return gameData;
	}
	
	public void closeGame() {
		eatEngine.dispose();
		Gdx.app.exit();
	}
}
