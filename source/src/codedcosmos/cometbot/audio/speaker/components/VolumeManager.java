package codedcosmos.cometbot.audio.speaker.components;

import codedcosmos.hyperdiscord.utils.debug.Log;

public class VolumeManager {
	
	// Volume
	private int volume;
	private int minVolume = 30;
	private int maxVolume = 100;
	
	// State
	private boolean updateVolume = true;
	private boolean isReducingVolume = false;
	
	public VolumeManager() {
		volume = 100;
	}
	
	public void tick100ms(boolean isPlayingAudio) {
		int lastvolume = volume;
		
		if (isPlayingAudio && isReducingVolume) {
			volume = minVolume;
		} else {
			volume = maxVolume;
		}
		
		updateVolume = (lastvolume != volume);
	}
	
	public boolean shouldUpdateVolume() {
		return updateVolume;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public boolean isReducingVolume() {
		return isReducingVolume;
	}
	
	public void setReducingVolume(boolean isReducingVolume) {
		this.isReducingVolume = isReducingVolume;
	}
}
