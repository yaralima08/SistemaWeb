package br.ufscar.dc.dsw;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.ufscar.dc.dsw.dao.ILojaDAO;
import br.ufscar.dc.dsw.dao.IRoupasDAO;
import br.ufscar.dc.dsw.domain.Loja;
import br.ufscar.dc.dsw.domain.Roupas;

@SpringBootApplication
public class LojaRoupasApplication {

	private static final Logger log = LoggerFactory.getLogger(LojaRoupasApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LojaRoupasApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ILojaDAO lojaDAO, IRoupasDAO roupasDAO) {
		return (args) -> {
			log.info("=== CRUD: inicia ===");

			// CREATE: cria uma loja (idempotente para não falhar em reinícios)
			String cnpj = "12.345.678/0001-90";
			String nome = "Loja Central";

			Loja loja;
			var lojas = lojaDAO.findAll();
			loja = lojas.stream()
					.filter(l -> cnpj.equals(l.getCNPJ()))
					.findFirst()
					.orElseGet(() -> {
						Loja nova = new Loja();
						nova.setCNPJ(cnpj);
						nova.setNome(nome);
						return nova;
					});

			// se não existia, persiste
			if (loja.getId() == null) {
				loja = lojaDAO.save(loja);
				log.info("Loja criada: {}", loja);
			} else {
				log.info("Loja existente: {}", loja);
			}


			// CREATE: cria uma roupa associada à loja
			Roupas roupa = new Roupas();
			roupa.setTamanho(42);
			roupa.setCorRoupa("Preto");
			roupa.setTipoRoupa("Camiseta");
			roupa.setPreco(new BigDecimal("59.90"));
			roupa.setLoja(loja);
			roupa = roupasDAO.save(roupa);
			log.info("Roupa criada: {}", roupa);

			// READ ALL
			log.info("Roupas recuperados -- findAll():");
			log.info("--------------------------------");
			for (Roupas r : roupasDAO.findAll()) {
				log.info(r.toString());
			}
			log.info("");

			// READ BY ID
			roupasDAO.findById(roupa.getId()).ifPresent(r -> {
				log.info("Roupa recuperada -- findById({}):", r.getId());
				log.info("---------------------------------");
				log.info(r.toString());
				log.info("");
			});

			// UPDATE
			Roupas roupaAtualizada = roupasDAO.findById(roupa.getId()).orElseThrow();
			roupaAtualizada.setPreco(new BigDecimal("69.90"));
			roupaAtualizada = roupasDAO.save(roupaAtualizada);
			log.info("Roupa atualizada: {}", roupaAtualizada);

			// DELETE
			roupasDAO.deleteById(roupaAtualizada.getId());
			log.info("Roupa deletada. Total de roupas restantes: {}", roupasDAO.count());

			log.info("=== CRUD: fim ===");
		};
	}

}
