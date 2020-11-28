package com.example.hogar_rural.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hogar_rural.Adapter;
import com.example.hogar_rural.AdapterBooking;
import com.example.hogar_rural.Model.Available;
import com.example.hogar_rural.Model.Booking;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.R;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyBookingsFragment extends Fragment {

    //--> VARIBALES
    private ImageView cvBooking_avatar;
    private ImageView imgEmpty;
    private TextView tvBooking_name, tvBooking_dates;
    private Spinner spinOwnHouse;
    // RecyclerView
    private RecyclerView recyclerView;
    private ArrayList<Booking> list_bookings = new ArrayList<Booking>();
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private List<String> idUsersHomes = new ArrayList<>();

    String selecteHouseName = "";
    //--> CONTRUCTOR
    public MyBookingsFragment() {}

    public static MyBookingsFragment newInstance() {

        MyBookingsFragment fragment = new MyBookingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        // Iniciar componentes
        initComponent(view);
        return view;
    }

    // MÉTODOS
    private void initComponent(View view) {

        // Relaccionar las variables con la parte gráfica
        cvBooking_avatar = (ImageView) view.findViewById(R.id.cvBooking_avatar);
        imgEmpty = (ImageView) view.findViewById(R.id.imgEmpty);
        tvBooking_name = (TextView) view.findViewById(R.id.tvBooking_name);
        tvBooking_dates = (TextView) view.findViewById(R.id.tvBooking_dates);
        spinOwnHouse = (Spinner) view.findViewById(R.id.spinOwnHouse);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvMyBooking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        spinOwnHouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteHouseName =  (String)parent.getItemAtPosition(position);

                loadBookings(idUsersHomes.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Init firestore
        mFirestore = FirebaseFirestore.getInstance();
        //Instanciamos el auth
        mAuth = FirebaseAuth.getInstance();
        //Instaciamos Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseStorage  = FirebaseStorage.getInstance();

        cargarHomeSpinner();

    }

    private void cargarHomeSpinner(){
            mFirestore.collection("homes").whereEqualTo("owner",mAuth.getCurrentUser().getUid())

                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            ArrayList<String> nameHomes = new ArrayList<>();

                            // Recorrer firebase y guardar cada infomación de la casa en un objeto tipo Home y en una lista (list_home)
                            for (QueryDocumentSnapshot document : value) {
                                Home h = document.toObject(Home.class);
                                nameHomes.add(h.getName());
                                idUsersHomes.add(h.getId());

                                Log.i("aaa",h.getName()+" id: "+h.getId());
                            }

                            if(nameHomes.size()!=0){
                                ArrayAdapter<String> adapter =   new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nameHomes);
                                spinOwnHouse.setAdapter(adapter);
                            }else{
                                UtilMethod.showToast(TypeToast.ERROR,getActivity(),"No tienes casas asignadas para buscar");
                            }

                        }
                    });



    }
    private List<Booking> getBookingAgrupate(List<DocumentReference> users, List<Timestamp> dates){
        List<Booking> bookings = new ArrayList<>();
        List<String> dates_str = new ArrayList<>();
        List<DocumentReference> userFilter = new ArrayList<>();
        List<Timestamp> datesFilter = new ArrayList<>();
        String firstUserStr = "";
        DocumentReference firstUser = null;
        Date today = Timestamp.now().toDate();

        for (int i=0; i<dates.size(); i++) {
            if(dates.get(i).toDate().after(today)){
                userFilter.add(users.get(i));
                datesFilter.add(dates.get(i));

            }

        }


        if(userFilter.size()>=0){
            firstUserStr = userFilter.get(0).toString();
            firstUser = userFilter.get(0);
        }

        int index = 0;
        for (DocumentReference docRef: userFilter
             ) {
            String ref = docRef.toString();

            if (ref.equals(firstUserStr)) {
                Date date = new Date(datesFilter.get(index).toDate().getTime() + (1000 * 60 * 60 * 24));
                dates_str.add(UtilMethod.getDate(date));


            }else{
                Booking booking = new Booking(firstUser, dates_str);
                bookings.add(booking);

                dates_str = new ArrayList<>();
                firstUserStr = ref;
                firstUser = docRef;

                Date date = new Date(datesFilter.get(index).toDate().getTime() + (1000 * 60 * 60 * 24));
                dates_str.add(UtilMethod.getDate(date));
            }

            index++;

            if(index == userFilter.size()-1){
                Booking booking = new Booking(firstUser, dates_str);
                bookings.add(booking);
            }


        }

        return bookings;
    }
    private void loadBookings(String homeId){


        DocumentReference docRef = mFirestore.collection("availables").document(homeId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        Date today = Timestamp.now().toDate();
                        // Recoger los datos del usuario
                        Available available = doc.toObject(Available.class);
                        List<Timestamp> datesReserverd = available.getDates_reserved();
                        List<DocumentReference> usersId = available.getUsers_reserved();
                        List<Booking> bookings = new ArrayList<>();
                        if(datesReserverd!=null && usersId!=null && datesReserverd.size() !=0 && datesReserverd.size() == usersId.size()){

                            List<Booking> bookingsAgrupate = getBookingAgrupate(usersId,datesReserverd);
                           /* for (int i=0; i<datesReserverd.size(); i++) {
                                if(datesReserverd.get(i).toDate().after(today)){

                                    Booking booking = new Booking(datesReserverd.get(i),usersId.get(i));
                                    bookings.add(booking);
                                }

                            }*/
                            loadAdapterBooking(bookingsAgrupate);
                        }else{
                            UtilMethod.showToast(TypeToast.WARNNING,getActivity(),"no hay casas");
                        }
                    }
                    else{
                        Log.i("ERROR Available", task.getResult().toString());
                        UtilMethod.showToast(TypeToast.INFO,getActivity(), "No Tienes reservas");
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Log.i("ERROR GET Available", task.getResult().toString());
                }
            }
        });
    }

    private void loadAdapterBooking(List<Booking> bookings){

        AdapterBooking adapterBooking = new AdapterBooking(getActivity(), bookings);
            recyclerView.setAdapter(adapterBooking);
            recyclerView.setVisibility(View.VISIBLE);



    }
}