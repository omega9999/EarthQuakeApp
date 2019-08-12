
package com.example.android.earthquakeapp.geojson;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties implements Serializable, Parcelable
{

    @SerializedName("mag")
    @Expose
    private double mag;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("updated")
    @Expose
    private long updated;
    @SerializedName("tz")
    @Expose
    private long tz;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("felt")
    @Expose
    private long felt;
    @SerializedName("cdi")
    @Expose
    private double cdi;
    @SerializedName("mmi")
    @Expose
    private double mmi;
    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tsunami")
    @Expose
    private long tsunami;
    @SerializedName("sig")
    @Expose
    private long sig;
    @SerializedName("net")
    @Expose
    private String net;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ids")
    @Expose
    private String ids;
    @SerializedName("sources")
    @Expose
    private String sources;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("nst")
    @Expose
    private int nst;
    @SerializedName("dmin")
    @Expose
    private double dmin;
    @SerializedName("rms")
    @Expose
    private double rms;
    @SerializedName("gap")
    @Expose
    private double gap;
    @SerializedName("magType")
    @Expose
    private String magType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    public final static Parcelable.Creator<Properties> CREATOR = new Creator<Properties>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Properties createFromParcel(Parcel in) {
            return new Properties(in);
        }

        public Properties[] newArray(int size) {
            return (new Properties[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6366758129567766969L;

    protected Properties(Parcel in) {
        this.mag = ((double) in.readValue((double.class.getClassLoader())));
        this.place = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((long) in.readValue((long.class.getClassLoader())));
        this.updated = ((long) in.readValue((long.class.getClassLoader())));
        this.tz = ((long) in.readValue((long.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.detail = ((String) in.readValue((String.class.getClassLoader())));
        this.felt = ((long) in.readValue((long.class.getClassLoader())));
        this.cdi = ((double) in.readValue((double.class.getClassLoader())));
        this.mmi = ((double) in.readValue((double.class.getClassLoader())));
        this.alert = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tsunami = ((long) in.readValue((long.class.getClassLoader())));
        this.sig = ((long) in.readValue((long.class.getClassLoader())));
        this.net = ((String) in.readValue((String.class.getClassLoader())));
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.ids = ((String) in.readValue((String.class.getClassLoader())));
        this.sources = ((String) in.readValue((String.class.getClassLoader())));
        this.types = ((String) in.readValue((String.class.getClassLoader())));
        this.nst = ((int) in.readValue((int.class.getClassLoader())));
        this.dmin = ((double) in.readValue((double.class.getClassLoader())));
        this.rms = ((double) in.readValue((double.class.getClassLoader())));
        this.gap = ((double) in.readValue((double.class.getClassLoader())));
        this.magType = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Properties() {
    }

    /**
     *  @param mag
     * @param place
     * @param time
     * @param updated
     * @param tz
     * @param url
     * @param detail
     * @param felt
     * @param cdi
     * @param mmi
     * @param alert
     * @param status
     * @param tsunami
     * @param sig
     * @param net
     * @param code
     * @param ids
     * @param sources
     * @param types
     * @param nst
     * @param dmin
     * @param rms
     * @param gap
     * @param magType
     * @param type
     * @param title
     */
    public Properties(double mag, String place, long time, long updated, long tz, String url, String detail, long felt, long cdi, long mmi, String alert, String status, long tsunami, long sig, String net, String code, String ids, String sources, String types, int nst, double dmin, double rms, long gap, String magType, String type, String title) {
        super();
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.updated = updated;
        this.tz = tz;
        this.url = url;
        this.detail = detail;
        this.felt = felt;
        this.cdi = cdi;
        this.mmi = mmi;
        this.alert = alert;
        this.status = status;
        this.tsunami = tsunami;
        this.sig = sig;
        this.net = net;
        this.code = code;
        this.ids = ids;
        this.sources = sources;
        this.types = types;
        this.nst = nst;
        this.dmin = dmin;
        this.rms = rms;
        this.gap = gap;
        this.magType = magType;
        this.type = type;
        this.title = title;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getTz() {
        return tz;
    }

    public void setTz(long tz) {
        this.tz = tz;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getFelt() {
        return felt;
    }

    public void setFelt(long felt) {
        this.felt = felt;
    }

    public double getCdi() {
        return cdi;
    }

    public void setCdi(double cdi) {
        this.cdi = cdi;
    }

    public double getMmi() {
        return mmi;
    }

    public void setMmi(double mmi) {
        this.mmi = mmi;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTsunami() {
        return tsunami;
    }

    public void setTsunami(long tsunami) {
        this.tsunami = tsunami;
    }

    public long getSig() {
        return sig;
    }

    public void setSig(long sig) {
        this.sig = sig;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public int getNst() {
        return nst;
    }

    public void setNst(int nst) {
        this.nst = nst;
    }

    public double getDmin() {
        return dmin;
    }

    public void setDmin(double dmin) {
        this.dmin = dmin;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public String getMagType() {
        return magType;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mag);
        dest.writeValue(place);
        dest.writeValue(time);
        dest.writeValue(updated);
        dest.writeValue(tz);
        dest.writeValue(url);
        dest.writeValue(detail);
        dest.writeValue(felt);
        dest.writeValue(cdi);
        dest.writeValue(mmi);
        dest.writeValue(alert);
        dest.writeValue(status);
        dest.writeValue(tsunami);
        dest.writeValue(sig);
        dest.writeValue(net);
        dest.writeValue(code);
        dest.writeValue(ids);
        dest.writeValue(sources);
        dest.writeValue(types);
        dest.writeValue(nst);
        dest.writeValue(dmin);
        dest.writeValue(rms);
        dest.writeValue(gap);
        dest.writeValue(magType);
        dest.writeValue(type);
        dest.writeValue(title);
    }

    public int describeContents() {
        return  0;
    }

}
