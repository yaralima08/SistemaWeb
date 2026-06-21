package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.model.Vendedor;
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

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Vendedor vendedor = vendedorService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        
        List<Produto> produtos = produtoService.listarPorVendedor(vendedor.getId());
        model.addAttribute("vendedor", vendedor);
        model.addAttribute("produtos", produtos);
        
        return "vendedor/dashboard";
    }

    @GetMapping("/produtos/novo")
    public String novoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "vendedor/novo-produto";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(
            @ModelAttribute Produto produto,
            @RequestParam(required = false) String imagens,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            // Adicionar imagens se fornecidas
            if (imagens != null && !imagens.isEmpty()) {
                String[] urls = imagens.split(",");
                for (String url : urls) {
                    if (!url.trim().isEmpty()) {
                        produto.adicionarImagem(url.trim());
                    }
                }
            }
            
            produtoService.salvar(produto, vendedor.getId());
            redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "vendedor/novo-produto";
        }
        
        return "redirect:/vendedor/dashboard";
    }

    @PostMapping("/produtos/deletar/{id}")
    public String deletarProduto(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            Produto produto = produtoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            if (!produto.getVendedor().getId().equals(vendedor.getId())) {
                throw new RuntimeException("Você não tem permissão para deletar este produto");
            }
            
            produtoService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Produto deletado com sucesso!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/vendedor/dashboard";
    }
}