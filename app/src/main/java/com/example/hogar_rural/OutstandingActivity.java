package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hogar_rural.Fragments.PagerControllerAccount;
import com.example.hogar_rural.Fragments.PagerControllerOutstanding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class OutstandingActivity extends AppCompatActivity {

    //--> VARIABLES
    private ListView listView;
    private String[] nameList = {"Pakistan", "Canada", "India", "Alemania", "Australia", "China", "España", "Francia", "Italia"};
    private ArrayAdapter<String> adapterList;
    TabLayout tabLayoutAccount;
    ViewPager viewPagerAccount;
    TabItem tabMyNews, tabMyLowPrices;
    PagerControllerOutstanding pagerAdapter;
    BottomNavigationView bottomNavigationView;

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

        // Inicializar y asignar VARIABLES
        bottomNavigationView = findViewById(R.id.bottom_navegation);
        tabLayoutAccount = (TabLayout) findViewById(R.id.tabLayoututstanding);
        viewPagerAccount = (ViewPager) findViewById(R.id.viewPagerOutstanding);
        tabMyNews = (TabItem) findViewById(R.id.tabMyNews);
        tabMyLowPrices = (TabItem) findViewById(R.id.tabMyLowPrices);

        // Seleccionar y gestionar los diferentes tabs (Mi perfil y Mis casas)
        pagerAdapter = new PagerControllerOutstanding(getSupportFragmentManager(), tabLayoutAccount.getTabCount());
        viewPagerAccount.setAdapter(pagerAdapter);
        tabLayoutAccount.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAccount.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();;
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

        // BARRA DE NAVEGACIÓN INFERIOR
        // Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.OutStanding);
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

}