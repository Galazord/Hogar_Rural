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
import android.media.MediaPlayer;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class LoginFormActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tvForm_title, tvForm_info, etForm_input_birthday;
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
    private String typeFormUser;
    private MediaPlayer soundError, soundCorrect;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Recibir parámetros
        typeFormUser = getIntent().getStringExtra("typeFormUser"); // tipo: crear/modificar usuario

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
        tvForm_title = (TextView) findViewById(R.id.tvForm_title);
        tvForm_info = (TextView) findViewById(R.id.tvForm_info);
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
        soundError = MediaPlayer.create(this, R.raw.sound_error);
        soundCorrect = MediaPlayer.create(this, R.raw.sound_correct);

        // Activar la función de calendario para la fecha de nacimiento
        activateCalendar();

        // Dependiendo si venimos de registrarse o modificar, el nombre del botón de confirmación cambia de nombre
        if(typeFormUser.equals("create")){

            btnRegister.setText(R.string.formUser_register);

            // Mostrar los checkbox
            cbForm_terms.setVisibility(View.VISIBLE);
            cbForm_advertising.setVisibility(View.VISIBLE);

        }
        // Si viene de registrarse, ir a la clase UserAccountActivity.
        else if(typeFormUser.equals("modify")){

            // Modificar titular y texto introductorio
            tvForm_title.setText(R.string.formUser_title_modify);
            tvForm_info.setText(R.string.formUser_info_modify);

            btnRegister.setText(R.string.formUser_save_changes);

            // Ocultar los checkbox
            cbForm_terms.setVisibility(View.GONE);
            cbForm_advertising.setVisibility(View.GONE);

            // mmostrar en los campos los datos del usuario logado actualmente
            showDataUser();

        }
        else {
            UtilMethod.showToast(TypeToast.ERROR, this,"ERROR. Problemas con la variable typeFormUser.");
            soundError.start();
        }

    }

    // mmostrar en los campos los datos del usuario logado actualmente
    private void showDataUser() {

        // Recoger el ID del usuario logado
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = mFirestore.collection("users").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos del usuario
                        User user = doc.toObject(User.class);
                        // mostrar los datos del usuario en los campos correspondientes
                        etForm_input_user.setText(user.getNickname());
                        etForm_input_email.setText(user.getEmail());
                        etForm_input_passw.setText(user.getPassword());
                        etForm_input_passwRepit.setText(user.getPassword());
                        etForm_input_name.setText(user.getName());
                        etForm_input_lastname.setText(user.getLastname());
                        etForm_input_dni.setText(user.getDni());
                        etForm_input_birthday.setText(user.getBirthday());
                        etForm_input_phone.setText(user.getPhone());
                        etForm_input_address.setText(user.getAddress());
                        etForm_input_postal.setText(user.getPostal());
                        etForm_input_municipality.setText(user.getMunicipality());
                        etForm_input_province.setText(user.getProvince());

                    }
                    else{
                        Log.i("ERROR USER NOT EXIST", task.getResult().toString());
                    }
                }else{
                    Log.i("ERROR GET USER", task.getResult().toString());
                }
            }
        });
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
    private void registerUser(final User user){

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

                        } else {

                            // Si la señal falla mostrará el mensaje de error correspondiente
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. La contraseña es incorrecta");
                                soundError.start();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. El e-mail es incorrecto");
                                soundError.start();
                            } catch(FirebaseAuthUserCollisionException e) {
                                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. El usuario introducido ya existe");
                                soundError.start();
                            } catch(Exception e) {
                                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. Se ha producido un fallo inesperado.");
                                soundError.start();
                            }
                        }

                    }
                });
    }

    // Dar de alta al usuario en Firebase y guardar sus datos
    private void registerUserFirestore(User user){

        mFirestore.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        UtilMethod.showToast(TypeToast.SUCCESS, LoginFormActivity.this,"Usuario registrado con éxito");
                        soundCorrect.start();

                        Intent intent = new Intent (getApplicationContext(), ExplorerActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR GENERAL.");
                        soundError.start();
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
                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: " + response);
                soundError.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: " + error.getMessage().toString());
                soundError.start();
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

        if(TextUtils.isEmpty(etForm_input_user.getText()) || TextUtils.isEmpty(etForm_input_email.getText()) || TextUtils.isEmpty(etForm_input_passw.getText()) || TextUtils.isEmpty(etForm_input_passwRepit.getText()) || TextUtils.isEmpty(etForm_input_name.getText()) || TextUtils.isEmpty(etForm_input_lastname.getText()) || TextUtils.isEmpty(etForm_input_dni.getText()) || TextUtils.isEmpty(etForm_input_birthday.getText()) || TextUtils.isEmpty(etForm_input_phone.getText())){
            // En caso de no rellenar todos los campos obligatorios
            UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. Rellenar todos los campos obligatorios (*).");
            soundError.start();
        }else{

            // Comprobar que la contraseña y la repeticción de la contraseña son iguales
            if(password.equals(repitPassword)){

                // Guardar todos los datos del usuario registrado
                User user = new User(email, nickname, password, name, lastname, "imagen.png", dni, birthday, phone, address, postal, municipality, province);

                // Registrar los datos del usuario
                registerUser(user);

                // Subir la foto de avatar de usuario
                //uploadImage();

            }
            else{
                UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. Las dos contraseñas introducidas no son iguales.");
                soundError.start();
            }

        }

    }

    //--> CLICK BOTONES
    // Volver a la vista anterior
    public void clickBtnBackRegister(View view) {

        // Si viene de registrarse, ir a la clase MyProfileActivity.
        if(typeFormUser.equals("create")){

            Intent intent = new Intent (getApplicationContext(), MyProfileActivity.class);
            startActivity(intent);
            finish();

        }
        // Si viene de registrarse, ir a la clase UserAccountActivity.
        else if(typeFormUser.equals("modify")){

            Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. No se ha podido volver atrás");
            soundError.start();
        }

    }
}