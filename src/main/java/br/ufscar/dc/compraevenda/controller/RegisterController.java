package br.ufscar.dc.compraevenda.controller;

import br.ufscar.dc.compraevenda.model.Cliente;
import br.ufscar.dc.compraevenda.model.Endereco;
import br.ufscar.dc.compraevenda.model.Vendedor;
import br.ufscar.dc.compraevenda.service.ClienteService;
import br.ufscar.dc.compraevenda.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final ClienteService clienteService;
    private final VendedorService vendedorService;

    @GetMapping("/cliente")
    public String registerCliente(Model model) {
        if (!model.containsAttribute("cliente")) {
            model.addAttribute("cliente", new Cliente());
        }
        if (!model.containsAttribute("endereco")) {
            model.addAttribute("endereco", new Endereco());
        }
        return "register";
    }

    @GetMapping("/vendedor")
    public String registerVendedor(Model model) {
        if (!model.containsAttribute("vendedor")) {
            model.addAttribute("vendedor", new Vendedor());
        }
        if (!model.containsAttribute("endereco")) {
            model.addAttribute("endereco", new Endereco());
        }
        return "register-vendedor";
    }

    @PostMapping("/cliente/salvar")
    public String salvarCliente(
            @ModelAttribute Cliente cliente,
            @ModelAttribute Endereco endereco,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("📝 Cadastrando cliente: " + cliente.getEmail());
        System.out.println("   Nome: " + cliente.getNome());
        System.out.println("   CPF: " + cliente.getCpf());
        
        try {
            cliente.setEndereco(endereco);
            Cliente salvo = clienteService.salvar(cliente);
            System.out.println("✅ Cliente salvo com ID: " + salvo.getId());
            redirectAttributes.addFlashAttribute("success", "✅ Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            redirectAttributes.addFlashAttribute("cliente", cliente);
            redirectAttributes.addFlashAttribute("endereco", endereco);
            return "redirect:/register/cliente";
        }
    }

    @PostMapping("/vendedor/salvar")
    public String salvarVendedor(
            @ModelAttribute Vendedor vendedor,
            @ModelAttribute Endereco endereco,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("📝 Cadastrando vendedor: " + vendedor.getEmail());
        System.out.println("   Nome da Loja: " + vendedor.getNomeLoja());
        System.out.println("   CNPJ: " + vendedor.getCnpj());
        
        try {
            vendedor.setEndereco(endereco);
            Vendedor salvo = vendedorService.salvar(vendedor);
            System.out.println("✅ Vendedor salvo com ID: " + salvo.getId());
            redirectAttributes.addFlashAttribute("success", "✅ Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar vendedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            redirectAttributes.addFlashAttribute("vendedor", vendedor);
            redirectAttributes.addFlashAttribute("endereco", endereco);
            return "redirect:/register/vendedor";
        }
    }
}