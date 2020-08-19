package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileActivity extends AppCompatActivity {

    // VARIABLES
    private TextView tv_infoProfile;
    private Button btnLogin, btnRegister;
    private EditText et_input_user, et_input_passw;
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initComponent();
    }

    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Acceder a la sección de usuario
                            Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(getApplicationContext(),"No se ha podido acceder", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }
    private void initComponent(){
        //Iniciamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Relacionar variables con la parte gráfica
        tv_infoProfile = (TextView) findViewById(R.id.tv_infoProfile);
        tv_infoProfile.setText("Si deseas iniciarte como usuario tendrás las siguientes ventajas:\n\n" +
                "- Tus datos, lugares favoritos y otros marcadores se guardarán para recuperarlos en caso de cambiar de dispositivo.\n" +
                "- Podrás incluir tu mismo una oferta de casa rural y disponer de las funciones de propietario para gestionarlo.\n" +
                "- Diponer de la sección “Novedades” donde te sugerimos casas rurales en función de tus gustos.\n\n\n" +
                "Si dispone ya de una cuenta de usuario inicie sesión a continuación:");
        et_input_user = (EditText) findViewById(R.id.et_input_user);
        et_input_passw = (EditText) findViewById(R.id.et_input_passw);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Acceder al menu de la app
                Intent intent = new Intent (getApplicationContext(), LoginFormActivity.class);
                startActivity(intent);
            }
        });


        // Inicializar y asignar VARIABLES
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navegation);

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


    public void clickBtnLogIn(View view) {

        String email = et_input_user.getText().toString();
        String pass = et_input_passw.getText().toString();

        if(TextUtils.isEmpty(et_input_passw.getText()) || TextUtils.isEmpty(et_input_user.getText())){
            Toast.makeText(getApplicationContext(),"No se ha podido acceder", Toast.LENGTH_LONG).show();
        }else{
            logIn(email,pass);
        }
    }
}