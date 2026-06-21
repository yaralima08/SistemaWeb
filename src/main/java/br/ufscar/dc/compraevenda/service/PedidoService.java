package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.*;
import br.ufscar.dc.compraevenda.repository.PedidoRepository;
import br.ufscar.dc.compraevenda.service.exception.EnderecoNaoEncontradoException;
// import br.ufscar.dc.compraevenda.service.exception.EstoqueInsuficienteException;
import br.ufscar.dc.compraevenda.service.exception.PedidoNaoEncontradoException;
import br.ufscar.dc.compraevenda.service.exception.ProdutoNaoEncontradoException;
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
        pedido.setCliente(cliente);

        if (cliente.getEndereco() == null || pedido.getEnderecoEntrega() == null) {
            throw new EnderecoNaoEncontradoException();
        }


        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProduto().getId())
                    .orElseThrow(ProdutoNaoEncontradoException::new);

            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());

            // Atualiza estoque
            produtoService.atualizarEstoque(produto.getId(), item.getQuantidade());
        }

        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }
    
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
    
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(PedidoNaoEncontradoException::new);

    }
    
    @Transactional
    public Pedido atualizarStatus(Long id, String status) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }
}