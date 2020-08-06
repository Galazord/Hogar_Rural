package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.hogar_rural.Fragments.PagerController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class UserAccountActivity extends AppCompatActivity {

    // VARIABLES
    TabLayout tabLayoutAccount;
    ViewPager viewPagerAccount;
    TabItem tabMyProfile, tabMyHouses;
    PagerController pagerAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // Relaccionar las variables con la parte gráfica
        tabLayoutAccount = (TabLayout) findViewById(R.id.tabLayoutAccount);
        viewPagerAccount = (ViewPager) findViewById(R.id.viewPagerAccount);
        tabMyProfile = (TabItem) findViewById(R.id.tabMyProfile);
        tabMyHouses = (TabItem) findViewById(R.id.tabMyHouses);
        bottomNavigationView = findViewById(R.id.bottom_navegation);

        // Seleccionar y gestionar los diferentes tabs (Mi perfil y Mis casas)
        pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayoutAccount.getTabCount());
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