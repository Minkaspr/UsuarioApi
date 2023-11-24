package com.mk.usuariosapi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.usuariosapi.R;
import com.mk.usuariosapi.model.Usuario;
import com.mk.usuariosapi.ui.UsuarioGetPutActivity;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{
    private final List<Usuario> usuarioList;
    private final Context context;
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        public TextView datosPersonales, correo;
        private final List<Usuario> usuarioList;
        public UsuarioViewHolder(View itemView, List<Usuario> usuarioList) {
            super(itemView);
            this.usuarioList = usuarioList;
            datosPersonales = itemView.findViewById(R.id.tvNombreApellido);
            correo = itemView.findViewById(R.id.tvEmail);

            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Usuario usuarioSeleccionado = usuarioList.get(position);
                    Log.d("Usuario seleccionado", "ID: " + usuarioSeleccionado.getId());
                    Intent intent = new Intent(v.getContext(), UsuarioGetPutActivity.class);
                    intent.putExtra("ID_USUARIO", usuarioSeleccionado.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public UsuarioAdapter(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView, usuarioList);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarioList.get(position);
        String nombreApellido = context.getResources().getString(R.string.nombre_apellido, usuario.getNombre(), usuario.getApellido());
        holder.datosPersonales.setText(nombreApellido);
        holder.correo.setText(usuario.getCorreo_electronico());
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }
}
