package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hogar_rural.Interface.DbRetrofitApi;
import com.example.hogar_rural.Model.House;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExplorerActivity extends AppCompatActivity {

    //--> VARIABLES
    private ListView listView;
    private String[] nameList = {"Pakistan", "Canada", "India", "Alemania", "Australia", "China", "España", "Francia", "Italia"};
    private ArrayAdapter<String> adapterList;
    private Button btnFilters, btnMap;
    // RecyclerView
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<House> modelHouse;
    private DbRetrofitApi dbRetrofitApi;

    private FirebaseAuth mAuth;
    //--> VARIABLES FIJAS
    private final String URL_PHP = "https://hogarruralapp.000webhostapp.com/hogarRural/php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        // Subtitulo en la toolbar (número de resultados de búsqueda)
        getSupportActionBar().setSubtitle(getString(R.string.app_sub));

        // Iniciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();

        // Inicializar y/o asignar VARIABLES a la parte gráfica
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navegation);
        recyclerView = findViewById(R.id.exploreRecyclerView);
        modelHouse = new ArrayList<>();
        btnFilters = (Button) findViewById(R.id.btnFilters);
        btnMap = (Button) findViewById(R.id.btnMap);

        // RecyclerView: instanciar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_PHP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbRetrofitApi = retrofit.create(DbRetrofitApi.class);

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

        // RecyclerView 1: Mostrar los datos del listado de casas
        showListHouses();

    }

    // Mostrar datos de cada casa en el recyclerView
    private void showListHouses() {

        Call<List<House>> call = dbRetrofitApi.getHouses();
        call.enqueue(new Callback<List<House>>() {
            @Override
            public void onResponse(Call<List<House>> call, Response<List<House>> response) {
                // Cuando la conexión NO fue exitosa
                if(!response.isSuccessful()){
                    Toast.makeText(ExplorerActivity.this, "showListHouses: Error código: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                // Recoger los campos de la base de datos e incluirlos en el Arraylist (aqui se guardan los datos de cada casa)
                List<House> houses = response.body();
                for (House house: houses){
                    House h = new House();
                    h.setIdHouse(house.getIdHouse());
                    h.setName(house.getName());
                    h.setRental(house.getRental());
                    h.setPersonMax(house.getPersonMax() + " personas");
                    h.setPrice(house.getPrice() + "€");
                    h.setNumOpinion(house.getNumOpinion() + " opiniones");
                    h.setGalleryImg(house.getGalleryImg());
                    modelHouse.add(h);

                }
                showFinalHouses();

            }

            @Override
            public void onFailure(Call<List<House>> call, Throwable t) {
                // Cuando la conexión falla por cualquier motivo
                Toast.makeText(ExplorerActivity.this, "showListHouses: Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Mostrar el resultado final del listado del RecyclerView
    private void showFinalHouses() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, modelHouse);
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