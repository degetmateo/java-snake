package media;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sonido {
	public static Clip cargarSonido(String file) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds" + System.getProperty("file.separator") + file).getAbsoluteFile());
	        AudioFormat format = audioInputStream.getFormat();
	        DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip sound = (Clip)AudioSystem.getLine(info);
			sound.open(audioInputStream);
			return sound;
		} catch(Exception e) {
			return null;
		}
	}

	public static void ejecutarSonido(Clip clip) {
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public static void detenerSonido(Clip clip) {
		clip.stop();
	}

	public static void repetirSonido(Clip clip) {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void detenerBucle(Clip clip) {
		clip.loop(0);
	}
}