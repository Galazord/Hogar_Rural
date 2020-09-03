package com.example.hogar_rural;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hogar_rural.Model.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    //--> VARIABLES
    private LayoutInflater inflater;
    private ArrayList<Home> model;
    private Context context;
    FirebaseStorage firebaseStorage;



    //--> CONSTRCUTOR
    public Adapter(Context context, ArrayList<Home> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
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

        List<String> imgGallery = model.get(position).getImages();
        for(String url: imgGallery){
            cargarImagen(url,holder.imageGalery);
        }

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

    @Override
    public int getItemCount() {
        return model.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtPlace, txtRental, txtPeople, txtPrice, txtNumOpinions;
        ImageView imageGalery;

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

                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Abrir actividad: DetailHouseActivity
                    view.getContext().startActivity(in);


                }
            });

            firebaseStorage  = FirebaseStorage.getInstance();
            // Relacionar las variables con la parte gráfica
            txtPlace = itemView.findViewById(R.id.txtPlace);
            txtRental = itemView.findViewById(R.id.txtRental);
            txtPeople = itemView.findViewById(R.id.txtPeople);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtNumOpinions = itemView.findViewById(R.id.txtNumOpinions);
            imageGalery = itemView.findViewById(R.id.CardGaleryImage);


        }
    }
}
