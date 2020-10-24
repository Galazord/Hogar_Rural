package com.example.hogar_rural.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hogar_rural.Adapter;
import com.example.hogar_rural.Model.Booking;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyBookingsFragment extends Fragment {

    //--> VARIBALES
    private ImageView cvBooking_avatar;
    private TextView tvBooking_name, tvBooking_dates;
    private Spinner spinOwnHouse;
    // RecyclerView
    private RecyclerView recyclerView;
    private ArrayList<Booking> list_bookings = new ArrayList<Booking>();
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    //--> CONTRUCTOR
    public MyBookingsFragment() {}

    public static MyBookingsFragment newInstance() {

        MyBookingsFragment fragment = new MyBookingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        // Iniciar componentes
        initComponent(view);

        return view;
    }

    // MÉTODOS
    private void initComponent(View view) {

        // Relaccionar las variables con la parte gráfica
        cvBooking_avatar = (ImageView) view.findViewById(R.id.cvBooking_avatar);
        tvBooking_name = (TextView) view.findViewById(R.id.tvBooking_name);
        tvBooking_dates = (TextView) view.findViewById(R.id.tvBooking_dates);
        spinOwnHouse = (Spinner) view.findViewById(R.id.spinOwnHouse);
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
}