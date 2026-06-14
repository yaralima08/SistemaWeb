package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IVendedorDAO;
import br.ufscar.dc.dsw.domain.Vendedor;
import br.ufscar.dc.dsw.service.VendedorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VendedorServiceImpl implements VendedorService {

    private final IVendedorDAO vendedorDAO;

    public VendedorServiceImpl(IVendedorDAO vendedorDAO) {
        this.vendedorDAO = vendedorDAO;
    }

    @Override
    public Vendedor salvar(Vendedor vendedor) {
        return vendedorDAO.save(vendedor);
    }

    @Override
    public Optional<Vendedor> buscarPorId(Long id) {
        return vendedorDAO.findById(id);
    }

    @Override
    public Iterable<Vendedor> listar() {
        return vendedorDAO.findAll();
    }

    @Override
    public void excluirPorId(Long id) {
        vendedorDAO.deleteById(id);
    }
}

