package com.transvip.test.ui.home.viewModel;

import com.transvip.test.data.ListDataSet;
import com.transvip.test.data.callBackLocations;

public class LocationUseCase {

   private final ListDataSet ReportItemDataSet = new ListDataSet();
    ///Función para obtener la lista de Item de esta forma se tiene un sistema independiente
    // y permitiendo tener sistemas que mas simple modificar a gran escala
    public void getLocationItem(callBackLocations DatabaseResponseLocations){
         ReportItemDataSet.getLocationItem(DatabaseResponseLocations);
    }
}


