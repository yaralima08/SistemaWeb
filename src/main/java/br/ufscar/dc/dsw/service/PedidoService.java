package br.ufscar.dc.dsw.service;

import br.ufscar.dc.dsw.domain.Pedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    Iterable<Pedido> listar();

    void excluirPorId(Long id);
}

