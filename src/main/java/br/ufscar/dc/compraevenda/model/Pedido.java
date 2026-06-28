package br.ufscar.dc.compraevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataPedido = LocalDateTime.now();
    private String status = "PENDENTE";
    private BigDecimal total;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore  // ← ADICIONADO PARA EVITAR RECURSÃO
    private Cliente cliente;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_entrega_id")
    @JsonIgnore  // ← ADICIONADO PARA EVITAR RECURSÃO
    private Endereco enderecoEntrega;
    
    public void addItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }
    
    public void calcularTotal() {
        this.total = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
