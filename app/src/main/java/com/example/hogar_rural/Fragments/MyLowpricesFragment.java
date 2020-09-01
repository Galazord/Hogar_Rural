package com.example.hogar_rural.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.hogar_rural.Adapter;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyLowpricesFragment extends Fragment {

    //--> VARIBALES
    private FrameLayout FLMain_lowprices;
    // RecyclerView
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Home> list_home = new ArrayList<Home>();
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;


    public MyLowpricesFragment() {
        // Required empty public constructor
    }

    public static MyLowpricesFragment newInstance() {
        MyLowpricesFragment fragment = new MyLowpricesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_lowprices, container, false);

        // Iniciar componentes
        initComponent(view);

        // Comprobar si el usuario está logado para mostrar una vista u otra
        if(mAuth.getCurrentUser()!=null){

            // Establecer como fondo el gris neutro
            FLMain_lowprices.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.background_grey));

            // Llamar a firebase para recoger los datos y mostrarlos
            loadFromFirebase();

        }

        return view;
    }

    //--> MÉTODOS
    private void initComponent(View view) {

        // Relaccionar las variables con la parte gráfica
        FLMain_lowprices = view.findViewById(R.id.FLMain_lowprices);
        recyclerView = view.findViewById(R.id.myLowpricesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();


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
        Adapter adapter = new Adapter(getActivity().getApplicationContext(), list_home);
        recyclerView.setAdapter(adapter);
    }

}