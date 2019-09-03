
package com.example.android.earthquakeapp.db.geojson;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata implements Serializable, Parcelable
{

    @SerializedName("generated")
    @Expose
    private long generated;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("api")
    @Expose
    private String api;
    @SerializedName("limit")
    @Expose
    private long limit;
    @SerializedName("offset")
    @Expose
    private long offset;
    @SerializedName("count")
    @Expose
    private long count;
    public final static Parcelable.Creator<Metadata> CREATOR = new Creator<Metadata>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        public Metadata[] newArray(int size) {
            return (new Metadata[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8760362209395153673L;

    protected Metadata(Parcel in) {
        this.generated = ((long) in.readValue((long.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((long) in.readValue((long.class.getClassLoader())));
        this.api = ((String) in.readValue((String.class.getClassLoader())));
        this.limit = ((long) in.readValue((long.class.getClassLoader())));
        this.offset = ((long) in.readValue((long.class.getClassLoader())));
        this.count = ((long) in.readValue((long.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Metadata() {
    }

    /**
     * 
     * @param limit
     * @param title
     * @param count
     * @param status
     * @param generated
     * @param api
     * @param offset
     * @param url
     */
    public Metadata(long generated, String url, String title, long status, String api, long limit, long offset, long count) {
        super();
        this.generated = generated;
        this.url = url;
        this.title = title;
        this.status = status;
        this.api = api;
        this.limit = limit;
        this.offset = offset;
        this.count = count;
    }

    public long getGenerated() {
        return generated;
    }

    public void setGenerated(long generated) {
        this.generated = generated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(generated);
        dest.writeValue(url);
        dest.writeValue(title);
        dest.writeValue(status);
        dest.writeValue(api);
        dest.writeValue(limit);
        dest.writeValue(offset);
        dest.writeValue(count);
    }

    public int describeContents() {
        return  0;
    }

}
