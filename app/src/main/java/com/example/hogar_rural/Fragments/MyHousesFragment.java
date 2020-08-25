package com.example.hogar_rural.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hogar_rural.ExplorerActivity;
import com.example.hogar_rural.HouseUpActivity;
import com.example.hogar_rural.R;

public class MyHousesFragment extends Fragment {

    //--> VARIBALES
    private Button btnCreateNewHouse;

    //--> CONTRUCTOR
    public MyHousesFragment() {}

    public static MyHousesFragment newInstance() {
        MyHousesFragment fragment = new MyHousesFragment();

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
        View view = inflater.inflate(R.layout.fragment_my_houses, container, false);

        // Iniciar componentes
        initComponent(view);

        return view;
    }


    //--> MÉTODOS
    private void initComponent(View view) {

        // Relacionar las variables con la parte gráfica
        btnCreateNewHouse = (Button) view.findViewById(R.id.btnCreateNewHouse);

        // BOTÓN: Ir al formulario de registrar nueva casa
        btnCreateNewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Ir a la vista del formulario para registrar una nueva casa
                Intent i = new Intent(getContext(), HouseUpActivity.class);
                startActivity(i);

            }
        });
    }


}