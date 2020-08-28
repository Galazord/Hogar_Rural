package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.Utils.Constant;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

import static com.example.hogar_rural.Utils.Constant.PRICE_MAX;
import static com.example.hogar_rural.Utils.Constant.PRICE_MIN;

public class HouseUpActivity extends AppCompatActivity {

    //--> VARIABLES
    private EditText etHouse_input_name, etHouse_input_address, etHouse_input_code, etHouse_input_municipaly, etHouse_input_province, etHouse_input_features, etHouse_input_activities, etHouse_input_interest;
    private RadioGroup RGHouse;
    private RadioButton rbHouseUp_complete, rbHouseUp_rooms;
    private TextView tvCounter_capMax, tvHouseUp_priceIndicator;
    private SeekBar sbHouseUp_priceSelector;
    private ToggleButton tbFilter_adapted, tbFilter_air, tbFilter_barbecue, tbFilter_bath, tbFilter_pool, tbFilter_climatized, tbFilter_garden, tbFilter_heating, tbFilter_jacuzzi, tbFilter_kitchen, tbFilter_mountain, tbFilter_parking, tbFilter_beach, tbFilter_breakfast, tbFilter_children, tbFilter_fireplace, tbFilter_pets, tbFilter_spa, tbFilter_tv, tbFilter_wifi;
    private Button btnSelectGaleryImage, btnCapMax_less, btnCapMax_plus, btnGallery_next_left, btnGallery_next_right, btnPrice_less, btnPrice_plus;
    private int numPeople = 1;
    private int price = PRICE_MIN;
    private MediaPlayer soundError, soundCorrect;
    private List<String> services;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_up);

        // Inciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Inciar componentes
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initComponent() {
        services = new ArrayList<>();
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
        sbHouseUp_priceSelector.setMin(PRICE_MIN);
        sbHouseUp_priceSelector.setMax(PRICE_MAX);
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
        btnGallery_next_left = (Button) findViewById(R.id.btnGallery_next_left);
        btnGallery_next_right = (Button) findViewById(R.id.btnGallery_next_right);
        btnPrice_less = (Button) findViewById(R.id.btnPrice_less);
        btnPrice_plus = (Button) findViewById(R.id.btnPrice_plus);
        soundError = MediaPlayer.create(this, R.raw.sound_error);
        soundCorrect = MediaPlayer.create(this, R.raw.sound_correct);

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();

        // Gestión del SeekBar para seleccionar el precio
        generateSeekBar();

    }

    //--> MÉTODOS
    // Gestión del SeekBar para seleccionar el precio
    private void generateSeekBar() {

        sbHouseUp_priceSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Cambiar color del botón restar por si acaso está gris de fondo
                btnPrice_less.setBackgroundResource(R.drawable.gradient_green);

                // Mostrar indicador de texto de la selección
                tvHouseUp_priceIndicator.setText(progress + " € por persona");
                price = progress;
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
    public void OnDefaultToggleClickServices(View view) {
        ToggleButton toggleButton = (ToggleButton)view;
        String serviceName = toggleButton.getTag().toString();
        services = UtilMethod.checkServicesList(services,serviceName);

        /*
        Log.i("ARRAY_SERVICIOS","Tam "+services.size());

        for(String a: services){
            Log.i("item de servicios",a);

        }
        */

    }

    // Publicar/ Subir oferta de la casa
    public void clickPublishHouse(View view) {


        //Añadir a este if todos aquellos campos que quieras que sean obligatorios.
        if(services.isEmpty() || TextUtils.isEmpty(etHouse_input_name.getText())){

        }else{
            String nameHome = etHouse_input_name.getText().toString();
            Long typeRoom = -1L;
            String address = etHouse_input_address.getText().toString();
            String municipality = etHouse_input_municipaly.getText().toString();
            String province = etHouse_input_province.getText().toString();
            String cp = etHouse_input_code.getText().toString();
            String features = etHouse_input_features.getText().toString();
            String activities = etHouse_input_activities.getText().toString();
            String interest_places = etHouse_input_interest.getText().toString();


          /*  Date date = new Date();
            String date_str = UtilMethod.getDate(date);
            Timestamp date_now = UtilMethod.getTimestamp(date_str);*/
            Timestamp date_now = Timestamp.now();
           typeRoom = UtilMethod.getNameSelectedRadioButton(RGHouse, getWindow().getDecorView().getRootView(), getApplicationContext());


            String idHome = UtilMethod.getUIID();

            Home home = new Home(idHome, mAuth.getCurrentUser().getUid(),nameHome,address, cp, municipality,province, features, activities,interest_places,typeRoom, (long) numPeople, (long)  price, (long) 0,date_now,date_now,services);
            registerHomesFirestore(home);
        }


    }

    private void registerHomesFirestore(Home home){

            // Subir los datos de usuario a firebase
            mFirestore.collection("homes")
                    .document(home.getId())
                    .set(home)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            UtilMethod.showToast(TypeToast.SUCCESS, HouseUpActivity.this,"Usuario registrado con éxito");
                            soundCorrect.start();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            UtilMethod.showToast(TypeToast.ERROR, HouseUpActivity.this,"ERROR GENERAL.");
                            soundError.start();
                            Log.e("ERROR FIRESTORE USER: ", e.getMessage());
                        }
                    });
    }

    // Volver a la vista de Mi Perfil
    public void clickBackFilters(View view) {

        Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
        startActivity(intent);
        finish();

    }

    // Añadir + personas
    public void clickAddPeople(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnCapMax_less.setBackgroundResource(R.drawable.gradient_green);

        // Sumar 1
        numPeople++;
        tvCounter_capMax.setText(numPeople+" personas");
    }

    // Añadir - personas
    public void clickLessPeople(View view) {
        if(numPeople==1){

            // Cambiar color para indicar que se ha llegado al mínimo posible
            btnCapMax_less.setBackgroundResource(R.drawable.gradient_grey);

        }else{

            // Restar 1
            numPeople--;
            tvCounter_capMax.setText(numPeople+" personas");
        }
    }

    // Pasar en la galería a la foto de la izquierda
    public void clickGalleryNextLeft(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnGallery_next_left.setBackgroundResource(R.drawable.gradient_green);

    }

    // Pasar en la galería a la foto de la izquierda
    public void clickGalleryNextRight(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnGallery_next_left.setBackgroundResource(R.drawable.gradient_grey);
    }

    // Disminuir en un punto el precio por día
    public void clickLessPrice(View view) {

        if(price == PRICE_MIN){

            // Cambiar color para indicar que se ha llegado al mínimo posible
            btnPrice_less.setBackgroundResource(R.drawable.gradient_grey);

        }else{

            // Restar 1
            price--;
            tvHouseUp_priceIndicator.setText(price+" € por persona");
        }

    }

    // Aumentar en un punto el precio por día
    public void clickPlusPrice(View view) {

        // Cambiar color para indicar que se puede llegar al mínimo posible
        btnPrice_less.setBackgroundResource(R.drawable.gradient_green);

        // Sumar 1
        price++;
        tvHouseUp_priceIndicator.setText(price+" € por persona");

    }
}