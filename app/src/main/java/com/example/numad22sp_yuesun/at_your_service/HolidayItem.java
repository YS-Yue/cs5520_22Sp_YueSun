package com.example.numad22sp_yuesun.at_your_service;

public class HolidayItem {
    String date;
    String localName;
    String name;
    Boolean fixed;
    String countryCode;

    public HolidayItem(String date, String localName, String name, Boolean isFixed, String countryCode) {
        this.date = date;
        this.localName = localName;
        this.name = name;
        this.fixed = isFixed;
        this.countryCode = countryCode;
    }

    public String getDate() {
        return date;
    }

    public String getLocalName() {
        return localName;
    }

    public String getName() {
        return name;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
