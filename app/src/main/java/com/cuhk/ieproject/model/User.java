package com.cuhk.ieproject.model;

/**
 * Created by anson on 7/11/2016.
 */
public class User {
    private int id;
    private String name;
    private int machinePath;

    public User(int id, String name, int machinePath){
        super();
        this.id = id;
        this.name = name;
        this.machinePath = machinePath;
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
}
