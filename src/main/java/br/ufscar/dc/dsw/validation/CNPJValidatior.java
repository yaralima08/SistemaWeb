package br.ufscar.dc.dsw.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public void initialize(CNPJ constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) return true; // @NotBlank/@NotNull cuidam da presença

        String normalized = cnpj.replaceAll("\\D", "");
        if (normalized.length() != 14) return false;

        // rejeita CNPJs com todos os dígitos iguais
        if (normalized.chars().distinct().count() == 1) return false;

        try {
            int d1 = calculateDigit(normalized, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            int d2 = calculateDigit(normalized, 13, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            return normalized.charAt(12) - '0' == d1 && normalized.charAt(13) - '0' == d2;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private int calculateDigit(String digits, int length, int[] weights) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            int digit = digits.charAt(i) - '0';
            sum += digit * weights[i];
        }
        int mod = sum % 11;
        return (mod < 2) ? 0 : 11 - mod;
    }
}

