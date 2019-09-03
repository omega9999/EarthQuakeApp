package com.example.android.earthquakeapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.android.earthquakeapp.activity.UiUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

@Entity(tableName = "earth_quake")
public class Earthquake implements Parcelable {

    public Earthquake(@NonNull String id) {
        this.mId = id;
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
        dest.writeString(mId);
        dest.writeDouble((mMagnitude));
        dest.writeString(mPrimaryLocation);
        dest.writeString(mLocationOffset);
        dest.writeLong(mDate.getTime());
        dest.writeString(mUrl);
        dest.writeDouble((mLongitude));
        dest.writeDouble((mLatitude));
        dest.writeDouble((mDept));
        dest.writeString((mUrlRequest));
    }

    public Earthquake(@NonNull final Parcel in) {
        this(in.readString());
        this.setMagnitude((in.readDouble()));
        this.setPrimaryLocation(in.readString());
        this.setLocationOffset(in.readString());
        this.setDate(new Date(in.readLong()));
        this.setUrl(in.readString());
        this.setLongitude((in.readDouble()));
        this.setLatitude((in.readDouble()));
        this.setDept((in.readDouble()));
        this.setUrlRequest(in.readString());
    }


    /**
     * TODO make color scale absolute and relative scale (min/max) from preferences
     *
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

    @NonNull
    public String getId() {
        return mId;
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

    public String getUrlRequest() {
        return mUrlRequest;
    }

    public Earthquake setUrlRequest(String urlRequest) {
        this.mUrlRequest = urlRequest;
        return this;
    }

    public boolean isCoordinates() {
        return getLatitude() != null && getLongitude() != null;
    }

    @CheckResult
    @Nullable
    public LatLng getLatLng() {
        LatLng latLng = null;
        if (isCoordinates()) {
            latLng = new LatLng(getLatitude(), getLongitude());
        }
        return latLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Earthquake that = (Earthquake) o;
        return mId.equals(that.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_geo")
    private final String mId;


    @ColumnInfo(name = "magnitude")
    private Double mMagnitude;

    @ColumnInfo(name = "primary_location")
    private String mPrimaryLocation;

    @ColumnInfo(name = "location_offset")
    private String mLocationOffset;

    @ColumnInfo(name = "date")
    private Date mDate;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "longitude")
    private Double mLongitude;

    @ColumnInfo(name = "latitude")
    private Double mLatitude;

    @ColumnInfo(name = "dept")
    private Double mDept;

    @ColumnInfo(name = "url_request")
    private String mUrlRequest;

}
