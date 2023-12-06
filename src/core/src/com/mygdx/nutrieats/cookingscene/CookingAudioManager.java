package com.mygdx.nutrieats.cookingscene;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.nutrieats.adventurescene.entities.CharacterMood;

import eatengine.GameObject;
import eatengine.components.audio.MusicSource;
import eatengine.components.audio.SoundSource;

class CookingAudioManager extends GameObject {
	
	private static final float MUSIC_VOLUME = 0.1f;
	private static final String MUSIC_PATHS[] = new String[] {
			"music\\Cook_Track_0.mp3",
			"music\\Cook_Track_1.mp3",
			"music\\Cook_Track_2.mp3",
			"music\\Cook_Track_3.mp3"
	};
	
	private static final float SOUND_VOLUME = 0.15f;
	private static final Map<CookingAudioType, String> SOUND_EFFECT_LOCATION_MAPPING;

	static {
		SOUND_EFFECT_LOCATION_MAPPING = new HashMap<CookingAudioType, String>();
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.BUTTON_CLICK, "sound_effects\\BUTTON_CLICK.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.INGREDIENT_LAND,"sound_effects\\INGREDIENT_LAND.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.COIN_GET,"sound_effects\\COIN_GET.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.ORDER_SERVED, "sound_effects\\ORDER_SERVED.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.NEW_ORDER,"sound_effects\\NEW_ORDER.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(CookingAudioType.ITEM_COLLECT,"sound_effects\\ITEM_COLLECT.mp3");
	}
	
	private final Map<CookingAudioType, SoundSource> sourceSourceTypes;
	
	private MusicSource musicSource;
	
	public CookingAudioManager() {
		super("Cooking Music");
		
		sourceSourceTypes = new HashMap<CookingAudioType, SoundSource>();
		createMusicComponent();
		createSoundComponents();
	}

	@Override
	protected void onStart() {
		musicSource.play();
	}
	
	public void playSoundByType(CookingAudioType audioType) {
		sourceSourceTypes.get(audioType).play();
	}
	
	private void createSoundComponents() {
		for (Entry<CookingAudioType, String> entry : SOUND_EFFECT_LOCATION_MAPPING.entrySet()) {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal(entry.getValue()));
			SoundSource soundSource = new SoundSource(sound, 10, SOUND_VOLUME);
			sourceSourceTypes.put(entry.getKey(), soundSource);
		}
	}
	
	private void createMusicComponent() {
		Random rand = new Random();
		int pickedSoundIndex = rand.nextInt(MUSIC_PATHS.length);
		Music music = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_PATHS[pickedSoundIndex]));
		musicSource = new MusicSource(music, MUSIC_VOLUME);
		musicSource.setLooping(true);
		this.addComponent(musicSource);
	}
}
