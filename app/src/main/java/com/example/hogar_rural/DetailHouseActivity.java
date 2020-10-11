package com.example.hogar_rural;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Interface.DbRetrofitApi;
import com.example.hogar_rural.Model.Comment;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.Service;
import com.example.hogar_rural.Model.User;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DetailHouseActivity extends AppCompatActivity {

    //--> VARIABLES
    private TextView DetailPlace, DetailRental, DetailPeople, DetailPrice, DetailNumOpinions,tvEmptyComment, DetailValorations, DetailTextMultiLine1, DetailTextMultiLine2, DetailTextMultiLine3, DetailTextMultiLine4, tvPropertyName, tvPropertyUpdate, tvPropertyUpdateData;
    private ImageView CardDetailImage, ivDetail_avatarProperty, ivDetail_avatarUser,iconTempOff1,iconTempOff2,iconTempOff3,iconTempOff4,iconTempOff5;
    private ImageView[] arrValoration;
    private ToggleButton tbFilterDetail_adapted, tbFilterDetail_air, tbFilterDetail_barbecue, tbFilterDetail_bath, tbFilterDetail_pool, tbFilterDetail_climatized, tbFilterDetail_garden, tbFilterDetail_heating, tbFilterDetail_jacuzzi, tbFilterDetail_kitchen, tbFilterDetail_mountain, tbFilterDetail_parking, tbFilterDetail_beach, tbFilterDetail_breakfast, tbFilterDetail_children, tbFilterDetail_fireplace, tbFilterDetail_pets, tbFilterDetail_spa, tbFilterDetail_tv, tbFilterDetail_wifi;
    private List<ToggleButton> servicesIcon;
    private Button btnFavorite,btnShowMore1,btnShowMore2,btnShowMore3;
    private DbRetrofitApi dbRetrofitApi;
    private MediaPlayer soundError;
    private String idHouse, idProperty,destine;
    private User user;
    private User ownerHome;
    private RecyclerView recyclerViewComments, recyclerViewServices;
    private List<Comment> comments;
    private List<Service> serviciosFinalHome;
    private Home home;
    private int pagination = 2;
    private Long valoration_comment = -1L;
    private EditText etDetail_addComent;
    private Boolean isFavorite = false;
    private Boolean activeDetailRental = true, activeDetailActivities= true, activePlaces= true;
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private List<DocumentReference> favorites  = new ArrayList<>();;
    private List<String> listUrlImages;
    private int index = 0;
    // Música & Sonidos
    private MediaPlayer soundSuccess;

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
        comments = new ArrayList<>();
        // Relaccionar las variables con la parte gráfica
        etDetail_addComent = (EditText) findViewById(R.id.etDetail_addComent);
        DetailPlace = (TextView) findViewById(R.id.DetailPlace);
        DetailRental = (TextView) findViewById(R.id.DetailRental);
        DetailPeople = (TextView) findViewById(R.id.DetailPeople);
        DetailPrice = (TextView) findViewById(R.id.DetailPrice);
        DetailNumOpinions = (TextView) findViewById(R.id.DetailNumOpinions);
        DetailValorations = (TextView) findViewById(R.id.DetailValorations);
        btnShowMore1 = (Button) findViewById(R.id.btnShowMore1);
        btnShowMore2 = (Button) findViewById(R.id.btnShowMore2);
        btnShowMore3 = (Button) findViewById(R.id.btnShowMore3);
        DetailTextMultiLine1 = (TextView) findViewById(R.id.DetailTextMultiLine1);
        DetailTextMultiLine2 = (TextView) findViewById(R.id.DetailTextMultiLine2);
        DetailTextMultiLine3 = (TextView) findViewById(R.id.DetailTextMultiLine3);

      /*  tbFilterDetail_adapted = (ToggleButton) findViewById(R.id.tbFilterDetail_adapted);
        tbFilterDetail_air = (ToggleButton) findViewById(R.id.tbFilterDetail_air);
        tbFilterDetail_barbecue = (ToggleButton) findViewById(R.id.tbFilterDetail_barbecue);
        tbFilterDetail_bath = (ToggleButton) findViewById(R.id.tbFilterDetail_bath);
        tbFilterDetail_pool = (ToggleButton) findViewById(R.id.tbFilterDetail_pool);
        tbFilterDetail_climatized = (ToggleButton) findViewById(R.id.tbFilterDetail_climatized);
        tbFilterDetail_garden = (ToggleButton) findViewById(R.id.tbFilterDetail_garden);
        tbFilterDetail_heating = (ToggleButton) findViewById(R.id.tbFilterDetail_heating);
        tbFilterDetail_jacuzzi = (ToggleButton) findViewById(R.id.tbFilterDetail_jacuzzi);
        tbFilterDetail_kitchen = (ToggleButton) findViewById(R.id.tbFilterDetail_kitchen);
        tbFilterDetail_mountain = (ToggleButton) findViewById(R.id.tbFilterDetail_mountain);
        tbFilterDetail_parking = (ToggleButton) findViewById(R.id.tbFilterDetail_parking);
        tbFilterDetail_beach = (ToggleButton) findViewById(R.id.tbFilterDetail_beach);
        tbFilterDetail_breakfast = (ToggleButton) findViewById(R.id.tbFilterDetail_breakfast);
        tbFilterDetail_children = (ToggleButton) findViewById(R.id.tbFilterDetail_children);
        tbFilterDetail_fireplace = (ToggleButton) findViewById(R.id.tbFilterDetail_fireplace);
        tbFilterDetail_pets = (ToggleButton) findViewById(R.id.tbFilterDetail_pets);
        tbFilterDetail_spa = (ToggleButton) findViewById(R.id.tbFilterDetail_spa);
        tbFilterDetail_tv = (ToggleButton) findViewById(R.id.tbFilterDetail_tv);
        tbFilterDetail_wifi = (ToggleButton) findViewById(R.id.tbFilterDetail_wifi);

        servicesIcon = new ArrayList<>();
        Collections.addAll(servicesIcon, tbFilterDetail_adapted,
                tbFilterDetail_air,
                tbFilterDetail_barbecue,
                tbFilterDetail_bath,
                tbFilterDetail_pool,
                tbFilterDetail_climatized,
                tbFilterDetail_garden,
                tbFilterDetail_heating,
                tbFilterDetail_jacuzzi,
                tbFilterDetail_kitchen,
                tbFilterDetail_mountain,
                tbFilterDetail_parking,
                tbFilterDetail_beach,
                tbFilterDetail_breakfast,
                tbFilterDetail_children,
                tbFilterDetail_fireplace,
                tbFilterDetail_pets,
                tbFilterDetail_spa,
                tbFilterDetail_tv,
                tbFilterDetail_wifi);
*/
        tvPropertyName = (TextView) findViewById(R.id.tvPropertyName);
        tvEmptyComment = (TextView) findViewById(R.id.tvEmptyComment);
        tvPropertyUpdate = (TextView) findViewById(R.id.tvPropertyUpdate);
        tvPropertyUpdateData = (TextView) findViewById(R.id.tvPropertyUpdateData);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
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

        iconTempOff1 = (ImageView) findViewById(R.id.iconTempOff1);
        iconTempOff2 = (ImageView) findViewById(R.id.iconTempOff2);
        iconTempOff3 = (ImageView) findViewById(R.id.iconTempOff3);
        iconTempOff4 = (ImageView) findViewById(R.id.iconTempOff4);
        iconTempOff5 = (ImageView) findViewById(R.id.iconTempOff5);

        arrValoration = new  ImageView[]{iconTempOff1,iconTempOff2,iconTempOff3,iconTempOff4,iconTempOff5};

        // Cargar imagen de avatar del comentario
        initValorationImages();

        for (ImageView iv: arrValoration
             ) {

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilMethod.showToast(TypeToast.INFO,DetailHouseActivity.this,v.getTag().toString());
                    // Cargar listado de valoraciones
                    loadValoration(Long.valueOf(v.getTag().toString()));
                }
            });
        }

        ivDetail_avatarProperty = (ImageView) findViewById(R.id.ivDetail_avatarProperty);
        ivDetail_avatarUser = (ImageView) findViewById(R.id.ivDetail_avatarUser);
        recyclerViewComments = (RecyclerView) findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewComments.setNestedScrollingEnabled(false);

        recyclerViewServices = (RecyclerView) findViewById(R.id.rvServices);
        recyclerViewServices.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        recyclerViewServices.setNestedScrollingEnabled(false);
        soundError = MediaPlayer.create(this, R.raw.sound_error);
        soundSuccess = MediaPlayer.create(this, R.raw.sound_correct);

        // Recibir los parámetros del intent procedente de Adapter.java
        Intent i = getIntent();
        idHouse = i.getStringExtra("idHouse");

        if(i.getExtras()!=null && i.getStringExtra("destine")!=null){

            destine = i.getStringExtra("destine");
        }else{

            destine = "";
        }

        // Cargar el contenido de la vivienda
        loadHomeFromDB();

    }

    // Menú superior: Resolver la búsqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details_house, menu);

        return true;
    }

    // Botón de compartir vivienda
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_shared:
                // Ejecuta la función de compartir vivienda
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Quiero compartir contigo esta casa que he visto en HOGAR RURAL.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;

            case android.R.id.home:
                // Opción para que conserve el destino al volver a la vista de Explorar
                Intent result = new Intent();
                result.putExtra("destine", destine);
                setResult(RESULT_OK, result);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Cargar los comentarios existentes en firebase sobre la vivienda seleccionada
    private void loadCommentFirestore(){


        db.collection("comments").orderBy("date", Query.Direction.DESCENDING).limit(pagination)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        comments.removeAll(comments);

                           for (QueryDocumentSnapshot document : value) {

                            Comment comment = document.toObject(Comment.class);

                            if(comment.getId_homes().equals(home.getId())){
                                comments.add(comment);

                                   }

                        }
                           // En el caso de que haya o no comentarios
                        if(comments.size()!=0){
                            tvEmptyComment.setVisibility(View.INVISIBLE);
                            tvEmptyComment.setText("");
                            loadRecyclerView();
                        }else{
                            tvEmptyComment.setVisibility(View.VISIBLE);
                            tvEmptyComment.setText("No hay comentarios");
                            //recyclerView.setBackgroundResource(getDrawable(R.id.myHouse_recycler_view_my_houses));
                        }
                    }
                });



    }
    private void countComments(){

        // inicializar el número de comentarios.


        db.collection("comments")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                          int cont = 0;
                        for (QueryDocumentSnapshot document : value) {

                            Comment comment = document.toObject(Comment.class);

                            if(comment.getId_homes().equals(home.getId())){
                                cont++;
                            }

                        }
                        DetailNumOpinions.setText(cont+" "+getResources().getString(R.string.house_nOpinion));

                    }
                });



    }
    // Cargar/ mostrar la información en el recyclerView
    private void loadRecyclerView(){

        AdapterComment adapter = new AdapterComment(getApplicationContext(), comments);
        recyclerViewComments.setAdapter(adapter);


    }

    private void loadRecyclerViewServices(List<Service> services){

        AdapterService adapter = new AdapterService(getApplicationContext(), services);
        recyclerViewServices.setAdapter(adapter);


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
                         home = doc.toObject(Home.class);
                        loadServices();
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
                        DetailNumOpinions.setText("");
                        DetailTextMultiLine1.setText(home.getDescription().replace("\\n", "\n"));
                        DetailTextMultiLine2.setText(home.getActivities().replace("\\n", "\n"));
                        DetailTextMultiLine3.setText(home.getInteresting_places().replace("\\n", "\n"));

                        Date date = home.getUpdate_date().toDate(); // Convertir en formato fecha (timeStamp --> date)
                        String dateStr =  UtilMethod.getDate(date); // Convertirlo en String y separar por partes
                        String parts[] = dateStr.split("-"); // Separar por partes
                        String dateUpdateLast = parts[0] + "/" + parts[1] + "/" + parts[2];
                        tvPropertyUpdate.setText(getResources().getString(R.string.house_lastUpdate));
                        tvPropertyUpdateData.setText(dateUpdateLast);

                        // Limitar el núemro de caracteres y poner puntos suspensivos a los bloques de texto
                        hideViewMore();

                        listUrlImages = home.getImages();
                        // Cargar la galería de imágenes de la casa actual
                         loadHomeGallery(listUrlImages.get(index));

                        // Cargar y mostrar los datos del propietario de la casa actual
                        idProperty = home.getOwner();
                        loadPropertyFromDB(idProperty);

                        // cargar la imagen del usuario logado en ese momento
                        if(mAuth.getCurrentUser()!=null){
                            loadUserFromDB();
                        }

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

    // Limitar el núemro de caracteres y poner puntos suspensivos a los bloques de texto
    private void hideViewMore(){
        DetailTextMultiLine1.setText(UtilMethod.subStringText(DetailTextMultiLine1.getText().toString(),250,"..."));
        DetailTextMultiLine2.setText(UtilMethod.subStringText(DetailTextMultiLine2.getText().toString(),250,"..."));
        DetailTextMultiLine3.setText(UtilMethod.subStringText(DetailTextMultiLine3.getText().toString(),250,"..."));

    }

    // Cargar imagen de avatar del comentario
    private void initValorationImages(){
        for (int i = 0; i<arrValoration.length; i++){
            arrValoration[i].setBackgroundResource(R.drawable.ic_leaf_off);
        }
    }

    // Cargar listado de valoraciones
    private void loadValoration(Long val ){
        valoration_comment = val;
        for (int i = 0; i<val; i++){

            arrValoration[i].setBackgroundResource(R.drawable.ic_leaf_on);
        }

        for (int i = val.intValue(); i<arrValoration.length; i++){

            arrValoration[i].setBackgroundResource(R.drawable.ic_leaf_off);
        }


    }

    // Cargar los iconos activos de los servicios
    private void loadServices(){
        Service service = new Service();
        List<String> serviciosHome = home.getServices();
       serviciosFinalHome = new ArrayList<>();
        for (Service s: service.getServices(this)
             ) {

            // Comprueba si el servicio está en el array de Firebase. Y si está guarda el servicio entero.
            if(serviciosHome.contains(s.getName())){
                serviciosFinalHome.add(s);
            }
        }

        loadRecyclerViewServices(serviciosFinalHome);

    }

    // Cargar la galería de imágenes de la vivienda actual
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



    // Cargar y mostrar los datos del propietario de la casa
    private void loadPropertyFromDB(String idProperty){

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
                        loadPropertyImage(user);

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

    // Recoger los datos del usuario propietario de la vivienda actual
    private void loadInfoOwner(){

        DocumentReference docRef = db.collection("users").document(home.getOwner());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos del usuario
                        ownerHome = doc.toObject(User.class);


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
    private void loadPropertyImage(User user){

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

    // Cargar y mostrar los datos del usuario logado en ese momento
    private void loadUserFromDB(){

        DocumentReference docRef = db.collection("users").document(mAuth.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        // Recoger los datos del usuario
                        user = doc.toObject(User.class);

                        // Cargar la imagen de usuario por defecto o la suya
                        loadUserImage(user);
                        countComments();
                        loadFavorite();
                        loadCommentFirestore();
                        loadInfoOwner();
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

    // Cargar la imagen del usuario actual
    private void loadUserImage(User user){

        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(user.getImage());
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(getApplicationContext()).load(task.getResult()).into(ivDetail_avatarUser);
                }
            }
        });
    }

    // Carga las viviendas qeu están marcadas como favoritas
    private void loadFavorite(){
        if(mAuth.getCurrentUser()!=null){
            btnFavorite.setVisibility(View.VISIBLE);

            // Cuando el usuario está logado
            if(user.getFavorites()!=null){
                favorites = user.getFavorites();
                DocumentReference documentReference = db.collection("homes").document(home.getId());

                if(favorites.contains(documentReference)){
                    btnFavorite.setBackgroundResource(R.drawable.ic_favo_on);
                    isFavorite = true;
                }

            }

        // Si el usuario NO está logado el icono no se mostrará
        }else{
            btnFavorite.setVisibility(View.INVISIBLE);
        }
    }

    // Guardar/ eliminar el icono de favorito de la casa seleccionada
    private void saveFav(){
        // Recoger el ID del usuario logado
        DocumentReference documentReference = db.collection("homes").document(home.getId());

        // Si la lista de favoritos del usuario logado ya tiene alguno...
        if(favorites.size()!=0){

            //... comprueba si está en ON...
            if(isFavorite){
                favorites.remove(documentReference);
                btnFavorite.setBackgroundResource(R.drawable.ic_favo_off);
                isFavorite = false;
            }else{
                //... o en OFF
                favorites.add(documentReference);
                btnFavorite.setBackgroundResource(R.drawable.ic_favo_on);
                isFavorite = true;
            }


        }else{
            favorites.add(documentReference);
            btnFavorite.setBackgroundResource(R.drawable.ic_favo_on);
            isFavorite = true;
        }

        // Guarda la información del estado del favorito en la base de datos
        db.collection("users").document(user.getId())
                .update("favorites",favorites).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }


    //--> CLICK BOTONES
    // Botón de disponibilidad de fechas
    public void clickShowDisponibility(View view) {

        // Ir a la ventana se seleccionar fechas
        Intent intentDate = new Intent (getApplicationContext(), DiponibilityActivity.class);
        intentDate.putExtra("ID_HOUSE",home.getId());
        startActivity(intentDate);

    }

    // Ver más información de las características
    public void clickShowMoreFeatures(View view) {
        if(activeDetailRental){
            DetailTextMultiLine1.setText(home.getDescription().replace("\\n", "\n"));
            activeDetailRental = false;
            btnShowMore1.setText("Ver menos");
        }else{
            DetailTextMultiLine1.setText(UtilMethod.subStringText(DetailTextMultiLine1.getText().toString().replace("\\n", "\n"),250,"..."));

            activeDetailRental = true;
            btnShowMore1.setText("Ver más");
        }

    }

    // Ver más información de las actividades
    public void clickShowMoreActivities(View view) {
        if(activeDetailActivities){
            DetailTextMultiLine2.setText(home.getActivities().replace("\\n", "\n"));
            activeDetailActivities = false;
            btnShowMore2.setText("Ver menos");

        }else{
            DetailTextMultiLine2.setText(UtilMethod.subStringText(DetailTextMultiLine2.getText().toString().replace("\\n", "\n"),250,"..."));

            activeDetailActivities = true;
            btnShowMore2.setText("Ver más");
        }

    }

    // Ver más información de los lugares de interés
    public void clickShowMorePlaceInterest(View view) {
        if(activePlaces){
            DetailTextMultiLine3.setText(home.getInteresting_places().replace("\\n", "\n"));
            activePlaces = false;
            btnShowMore3.setText("Ver menos");
        }else{
            DetailTextMultiLine3.setText(UtilMethod.subStringText(DetailTextMultiLine3.getText().toString().replace("\\n", "\n"),250,"..."));

            activePlaces = true;
            btnShowMore3.setText("Ver más");
        }

    }

    // Ver más información de los comentarios
    public void clickShowMoreComments(View view) {

        pagination = pagination+2;
        loadCommentFirestore();
    }

    // Botón OK para enviar el comentario
    public void clickSendComment(View view) {

        // Comprobar que hay al menos un comentario para poder enviarlo
        if(!TextUtils.isEmpty(etDetail_addComent.getText())){
            String id_comment = UtilMethod.getUIID();
            String comment = etDetail_addComent.getText().toString();
            String id_user = user.getId();
            String name_user = user.getName();
            String id_homes = home.getId();

            Comment c = new Comment(id_comment,id_homes,comment,id_user,name_user,valoration_comment,Timestamp.now());

            registerCommentFirestore(c);

        }else{
          UtilMethod.showToast(TypeToast.INFO,this,"Debes de escribir un comentario");
        }

    }
    // Subir los datos de usuario a firebase
    private void registerCommentFirestore(Comment com){

        // Generar una ID para cada casa
        final String id = com.getId();

        // Gestionar el registro final
        db.collection("comments")
                .document(id)
                .set(com)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etDetail_addComent.setText("");
                        pagination = 2;
                        loadRecyclerView();
                        initValorationImages();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        UtilMethod.showToast(TypeToast.ERROR, DetailHouseActivity.this,"ERROR Al guardar comentario.");
                        soundError.start();
                        Log.e("ERROR FIRESTORE USER: ", e.getMessage());
                    }
                });
    }
    // Botón para llamar al propietario de la casa
    public void clickCallOwner(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+ownerHome.getPhone()));
        startActivity(callIntent);
    }

    // Botón para enviar un email al propietario de la casa
    public void clickEmailOwner(View vinoew) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { ownerHome.getEmail() });
        intent.putExtra(Intent.EXTRA_SUBJECT, "¡Hola amigo! Mira que casa he visto en la APP HOGAR RURAL");
        intent.putExtra(Intent.EXTRA_TEXT, home.getName()+" "+home.getPrice());
        startActivity(Intent.createChooser(intent, ""));

        /*
        // Mensaje satisfactorio
        UtilMethod.showToast(TypeToast.SUCCESS, this,"Gracias, su mensaje ha sido enviado.");
        soundSuccess.start();
         */

    }

    // Efecto de pasar imágenes en la galería
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

    // Activar/ desactivar icono de favorito
    public void clickFavorite(View view) {
        saveFav();
    }

    // Ir al mapa de "cómo llegar"
    public void clickGoToMap(View view) {
        Intent i =new Intent(this, MapActivity.class);
        startActivity(i);
    }
}