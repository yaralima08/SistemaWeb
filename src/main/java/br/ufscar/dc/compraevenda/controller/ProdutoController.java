package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/")
    public String index(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        model.addAttribute("produtos", produtos);
        return "index";
    }

    @GetMapping("/produtos")
    public String listarProdutos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String tamanho,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            Model model) {
        
        List<Produto> produtos = produtoService.filtrar(categoria, tamanho, cor, precoMin, precoMax);
        model.addAttribute("produtos", produtos);
        model.addAttribute("categoriaSelecionada", categoria);
        model.addAttribute("tamanhoSelecionado", tamanho);
        model.addAttribute("corSelecionada", cor);
        
        return "produtos/listagem";
    }

    @GetMapping("/produtos/detalhe/{id}")
    public String detalheProduto(@PathVariable Long id, Model model) {
        System.out.println("🔍 Buscando detalhes do produto ID: " + id);
        
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> {
                    System.err.println("❌ Produto não encontrado: " + id);
                    return new RuntimeException("Produto não encontrado");
                });
        
        System.out.println("📦 Produto encontrado: " + produto.getNome());
        System.out.println("   Preço: R$ " + produto.getPreco());
        System.out.println("   Estoque: " + produto.getQuantidadeEstoque());
        System.out.println("   Imagens: " + (produto.getImagens() != null ? produto.getImagens().size() : 0));
        
        model.addAttribute("produto", produto);
        return "produtos/detalhe";
    }
}