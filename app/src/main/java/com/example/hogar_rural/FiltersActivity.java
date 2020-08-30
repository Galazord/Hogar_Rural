package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Utils.Constant;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.firebase.Timestamp;

import java.util.Calendar;

import static com.example.hogar_rural.Utils.Constant.PRICE_MIN;

public class FiltersActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tvFilter_priceIndicator, tvFilter_input_entrance, tvFilter_input_exit, tvFilter_nPersons;
    private Button btnLessPerson, btnPlusPerson, btnLessValoration, btnPlusValoration, btnPrice_plus, btnPrice_less;
    private ToggleButton tbFilter_complete, tbFilter_room;
    private SeekBar sbFilter_priceSelector;
    private DatePickerDialog.OnDateSetListener setListener;
    private MediaPlayer soundError;
    private int numPeople = 1, numValoration = 0;
    private int price = PRICE_MIN;
    private CheckBox cbFilter_cheaper, cbFilter_expensive, cbFilter_MoreValor, cbFilter_Comentary;

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
        btnPrice_less = (Button) findViewById(R.id.btnPrice_less);
        btnPrice_plus = (Button) findViewById(R.id.btnPrice_plus);
        tbFilter_complete = (ToggleButton) findViewById(R.id.tbFilter_complete);
        tbFilter_room = (ToggleButton) findViewById(R.id.tbFilter_room);
        sbFilter_priceSelector = (SeekBar) findViewById(R.id.sbFilter_priceSelector);
        soundError = MediaPlayer.create(this, R.raw.sound_error);
        cbFilter_cheaper = (CheckBox) findViewById(R.id.cbFilter_cheaper);
        cbFilter_expensive = (CheckBox) findViewById(R.id.cbFilter_expensive);
        cbFilter_MoreValor = (CheckBox) findViewById(R.id.cbFilter_MoreValor);
        cbFilter_Comentary = (CheckBox) findViewById(R.id.cbFilter_Comentary);

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

                // Igualar a la variable precio
                price = progress;

                // No bajar del mínimo
                if(price < PRICE_MIN){
                    price = PRICE_MIN;
                }

                // Mostrar indicador de texto de la selección
                tvFilter_priceIndicator.setText(progress + " € por persona");

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
    // Añadir + personas
    public void clickAddPeople(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnLessPerson.setBackgroundResource(R.drawable.gradient_green);

        // Sumar 1
        numPeople++;
        tvFilter_nPersons.setText(numPeople+" personas");
    }

    // Añadir - personas
    public void clickLessPeople(View view) {
        if(numPeople==1){

            // Cambiar color para indicar que se ha llegado al mínimo posible
            btnLessPerson.setBackgroundResource(R.drawable.gradient_grey);

        }else{

            // Restar 1
            numPeople--;
            tvFilter_nPersons.setText(numPeople+" personas");
        }
    }

    // Añadir + valoración
    public void clickAddValoration(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnLessValoration.setBackgroundResource(R.drawable.gradient_green);

        // Sumar 1
        numValoration++;

    }

    // Añadir - personas
    public void clickLessValoration(View view) {
        if(numValoration==1){

            // Cambiar color para indicar que se ha llegado al mínimo posible
            btnLessValoration.setBackgroundResource(R.drawable.gradient_grey);

        }else{

            // Restar 1
            numValoration--;

        }
    }

    // Confirmar la selección de filtros
    public void clickConfirmFilters(View view) {

        // Recoger valores de los campos
        int totalPeople = numPeople;
        String dateEntrance = tvFilter_input_entrance.getText().toString();
        String dateExit = tvFilter_input_exit.getText().toString();
        Long typeRoom = -1L;
        String priceDay = tvFilter_priceIndicator.getText().toString();
        boolean checkCheaper = false;
        if(cbFilter_cheaper.isChecked()){
            checkCheaper = true;
        }
        boolean checkExpensive = false;
        if(cbFilter_expensive.isChecked()){
            checkCheaper = true;
        }
        boolean checkValoration = false;
        if(cbFilter_MoreValor.isChecked()){
            checkCheaper = true;
        }
        boolean checkComentary = false;
        if(cbFilter_Comentary.isChecked()){
            checkCheaper = true;
        }

        // Recoger valoraciones


        // Tipo de vivienda (Ïntegra/ habitaciones)


        // Realizar la gestión del filtro

    }

    // Volver a la vista de explorar
    public void clickBackFilters(View view) {

        Intent intent = new Intent (getApplicationContext(), ExplorerActivity.class);
        startActivity(intent);
        finish();

    }

    // Acción ToggleButtom: Habitaciones íntegras
    public void OnDefaultToggleClickComplete(View view) {
        //UtilMethod.showToast(TypeToast.SUCCESS, this,"OnDefaultToggleClickComplete");

        // Indicar que sólo pueda haber uno seleccinado en cuanto al tipo de casa
        if(tbFilter_complete.isChecked()){
            tbFilter_room.setChecked(false);
        }
        if(tbFilter_room.isChecked()){
            tbFilter_complete.setChecked(false);
        }

    }

    // Aumentar en un punto el precio
    public void clickPlusPriceFilter(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnPrice_less.setBackgroundResource(R.drawable.gradient_green);

        // Sumar 1
        price++;
        tvFilter_priceIndicator.setText(price+" € por persona");

    }

    // Disminuir en un punto el precio
    public void clickLessPriceFilter(View view) {

        if(price == PRICE_MIN){

            // Cambiar color para indicar que se ha llegado al mínimo posible
            btnPrice_less.setBackgroundResource(R.drawable.gradient_grey);

        }else{

            // Restar 1
            price--;

            // No bajar del mínimo
            if(price < PRICE_MIN){
                price = PRICE_MIN;
            }

            tvFilter_priceIndicator.setText(price+" € por persona");
        }

    }

}