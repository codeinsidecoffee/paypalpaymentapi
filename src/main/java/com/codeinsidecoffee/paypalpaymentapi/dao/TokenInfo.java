package com.codeinsidecoffee.paypalpaymentapi.dao;

import android.os.AsyncTask;
import android.util.Log;

import com.codeinsidecoffee.paypalpaymentapi.model.TokenBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenInfo extends AsyncTask<Void,Void,Void> {
    String baseURL;
    String paypalClientID,paypalSecretID;
    String grant_type;
    OnTokenInfoListener listener;
    private String TAG="TokenInfo";


    public TokenInfo(String baseURL, String paypalClientID, String paypalSecretID, String grant_type, OnTokenInfoListener listener) {
        this.baseURL=baseURL;
        this.paypalClientID = paypalClientID;
        this.paypalSecretID = paypalSecretID;
        this.grant_type = grant_type;
        this.listener = listener;
    }

    public  interface OnTokenInfoListener{
        void onTokenInfo(TokenBean tokenBean);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        PayPalRetroFitService retroFitService= PayPalRetrofitClient.getAccessToken(baseURL);
        String authorization = Credentials.basic(paypalClientID, paypalSecretID);
        Call<TokenBean> call=retroFitService.tokenInfo(authorization,grant_type);
        call.enqueue(new Callback<TokenBean>() {
            @Override
            public void onResponse(Call<TokenBean> call, Response<TokenBean> response) {
                if(response.isSuccessful()){
                    TokenBean tokenBean=response.body();
                    listener.onTokenInfo(tokenBean);
                }else{
                    Log.e(TAG, "onResponse: Not Found");
                    if (response.code() == 400) {
                        if(!response.isSuccessful()) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String userMessage = jsonObject.getString("error");
                                String internalMessage = jsonObject.getString("error_description");

                                Log.e(TAG, "onResponse: \n erro: "+userMessage +"\n error_description: "+internalMessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenBean> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });
        return null;
    }
}
