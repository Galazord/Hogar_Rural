package com.example.hogar_rural.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.firebase.Timestamp;
import com.example.hogar_rural.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UtilMethod {

    // TOAST DESIGN
    static public void showToast(TypeToast typeToast, Activity context, String msg){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = context.getLayoutInflater().inflate(R.layout.toast_custom, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(msg);

        switch (typeToast){
            case SUCCESS:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_icon_toast);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.toast_green));
                break;
            case ERROR:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_icon_toast);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.toast_red));
                break;
            case INFO:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_icon_toast);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.toast_blue));
                break;
            case WARNNING:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_icon_toast);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.toast_yellow));
                break;
            default:
                break;
        }

        toast.setView(custom_view);
        toast.show();
    }


    // Generar una clave ID aleatoria.
    static public String getUIID(){

        return UUID.randomUUID().toString();
    }

    // Recoger la opción seleccionada de los radioButton del tipo de vivienda (Íntegro/ habitaciones)
    static public Long getNameSelectedRadioButton(RadioGroup radioGroup, View view, Context context){

        int selectedId = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton)view.findViewById(selectedId);

        if(radioButton.getText().equals(context.getString(R.string.houseUp_complete))){
           return  1L;
        }else if(radioButton.getText().equals(context.getString(R.string.houseUp_rooms))){
            return  2L;
        }
        return -1L;
    }

    // Convertir el formato fecha en Timestamp
    public static Timestamp  getTimestamp(String date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate);
        } catch(Exception e) { //this generic but you can control another types of exception
            return null;
        }
    }

    // Formato fecha de hoy
    public static String getDate(Date date){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);

    }


    public static List<String> checkServicesList(List<String> services, String new_service){

        if (services.contains(new_service)) {
            services.remove(new_service);
        }else{
            services.add(new_service);
        }

        return services;
    }
    
}
