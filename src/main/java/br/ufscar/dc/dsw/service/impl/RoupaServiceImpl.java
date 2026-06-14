package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IRoupaDAO;
import br.ufscar.dc.dsw.domain.Roupa;
import br.ufscar.dc.dsw.service.RoupaService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoupaServiceImpl implements RoupaService {

    private final IRoupaDAO roupaDAO;

    public RoupaServiceImpl(IRoupaDAO roupaDAO) {
        this.roupaDAO = roupaDAO;
    }

    @Override
    public Roupa salvar(Roupa roupa) {
        return roupaDAO.save(roupa);
    }

    @Override
    public Optional<Roupa> buscarPorId(Long id) {
        return roupaDAO.findById(id);
    }

    @Override
    public Iterable<Roupa> listar() {
        return roupaDAO.findAll();
    }

    @Override
    public void excluirPorId(Long id) {
        roupaDAO.deleteById(id);
    }
}

