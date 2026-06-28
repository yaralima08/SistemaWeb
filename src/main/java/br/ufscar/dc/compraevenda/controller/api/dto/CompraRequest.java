package br.ufscar.dc.compraevenda.controller.api.dto;

import br.ufscar.dc.compraevenda.model.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CompraRequest {

    @NotNull(message = "produtoId obrigatório")
    private Long produtoId;

    @NotNull(message = "quantidade obrigatória")
    @Min(value = 1, message = "quantidade deve ser >= 1")
    private Integer quantidade;

    // Opcional: se vier nulo, usa o endereço do cliente (se existir)
    @Valid
    private Endereco endereco;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}

