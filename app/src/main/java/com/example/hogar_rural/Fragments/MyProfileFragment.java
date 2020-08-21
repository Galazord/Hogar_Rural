package com.example.hogar_rural.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hogar_rural.ExplorerActivity;
import com.example.hogar_rural.Model.Users;
import com.example.hogar_rural.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {


    private Button btnCloseSession, btnUpdateProfile;
    private TextView tvUser_name, tvUser_dni, tvUser_birthday, tvUser_phone, tvUser_mail, tvUser_adress, tvUser_postal, tvUser_municipality, tvUser_province;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public MyProfileFragment() {
        // Required empty public constructor
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
        initComponent(view);
        loadUserFromDB();
        return view;
    }

    private void initComponent(View view){

        // Relaccionar las variables con la parte gráfica
        tvUser_name = (TextView) view.findViewById(R.id.tvUser_name);
        tvUser_dni = (TextView) view.findViewById(R.id.tvUser_dni);
        tvUser_birthday = (TextView) view.findViewById(R.id.tvUser_birthday);
        tvUser_phone = (TextView) view.findViewById(R.id.tvUser_phone);
        tvUser_mail = (TextView) view.findViewById(R.id.tvUser_mail);
        tvUser_adress = (TextView) view.findViewById(R.id.tvUser_adress);
        tvUser_postal = (TextView) view.findViewById(R.id.tvUser_postal);
        tvUser_municipality = (TextView) view.findViewById(R.id.tvUser_municipality);
        tvUser_province = (TextView) view.findViewById(R.id.tvUser_province);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

                                mAuth.signOut();

                                Intent i = new Intent(getContext(), ExplorerActivity.class);
                                startActivity(i);


                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });
        btnUpdateProfile = (Button) view.findViewById(R.id.btnModifyUser);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadUserFromDB(){
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Users user = doc.toObject(Users.class);
                        tvUser_name.setText(user.getNickname());
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
}