package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hogar_rural.Fragments.PagerControllerAccount;
import com.example.hogar_rural.Fragments.PagerControllerOutstanding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OutstandingActivity extends AppCompatActivity {

    //--> VARIABLES
    private ListView listView;
    private String[] nameList = {"Pakistan", "Canada", "India", "Alemania", "Australia", "China", "España", "Francia", "Italia"};
    private ArrayAdapter<String> adapterList;
    private TabLayout tabLayoutAccount;
    private ViewPager viewPagerAccount;
    private TabItem tabMyNews, tabMyLowPrices;
    private PagerControllerOutstanding pagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private String destiny;
    private String destiny_old ="";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        // Iniciar componentes
        initComponent();
    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {
        Bundle b = getIntent().getExtras();
        if(b!=null){
            destiny = b.getString("destiny");
            if(b.getString("destiny_old")!=null){
                destiny_old=b.getString("destiny_old");
            } else{
                destiny_old = destiny;
            }
        }
        // Inicializar y asignar VARIABLES
        bottomNavigationView = findViewById(R.id.bottom_navegation);
        bottomNavigationView.setItemIconSize(120);
        tabLayoutAccount = (TabLayout) findViewById(R.id.tabLayoututstanding);
        viewPagerAccount = (ViewPager) findViewById(R.id.viewPagerOutstanding);
        tabMyNews = (TabItem) findViewById(R.id.tabMyNews);
        tabMyLowPrices = (TabItem) findViewById(R.id.tabMyLowPrices);

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();

        // Seleccionar y gestionar los diferentes tabs (Mi perfil y Mis casas)
        cargarPageView();
        // BARRA DE NAVEGACIÓN INFERIOR
        // Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.OutStanding);
        // Incorporar ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.SearchList: // EXPLORAR
                        Intent i =new Intent(getApplicationContext(), ExplorerActivity.class);
                        i.putExtra("destiny",destiny_old);
                        startActivity(i);
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        //Existe un usuario logueado
                        if(mAuth.getCurrentUser()!=null){
                            Intent intent =new Intent(getApplicationContext(), UserAccountActivity.class);
                            intent.putExtra("destine",destiny_old);
                            startActivity(intent);
                        }else{
                            Intent intent =new Intent(getApplicationContext(), MyProfileActivity.class);
                            intent.putExtra("destine",destiny_old);
                            startActivity(intent);
                        }

                        overridePendingTransition(0,0);
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        Intent i2 =new Intent(getApplicationContext(), FavoriteActivity.class);
                        i2.putExtra("destiny",destiny_old);
                        startActivity(i2);
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
    private void cargarPageView(){
        // Seleccionar y gestionar los diferentes tabs (Mi perfil y Mis casas)
        pagerAdapter = new PagerControllerOutstanding(getSupportFragmentManager(), tabLayoutAccount.getTabCount(),destiny);
        viewPagerAccount.setAdapter(pagerAdapter);
        tabLayoutAccount.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAccount.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){

                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerAccount.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutAccount));

    }
    // Menú superior: Resolver la búsqueda
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

    // Sistema de alertas o errores
    public void showInputAlertDialog(){
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
                                destiny_old = destiny;

                                destiny = strReturn[0];

                                Intent intent = new Intent(getApplicationContext(), OutstandingActivity.class);
                                intent.putExtra("destiny",destiny);
                                intent.putExtra("destiny_old",destiny_old);
                                startActivity(intent);
                                finish();

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
    //--> CLICK BOTONES

}