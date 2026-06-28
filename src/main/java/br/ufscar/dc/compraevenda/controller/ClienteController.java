package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Pedido;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        List<Pedido> pedidos = pedidoService.listarPorCliente(cliente.getId());
        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidos);
        
        return "cliente/dashboard";
    }

    // ===== MÉTODO QUE JÁ EXISTE =====
    @GetMapping("/pedidos")
    public String listarPedidos(Authentication authentication, Model model) {
        String email = authentication.getName();
        Cliente cliente = clienteService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        List<Pedido> pedidos = pedidoService.listarPorCliente(cliente.getId());
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("cliente", cliente);
        
        return "cliente/pedidos";
    }
}