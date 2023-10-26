package com.mk.usuariosapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioApiService {
    @GET("index.php")
    Call<List<Usuario>> getUsuarios();

    @GET("index.php/{id}")
    Call<List<Usuario>> getUsuario(@Path("id") int id);
}
