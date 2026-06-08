package br.ufscar.dc.dsw.service;

import br.ufscar.dc.dsw.domain.Vendedor;

import java.util.Optional;

public interface VendedorService {

    Vendedor salvar(Vendedor vendedor);

    Optional<Vendedor> buscarPorId(Long id);

    Iterable<Vendedor> listar();

    void excluirPorId(Long id);
}

