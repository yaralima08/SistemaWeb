package br.ufscar.dc.compraevenda.api;

import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/vendedores")
@RequiredArgsConstructor
public class VendedorRestController {

    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<List<Vendedor>> listarTodos() {
        return ResponseEntity.ok(vendedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> buscarPorId(@PathVariable Long id) {
        return vendedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vendedor> criar(@Valid @RequestBody Vendedor vendedor) {
        return ResponseEntity.ok(vendedorService.salvar(vendedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> atualizar(@PathVariable Long id, @Valid @RequestBody Vendedor vendedor) {
        vendedor.setId(id);
        return ResponseEntity.ok(vendedorService.atualizar(vendedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vendedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}