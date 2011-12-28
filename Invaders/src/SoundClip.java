import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {
	private Clip clip = null;

	public SoundClip(String fileName) {
		InputStream stream = ClassLoader.getSystemResourceAsStream(fileName);
		AudioInputStream sound;
		try {
			if ((sound = AudioSystem.getAudioInputStream(stream)) != null) {
				clip = AudioSystem.getClip();
				clip.open(sound);
			}
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
			if (clip.isRunning())
				clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public boolean isPlaying() {
		if (clip != null)
			return (clip.isRunning());

		return (false);
	}
}
