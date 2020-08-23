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
}
