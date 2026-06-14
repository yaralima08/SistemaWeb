package br.ufscar.dc.dsw.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter simples para formulários Thymeleaf.
 */
public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Long convert(String source) {
        if (source == null) {
            return null;
        }
        String s = source.trim();
        if (s.isEmpty()) {
            return null;
        }
        return Long.valueOf(s);
    }
}

