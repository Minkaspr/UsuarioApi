package com.mk.usuariosapi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mk.usuariosapi.R;
import com.mk.usuariosapi.controller.UsuarioApiClient;
import com.mk.usuariosapi.controller.UsuarioApiService;
import com.mk.usuariosapi.model.Usuario;
import com.mk.usuariosapi.ui.adapter.UsuarioAdapter;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosGetActivity extends AppCompatActivity {
    private RecyclerView rvUsuarios;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_get);

        FloatingActionButton btnPost = findViewById(R.id.btnPost);
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
                        adapter = new UsuarioAdapter(UsuariosGetActivity.this, usuarios);
                        rvUsuarios.setAdapter(adapter);

                        // Eliminar un Usuario
                        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                            @Override
                            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                                // Obtiene la posición del elemento deslizado
                                int position = viewHolder.getBindingAdapterPosition();
                                // Guarda el usuario que se va a eliminar
                                Usuario usuarioEliminado = usuarios.get(position);

                                // Muestra un Snackbar con la opción de deshacer
                                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Usuario eliminado", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Deshacer", view -> {
                                    // Si el usuario selecciona "Deshacer", agrega de nuevo el usuario eliminado
                                    usuarios.add(position, usuarioEliminado);
                                    adapter.notifyItemInserted(position);
                                });

                                snackbar.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                            // Si el Snackbar se descarta y el usuario no seleccionó "Deshacer", elimina el usuario de la base de datos
                                            Call<Void> callDelete = apiService.deleteUsuario(usuarioEliminado.getId());
                                            callDelete.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                                    if (response.isSuccessful()) {
                                                        // Remueve el elemento de tu conjunto de datos
                                                        usuarios.remove(position);
                                                        adapter.notifyItemRemoved(position);
                                                    } else {
                                                        Log.e("API ERROR", "Request not successful");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                                    String errorMessage = (t.getMessage() != null) ? t.getMessage() : "Unknown error";
                                                    Log.e("API ERROR", errorMessage);
                                                }
                                            });
                                        }
                                    }
                                });
                                snackbar.show();
                            }
                            @Override
                            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                        .addBackgroundColor(ContextCompat.getColor(UsuariosGetActivity.this, R.color.red))
                                        .addActionIcon(R.drawable.ic_delete)
                                        .create()
                                        .decorate();
                                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            }
                        };

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                        itemTouchHelper.attachToRecyclerView(rvUsuarios);
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

        btnPost.setOnClickListener(v->{
            Intent intent = new Intent(UsuariosGetActivity.this, UsuarioPostActivity.class);
            startActivity(intent);
        });
    }
}