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
    public static final int LOCATION_TEST = 3;

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
        cards.add(new Card(8, LOCATION_PLAYGROUND, "射球", R.drawable.shoot, -1));
        cards.add(new Card(9, LOCATION_PLAYGROUND, "溜冰", R.drawable.skate, -1));
        cards.add(new Card(10, LOCATION_PLAYGROUND, "滑板", R.drawable.skateboard, -1));
        cards.add(new Card(11, LOCATION_PLAYGROUND, "游泳池", R.drawable.swimming_pool, -1));
        cards.add(new Card(12, LOCATION_PLAYGROUND, "網球", R.drawable.tennis_ball, -1));
        cards.add(new Card(13, LOCATION_PLAYGROUND, "網球手", R.drawable.tennis_player, -1));
        cards.add(new Card(14, LOCATION_PLAYGROUND, "跳水", R.drawable.to_dive, -1));
        cards.add(new Card(15, LOCATION_PLAYGROUND, "欖球", R.drawable.to_play_rugby, -1));
        cards.add(new Card(16, LOCATION_PLAYGROUND, "投擲", R.drawable.tothrow, -1));
        cards.add(new Card(17, LOCATION_PLAYGROUND, "網球場", R.drawable.volleyball_court, -1));
//        cards.add(new Card(, LOCATION_PLAYGROUND, "", R.drawable., -1));

        cards.add(new Card(18, LOCATION_CLASSROOM, "黑板", R.drawable.blackboard, -1));
        cards.add(new Card(19, LOCATION_CLASSROOM, "書", R.drawable.book, -1));
        cards.add(new Card(20, LOCATION_CLASSROOM, "計數機", R.drawable.calculator, -1));
        cards.add(new Card(21, LOCATION_CLASSROOM, "字母", R.drawable.character, -1));
        cards.add(new Card(22, LOCATION_CLASSROOM, "電腦", R.drawable.computers, -1));
        cards.add(new Card(23, LOCATION_CLASSROOM, "畫", R.drawable.drawing, -1));
        cards.add(new Card(24, LOCATION_CLASSROOM, "橡皮擦", R.drawable.erase, -1));
        cards.add(new Card(25, LOCATION_CLASSROOM, "圖書館", R.drawable.library, -1));
        cards.add(new Card(26, LOCATION_CLASSROOM, "椅子", R.drawable.chair, -1));
        cards.add(new Card(27, LOCATION_CLASSROOM, "字典", R.drawable.dictionary, -1));
        cards.add(new Card(28, LOCATION_CLASSROOM, "記事簿", R.drawable.logbook, -1));
        cards.add(new Card(29, LOCATION_CLASSROOM, "紙張", R.drawable.paper, -1));
        cards.add(new Card(30, LOCATION_CLASSROOM, "安靜", R.drawable.quiet, -1));
        cards.add(new Card(31, LOCATION_CLASSROOM, "坐下", R.drawable.sit, -1));
        cards.add(new Card(32, LOCATION_CLASSROOM, "學生", R.drawable.student, -1));
        cards.add(new Card(33, LOCATION_CLASSROOM, "學習", R.drawable.study, -1));
        cards.add(new Card(34, LOCATION_CLASSROOM, "老師", R.drawable.teacher, -1));
        cards.add(new Card(35, LOCATION_CLASSROOM, "謝謝", R.drawable.thank, -1));
        cards.add(new Card(36, LOCATION_CLASSROOM, "填顏色", R.drawable.to_colour, -1));
        cards.add(new Card(37, LOCATION_CLASSROOM, "合作", R.drawable.to_cooperate, -1));
        cards.add(new Card(38, LOCATION_CLASSROOM, "批改", R.drawable.to_correct, -1));
        cards.add(new Card(39, LOCATION_CLASSROOM, "洗手間", R.drawable.toilet, -1));
//        cards.add(new Card(, LOCATION_CLASSROOM, "", R.drawable., -1));

        cards.add(new Card(40, LOCATION_CANTEEN, "香蕉", R.drawable.banana, -1));
        cards.add(new Card(41, LOCATION_CANTEEN, "碗", R.drawable.bowl, -1));
        cards.add(new Card(42, LOCATION_CANTEEN, "麵包", R.drawable.bread, -1));
        cards.add(new Card(43, LOCATION_CANTEEN, "蘿蔔", R.drawable.carrot, -1));
        cards.add(new Card(44, LOCATION_CANTEEN, "櫻桃", R.drawable.cherries, -1));
        cards.add(new Card(45, LOCATION_CANTEEN, "巧克力", R.drawable.chocolates, -1));
        cards.add(new Card(46, LOCATION_CANTEEN, "咖啡", R.drawable.coffee, -1));
        cards.add(new Card(47, LOCATION_CANTEEN, "曲奇餅", R.drawable.cookie, -1));
        cards.add(new Card(48, LOCATION_CANTEEN, "青瓜", R.drawable.cucumber, -1));
        cards.add(new Card(49, LOCATION_CANTEEN, "蛋", R.drawable.egg, -1));
        cards.add(new Card(50, LOCATION_CANTEEN, "水果沙律", R.drawable.fruit_salad, -1));
        cards.add(new Card(51, LOCATION_CANTEEN, "葡萄", R.drawable.grapes, -1));
        cards.add(new Card(52, LOCATION_CANTEEN, "漢堡包", R.drawable.hamburger, -1));
        cards.add(new Card(53, LOCATION_CANTEEN, "熱狗", R.drawable.hot_dog, -1));
        cards.add(new Card(54, LOCATION_CANTEEN, "冰淇淋", R.drawable.ice_cream, -1));
        cards.add(new Card(55, LOCATION_CANTEEN, "米飯", R.drawable.rice, -1));
        cards.add(new Card(56, LOCATION_CANTEEN, "湯", R.drawable.soup, -1));
        cards.add(new Card(57, LOCATION_CANTEEN, "草莓", R.drawable.strawberry, -1));
        cards.add(new Card(58, LOCATION_CANTEEN, "蔬菜", R.drawable.vegetable, -1));
        cards.add(new Card(59, LOCATION_CANTEEN, "開水", R.drawable.water, -1));
        cards.add(new Card(60, LOCATION_CANTEEN, "西瓜", R.drawable.watermelon, -1));
//        cards.add(new Card(, LOCATION_CANTEEN, "", R.drawable., -1));

        cards.add(new Card(999, LOCATION_TEST, "字母", R.drawable.character, -1));
        cards.add(new Card(998, LOCATION_TEST, "電腦", R.drawable.computers, -1));
        cards.add(new Card(997, LOCATION_TEST, "畫", R.drawable.drawing, -1));

        return  cards;
    }


}
