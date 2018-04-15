package com.projetos.wellingtonjs.organizze.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.projetos.wellingtonjs.organizze.R;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;
import com.projetos.wellingtonjs.organizze.helper.Base64Custom;
import com.projetos.wellingtonjs.organizze.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome,campoInstrumento,campoEmail,campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //getSupportActionBar().setTitle("Lets Rock");

        campoNome = findViewById(R.id.editNome);
        campoInstrumento = findViewById(R.id.editInstrumento);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoNome = campoNome.getText().toString();
                String textoInstrumento = campoInstrumento.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoNome.isEmpty()){

                    if (!textoEmail.isEmpty()){

                        if (!textoSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setInstrumento(textoInstrumento);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            cadastrarUsuario();


                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha a senha", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha o e-mail", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(CadastroActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String idUsuario = Base64Custom.codoficarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();

                    finish();

                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Conta ja cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
