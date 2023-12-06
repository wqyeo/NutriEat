package com.mygdx.nutrieats.debugscene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.mygdx.nutrieats.GameSceneManager;

import eatengine.Log;
import eatengine.Scene;

public final class DebugSceneManager extends GameSceneManager {
	private Scene debugScene;
	
	public DebugSceneManager() {
		initalizeDebugScene();
	}
	
	private void initalizeDebugScene() {
		debugScene = new Scene("TestScene", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Random r = new Random();
		// Random amount of rats to click on.
		for (int i = 0; i < r.nextInt(2, 5); ++i) {
			debugScene.addGameObjectToRoot(new ClickRat());
		}
		Log.Info("Created a bunch of rats");
		
		// Thonks
		ThonkMan thonks = new ThonkMan(10);
		debugScene.addGameObjectToRoot(thonks);
		Log.Info("Created thonks and added to test scene");
		
		// A word
		Wordy aWord = new Wordy();
		aWord.setParent(thonks);	
		Log.Warning("Created a word now");
		
		// Create music
		debugScene.addGameObjectToRoot(new AudioMan());
		Log.Debug("Made music");
	}
	
	public Scene getScene() {
		return debugScene;
	}
}
