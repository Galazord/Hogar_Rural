package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class FavoriteActivity extends AppCompatActivity {

    //--> VARIABLES
    private RelativeLayout RLMain_Favorite;
    private BottomNavigationView bottomNavigationView;
    private String destiny;
    // RecyclerView
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Home> list_home = new ArrayList<Home>();

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Iniciar componentes
        initComponent();

        // Comprobar si el usuario está logado para mostrar una vista u otra
        if(mAuth.getCurrentUser()!=null){

            // Establecer como fondo el gris neutro
            RLMain_Favorite.setBackgroundColor(ContextCompat.getColor(this, R.color.background_grey));

            // Llamar a firebase para recoger los datos y mostrarlos
            loadFavorite();

        }

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {
// Recoger la palabra de destino
        Bundle b = getIntent().getExtras();
        if(b!=null){
            destiny = b.getString("destiny");
        }
        // Relaccionar las variables con la parte gráfica
        RLMain_Favorite = (RelativeLayout) findViewById(R.id.RLMain_Favorite);
        bottomNavigationView = findViewById(R.id.bottom_navegation);
        bottomNavigationView.setItemIconSize(120);
        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
                        Intent i =new Intent(getApplicationContext(), ExplorerActivity.class);
                        i.putExtra("destiny",destiny);
                        startActivity(i);
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        //Existe un usuario logueado
                        if(mAuth.getCurrentUser()!=null){
                            Intent intent =new Intent(getApplicationContext(), UserAccountActivity.class);
                            intent.putExtra("destine",destiny);
                            startActivity(intent);
                        }else{
                            Intent intent =new Intent(getApplicationContext(), MyProfileActivity.class);
                            intent.putExtra("destine",destiny);
                            startActivity(intent);
                        }

                        overridePendingTransition(0,0);
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        Intent intent =new Intent(getApplicationContext(), OutstandingActivity.class);
                        intent.putExtra("destiny",destiny);
                        startActivity(intent);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        Intent intent2 =new Intent(getApplicationContext(), FavoriteActivity.class);
                        intent2.putExtra("destiny",destiny);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    // Llamar a firebase para recoger los datos y mostrarlos
    private void loadFromFirebase(){
        mFirestore.collection("homes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        // Empezar con la lista vacía antes de mostrar
                        list_home.removeAll(list_home);
                        // Recorrer firebase y guardar cada infomación de la casa en un objeto tipo Home y en una lista (list_home)
                        for (QueryDocumentSnapshot document : value) {

                            Home h = document.toObject(Home.class);
                            // list_home.add(h);
                        }
                        // Cargar/ mostrar la información en el recyclerView
                        loadRecyclerView();
                    }
                });
    }

    private void loadFavorite(){
        if(mAuth.getCurrentUser()!=null){
            String idUser = mAuth.getCurrentUser().getUid();
            mFirestore.collection("users").document(idUser).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user  = documentSnapshot.toObject(User.class);
                            if(user.getFavorites()!=null){
                                List<DocumentReference> favoritesUser = user.getFavorites();
                                for (DocumentReference docRef: favoritesUser
                                ) {
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Home h = documentSnapshot.toObject(Home.class);
                                            list_home.add(h);

                                            loadRecyclerView();
                                        }
                                    });
                                }
                            }
                        }


                    });
        }
    }

    // Cargar/ mostrar la información en el recyclerView
    private void loadRecyclerView(){
        Adapter adapter = new Adapter(getApplicationContext(), list_home, destiny,true,true);
        recyclerView.setAdapter(adapter);
    }

}