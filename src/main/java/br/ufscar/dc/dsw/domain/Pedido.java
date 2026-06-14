package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "roupa_id")
    @NotNull
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

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Roupa getRoupa() {
        return roupa;
    }

    public void setRoupa(Roupa roupa) {
        this.roupa = roupa;
    }
}