package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final VendedorService vendedorService;

    // Salvar produto
    @Transactional
    public Produto salvar(Produto produto, Long vendedorId) {
        Vendedor vendedor = vendedorService.buscarPorId(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        produto.setVendedor(vendedor);
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    // Atualizar produto
    @Transactional
    public Produto atualizar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Deletar produto (desativar)
    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    // Deletar produto do vendedor (verifica se pertence ao vendedor)
    @Transactional
    public void deletarDoVendedor(Long produtoId, Long vendedorId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new RuntimeException("Este produto não pertence ao vendedor");
        }
        
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    // Buscar produto ativo do vendedor
    public Produto buscarAtivoPorIdDoVendedor(Long produtoId, Long vendedorId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        if (!produto.isAtivo()) {
            throw new RuntimeException("Produto está inativo");
        }
        
        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new RuntimeException("Este produto não pertence ao vendedor");
        }
        
        return produto;
    }

    // Listar todos os produtos
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Listar produtos de um vendedor
    public List<Produto> listarPorVendedor(Long vendedorId) {
        return produtoRepository.findByVendedorId(vendedorId);
    }

    // Buscar produto por ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // Filtrar produtos
    public List<Produto> filtrar(String categoria, String tamanho, String cor, 
                                 BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.filtrarProdutos(categoria, tamanho, cor, precoMin, precoMax);
    }

    // Atualizar estoque
    @Transactional
    public void atualizarEstoque(Long produtoId, int quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        int novoEstoque = produto.getQuantidadeEstoque() - quantidade;
        if (novoEstoque < 0) {
            throw new RuntimeException("Estoque insuficiente");
        }
        produto.setQuantidadeEstoque(novoEstoque);
        produtoRepository.save(produto);
    }
}