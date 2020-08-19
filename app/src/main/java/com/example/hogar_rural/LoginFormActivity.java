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

import org.w3c.dom.Text;

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

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        initComponent();

    }


    private void initComponent(){
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
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

        // Realizar el registro del formulario
        btnRegister = (Button) findViewById(R.id.btnRegister);


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

    private void registerUser(final Users user){
        final String emailUser = user.getEmail();

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
                            // If sign in fails, display a message to the user.

                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),"Password erronea", Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),"Email erroneo", Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(),"Ya existe", Toast.LENGTH_LONG).show();
                            } catch(Exception e) {
                                Toast.makeText(getApplicationContext(),"Error general", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                });
    }
    private void registerUserFirestore(Users user){
        mFirestore.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Usuario creado en bd", Toast.LENGTH_LONG).show();
                       // singIn();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error general", Toast.LENGTH_LONG).show();
                        Log.e("ERROR FIRESTORE USER", e.getMessage());
                    }
                });
    }

    public void clickBtnRegister(View view) {
        String name = etForm_input_name.getText().toString();
        String nickname =etForm_input_user.getText().toString();
        String email = etForm_input_email.getText().toString();
        String password = etForm_input_passw.getText().toString();

        if(TextUtils.isEmpty(etForm_input_name.getText()) || TextUtils.isEmpty(etForm_input_email.getText()) ){
            Toast.makeText(getApplicationContext(),"Debes de rellenar todos los camposr", Toast.LENGTH_LONG).show();
        }else{
            Users user = new Users(email, nickname, password, name, "apellido", "imagen.png","dni","fecha_nac","telefono","direccion","11400","municipio","provincia");

            registerUser(user);
        }


    }

    public void clickBtnUploadImage(View view) {

    }
}