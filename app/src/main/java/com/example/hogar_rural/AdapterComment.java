package com.example.hogar_rural;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hogar_rural.Model.Home;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AdapterComment {

    //--> VARIABLES
    private LayoutInflater inflater;
    private ArrayList<Home> model;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private List<String> imgGallery;

    //--> CONSTRCUTOR
    public AdapterComment(Context context, ArrayList<Home> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
    }


}
