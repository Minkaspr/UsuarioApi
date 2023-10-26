package com.mk.usuariosapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));

        UsuarioApiService apiService = UsuarioApiClient.getApiClient().create(UsuarioApiService.class);

        Call<List<Usuario>> call = apiService.getUsuarios();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    // Comprueba si la lista de usuarios es null antes de llamar a toString()
                    if (usuarios != null) {
                        // Registra la representación en cadena de la lista de usuarios
                        Log.d("RESPUESTA API", usuarios.toString());
                        adapter = new UsuarioAdapter(MainActivity.this, usuarios);
                        rvUsuarios.setAdapter(adapter);
                    } else {
                        Log.e("ERROR API", "Usuarios es null");
                    }
                } else {
                    Log.e("ERROR API", "La solicitud no fue exitosa");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                // Manejo de errores
            }
        });
    }
}