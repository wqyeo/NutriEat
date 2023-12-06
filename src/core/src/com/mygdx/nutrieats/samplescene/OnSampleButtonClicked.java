package com.mygdx.nutrieats.samplescene;

import eatengine.components.audio.SoundSource;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;

public class OnSampleButtonClicked implements ClickEventListener {
	
	private SampleSceneManager managerRef;
	private SampleSceneState state;
	
	private SoundSource buttonSound;
	
	public OnSampleButtonClicked(SampleSceneManager managerRef, SampleSceneState state) {
		this.managerRef = managerRef; 
		this.state = state;
		buttonSound = null;
	}

	@Override
	public void onClickEvent(Clickable clickedObject) {
		System.out.println("Clicked: " + state.toString());
		managerRef.changeSceneState(state);
		
		if (buttonSound != null) {
			buttonSound.play();
		}
	}
	
	
	public void setButtonSound(SoundSource soundSource) {
		buttonSound = soundSource;
	}
}