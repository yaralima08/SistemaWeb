package br.ufscar.dc.dsw.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) return true; // @NotBlank/@NotNull cuidam da presença

        String normalized = cpf.replaceAll("\\D", "");
        if (normalized.length() != 11) return false;

        // rejeita CPFs com todos os dígitos iguais
        if (normalized.chars().distinct().count() == 1) return false;

        try {
            int d1 = calculateDigit(normalized, 9);
            int d2 = calculateDigit(normalized, 10);
            return normalized.charAt(9) - '0' == d1 && normalized.charAt(10) - '0' == d2;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private int calculateDigit(String cpfDigits, int position) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            int digit = cpfDigits.charAt(i) - '0';
            sum += digit * (position + 1 - i);
        }
        int mod = sum % 11;
        return (mod < 2) ? 0 : 11 - mod;
    }
}

