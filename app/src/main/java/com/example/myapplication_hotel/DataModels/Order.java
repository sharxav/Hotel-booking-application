package com.example.myapplication_hotel.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hotel_name")
    @Expose
    private String hotelName;
    @SerializedName("num_of_rooms")
    @Expose
    private String numOfRooms;
    @SerializedName("number")
    @Expose
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(String numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}