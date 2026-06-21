package br.ufscar.dc.compraevenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "enderecos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "{error.required}")
    private String logradouro;
    
    @NotBlank(message = "{error.required}")
    private String numero;
    
    private String complemento;
    
    @NotBlank(message = "{error.required}")
    private String bairro;
    
    @NotBlank(message = "{error.required}")
    private String cidade;
    
    @NotBlank(message = "{error.required}")
    private String estado;
    
    @NotBlank(message = "{error.required}")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido")
    private String cep;
}