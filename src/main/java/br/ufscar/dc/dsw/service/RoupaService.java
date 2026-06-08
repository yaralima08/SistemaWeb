package br.ufscar.dc.dsw.service;

import br.ufscar.dc.dsw.domain.Roupa;

import java.util.Optional;

public interface RoupaService {

    Roupa salvar(Roupa roupa);

    Optional<Roupa> buscarPorId(Long id);

    Iterable<Roupa> listar();

    void excluirPorId(Long id);
}

