
package de.pxlab.pxl;

/** Windows Direct Draw native methods for vertical blanking
    synchronization. This class provides a static VideoSynchronization
    object which can be used for synchronizing drawing operations to
    the vertical blanking interval. Since this is a feature which
    requires native code the class also provides emulation code for
    those systems which do not have vertical blanking synchronization
    support.

    <p>To work properly the following requirements must be met:

    <ol><li>Only applications can use this code since it requires the
    native library pxlab.dll which may only be accessed by
    applications. Proper applets can access the methods from this
    class but they only will get the emulation code.

    <li>The methods used here rely on Windows DirectX functions and
    thus they only work if DirectX is installed.

    <li>Also note that vertical blanking synchronization only
    works if double buffering is switched off!
    </ol>

    <p>The vertical retrace blanking interval on my screen with
    1600x1200 at 75 Hz is approximately 0.025 ms. The visible interval
    is approximately 13.31 ms. These times have been measured with a
    WindowsPerformanceCounter timer object.

    @author H. Irtel
    @version 0.1.0 
    @see WindowsPerformanceCounter
*/

public class VideoSync {

    private static VideoSynchronization vsync = new VideoSynchronizationEmulation();
    private static boolean emulated = true;

    static {
		try {
	    		vsync = new DirectDrawVideoSynchronization();
			if (!vsync.init()) {
		    		System.out.println("VideoSync: Can't initialize video synchronization - use emulation.");
		    		vsync = new VideoSynchronizationEmulation();
			} else {
				emulated = false;
			}
		} catch (NoClassDefFoundError ndf) {
			System.out.println("VideoSync: Can't initialize video synchronization.");
	    	} catch (SecurityException sex) {
			System.out.println("VideoSync: Can't access pxsync.dll - use emulated video synchronization only.");
	    	} catch (UnsatisfiedLinkError ule) {
			System.out.println("VideoSync: Can't find pxsync.dll - use emulated video synchronization only.");
	    	}
    }


    /** Wait until the next vertical blanking interval starts. */
    public static void waitForBeginOfVerticalBlank() {
	vsync.waitForBeginOfVerticalBlank();
    }


    /** Wait until the current/next vertical blanking interval is finished. */
    public static void waitForEndOfVerticalBlank() {
	vsync.waitForBeginOfVerticalBlank();
    }


    /** Check whether we currently are in a vertical blanking
        interval. Note that vertical blanking duration is
        only approximately 0.02 to 0.03 ms. */
    public static boolean inVerticalBlank() {
	return vsync.inVerticalBlank();
    }

    public static boolean isEmulated() {
    	return emulated;
    }
}
