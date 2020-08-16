package com.example.hogar_rural;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class LoginFormActivity extends AppCompatActivity {

    // VARIABLES
    private TextView etForm_input_birthday;
    private EditText etForm_input_user, etForm_input_passw, etForm_input_passwRepit, etForm_input_name, etForm_input_lastname, etForm_input_dni, etForm_input_phone, etForm_input_email, etForm_input_postal, etForm_input_municipality, etForm_input_province;
    private ImageView ivForm_avatar;
    private CheckBox cbForm_terms, cbForm_advertising;
    private Button btnUpAvatar, btnRegister, btnBack;
    private DatePickerDialog.OnDateSetListener setListener;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private final String URL_UPLOAD = "https://hogarruralapp.000webhostapp.com/hogarRural/php/upload.php";
    private String KEY_IMAGE = "photo";
    private String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Establecer Titular en el action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulario para darse de alta como usuario");

        // Relaccionar las variables con la parte gráfica
        etForm_input_birthday = (TextView) findViewById(R.id.etForm_input_birthday);

        etForm_input_user = (EditText) findViewById(R.id.etForm_input_user);
        etForm_input_passw = (EditText) findViewById(R.id.etForm_input_passw);
        etForm_input_passwRepit = (EditText) findViewById(R.id.etForm_input_passwRepit);
        etForm_input_name = (EditText) findViewById(R.id.etForm_input_name);
        etForm_input_lastname = (EditText) findViewById(R.id.etForm_input_lastname);
        etForm_input_dni = (EditText) findViewById(R.id.etForm_input_dni);
        etForm_input_phone = (EditText) findViewById(R.id.etForm_input_phone);
        etForm_input_email = (EditText) findViewById(R.id.etForm_input_email);
        etForm_input_postal = (EditText) findViewById(R.id.etForm_input_postal);
        etForm_input_municipality = (EditText) findViewById(R.id.etForm_input_municipality);
        etForm_input_province = (EditText) findViewById(R.id.etForm_input_province);

        ivForm_avatar = (ImageView) findViewById(R.id.ivForm_avatar);

        cbForm_terms = (CheckBox) findViewById(R.id.cbForm_terms);
        cbForm_advertising = (CheckBox) findViewById(R.id.cbForm_advertising);

        // Buscar la imagen
        btnUpAvatar = (Button) findViewById(R.id.btnUpAvatar);
        btnUpAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        // Realizar el registro del formulario
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Subir la imagen
                uploadImage();
            }
        });

        // Volver atrás
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activateCalendar();

    }

    // Activar el efecto para mostrar un calendario e introducir una fecha
    private void activateCalendar() {

        // Gestión de la opción del calendario
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Generar la ventana del calendario
        etForm_input_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LoginFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // Modo calendario con ruletas para introducir las fechas
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                etForm_input_birthday.setText(date);
            }
        };

        /*
        // Modo calendario tradicional introducir las fechas
        etForm_input_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(LoginFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        etForm_input_birthday.setText(date);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
         */

    }

    // Convierte la imagen en un string
    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;

    }

    // Subir imagen al servidor
    public void uploadImage(){

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Toast.makeText(LoginFormActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(LoginFormActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{

                String image = getStringImage(bitmap);
                String name = "miFoto";

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Para seleccionar una imagen
    private void showFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri filePath = data.getData();

            try {

                // Cómo obtener el mapa de bits de la galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Configuración del mapa de bits en el ImageView
                ivForm_avatar.setImageBitmap(bitmap);
                
            }catch (IOException e){

                e.printStackTrace();
            }

        }
    }
}