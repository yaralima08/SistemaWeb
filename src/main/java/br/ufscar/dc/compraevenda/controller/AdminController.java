package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final ClienteService clienteService;
    private final VendedorService vendedorService;
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
    
    // Clientes CRUD
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "admin/clientes";
    }
    
    @GetMapping("/clientes/novo")
    public String novoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "admin/cliente-form";
    }
    
    @PostMapping("/clientes/salvar")
    public String salvarCliente(@Valid @ModelAttribute Cliente cliente, 
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/cliente-form";
        }
        try {
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("success", "cliente.realizado-sucesso");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.general");
            return "admin/cliente-form";
        }

        return "redirect:/admin/clientes";
    }
    
    @GetMapping("/clientes/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        clienteService.buscarPorId(id).ifPresent(cliente -> 
            model.addAttribute("cliente", cliente));
        return "admin/cliente-form";
    }
    
    @PostMapping("/clientes/atualizar")
    public String atualizarCliente(@Valid @ModelAttribute Cliente cliente,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/cliente-form";
        }
        try {
            clienteService.atualizar(cliente);
            redirectAttributes.addFlashAttribute("success", "cliente.atualizado-sucesso");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.general");
            return "admin/cliente-form";
        }

        return "redirect:/admin/clientes";
    }
    
    @PostMapping("/clientes/deletar/{id}")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "cliente.deletado-sucesso");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.general");
        }
        return "redirect:/admin/clientes";

    }
    
    // Vendedores CRUD
    @GetMapping("/vendedores")
    public String listarVendedores(Model model) {
        model.addAttribute("vendedores", vendedorService.listarTodos());
        return "admin/vendedores";
    }
    
    @GetMapping("/vendedores/novo")
    public String novoVendedor(Model model) {
        model.addAttribute("vendedor", new Vendedor());
        return "admin/vendedor-form";
    }
    
    @PostMapping("/vendedores/salvar")
    public String salvarVendedor(@Valid @ModelAttribute Vendedor vendedor,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/vendedor-form";
        }
        try {
            vendedorService.salvar(vendedor);
            redirectAttributes.addFlashAttribute("success", "vendedor.realizado-sucesso");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.general");
            return "admin/vendedor-form";
        }

        return "redirect:/admin/vendedores";
    }
    
    @GetMapping("/vendedores/editar/{id}")
    public String editarVendedor(@PathVariable Long id, Model model) {
        vendedorService.buscarPorId(id).ifPresent(vendedor -> 
            model.addAttribute("vendedor", vendedor));
        return "admin/vendedor-form";
    }
    
    @PostMapping("/vendedores/deletar/{id}")
    public String deletarVendedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vendedorService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Vendedor deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error.general");

        }
        return "redirect:/admin/vendedores";
    }
}