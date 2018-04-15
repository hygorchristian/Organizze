package com.projetos.wellingtonjs.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projetos.wellingtonjs.organizze.R;
import com.projetos.wellingtonjs.organizze.adapters.SugestaoAdapter;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;
import com.projetos.wellingtonjs.organizze.helper.Base64Custom;
import com.projetos.wellingtonjs.organizze.model.Sugestao;
import com.projetos.wellingtonjs.organizze.model.Usuario;
import java.util.ArrayList;
import java.util.List;



public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private TextView textoSaudacao;
    private Button botaoAdmin;
    private RecyclerView recyclerLista;
    private SugestaoAdapter sugestaoAdapter;
    private List<Sugestao> sugestoes;
    private List<Usuario> usuarios;

    String emailUsuario = autenticacao.getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Titulo");
        setSupportActionBar(toolbar);
        recuperarResumo();

        textoSaudacao = findViewById(R.id.textSaudacao);
        botaoAdmin = findViewById(R.id.btAdmin);
        recyclerLista = (RecyclerView) findViewById(R.id.recycler_view);

        if (emailUsuario.equals("admin@letsrock.com")){
            botaoAdmin.setVisibility(View.VISIBLE);
        }else{
            botaoAdmin.setVisibility(View.GONE);
        }

        setUsuario();
        setSugestoes();
        setRecyclerView();
    }

    private void setUsuario() {
        usuarios = new ArrayList<>();
        String id = Base64Custom.codoficarBase64(emailUsuario);
        DatabaseReference reference = ConfiguracaoFirebase.getUsuario(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                usuarios.add(usuario);
                sugestaoAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void setSugestoes() {
        sugestoes = new ArrayList<>();
        firebaseRef = ConfiguracaoFirebase.getSugestoes();
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sugestoes.clear();

                for (DataSnapshot dado : dataSnapshot.getChildren()) {
                    Sugestao sugestao = dado.getValue(Sugestao.class);
                    if(sugestao.getStatus() == 0){
                        sugestoes.add(sugestao);
                    }
                }
                sugestaoAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setRecyclerView() {
        recyclerLista.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerLista.setLayoutManager(linearLayoutManager);

        sugestaoAdapter = new SugestaoAdapter(this, sugestoes, usuarios);
        recyclerLista.setAdapter(sugestaoAdapter);


    }

    public void recuperarResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codoficarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                textoSaudacao.setText("Olá, " + usuario.getNome() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                //autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void btCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }


    public void sugerir(View view){
        startActivity(new Intent(this, SugerirActivity.class));
    }


    public void setListEnsaio(View view){
        startActivity(new Intent(this, SetListEnsaioActivity.class));
    }

}
