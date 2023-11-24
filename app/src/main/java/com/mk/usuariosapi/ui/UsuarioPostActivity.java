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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioPostActivity extends AppCompatActivity {

    private TextInputEditText nombresEditText;
    private TextInputEditText apellidosEditText;
    private TextInputEditText direccionEditText;
    private TextInputEditText correoEditText;
    private AutoCompleteTextView acGenero;
    private AutoCompleteTextView acEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_post);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);

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

        UsuarioApiService apiService = UsuarioApiClient.getApiClient().create(UsuarioApiService.class);

        final Context context = this;

        btnRegistrar.setOnClickListener(v->{
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
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombres);
            nuevoUsuario.setApellido(apellidos);
            nuevoUsuario.setDireccion(direccion);
            nuevoUsuario.setCorreo_electronico(correo);
            nuevoUsuario.setGenero(genero);
            nuevoUsuario.setEstado(estado);

            // Obtiene la fecha y la hora actuales
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Para API 26 y superior
                LocalDate fechaActual = LocalDate.now();
                LocalTime horaActual = LocalTime.now();
                nuevoUsuario.setFecha_nacimiento(fechaActual.toString());
                nuevoUsuario.setHora_registro(horaActual.toString());
            } else {
                // Para API 25 y inferior
                Date fechaHoraActual = new Date();
                /*SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");*/
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.US);
                nuevoUsuario.setFecha_nacimiento(formatoFecha.format(fechaHoraActual));
                nuevoUsuario.setHora_registro(formatoHora.format(fechaHoraActual));
            }

            // Llama al método createUsuario de la API
            Call<Usuario> callCreate = apiService.createUsuario(nuevoUsuario);
            callCreate.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        finish();  // Cierra la actividad actual y vuelve a MainActivity <> UsuariosGetActivity
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
