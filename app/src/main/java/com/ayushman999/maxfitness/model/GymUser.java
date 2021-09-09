package com.ayushman999.maxfitness.model;

public class GymUser {
    private String name;
    private String email;
    private String uniqueID;
    private String phoneNum;
    private String startDate;
    private String endDate;

    public GymUser(String name, String email, String uniqueID, String phoneNum) {
        this.name = name;
        this.email = email;
        this.uniqueID = uniqueID;
        this.phoneNum = phoneNum;
    }
    public GymUser(String name,String uniqueID,String endDate)
    {
        this.name=name;
        this.uniqueID=uniqueID;
        this.endDate=endDate;
    }
    public GymUser(String name, String email, String uniqueID, String phoneNum, String startDate, String endDate) {
        this.name = name;
        this.email = email;
        this.uniqueID = uniqueID;
        this.phoneNum = phoneNum;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public GymUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
