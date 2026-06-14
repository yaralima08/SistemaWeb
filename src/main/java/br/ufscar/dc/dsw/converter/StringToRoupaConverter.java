package br.ufscar.dc.dsw.converter;

import br.ufscar.dc.dsw.domain.Roupa;
import br.ufscar.dc.dsw.service.RoupaService;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class StringToRoupaConverter implements Converter<String, Roupa> {

    private final RoupaService roupaService;

    public StringToRoupaConverter(RoupaService roupaService) {
        this.roupaService = roupaService;
    }

    @Override
    public Roupa convert(String source) {
        if (source == null) {
            return null;
        }
        String s = source.trim();
        if (s.isEmpty()) {
            return null;
        }

        Long id = Long.valueOf(s);
        Optional<Roupa> opt = roupaService.buscarPorId(id);
        // Retornar null para que a validação (@NotNull em Pedido) trate o erro via BindingResult
        return opt.orElse(null);

    }
}

