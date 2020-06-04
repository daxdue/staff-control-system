package com.example.indoornavigation;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.simulator.BeaconSimulator;

import java.util.ArrayList;
import java.util.List;

public class TimedBeaconSimulator implements BeaconSimulator {
    protected static final String TAG = "TimedBeaconSimulator";
    private List<Beacon> beacons;

    public boolean USE_SIMULATED_BEACONS = true;

    public TimedBeaconSimulator() {
        beacons = new ArrayList<Beacon>();
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void createBasicSimulatedBeacons() {
        if(USE_SIMULATED_BEACONS) {
            Beacon beacon1 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("1").setRssi(-55).setTxPower(-55).build();
            Beacon beacon2 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("2").setRssi(-55).setTxPower(-55).build();
            Beacon beacon3 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("3").setRssi(-55).setTxPower(-55).build();
            Beacon beacon4 = new AltBeacon.Builder().setId1("DF7E1C79-43E9-44FF-886F-1D1F7DA6997A")
                    .setId2("1").setId3("4").setRssi(-55).setTxPower(-55).build();
            beacons.add(beacon1);
            beacons.add(beacon2);
            beacons.add(beacon3);
            beacons.add(beacon4);
        }
    }
}
