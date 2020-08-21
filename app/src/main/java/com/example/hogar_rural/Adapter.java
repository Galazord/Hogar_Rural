package com.example.hogar_rural;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Model.House;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    //--> VARIABLES
    private LayoutInflater inflater;
    private ArrayList<House> model;

    //--> CONSTRCUTOR
    public Adapter(Context context, ArrayList<House> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
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
        String typeRental = model.get(position).getRental();
        String imgGalery = model.get(position).getGalleryImg();
        String price = model.get(position).getPrice();
        String numPerson = model.get(position).getPersonMax();
        String numOpinion = model.get(position).getNumOpinion();

        holder.txtPlace.setText(namePlace);
        holder.txtRental.setText(typeRental);
        holder.txtPrice.setText(price);
        holder.txtPeople.setText(numPerson);
        holder.txtNumOpinions.setText(numOpinion);

        // Incluir la imagen en la Galería de imagenes del Holder
        Glide.with(holder.itemView.getContext())
                .load(imgGalery)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageGalery);



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

                    Intent in = new Intent(view.getContext(), DetailHouseActivity.class);
                    in.putExtra("idHouse", model.get(getAdapterPosition()).getIdHouse());
                    // Abrir actividad: DetailHouseActivity
                    view.getContext().startActivity(in);

                }
            });

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
