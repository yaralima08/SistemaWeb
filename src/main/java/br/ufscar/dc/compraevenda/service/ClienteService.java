package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public Cliente salvar(Cliente cliente) {
        System.out.println("🔍 Verificando email: " + cliente.getEmail());
        
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já cadastrado. Por favor, use outro email.");
        }
        
        System.out.println("🔍 Verificando CPF: " + cliente.getCpf());
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("CPF já cadastrado. Por favor, verifique seus dados.");
        }
        
        // Criptografar senha
        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);
        cliente.setAtivo(true);
        
        System.out.println("💾 Salvando cliente...");
        Cliente salvo = clienteRepository.save(cliente);
        System.out.println("✅ Cliente salvo: ID=" + salvo.getId() + ", Email=" + salvo.getEmail());
        
        return salvo;
    }
    
    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Cliente existente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        if (!existente.getEmail().equals(cliente.getEmail()) && 
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já cadastrado por outro usuário.");
        }
        
        existente.setNome(cliente.getNome());
        existente.setTelefone(cliente.getTelefone());
        existente.setSexo(cliente.getSexo());
        existente.setDataNascimento(cliente.getDataNascimento());
        existente.setEndereco(cliente.getEndereco());
        
        if (cliente.getSenha() != null && !cliente.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }
        
        return clienteRepository.save(existente);
    }
    
    @Transactional
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}