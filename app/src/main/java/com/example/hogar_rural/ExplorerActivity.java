package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.House;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
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
    private ImageView imgEmpty;
    MenuItem itemResultados;
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        // Iniciar componentes
        initComponent();

        // Llamar a firebase para recoger los datos y mostrarlos
        loadFromFirebase();


        getSupportActionBar().setTitle("Destino: "+destiny);


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

    // Cargar/ mostrar la información en el recyclerView
    private void loadRecyclerView(){

            Adapter adapter = new Adapter(getApplicationContext(), list_home, destiny);
            recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==RESULT_OK){
          destiny =  data.getStringExtra("destine");
        }
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

        Intent intentFilter = new Intent (getApplicationContext(), FiltersActivity.class);
        startActivity(intentFilter);
    }

    public void clickGoMap(View view) {

        // Ir a la sección Mapa
        Intent intentMap = new Intent (getApplicationContext(), MapActivity.class);
        startActivity(intentMap);

    }

    public   void showInputAlertDialog(){
        final String[] strReturn = {""};
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.alertdialog_custom, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

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
                               /* Intent intentExplorer = new Intent (getApplicationContext(), ExplorerActivity.class);
                                intentExplorer.putExtra("destiny", strReturn[0]);
                                startActivity(intentExplorer);
                                finish();*/
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

}