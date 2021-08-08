package com.transvip.test.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.transvip.test.EventBus.LocationEvent;
import com.transvip.test.R;
import com.transvip.test.ui.home.model.locationItem;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LocationService extends LifecycleService  {
    private Location mCurrentLocation;
    private LocationCallback mLocationCallback;
    private String mLastUpdateTime;
    public LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mDatabase;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 8000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    @Override
    public void onCreate() {
        super.onCreate();

        //Inicialización de variables
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();
        update_location_gps();


        //para crear el servicio en Foreground
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? getNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(110, notification);
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
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

                //Eventbus permite enviar boardCastReceiver  como una clase y mucho mas simple
                EventBus.getDefault().post(new LocationEvent(listLocationsSend));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private String getNotificationChannel(NotificationManager notificationManager){
        String channelId = "channelid";
        String channelName = getResources().getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    public void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                HashMap<String,Object> latlong = new HashMap<>();
                latlong.put("latitude",mCurrentLocation.getLatitude());
                latlong.put("longitude",mCurrentLocation.getLongitude());
                latlong.put("date",mLastUpdateTime);
                mDatabase.child("users").push().setValue(latlong);
            }
        };
    }
    @SuppressLint("MissingPermission")
    public void update_location_gps(){
        fusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(mLocationCallback);

    }


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
