package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Interface.DbRetrofitApi;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DetailHouseActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView DetailPlace, DetailRental, DetailPeople, DetailPrice, DetailNumOpinions, DetailValorations, DetailTextMultiLine1, DetailTextMultiLine2, DetailTextMultiLine3, DetailTextMultiLine4, tvPropertyName;
    private ImageView CardDetailImage, ivDetail_avatarProperty;
    private DbRetrofitApi dbRetrofitApi;
    private MediaPlayer soundError;
    private String idHouse, idProperty;
    private User user;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    private List<String> listUrlImages;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        // Iniciar componentes
        initComponent();

    }

    //--> MÉTODOS
    // Iniciar componentes
    private void initComponent() {

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage  = FirebaseStorage.getInstance();

        // Relaccionar las variables con la parte gráfica
        DetailPlace = (TextView) findViewById(R.id.DetailPlace);
        DetailRental = (TextView) findViewById(R.id.DetailRental);
        DetailPeople = (TextView) findViewById(R.id.DetailPeople);
        DetailPrice = (TextView) findViewById(R.id.DetailPrice);
        DetailNumOpinions = (TextView) findViewById(R.id.DetailNumOpinions);
        DetailValorations = (TextView) findViewById(R.id.DetailValorations);
        DetailTextMultiLine1 = (TextView) findViewById(R.id.DetailTextMultiLine1);
        DetailTextMultiLine2 = (TextView) findViewById(R.id.DetailTextMultiLine2);
        DetailTextMultiLine3 = (TextView) findViewById(R.id.DetailTextMultiLine3);
        DetailTextMultiLine4 = (TextView) findViewById(R.id.DetailTextMultiLine4);
        tvPropertyName = (TextView) findViewById(R.id.tvPropertyName);
        CardDetailImage = (ImageView) findViewById(R.id.CardDetailImage);
        CardDetailImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //Check between +- 10px jsu tto have some are to hit
                    int centerX = (v.getWidth() /2);
                    if(event.getX() > centerX  ) {
                        index++;
                        Log.i("INDEX",index+"");

                    }else{

                        index--;
                        Log.i("INDEX",index+"");

                    }
                    configSwipeImage();
                }
                return true;
            }
        });



        ivDetail_avatarProperty = (ImageView) findViewById(R.id.ivDetail_avatarProperty);
        soundError = MediaPlayer.create(this, R.raw.sound_error);

        // Recibir los parámetros del intent procedente de Adapter.java
        Intent i = getIntent();
        idHouse = i.getStringExtra("idHouse");

        loadHomeFromDB();

    }

    // Cargar y mostrar los datos del usuario logado en ese momento
    private void loadHomeFromDB(){

        DocumentReference docRef = db.collection("homes").document(idHouse);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos de la casa
                        Home home = doc.toObject(Home.class);
                        // Mostrar los datos de la casa en los campos correspondientes
                        DetailPlace.setText(home.getName());
                        DetailPrice.setText(String.valueOf(home.getPrice()).concat(getApplicationContext().getString(R.string.adapter_price)));
                        String typeRental = "Error";
                        if(home.getType() == 1){
                            typeRental = "Íntegro";
                        }else if(home.getType() == 2) {
                            typeRental = "Habitaciones";
                        }
                        DetailRental.setText(typeRental);
                        DetailPeople.setText(String.valueOf(home.getAmount()).concat(getApplicationContext().getString(R.string.adapter_people)));
                        DetailNumOpinions.setText("Pendiente".concat(getApplicationContext().getString(R.string.adapter_comments)));
                        DetailTextMultiLine1.setText(home.getDescription());
                        DetailTextMultiLine2.setText(home.getActivities());
                        DetailTextMultiLine3.setText(home.getInteresting_places());
                        listUrlImages = home.getImages();
                        // Cargar la galería de imágenes de la casa actual
                         loadHomeGallery(listUrlImages.get(index));

                        // Cargar y mostrar los datos del usuario logado en ese momento
                        idProperty = home.getOwner();
                        loadUserFromDB(idProperty);

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

    private void loadHomeGallery(String img){


        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(img);
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(getApplicationContext()).load(task.getResult()).into(CardDetailImage);
                }
            }

        });
    }



    // Cargar y mostrar los datos del usuario logado en ese momento
    private void loadUserFromDB(String idProperty){

        DocumentReference docRef = db.collection("users").document(idProperty);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos del usuario
                        user = doc.toObject(User.class);
                        // mostrar los datos del usuario en los campos correspondientes
                        tvPropertyName.setText(user.getNickname());

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

    // Cargar la imagen del propietario
    private void loadUserImage(User user){

        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(user.getImage());
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(getApplicationContext()).load(task.getResult()).into(ivDetail_avatarProperty);
                }
            }
        });
    }

    //--> CLICK BOTONES
    // Botón de disponibilidad de fechas
    public void clickShowDisponibility(View view) {


    }

    // Ver más información de las características
    public void clickShowMoreFeatures(View view) {


    }

    // Ver más información de las actividades
    public void clickShowMoreActivities(View view) {


    }

    // Ver más información de los lugares de interés
    public void clickShowMorePlaceInterest(View view) {


    }

    // Ver más información de los comentarios
    public void clickShowMoreComments(View view) {


    }

    // Botón OK para enviar el comentario
    public void clickSendComment(View view) {


    }

    // Botón para llamar al propietario de la casa
    public void clickCallOwner(View view) {


    }

    // Botón para enviar un email al propietario de la casa
    public void clickEmailOwner(View view) {


    }
    private void configSwipeImage(){
        if(index == listUrlImages.size()){
            index = 0;
        }else if(index < 0){
            index = listUrlImages.size()-1;
        }

           /* Animation aniFadeOut = AnimationUtils.loadAnimation(context,R.anim.animation_fade_out);
            imageGalery.startAnimation(aniFadeOut);*/
        loadHomeGallery(listUrlImages.get(index));
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_fade_in);
        CardDetailImage.startAnimation(aniFade);
    }
    public void clickImageHomes(View view) {
       /* index++;
        configSwipeImage();*/
    }
}