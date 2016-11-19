package com.cuhk.ieproject.model;

import java.util.ArrayList;

/**
 * Created by anson on 8/11/2016.
 */
public class iBeacon {
    private int id;
    private String location;
    private int locationID;
    private String macAddress;
    private String UUID;
    private int major;
    private int minor;

    public iBeacon(int id, String location, int locationID, String macAddress, String UUID, int major, int minor){
        super();
        this.id = id;
        this.location = location;
        this.locationID = locationID;
        this.macAddress = macAddress;
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getLocationID() {
        return locationID;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getUUID() {
        return UUID;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public static ArrayList<iBeacon> dummy() {
        ArrayList<iBeacon> ibeacons = new ArrayList<>();
//        ibeacons.add(new iBeacon(0, "Playground", Card.LOCATION_PLAYGROUND, "[D1:AC:D9:7C:58:15]", "", -1, -1)); //lemon
        ibeacons.add(new iBeacon(0, "Playground", Card.LOCATION_PLAYGROUND, "[F0:F4:78:E8:58:25]", "", -1, -1)); // Chun

//        ibeacons.add(new iBeacon(1, "Canteen", Card.LOCATION_CANTEEN, "[CA:8E:FF:DA:A4:C5]", "", -1, -1)); //淺綠
        ibeacons.add(new iBeacon(1, "Canteen", Card.LOCATION_CANTEEN, "[CF:FA:CB:FD:7F:BF]", "", -1, -1)); // Fai

//        ibeacons.add(new iBeacon(2, "Classroom", Card.LOCATION_CLASSROOM, "[CF:FA:CB:FD:7F:BF]", "", -1, -1)); //深藍
        ibeacons.add(new iBeacon(2, "Classroom", Card.LOCATION_CLASSROOM, "[DF:49:A6:CD:24:9B]", "", -1, -1)); // Kobe

//        ibeacons.add(new iBeacon(3, "Playground", Card.LOCATION_PLAYGROUND, "[DF:49:A6:CD:24:9B]", "", -1, -1)); //淺藍
        return ibeacons; //hello fuck
    }
}
