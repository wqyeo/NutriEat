package com.mygdx.nutrieats.mainmenuscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.GameSceneManager;
import com.mygdx.nutrieats.NutriEats;
import com.mygdx.nutrieats.SceneManagerType;

import eatengine.EatEngine;
import eatengine.Scene;
import eatengine.Util;

public class MainMenuSceneManager extends GameSceneManager {
	
	private static final Vector2 NEW_GAME_BUTTON_POSITION_OFFSET = new Vector2(-325f, -125f);
	private static final Vector2 CONTINUE_BUTTON_POSITION_OFFSET = new Vector2(-325f, -225f);
	private static final Vector2 EXIT_BUTTON_POSITION_OFFSET = new Vector2(-325f, -325f);
	
	private final float screenWidth;
	private final float screenHeight;
	
	private NutriEats coreRef;
	private EatEngine engineRef;

	private Scene mainMenuScene;

	public MainMenuSceneManager(NutriEats coreRef, EatEngine engineRef) {
		this.coreRef = coreRef;
		this.engineRef = engineRef;

		screenWidth = Gdx.graphics.getWidth() / 1.4f;
		screenHeight = Gdx.graphics.getHeight() / 1.4f;
		createMainMenuScene();
		MainMenuAudioSource audioSource = new MainMenuAudioSource();
		mainMenuScene.addGameObjectToRoot(audioSource);
	}
	
	private void createMainMenuScene() {
		mainMenuScene = new Scene("Main Menu", screenWidth, screenHeight);
		
		Vector2 buttonPosition = new Vector2(screenWidth /2f, screenHeight /2f);
		
		// Create newgame, continue and exit buttons.
		// Set positions for the button with the respective offsets.
		MenuButton newGameButton = new MenuButton("New Game Button", this, MenuButtonType.NEW_GAME);
		newGameButton.setPosition(Util.addVec2(buttonPosition, NEW_GAME_BUTTON_POSITION_OFFSET));
		mainMenuScene.addGameObjectToRoot(newGameButton);
		
		MenuButton continueButton = new MenuButton("Continue Button", this, MenuButtonType.CONTINUE);
		continueButton.setPosition(Util.addVec2(buttonPosition, CONTINUE_BUTTON_POSITION_OFFSET));
		mainMenuScene.addGameObjectToRoot(continueButton);
		
		MenuButton exitButton = new MenuButton("Exit Button", this, MenuButtonType.EXIT);
		exitButton.setPosition(Util.addVec2(buttonPosition, EXIT_BUTTON_POSITION_OFFSET));
		mainMenuScene.addGameObjectToRoot(exitButton);
	}

	@Override
	public Scene getScene() {
		return mainMenuScene;
	}
	
	public float getScreenWidth() {return screenWidth;}

	public float getScreenHeight() {return screenHeight;}

	public String createTextureId(String texturePath) {
		return engineRef.createTexutre(texturePath);
	}

	public Sprite requestSpriteByTexture(String textureId) {
		return engineRef.createSpriteFromTextureId(textureId);
	}
	
	public void triggerNewGame() {
		coreRef.shiftToScene(SceneManagerType.ADVENTURE);
	}

	
	public void triggerExitGame() {
		coreRef.closeGame();
	}
}
