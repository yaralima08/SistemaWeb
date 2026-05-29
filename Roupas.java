package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "Roupa")
public class Roupas {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 5)
	private Integer tamanho;

	@Column(nullable = false, unique = true, length = 60)
	private String corRoupa;

	@Column(nullable = false, unique = true, length = 60)
	private String tipoRoupa;
    
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(7,2) DEFAULT 0.0")
	private BigDecimal preco;
    
	@ManyToOne
	@JoinColumn(name = "loja_id")
	private Loja loja;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getTamanho(){
		return tamanho;
	}

    public void setTamanho(int tamanho){
		this.tamanho = tamanho;
	}
	
    public String getCorRoupa(){
		return corRoupa;
	} 

	public void setCorRoupa(String corRoupa){
		this.corRoupa = corRoupa;
	}

    public String getTipoRoupa(){
		return tipoRoupa;
	} 

	public void setTipoRoupa(String tipoRoupa){
		this.tipoRoupa = tipoRoupa;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		Roupas other = (Roupas) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("Tamanho: " + tamanho + ", ");
		sb.append("Cor da roupa: " + corRoupa + ", ");
		sb.append("Tipo de Roupa: " + tipoRoupa + ", ");
		sb.append("Preço: " + preco + ", ");
		sb.append("Loja: " + loja);
		sb.append("]");
		return sb.toString(); 
	}
}