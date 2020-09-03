package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tv_infoProfile;
    private Button btnLogin, btnRegister;
    private EditText et_input_mail, et_input_passw;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;
    private MediaPlayer soundError, soundCorrect;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Inicializar componentes
        initComponent();
    }

    //--> MÉTODOS
    // Inicializar componentes
    private void initComponent(){
        //Iniciamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Relacionar variables con la parte gráfica
        tv_infoProfile = (TextView) findViewById(R.id.tv_infoProfile);
        et_input_mail = (EditText) findViewById(R.id.et_input_email);
        et_input_passw = (EditText) findViewById(R.id.et_input_passw);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        bottomNavigationView = findViewById(R.id.bottom_navegation);
        soundError = MediaPlayer.create(this, R.raw.sound_error);
        soundCorrect = MediaPlayer.create(this, R.raw.sound_correct);

        //--> BARRA DE NAVEGACIÓN INFERIOR
        // Establecer este icono como marcado en el actual
        bottomNavigationView.setSelectedItemId(R.id.MyProfile);
        // Incorporar ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.SearchList: // EXPLORAR
                        startActivity(new Intent(getApplicationContext(), ExplorerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyProfile: // MI PERFIL
                        return true;
                    case R.id.OutStanding: // DESTACADOS
                        startActivity(new Intent(getApplicationContext(), OutstandingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Favorites: // FAVORITOS
                        startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

    // Acceder al login por una clave de usuario y mail
    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Acceder a la sección de usuario (UserAccountActivity)
                            Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error de acceso a la cuenta de usuario.
                            UtilMethod.showToast(TypeToast.ERROR, MyProfileActivity.this,"ERROR. No se ha podido acceder. Comprueba tu email y contraseña");
                            soundError.start();
                        }

                    }
                });
    }

    //--> CLICK BOTONES
    // Entrar para loguearse como usuario
    public void clickBtnLogIn(View view) {

        // Recoger el valor escrito en los campos de email y contraseña
        String email = et_input_mail.getText().toString();
        String pass = et_input_passw.getText().toString();

        // Verificar si los campos están vacíos
        if(TextUtils.isEmpty(et_input_passw.getText()) || TextUtils.isEmpty(et_input_mail.getText())){
            // Error si los campos están vacíos.
            UtilMethod.showToast(TypeToast.ERROR, MyProfileActivity.this,"ERROR. No se ha podido acceder. Rellena todos los campos.");
            soundError.start();
        }else{

            // Comprobar si el usuario existe en la base de datos
            logIn(email,pass);
        }
    }

    // Ir al formulario para darse de alta como usuario
    public void clickBtnGoFormRegister(View view) {

        // Ir a la clase LoginFormActivity.
        Intent intent = new Intent (getApplicationContext(), LoginFormActivity.class);
        intent.putExtra("typeFormUser", "create");
        startActivity(intent);
        finish();

    }

    // Olvidar contraseña
    public void clickBtnForgotPassw(View view) {

        if(TextUtils.isEmpty(et_input_mail.getText())){
            UtilMethod.showToast(TypeToast.ERROR, MyProfileActivity.this,"ERROR. Introduce un email.");
            soundError.start();
        }else{
            String email = et_input_mail.getText().toString();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UtilMethod.showToast(TypeToast.SUCCESS, MyProfileActivity.this,"Se le ha enviado un email para reestrablecer su contraseña");
                                soundCorrect.start();
                            }
                        }
                    });
        }
    }
}