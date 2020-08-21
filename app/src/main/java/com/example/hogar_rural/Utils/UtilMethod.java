package com.example.hogar_rural.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.hogar_rural.R;

public class UtilMethod {


    static public void showToast(TypeToast typeToast, Activity context, String msg){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = context.getLayoutInflater().inflate(R.layout.toast_custom, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(msg);

        switch (typeToast){
            case SUCCESS:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.green_500));
                break;
            case ERROR:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.red_600));
                break;
            case INFO:
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_error_outline);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.blue_500));
                break;
            default:
                break;
        }

        toast.setView(custom_view);
        toast.show();
    }
}
