package com.mygdx.nutrieats.debugscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import eatengine.GameObject;
import eatengine.components.audio.MusicSource;

public class AudioMan extends GameObject {
	
	MusicSource musicSource;

	public AudioMan() {
		super("AudioGuy");
		addMusicComponent();
	}

	@Override
	protected void onStart() {
		musicSource.play();
	}
	
	private void addMusicComponent() {
		Music music = Gdx.audio.newMusic(Gdx.files.internal("All_Systems_Go.mp3"));
		musicSource = new MusicSource(music, 0.1f);
		musicSource.setLooping(true);
		
		this.addComponent(musicSource);
	}
}
