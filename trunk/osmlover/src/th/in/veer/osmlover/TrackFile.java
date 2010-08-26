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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;
import javax.microedition.io.OutputConnection;
import javax.microedition.io.file.FileConnection;

class TrackFile {

    private OutputConnection outConn;
    private PrintStream out;
    private String filename;
    private String path;

    TrackFile() {
        filename = "osmlover.gpx";
        path = "file:///E:/" + filename;
    }

    private void create(String path) throws IOException {
        FileConnection conn = (FileConnection)Connector.open(path);
        if(!conn.exists())
            conn.create();
        conn.close();
    }

    public void init() throws IOException {
        create(path);
        outConn = (OutputConnection)Connector.open(path, Connector.WRITE);
        out = new PrintStream(outConn.openOutputStream());
        out.println(GpxUtil.getHeader());
    }

    public void log(double lat, double lon, double ele,
            long timestamp, String note) {
        // TODO: add note
        out.println(GpxUtil.toGpx(lat, lon, ele, timestamp, note));
    }

    public void close() throws IOException {
        out.println(GpxUtil.getFooter());
        out.close();
        outConn.close();
    }
}
