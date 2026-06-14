package br.ufscar.dc.dsw.converter;

import br.ufscar.dc.dsw.domain.Vendedor;
import br.ufscar.dc.dsw.service.VendedorService;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class StringToVendedorConverter implements Converter<String, Vendedor> {

    private final VendedorService vendedorService;

    public StringToVendedorConverter(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @Override
    public Vendedor convert(String source) {
        if (source == null) {
            return null;
        }
        String s = source.trim();
        if (s.isEmpty()) {
            return null;
        }

        Long id = Long.valueOf(s);
        Optional<Vendedor> opt = vendedorService.buscarPorId(id);
        return opt.orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado para id=" + id));
    }
}

