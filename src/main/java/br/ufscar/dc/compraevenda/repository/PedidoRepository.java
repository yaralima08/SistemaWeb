package br.ufscar.dc.compraevenda.repository;

import br.ufscar.dc.compraevenda.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    
    List<Pedido> findByClienteId(Long clienteId);
    
    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.itens i WHERE i.produto.vendedor.id = :vendedorId ORDER BY p.dataPedido DESC")
    List<Pedido> findByVendedorId(@Param("vendedorId") Long vendedorId);
}