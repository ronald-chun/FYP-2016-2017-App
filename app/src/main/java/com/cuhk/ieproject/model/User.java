package com.cuhk.ieproject.model;

/**
 * Created by anson on 7/11/2016.
 */
public class User {
    private int id;
    private String name;
    private int machinePath;
    private int sizeLevel;

    public User(int id, String name, int machinePath, int sizeLevel){
        super();
        this.id = id;
        this.name = name;
        this.machinePath = machinePath;
        this.sizeLevel = sizeLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMachinePath() {
        return machinePath;
    }

    public int getSizeLevel() {
        return sizeLevel;
    }

    public void setSizeLevel(int sizeLevel) {
        this.sizeLevel = sizeLevel;
    }
}
