package com.mygdx.nutrieats.mainmenuscene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import eatengine.GameObject;
import eatengine.components.audio.MusicSource;

class MainMenuAudioSource extends GameObject {
	
	private static final float MUSIC_VOLUME = 0.1f;
	private static final String SOUND_PATHS[] = new String[] {
			"music\\MainMenu_Track_0.mp3",
			"music\\MainMenu_Track_1.mp3"
	} ;
	
	private MusicSource musicSource;
	
	public MainMenuAudioSource() {
		super("Main Menu Aduio Source");
		
		createMusicComponent();
	}
	
	@Override
	protected void onStart() {
		musicSource.play();
	}
	
	private void createMusicComponent() {
		Random rand = new Random();
		int pickedSoundIndex = rand.nextInt(SOUND_PATHS.length);
		Music music = Gdx.audio.newMusic(Gdx.files.internal(SOUND_PATHS[pickedSoundIndex]));
		musicSource = new MusicSource(music, MUSIC_VOLUME);
		musicSource.setLooping(true);
		this.addComponent(musicSource);
	}
	
}
