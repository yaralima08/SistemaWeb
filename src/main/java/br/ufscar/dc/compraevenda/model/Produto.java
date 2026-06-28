package br.ufscar.dc.compraevenda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String categoria;
    private String tamanho;
    private String cor;
    private String descricao;
    private Integer quantidadeEstoque = 0;
    private BigDecimal preco;
    
    @ElementCollection
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "imagem_url")
    private List<String> imagens = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    @JsonIgnore  // ← ADICIONADO PARA EVITAR RECURSÃO
    private Vendedor vendedor;
    
    private boolean ativo = true;
    
    public void adicionarImagem(String url) {
        if (imagens.size() < 10) imagens.add(url);
    }
}
