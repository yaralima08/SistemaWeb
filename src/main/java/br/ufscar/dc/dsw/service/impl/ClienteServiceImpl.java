package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IClienteDAO;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final IClienteDAO clienteDAO;

    public ClienteServiceImpl(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return clienteDAO.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteDAO.findById(id);
    }

    @Override
    public Iterable<Cliente> listar() {
        return clienteDAO.findAll();
    }

    @Override
    public void excluirPorId(Long id) {
        clienteDAO.deleteById(id);
    }
}

