package com.rallycallsoftware.cellojws.general.core;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioManager {

	private boolean playing = false;

	private Clip clip = null;

	public AudioManager() {

	}

	public void stop() {
		if (clip != null) {
			clip.stop();
			clip.close();
			playing = false;
		}

	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean playAudio(final String filename, final boolean loopContinuously) {
		stop();

		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			return false;
		}

		final File file = new File(filename);
		try {
			AudioInputStream input = AudioSystem.getAudioInputStream(file);
			clip.close();
			clip.open(input);
			if (loopContinuously) {
				// TODO: Uncomment this to turn music back on
				// clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				// TODO: Uncomment this to turn music back on
				// clip.start();
			}
		} catch (Exception e) {
			return false;
		}

		playing = true;
		return true;
	}

}
