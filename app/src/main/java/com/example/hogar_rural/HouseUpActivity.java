package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class HouseUpActivity extends AppCompatActivity {

    //--> VARIABLES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_up);

        // Inciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Inciar componentes
    private void initComponent() {

        // Relaccionar las variables con la parte gráfica

    }

    //--> CLICK BOTONES
    // Controlador de los radio buttom: Integro/ Habitaciones
    public void CheckButtonTypeHouse(View view) {
    }

    // Acción para seleccionar ON/OFF los iconos de los servicios
    public void OnDefaultToggleClickComplete(View view) {
    }

    // Publicar/ Subir oferta de la casa
    public void clickPublishHouse(View view) {


    }
}