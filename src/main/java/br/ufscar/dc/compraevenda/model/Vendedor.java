package br.ufscar.dc.compraevenda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(unique = true, nullable = false)
    private String cnpj;
    
    @Column(nullable = false)
    private String nomeLoja;
    
    private String descricao;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
    
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();
    
    private boolean ativo = true;
}