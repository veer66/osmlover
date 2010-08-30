// Copyright (c) 2010 Vee Satayamas
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package th.in.veer.osmlover;

import th.in.veer.osmlover.model.TrackFile;
import th.in.veer.osmlover.view.NoteScreen;
import th.in.veer.osmlover.view.TrackerScreen;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDletStateChangeException;

public class OsmLoverController {

    private OsmLoverMidlet midlet;
    private Display display;
    private TrackerScreen trackerScreen;
    private NoteScreen noteScreen;
    private LocationHandler locationHandler;
    private TrackFile trackFile;
    public static final int INACTIVE = 0;
    public static final int ACTIVE = 1;
    public static final int NOLOCATION = 2;
    public static final int PAUSE = 0;
    public static final int TRACKING = 1;
    private int gpsStatus = OsmLoverController.INACTIVE;
    private int logStatus = OsmLoverController.PAUSE;
    private String note;

    public OsmLoverController(OsmLoverMidlet midlet) {
        this.midlet = midlet;
        display = Display.getDisplay(midlet);
        trackerScreen = new TrackerScreen(this);
        noteScreen = new NoteScreen(this);
        locationHandler = new LocationHandler(this);
        trackFile = new TrackFile();
        note = null;
    }

    public void onResume() {
        display.setCurrent(trackerScreen);
    }

    public void onStart() {
        onResume();
        locationHandler.startTracking();
        try {
            trackFile.init();
        } catch (IOException ex) {
            Alert alert = new Alert("Cannot open file", ex.getMessage(), null, null);
            display.setCurrent(alert, trackerScreen);
        }
    }

    public void updateLocationStatus(int status) {
        gpsStatus = status;
        trackerScreen.setStatus(gpsStatus, logStatus);
    }

    public void updateLocation(double lat, double lon, double ele,
            long timestamp) {
        trackerScreen.setLocation(lat, lon);

        if (logStatus == OsmLoverController.TRACKING) {
            trackFile.log(lat, lon, ele, timestamp, note);
            note = null;
        }
    }

    public void onExit() {
        try {
            display.setCurrent(null);
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        } catch (MIDletStateChangeException ex) {
        }
    }

    public void onLogPause() {
        if (logStatus != OsmLoverController.PAUSE) {
            logStatus = OsmLoverController.PAUSE;
            trackerScreen.setStatus(gpsStatus, logStatus);
        }
    }

    public void onLogStart() {
        if (logStatus != OsmLoverController.TRACKING) {
            logStatus = OsmLoverController.TRACKING;
            trackerScreen.setStatus(gpsStatus, logStatus);
        }
    }

    public void saveLog() throws IOException {
        trackFile.close();
    }

    public void onNote() {
        display.setCurrent(noteScreen);
    }

    public void onNoteOk(String note) {
        this.note = note;
        trackerScreen.setLatestNote(note);
        display.setCurrent(trackerScreen);
    }

    public void onSave() {
        try {
            saveLog();
        } catch (Exception e) {
            Alert alert = new Alert("Cannot save file", e.getMessage(), null, null);
            display.setCurrent(alert, trackerScreen);
        }
        onExit();
    }
}
