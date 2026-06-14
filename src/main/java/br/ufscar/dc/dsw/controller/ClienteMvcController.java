package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteMvcController {

    private final ClienteService clienteService;

    public ClienteMvcController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listar());
        return "clientes/lista";
    }

    @GetMapping("/cadastrar")
    public String formularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("modo", "cadastro");
        return "clientes/formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("cliente") Cliente cliente,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modo", "cadastro");
            return "clientes/formulario";
        }

        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String formularioEdicao(@PathVariable Long id, Model model) {
        Optional<Cliente> opt = clienteService.buscarPorId(id);
        if (opt.isEmpty()) {
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", opt.get());
        model.addAttribute("modo", "edicao");
        return "clientes/formulario";
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                           @Valid @ModelAttribute("cliente") Cliente cliente,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modo", "edicao");
            return "clientes/formulario";
        }

        // implementação consistente com os outros controllers: remove se existir antes de salvar
        if (clienteService.buscarPorId(id).isPresent()) {
            clienteService.excluirPorId(id);
        }

        // como o formulário envia id escondido, a entidade pode manter/ser ignorada conforme o JPA.
        // Ao final, o service salvarPersistirá os dados.
        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        if (clienteService.buscarPorId(id).isPresent()) {
            clienteService.excluirPorId(id);
        }
        return "redirect:/clientes";
    }
}

