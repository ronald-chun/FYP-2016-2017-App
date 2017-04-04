package com.cuhk.ieproject.model;

import com.cuhk.ieproject.R;

import java.util.ArrayList;

/**
 * Created by anson on 7/11/2016.
 */
public class Card {
    private int id;
    private int location;
    private String name;
    private String imagePath;
    private int soundPath;
    private boolean isPhoto;

    public static final int LOCATION_NULL = -1;
    public static final int LOCATION_PLAYGROUND = 0;
    public static final int LOCATION_CANTEEN = 1;
    public static final int LOCATION_CLASSROOM = 2;
    public static final int LOCATION_RUBBISH = 999;

    public Card(int id, int location, String name, String imagePath, int soundPath, boolean isPhoto){
        super();
        this.id = id;
        this.location = location;
        this.name = name;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
        this.isPhoto = isPhoto;
    }

    public int getId() {
        return id;
    }

    public int getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getSoundPath() {
        return soundPath;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public static ArrayList<Card> dummy() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(0, LOCATION_PLAYGROUND, "籃球", "basketball", -1, false));
        cards.add(new Card(1, LOCATION_PLAYGROUND, "足球", "football_ball", -1, false));
        cards.add(new Card(2, LOCATION_PLAYGROUND, "乒乓球", "play_ping_pong", -1, false));
        cards.add(new Card(3, LOCATION_PLAYGROUND, "網球", "tennis", -1, false));
        cards.add(new Card(4, LOCATION_PLAYGROUND, "跑步", "to_run", -1, false));
        cards.add(new Card(5, LOCATION_PLAYGROUND, "游水", "to_swim", -1, false));
        cards.add(new Card(6, LOCATION_PLAYGROUND, "運動員", "athlete", -1, false));
        cards.add(new Card(7, LOCATION_PLAYGROUND, "Test", "https://cdn.pixabay.com/photo/2012/11/09/07/39/ball-65471_960_720.jpg", -1, false));
        cards.add(new Card(8, LOCATION_PLAYGROUND, "跳繩", "skipping_rope", -1, false));


        cards.add(new Card(9, LOCATION_CLASSROOM, "黑板", "blackboard", -1, false));
        cards.add(new Card(10, LOCATION_CLASSROOM, "書", "book", -1, false));
        cards.add(new Card(11, LOCATION_CLASSROOM, "計數機", "calculator", -1, false));
        cards.add(new Card(12, LOCATION_CLASSROOM, "字母", "character", -1, false));
        cards.add(new Card(13, LOCATION_CLASSROOM, "電腦", "computers", -1, false));
        cards.add(new Card(14, LOCATION_CLASSROOM, "畫", "drawing", -1, false));
        cards.add(new Card(15, LOCATION_CLASSROOM, "橡皮擦", "erase", -1, false));
        cards.add(new Card(16, LOCATION_CLASSROOM, "圖書館", "library", -1, false));
        cards.add(new Card(17, LOCATION_CLASSROOM, "書", "book", -1, false));
        cards.add(new Card(18, LOCATION_CLASSROOM, "Test", "https://192.168.65.99:8080/uploads/Desert.jpg", -1, false));

        return  cards;
    }
}


