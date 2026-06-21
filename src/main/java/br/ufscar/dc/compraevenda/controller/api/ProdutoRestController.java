package br.ufscar.dc.compraevenda.api;

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

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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