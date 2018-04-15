package com.projetos.wellingtonjs.organizze.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.projetos.wellingtonjs.organizze.R;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;
import com.projetos.wellingtonjs.organizze.model.Sugestao;

public class SugerirActivity extends AppCompatActivity {

    private EditText nome;
    private EditText link;
    private EditText status;
    private Button botaosugerir;

    private Sugestao sugerir;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir);

        nome = (EditText) findViewById(R.id.add_nome);
        link = findViewById(R.id.add_link);
        status = findViewById(R.id.add_status);

        botaosugerir = (Button) findViewById(R.id.btn_cadastrar);


        botaosugerir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().isEmpty()){

                    String sNome = nome.getText().toString();
                    String sLink = link.getText().toString();
                    int iStatus = Integer.parseInt(status.getText().toString());

                    Sugestao sugestao = new Sugestao(sNome, sLink, iStatus);
                    sugestao.salvar();

                    finish();

                }else{
                    Toast.makeText(SugerirActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });






    }
}
