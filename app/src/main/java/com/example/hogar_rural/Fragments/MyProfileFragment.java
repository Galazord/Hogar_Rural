package com.example.hogar_rural.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.ExplorerActivity;
import com.example.hogar_rural.LoginFormActivity;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 ---------- MIS DATOS DE PERFIL ----------
 Aquí se muestra los datos del usuario que se ha logado (si no lo está se mostrará la vista MyProfile para loguearse o registrarse).
 En esta vista de pueden modificar los datos y cerrar sesión del usuario actual.
 */

public class MyProfileFragment extends Fragment {

    //--> VARIABLES
    private Button btnCloseSession, btnUpdateProfile;
    private TextView tvUser_nick, tvUser_name, tvUser_dni, tvUser_birthday, tvUser_phone, tvUser_mail, tvUser_adress, tvUser_postal, tvUser_municipality, tvUser_province;
    private ImageView ivUser_avatar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    public MyProfileFragment() {

    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Iniciar componentes
        initComponent(view);

        // Cargar los datos del usuario logado
        loadUserFromDB();

        return view;
    }

    //--> MÉTODOS
    private void initComponent(View view){

        // Relaccionar las variables con la parte gráfica
        tvUser_nick = (TextView) view.findViewById(R.id.tvUser_nick);
        tvUser_name = (TextView) view.findViewById(R.id.tvUser_name);
        tvUser_dni = (TextView) view.findViewById(R.id.tvUser_dni);
        tvUser_birthday = (TextView) view.findViewById(R.id.tvUser_birthday);
        tvUser_phone = (TextView) view.findViewById(R.id.tvUser_phone);
        tvUser_mail = (TextView) view.findViewById(R.id.tvUser_mail);
        tvUser_adress = (TextView) view.findViewById(R.id.tvUser_adress);
        tvUser_postal = (TextView) view.findViewById(R.id.tvUser_postal);
        tvUser_municipality = (TextView) view.findViewById(R.id.tvUser_municipality);
        tvUser_province = (TextView) view.findViewById(R.id.tvUser_province);
        ivUser_avatar = (ImageView) view.findViewById(R.id.ivUser_avatar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage  = FirebaseStorage.getInstance();

        // BOTÓN: Modificar los datos de usuario
        btnUpdateProfile = (Button) view.findViewById(R.id.btnModifyUser);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Ir a la vista de activity_login_form desde la opción de modificar
                Intent i = new Intent(getContext(), LoginFormActivity.class);
                // Mandamos un parámetro (extra) para indicar que venimos del botón modificar usuario
                i.putExtra("typeFormUser", "modify");
                startActivity(i);

            }
        });

        // BOTÓN: Cerrar sesión de usuario y volver al Explorar
        btnCloseSession = (Button) view.findViewById(R.id.btnCloseSession);
        btnCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                        .setTitle("Cerrar Sesión")
                        .setMessage("Estás seguro de cerrar sesion?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAuth.signOut(); // Dar la orden en firebase de cerrar sesión

                                // Ir a la vista del explorador de casas
                                Intent i = new Intent(getContext(), ExplorerActivity.class);
                                startActivity(i);

                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

    }

    // Cargar y mostrar los datos del usuario logado en ese momento
    private void loadUserFromDB(){

        // Recoger el ID del usuario logado
        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos del usuario
                        User user = doc.toObject(User.class);
                        // mostrar los datos del usuario en los campos correspondientes
                        tvUser_nick.setText(user.getNickname());
                        tvUser_name.setText(user.getName() + " " + user.getLastname());
                        tvUser_dni.setText(user.getDni());
                        tvUser_birthday.setText(user.getBirthday());
                        tvUser_phone.setText(user.getPhone());
                        tvUser_mail.setText(user.getEmail());
                        tvUser_adress.setText(user.getAddress());
                        tvUser_postal.setText(user.getPostal());
                        tvUser_municipality.setText(user.getMunicipality());
                        tvUser_province.setText(user.getProvince());

                        // Cargar la imagen de usuario por defecto o la suya
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
                    Glide.with(getContext()).load(task.getResult()).into(ivUser_avatar);
                }
            }

        });
    }
}