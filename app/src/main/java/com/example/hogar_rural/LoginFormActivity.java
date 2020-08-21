package com.example.hogar_rural;

import androidx.annotation.NonNull;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hogar_rural.Fragments.MyProfileFragment;
import com.example.hogar_rural.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class LoginFormActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView etForm_input_birthday;
    private EditText etForm_input_user, etForm_input_email, etForm_input_passw, etForm_input_passwRepit, etForm_input_name, etForm_input_lastname, etForm_input_dni, etForm_input_phone, etForm_input_address, etForm_input_postal, etForm_input_municipality, etForm_input_province;
    private ImageView ivForm_avatar;
    private CheckBox cbForm_terms, cbForm_advertising;
    private Button btnUpAvatar, btnRegister, btnBack;
    private DatePickerDialog.OnDateSetListener setListener;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private final String URL_UPLOAD = "https://hogarruralapp.000webhostapp.com/hogarRural/php/upload.php";
    private String KEY_IMAGE = "photo";
    private String KEY_NAME = "name";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Inicializar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Inicializar componentes
    private void initComponent(){

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();

        // Establecer Titular en el action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulario para darse de alta como usuario");

        // Relaccionar las variables con la parte gráfica
        etForm_input_user = (EditText) findViewById(R.id.etForm_input_user);
        etForm_input_email = (EditText) findViewById(R.id.etForm_input_email);
        etForm_input_passw = (EditText) findViewById(R.id.etForm_input_passw);
        etForm_input_passwRepit = (EditText) findViewById(R.id.etForm_input_passwRepit);
        etForm_input_name = (EditText) findViewById(R.id.etForm_input_name);
        etForm_input_lastname = (EditText) findViewById(R.id.etForm_input_lastname);
        etForm_input_dni = (EditText) findViewById(R.id.etForm_input_dni);
        etForm_input_birthday = (TextView) findViewById(R.id.etForm_input_birthday);
        etForm_input_phone = (EditText) findViewById(R.id.etForm_input_phone);
        etForm_input_address = (EditText) findViewById(R.id.etForm_input_address);
        etForm_input_postal = (EditText) findViewById(R.id.etForm_input_postal);
        etForm_input_municipality = (EditText) findViewById(R.id.etForm_input_municipality);
        etForm_input_province = (EditText) findViewById(R.id.etForm_input_province);
        ivForm_avatar = (ImageView) findViewById(R.id.ivForm_avatar);
        cbForm_terms = (CheckBox) findViewById(R.id.cbForm_terms);
        cbForm_advertising = (CheckBox) findViewById(R.id.cbForm_advertising);
        btnUpAvatar = (Button) findViewById(R.id.btnUpAvatar);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack = (Button) findViewById(R.id.btnBack);

        // Activar la función de calendario para la fecha de nacimiento
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

    // Realizar el registro de usuario
    private void registerUser(final Users user){

        // Recoger el dato del email correcto del usuario
        final String emailUser = user.getEmail();

        // Validar el email y la contraseña introducidas
        mAuth.createUserWithEmailAndPassword(emailUser, user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //Aquí guardaríamos el usuario en base de datos.
                            user.setId(mAuth.getCurrentUser().getUid());
                            registerUserFirestore(user);

                            //saveUserDB(user);
                            //UtilMethod.showToast(TypeToast.SUCCESS, getParent().getParent(), getString(R.string.msg_success_register));
                        } else {

                            // Si la señal falla mostrará el mensaje de error correspondiente
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),"ERROR. La contraseña es incorrecta", Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),"ERROR. El e-mail es incorrecto", Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(),"ERROR. El usuario introducido ya existe", Toast.LENGTH_LONG).show();
                            } catch(Exception e) {
                                Toast.makeText(getApplicationContext(),"ERROR. Se ha producido un fallo inesperado.", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    // Dar de alta al usuario en Firebase y guardar sus datos
    private void registerUserFirestore(Users user){
        mFirestore.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                        // singIn();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error general", Toast.LENGTH_LONG).show();
                        Log.e("ERROR FIRESTORE USER: ", e.getMessage());
                    }
                });
    }

    // Para seleccionar una imagen
    private void showFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
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
            protected Map<String, String> getParams() throws AuthFailureError {

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
    // Validación de la imagen
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

    //--> CLICK BOTONES
    // Buscar y selecionar imagen de usuario
    public void clickBtnUploadImage(View view) {

        showFileChooser();
    }

    // Validar que se han introducido todos los campos obligatorios
    public void clickBtnRegister(View view) {

        // Campos obligatorios a recoger
        String nickname =etForm_input_user.getText().toString();
        String email = etForm_input_email.getText().toString();
        String password = etForm_input_passw.getText().toString();
        String repitPassword = etForm_input_passwRepit.getText().toString();
        String name = etForm_input_name.getText().toString();
        String lastname = etForm_input_lastname.getText().toString();
        String dni = etForm_input_dni.getText().toString();
        String birthday = etForm_input_birthday.getText().toString();
        String phone = etForm_input_phone.getText().toString();
        // Campos opcionales a recoger
        String address = etForm_input_address.getText().toString();
        String postal = etForm_input_postal.getText().toString();
        String municipality = etForm_input_municipality.getText().toString();
        String province = etForm_input_province.getText().toString();

        if(TextUtils.isEmpty(etForm_input_name.getText()) || TextUtils.isEmpty(etForm_input_email.getText()) ){
            // En caso de no rellenar todos los campos obligatorios
            Toast.makeText(getApplicationContext(),"ERROR. Rellenar todos los campos obligatorios (*).", Toast.LENGTH_LONG).show();
        }else{

            // Comprobar que la contraseña y la repeticción de la contraseña son iguales
            if(password.equals(repitPassword)){

                // Guardar todos los datos del usuario registrado
                Users user = new Users(email, nickname, password, name, lastname, "imagen.png", dni, birthday, phone, address, postal, municipality, province);

                // Registrar los datos del usuario
                registerUser(user);

                // Subir la foto de avatar de usuario
                //uploadImage();

                // Iniciar la vista del perfil de usuario
                Intent intent = new Intent (getApplicationContext(), MyProfileFragment.class);
                startActivity(intent);
                finish();

            }
            else{
                Toast.makeText(getApplicationContext(),"ERROR. Las dos contraseñas introducidas no son iguales.", Toast.LENGTH_LONG).show();
            }

        }

    }

    //--> CLICK BOTONES
    // Volver a la vista de inicio de sesión
    public void clickBtnBackRegister(View view) {

        // Ir a la clase MyProfileActivity.
        Intent intent = new Intent (getApplicationContext(), MyProfileActivity.class);
        startActivity(intent);
        finish();

    }
}