package eatengine.components.audio;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

import com.badlogic.gdx.audio.Sound;

public class SoundSource extends AudioSource {

	// Represents the max amount of this sounds that can be played at once.
	private int maxConcurrentSounds;
	
	private Sound sound;
	
	private float pitch;
	
	private Queue<Long> soundQueue;
	
	public SoundSource(Sound sound, int maxConcurrentSounds) {
		super(1f);
		this.sound = sound;
		this.maxConcurrentSounds = maxConcurrentSounds;
		soundQueue = new ArrayDeque<Long>();
		
		pitch = 1f;
	}
	
	public SoundSource(Sound sound, int maxConcurrentSounds, float volume) {
		super(volume);
		this.sound = sound;
		this.maxConcurrentSounds = maxConcurrentSounds;
		soundQueue = new ArrayDeque<Long>();
		pitch = 1f;
	}

//region getter/setters
	
	public final float getPitch() {
		return pitch;
	}

	/**
	 * 
	 * @param pitch 0.5f to 2.0f; 1f is default.
	 */
	public final void setPitch(float pitch) {
		if (pitch < 0.5f || pitch > 2.0f) {
			// TODO: Warning;
			return;
		}
		this.pitch = pitch;
		updateAllSoundsWithCurrentValues();
	}

	public final int getMaxConcurrentSounds() {
		return maxConcurrentSounds;
	}
//endregion
	
	@Override
	protected void onPanSet(float newPan) {
		updateAllSoundsWithCurrentValues();
	}
	
	@Override
	protected void onVolumeSet(float newVolume) {
		updateAllSoundsWithCurrentValues();
	}
	
	@Override
	protected void onLoopingSet(boolean looping) {
		updateAllSoundsWithCurrentValues();
	}
	
	protected void updateAllSoundsWithCurrentValues() {
		// TODO: Maybe change to 1.8 JRE and use method reference instead?
		soundQueue.forEach(new Consumer<Long>() {
		    @Override
		    public void accept(Long soundId) {
				sound.setLooping(soundId, getLooping());
				sound.setPan(soundId, getPan(), getVolume());
				sound.setPitch(soundId, pitch);
				sound.setVolume(soundId, getVolume());
		    }
		});
	}
	
	@Override
	public void dispose() {
		stop();
		sound.dispose();
	}

	@Override
	public void play() {
		long soundId;
		if (soundQueue.size() >= maxConcurrentSounds) {
			// Hit max concurrent sound size limit...
			// stop the last sound from queue.
			soundId = soundQueue.remove();
			sound.stop(soundId);
		}
		
		soundId = sound.play(getVolume(), getPitch(), getPan());
		soundQueue.add(soundId);
	}

	@Override
	public void stop() {
		sound.stop();
		soundQueue.clear();
		maxConcurrentSounds = 0;
	}
}
