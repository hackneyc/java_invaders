package de.pxlab.pxl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Windows native code video sychronization based on Microsoft's DirectDraw
 * library. This class should not be used directly but only via
 * VideoSynchronization objects which make sure that only a single object of
 * this class gets instantiated for any application.
 * 
 * @author H. Irtel
 * @version 0.1.0
 * @see VideoSync
 */

public class DirectDrawVideoSynchronization implements VideoSynchronization {

	static {
		File file = new File(System.getProperty("java.io.tmpdir")
				+ "pxsync.dll");
		try {
			InputStream inStream = ClassLoader
					.getSystemResourceAsStream("pxsync.dll");
			OutputStream outStream = new FileOutputStream(
					System.getProperty("java.io.tmpdir") + "pxsync.dll");
			byte[] buf = new byte[8192];
			int len;
			while ((len = inStream.read(buf)) != -1) {
				outStream.write(buf, 0, len);
			}
			inStream.close();
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Loading " + file.getAbsolutePath());
		System.load(file.getAbsolutePath());
	}

	protected DirectDrawVideoSynchronization() {
	}

	public native void waitForBeginOfVerticalBlank();

	public native void waitForEndOfVerticalBlank();

	public native boolean inVerticalBlank();

	public native boolean init();

	public native void close();

}
