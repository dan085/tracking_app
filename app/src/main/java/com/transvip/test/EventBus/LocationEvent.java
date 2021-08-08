package com.transvip.test.EventBus;
import com.transvip.test.ui.home.model.locationItem;
import java.util.ArrayList;
public  class LocationEvent {
    public ArrayList<locationItem> mlistLocationsSend;
    public LocationEvent(ArrayList<locationItem> listLocationsSend){
        this.mlistLocationsSend = listLocationsSend;
    }
}