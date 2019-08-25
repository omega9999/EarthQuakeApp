package com.example.android.earthquakeapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

import com.example.android.earthquakeapp.activity.UiUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Earthquake implements Parcelable {

    public Earthquake() {
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(double2String(mMagnitude));
        dest.writeString(mPrimaryLocation);
        dest.writeString(mLocationOffset);
        dest.writeLong(mDate.getTime());
        dest.writeString(mUrl);
        dest.writeString(mId);
        dest.writeString(double2String(mLongitude));
        dest.writeString(double2String(mLatitude));
        dest.writeString(double2String(mDept));
    }

    public Earthquake(Parcel in) {
        this();
        if (in != null) {
            this.setMagnitude(string2Double(in.readString()));
            this.setPrimaryLocation(in.readString());
            this.setLocationOffset(in.readString());
            this.setDate(new Date(in.readLong()));
            this.setUrl(in.readString());
            this.setId(in.readString());
            this.setLongitude(string2Double(in.readString()));
            this.setLatitude(string2Double(in.readString()));
            this.setDept(string2Double(in.readString()));
        }
    }

    private static String double2String(@Nullable final Double aDouble){
        if (aDouble != null){
            return String.valueOf(aDouble);
        }
        else {
            return null;
        }
    }

    private static Double string2Double(@Nullable final String string){
        if (string != null){
            try {
                return Double.valueOf(string);
            }catch (NumberFormatException e){
                return null;
            }
        }
        else {
            return null;
        }
    }


    /**
     * TODO make color scale absolute and relative scale (min/max) from preferences
     * @return
     */
    public int getMagnitudeColorIdRef() {
        return UiUtils.getMagnitudeColorIdRef(this.mMagnitude);
    }


    public Double getMagnitude() {
        return mMagnitude;
    }

    public Earthquake setMagnitude(Double magnitude) {
        this.mMagnitude = magnitude;
        return this;
    }

    public String getPrimaryLocation() {
        return mPrimaryLocation;
    }

    public Earthquake setPrimaryLocation(String location) {
        this.mPrimaryLocation = location;
        return this;
    }

    public String getLocationOffset() {
        return mLocationOffset;
    }

    public Earthquake setLocationOffset(String location) {
        this.mLocationOffset = location;
        return this;
    }

    public Date getDate() {
        return mDate;
    }

    public Earthquake setDate(Date date) {
        this.mDate = date;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public Earthquake setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public String getId() {
        return mId;
    }

    public Earthquake setId(String id) {
        this.mId = id;
        return this;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public Earthquake setLongitude(Double longitude) {
        this.mLongitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Earthquake setLatitude(Double latitude) {
        this.mLatitude = latitude;
        return this;
    }

    public Double getDept() {
        return mDept;
    }

    public Earthquake setDept(Double dept) {
        this.mDept = dept;
        return this;
    }

    public boolean isCoordinates() {
        return getLatitude() != null && getLongitude() != null;
    }

    @CheckResult
    @Nullable
    public LatLng getLatLng(){
        LatLng latLng = null;
        if (isCoordinates()){
            latLng = new LatLng(getLatitude(), getLongitude());
        }
        return latLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (mId == null) return false;
        Earthquake that = (Earthquake) o;
        return mId.equals(that.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    private Double mMagnitude;
    private String mPrimaryLocation;
    private String mLocationOffset;
    private Date mDate;
    private String mUrl;
    private String mId;
    private Double mLongitude;
    private Double mLatitude;
    private Double mDept;


}
