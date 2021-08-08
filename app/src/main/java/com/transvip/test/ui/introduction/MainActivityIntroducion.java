package com.transvip.test.ui.introduction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.transvip.test.R;
import com.transvip.test.databinding.ActivityMainIntroducionBinding;
import com.transvip.test.ui.home.MainActivityHome;

public class MainActivityIntroducion extends AppCompatActivity {
    private final int PERMISSIONLOCATION = 100;
    private ActivityMainIntroducionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainIntroducionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       ///Descripción Una
        binding.textViewDescription1.setText(fromHtml(getResources().getString(R.string.intro_one)));

        ///Descripción dos
        binding.textViewDescription2.setText(fromHtml(getResources().getString(R.string.intro_two)));

        ///Descripción tres
        binding.textViewDescription3.setText(fromHtml(getResources().getString(R.string.intro_three)));

        binding.buttonBegin.setOnClickListener(v -> uploadLocation());

    }

    public void uploadLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED   && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONLOCATION);
        }else{
            startActivity(new Intent(MainActivityIntroducion.this, MainActivityHome.class));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONLOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadLocation();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.access_denied), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, this.getResources().getString(R.string.access_denied), Toast.LENGTH_SHORT).show();
        }
    }

  ///Cargar el texto de descripción con caracteristicas de estilo html
    public Spanned fromHtml(String source )  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return   Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return  Html.fromHtml(source);
        }
    }
}