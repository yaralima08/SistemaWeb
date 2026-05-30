package br.ufscar.dc.dsw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.ufscar.dc.dsw.dao.*;
import br.ufscar.dc.dsw.domain.*;

@SpringBootApplication
public class SistemaRoupasApplication implements CommandLineRunner {

    @Autowired
    private IClienteDAO clienteDAO;

    @Autowired
    private IVendedorDAO vendedorDAO;

    @Autowired
    private IRoupaDAO roupaDAO;

    @Autowired
    private IPedidoDAO pedidoDAO;

    public static void main(String[] args) {
        SpringApplication.run(SistemaRoupasApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Cliente c1 = new Cliente(
                "Yara",
                "yara@email.com",
                "123",
                "11111111111",
                "16999999999"
        );

        clienteDAO.save(c1);

        Vendedor v1 = new Vendedor(
                "Loja Fashion",
                "loja@email.com",
                "123",
                "99999999999999",
                "Loja de roupas femininas"
        );

        vendedorDAO.save(v1);

        Roupa r1 = new Roupa(
                "Vestido",
                "Feminino",
                "M",
                "Preto",
                150.0,
                10,
                v1
        );

        roupaDAO.save(r1);

        Pedido p1 = new Pedido(2, c1, r1);

        pedidoDAO.save(p1);

        System.out.println("CRUD executado com sucesso!");
    }
}