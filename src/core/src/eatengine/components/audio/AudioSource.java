package eatengine.components.audio;

import eatengine.Component;
import eatengine.Log;

public abstract class AudioSource extends Component {
	public static final float MAX_VOLUME_VALUE = 1f;
	public static final float MIN_VOLUME_VALUE = 0f;
	
	public static final float MAX_PAN_VALUE = 1f;
	public static final float MIN_PAN_VALUE = -1f;
	
	private float volume;
	private float pan;
	
	private boolean looping;
	
	/**
	 *
	 * @param volume 0f ~ 1f
	 */
	public AudioSource(float volume) {
		if (volume < MIN_VOLUME_VALUE || volume > MAX_VOLUME_VALUE) {
			Log.Warning(getGameObject().name + " AudioSource had invalid volume input on creation, defaulting to 1.");
			this.volume = 1f;
		} else {
			this.volume = volume;
		}
		
		pan = 1f;
		looping = false;
	}
	
	public abstract void play();
	
	public abstract void stop();
	
	public final float getVolume() {
		return volume;
	}
	
	/**
	 * Set volume
	 * @param volume 0f to 1f; 1 is default.
	 */
	public final void setVolume(float volume) {
		if (volume < MIN_VOLUME_VALUE || volume > MAX_VOLUME_VALUE) {
			Log.Warning(getGameObject().name + " AudioSource had invalid volume input, defaulting to original.");
			return;
		}
		
		this.volume = volume;
		onVolumeSet(volume);
	}
	
	protected void onVolumeSet(float newVolume) {}
	
	public final float getPan() {
		return pan;
	}

	/**
	 * 
	 * @param pan -1f (full left) to 1f (full right); 0 is default.
	 */
	public final void setPan(float pan) {
		if (pan < MIN_PAN_VALUE || pan > MAX_PAN_VALUE) {
			Log.Warning(getGameObject().name + " AudioSource had invalid pan input, defaulting to original.");
			return;
		}
		this.pan = pan;
		onPanSet(pan);
	}
	
	protected void onPanSet(float newPan) {}
	
	public final boolean getLooping() {
		return looping;
	}
	
	public final void setLooping(boolean looping) {
		this.looping = looping;
		onLoopingSet(looping);
	}
	
	protected void onLoopingSet(boolean newLooping) {}
}
