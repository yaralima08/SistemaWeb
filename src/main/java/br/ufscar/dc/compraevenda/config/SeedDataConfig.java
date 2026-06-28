package br.ufscar.dc.compraevenda.config;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.repository.ClienteRepository;
import br.ufscar.dc.compraevenda.repository.ProdutoRepository;
import br.ufscar.dc.compraevenda.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=============================================");
        System.out.println("📦 Inicializando dados do sistema (SeedData)...");
        System.out.println("=============================================");

        // 1. CRIAR ADMIN
        criarAdmin();

        // 2. CRIAR VENDEDOR
        Vendedor vendedor = criarVendedor();

        // 3. CRIAR CLIENTE
        criarCliente();

        // 4. CRIAR PRODUTOS
        if (vendedor != null && vendedor.getId() != null) {
            criarProdutos(vendedor);
        }

        System.out.println("=============================================");
        System.out.println("✅ Dados inicializados com sucesso!");
        System.out.println("=============================================");
    }

    private void criarAdmin() {
        try {
            // Garante que a senha do admin esteja sempre consistente com o login
            Cliente admin = clienteRepository.findByEmail("admin@stylestore.com")
                    .orElseGet(() -> {
                        Cliente c = new Cliente();
                        c.setEmail("admin@stylestore.com");
                        c.setCpf("000.000.000-00");
                        c.setNome("Administrador");
                        c.setTelefone("(00) 00000-0000");
                        c.setSexo("MASCULINO");
                        return c;
                    });

            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setAtivo(true);

            clienteRepository.save(admin);
            System.out.println("✅ Admin pronto: admin@stylestore.com / admin123");
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao criar Admin: " + e.getMessage());
        }
    }


    private Vendedor criarVendedor() {
        try {
            if (!vendedorRepository.existsByEmail("vendedor@loja.com") && 
                !vendedorRepository.existsByCnpj("12.345.678/0001-90")) {
                Vendedor vendedor = new Vendedor();
                vendedor.setEmail("vendedor@loja.com");
                vendedor.setSenha(passwordEncoder.encode("vendedor123"));
                vendedor.setCnpj("12.345.678/0001-90");
                vendedor.setNomeLoja("Loja Exemplo");
                vendedor.setDescricao("Loja de roupas com as melhores tendências da moda");
                vendedor.setAtivo(true);
                Vendedor salvo = vendedorRepository.save(vendedor);
                System.out.println("✅ Vendedor criado: vendedor@loja.com / vendedor123 (ID: " + salvo.getId() + ")");
                return salvo;
            } else {
                System.out.println("ℹ️ Vendedor já existe");
                return vendedorRepository.findByEmail("vendedor@loja.com").orElse(null);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao criar Vendedor: " + e.getMessage());
            return null;
        }
    }

    private void criarCliente() {
        try {
            if (!clienteRepository.existsByEmail("cliente@email.com")) {
                Cliente cliente = new Cliente();
                cliente.setEmail("cliente@email.com");
                cliente.setSenha(passwordEncoder.encode("cliente123"));
                cliente.setCpf("111.222.333-44");
                cliente.setNome("Cliente Exemplo");
                cliente.setTelefone("(11) 91234-5678");
                cliente.setSexo("FEMININO");
                cliente.setAtivo(true);
                clienteRepository.save(cliente);
                System.out.println("✅ Cliente criado: cliente@email.com / cliente123");
            } else {
                System.out.println("ℹ️ Cliente já existe");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao criar Cliente: " + e.getMessage());
        }
    }

    private void criarProdutos(Vendedor vendedor) {
        try {
            // Verifica se já existem produtos
            if (produtoRepository.count() > 0) {
                System.out.println("ℹ️ Produtos já existem (" + produtoRepository.count() + " produtos)");
                return;
            }

            System.out.println("📦 Criando produtos para o vendedor: " + vendedor.getNomeLoja());

            List<ProdutoInfo> produtos = Arrays.asList(
                // ===== FEMININO =====
                new ProdutoInfo("Vestido Floral Verão", "FEMININO", "M", "VERMELHO", 
                    "Vestido leve e florido, ideal para dias quentes.", 30, 89.90),
                new ProdutoInfo("Blusa de Seda Elegante", "FEMININO", "P", "BEGE", 
                    "Blusa de seda pura com decote V e mangas longas.", 25, 199.90),
                new ProdutoInfo("Calça Jeans Skinny", "FEMININO", "G", "AZUL", 
                    "Calça jeans skinny com elastano, alta elasticidade.", 40, 129.90),
                new ProdutoInfo("Short Jeans Feminino", "FEMININO", "M", "AZUL", 
                    "Short jeans com cintura alta e modelo destroyed.", 30, 89.90),
                new ProdutoInfo("Saia Midi Lápis", "FEMININO", "P", "PRETO", 
                    "Saia midi lápis em tecido crepe com fenda lateral.", 25, 139.90),
                new ProdutoInfo("Blusa Cropped", "FEMININO", "P", "ROSA", 
                    "Blusa cropped de malha com gola careca.", 35, 69.90),
                new ProdutoInfo("Vestido Longo Estampado", "FEMININO", "M", "AZUL", 
                    "Vestido longo com estampa tropical e alças finas.", 20, 189.90),
                
                // ===== MASCULINO =====
                new ProdutoInfo("Camiseta Básica Premium", "MASCULINO", "G", "BRANCO", 
                    "Camiseta 100% algodão, confortável e durável.", 50, 39.90),
                new ProdutoInfo("Camisa Polo", "MASCULINO", "M", "AZUL", 
                    "Camisa polo em malha piqué com gola canelada.", 35, 79.90),
                new ProdutoInfo("Calça Jeans Masculina", "MASCULINO", "G", "AZUL", 
                    "Calça jeans com modelagem slim.", 40, 119.90),
                new ProdutoInfo("Bermuda Jeans", "MASCULINO", "G", "AZUL", 
                    "Bermuda jeans com acabamento desfiado.", 30, 89.90),
                new ProdutoInfo("Camisa Social", "MASCULINO", "G", "BRANCO", 
                    "Camisa social de algodão com colarinho estruturado.", 30, 89.90),
                new ProdutoInfo("Moletom com Capuz", "MASCULINO", "G", "CINZA", 
                    "Moletom com capuz e bolso canguru.", 25, 129.90),
                
                // ===== INFANTIL =====
                new ProdutoInfo("Conjunto Baby", "INFANTIL", "M", "AZUL", 
                    "Conjunto body e calça em algodão macio.", 30, 69.90),
                new ProdutoInfo("Macacão Infantil", "INFANTIL", "G", "VERMELHO", 
                    "Macacão em algodão com estampa de animais.", 25, 59.90)
            );

            for (ProdutoInfo info : produtos) {
                Produto produto = new Produto();
                produto.setNome(info.nome);
                produto.setCategoria(info.categoria);
                produto.setTamanho(info.tamanho);
                produto.setCor(info.cor);
                produto.setDescricao(info.descricao);
                produto.setQuantidadeEstoque(info.quantidade);
                produto.setPreco(new BigDecimal(info.preco));
                produto.setVendedor(vendedor);
                produto.setAtivo(true);
                
                produtoRepository.save(produto);
                System.out.println("   ✅ Produto criado: " + info.nome);
            }

            System.out.println("✅ Total de " + produtoRepository.count() + " produtos criados!");

        } catch (Exception e) {
            System.out.println("⚠️ Erro ao criar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Classe interna para dados dos produtos
    private static class ProdutoInfo {
        String nome;
        String categoria;
        String tamanho;
        String cor;
        String descricao;
        int quantidade;
        double preco;

        ProdutoInfo(String nome, String categoria, String tamanho, String cor, 
                    String descricao, int quantidade, double preco) {
            this.nome = nome;
            this.categoria = categoria;
            this.tamanho = tamanho;
            this.cor = cor;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.preco = preco;
        }
    }
}
