package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Roupas;

public interface IRoupasDAO extends JpaRepository<Roupas, Long> {
}

