package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.domain.Vendedor;
import br.ufscar.dc.dsw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/vendedores")
public class VendedorMvcController {

    private final VendedorService vendedorService;

    public VendedorMvcController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendedores", vendedorService.listar());
        return "vendedor/lista";
    }

    @GetMapping("/cadastrar")
    public String formularioCadastro(Model model) {
        model.addAttribute("vendedor", new Vendedor());
        model.addAttribute("modo", "cadastro");
        return "vendedor/formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("vendedor") Vendedor vendedor,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modo", "cadastro");
            return "vendedor/formulario";
        }

        vendedorService.salvar(vendedor);
        return "redirect:/vendedores";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Vendedor> opt = vendedorService.buscarPorId(id);
        if (opt.isEmpty()) {
            return "redirect:/vendedores";
        }

        model.addAttribute("vendedor", opt.get());
        model.addAttribute("modo", "edicao");
        return "vendedor/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                           @Valid @ModelAttribute("vendedor") Vendedor vendedor,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modo", "edicao");
            return "vendedor/formulario";
        }

        if (vendedorService.buscarPorId(id).isPresent()) {
            vendedorService.excluirPorId(id);
        }

        vendedorService.salvar(vendedor);
        return "redirect:/vendedores";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        if (vendedorService.buscarPorId(id).isPresent()) {
            vendedorService.excluirPorId(id);
        }
        return "redirect:/vendedores";
    }
}

