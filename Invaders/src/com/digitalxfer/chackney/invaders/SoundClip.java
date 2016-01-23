package com.digitalxfer.chackney.invaders;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.URL;

public class SoundClip {
	private Clip clip = null;
	public SoundClip(String fileName) {
		try {
			// Convert the filename to a URL
	        URL url = this.getClass().getClassLoader().getResource(fileName);
	        // Display the URL
			System.out.println("Loading audio clip: " + url);
			
	  		AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}
}
