package br.ufscar.dc.dsw.service;

import br.ufscar.dc.dsw.domain.Cliente;

import java.util.Optional;

public interface ClienteService {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Iterable<Cliente> listar();

    void excluirPorId(Long id);
}

