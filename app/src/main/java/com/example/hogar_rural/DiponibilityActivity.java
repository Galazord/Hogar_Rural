package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.hogar_rural.Model.Available;
import com.example.hogar_rural.Model.Comment;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class DiponibilityActivity extends AppCompatActivity {

    //--> VARIABLES

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String idHouse;
    private List<Timestamp> dates_reserved;
    private List<DocumentReference> users_reserved;
    private List<DocumentReference> users_new_reserved;
    private List<Timestamp> dates_new_reserved;
    DocumentReference documentReferenceUser;
    TextView tvPrecio;
    MCalendarView calendar;
    private static long price;
    private int numberOfDay=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diponibility);

        // Inicializar componentes
        initComponent();

        Bundle b = getIntent().getExtras();

        if(b!=null){
            idHouse = b.getString("ID_HOUSE");
            price = b.getLong("PRECIO");
            loadHomeFromDB();
        }

    }

    //--> MÉTODOS
    // Inicializar componentes


    private void initComponent() {
        dates_reserved = new ArrayList<>(); // lista de reserva
        dates_new_reserved = new ArrayList<>(); // Lista de nuevas reservas
        users_reserved = new ArrayList<>();
        users_new_reserved = new ArrayList<>();
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Relaccionar la parte gráfica con las variables
        tvPrecio = (TextView) findViewById(R.id.tvPrecio);
        calendar = (MCalendarView)findViewById(R.id.calendarView);




        documentReferenceUser = db.collection("users").document(mAuth.getCurrentUser().getUid());

        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

             if(UtilMethod.datePermited(date.getYear(),date.getMonth(),date.getDay())){
                 UtilMethod.showToast(TypeToast.INFO,DiponibilityActivity.this,"No puedes seleccionar una fecha inferior al dia actual");
             }else{
                 Timestamp dateSelected =  UtilMethod.getTimestamp(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());

                 // Si está en verde deseleccionar
                 if(dates_new_reserved.contains(dateSelected)){
                     dates_new_reserved.remove(dateSelected);
                     users_new_reserved.remove(documentReferenceUser);
                     calendar.unMarkDate(new DateData(date.getYear(),date.getMonth(),date.getDay()));
                         numberOfDay--;
                     // Si está deselecionado marcar en verde
                 }else{
                     dates_new_reserved.add(dateSelected);
                     calendar.markDate(new DateData(date.getYear(),date.getMonth(),date.getDay()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)));
                     users_new_reserved.add(documentReferenceUser);
                     numberOfDay++;
                 }
                 setPrecioInfo();
             }



            }
        });
    }

    private void setPrecioInfo(){
        if(numberOfDay == 0){

            tvPrecio.setText("");
        }else{
            tvPrecio.setText(numberOfDay+" dia/s\nPrecio por : "+price+"€\nTotal: "+(price*numberOfDay)+"€");
        }
    }
    private void loadHomeFromDB(){


        db.collection("availables").whereEqualTo("id",idHouse)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        for (QueryDocumentSnapshot document : value) {
                            Available available = document.toObject(Available.class);
                            dates_reserved = available.getDates_reserved();
                            users_reserved = available.getUsers_reserved();
                        }

                        selectReservedDate();
                    }
                });

    }

    // Mostrar las reservas que ya tiene
    private void selectReservedDate(){

        // Coger las fechas reservadas de firebase
        for (Timestamp tmp :
                dates_reserved) {

            //Date date = new Date(tmp.toDate().getTime() + (1000 * 60 * 60 * 24));
            Date date = tmp.toDate(); // Convertir en formato fecha (timeStamp --> date)
            String dateStr =  UtilMethod.getDate(date); // Convertirlo en String y separar por partes
            String parts[] = dateStr.split("-"); // Separar por partes
            int day = Integer.parseInt(parts[0]); // DÍA
            int month = Integer.parseInt(parts[1]); // MES
            int year = Integer.parseInt(parts[2]); // AÑO

            // Marcar en rojo las que ya están reservadas
            calendar.markDate(
                    new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)
                    ));

            }

    }



    // Subir los datos de usuario a firebase
    private void saveReserved(){
        for (Timestamp t: dates_new_reserved
             ) {
            dates_reserved.add(t);

        }
        for (DocumentReference df: users_new_reserved
        ) {
            users_reserved.add(df);

        }
        Available available = new Available(idHouse,dates_reserved,users_reserved);

        // Gestionar el registro final
        db.collection("availables")
                .document(idHouse)
                .set(available)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        UtilMethod.showToast(TypeToast.SUCCESS, DiponibilityActivity.this,"Se ha realizado la reserva correctamente");
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        UtilMethod.showToast(TypeToast.ERROR, DiponibilityActivity.this,"ERROR GENERAL.");

                        Log.e("ERROR FIRESTORE USER: ", e.getMessage());
                    }
                });
    }


    //--> BOTONES
    // Confimar fechas seleccionadas en el calendario
    public void btnConfirmDisponibility(View view) {
        saveReserved();

    }
}