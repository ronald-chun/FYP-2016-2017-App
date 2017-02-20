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

        ibeacons.add(new iBeacon(0, "Playground", Card.LOCATION_PLAYGROUND, "[D1:AC:D9:7C:58:15]", "", -1, -1)); //lemon
        ibeacons.add(new iBeacon(1, "Canteen", Card.LOCATION_CANTEEN, "[CA:8E:FF:DA:A4:C5]", "", -1, -1)); //淺綠
//        ibeacons.add(new iBeacon(2, "Classroom", Card.LOCATION_CLASSROOM, "[CF:FA:CB:FD:7F:BF]", "", -1, -1)); //深藍
//        ibeacons.add(new iBeacon(3, "Playground", Card.LOCATION_PLAYGROUND, "[DF:49:A6:CD:24:9B]", "", -1, -1)); //淺藍
//        ibeacons.add(new iBeacon(4, "Classroom", Card.LOCATION_CLASSROOM, "[F0:F4:78:E8:58:25]", "", -1, -1)); //淺藍

        ibeacons.add(new iBeacon(5, "Playground", Card.LOCATION_PLAYGROUND, "[F3:E9:CB:AB:CD:F6]", "", -1, -1)); //pink
        ibeacons.add(new iBeacon(6, "Classroom", Card.LOCATION_CLASSROOM, "[F2:A3:BF:FB:FD:5A]", "", -1, -1)); //purple
        ibeacons.add(new iBeacon(7, "Canteen", Card.LOCATION_CANTEEN, "[C5:95:18:55:90:43]", "", -1, -1)); //yellow

        ibeacons.add(new iBeacon(8, "Playground", Card.LOCATION_PLAYGROUND, "F0:F4:78:E8:58:25]", "", -1, -1));  // Chun's 1st Purple
        ibeacons.add(new iBeacon(9, "Canteen", Card.LOCATION_CANTEEN, "[F3:C7:5C:EB:92:CA]", "", -1, -1));  // Chun's 2nd Yellow
        ibeacons.add(new iBeacon(10, "Classroom", Card.LOCATION_CLASSROOM, "[CF:FA:CB:FD:7F:BF]", "", -1, -1));  // Fai
        ibeacons.add(new iBeacon(11, "Playground", Card.LOCATION_PLAYGROUND, "[DF:49:A6:CD:24:9B]", "", -1, -1));  // Kobe



        return ibeacons;
    }
}
