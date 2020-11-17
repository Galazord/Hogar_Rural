package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.hogar_rural.Fragments.PagerControllerAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class UserAccountActivity extends AppCompatActivity {

    //--> VARIABLES
    TabLayout tabLayoutAccount;
    ViewPager viewPagerAccount;
    TabItem tabMyProfile, tabMyHouses, tabMyBookings;
    PagerControllerAccount pagerAdapter;
    BottomNavigationView bottomNavigationView;
    String destine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Iniciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {
        if(getIntent().getExtras()!=null && getIntent().getStringExtra("destine")!=null){
            destine = getIntent().getStringExtra("destine");
        }else{

            destine = "";
        }

        // Relaccionar las variables con la parte gráfica
        tabLayoutAccount = (TabLayout) findViewById(R.id.tabLayoutAccount);
        viewPagerAccount = (ViewPager) findViewById(R.id.viewPagerAccount);
        tabMyProfile = (TabItem) findViewById(R.id.tabMyProfile);
        tabMyHouses = (TabItem) findViewById(R.id.tabMyHouses);
        tabMyBookings = (TabItem) findViewById(R.id.tabMyBookings);
        bottomNavigationView = findViewById(R.id.bottom_navegation);

        // Seleccionar y gestionar los diferentes tabs (Mi perfil / Mis casas / Mis reservas)
        pagerAdapter = new PagerControllerAccount(getSupportFragmentManager(), tabLayoutAccount.getTabCount(),destine);
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
                if(tab.getPosition() == 2){
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
        bottomNavigationView.setSelectedItemId(R.id.MyProfile);
        bottomNavigationView.setItemIconSize(120);
        // Incorporar ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.SearchList: // EXPLORAR
                        Intent i = new Intent(getApplicationContext(), ExplorerActivity.class);
                        i.putExtra("destiny",destine);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        Intent intent =new Intent(getApplicationContext(), OutstandingActivity.class);
                        intent.putExtra("destiny",destine);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        Intent i2 =new Intent(getApplicationContext(), FavoriteActivity.class);
                        i2.putExtra("destiny",destine);
                        startActivity(i2);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }
}