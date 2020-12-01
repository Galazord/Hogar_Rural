package com.example.hogar_rural;

import android.content.Context;
import android.content.Intent;
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
    private  List<Service> servicesSel;
    private Context context;
    private List<String> selectedServices;
    private boolean all =false;

    //--> CONSTRCUTOR
    public AdapterService(Context context, List<Service> services) {
        this.inflater = LayoutInflater.from(context);
        this.services = services;
        this.context = context;
        this.selectedServices = new ArrayList<>();
    }
    public AdapterService(Context context, List<Service> services, boolean all, List<Service> servicesSel) {
        this.inflater = LayoutInflater.from(context);
        this.services = services;
        this.context = context;
        this.all = all;
        this.servicesSel = servicesSel;
        loadSelectedService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_view_service, parent, false);

        return new AdapterService.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterService.ViewHolder holder, int position) {

         if(all){
             String name = services.get(position).getName();
             holder.txtName.setText(name);

             if(searchService(services.get(position))){
                 int resoruceIcon = services.get(position).getIcon_on();
                 holder.imageAvatar.setBackgroundResource(resoruceIcon);
             }else{
                 int resoruceIcon = services.get(position).getIcon();
                 holder.imageAvatar.setBackgroundResource(resoruceIcon);
             }
         }else{
             String name = services.get(position).getName();
             int resoruceIcon = services.get(position).getIcon();
             holder.txtName.setText(name);
             holder.imageAvatar.setBackgroundResource(resoruceIcon);
         }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public List<String> getSelectedServices(){
        return this.selectedServices;
    }

    private void loadSelectedService(){
        this.selectedServices = new ArrayList<>();

        for (Service s:servicesSel
        ) {
            this.selectedServices.add(s.getName());
        }
    }
    private boolean searchService(Service nameServ ){
        for (Service s:servicesSel
             ) {
            if(s.getName().equals(nameServ.getName())){
                return true;
            }
        }
        return false;
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
                    setCheckedService(service.getName(),service.getIcon_on(),service.getIcon());
                }
            });
            index = 0;

            imageAvatar = itemView.findViewById(R.id.cvServiceIcon);
            imageViewSelected = itemView.findViewById(R.id.imageViewSelected);
            txtName = itemView.findViewById(R.id.cvServiceName);

        }

        private void setCheckedService(String serviceName, int iconOn, int iconOff){

                    if(selectedServices.contains(serviceName)){
                        selectedServices.remove(serviceName);
                        imageAvatar.setBackgroundResource(iconOff);
                    }else{
                        selectedServices.add(serviceName);

                        imageAvatar.setBackgroundResource(iconOn);
                    }
            Log.i("service",  selectedServices.size()+"");
        }

    }

}