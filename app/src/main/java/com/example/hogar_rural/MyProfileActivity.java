package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Inicializar y asignar VARIABLES
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navegation);

        // Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.MyProfile);

        // Incorporar ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.SearchList: // EXPLORAR
                        startActivity(new Intent(getApplicationContext(), ExplorerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        startActivity(new Intent(getApplicationContext(), OutstandingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }
}