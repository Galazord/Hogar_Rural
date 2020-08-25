package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class HouseUpActivity extends AppCompatActivity {

    //--> VARIABLES
    private EditText etHouse_input_name, etHouse_input_address, etHouse_input_code, etHouse_input_municipaly, etHouse_input_province, etHouse_input_features, etHouse_input_activities, etHouse_input_interest;
    private RadioGroup RGHouse;
    private RadioButton rbHouseUp_complete, rbHouseUp_rooms;
    private TextView tvCounter_capMax, tvHouseUp_priceIndicator;
    private SeekBar sbHouseUp_priceSelector;
    private ToggleButton tbFilter_adapted, tbFilter_air, tbFilter_barbecue, tbFilter_bath, tbFilter_pool, tbFilter_climatized, tbFilter_garden, tbFilter_heating, tbFilter_jacuzzi, tbFilter_kitchen, tbFilter_mountain, tbFilter_parking, tbFilter_beach, tbFilter_breakfast, tbFilter_children, tbFilter_fireplace, tbFilter_pets, tbFilter_spa, tbFilter_tv, tbFilter_wifi;
    private Button btnSelectGaleryImage, btnCapMax_less, btnCapMax_plus;

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
        etHouse_input_name = (EditText) findViewById(R.id.etHouse_input_name);
        etHouse_input_address = (EditText) findViewById(R.id.etHouse_input_address);
        etHouse_input_code = (EditText) findViewById(R.id.etHouse_input_code);
        etHouse_input_municipaly = (EditText) findViewById(R.id.etHouse_input_municipaly);
        etHouse_input_province = (EditText) findViewById(R.id.etHouse_input_province);
        etHouse_input_features = (EditText) findViewById(R.id.etHouse_input_features);
        etHouse_input_activities = (EditText) findViewById(R.id.etHouse_input_activities);
        etHouse_input_interest = (EditText) findViewById(R.id.etHouse_input_interest);
        RGHouse = (RadioGroup) findViewById(R.id.RGHouse);
        rbHouseUp_complete = (RadioButton) findViewById(R.id.rbHouseUp_complete);
        rbHouseUp_rooms = (RadioButton) findViewById(R.id.rbHouseUp_rooms);
        tvCounter_capMax = (TextView) findViewById(R.id.tvCounter_capMax);
        tvHouseUp_priceIndicator = (TextView) findViewById(R.id.tvHouseUp_priceIndicator);
        sbHouseUp_priceSelector = (SeekBar) findViewById(R.id.sbHouseUp_priceSelector);
        tbFilter_adapted = (ToggleButton) findViewById(R.id.tbFilter_adapted);
        tbFilter_air = (ToggleButton) findViewById(R.id.tbFilter_air);
        tbFilter_barbecue = (ToggleButton) findViewById(R.id.tbFilter_barbecue);
        tbFilter_bath = (ToggleButton) findViewById(R.id.tbFilter_bath);
        tbFilter_pool = (ToggleButton) findViewById(R.id.tbFilter_pool);
        tbFilter_climatized = (ToggleButton) findViewById(R.id.tbFilter_climatized);
        tbFilter_garden = (ToggleButton) findViewById(R.id.tbFilter_garden);
        tbFilter_heating = (ToggleButton) findViewById(R.id.tbFilter_heating);
        tbFilter_jacuzzi = (ToggleButton) findViewById(R.id.tbFilter_jacuzzi);
        tbFilter_kitchen = (ToggleButton) findViewById(R.id.tbFilter_kitchen);
        tbFilter_mountain = (ToggleButton) findViewById(R.id.tbFilter_mountain);
        tbFilter_parking = (ToggleButton) findViewById(R.id.tbFilter_parking);
        tbFilter_beach = (ToggleButton) findViewById(R.id.tbFilter_beach);
        tbFilter_breakfast = (ToggleButton) findViewById(R.id.tbFilter_breakfast);
        tbFilter_children = (ToggleButton) findViewById(R.id.tbFilter_children);
        tbFilter_fireplace = (ToggleButton) findViewById(R.id.tbFilter_fireplace);
        tbFilter_pets = (ToggleButton) findViewById(R.id.tbFilter_pets);
        tbFilter_spa = (ToggleButton) findViewById(R.id.tbFilter_spa);
        tbFilter_tv = (ToggleButton) findViewById(R.id.tbFilter_tv);
        tbFilter_wifi = (ToggleButton) findViewById(R.id.tbFilter_wifi);
        btnSelectGaleryImage = (Button) findViewById(R.id.btnSelectGaleryImage);
        btnCapMax_less = (Button) findViewById(R.id.btnCapMax_less);
        btnCapMax_plus = (Button) findViewById(R.id.btnCapMax_plus);

        // Gestión del SeekBar para seleccionar el precio
        generateSeekBar();

    }

    //--> MÉTODOS
    // Gestión del SeekBar para seleccionar el precio
    private void generateSeekBar() {

        sbHouseUp_priceSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Mostrar indicador de texto de la selección
                tvHouseUp_priceIndicator.setText(progress + " € por persona");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //--> CLICK BOTONES
    // Controlador de los radio buttom: Integro/ Habitaciones
    public void CheckButtonTypeHouse(View view) {
    }

    // Seleccionar fechas disponibles
    public void clickSelectDateAvailable(View view) {
    }

    // Acción para seleccionar ON/OFF los iconos de los servicios
    public void OnDefaultToggleClickComplete(View view) {
    }

    // Publicar/ Subir oferta de la casa
    public void clickPublishHouse(View view) {


    }

    // Volver a la vista de Mi Perfil
    public void clickBackFilters(View view) {

        Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
        startActivity(intent);
        finish();

    }
}