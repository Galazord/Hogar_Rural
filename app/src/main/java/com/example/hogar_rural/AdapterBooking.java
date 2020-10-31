package com.example.hogar_rural;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Model.Booking;
import com.example.hogar_rural.Model.User;
import com.example.hogar_rural.Utils.Constant;
import com.example.hogar_rural.Utils.UtilMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder>  {

    //--> VARIABLES
    private LayoutInflater inflater;
    private  List<Booking> bookings;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public List<DocumentReference> favorites;

    //--> CONSTRCUTOR
    public AdapterBooking(Context context, List<Booking> bookings) {
        this.inflater = LayoutInflater.from(context);
        this.bookings = bookings;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_view_bookings, parent, false);

        return new AdapterBooking.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.ViewHolder holder, int position) {

        // Obtener los datos del modelo y pasarlos a la vista
        // Aqui se ponen los componentes(titulo, fecha e imagen)
        DocumentReference user = bookings.get(position).getUser();
        Timestamp dateReserd = bookings.get(position).getDate_reserved();
        holder.txtBooking.setText(UtilMethod.getDate(dateReserd.toDate()));
        cargarUserInfo(user,holder.imageAvatar,holder.textNameUser);





    }
    private void cargarUserInfo(DocumentReference user,final ImageView imageView, final TextView name){

                    user.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            User user  = documentSnapshot.toObject(User.class);
                            name.setText(user.getName());

                            StorageReference gsReference = firebaseStorage.getReferenceFromUrl(user.getImage());
                            gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()){
                                        Glide.with(context).load(task.getResult()).into(imageView);
                                    }
                                }

                            });

                        }


                    });





    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        public int getIndex() {
            return index;
        }
        int index;
        TextView txtBooking, textNameUser;
        ImageView imageAvatar;
        // CONTRUCTOR DEL ViewHolder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            index = 0;
            firebaseStorage  = FirebaseStorage.getInstance();
            // Firebase
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            // Relacionar las variables con la parte gráfica
            textNameUser = itemView.findViewById(R.id.tvBooking_name);
            txtBooking = itemView.findViewById(R.id.tvBooking_dates);
            imageAvatar = itemView.findViewById(R.id.cvBooking_avatar);

        }

    }


}