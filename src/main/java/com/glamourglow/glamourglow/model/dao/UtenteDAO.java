package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Utente;
import java.util.List;
public interface UtenteDAO {

    public Utente create(

            long id_nome,
            String password,
            String nome,
            String cognome,
            String email,
            String telefono,
            Double wallet,
            String statoaccount,
            boolean ruolo
            );

    public Utente findLoggedUser ();

    public Utente findEmail(String email);

    public void delete(Utente utente);
    public void update(Utente utente);
    public Utente findById(long id);

    void updatewallet(long idNome, Double saldo);

    List<Utente> findSearch();
}
