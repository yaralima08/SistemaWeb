package br.ufscar.dc.compraevenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    
    @NotBlank(message = "{error.required}")
    private String nome;
    
    @NotBlank(message = "{error.required}")
    private String categoria;
    
    @NotBlank(message = "{error.required}")
    private String tamanho;
    
    @NotBlank(message = "{error.required}")
    private String cor;
    
    private String descricao;
    
    @Min(value = 0, message = "Estoque deve ser maior ou igual a 0")
    private Integer quantidadeEstoque = 0;
    
    @NotNull(message = "{error.required}")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que 0")
    private BigDecimal preco;
    
    // URLS DAS IMAGENS - agora você pode colocar links de imagens
    @ElementCollection
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "imagem_url")
    private List<String> imagens = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;
    
    private boolean ativo = true;

    public void adicionarImagem(String url) {
        if (imagens.size() < 10) {
            imagens.add(url);
        }
    }
}