package br.ufscar.dc.compraevenda.controller.api;

import br.ufscar.dc.compraevenda.model.Produto;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.service.ProdutoService;
import br.ufscar.dc.compraevenda.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendedor")
@RequiredArgsConstructor
public class VendedorRestController {

    private final ProdutoService produtoService;
    private final VendedorService vendedorService;

    @GetMapping("/produtos")
    public ResponseEntity<?> listarProdutos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        try {
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            List<Produto> produtos = produtoService.listarPorVendedor(vendedor.getId());
            return ResponseEntity.ok(produtos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar produtos: " + e.getMessage());
        }
    }

    @PostMapping("/produtos")
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        try {
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            Produto salvo = produtoService.salvar(produto, vendedor.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar produto: " + e.getMessage());
        }
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        try {
            String email = authentication.getName();
            Vendedor vendedor = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            Produto existente = produtoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            if (!existente.getVendedor().getId().equals(vendedor.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Este produto não pertence a este vendedor.");
            }
            
            produto.setId(id);
            produto.setVendedor(vendedor);
            Produto atualizado = produtoService.atualizar(produto);
            return ResponseEntity.ok(atualizado);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado.");
        }

        try {
            String email = authentication.getName();
            Vendedor vendedorLogado = vendedorService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            
            Produto produto = produtoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            if (!produto.getVendedor().getId().equals(vendedorLogado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Este produto não pertence a este vendedor.");
            }
            
            produtoService.deletar(id);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar produto: " + e.getMessage());
        }
    }
}
