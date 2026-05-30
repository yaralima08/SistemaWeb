package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "roupa_id")
    private Roupa roupa;

    public Pedido() {
    }

    public Pedido(Integer quantidade, Cliente cliente, Roupa roupa) {
        this.quantidade = quantidade;
        this.cliente = cliente;
        this.roupa = roupa;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Roupa getRoupa() {
        return roupa;
    }
}