package com.mk.usuariosapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioDetallesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalles);

        int idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        Log.d("UsuarioDetallesActivity", "ID del usuario: " + idUsuario);

        UsuarioApiService apiService = UsuarioApiClient.getApiClient().create(UsuarioApiService.class);

        final Context context = this;

        Call<List<Usuario>> call = apiService.getUsuario(idUsuario);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    if (usuarios != null && !usuarios.isEmpty()) {
                        Usuario usuario = usuarios.get(0); // Obtiene el primer elemento de la lista

                        // Encuentra los TextInputEditText por su ID
                        TextInputEditText nombresEditText = findViewById(R.id.tietNombres);
                        TextInputEditText apellidosEditText = findViewById(R.id.tietApellidos);
                        TextInputEditText direccionEditText = findViewById(R.id.tietDireccion);
                        TextInputEditText correoEditText = findViewById(R.id.tietCorreo);
                        AutoCompleteTextView acGenero = findViewById(R.id.acGenero);
                        AutoCompleteTextView acEstado = findViewById(R.id.acEstado);

                        // Crea los ArrayAdapter utilizando los arrays de strings
                        /*ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(context,
                                R.array.genero, android.R.layout.simple_spinner_item);
                        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(context,
                                R.array.estado, android.R.layout.simple_spinner_item);

                        // Especifica el layout a utilizar cuando la lista de opciones aparece
                        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

                        // Crea una lista con las opciones para el menú desplegable
                        String[] itemsGenero = getResources().getStringArray(R.array.genero);
                        String[] itemsEstado = getResources().getStringArray(R.array.estado);

                        // Crea el ArrayAdapter utilizando el layout personalizado
                        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(context, R.layout.item_genero_list, itemsGenero);
                        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(context, R.layout.item_genero_list, itemsEstado);

                        // Aplica los ArrayAdapter a los AutoCompleteTextView
                        acGenero.setAdapter(adapterGenero);
                        acEstado.setAdapter(adapterEstado);

                        // Asigna los datos del usuario a los TextInputEditText
                        nombresEditText.setText(usuario.getNombre());
                        apellidosEditText.setText(usuario.getApellido());
                        direccionEditText.setText(usuario.getDireccion());
                        correoEditText.setText(usuario.getCorreo_electronico());
                        // Asigna los datos del usuario a los AutoCompleteTextView
                        /*acGenero.setText(adapterGenero.getItem(usuario.getGenero()), false);
                        acEstado.setText(adapterEstado.getItem(usuario.getEstado()), false);*/
                        // Asigna los datos del usuario a los AutoCompleteTextView
                        acGenero.setText(itemsGenero[usuario.getGenero()], false);
                        acEstado.setText(itemsEstado[usuario.getEstado()], false);
                    }
                } else {
                    Log.e("API ERROR", "Request not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e("API ERROR", t.getMessage());
            }
        });
    }
}