package eatengine.components.audio;

import com.badlogic.gdx.audio.Music;

public class MusicSource extends AudioSource {
	private Music music;
	
	public MusicSource(Music music) {
		super(1f);
		this.music = music;
	}
	
	public MusicSource(Music music, float volume) {
		super(volume);
		this.music = music;
	}
	
	@Override
	public void dispose() {
		music.stop();
		music.dispose();
	}

	@Override
	public void play() {
		music.setVolume(getVolume());
		music.setPan(getPan(), getVolume());
		music.play();
	}

	@Override
	public void stop() {
		music.stop();
		
	}
	
	@Override
	protected void onPanSet(float newPan) {
		music.setPan(newPan, getVolume());
	}
	
	@Override
	protected void onVolumeSet(float newVolume) {
		music.setVolume(newVolume);
	}
	
	@Override
	protected void onLoopingSet(boolean looping) {
		music.setLooping(looping);
	}
}
