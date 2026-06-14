package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Pedido;
import br.ufscar.dc.dsw.domain.Roupa;
import br.ufscar.dc.dsw.service.ClienteService;
import br.ufscar.dc.dsw.service.PedidoService;
import br.ufscar.dc.dsw.service.RoupaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoMvcController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final RoupaService roupaService;

    public PedidoMvcController(PedidoService pedidoService, ClienteService clienteService, RoupaService roupaService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.roupaService = roupaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listar());
        return "pedidos/lista";
    }

    @GetMapping("/cadastrar")
    public String formularioCadastro(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("roupas", roupaService.listar());
        return "pedidos/formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("pedido") Pedido pedido,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", clienteService.listar());
            model.addAttribute("roupas", roupaService.listar());
            return "pedidos/formulario";
        }

        pedidoService.salvar(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Pedido> opt = pedidoService.buscarPorId(id);
        if (opt.isEmpty()) {
            return "redirect:/pedidos";
        }

        model.addAttribute("pedido", opt.get());
        model.addAttribute("clientes", clienteService.listar());
        model.addAttribute("roupas", roupaService.listar());
        return "pedidos/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                           @Valid @ModelAttribute("pedido") Pedido pedido,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", clienteService.listar());
            model.addAttribute("roupas", roupaService.listar());
            return "pedidos/formulario";
        }

        if (pedidoService.buscarPorId(id).isPresent()) {
            pedidoService.excluirPorId(id);
        }

        pedidoService.salvar(pedido);
        return "redirect:/pedidos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        if (pedidoService.buscarPorId(id).isPresent()) {
            pedidoService.excluirPorId(id);
        }
        return "redirect:/pedidos";
    }

    @ModelAttribute("cliente")
    public Cliente cliente() {
        return new Cliente();
    }

    @ModelAttribute("roupa")
    public Roupa roupa() {
        return new Roupa();
    }
}

