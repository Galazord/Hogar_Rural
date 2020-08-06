package com.example.hogar_rural;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hogar_rural.Interface.DbRetrofitApi;
import com.example.hogar_rural.Model.HouseDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailHouseActivity extends AppCompatActivity {

    // VARIABLES
    private TextView DetailPlace, DetailRental, DetailPeople, DetailPrice, DetailNumOpinions, DetailValorations, DetailTextMultiLine1, DetailTextMultiLine2, DetailTextMultiLine3, DetailTextMultiLine4;
    private ImageView CardDetailImage;
    private DbRetrofitApi dbRetrofitApi;

    // VARIABLES FIJAS
    private final String URL_PHP = "https://hogarruralapp.000webhostapp.com/hogarRural/php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        // Relaccionar las variables con la parte gráfica
        DetailPlace = (TextView) findViewById(R.id.DetailPlace);
        DetailRental = (TextView) findViewById(R.id.DetailRental);
        DetailPeople = (TextView) findViewById(R.id.DetailPeople);
        DetailPrice = (TextView) findViewById(R.id.DetailPrice);
        DetailNumOpinions = (TextView) findViewById(R.id.DetailNumOpinions);
        DetailValorations = (TextView) findViewById(R.id.DetailValorations);
        DetailTextMultiLine1 = (TextView) findViewById(R.id.DetailTextMultiLine1);
        DetailTextMultiLine2 = (TextView) findViewById(R.id.DetailTextMultiLine2);
        DetailTextMultiLine3 = (TextView) findViewById(R.id.DetailTextMultiLine3);
        DetailTextMultiLine4 = (TextView) findViewById(R.id.DetailTextMultiLine4);
        CardDetailImage = (ImageView) findViewById(R.id.CardDetailImage);

        // Recibir los parámetros del intent procedente de Adapter.java
        Intent i = getIntent();
        String idHouse = i.getStringExtra("idHouse");
        //Toast.makeText(this, "ID: " + idHouse, Toast.LENGTH_SHORT).show();

        // RecyclerView: instanciar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_PHP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbRetrofitApi = retrofit.create(DbRetrofitApi.class);

        // Mostrar la información de la base de datos en la vista final
        showDetailList(idHouse);

    }

    private void showDetailList(String idHouse) {

        Call<List<HouseDetail>> call = dbRetrofitApi.getDetailHouse(Integer.parseInt(idHouse));
        call.enqueue(new Callback<List<HouseDetail>>() {
            @Override
            public void onResponse(Call<List<HouseDetail>> call, Response<List<HouseDetail>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(DetailHouseActivity.this, "Error de código: DetailHouseActivity: " + response.code(), Toast.LENGTH_SHORT).show();

                }

                List<HouseDetail> houseDetails = response.body();
                // Establecer valores a la lista
                for(HouseDetail detail: houseDetails){
                    DetailPlace.setText(detail.getName());
                    DetailRental.setText(detail.getRental());
                    DetailPeople.setText(detail.getPersonMax() + " personas");
                    DetailPrice.setText(detail.getPrice() + "€");
                    DetailNumOpinions.setText(detail.getNumOpinion() + " opiniones");
                    DetailTextMultiLine1.setText(detail.getFeatures());
                    DetailTextMultiLine2.setText(detail.getActivities());
                    DetailTextMultiLine3.setText(detail.getPlacesinterest());
                    DetailTextMultiLine4.setText(detail.getServices());

                    // Incluir la imagen en la Galería de imagenes del Holder
                    String imageInsert = detail.getGalleryImg();
                    Glide.with(getApplicationContext())
                            .load(imageInsert)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .into(CardDetailImage);

                }

            }

            @Override
            public void onFailure(Call<List<HouseDetail>> call, Throwable t) {

                Toast.makeText(DetailHouseActivity.this, "Fallo en la conexión: DetailHouseActivity: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}