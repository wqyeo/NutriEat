package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import eatengine.GameObject;
import eatengine.components.audio.MusicSource;

public class SampleMusicOutput extends GameObject {

	private static final String MUSIC_FILE_PATH = "Follow_the_Trail.mp3";
	private MusicSource musicSource;
	
	public SampleMusicOutput() {
		super("MusicSample");
		addMusicComponent();
	}
	
	@Override
	protected void onStart() {
		musicSource.play();
	}
	
	private void addMusicComponent() {
		Music music = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_FILE_PATH));
		musicSource = new MusicSource(music, 0.1f);
		musicSource.setLooping(true);
		
		this.addComponent(musicSource);
	}
}
