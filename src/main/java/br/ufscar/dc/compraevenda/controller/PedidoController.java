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
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    // ===== PÁGINA DE COMPRA - CHECKOUT =====
    @GetMapping("/comprar/{produtoId}")
    public String comprar(@PathVariable Long produtoId, Authentication authentication, Model model) {
        System.out.println("🛒 Entrando na página de compra - Produto ID: " + produtoId);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ Usuário não autenticado - redirecionando para login");
            return "redirect:/login";
        }

        String email = authentication.getName();
        System.out.println("👤 Cliente logado: " + email);
        
        Cliente cliente = clienteService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Produto produto = produtoService.buscarPorId(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        System.out.println("📦 Produto: " + produto.getNome() + " - R$ " + produto.getPreco());
        
        model.addAttribute("produto", produto);
        model.addAttribute("cliente", cliente);
        
        // Se o cliente tem endereço, usa ele, senão cria um novo
        Endereco endereco = cliente.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
            System.out.println("⚠️ Cliente sem endereço - criando novo");
        } else {
            System.out.println("📌 Endereço do cliente: " + endereco.getLogradouro() + ", " + endereco.getNumero());
        }
        model.addAttribute("endereco", endereco);
        model.addAttribute("quantidade", 1);
        
        return "pedido/checkout";
    }

    // ===== FINALIZAR COMPRA =====
    @PostMapping("/comprar/finalizar")
    public String finalizarCompra(
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade,
            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String complemento,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cep,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        System.out.println("🛒 Finalizando compra - Produto ID: " + produtoId + ", Quantidade: " + quantidade);
        
        try {
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            Produto produto = produtoService.buscarPorId(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            // Verificar estoque
            if (produto.getQuantidadeEstoque() < quantidade) {
                throw new RuntimeException("Estoque insuficiente. Disponível: " + produto.getQuantidadeEstoque());
            }

            // Usar endereço do cliente ou o enviado pelo formulário
            Endereco endereco = cliente.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
                endereco.setLogradouro(logradouro);
                endereco.setNumero(numero);
                endereco.setComplemento(complemento);
                endereco.setBairro(bairro);
                endereco.setCidade(cidade);
                endereco.setEstado(estado);
                endereco.setCep(cep);
                System.out.println("📌 Endereço do formulário: " + logradouro + ", " + numero);
            }

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
            Pedido pedidoSalvo = pedidoService.criarPedido(pedido, cliente);
            System.out.println("✅ Pedido criado com sucesso! ID: " + pedidoSalvo.getId());

            redirectAttributes.addFlashAttribute("success", 
                "✅ Compra realizada com sucesso! Pedido #" + pedidoSalvo.getId());

            return "redirect:/cliente/pedidos";

        } catch (Exception e) {
            System.err.println("❌ Erro na compra: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            return "redirect:/comprar/" + produtoId;
        }
    }
}