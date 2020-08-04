package com.example.hogar_rural.Interface;

import com.example.hogar_rural.Model.House;
import com.example.hogar_rural.Model.HouseDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DbRetrofitApi {

    @GET("houses.php")
    Call<List<House>> getHouses();

    @GET("details_houses.php")
    Call<List<HouseDetail>> getDetailHouse(
            @Query("idHouse") int idHouse
    );

}
