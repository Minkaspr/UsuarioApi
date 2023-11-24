package com.mk.usuariosapi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mk.usuariosapi.R;
import com.mk.usuariosapi.controller.UsuarioApiClient;
import com.mk.usuariosapi.controller.UsuarioApiService;
import com.mk.usuariosapi.model.Usuario;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioGetPutActivity extends AppCompatActivity {

    private TextInputEditText nombresEditText;
    private TextInputEditText apellidosEditText;
    private TextInputEditText direccionEditText;
    private TextInputEditText correoEditText;
    private AutoCompleteTextView acGenero;
    private AutoCompleteTextView acEstado;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_get_put);

        Button btnActualizar = findViewById(R.id.btnActualizar);
        // Encuentra los TextInputEditText por su ID
        nombresEditText = findViewById(R.id.tietNombres);
        apellidosEditText = findViewById(R.id.tietApellidos);
        direccionEditText = findViewById(R.id.tietDireccion);
        correoEditText = findViewById(R.id.tietCorreo);
        acGenero = findViewById(R.id.acGenero);
        acEstado = findViewById(R.id.acEstado);

        // Crea una lista con las opciones para el menú desplegable
        String[] itemsGenero = getResources().getStringArray(R.array.genero);
        String[] itemsEstado = getResources().getStringArray(R.array.estado);

        // Crea el ArrayAdapter utilizando el layout personalizado
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(this, R.layout.item_genero_list, itemsGenero);
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(this, R.layout.item_genero_list, itemsEstado);

        // Aplica los ArrayAdapter a los AutoCompleteTextView
        acGenero.setAdapter(adapterGenero);
        acEstado.setAdapter(adapterEstado);

        int idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        Log.d("UsuarioDetallesActivity", "ID del usuario: " + idUsuario);

        UsuarioApiService apiService = UsuarioApiClient.getApiClient().create(UsuarioApiService.class);

        final Context context = this;

        Call<Usuario> call = apiService.getUsuario(idUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    if (usuario != null) {
                        // Asigna los datos del usuario a los TextInputEditText
                        nombresEditText.setText(usuario.getNombre());
                        apellidosEditText.setText(usuario.getApellido());
                        direccionEditText.setText(usuario.getDireccion());
                        correoEditText.setText(usuario.getCorreo_electronico());
                        // Asigna los datos del usuario a los AutoCompleteTextView
                        if (usuario.getGenero() != -1) {
                            acGenero.setText(itemsGenero[usuario.getGenero()], false);
                        }
                        if (usuario.getEstado() != -1) {
                            acEstado.setText(itemsEstado[usuario.getEstado()], false);
                        }
                    } else {
                        Log.e("API ERROR", "Usuario es null");
                    }
                } else {
                    Log.e("API ERROR", "Request not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                String errorMessage = (t.getMessage() != null) ? t.getMessage() : "Unknown error";
                Log.e("API ERROR", errorMessage);
            }
        });

        btnActualizar.setOnClickListener(v->{
            // Obtiene los datos del usuario de los TextInputEditText
            String nombres = (nombresEditText.getText() != null) ? nombresEditText.getText().toString() : "";
            String apellidos = (apellidosEditText.getText() != null) ? apellidosEditText.getText().toString() : "";
            String direccion = (direccionEditText.getText() != null) ? direccionEditText.getText().toString() : "";
            String correo = (correoEditText.getText() != null) ? correoEditText.getText().toString() : "";
            String generoSeleccionado = acGenero.getText().toString();
            int genero = Arrays.asList(itemsGenero).indexOf(generoSeleccionado);
            String estadoSeleccionado = acEstado.getText().toString();
            int estado = Arrays.asList(itemsEstado).indexOf(estadoSeleccionado);

            // Crea un nuevo objeto Usuario con los datos obtenidos
            Usuario usuarioActualizado = new Usuario();
            usuarioActualizado.setNombre(nombres);
            usuarioActualizado.setApellido(apellidos);
            usuarioActualizado.setDireccion(direccion);
            usuarioActualizado.setCorreo_electronico(correo);
            usuarioActualizado.setGenero(genero);
            usuarioActualizado.setEstado(estado);

            // Llama al método updateUsuario de la API
            Call<Usuario> callUpdate = apiService.updateUsuario(idUsuario, usuarioActualizado);
            callUpdate.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
                        finish();  // Cierra la actividad actual y vuelve a MainActivity
                    } else {
                        Log.e("API ERROR", "Request not successful");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                    String errorMessage = (t.getMessage() != null) ? t.getMessage() : "Unknown error";
                    Log.e("API ERROR", errorMessage);
                }
            });
        });
    }
}