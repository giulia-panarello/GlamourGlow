package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Categoria;
import java.util.List;

public interface CategoriaDAO {

    Categoria findById(Long idCategoria);

    List<Categoria> findAll();
}
