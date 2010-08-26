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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class TrackerScreen extends Form implements CommandListener {

    private OsmLoverController controller;

    private StringItem statusItem;
    private StringItem locationItem;
    private StringItem annotLogItem;

    private Command startCommand;
    private Command pauseCommand;
    private Command exitCommand;

    public TrackerScreen(OsmLoverController controller) {
        super("OSM Lover");
        this.controller = controller;
        statusItem = new StringItem("Status", "Inactive");
        append(statusItem);
        locationItem = new StringItem("Location", "...");
        append(locationItem);
        annotLogItem = new StringItem("Annotation Log", "");
        append(annotLogItem);

        startCommand = new Command("Start", Command.ITEM, 0);
        addCommand(startCommand);
        pauseCommand = new Command("Pause", Command.ITEM, 0);
        addCommand(pauseCommand);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        addCommand(exitCommand);
        setCommandListener(this);
    }

    public void appendAnnotLog(String log) {
        annotLogItem.setText(annotLogItem.getText() + " " + log);
    }

    public void appendStatus(String status) {
        statusItem.setText(statusItem.getText() + " " + status);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (displayable == this) {
            if (command == startCommand) {
                controller.onLogStart();
            } else if (command == pauseCommand) {
                controller.onLogPause();
            } else if (command == exitCommand) {
                controller.onExit();
            }
        }
    }

    public void setLocation(double lat, double lon) {
        locationItem.setText(lat + " " + lon);
    }

    void setStatus(int gpsStatus, int logStatus) {
        if(gpsStatus == OsmLoverController.INACTIVE) {
            statusItem.setText("Inactive");
        } else {
            if(logStatus == OsmLoverController.TRACKING) {
                statusItem.setText("Tracking");
            } else {
                statusItem.setText("Pause");
            }
        }
    }
}
