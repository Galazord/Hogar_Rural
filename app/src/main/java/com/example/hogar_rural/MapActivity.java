package com.example.hogar_rural;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String destiny;
    FirebaseFirestore db;
    private ArrayList<Home> list_home = new ArrayList<Home>();
    String idHome;
    boolean comoLlegar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle b = getIntent().getExtras();
        if (b!=null){
            destiny = b.getString("destiny");
            idHome = b.getString("idHome");
            if(idHome!=null){
                comoLlegar = true;
            }
            initComponent();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else{
            UtilMethod.showToast(TypeToast.INFO,this,"No se ha podido cargar el mapa");
        }


    }
    private void initComponent(){
        db = FirebaseFirestore.getInstance();
    }

    private void loadMarketFromDir(String dir, String name,String idHome){
        Geocoder coder = new Geocoder(getApplicationContext());

        try {
            ArrayList<Address> adresses = (ArrayList<Address>)  coder.getFromLocationName(dir, 1);

            if(adresses.size()!=0) {

                LatLng dirLatLng = new LatLng(adresses.get(0).getLatitude(), adresses.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(dirLatLng)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_market_map))
                        .title(name)
                ).setTag(idHome);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dirLatLng, 8f));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    private void loadFromFirebase(){
        db.collection("homes")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        // Empezar con la lista vacía antes de mostrar
                        list_home.removeAll(list_home);

                        // Recorrer firebase y guardar cada infomación de la casa en un objeto tipo Home y en una lista (list_home)
                        for (QueryDocumentSnapshot document : value) {

                            Home h = document.toObject(Home.class);
                             if(destiny.toLowerCase().equals(h.getProvince().toLowerCase())){

                                 String dir = h.getAddress().concat(", "+h.getPostal()+", "+h.getMunicipality()+", "+h.getProvince());
                                 Log.i("dir", dir);
                                 loadMarketFromDir(dir, h.getName(),h.getId());
                             }

                            Log.d("QUERY DB", document.getId() + " => " + document.getData());
                        }






                    }
                });


    }

    private void loadFromFirebaseHouse(){

        DocumentReference docRef = db.collection("homes").document(idHome);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos de la casa
                        Home h = doc.toObject(Home.class);
                        String dir = h.getAddress().concat(", "+h.getPostal()+", "+h.getMunicipality()+", "+h.getProvince());

                        loadMarketFromDir(dir, h.getName(),h.getId());

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
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_market_map);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(60, 40, vectorDrawable.getIntrinsicWidth() + 60, vectorDrawable.getIntrinsicHeight() + 40);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // the default resource ID of the actionBar's back button
                Intent intent = new Intent(getApplicationContext(), ExplorerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("destiny",destiny);
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                String idHouse = (String)(marker.getTag());
                if(!comoLlegar) {
                    Intent in = new Intent(getApplicationContext(), DetailHouseActivity.class);
                    in.putExtra("idHouse", idHouse);
                    in.putExtra("destine", destiny);
                    startActivity(in);
                }

                return false;
            }
        });

        if(!comoLlegar){

            loadFromFirebase();
        }else{
            loadFromFirebaseHouse();
        }


    }
}
