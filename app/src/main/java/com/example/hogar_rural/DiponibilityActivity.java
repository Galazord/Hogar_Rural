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
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

public class DiponibilityActivity extends AppCompatActivity {

    //--> VARIABLES

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String idHouse;
    private List<Timestamp> dates_reserved;
    MCalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diponibility);

        // Inicializar componentes
        initComponent();

        Bundle b = getIntent().getExtras();

        if(b!=null){
            idHouse = b.getString("ID_HOUSE");
            loadHomeFromDB();
        }

    }

    //--> MÉTODOS
    // Inicializar componentes

    private void initComponent() {
        dates_reserved = new ArrayList<>();
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Relaccionar la parte gráfica con las variables
        calendar = (MCalendarView)findViewById(R.id.calendarView);
        calendar.setDateCell(Color.TRANSPARENT);
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
                        }
                        Log.i("size",dates_reserved.size()+"");
                        selectReservedDate();
                    }
                });

    }

    private void selectReservedDate(){
        for (Timestamp tmp :
                dates_reserved) {

            Date date = new Date(tmp.toDate().getTime() + (1000 * 60 * 60 * 24));
            String dateStr =  UtilMethod.getDate(date);
            String parts[] = dateStr.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);


            calendar.markDate(
                    new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)
                    ));

            }

    }
    //--> BOTONES
    // Confimar fechas seleccionadas en el calendario
    public void btnConfirmDisponibility(View view) {

    }
}