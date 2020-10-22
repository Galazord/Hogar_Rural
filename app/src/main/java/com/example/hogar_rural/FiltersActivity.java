package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hogar_rural.Model.Filter;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.Service;
import com.example.hogar_rural.Utils.Constant;
import com.example.hogar_rural.Utils.TypeOrder;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.internal.Util;

import static com.example.hogar_rural.Utils.Constant.PRICE_MIN;

public class FiltersActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tvFilter_priceIndicator, tvFilter_input_entrance, tvFilter_input_exit, tvFilter_nPersons;
    private Button btnLessPerson, btnPlusPerson, btnLessValoration, btnPlusValoration, btnPrice_plus, btnPrice_less;
    private ImageView iconTemp, iconTemp2,iconTemp3,iconTemp4,iconTemp5;
    private ImageView[] valorationsImg;
    private ToggleButton tbFilter_complete, tbFilter_room;
    private SeekBar sbFilter_priceSelector;
    private DatePickerDialog.OnDateSetListener setListener;
    private MediaPlayer soundError;
    private int numPeople = 0, numValoration = 0;
    private int price = 0;
    private CheckBox cbFilter_cheaper, cbFilter_expensive, cbFilter_MoreValor;
    private CheckBox[] ordersCheck;
    private Boolean isIntegro= false, isHabitaciones = false;
    private RecyclerView rvServices;
    private List<Service> serviciosFinalHome;
    private AdapterService adapter;
    private int typeRoom = -1;
    private String destiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        // Iniciar componentes
        initComponent();
        loadServices();

    }


    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {
        Bundle b = getIntent().getExtras();
        if(b!=null){
            destiny = b.getString("destiny");
        }
        // Relacionar las variables con la parte gráfica
        rvServices = (RecyclerView) findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        rvServices.setNestedScrollingEnabled(false);
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
        ordersCheck =new CheckBox[] {cbFilter_cheaper,cbFilter_expensive,cbFilter_MoreValor};
        checkOrderControl();
        iconTemp = (ImageView)findViewById(R.id.iconTemp);
        iconTemp2 = (ImageView)findViewById(R.id.iconTemp2);
        iconTemp3 = (ImageView)findViewById(R.id.iconTemp3);
        iconTemp4 = (ImageView)findViewById(R.id.iconTemp4);
        iconTemp5 = (ImageView)findViewById(R.id.iconTemp5);
        valorationsImg = new ImageView[]{iconTemp,iconTemp2,iconTemp3,iconTemp4,iconTemp5};
        // Mostrar calendario para la fecha de llegada
        activateCalendar(tvFilter_input_entrance);
        // Mostrar calendario para la fecha de salida
        activateCalendar(tvFilter_input_exit);

        // Gestión del SeekBar para seleccionar el precio
        generateSeekBar();

    }
    private void checkOrderControl(){
        for (final CheckBox chekbox:ordersCheck
             ) {
            chekbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                        for (CheckBox ch:ordersCheck){
                            if(!ch.getTag().equals(chekbox.getTag())){
                                ch.setChecked(false);
                            }
                        }

                    }
                }
            });
        }
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


    private void loadRecyclerViewServices(List<Service> services){

        adapter = new AdapterService(getApplicationContext(), services);
        rvServices.setAdapter(adapter);


    }
    // Cargar los iconos activos de los servicios
    private void loadServices(){
        Service service = new Service();
        List<String> serviciosHome = new Home().getServices();
        List<Service> serviciosFinalHome = new ArrayList<>();
        for (Service s: service.getServices(this)
        ) {
             serviciosFinalHome.add(s);
        }

        loadRecyclerViewServices(serviciosFinalHome);

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

        if(numValoration<=valorationsImg.length-1) {
            numValoration++;
            for (int i = 0; i < numValoration; i++) {
                valorationsImg[i].setBackgroundResource(R.drawable.ic_leaf_on);
            }
            for (int i = numValoration; i < valorationsImg.length; i++) {
                valorationsImg[i].setBackgroundResource(R.drawable.ic_leaf_off);
            }
        }

    }

    // Añadir - personas
    public void clickLessValoration(View view) {

        if(numValoration>0) {
            numValoration--;
            for (int i = 0; i < numValoration; i++) {
                valorationsImg[i].setBackgroundResource(R.drawable.ic_leaf_on);
            }
            for (int i = numValoration; i < valorationsImg.length; i++) {
                valorationsImg[i].setBackgroundResource(R.drawable.ic_leaf_off);
            }
        }

    }

    // Confirmar la selección de filtros
    public void clickConfirmFilters(View view) {

        // Recoger valores de los campos

        String dateEntrance = tvFilter_input_entrance.getText().toString();
        String dateExit = tvFilter_input_exit.getText().toString();

        String priceDay = tvFilter_priceIndicator.getText().toString();
        TypeOrder order = UtilMethod.getOrder(ordersCheck);



        // Realizar la gestión del filtro*/
        Filter filter = new Filter(numPeople,dateEntrance,dateExit,numValoration,typeRoom,price,adapter.getSelectedServices(),order);

        Intent data = new Intent(getApplicationContext(),ExplorerActivity.class);
        data.putExtra("destiny", destiny);
        data.putExtra("filter", filter);
        startActivity(data);
        finish();


    }

    // Volver a la vista de explorar
    public void clickBackFilters(View view) {

        Intent intent = new Intent (getApplicationContext(), ExplorerActivity.class);
        intent.putExtra("destiny", destiny);
        startActivity(intent);
        finish();

    }

    // Acción ToggleButtom: Habitaciones íntegras
    public void OnDefaultToggleClickComplete(View view) {
        //UtilMethod.showToast(TypeToast.SUCCESS, this,"OnDefaultToggleClickComplete");

        if(view.getTag().toString().equals(getResources().getString(R.string.filters_rooms))){
            if(isHabitaciones){
                isHabitaciones = false;
                typeRoom = -1;
            }else{
                isHabitaciones= true;
                typeRoom = 2;
            }
            isIntegro = false;
        }else if(view.getTag().toString().equals(getResources().getString(R.string.filters_complete))){
            if(isIntegro){
                isIntegro = false;
                typeRoom = -1;
            }else{
                isIntegro= true;
                typeRoom = 1;
            }
            isHabitaciones= false;
        }

        tbFilter_room.setChecked(isHabitaciones);
        tbFilter_complete.setChecked(isIntegro);



    }

    // Aumentar en un punto el precio
    public void clickPlusPriceFilter(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnPrice_less.setBackgroundResource(R.drawable.gradient_green);

        if(price == 0)
        {
            price =  PRICE_MIN;

        }else{
            price++;
        }

        sbFilter_priceSelector.setProgress(price);
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
            sbFilter_priceSelector.setProgress(price);
            // No bajar del mínimo
            if(price < PRICE_MIN){
                price = PRICE_MIN;
            }

            tvFilter_priceIndicator.setText(price+" € por persona");
        }

    }

}