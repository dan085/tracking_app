package com.transvip.test.ui.home.model;
import java.io.Serializable;
public class locationItem  implements Serializable {

    public Double latitude;
    public Double longitude;
    public  String date;
    public locationItem(Double latitude, Double longitude, String date) {
        super();
        this.latitude=latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public locationItem(){}

}
