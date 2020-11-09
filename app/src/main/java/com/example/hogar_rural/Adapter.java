package com.example.hogar_rural;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Model.Comment;
import com.example.hogar_rural.Model.Home;
import com.example.hogar_rural.Model.Image;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.Utils.TypeToast;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    //--> VARIABLES
    private LayoutInflater inflater;
    private ArrayList<Home> model;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> imgGallery;
    private String idUser;
    private String destine;

    public List<DocumentReference> favorites;

    //--> CONSTRCUTOR
    public Adapter(Context context, ArrayList<Home> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
    }

    public Adapter(Context context, ArrayList<Home> model,String destine) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
        this.destine = destine;
    }

    // Los 3 métodos implementados del RecyclerView.Adapter
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Crear la vista del inflater
        View view = inflater.inflate(R.layout.custom_view_explore, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Obtener los datos del modelo y pasarlos a la vista
        // Aqui se ponen los componentes(titulo, fecha e imagen)
        String namePlace = model.get(position).getName();
        String typeRental ="";
        if(model.get(position).getType() == 1){
            typeRental = "Íntegro";
        }else if(model.get(position).getType() == 2) {
            typeRental = "Habitaciones";
        }

        holder.home = model.get(position);

        imgGallery = new ArrayList<>();
        imgGallery = holder.home.getImages();
        cargarImagen(imgGallery.get(0),holder.imageGalery);
        countComments(model.get(position).getId(),holder.txtNumOpinions);
        loadFavorite(holder.imageFavorite, model.get(position).getId());

        cargarValoration(holder.arrayValoration,model.get(position).getValoration());
        String price = String.valueOf(model.get(position).getPrice()).concat(this.context.getString(R.string.adapter_price));
        String numPerson = String.valueOf(model.get(position).getAmount()).concat(this.context.getString(R.string.adapter_people));
        String numOpinion = "4".concat(this.context.getString(R.string.adapter_comments));

        // Colocar datos en los campos de texto
        holder.txtPlace.setText(namePlace);
        holder.txtRental.setText(typeRental);
        holder.txtPrice.setText(price);
        holder.txtPeople.setText(numPerson);
        holder.txtNumOpinions.setText(numOpinion);

        // Incluir la imagen en la Galería de imagenes del Holder
        /* Glide.with(holder.itemView.getContext())
                .load(imgGalery)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageGalery);
*/


    }
    private void loadFavorite(final ImageView ivFavorite,final String idHome){
        if(mAuth.getCurrentUser()!=null){
            String idUser = mAuth.getCurrentUser().getUid();

            db.collection("users").document(idUser).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User user  = documentSnapshot.toObject(User.class);
                    if(user.getFavorites()!=null){
                        favorites = user.getFavorites();
                        Log.i("ADAPTER_FAV",idHome+" 1FAV");
                        DocumentReference documentReference = db.collection("homes").document(idHome);

                        if(user.getFavorites().contains(documentReference)){
                            ivFavorite.setBackgroundResource(R.drawable.ic_favo_on);

                        }else{
                            ivFavorite.setBackgroundResource(R.drawable.ic_favo_off);
                        }

                    }else{
                        Log.i("ADAPTER_FAV",idHome+" 0FAV");
                    }



                }


            });

        }
    }
    private void countComments(final String idHome, final TextView textView){

        // inicializar el número de comentarios.


        db.collection("comments")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        int cont = 0;
                        for (QueryDocumentSnapshot document : value) {

                            Comment comment = document.toObject(Comment.class);

                            if(comment.getId_homes().equals(idHome)){
                                cont++;
                            }

                        }
                        textView.setText(cont+" "+context.getResources().getString(R.string.house_nOpinion));

                    }
                });



    }

