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

    public void setMagnitude(Double magnitude) {
        this.mMagnitude = magnitude;
    }

    public String getPrimaryLocation() {
        return mPrimaryLocation;
    }

    public void setPrimaryLocation(String location) {
        this.mPrimaryLocation = location;
    }

    public String getLocationOffset() {
        return mLocationOffset;
    }

    public void setLocationOffset(String location) {
        this.mLocationOffset = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public Double getDept() {
        return mDept;
    }

    public void setDept(Double dept) {
        this.mDept = dept;
    }

    public String getUrlRequest() {
        return mUrlRequest;
    }

    public void setUrlRequest(String urlRequest) {
        this.mUrlRequest = urlRequest;
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

    @Nullable
    @ColumnInfo(name = "magnitude")
    private Double mMagnitude;

    @Nullable
    @ColumnInfo(name = "primary_location")
    private String mPrimaryLocation;

    @Nullable
    @ColumnInfo(name = "location_offset")
    private String mLocationOffset;

    @Nullable
    @ColumnInfo(name = "date")
    private Date mDate;

    @Nullable
    @ColumnInfo(name = "url")
    private String mUrl;

    @Nullable
    @ColumnInfo(name = "longitude")
    private Double mLongitude;

    @Nullable
    @ColumnInfo(name = "latitude")
    private Double mLatitude;

    @Nullable
    @ColumnInfo(name = "dept")
    private Double mDept;

    @Nullable
    @ColumnInfo(name = "url_request")
    private String mUrlRequest;

}
