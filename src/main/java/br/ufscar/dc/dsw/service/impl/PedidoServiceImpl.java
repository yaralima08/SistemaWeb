package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IPedidoDAO;
import br.ufscar.dc.dsw.domain.Pedido;
import br.ufscar.dc.dsw.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final IPedidoDAO pedidoDAO;

    public PedidoServiceImpl(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        return pedidoDAO.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoDAO.findById(id);
    }

    @Override
    public Iterable<Pedido> listar() {
        return pedidoDAO.findAll();
    }

    @Override
    public void excluirPorId(Long id) {
        pedidoDAO.deleteById(id);
    }
}

