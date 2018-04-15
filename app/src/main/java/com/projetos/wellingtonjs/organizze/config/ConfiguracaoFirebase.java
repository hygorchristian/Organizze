package com.projetos.wellingtonjs.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    public static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){

        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }


    public static DatabaseReference getFirebaseDatabase(){
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static DatabaseReference getSugestoes(){
        return getDatabaseReference().child("sugestoes");
    }


    public static DatabaseReference getUsuario(String id) {
        return getDatabaseReference().child("usuarios").child(id);
    }

    public static DatabaseReference getVotos(String idUsuario, String id) {
        return getDatabaseReference().child("votos").child(idUsuario).child(id);
    }
}
