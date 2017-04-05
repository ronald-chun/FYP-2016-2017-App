package com.cuhk.ieproject.model;

import java.util.ArrayList;

/**
 * Created by anson on 7/11/2016.
 */
public class Card {
    private int location;
    private int id;
    private String name;
    private String imagePath;
    private String soundPath;
    private String description;
    private boolean isPhoto;

    public static final int LOCATION_NULL = -1;
    public static final int LOCATION_PLAYGROUND = 0;
    public static final int LOCATION_CANTEEN = 1;
    public static final int LOCATION_CLASSROOM = 2;
    public static final int LOCATION_RUBBISH = 999;

    public Card(int location, int id, String name, String imagePath, String soundPath, String description, boolean isPhoto){
        super();
        this.location = location;
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
        this.description = description;
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

    public String getSoundPath() {
        return soundPath;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<Card> dummy() {
        ArrayList<Card> cards = new ArrayList<>();
//        cards.add(new Card(0, LOCATION_PLAYGROUND, "籃球", "basketball", -1, false));
//        cards.add(new Card(1, LOCATION_PLAYGROUND, "足球", "football_ball", -1, false));
//        cards.add(new Card(2, LOCATION_PLAYGROUND, "乒乓球", "play_ping_pong", -1, false));
//        cards.add(new Card(3, LOCATION_PLAYGROUND, "網球", "tennis", -1, false));
//        cards.add(new Card(4, LOCATION_PLAYGROUND, "跑步", "to_run", -1, false));
//        cards.add(new Card(5, LOCATION_PLAYGROUND, "游水", "to_swim", -1, false));
//        cards.add(new Card(6, LOCATION_PLAYGROUND, "運動員", "athlete", -1, false));
//        cards.add(new Card(7, LOCATION_PLAYGROUND, "Test", "https://cdn.pixabay.com/photo/2012/11/09/07/39/ball-65471_960_720.jpg", -1, false));
//        cards.add(new Card(8, LOCATION_PLAYGROUND, "跳繩", "skipping_rope", -1, false));
//
//
//        cards.add(new Card(9, LOCATION_CLASSROOM, "黑板", "blackboard", -1, false));
//        cards.add(new Card(10, LOCATION_CLASSROOM, "書", "book", -1, false));
//        cards.add(new Card(11, LOCATION_CLASSROOM, "計數機", "calculator", -1, false));
//        cards.add(new Card(12, LOCATION_CLASSROOM, "字母", "character", -1, false));
//        cards.add(new Card(13, LOCATION_CLASSROOM, "電腦", "computers", -1, false));
//        cards.add(new Card(14, LOCATION_CLASSROOM, "畫", "drawing", -1, false));
//        cards.add(new Card(15, LOCATION_CLASSROOM, "橡皮擦", "erase", -1, false));
//        cards.add(new Card(16, LOCATION_CLASSROOM, "圖書館", "library", -1, false));
//        cards.add(new Card(17, LOCATION_CLASSROOM, "書", "book", -1, false));
//        cards.add(new Card(18, LOCATION_CLASSROOM, "Test", "https://192.168.65.99:8080/uploads/Desert.jpg", -1, false));
        cards.add(new Card(7, 1, "廁所", "https://192.168.65.99:8080/uploads/01廁所.jpg", "/uploads/test.mp3", "廁所", false));
        cards.add(new Card(7, 2, "水", "https://192.168.65.99:8080/uploads/02水.jpg", "/uploads/test.mp3", "水", false));
        cards.add(new Card(7, 3, "飯", "https://192.168.65.99:8080/uploads/03飯.jpg", "/uploads/test.mp3", "飯", false));
        cards.add(new Card(7, 4, "蘋果", "https://192.168.65.99:8080/uploads/04蘋果.jpg", "/uploads/test.mp3", "蘋果", false));
        cards.add(new Card(7, 5, "梨", "https://192.168.65.99:8080/uploads/05梨.jpg", "/uploads/test.mp3", "梨", false));
        cards.add(new Card(7, 6, "肉", "https://192.168.65.99:8080/uploads/06肉.jpg", "/uploads/test.mp3", "肉", false));
        cards.add(new Card(7, 7, "粉麵", "https://192.168.65.99:8080/uploads/07粉麵.jpg", "/uploads/test.mp3", "粉麵", false));
        cards.add(new Card(7, 8, "雞翼", "https://192.168.65.99:8080/uploads/08雞翼.jpg", "/uploads/test.mp3", "雞翼", false));
        cards.add(new Card(7, 9, "麵包-1", "https://192.168.65.99:8080/uploads/09麵包-1.jpg", "/uploads/test.mp3", "麵包", false));
        cards.add(new Card(7, 10, "蛋糕", "https://192.168.65.99:8080/uploads/10蛋糕.jpg", "/uploads/test.mp3", "蛋糕", false));
        cards.add(new Card(7, 11, "旺旺仙貝", "https://192.168.65.99:8080/uploads/11旺旺仙貝.jpg", "/uploads/test.mp3", "旺旺仙貝", false));
        cards.add(new Card(7, 12, "益力多", "https://192.168.65.99:8080/uploads/12益力多.jpg", "/uploads/test.mp3", "益力多", false));
        cards.add(new Card(7, 13, "果汁", "https://192.168.65.99:8080/uploads/13果汁.jpg", "/uploads/test.mp3", "果汁", false));
        cards.add(new Card(7, 14, "百力滋", "https://192.168.65.99:8080/uploads/14百力滋.jpg", "/uploads/test.mp3", "百力滋", false));
        cards.add(new Card(7, 15, "蝦條", "https://192.168.65.99:8080/uploads/15蝦條.jpg", "/uploads/test.mp3", "蝦條", false));
        cards.add(new Card(7, 16, "餅乾", "https://192.168.65.99:8080/uploads/16餅乾.jpg", "/uploads/test.mp3", "餅乾", false));
        cards.add(new Card(7, 17, "匙羹", "https://192.168.65.99:8080/uploads/17匙羹.jpg", "/uploads/test.mp3", "匙羹", false));
        cards.add(new Card(7, 18, "杯", "https://192.168.65.99:8080/uploads/18杯.jpg", "/uploads/test.mp3", "杯", false));
        cards.add(new Card(7, 19, "食飯", "https://192.168.65.99:8080/uploads/19食飯.jpg", "/uploads/test.mp3", "食飯", false));
        cards.add(new Card(7, 20, "睇書", "https://192.168.65.99:8080/uploads/20睇書.jpg", "/uploads/test.mp3", "睇書", false));

        cards.add(new Card(8, 35, "熱", "https://192.168.65.99:8080/uploads/35熱.jpg", "/uploads/test.mp3", "熱", false));
        cards.add(new Card(8, 36, "廁所", "https://192.168.65.99:8080/uploads/01廁所.jpg", "/uploads/test.mp3", "廁所", false));
        cards.add(new Card(8, 37, "水", "https://192.168.65.99:8080/uploads/02水.jpg", "/uploads/test.mp3", "水", false));
        cards.add(new Card(8, 38, "睇書", "https://192.168.65.99:8080/uploads/03睇書.jpg", "/uploads/test.mp3", "睇書", false));
        cards.add(new Card(8, 39, "餅乾", "https://192.168.65.99:8080/uploads/03餅乾.jpg", "/uploads/test.mp3", "餅乾", false));
        cards.add(new Card(8, 40, "聽歌", "https://192.168.65.99:8080/uploads/04聽歌.jpg", "/uploads/test.mp3", "聽歌", false));
        cards.add(new Card(8, 41, "玩ipad", "https://192.168.65.99:8080/uploads/05玩ipad.jpg", "/uploads/test.mp3", "玩ipad", false));
        cards.add(new Card(8, 42, "畫畫", "https://192.168.65.99:8080/uploads/06畫畫.jpg", "/uploads/test.mp3", "畫畫", false));
        cards.add(new Card(8, 43, "刷牙", "https://192.168.65.99:8080/uploads/07刷牙.jpg", "/uploads/test.mp3", "刷牙", false));
        cards.add(new Card(8, 44, "抹面", "https://192.168.65.99:8080/uploads/08抹面.jpg", "/uploads/test.mp3", "抹面", false));
        cards.add(new Card(8, 45, "梳頭", "https://192.168.65.99:8080/uploads/09梳頭.jpg", "/uploads/test.mp3", "梳頭", false));
        cards.add(new Card(8, 46, "食飯", "https://192.168.65.99:8080/uploads/10食飯.jpg", "/uploads/test.mp3", "食飯", false));
        cards.add(new Card(8, 47, "玩", "https://192.168.65.99:8080/uploads/11玩.jpg", "/uploads/test.mp3", "玩", false));
        cards.add(new Card(8, 48, "-波-1", "https://192.168.65.99:8080/uploads/12-波-1.jpg", "/uploads/test.mp3", "波", false));
        cards.add(new Card(8, 49, "玩具車", "https://192.168.65.99:8080/uploads/13玩具車.jpg", "/uploads/test.mp3", "玩具車", false));
        cards.add(new Card(8, 50, "砌圖", "https://192.168.65.99:8080/uploads/14砌圖.jpg", "/uploads/test.mp3", "砌圖", false));
        cards.add(new Card(8, 51, "泡泡水", "https://192.168.65.99:8080/uploads/15泡泡水.jpg", "/uploads/test.mp3", "泡泡水", false));
        cards.add(new Card(8, 52, "琴", "https://192.168.65.99:8080/uploads/16琴.jpg", "/uploads/test.mp3", "琴", false));
        cards.add(new Card(8, 53, "積木", "https://192.168.65.99:8080/uploads/17積木.jpg", "/uploads/test.mp3", "積木", false));
        cards.add(new Card(8, 54, "鼓", "https://192.168.65.99:8080/uploads/18鼓.jpg", "/uploads/test.mp3", "鼓", false));



        return  cards;
    }


}


