package br.ufscar.dc.compraevenda.controller.api;

import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoRestController {

    private final ProdutoService produtoService;

    // ===== LISTAR TODOS OS PRODUTOS (PÚBLICO) =====
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // ===== BUSCAR PRODUTO POR ID (PÚBLICO) =====
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== FILTRAR PRODUTOS (PÚBLICO) =====
    @GetMapping("/filtrar")
    public ResponseEntity<List<Produto>> filtrar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String tamanho,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax) {
        return ResponseEntity.ok(produtoService.filtrar(categoria, tamanho, cor, precoMin, precoMax));
    }
}
