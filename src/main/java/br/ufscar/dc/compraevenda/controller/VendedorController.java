package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Pedido;
import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.service.PedidoService;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import br.ufscar.dc.compraevenda.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vendedor")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Vendedor vendedor = vendedorService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        
        List<Produto> produtos = produtoService.listarPorVendedor(vendedor.getId());
        List<Pedido> pedidos = pedidoService.listarPedidosPorVendedor(vendedor.getId());
        
        Double totalVendas = pedidos.stream()
                .mapToDouble(p -> p.getTotal().doubleValue())
                .sum();
        
        model.addAttribute("vendedor", vendedor);
        model.addAttribute("produtos", produtos);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalProdutos", produtos.size());
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalVendas", totalVendas);
        
        return "vendedor/dashboard";
    }

    @GetMapping("/produtos")
    public String listarProdutos(Authentication authentication, Model model) {
        String email = authentication.getName();
        Vendedor vendedor = vendedorService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        
        List<Produto> produtos = produtoService.listarPorVendedor(vendedor.getId());
        
        System.out.println("📦 Produtos do vendedor " + vendedor.getNomeLoja() + ": " + produtos.size());
        for (Produto p : produtos) {
            System.out.println("   - " + p.getNome() + " (ID: " + p.getId() + ", Ativo: " + p.isAtivo() + ")");
        }
        
        model.addAttribute("produtos", produtos);
        model.addAttribute("vendedor", vendedor);
        
        return "vendedor/produtos";
    }

    @PostMapping("/produtos/deletar/{id}")
    public String deletarProduto(@PathVariable Long id, 
                                  Authentication authentication, 
                                  RedirectAttributes redirectAttributes) {
        try {
            System.out.println("🗑️ Tentando deletar produto ID: " + id);
            
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            Produto produto = produtoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            System.out.println("   Produto: " + produto.getNome());
            System.out.println("   Vendedor do produto: " + produto.getVendedor().getId());
            System.out.println("   Vendedor logado: " + vendedor.getId());
            System.out.println("   Ativo antes: " + produto.isAtivo());
            
            if (!produto.getVendedor().getId().equals(vendedor.getId())) {
                throw new RuntimeException("Você não tem permissão para deletar este produto");
            }
            
            produtoService.deletar(id);
            
            // Verificar se foi deletado
            Produto verificar = produtoService.buscarPorId(id).orElse(null);
            System.out.println("   Ativo depois: " + (verificar != null ? verificar.isAtivo() : "Produto não encontrado"));
            
            redirectAttributes.addFlashAttribute("success", "✅ Produto deletado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao deletar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "❌ Erro: " + e.getMessage());
        }
        
        return "redirect:/vendedor/produtos";
    }

}