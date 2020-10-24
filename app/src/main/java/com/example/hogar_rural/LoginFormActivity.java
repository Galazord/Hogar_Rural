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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.Utils.Constant;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.Calendar;

public class LoginFormActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView tvForm_title, tvForm_info, etForm_input_birthday, tvForm_link_terms;
    private EditText etForm_input_user, etForm_input_email, etForm_input_passw, etForm_input_passwRepit, etForm_input_name, etForm_input_lastname, etForm_input_dni, etForm_input_phone, etForm_input_address, etForm_input_postal, etForm_input_municipality, etForm_input_province;
    private ImageView ivForm_avatar;
    private CheckBox cbForm_terms, cbForm_advertising;
    private Button btnUpAvatar, btnRegister, btnBack;
    private DatePickerDialog.OnDateSetListener setListener;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String typeFormUser;
    private MediaPlayer soundError, soundCorrect;
    private Uri filePath;
    private String urlImage;
    private boolean ImageExist = false;
    private String urlImageModify;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Recibir parámetros
        // tipo: crear/modificar usuario
        Bundle b = getIntent().getExtras();
        if(b!=null){
            typeFormUser = b.getString("typeFormUser");
        }

        // Inicializar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Inicializar componentes
    private void initComponent(){

        // Establecer Titular en el action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulario para darse de alta como usuario");

        // Relaccionar las variables con la parte gráfica
        tvForm_title = (TextView) findViewById(R.id.tvForm_title);
        tvForm_info = (TextView) findViewById(R.id.tvForm_info);
        tvForm_link_terms = (TextView) findViewById(R.id.tvForm_link_terms);
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

        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();

        // Activar la función de calendario para la fecha de nacimiento
        activateCalendar();

        // Dependiendo si venimos de registrarse o modificar, el nombre del botón de confirmación cambia de nombre
        if(typeFormUser.equals(Constant.BUNDLE_CREATE)){

            btnRegister.setText(R.string.formUser_register);

            // Mostrar los checkbox
            cbForm_terms.setVisibility(View.VISIBLE);
            cbForm_advertising.setVisibility(View.VISIBLE);

        }
        // Si viene de registrarse, ir a la clase UserAccountActivity.
        else if(typeFormUser.equals(Constant.BUNDLE_MODIFY)){

            // Modificar titular y texto introductorio
            tvForm_title.setText(R.string.formUser_title_modify);
            tvForm_info.setText(R.string.formUser_info_modify);

            btnRegister.setText(R.string.formUser_save_changes);

            // Ocultar los checkbox
            cbForm_terms.setVisibility(View.GONE);
            cbForm_advertising.setVisibility(View.GONE);
            tvForm_link_terms.setVisibility(View.GONE);

            // mmostrar en los campos los datos del usuario logado actualmente
            showDataUser();

        }
        else {
            UtilMethod.showToast(TypeToast.ERROR, this,"ERROR. Problemas con la variable typeFormUser.");
            soundError.start();
        }

    }

    // Mostrar en los campos los datos del usuario logado actualmente
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
                        urlImageModify = user.getImage();
                        loadUserImage(user);
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

    private void loadUserImage(User user){
        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(user.getImage());
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(getApplicationContext()).load(task.getResult()).into(ivForm_avatar);
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

    }

    // Realizar el registro de usuario
    private void registerUser(final User user){

        if(typeFormUser.equals(Constant.BUNDLE_MODIFY)){
            // Modificar datos de usuario
            String idUser = mAuth.getCurrentUser().getUid();
            user.setId(idUser);

            registerUserFirestore(user);

        }else if(typeFormUser.equals(Constant.BUNDLE_CREATE)){

            // Crear nuevo usuario


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

        }else {

        }
    }

    // Dar de alta al usuario en Firebase y guardar sus datos
    private void registerUserFirestore(User user){

        final String idUser = user.getId();

        // Comprobar si la imagen es nueva, modificada o poner una por defecto
        if(ImageExist){
            urlImage = Constant.URL_GS_IMAGE_USER+user.getId();
        }else{
            if(typeFormUser.equals(Constant.BUNDLE_MODIFY)){
                urlImage = user.getImage();
            }else{
                urlImage = Constant.URL_GS_IMAGE_USER_DEFAULT;
            }

        }
        user.setImage(urlImage);

        // Subir los datos de usuario a firebase
        mFirestore.collection("users")
                .document(user.getId())
                    .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadImage(idUser);
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

        ImageExist = true;

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    // Subir imagen al servidor
    private void uploadImage(String idUser)
    {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo foto...");
            progressDialog.show();

            StorageReference ref = storageReference.child("users/"+idUser);

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressDialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: No se ha subido la imagen" + e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // Barra de progreso al subir la imagen
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                            progressDialog.setMessage("Subida "+(int)progress+"%");
                        }
                    });
        }



    }
    // Validación de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            filePath = data.getData();

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
        boolean advertising = false;

        if(TextUtils.isEmpty(etForm_input_user.getText()) || TextUtils.isEmpty(etForm_input_email.getText()) || TextUtils.isEmpty(etForm_input_passw.getText()) || TextUtils.isEmpty(etForm_input_passwRepit.getText()) || TextUtils.isEmpty(etForm_input_name.getText()) || TextUtils.isEmpty(etForm_input_lastname.getText()) || TextUtils.isEmpty(etForm_input_dni.getText()) || TextUtils.isEmpty(etForm_input_birthday.getText()) || TextUtils.isEmpty(etForm_input_phone.getText())){
            // En caso de no rellenar todos los campos obligatorios
            UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. Rellenar todos los campos obligatorios (*).");
            soundError.start();
        }else{

            // Si viene de registrarse, debe aceptar los terminos y condiciones de uso.
            if(typeFormUser.equals(Constant.BUNDLE_CREATE)){

                if(cbForm_terms.isChecked()){

                    // Comprobar que la contraseña y la repeticción de la contraseña son iguales
                    if(password.equals(repitPassword)){

                        // Si acepta el usuario recibir publicidad
                        if(cbForm_advertising.isChecked()){
                            advertising = true;
                        }

                        // Terminar de registrar o modificar datos de usuario
                        finalRegisterUser(email, nickname, password, name, lastname, Constant.URL_GS_IMAGE_USER_DEFAULT, dni, birthday, phone, address, postal, municipality, province, advertising);

                    }
                    else{
                        UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: Las dos contraseñas introducidas no son iguales.");
                        soundError.start();
                    }

                }
                else{
                    UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: Debe aceptar los términos y condiciones de uso.");
                    soundError.start();
                }

            }
            else{

                // Comprobar que la contraseña y la repeticción de la contraseña son iguales
                if(password.equals(repitPassword)){

                    // Terminar de registrar o modificar datos de usuario
                    finalRegisterUser(email, nickname, password, name, lastname, urlImageModify, dni, birthday, phone, address, postal, municipality, province, advertising);

                }
                else{
                    UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR: Las dos contraseñas introducidas no son iguales.");
                    soundError.start();
                }

            }

        }

    }

    // Terminar de registrar o modificar datos de usuario
    private void finalRegisterUser(String email, String nickname, String password, String name, String lastname, String urlAvatar, String dni, String birthday, String phone, String address, String postal, String municipality, String province, boolean advertising) {

        // Guardar todos los datos del usuario registrado
        User user = new User(email, nickname, password, name, lastname, urlAvatar, dni, birthday, phone, address, postal, municipality, province, advertising);

        // Registrar los datos del usuario
        registerUser(user);

    }

    //--> CLICK BOTONES
    // Volver a la vista anterior
    public void clickBtnBackRegister(View view) {

        // Si viene de registrarse, ir a la clase MyProfileActivity.
        if(typeFormUser.equals(Constant.BUNDLE_CREATE)){

            Intent intent = new Intent (getApplicationContext(), MyProfileActivity.class);
            startActivity(intent);
            finish();

        }
        // Si viene de registrarse, ir a la clase UserAccountActivity.
        else if(typeFormUser.equals(Constant.BUNDLE_MODIFY)){

            Intent intent = new Intent (getApplicationContext(), UserAccountActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            UtilMethod.showToast(TypeToast.ERROR, LoginFormActivity.this,"ERROR. No se ha podido volver atrás");
            soundError.start();
        }

    }

    // Ir a la página para ver el documento legal de términos de uso de la apliación
    public void clickReadTermsUser(View view) {

        Uri uri = Uri.parse("http://www.galanteartdesign.com/");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }
}