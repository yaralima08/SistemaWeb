package br.ufscar.dc.compraevenda.repository;

import br.ufscar.dc.compraevenda.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    @Query("SELECT p FROM Produto p WHERE p.vendedor.id = :vendedorId AND p.ativo = true")
    List<Produto> findByVendedorId(@Param("vendedorId") Long vendedorId);
    
    @Query("SELECT p FROM Produto p WHERE p.ativo = true")
    List<Produto> findAllAtivos();
    
    @Query("SELECT p FROM Produto p WHERE p.ativo = true " +
           "AND (:categoria IS NULL OR :categoria = '' OR p.categoria = :categoria) " +
           "AND (:tamanho IS NULL OR :tamanho = '' OR p.tamanho = :tamanho) " +
           "AND (:cor IS NULL OR :cor = '' OR p.cor = :cor) " +
           "AND (:precoMin IS NULL OR p.preco >= :precoMin) " +
           "AND (:precoMax IS NULL OR p.preco <= :precoMax)")
    List<Produto> filtrarProdutos(
            @Param("categoria") String categoria,
            @Param("tamanho") String tamanho,
            @Param("cor") String cor,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax
    );
}