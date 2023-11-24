package com.mk.usuariosapi.controller;

import com.mk.usuariosapi.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioApiService {
    @GET("index.php")
    Call<List<Usuario>> getUsuarios();

    @GET("index.php/{id}")
    Call<Usuario> getUsuario(@Path("id") int id);

    @PUT("index.php/{id}")
    Call<Usuario> updateUsuario(@Path("id") int id, @Body Usuario usuario);

    @POST("index.php")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    @DELETE("index.php/{id}")
    Call<Void> deleteUsuario(@Path("id") int id);
}
