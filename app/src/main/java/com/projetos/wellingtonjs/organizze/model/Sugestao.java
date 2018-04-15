package com.projetos.wellingtonjs.organizze.model;


import com.google.firebase.database.DatabaseReference;
import com.projetos.wellingtonjs.organizze.config.ConfiguracaoFirebase;

public class Sugestao {

    public String nome;
    public String link;
    public int status;
    public int likes;
    public int deslikes;
    public String id;

    public Sugestao(String nome, String link, int status){
        this.nome = nome;
        this.link = link;
        this.status = status;
        this.likes = 0;
        this.deslikes = 0;
    }

    public Sugestao() {
    }

    public Sugestao(String nome, String link, int status, int likes, int deslikes, String id) {
        this.nome = nome;
        this.link = link;
        this.status = status;
        this.likes = likes;
        this.deslikes = deslikes;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDeslikes() {
        return deslikes;
    }

    public void setDeslikes(int deslikes) {
        this.deslikes = deslikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Sugestao{" +
                "nome='" + nome + '\'' +
                ", link='" + link + '\'' +
                ", status=" + status +
                ", likes=" + likes +
                ", deslikes=" + deslikes +
                ", id='" + id + '\'' +
                '}';
    }

    public void salvar(){
        DatabaseReference reference = ConfiguracaoFirebase.getSugestoes();
        reference.push();
        this.id = reference.getKey();
        reference.setValue(this);
    }
}
