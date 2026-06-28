package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.ItemPedido;
import br.ufscar.dc.compraevenda.model.Pedido;
import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    @Transactional
    public Pedido criarPedido(Pedido pedido, Cliente cliente) {
        System.out.println("📝 Criando pedido para cliente: " + cliente.getEmail());
        
        pedido.setCliente(cliente);
        
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
            
            // Atualiza estoque
            int novoEstoque = produto.getQuantidadeEstoque() - item.getQuantidade();
            if (novoEstoque < 0) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            produto.setQuantidadeEstoque(novoEstoque);
            produtoService.atualizar(produto);
        }
        
        pedido.calcularTotal();
        Pedido salvo = pedidoRepository.save(pedido);
        System.out.println("✅ Pedido salvo com ID: " + salvo.getId());
        
        return salvo;
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        System.out.println("🔍 Buscando pedidos do cliente ID: " + clienteId);
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        System.out.println("📦 Encontrados " + (pedidos != null ? pedidos.size() : 0) + " pedidos");
        return pedidos != null ? pedidos : List.of();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    // Listar pedidos por vendedor
    public List<Pedido> listarPedidosPorVendedor(Long vendedorId) {
        return pedidoRepository.findByVendedorId(vendedorId);
    }
}