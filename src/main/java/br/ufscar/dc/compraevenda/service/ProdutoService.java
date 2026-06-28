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

    @Transactional
    public Produto salvar(Produto produto, Long vendedorId) {
        Vendedor vendedor = vendedorService.buscarPorId(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        produto.setVendedor(vendedor);
        produto.setAtivo(true);
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        System.out.println("🗑️ ProdutoService.deletar() - ID: " + id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        System.out.println("   Produto: " + produto.getNome());
        produto.setAtivo(false);
        produtoRepository.save(produto);
        System.out.println("   ✅ Produto desativado");
    }

    @Transactional
    public void deletarDoVendedor(Long produtoId, Long vendedorId) {
        System.out.println("🗑️ ProdutoService.deletarDoVendedor() - Produto: " + produtoId + ", Vendedor: " + vendedorId);
        
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new RuntimeException("Este produto não pertence ao vendedor");
        }
        
        produto.setAtivo(false);
        produtoRepository.save(produto);
        System.out.println("   ✅ Produto desativado pelo vendedor");
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAllAtivos();
    }

    public List<Produto> listarPorVendedor(Long vendedorId) {
        return produtoRepository.findByVendedorId(vendedorId);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> filtrar(String categoria, String tamanho, String cor, 
                                 BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.filtrarProdutos(categoria, tamanho, cor, precoMin, precoMax);
    }

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