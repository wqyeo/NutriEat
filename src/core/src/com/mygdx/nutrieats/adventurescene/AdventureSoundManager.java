package com.mygdx.nutrieats.adventurescene;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;

import eatengine.GameObject;
import eatengine.components.audio.MusicSource;
import eatengine.components.audio.SoundSource;

class AdventureSoundManager extends GameObject {
	private static final float MUSIC_VOLUME = 0.1f;
	private static final String MUSIC_PATHS[] = new String[] {
			"music\\Stage_Track_0.mp3",
			"music\\Stage_Track_1.mp3",
			"music\\Stage_Track_2.mp3"
	};
	
	private static final float SOUND_VOLUME = 0.15f;
	private static final Map<AdventureAudioType, String> SOUND_EFFECT_LOCATION_MAPPING;

	static {
		SOUND_EFFECT_LOCATION_MAPPING = new HashMap<AdventureAudioType, String>();
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.BUTTON_CLICK, "sound_effects\\BUTTON_CLICK.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.ITEM_COLLECT, "sound_effects\\ITEM_COLLECT.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.COIN_GET, "sound_effects\\COIN_GET.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.DICE_LAND, "sound_effects\\DICE_LAND.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.DICE_THROW, "sound_effects\\DICE_THROW.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.STAGE_CLEAR, "sound_effects\\STAGE_CLEAR.mp3");
		SOUND_EFFECT_LOCATION_MAPPING.put(AdventureAudioType.BACK_TRACK, "sound_effects\\BACK_TRACK.mp3");
	}
	
	private final Map<AdventureAudioType, SoundSource> sourceSourceTypes;
	
	private MusicSource musicSource;
	
	public AdventureSoundManager() {
		super("Adventure Sound SOurce");
		
		sourceSourceTypes = new HashMap<AdventureAudioType, SoundSource>();
		createMusicComponent();
		createSoundComponents();
	}

	@Override
	protected void onStart() {
		musicSource.play();
	}
	
	public void playSoundByType(AdventureAudioType audioType) {
		sourceSourceTypes.get(audioType).play();
	}
	
	private void createSoundComponents() {
		for (Entry<AdventureAudioType, String> entry : SOUND_EFFECT_LOCATION_MAPPING.entrySet()) {
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
