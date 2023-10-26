package com.mk.usuariosapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioApiClient {
//    private static final String BASE_URL = "https://minka123.000webhostapp.com/";
    private static final String BASE_URL = "http://10.0.2.2/api_usuarios_v1/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
