package br.ufscar.dc.dsw.dao;

import org.springframework.data.repository.CrudRepository;
import br.ufscar.dc.dsw.domain.Roupa;

public interface IRoupaDAO extends CrudRepository<Roupa, Long> {
}