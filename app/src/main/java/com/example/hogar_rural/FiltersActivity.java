package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class FiltersActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tvFilter_priceIndicator, tvFilter_input_entrance, tvFilter_input_exit, tvFilter_nPersons;
    private Button btnLessPerson, btnPlusPerson, btnLessValoration, btnPlusValoration;
    private ToggleButton tbFilter_complete, tbFilter_room;
    private SeekBar sbFilter_priceSelector;
    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        // Iniciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {

        // Relacionar las variables con la parte gráfica
        tvFilter_priceIndicator = (TextView) findViewById(R.id.tvFilter_priceIndicator);
        tvFilter_input_entrance = (TextView) findViewById(R.id.tvFilter_input_entrance);
        tvFilter_input_exit = (TextView) findViewById(R.id.tvFilter_input_exit);
        tvFilter_nPersons = (TextView) findViewById(R.id.tvFilter_nPersons);
        btnLessPerson = (Button) findViewById(R.id.btnLessPerson);
        btnPlusPerson = (Button) findViewById(R.id.btnPlusPerson);
        btnLessValoration = (Button) findViewById(R.id.btnLessValoration);
        btnPlusValoration = (Button) findViewById(R.id.btnPlusValoration);
        tbFilter_complete = (ToggleButton) findViewById(R.id.tbFilter_complete);
        tbFilter_room = (ToggleButton) findViewById(R.id.tbFilter_room);
        sbFilter_priceSelector = (SeekBar) findViewById(R.id.sbFilter_priceSelector);

        // Mostrar calendario para la fecha de llegada
        activateCalendar(tvFilter_input_entrance);
        // Mostrar calendario para la fecha de salida
        activateCalendar(tvFilter_input_exit);

        // Gestión del SeekBar para seleccionar el precio
        generateSeekBar();

    }

    // Activar el efecto para mostrar un calendario e introducir una fecha
    private void activateCalendar(final TextView textView) {

        // Gestión de la opción del calendario
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Generar la ventana del calendario
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FiltersActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // Modo calendario tradicional introducir las fechas
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FiltersActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        textView.setText(date);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

    }

    // Gestión del SeekBar para seleccionar el precio
    private void generateSeekBar() {

        sbFilter_priceSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Mostrar indicador de texto de la selección
                tvFilter_priceIndicator.setText("- " + progress + " euros por persona -");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    // Acción ToggleButtom: Habitaciones íntegras
    public void OnDefaultToggleClickComplete(View view) {
        Toast.makeText(this, "OnDefaultToggleClickComplete", Toast.LENGTH_SHORT).show();
    }


    //--> CLICK BOTONES
    // Confirmar la selección de filtros
    public void clickConfirmFilters(View view) {


    }

    // Volver a la vista de explorar
    public void clickBackFilters(View view) {

        Intent intent = new Intent (getApplicationContext(), ExplorerActivity.class);
        startActivity(intent);
        finish();

    }


}