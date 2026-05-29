package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Loja;

public interface ILojaDAO extends JpaRepository<Loja, Long> {
}
