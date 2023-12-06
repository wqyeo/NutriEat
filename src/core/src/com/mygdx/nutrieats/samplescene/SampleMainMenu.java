package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import eatengine.Scene;

public class SampleMainMenu {
	private Scene scene;
	
	private SampleSceneManager managerRef;
	
	public SampleMainMenu(SampleSceneManager managerRef) {
		this.managerRef = managerRef;
		scene = new Scene("SampleMainMenu", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		createMenu();
		scene.addGameObjectToRoot(new SampleMusicOutput());
	}
	
	private void createMenu() {
		createFirstButton();
		createSecondButton();
	}
	
	private void createFirstButton() {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		
		Vector2 buttonPosition = new Vector2(screenWidth / 2f, (screenHeight / 2f) + 100f);
		String buttonText = "Sample Level";
		OnSampleButtonClicked onClick = new OnSampleButtonClicked(managerRef, SampleSceneState.SampleLevel);
		SampleButton button = new SampleButton(buttonText + "_button", buttonText, buttonPosition, onClick);
		scene.addGameObjectToRoot(button);
	}
	
	private void createSecondButton() {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		
		Vector2 buttonPosition = new Vector2(screenWidth / 2f, (screenHeight /2f) - 25f);
		String buttonText = "Exit Game";
		OnSampleButtonClicked onClick = new OnSampleButtonClicked(managerRef, SampleSceneState.EXIT);
		SampleButton button = new SampleButton(buttonText + "_button", buttonText, buttonPosition, onClick);
		scene.addGameObjectToRoot(button);
	}
	
	public Scene getScene() {
		return scene;
	}
}
