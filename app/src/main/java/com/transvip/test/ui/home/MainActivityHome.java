package com.transvip.test.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.transvip.test.EventBus.LocationEvent;
import com.transvip.test.R;
import com.transvip.test.databinding.ActivityMainHomeBinding;
import com.transvip.test.service.LocationService;
import com.transvip.test.ui.home.adapter.OnItemClickListenerItemAdapterRecycler;
import com.transvip.test.ui.home.adapter.locationAdapter;
import com.transvip.test.ui.home.model.locationItem;
import com.transvip.test.ui.home.viewModel.ListViewModel;
import com.transvip.test.ui.home.viewModel.ListViewModelFactory;
import com.transvip.test.ui.home.viewModel.LocationUseCase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivityHome extends AppCompatActivity implements OnItemClickListenerItemAdapterRecycler {
    private final int PERMISSIONLOCATION = 100;
    private locationAdapter adapter;
    ListViewModel viewModel;
    private Intent mLocationService;
    private final Integer START_DISPLAY_LENGTH = 4000;
    private ActivityMainHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///Mantiene la pantalla siempre encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding =  ActivityMainHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.toolbar.setTitle(R.string.home);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutEmpty.setVisibility(View.VISIBLE);
        this.adapter = new locationAdapter(new ArrayList<locationItem>(),this );
        binding.recyclerView.setAdapter(adapter);
        mLocationService =  new Intent(this, LocationService.class);
        new Handler(Looper.getMainLooper()).postDelayed(() -> uploadLocation(), START_DISPLAY_LENGTH);
    }
    @Override
    public void onStart() {
        super.onStart();
        try {
            if (!EventBus.getDefault().isRegistered(LocationEvent.class)) {
                EventBus.getDefault().register(this);
            }
        }catch (org.greenrobot.eventbus.EventBusException e){
            e.printStackTrace();
        }
    }

    ///Recive el evento de EventBus del servicios
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent event) {
        ArrayList<locationItem> locationItems =   event.mlistLocationsSend;
        if(locationItems.size()>0) {
            if (adapter.getItemCount() == 0) {///Para que cargue todo lo que esta en base de datos
                adapter.setLocationData(locationItems);
                adapter.notifyItemChanged(adapter.getItemCount());
                binding.layoutEmpty.setVisibility(View.GONE);
            } else {/// cargando el ultimo que fue agregado a la base de datos
                adapter.addLocationTop(locationItems);
                adapter.notifyItemInserted(0);
                binding.recyclerView.scrollToPosition(0);
                binding.layoutEmpty.setVisibility(View.GONE);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public  void setupViewModelAndObserve(){

        ///aAra cargar datos iniciales guardados en la base de datos
        viewModel = new ViewModelProvider(getViewModelStore(), new ListViewModelFactory(new LocationUseCase())).get(ListViewModel.class);
        var ItemsObserver = new Observer<ArrayList<locationItem>>() {
            @Override
            public void onChanged(ArrayList<locationItem> locationItems) {
                try {
                    if(locationItems.size()>0) {

                        if (adapter.getItemCount() == 0) {///Para que cargue todo lo que esta en base de datos
                            adapter.setLocationData(locationItems);
                            adapter.notifyItemChanged(adapter.getItemCount());
                            binding.layoutEmpty.setVisibility(View.GONE);
                        } else {/// cargando el ultimo que fue agregado a la base de datos preventivo
                            adapter.addLocationTop(locationItems);
                            adapter.notifyItemInserted(0);
                            binding.recyclerView.scrollToPosition(0);
                            binding.layoutEmpty.setVisibility(View.GONE);
                        }

                    }
                } catch (NullPointerException ee) {
                    ee.printStackTrace();
                } catch (IllegalArgumentException ee) {
                    ee.printStackTrace();
                } catch ( java.lang.IllegalStateException ee) {
                    ee.printStackTrace();
                }
          }
        };
        viewModel.getListItemsLiveData().observe(this, ItemsObserver);
        viewModel.getLocationItem();

        ////Inicia el servicio el background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this,mLocationService);
        }else{
            this.startService(mLocationService);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONLOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startActivityForResult(ImagePicker.getPickImageIntent_photo(), GALLERY_LOLLIPOP_INTENT_CALLED_PHOTO)
                setupViewModelAndObserve();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.access_denied), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.access_denied), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED   && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONLOCATION);
        }else{
            setupViewModelAndObserve();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
                   return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void OnClick(locationItem Item, int position, Context ctx) {
        Snackbar.make(findViewById(android.R.id.content),
                ctx.getResources().getString(R.string.click) + " "+ctx.getResources().getString(R.string.date)+": "+ Item.date, Snackbar.LENGTH_LONG)
                .show();

    }
}