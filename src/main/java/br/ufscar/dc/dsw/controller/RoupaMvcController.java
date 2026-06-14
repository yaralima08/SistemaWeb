package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.domain.Roupa;
import br.ufscar.dc.dsw.domain.Vendedor;
import br.ufscar.dc.dsw.service.RoupaService;
import br.ufscar.dc.dsw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/roupas")
public class RoupaMvcController {

    private final RoupaService roupaService;
    private final VendedorService vendedorService;

    public RoupaMvcController(RoupaService roupaService, VendedorService vendedorService) {
        this.roupaService = roupaService;
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roupas", roupaService.listar());
        return "roupas/lista";
    }

    @GetMapping("/cadastrar")
    public String formularioCadastro(Model model) {
        model.addAttribute("roupa", new Roupa());
        model.addAttribute("vendedores", vendedorService.listar());
        return "roupas/formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("roupa") Roupa roupa,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vendedores", vendedorService.listar());
            return "roupas/formulario";
        }

        roupaService.salvar(roupa);
        return "redirect:/roupas";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Roupa> opt = roupaService.buscarPorId(id);
        if (opt.isEmpty()) {
            return "redirect:/roupas";
        }

        model.addAttribute("roupa", opt.get());
        model.addAttribute("vendedores", vendedorService.listar());
        return "roupas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                           @Valid @ModelAttribute("roupa") Roupa roupa,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vendedores", vendedorService.listar());
            return "roupas/formulario";
        }

        if (roupaService.buscarPorId(id).isPresent()) {
            roupaService.excluirPorId(id);
        }

        roupaService.salvar(roupa);
        return "redirect:/roupas";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        if (roupaService.buscarPorId(id).isPresent()) {
            roupaService.excluirPorId(id);
        }
        return "redirect:/roupas";
    }

    @ModelAttribute("vendedor")
    public Vendedor vendedor() {
        return new Vendedor();
    }
}

