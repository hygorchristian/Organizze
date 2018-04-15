package com.projetos.wellingtonjs.organizze.activity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.projetos.wellingtonjs.organizze.R;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;
import com.projetos.wellingtonjs.organizze.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText cemail;
    private EditText csenha;
    private Button botaoentrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cemail = findViewById(R.id.editEmail);
        csenha = findViewById(R.id.editSenha);
        botaoentrar = findViewById(R.id.buttonEntrar);

        botaoentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoEmail = cemail.getText().toString();
                String textoSenha = csenha.getText().toString();

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this, "Digite sua senha", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Digite seu email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    abrirTelaprincipal();

                }else{

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não cadastrado";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e senha nao correspondem";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_LONG).show();

                };
            }
        });

    }

    public void abrirTelaprincipal(){
        startActivity(new Intent(this,PrincipalActivity.class));
        finish();
    }



}
