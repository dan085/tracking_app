package com.transvip.test.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.transvip.test.ui.home.model.locationItem;

import java.util.ArrayList;

///Se utiliza para realizar consultas a  base de datos de Firebase
public class ListDataSet {
    private callBackLocations mcallBackLocations;
    private DatabaseReference mDatabase;
    public void getLocationItem(callBackLocations resposeDatabaseLocations)   {
        this.mcallBackLocations = resposeDatabaseLocations;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<locationItem> listLocationsSend = new ArrayList<locationItem>();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    try {

                        double latitude = ds.child("latitude").getValue(Double.class);
                        double longitude = ds.child("longitude").getValue(Double.class);
                        String date = ds.child("date").getValue(String.class);
                        listLocationsSend.add(0, new locationItem(latitude, longitude, date));

                    }catch (NullPointerException ee){
                        ee.printStackTrace();////Prevenir cambio de atributos y no generar errores
                    }
                }
                mcallBackLocations.DataLocations(listLocationsSend);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

    }
}
