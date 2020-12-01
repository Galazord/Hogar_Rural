package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hogar_rural.Model.Available;
import com.example.hogar_rural.Model.Filter;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.HomeFilter;
import com.example.hogar_rural.Model.House;
import com.example.hogar_rural.Utils.Constant;
import com.example.hogar_rural.Utils.TypeOrder;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExplorerActivity extends AppCompatActivity {

    //--> VARIABLES
    private ArrayAdapter<String> adapterList;
    private Button btnFilters, btnMap;
    private MediaPlayer soundError;
    private String destiny;
    private Boolean searchWithFilters = false;
    Filter filter = null;
    // RecyclerView
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Home> list_home = new ArrayList<Home>();
    private ArrayList<Home> list_home_filter = new ArrayList<Home>();
    private ImageView imgEmpty;
    MenuItem itemResultados;
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    final boolean[] res = {false};
    boolean isFilter = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        // Iniciar componentes
        initComponent();


        if(searchWithFilters){
            // Llamar a firebase para recoger los datos y mostrarlos
            loadFromFirebaseFilters();
        }else{
            // Llamar a firebase para recoger los datos y mostrarlos
            loadFromFirebase();
        }

        getSupportActionBar().setTitle("Destino: "+destiny);


    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {

        // Recoger la palabra de destino
        Bundle b = getIntent().getExtras();
        if(b!=null){
            destiny = b.getString("destiny");
             filter = b.getParcelable("filter");
            if(filter!=null){
                searchWithFilters = true;
            }

        }

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar y/o asignar VARIABLES a la parte gráfica
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navegation);
        bottomNavigationView.setItemIconSize(120);
        recyclerView = findViewById(R.id.exploreRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2)));
        btnFilters = (Button) findViewById(R.id.btnFilters);
        btnMap = (Button) findViewById(R.id.btnMap);
        imgEmpty = (ImageView) findViewById(R.id.imgEmpty);
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
                            Intent i =new Intent(getApplicationContext(), UserAccountActivity.class);
                            i.putExtra("destine",destiny);
                            startActivity(i);
                        }else{
                            Intent i =new Intent(getApplicationContext(), MyProfileActivity.class);
                            i.putExtra("destine",destiny);
                            startActivity(i);
                        }

                        overridePendingTransition(0,0);
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        Intent intent =new Intent(getApplicationContext(), OutstandingActivity.class);
                        intent.putExtra("destiny",destiny);
                        startActivity(intent);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        Intent i =new Intent(getApplicationContext(), FavoriteActivity.class);
                        i.putExtra("destiny",destiny);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

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

                            if(destiny.toLowerCase().equals(h.getProvince().toLowerCase())){
                                list_home.add(h);
                            }

                            Log.d("QUERY DB", document.getId() + " => " + document.getData());
                        }

                        // Cargar/ mostrar la información en el recyclerView
                        getSupportActionBar().setTitle(destiny+"\n --> "+list_home.size()+" resultados");

                        if(list_home.size()!=0){
                           imgEmpty.setVisibility(View.INVISIBLE);
                            loadRecyclerView();
                        }else{
                            imgEmpty.setVisibility(View.VISIBLE);
                            //recyclerView.setBackgroundResource(getDrawable(R.id.myHouse_recycler_view_my_houses));
                        }
                    }
                });


    }
    // Llamar a firebase para recoger los datos de filtros y mostrarlos
    private void loadFromFirebaseFilters(){

        Query collectionReference = null;
        // Comprobar primero el orden en que se muestran
        if(filter.getOrder()!=null){

            if(filter.getOrder() == TypeOrder.VALORATION){
                collectionReference = db.collection("homes").orderBy("valoration",Query.Direction.DESCENDING);
            }else if(filter.getOrder() == TypeOrder.PRICE_ASC){
                collectionReference = db.collection("homes").orderBy("price", Query.Direction.ASCENDING);
            }else if(filter.getOrder() == TypeOrder.PRICE_DESC){
                collectionReference = db.collection("homes").orderBy("price", Query.Direction.DESCENDING);
            }else{
                collectionReference = db.collection("homes");
            }


        }else{
            collectionReference = db.collection("homes");
        }


        // Ir preguntando a la clase HomeFilter si el valor por defecto ha cambiado para tenerlo en cuenta en la búsqueda.
        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        // Empezar con la lista vacía antes de mostrar
                        list_home.removeAll(list_home);

                        // Recorrer firebase y guardar cada infomación de la casa en un objeto tipo Home y en una lista (list_home)
                        for (QueryDocumentSnapshot document : value) {

                            Home h = document.toObject(Home.class);
                            boolean noneFilter = true;
                            if(destiny.toLowerCase().equals(h.getProvince().toLowerCase())){
                                HomeFilter homeFilter = new HomeFilter(filter,h);

                                isFilter=true;
                                // Filtro de número de personas
                                if(homeFilter.filterNumberPeopleActive() && isFilter){
                                    isFilter = homeFilter.filterNumberPeople();
                                }
                                // Filtro de fechas disponibles
                                if(homeFilter.filterRangeOfDateActive() && isFilter){
                                    loadAvaible(h.getId(),homeFilter);
                                }
                                // Filtro de valoración
                                if(homeFilter.filterValorationsActive() && isFilter){
                                    isFilter = homeFilter.filterValorations();
                                }
                                // Filtro de tipo de vivienda (Íntegra/ Habitaciones)
                                if(homeFilter.filterRoomsActive() && isFilter){
                                    isFilter = homeFilter.filterRooms();
                                }
                                // Filtro de precio
                                if(homeFilter.filterPriceActive()){
                                    isFilter = homeFilter.filterPrice();
                                }
                                // Filtro de Servicios (iconos activos)
                                if(homeFilter.filterServicesActive() && isFilter){
                                        isFilter = homeFilter.filterServices();
                                }
                                // Si alguno de los filtros no se cumple, la búsqeuda se cancela y se vuelve a Explorer

                                    if(isFilter){
                                        list_home.add(homeFilter.getHome());
                                    }

                            }
                            isFilter=false;
                        }

                        // Cargar/ mostrar la información en el recyclerView
                        getSupportActionBar().setTitle(destiny+"\n --> "+list_home.size()+" resultados");

                        if(list_home.size()!=0){
                            imgEmpty.setVisibility(View.INVISIBLE);
                            loadRecyclerView();
                        }else{
                            imgEmpty.setVisibility(View.VISIBLE);
                            //recyclerView.setBackgroundResource(getDrawable(R.id.myHouse_recycler_view_my_houses));
                        }

                    }
                });


    }
    // Cargar/ mostrar la información en el recyclerView
    private void loadRecyclerView(){

            Adapter adapter = new Adapter(getApplicationContext(), list_home, destiny);
            recyclerView.setAdapter(adapter);

    }
    private void loadRecyclerViewFilter(){

        Adapter adapter = new Adapter(getApplicationContext(), list_home_filter, destiny);
        recyclerView.setAdapter(adapter);

    }

    // Menú superior: Resolver la búsqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_menu_search:
                showInputAlertDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //--> CLICK BOTONES
    // Ir a la sección Filtrar
    public void clickGoFilters(View view) {

        Intent i =new Intent(getApplicationContext(), FiltersActivity.class);
        i.putExtra("destiny",destiny);
        startActivity(i);
    }

    // Ir a la sección Mapas
    public void clickGoMap(View view) {

        // Ir a la sección Mapa
        Intent intentMap = new Intent (getApplicationContext(), MapActivity.class);
        intentMap.putExtra("destiny",destiny);
        startActivity(intentMap);

    }

    // Sistema de alertas o errores
    public void showInputAlertDialog(){
        final String[] strReturn = {""};
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.alertdialog_custom, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this,R.style.DialogTheme);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.custom_dialog_text);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Buscar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                strReturn[0] = userInput.getText().toString();
                               destiny = strReturn[0];
                               loadFromFirebase();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    // Filtro de fechas disponibles
    private void loadAvaible(String idHome, final HomeFilter homeFilter){

            db.collection("availables").whereEqualTo("id",idHome)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        for (QueryDocumentSnapshot document : value) {
                            Available available = document.toObject(Available.class);
                            //false -> no disponible | true -> disponible
                            isFilter = homeFilter.filterRangeOfDate(available);

                            if(isFilter){
                                list_home_filter.add(homeFilter.getHome());
                            }

                            isFilter = false;
                            // Cargar/ mostrar la información en el recyclerView
                            getSupportActionBar().setTitle(destiny+"\n --> "+list_home_filter.size()+" resultados");

                            if(list_home_filter.size()!=0){
                                imgEmpty.setVisibility(View.INVISIBLE);
                                loadRecyclerViewFilter();
                            }else{
                                imgEmpty.setVisibility(View.VISIBLE);
                            }
                        }


                    }
                });
            Log.i("asdas",isFilter+"");

    }

}