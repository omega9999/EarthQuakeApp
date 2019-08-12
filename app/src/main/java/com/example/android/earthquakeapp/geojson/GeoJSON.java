
package com.example.android.earthquakeapp.geojson;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * generated from http://www.jsonschema2pojo.org/
 * using input generated from http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10
 * for documentation see
 * https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php
 * https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson_detail.php
 */
class GeoJSON implements Serializable, Parcelable {

    /**
     * convert string json into GeoJSON object
     *
     * @param json string to convert
     * @return {@code GeoJSON} from JSON string
     */
    @NonNull
    @CheckResult
    static GeoJSON createFromJSON(@NonNull final String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GeoJSON.class);
    }

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("bbox")
    @Expose
    private List<Double> bbox = null;
    public final static Parcelable.Creator<GeoJSON> CREATOR = new Creator<GeoJSON>() {
        public GeoJSON createFromParcel(Parcel in) {
            return new GeoJSON(in);
        }

        public GeoJSON[] newArray(int size) {
            return (new GeoJSON[size]);
        }
    };
    private final static long serialVersionUID = 5245036494706619446L;

    private GeoJSON(Parcel in) {
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.metadata = ((Metadata) in.readValue((Metadata.class.getClassLoader())));
        in.readList(this.features, (com.example.android.earthquakeapp.geojson.Feature.class.getClassLoader()));
        in.readList(this.bbox, (java.lang.Double.class.getClassLoader()));
    }

    public GeoJSON() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeoJSON withType(String type) {
        this.type = type;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public GeoJSON withMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public GeoJSON withFeatures(List<Feature> features) {
        this.features = features;
        return this;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public GeoJSON withBbox(List<Double> bbox) {
        this.bbox = bbox;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(metadata);
        dest.writeList(features);
        dest.writeList(bbox);
    }

    public int describeContents() {
        return 0;
    }

}
