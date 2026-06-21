package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendedorService {
    
    private final VendedorRepository vendedorRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public Vendedor salvar(Vendedor vendedor) {
        System.out.println("📝 Salvando vendedor: " + vendedor.getEmail());
        
        if (vendedorRepository.existsByEmail(vendedor.getEmail())) {
            throw new RuntimeException("Email já cadastrado. Por favor, use outro email.");
        }
        if (vendedorRepository.existsByCnpj(vendedor.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado. Por favor, verifique seus dados.");
        }
        
        vendedor.setSenha(passwordEncoder.encode(vendedor.getSenha()));
        vendedor.setAtivo(true);
        
        Vendedor salvo = vendedorRepository.save(vendedor);
        System.out.println("✅ Vendedor salvo com sucesso! ID: " + salvo.getId());
        
        return salvo;
    }
    
    @Transactional
    public Vendedor atualizar(Vendedor vendedor) {
        Vendedor existente = vendedorRepository.findById(vendedor.getId())
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        
        if (!existente.getEmail().equals(vendedor.getEmail()) && 
            vendedorRepository.existsByEmail(vendedor.getEmail())) {
            throw new RuntimeException("Email já cadastrado por outro usuário.");
        }
        
        existente.setNomeLoja(vendedor.getNomeLoja());
        existente.setDescricao(vendedor.getDescricao());
        existente.setEndereco(vendedor.getEndereco());
        
        if (vendedor.getSenha() != null && !vendedor.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(vendedor.getSenha()));
        }
        
        return vendedorRepository.save(existente);
    }
    
    @Transactional
    public void deletar(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
        vendedor.setAtivo(false);
        vendedorRepository.save(vendedor);
    }
    
    public List<Vendedor> listarTodos() {
        return vendedorRepository.findAll();
    }
    
    public Optional<Vendedor> buscarPorId(Long id) {
        return vendedorRepository.findById(id);
    }
    
    public Optional<Vendedor> buscarPorEmail(String email) {
        return vendedorRepository.findByEmail(email);
    }
}