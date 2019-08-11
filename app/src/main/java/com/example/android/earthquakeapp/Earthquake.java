package com.example.android.earthquakeapp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Earthquake {

    public Earthquake(BigDecimal magnitude, String city, Date date) {
        this.mMagnitude = magnitude;
        this.mLocation = city;
        this.mDate = date;
    }

    public Earthquake(double magnitude, String city, String date) {
        this.mMagnitude = new BigDecimal(magnitude);
        this.mLocation = city;
        try {
            this.mDate = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(BigDecimal magnitude) {
        this.mMagnitude = magnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "mMagnitude=" + mMagnitude +
                ", mLocation='" + mLocation + '\'' +
                ", mDate=" + mDate +
                '}';
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd", Locale.ITALY);

    private BigDecimal mMagnitude;
    private String mLocation;
    private Date mDate;
}
