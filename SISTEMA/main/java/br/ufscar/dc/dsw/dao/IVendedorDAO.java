package br.ufscar.dc.dsw.dao;

import org.springframework.data.repository.CrudRepository;
import br.ufscar.dc.dsw.domain.Vendedor;

public interface IVendedorDAO extends CrudRepository<Vendedor, Long> {
}