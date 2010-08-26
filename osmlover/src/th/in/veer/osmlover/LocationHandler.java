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

import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

class LocationHandler implements LocationListener {

    private LocationProvider lp;
    private Criteria cr = new Criteria();
    private OsmLoverController controller;

    public LocationHandler(OsmLoverController yagController) {
        try {
            this.controller = yagController;
            lp = LocationProvider.getInstance(cr);
//			cr.setCostAllowed(true);
//			cr.setPreferredPowerConsumption(Criteria.NO_REQUIREMENT);
//			cr.setHorizontalAccuracy(5000);
//			cr.setVerticalAccuracy(5000);
        } catch (LocationException e) {
        }
    }

    public void startTracking() {
        setLocation();
    }

    private void setLocation() {
        lp.setLocationListener(this, 5, -1, -1);
    }

    public void locationUpdated(LocationProvider provider, Location location) {
        Coordinates c = location.getQualifiedCoordinates();

        if (c != null) {
            double lat = c.getLatitude();
            double lon = c.getLongitude();
            double ele = c.getAltitude();
            long timestamp = location.getTimestamp();
            controller.updateLocation(lat, lon, ele, timestamp);
            controller.updateLocationStatus(OsmLoverController.ACTIVE);
        } else {
            controller.updateLocationStatus(OsmLoverController.NOLOCATION);
        }
    }

    public void providerStateChanged(LocationProvider provider, int state) {
    }
}
