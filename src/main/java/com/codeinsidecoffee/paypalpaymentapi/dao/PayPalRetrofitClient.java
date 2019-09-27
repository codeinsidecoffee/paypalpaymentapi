package com.codeinsidecoffee.paypalpaymentapi.dao;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class PayPalRetrofitClient {

    public static PayPalRetroFitService getAccessToken(String baseURL){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PayPalRetroFitService retroFitService= retrofit.create(PayPalRetroFitService.class);


        return retroFitService;
    }


}
