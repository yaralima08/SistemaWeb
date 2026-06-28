package br.ufscar.dc.compraevenda.service;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.repository.ClienteRepository;
import br.ufscar.dc.compraevenda.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuário: " + email);

        // 1. Buscar como Cliente
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            System.out.println("✅ Cliente encontrado: " + cliente.getEmail());
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
            
            // Se for admin (email específico)
            if ("admin@stylestore.com".equals(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                System.out.println("   ➡️ ROLE_ADMIN adicionada");
            }
            
            System.out.println("   📋 Autoridades: " + authorities);
            
            return new User(cliente.getEmail(), cliente.getSenha(), authorities);
        }

        // 2. Buscar como Vendedor
        Optional<Vendedor> vendedorOpt = vendedorRepository.findByEmail(email);
        if (vendedorOpt.isPresent()) {
            Vendedor vendedor = vendedorOpt.get();
            System.out.println("✅ Vendedor encontrado: " + vendedor.getEmail());
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_VENDEDOR"));
            
            System.out.println("   📋 Autoridades: " + authorities);
            
            return new User(vendedor.getEmail(), vendedor.getSenha(), authorities);
        }

        System.out.println("❌ Usuário NÃO encontrado: " + email);
        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }
}