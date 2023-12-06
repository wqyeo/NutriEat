package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.GameSceneManager;
import com.mygdx.nutrieats.NutriEats;

import eatengine.EatEngine;
import eatengine.Scene;

public final class SampleSceneManager extends GameSceneManager {
	
	private SampleLevel sampleLevel;
	private SampleMainMenu mainMenu;
	private NutriEats coreGameRef;
	
	private SampleSceneState sceneState;
	private EatEngine engineRef;
	
	public SampleSceneManager(NutriEats core, EatEngine eatEngineRef) {
		 engineRef = eatEngineRef;
		 sampleLevel = new SampleLevel(this);
		 mainMenu = new SampleMainMenu(this);
		 
		 sceneState = SampleSceneState.SampleMenu;
		 coreGameRef = core;
	}
	
	public void changeSceneState(SampleSceneState newState) {
		sceneState = newState;
		
		if (newState == SampleSceneState.EXIT) {
			Gdx.app.exit();
		} else {
			coreGameRef.notifySceneChange();
		}
	}
	
	public Scene getScene() {
		if (sceneState == SampleSceneState.SampleLevel){
			return sampleLevel.getScene();
		} else {
			return mainMenu.getScene();
		}
	}
	
	public EatEngine getEngineReference() {
		return engineRef;
	}
}
