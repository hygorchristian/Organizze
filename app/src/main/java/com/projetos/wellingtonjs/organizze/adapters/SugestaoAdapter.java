package com.projetos.wellingtonjs.organizze.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projetos.wellingtonjs.organizze.R;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;
import com.projetos.wellingtonjs.organizze.model.Sugestao;
import com.projetos.wellingtonjs.organizze.model.Usuario;

import java.util.List;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoModel>{

    private Context context;
    private List<Sugestao> sugestoes;
    private List<Usuario> usuarios;


    public SugestaoAdapter(Context context, List<Sugestao> sugestoes, List<Usuario> usuarios) {
        this.sugestoes = sugestoes;
        this. usuarios = usuarios;
        context = context;
    }

    @Override
    public SugestaoModel onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sugestao, parent, false);
        return new SugestaoModel(view);
    }

    @Override
    public void onBindViewHolder(SugestaoModel holder, int position) {

        if(!sugestoes.isEmpty()){

            Sugestao sugestao = sugestoes.get(position);

            Log.i("Encontrado", sugestao.toString());

            holder.mNome.setText(sugestao.getNome());
            holder.mLikes.setText(String.valueOf(sugestao.getLikes()));
            holder.mDeslikes.setText(String.valueOf(sugestao.getDeslikes()));

        }

    }

    @Override
    public int getItemCount() {
        return sugestoes.size();
    }

    public class SugestaoModel extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNome;
        private TextView mLikes;
        private TextView mDeslikes;
        private ImageButton btnLike;
        private ImageButton btnDeslike;
        private Usuario usuario;
        private Sugestao sugestao;
        private DatabaseReference reference;


        public SugestaoModel(View itemView) {
            super(itemView);

            mNome = (TextView) itemView.findViewById(R.id.tv_nome_sugestao);
            mLikes = (TextView) itemView.findViewById(R.id.tv_likes);
            mDeslikes = (TextView) itemView.findViewById(R.id.tv_deslikes);
            btnLike = (ImageButton) itemView.findViewById(R.id.btn_like);
            btnDeslike = (ImageButton) itemView.findViewById(R.id.btn_deslike);

            btnLike.setOnClickListener(this);
            btnDeslike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.sugestao = sugestoes.get(getAdapterPosition());
            this.usuario = usuarios.get(0);

            if(v.getId() == btnLike.getId()){

                reference = ConfiguracaoFirebase.getVotos(usuario.getIdUsuario(), sugestao.getId());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            reference.setValue(true);
                            addLike();
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            }else if(v.getId() == btnDeslike.getId()){

                reference = ConfiguracaoFirebase.getVotos(usuario.getIdUsuario(), sugestao.getId());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            reference.setValue(false);
                            addDeslike();
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            }
        }

        private void addLike() {
            int likes = sugestao.getLikes() + 1;
            sugestao.setLikes(likes);
            ConfiguracaoFirebase.getDatabaseReference().child("sugestoes").child(sugestao.getId()).setValue(sugestao);
        }

        private void addDeslike() {
            int deslikes = sugestao.getDeslikes() + 1;
            sugestao.setDeslikes(deslikes);
            ConfiguracaoFirebase.getDatabaseReference().child("sugestoes").child(sugestao.getId()).setValue(sugestao);
        }
    }
}


