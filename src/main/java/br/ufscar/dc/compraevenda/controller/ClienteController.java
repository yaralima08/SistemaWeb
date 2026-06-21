package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    private final PedidoService pedidoService;
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        clienteService.buscarPorEmail(email).ifPresent(cliente -> {
            model.addAttribute("cliente", cliente);
            model.addAttribute("pedidos", pedidoService.listarPorCliente(cliente.getId()));
        });
        return "cliente/dashboard";
    }
    
    @GetMapping("/pedidos")
    public String listarPedidos(Authentication authentication, Model model) {
        String email = authentication.getName();
        clienteService.buscarPorEmail(email).ifPresent(cliente -> {
            model.addAttribute("pedidos", pedidoService.listarPorCliente(cliente.getId()));
        });
        return "cliente/pedidos";
    }
    
    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {
        String email = authentication.getName();
        clienteService.buscarPorEmail(email).ifPresent(cliente -> 
            model.addAttribute("cliente", cliente));
        return "cliente/perfil";
    }
}