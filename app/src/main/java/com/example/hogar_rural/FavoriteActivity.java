package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FavoriteActivity extends AppCompatActivity {

    //--> VARIABLES
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Iniciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {

        // Relaccionar las variables con la parte gráfica
        bottomNavigationView = findViewById(R.id.bottom_navegation);

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();

        // BARRA DE NAVEGACIÓN INFERIOR
        // Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.Favorites);
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
                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        startActivity(new Intent(getApplicationContext(), OutstandingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        return true;
                }

                return false;
            }
        });
    }
}