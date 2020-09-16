package com.example.hogar_rural;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Model.Comment;
import com.example.hogar_rural.Utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder>  {

    //--> VARIABLES
    private LayoutInflater inflater;
    private  List<Comment> comments;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public List<DocumentReference> favorites;

    //--> CONSTRCUTOR
    public AdapterComment(Context context,  List<Comment> comments) {
        this.inflater = LayoutInflater.from(context);
        this.comments = comments;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_view_comments, parent, false);

        return new AdapterComment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComment.ViewHolder holder, int position) {

        // Obtener los datos del modelo y pasarlos a la vista
        // Aqui se ponen los componentes(titulo, fecha e imagen)
        String comment = comments.get(position).getComment();
        String user = comments.get(position).getUser();
        String user_name = comments.get(position).getName_user();
        Long valoration  = comments.get(position).getValoration();

        // Colocar datos en los campos de texto
        holder.txtComment.setText(comment);
        holder.textNameUser.setText(user_name);

        cargarImagen(user,holder.imageAvatar);
        cargarValoracion(valoration, holder.arrImgVal);



    }
    private void cargarImagen(String nombre , final ImageView imageView){

        String nombre_url = Constant.URL_GS_IMAGE_USER.concat(nombre);
        StorageReference gsReference = firebaseStorage.getReferenceFromUrl(nombre_url);
        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Glide.with(context).load(task.getResult()).into(imageView);
                }
            }

        });


    }

    private void cargarValoracion(Long valoracion, ImageView[] arr){

       for (int i = 0; i<valoracion; i++){
           arr[i].setBackgroundResource(R.drawable.ic_leaf_on);
       }
       for(int i = valoracion.intValue(); i<arr.length; i++){
           arr[i].setBackgroundResource(R.drawable.ic_leaf_off);
       }

    }


    @Override
    public int getItemCount() {
        return comments.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        public int getIndex() {
            return index;
        }
        int index;
        TextView txtComment, textNameUser;
        Long valoration;
        ImageView imageAvatar, imgVal1,imgVal2,imgVal3,imgVal4,imgVal5;
        ImageView [] arrImgVal;
        // CONTRUCTOR DEL ViewHolder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);


            index = 0;
            firebaseStorage  = FirebaseStorage.getInstance();
            // Firebase
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            favorites   = new ArrayList<>();
            // Relacionar las variables con la parte gráfica
            textNameUser = itemView.findViewById(R.id.tvComment_name);
            txtComment = itemView.findViewById(R.id.tvComment_txtBody);
            imageAvatar = itemView.findViewById(R.id.cvComment_avatar);
            imgVal1 = itemView.findViewById(R.id.iconTemp);
            imgVal2 = itemView.findViewById(R.id.iconTemp2);
            imgVal3 = itemView.findViewById(R.id.iconTemp3);
            imgVal4 = itemView.findViewById(R.id.iconTemp4);
            imgVal5 = itemView.findViewById(R.id.iconTemp5);
            arrImgVal = new ImageView[]{ imgVal1, imgVal2, imgVal3, imgVal4, imgVal5};



        }



    }


}