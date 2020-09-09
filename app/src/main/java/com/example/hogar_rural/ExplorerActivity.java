package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.House;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExplorerActivity extends AppCompatActivity {

    //--> VARIABLES
    private ListView listView;
    private String[] nameList = {"Pakistan", "Canada", "India", "Alemania", "Australia", "China", "España", "Francia", "Italia"};
    private ArrayAdapter<String> adapterList;
    private Button btnFilters, btnMap;
    private MediaPlayer soundError;
    private String destiny;

    // RecyclerView
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Home> list_home = new ArrayList<Home>();

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        // Subtitulo en la toolbar (número de resultados de búsqueda)
        getSupportActionBar().setSubtitle(getString(R.string.app_sub));

        // Iniciar componentes
        initComponent();

        // Llamar a firebase para recoger los datos y mostrarlos
        loadFromFirebase();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {

        // Recoger la palabra de destino
        Bundle b = getIntent().getExtras();
        if(b!=null){
            destiny = b.getString("destiny");
        }

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar y/o asignar VARIABLES a la parte gráfica
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navegation);
        recyclerView = findViewById(R.id.exploreRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2)));
        btnFilters = (Button) findViewById(R.id.btnFilters);
        btnMap = (Button) findViewById(R.id.btnMap);
        soundError = MediaPlayer.create(this, R.raw.sound_error);

        //--> MENÚ DE NAVEGACIÓN INFERIOR
        // Menu de Navegación: Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.SearchList);
        // Menu de Navegación: Incorporar ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.SearchList: // EXPLORAR
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        //Existe un usuario logueado
                        if(mAuth.getCurrentUser()!=null){
                            startActivity(new Intent(getApplicationContext(), UserAccountActivity.class));
                        }else{
                            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        }

                        overridePendingTransition(0,0);
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

        // Menú superior: Listado de búsqueda (Provisional)
        // Buscar en la base de datos todas las ciudades y guardarlas en el array de String nameList (---------------- PENDIENTE -------------------)

        /*
        listView = (ListView) findViewById(R.id.myList);
        adapterList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapterList);

         */


    }

    // Llamar a firebase para recoger los datos y mostrarlos
    private void loadFromFirebase(){
        db.collection("homes")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        // Empezar con la lista vacía antes de mostrar
                        list_home.removeAll(list_home);

                        // Recorrer firebase y guardar cada infomación de la casa en un objeto tipo Home y en una lista (list_home)
                        for (QueryDocumentSnapshot document : value) {

                            Home h = document.toObject(Home.class);
                            list_home.add(h);
                            Log.d("QUERY DB", document.getId() + " => " + document.getData());
                        }

                        // Cargar/ mostrar la información en el recyclerView
                        loadRecyclerView();
                    }
                });


    }

    // Cargar/ mostrar la información en el recyclerView
    private void loadRecyclerView(){
        Adapter adapter = new Adapter(getApplicationContext(), list_home);
        recyclerView.setAdapter(adapter);
    }

    // Menú superior: Resolver la búsqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_top, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Introduce tu valor de búsqueda");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterList.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //--> CLICK BOTONES
    // Ir a la sección Filtrar
    public void clickGoFilters(View view) {

        Intent intentFilter = new Intent (getApplicationContext(), FiltersActivity.class);
        startActivity(intentFilter);
    }

    public void clickGoMap(View view) {

        // Ir a la sección Mapa
        Intent intentMap = new Intent (getApplicationContext(), MapActivity.class);
        startActivity(intentMap);

    }

}