package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeLoja;
    private String email;
    private String senha;
    private String cnpj;
    private String descricao;

    public Vendedor() {
    }

    public Vendedor(String nomeLoja, String email, String senha,
                     String cnpj, String descricao) {

        this.nomeLoja = nomeLoja;
        this.email = email;
        this.senha = senha;
        this.cnpj = cnpj;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}