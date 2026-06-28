package br.ufscar.dc.compraevenda.controller.api;

import br.ufscar.dc.compraevenda.controller.api.dto.CompraRequest;
import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Endereco;
import br.ufscar.dc.compraevenda.model.ItemPedido;
import br.ufscar.dc.compraevenda.model.Pedido;
import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.PedidoService;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    @GetMapping("/pedidos")
    public ResponseEntity<?> listarPedidos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado. Faça login primeiro.");
        }

        try {
            String email = authentication.getName();
            System.out.println("🔍 Buscando pedidos para: " + email);

            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            List<Pedido> pedidos = pedidoService.listarPorCliente(cliente.getId());
            return ResponseEntity.ok(pedidos);

        } catch (Exception e) {
            System.err.println("❌ Erro ao listar pedidos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        try {
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            Pedido pedido = pedidoService.buscarPorId(id);

            if (!pedido.getCliente().getId().equals(cliente.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Este pedido não pertence a este cliente.");
            }

            return ResponseEntity.ok(pedido);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pedido não encontrado: " + e.getMessage());
        }
    }

    // ===== R5 (COMPRA) VIA REST =====
    // Exemplo: POST /api/cliente/compras
    // Body: { "produtoId": 1, "quantidade": 2, "endereco": { ... } }
    @PostMapping("/compras")
    public ResponseEntity<?> realizarCompra(
            @Valid @RequestBody CompraRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado. Faça login primeiro.");
        }

        try {
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            Produto produto = produtoService.buscarPorId(request.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getQuantidadeEstoque() < request.getQuantidade()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Estoque insuficiente. Disponível: " + produto.getQuantidadeEstoque());
            }

            Endereco endereco = request.getEndereco();
            if (endereco == null) {
                endereco = cliente.getEndereco();
            }
            if (endereco == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Endereço de entrega obrigatório.");
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(endereco);

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(request.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            pedido.addItem(item);

            Pedido salvo = pedidoService.criarPedido(pedido, cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

        } catch (Exception e) {
            System.err.println("❌ Erro ao realizar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao realizar compra: " + e.getMessage());
        }
    }
}

