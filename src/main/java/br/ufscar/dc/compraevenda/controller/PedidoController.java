package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Endereco;
import br.ufscar.dc.compraevenda.model.ItemPedido;
import br.ufscar.dc.compraevenda.model.Pedido;
import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.PedidoService;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comprar")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    @GetMapping("/{produtoId}")
    public String comprar(@PathVariable Long produtoId, Authentication authentication, Model model) {
        // Verifica se o usuário está logado
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Produto produto = produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        model.addAttribute("produto", produto);
        model.addAttribute("endereco", new Endereco());
        model.addAttribute("quantidade", 1);
        
        return "pedido/checkout";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade,
            @ModelAttribute Endereco endereco,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            Produto produto = produtoService.buscarPorId(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            // Criar pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(endereco);

            // Criar item do pedido
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPreco());
            pedido.addItem(item);

            // Salvar pedido
            pedidoService.criarPedido(pedido, cliente);

            redirectAttributes.addFlashAttribute("success", 
                "Compra realizada com sucesso! Pedido #" + pedido.getId());

            return "redirect:/cliente/pedidos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/comprar/" + produtoId;
        }
    }
}