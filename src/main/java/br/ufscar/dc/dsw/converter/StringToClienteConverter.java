package br.ufscar.dc.dsw.converter;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.service.ClienteService;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class StringToClienteConverter implements Converter<String, Cliente> {

    private final ClienteService clienteService;

    public StringToClienteConverter(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public Cliente convert(String source) {
        if (source == null) {
            return null;
        }
        String s = source.trim();
        if (s.isEmpty()) {
            return null;
        }

        Long id = Long.valueOf(s);
        Optional<Cliente> opt = clienteService.buscarPorId(id);
        // Retornar null para que a validação (@NotNull em Pedido) trate o erro via BindingResult
        return opt.orElse(null);

    }
}

