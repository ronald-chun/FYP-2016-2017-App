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
    private int imagePath;
    private int soundPath;

    public static final int LOCATION_NULL = -1;
    public static final int LOCATION_PLAYGROUND = 0;
    public static final int LOCATION_CANTEEN = 1;
    public static final int LOCATION_CLASSROOM = 2;
    public static final int LOCATION_RUBBISH = 999;

    public Card(int id, int location, String name, int imagePath, int soundPath){
        super();
        this.id = id;
        this.location = location;
        this.name = name;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
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

    public int getImagePath() {
        return imagePath;
    }

    public int getSoundPath() {
        return soundPath;
    }

    public static ArrayList<Card> dummy() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(0, LOCATION_PLAYGROUND, "籃球", R.drawable.basketball, -1));
        cards.add(new Card(1, LOCATION_PLAYGROUND, "足球", R.drawable.football_ball, -1));
        cards.add(new Card(2, LOCATION_PLAYGROUND, "乒乓球", R.drawable.play_ping_pong, -1));
        cards.add(new Card(3, LOCATION_PLAYGROUND, "網球", R.drawable.tennis, -1));
        cards.add(new Card(4, LOCATION_PLAYGROUND, "跑步", R.drawable.to_run, -1));
        cards.add(new Card(5, LOCATION_PLAYGROUND, "游水", R.drawable.to_swim, -1));
        cards.add(new Card(6, LOCATION_PLAYGROUND, "運動員", R.drawable.athlete, -1));
        cards.add(new Card(7, LOCATION_PLAYGROUND, "跳繩", R.drawable.skipping_rope, -1));

        cards.add(new Card(8, LOCATION_CLASSROOM, "黑板", R.drawable.blackboard, -1));
        cards.add(new Card(9, LOCATION_CLASSROOM, "書", R.drawable.book, -1));
        cards.add(new Card(10, LOCATION_CLASSROOM, "計數機", R.drawable.calculator, -1));
        cards.add(new Card(11, LOCATION_CLASSROOM, "字母", R.drawable.character, -1));
        cards.add(new Card(12, LOCATION_CLASSROOM, "電腦", R.drawable.computers, -1));
        cards.add(new Card(13, LOCATION_CLASSROOM, "畫", R.drawable.drawing, -1));
        cards.add(new Card(14, LOCATION_CLASSROOM, "橡皮擦", R.drawable.erase, -1));
        cards.add(new Card(15, LOCATION_CLASSROOM, "圖書館", R.drawable.library, -1));
        cards.add(new Card(16, LOCATION_CLASSROOM, "黑板", R.drawable.blackboard, -1));
        cards.add(new Card(17, LOCATION_CLASSROOM, "書", R.drawable.book, -1));
        cards.add(new Card(18, LOCATION_CLASSROOM, "計數機", R.drawable.calculator, -1));

        cards.add(new Card(0, LOCATION_CANTEEN, "籃球", R.drawable.basketball, -1));

        return  cards;
    }


}
