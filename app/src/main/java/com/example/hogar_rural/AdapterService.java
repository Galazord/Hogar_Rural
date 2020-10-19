package com.example.hogar_rural;

import android.content.Context;
import android.content.Intent;
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
import com.example.hogar_rural.Model.Service;
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

public class AdapterService extends RecyclerView.Adapter<AdapterService.ViewHolder>  {

    //--> VARIABLES
    private LayoutInflater inflater;
    private  List<Service> services;
    private Context context;
    private List<String> selectedServices;

    //--> CONSTRCUTOR
    public AdapterService(Context context, List<Service> services) {
        this.inflater = LayoutInflater.from(context);
        this.services = services;
        this.context = context;
        this.selectedServices = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_view_service, parent, false);

        return new AdapterService.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterService.ViewHolder holder, int position) {


        String name = services.get(position).getName();
        int resoruceIcon = services.get(position).getIcon();
        holder.txtName.setText(name);
        holder.imageAvatar.setBackgroundResource(resoruceIcon);


    }




    @Override
    public int getItemCount() {
        return services.size();
    }

    public List<String> getSelectedServices(){
        return this.selectedServices;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public int getIndex() {
            return index;
        }
        int index;
        TextView txtName;
        ImageView imageAvatar;
        ImageView imageViewSelected;
        // CONTRUCTOR DEL ViewHolder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service service = services.get(getAdapterPosition());
                    setCheckedService(service.getName());



                }
            });
            index = 0;

            imageAvatar = itemView.findViewById(R.id.cvServiceIcon);
            imageViewSelected = itemView.findViewById(R.id.imageViewSelected);
            txtName = itemView.findViewById(R.id.cvServiceName);

        }

        private void setCheckedService(String serviceName){

                if(selectedServices.contains(serviceName)){
                    selectedServices.remove(serviceName);
                    imageViewSelected.setVisibility(View.INVISIBLE);
                }else{
                    selectedServices.add(serviceName);
                    imageViewSelected.setVisibility(View.VISIBLE);
                }

        }



    }


}