private void cargarValoration(ImageView[] array, Long val){

        if(val!=0){
            for (int i = 0; i<val; i++){

                array[i].setBackgroundResource(R.drawable.ic_leaf_on);
            }

            for (int i = val.intValue(); i<array.length; i++){

                array[i].setBackgroundResource(R.drawable.ic_leaf_off);
            }
        }


}
    private void cargarImagen(String nombre , final ImageView imageView){


        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(nombre);
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(context).load(task.getResult()).into(imageView);
                }
            }

        });


    }

    private List<DocumentReference> getFavoritesFromUser(){
        final List<DocumentReference>[] myFavorites = new List[]{null};
        String idUser = mAuth.getCurrentUser().getUid();
        db.collection("users").document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                         User user  = documentSnapshot.toObject(User.class);
                         if(user.getFavorites()!=null){
                             myFavorites[0] = user.getFavorites();
                         }


            }


        });

       return myFavorites[0];
    }

    @Override
    public int getItemCount() {
        return model.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        public int getIndex() {
            return index;
        }
        int index;
        TextView txtPlace, txtRental, txtPeople, txtPrice, txtNumOpinions;
        ImageView imageGalery, imageFavorite;
        ImageView iconTemp1,iconTemp2,iconTemp3,iconTemp4,iconTemp5;
        ImageView[] arrayValoration;
        Home home;

        // CONTRUCTOR DEL ViewHolder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            // Acción al pulsar un bloque del recyclerView que nos llevará a su detalle
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(context, "ID: " + model.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

                    Intent in = new Intent(view.getContext(), DetailHouseActivity.class);
                    in.putExtra("idHouse", model.get(getAdapterPosition()).getId());
                   if(destine!=null){
                       in.putExtra("destine", destine);
                   }
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Abrir actividad: DetailHouseActivity
                    view.getContext().startActivity(in);


                }
            });
            index = 0;
            firebaseStorage  = FirebaseStorage.getInstance();
            // Firebase
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            favorites   = new ArrayList<>();
            // Relacionar las variables con la parte gráfica
            txtPlace = itemView.findViewById(R.id.txtPlace);
            txtRental = itemView.findViewById(R.id.txtRental);
            txtPeople = itemView.findViewById(R.id.txtPeople);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtNumOpinions = itemView.findViewById(R.id.txtNumOpinions);
            imageGalery = itemView.findViewById(R.id.CardGaleryImage);
            iconTemp1 = itemView.findViewById(R.id.iconTemp);
            iconTemp2 = itemView.findViewById(R.id.iconTemp2);
            iconTemp3 = itemView.findViewById(R.id.iconTemp3);
            iconTemp4 = itemView.findViewById(R.id.iconTemp4);
            iconTemp5 = itemView.findViewById(R.id.iconTemp5);
             arrayValoration =new ImageView[] { iconTemp1,iconTemp2,iconTemp3,iconTemp4,iconTemp5};
            imageFavorite = itemView.findViewById(R.id.iconFavorite);

            imageFavorite.setBackgroundResource(R.drawable.ic_favo_off);
            if(mAuth.getCurrentUser()!=null){
                imageFavorite.setVisibility(View.VISIBLE);
            }else{
                imageFavorite.setVisibility(View.INVISIBLE);
            }

            imageFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        String id = model.get(getAdapterPosition()).getId();
                        saveFav(id);

                }
            });
            imageGalery.setOnTouchListener(new View.OnTouchListener() {
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
        }

        private void configSwipeImage(){
            if(index == home.getImages().size()){
                index = 0;
            }else if(index < 0){
                index = home.getImages().size()-1;
            }

            Log.i("TAG",home.getImages().get(index)+" tam");

            cargarImagen(home.getImages().get(index), imageGalery);
            Animation aniFade = AnimationUtils.loadAnimation(context,R.anim.animation_fade_in);
            imageGalery.startAnimation(aniFade);
        }


        private void saveFav(String idHome){
            // Recoger el ID del usuario logado
            DocumentReference documentReference = db.collection("homes").document(idHome);


            if(!favorites.contains(documentReference)){
                favorites.add(documentReference);
                imageFavorite.setBackgroundResource(R.drawable.ic_favo_on);

            }else{
                favorites.remove(documentReference);
                imageFavorite.setBackgroundResource(R.drawable.ic_favo_off);
            }

            db.collection("users").document(mAuth.getCurrentUser().getUid())
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
    }


}
