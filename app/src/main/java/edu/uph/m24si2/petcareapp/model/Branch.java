package edu.uph.m24si2.petcareapp.model;

public class Branch {

    private String name;
    private String address;
    private float rating;
    private int standardRoom;
    private int deluxeRoom;
    private int vipRoom;
    private String openHour;

    public Branch(String name,
                  String address,
                  float rating,
                  int standardRoom,
                  int deluxeRoom,
                  int vipRoom,
                  String openHour) {

        this.name = name;
        this.address = address;
        this.rating = rating;
        this.standardRoom = standardRoom;
        this.deluxeRoom = deluxeRoom;
        this.vipRoom = vipRoom;
        this.openHour = openHour;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public int getStandardRoom() {
        return standardRoom;
    }

    public int getDeluxeRoom() {
        return deluxeRoom;
    }

    public int getVipRoom() {
        return vipRoom;
    }

    public String getOpenHour() {
        return openHour;
    }
}