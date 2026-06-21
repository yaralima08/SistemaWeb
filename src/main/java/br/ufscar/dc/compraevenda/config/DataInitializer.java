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
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=============================================");
        System.out.println("📦 Inicializando dados do sistema...");
        System.out.println("=============================================");

        // 1. Criar ADMIN
        criarAdmin();

        // 2. Criar VENDEDOR
        Vendedor vendedor = criarVendedor();

        // 3. Criar CLIENTE
        criarCliente();

        // 4. Criar PRODUTOS (se houver vendedor)
        if (vendedor != null && vendedor.getId() != null) {
            criarProdutos(vendedor);
        }

        System.out.println("=============================================");
        System.out.println("👤 Usuários disponíveis:");
        System.out.println("   Admin: admin@stylestore.com / admin123");
        System.out.println("   Vendedor: vendedor@loja.com / vendedor123");
        System.out.println("   Cliente: cliente@email.com / cliente123");
        System.out.println("=============================================");
    }

    private void criarAdmin() {
        try {
            if (!clienteRepository.existsByEmail("admin@stylestore.com")) {
                Cliente admin = new Cliente();
                admin.setEmail("admin@stylestore.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setCpf("000.000.000-00");
                admin.setNome("Administrador");
                admin.setTelefone("(00) 00000-0000");
                admin.setSexo("MASCULINO");
                admin.setAtivo(true);
                clienteRepository.save(admin);
                System.out.println("✅ Admin criado");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Admin já existe ou erro: " + e.getMessage());
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
                System.out.println("✅ Vendedor criado (ID: " + salvo.getId() + ")");
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
                System.out.println("✅ Cliente criado");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Cliente já existe ou erro: " + e.getMessage());
        }
    }

    private void criarProdutos(Vendedor vendedor) {
        try {
            // Verifica se já existem produtos
            if (produtoRepository.count() > 0) {
                System.out.println("ℹ️ Produtos já existem (" + produtoRepository.count() + " produtos)");
                return;
            }

            System.out.println("📦 Criando produtos para o vendedor ID: " + vendedor.getId());

            // Lista de produtos com suas imagens
            List<ProdutoInfo> produtos = Arrays.asList(
                new ProdutoInfo("Vestido Floral Verão", "FEMININO", "M", "VERMELHO", 
                    "Vestido leve e florido, ideal para dias quentes.", 30, 89.90,
                    "https://i.pinimg.com/736x/11/4b/27/114b2726bf1df418b6c46d8d91db7eb3.jpg"),
                
                new ProdutoInfo("Blusa de Seda Elegante", "FEMININO", "P", "BEGE", 
                    "Blusa de seda pura com decote V e mangas longas.", 25, 199.90,
                    "https://i.pinimg.com/736x/a3/74/5c/a3745c3dcc72abe00207f097653c8525.jpg"),
                
                new ProdutoInfo("Calça Jeans Skinny", "FEMININO", "G", "AZUL", 
                    "Calça jeans skinny com elastano, alta elasticidade.", 40, 129.90,
                    "https://i.pinimg.com/736x/f5/10/79/f51079c743d187a0e6ef5f94edd80adc.jpg"),
                
                new ProdutoInfo("Short Jeans Feminino", "FEMININO", "M", "AZUL", 
                    "Short jeans com cintura alta e modelo destroyed.", 30, 89.90,
                    "https://i.pinimg.com/736x/1e/f6/5c/1ef65c2f87ff8bc1448bf81955680cf9.jpg"),
                
                new ProdutoInfo("Saia Midi Lápis", "FEMININO", "P", "PRETO", 
                    "Saia midi lápis em tecido crepe com fenda lateral.", 25, 139.90,
                    "https://i.pinimg.com/1200x/2a/b5/71/2ab57162c8431ac028e12bef4e8e631c.jpg"),
                
                new ProdutoInfo("Blusa Cropped", "FEMININO", "P", "ROSA", 
                    "Blusa cropped de malha com gola careca.", 35, 69.90,
                    "https://i.pinimg.com/736x/5b/03/46/5b0346382db0b86abf0a73fe032262be.jpg"),
                
                new ProdutoInfo("Vestido Longo Estampado", "FEMININO", "M", "AZUL", 
                    "Vestido longo com estampa tropical e alças finas.", 20, 189.90,
                    "https://i.pinimg.com/736x/e5/45/ac/e545acdfdefb05cebf15e3e1453980bc.jpg"),
                
                new ProdutoInfo("Camiseta Básica Premium", "MASCULINO", "G", "BRANCO", 
                    "Camiseta 100% algodão, confortável e durável.", 50, 39.90,
                    "https://i.pinimg.com/1200x/4c/76/cf/4c76cf9f13c1195bcec2db853d555606.jpg"),
                
                new ProdutoInfo("Camisa Polo", "MASCULINO", "M", "AZUL", 
                    "Camisa polo em malha piqué com gola canelada.", 35, 79.90,
                    "https://i.pinimg.com/736x/f4/4b/20/f44b20591a61d53b890230618fb5c4c2.jpg"),
                
                new ProdutoInfo("Calça Jeans Masculina", "MASCULINO", "G", "AZUL", 
                    "Calça jeans com modelagem slim.", 40, 119.90,
                    "https://i.pinimg.com/736x/8a/5f/4c/8a5f4cb383225540cb50d40dd2a6013e.jpg"),
                
                new ProdutoInfo("Bermuda Jeans", "MASCULINO", "G", "AZUL", 
                    "Bermuda jeans com acabamento desfiado.", 30, 89.90,
                    "https://i.pinimg.com/736x/c8/bf/08/c8bf08cc3ed41ae73e77d8de23035112.jpg"),
                
                new ProdutoInfo("Camisa Social", "MASCULINO", "G", "BRANCO", 
                    "Camisa social de algodão com colarinho estruturado.", 30, 89.90,
                    "https://i.pinimg.com/736x/f4/4b/20/f44b20591a61d53b890230618fb5c4c2.jpg"),
                
                new ProdutoInfo("Moletom com Capuz", "MASCULINO", "G", "CINZA", 
                    "Moletom com capuz e bolso canguru.", 25, 129.90,
                    "https://i.pinimg.com/736x/c8/bf/08/c8bf08cc3ed41ae73e77d8de23035112.jpg"),
                
                new ProdutoInfo("Conjunto Baby", "INFANTIL", "M", "AZUL", 
                    "Conjunto body e calça em algodão macio.", 30, 69.90,
                    "https://i.pinimg.com/736x/11/4b/27/114b2726bf1df418b6c46d8d91db7eb3.jpg"),
                
                new ProdutoInfo("Macacão Infantil", "INFANTIL", "G", "VERMELHO", 
                    "Macacão em algodão com estampa de animais.", 25, 59.90,
                    "https://i.pinimg.com/736x/5b/03/46/5b0346382db0b86abf0a73fe032262be.jpg")
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
                
                // Adicionar imagem
                if (info.imagemUrl != null && !info.imagemUrl.isEmpty()) {
                    produto.adicionarImagem(info.imagemUrl);
                }
                
                produtoRepository.save(produto);
                System.out.println("✅ Produto criado: " + info.nome);
            }

            System.out.println("✅ Total de " + produtoRepository.count() + " produtos criados!");

        } catch (Exception e) {
            System.out.println("⚠️ Erro ao criar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Classe interna para armazenar dados dos produtos
    private static class ProdutoInfo {
        String nome;
        String categoria;
        String tamanho;
        String cor;
        String descricao;
        int quantidade;
        double preco;
        String imagemUrl;

        ProdutoInfo(String nome, String categoria, String tamanho, String cor, 
                    String descricao, int quantidade, double preco, String imagemUrl) {
            this.nome = nome;
            this.categoria = categoria;
            this.tamanho = tamanho;
            this.cor = cor;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.preco = preco;
            this.imagemUrl = imagemUrl;
        }
    }
}