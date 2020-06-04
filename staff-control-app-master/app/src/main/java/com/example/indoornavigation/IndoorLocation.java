package com.example.indoornavigation;

import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import androidx.appcompat.app.AppCompatActivity;

public class IndoorLocation extends AppCompatActivity implements BeaconConsumer{

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    private BeaconManager beaconManager;
    private double distances[];


    IndoorLocation() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setForegroundScanPeriod(2000);
        beaconManager.setForegroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(2000);
        beaconManager.setBackgroundBetweenScanPeriod(0);


        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    //textView.setText("The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    //beacons.iterator().next().getBeaconTypeCode();
                    distances = new double[beacons.size()];
                    int i = 0;
                    for (Beacon beacon : beacons) {
                        distances[i] = beacon.getDistance();
                        i++;
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    public double[] getDistances() {
        return distances;
    }

}
