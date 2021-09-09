package com.ayushman999.maxfitness.model;

public class UserCheck {
    private String uniqueID;
    private String name;
    private String checkIn;
    private String timing;
    private String checkOut;

    public UserCheck() {
    }

    public UserCheck(String uniqueID, String name,String timing, String checkIn) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.checkIn = checkIn;
        this.timing=timing;
    }
    public UserCheck(String uniqueID,String name,String timing,String checkIn,String checkOut)
    {
        this.uniqueID = uniqueID;
        this.name = name;
        this.checkIn = checkIn;
        this.checkOut=checkOut;
        this.timing=timing;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }
}
