package com.glamourglow.glamourglow.model.mo;
import java.util.List;

public class Utente {

    private long idNome;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    private boolean ruolo;

    private String telefono;
    private String statoaccount;

    private Double wallet;


    public long getIdNome(){
        return idNome;
    }

    public void setIdNome(long idNome){
        this.idNome = idNome;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCognome(){
        return cognome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }


    public boolean getRuolo(){
        return ruolo;
    }

    public void setRuolo(boolean ruolo){
        this.ruolo = ruolo;
    }



    public String getTelefono(){
        return telefono;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }


    public String getStatoAccount(){
        return statoaccount;
    }

    public void setStatoAccount(String statoaccount){
        this.statoaccount = statoaccount;
    }



    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public boolean isRuolo() {
        return ruolo;
    }
}

