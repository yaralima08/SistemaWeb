package br.ufscar.dc.compraevenda.repository;

import br.ufscar.dc.compraevenda.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);  
}