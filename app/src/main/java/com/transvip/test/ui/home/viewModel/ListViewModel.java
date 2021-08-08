package com.transvip.test.ui.home.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.transvip.test.data.callBackLocations;
import com.transvip.test.ui.home.model.locationItem;

import java.util.ArrayList;

public class ListViewModel extends ViewModel implements callBackLocations {
    LocationUseCase mLocationUseCase;

    public ListViewModel(LocationUseCase itemUseCase){
        this.mLocationUseCase= itemUseCase;
    }

    ///Lista que puede ser mutable
    MutableLiveData<ArrayList<locationItem>> listData = new MutableLiveData<ArrayList<locationItem>>();

    //para enviar datos nuevos al observador
    public void  setListData(ArrayList<locationItem> listItems){
        listData.postValue(listItems);
    }

    public void  getLocationItem(){
       loadInformationLocation();
    }

    ///Función para obtener la información de localización
    private void loadInformationLocation(){
       mLocationUseCase.getLocationItem(this);
    }

    public LiveData<ArrayList<locationItem>> getListItemsLiveData() {
        return listData;
    }

    ///Función para remover todo el listData
    public void clear(){
        listData.postValue(new ArrayList());
    }

    @Override
    public void DataLocations(ArrayList<locationItem> response) {
        setListData(response);
    }
}
