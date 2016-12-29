package com.putuguna.sqllitetutorial;

/**
 * Created by putuguna on 03/11/16.
 */

public class ProfileModel {
    private int id;
    private String name;
    private String phoneNumber;

    public ProfileModel(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public ProfileModel() {
    }

    public ProfileModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ID = " + id + ", Name = " + name;
    }
}
