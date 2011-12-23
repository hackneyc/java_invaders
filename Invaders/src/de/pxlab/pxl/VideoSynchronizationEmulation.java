
package de.pxlab.pxl;

/** Emulates video sychronization capabilities. This class should not
be used directly but only via VideoSync objects.

    @author H. Irtel
    @version 0.1.0 
    @see VideoSync
*/

public class VideoSynchronizationEmulation implements VideoSynchronization {


    protected VideoSynchronizationEmulation() {
	// System.out.println("VideoSynchronizationEmulation initialization.");
    }


    /** Emulate an average delay of 7 ms for waiting for the vertical
        blanking interval. */
    public void waitForBeginOfVerticalBlank() { 
	try {
	    Thread.sleep(7);
	} catch (InterruptedException iex) {}
    }

    /** Does nothing but return immediately. */
    public void waitForEndOfVerticalBlank() {
    }

    public boolean inVerticalBlank() {
	return false;
    }

    public boolean init() {
	return true;
    }

    public void close() {
    }

}
