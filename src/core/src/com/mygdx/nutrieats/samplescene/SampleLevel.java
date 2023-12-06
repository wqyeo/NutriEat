package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import eatengine.Scene;

public class SampleLevel {
	private Scene scene;
	
	private SampleSceneManager managerRef;
	
	public SampleLevel(SampleSceneManager managerRef) {
		this.managerRef = managerRef;
		scene = new Scene("SampleLevel", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		createFloor();
		createChibi();
		createMainMenuButton();
	}
	
	private void createMainMenuButton() {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		
		Vector2 buttonPosition = new Vector2(screenWidth / 2f, (screenHeight / 2f) + 250f);
		String buttonText = "Main Menu";
		OnSampleButtonClicked onClick = new OnSampleButtonClicked(managerRef, SampleSceneState.SampleMenu);
		SampleButton button = new SampleButton(buttonText + "_button", buttonText, buttonPosition, onClick);
		scene.addGameObjectToRoot(button);
	}
	
	private void createFloor() {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		float worldUnitWidth = Tile.TILE_SIZE * Tile.TILE_SCALE;
		
		// TODO: Check sizing, or don't care.
		Vector2 position;
		for (int i = 0; i < screenWidth / worldUnitWidth + 1; i++) {
			position = new Vector2(i * worldUnitWidth, 0);
			scene.addGameObjectToRoot(new Tile(position));

			if (i > screenWidth / worldUnitWidth / 4 & i < screenWidth / worldUnitWidth / 4 * 3) {
				position = new Vector2(i * worldUnitWidth, screenHeight / 3);
				scene.addGameObjectToRoot(new Tile(position));
			}
			if (i < screenWidth / worldUnitWidth / 4 || i > screenWidth / worldUnitWidth / 4 * 3) {
				position = new Vector2(i * worldUnitWidth, screenHeight / 3 * 2);
				scene.addGameObjectToRoot(new Tile(position));
			}
		}
	}
	
	private void createChibi() {
		Vector2 position = new Vector2(400, 400);
		scene.addGameObjectToRoot(new ChibiCharacter(position, managerRef.getEngineReference()));
	}
	
	public Scene getScene() {
		return scene;
	}
}
