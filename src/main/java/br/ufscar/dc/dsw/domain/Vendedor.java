package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import br.ufscar.dc.dsw.validation.CNPJ;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "Vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String nomeLoja;

    @Email
    @NotBlank
    @Size(max = 200)
    private String email;

    @NotBlank
    @Size(max = 200)
    private String senha;

    @CNPJ
    @NotBlank
    @Size(max = 20)
    private String cnpj;


    @NotBlank
    @Size(max = 1000)
    private String descricao;

    // Endereço (embutido na própria entidade)

    @NotBlank
    @Size(max = 150)
    private String rua;

    @NotBlank
    @Size(max = 20)
    private String numero;

    @NotBlank
    @Size(max = 100)
    private String bairro;

    @NotBlank
    @Size(max = 100)
    private String cidade;

    @NotBlank
    @Size(max = 2)
    private String estado;

    @NotBlank
    @Size(max = 9)
    private String cep;

    public Vendedor() {
    }

    public Vendedor(String nomeLoja, String email, String senha, String cnpj, String descricao,
                     String rua, String numero, String bairro, String cidade, String estado, String cep) {
        this.nomeLoja = nomeLoja;
        this.email = email;
        this.senha = senha;
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}

