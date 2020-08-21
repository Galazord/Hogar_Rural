package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // declarar VARIABLES
    private EditText etInputDestiny;
    private Button btnAccess;
    private String valueDestiny = "";
    // Música & Sonidos
    private MediaPlayer soundError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ocultar en esta vista la toolbar
        getSupportActionBar().hide();

        // Inicializar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Inicializar componentes
    private void initComponent() {

        // Asignar VARIABLES a la parte gráfica
        etInputDestiny = (EditText) findViewById(R.id.etInputDestiny);
        btnAccess = (Button) findViewById(R.id.btnAccess);
        soundError = MediaPlayer.create(this, R.raw.sound_error);

    }

    // Acción al pulsar el botón de explorar
    private void startExplorer() {

        // Recoger el texto introducido en el campo Destino
        valueDestiny = etInputDestiny.getText().toString();

        // Validar que haya información introducida en el campo destino y sea correcta
        if(validData(valueDestiny)){

            //Toast.makeText(getApplicationContext(),"CORRECTO", Toast.LENGTH_SHORT).show();

            // Acceder al menu de la app
            Intent intentExplorer = new Intent (getApplicationContext(), ExplorerActivity.class);
            startActivity(intentExplorer);
        }


    }

    // Validar la información del campo
    private boolean validData(String value) {

        // VSi el campo está vacío, salta el error
        if (value.isEmpty()) {

            Toast.makeText(getApplicationContext(),"ERROR. Debes rellenar el campo de destino.", Toast.LENGTH_SHORT).show();
            soundError.start();
        }
        else{

            // Comprobar que el destino exista en la BD (------------------- PENDIENTE ---------------------)

            return true;
        }

        return false;

    }

    //--> CLICK BOTONES
    // Recoger el dato del destino e ir a la vista de explorar
    public void clickGoExplorer(View view) {

        startExplorer();

    }


}