package ga.josepolanco.dondeestaeldelito02.Noticia;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ga.josepolanco.dondeestaeldelito02.Clases.Denuncias;
import ga.josepolanco.dondeestaeldelito02.R;

public class DenunciasAdaptador extends RecyclerView.Adapter<DenunciasAdaptador.DenunciasViewHolder>{
    List<Denuncias> denuncias;

    public DenunciasAdaptador(List<Denuncias> denuncias) {
        this.denuncias = denuncias;
    }

    public static class DenunciasViewHolder extends RecyclerView.ViewHolder{
        TextView denuncia_nombre, denuncia_ubicacion, denuncia_hora,denuncia_descripcion,noticia_denuncias_tipo;
        ImageView noticia_denuncias_imagen, noticia_denuncias_foto_perfil;

        public DenunciasViewHolder(View itemView) {
            super(itemView);
            denuncia_nombre = itemView.findViewById(R.id.noticia_denuncias_nombre);
            denuncia_ubicacion = itemView.findViewById(R.id.noticia_denuncias_ubicacion);
            denuncia_hora = itemView.findViewById(R.id.noticia_denuncias_fecha);
            noticia_denuncias_tipo = itemView.findViewById(R.id.noticia_denuncias_tipo);
            denuncia_descripcion = itemView.findViewById(R.id.noticia_denuncias_descripcion);
            noticia_denuncias_imagen = itemView.findViewById(R.id.noticia_denuncias_imagen);
            noticia_denuncias_foto_perfil = itemView.findViewById(R.id.noticia_denuncias_foto_perfil);
        }
    }

    @NonNull
    @Override
    public DenunciasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_denuncias, viewGroup, false);
        DenunciasViewHolder holder = new DenunciasViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DenunciasViewHolder denunciasViewHolder, int i) {
        Denuncias Denuncias = denuncias.get(i);
        denunciasViewHolder.denuncia_nombre.setText(Denuncias.getNombre());
        denunciasViewHolder.denuncia_ubicacion.setText(Denuncias.getUbicacion());
        denunciasViewHolder.denuncia_hora.setText(Denuncias.getFecha());
        denunciasViewHolder.noticia_denuncias_tipo.setText(Denuncias.getTipo_delito());
        denunciasViewHolder.denuncia_descripcion.setText(Denuncias.getDescripcion());
        Picasso.get().load(Denuncias.getUrlImagen()).resize(500,700).centerInside().into(denunciasViewHolder.noticia_denuncias_imagen);
        Picasso.get().load(Denuncias.getUrlFotoPerfil()).resize(100,100).into(denunciasViewHolder.noticia_denuncias_foto_perfil);
//        if (Denuncias.getUrlImagen()!=null){
//            Picasso.get().load(Denuncias.getUrlImagen()).into(denunciasViewHolder.noticia_denuncias_imagen);
//        }else{
//            //Mustra imagen por defecto.
//        }
    }

    @Override
    public int getItemCount() {
        return denuncias.size();
    }
}
