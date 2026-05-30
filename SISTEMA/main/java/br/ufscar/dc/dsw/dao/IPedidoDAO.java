package br.ufscar.dc.dsw.dao;

import org.springframework.data.repository.CrudRepository;
import br.ufscar.dc.dsw.domain.Pedido;

public interface IPedidoDAO extends CrudRepository<Pedido, Long> {
}