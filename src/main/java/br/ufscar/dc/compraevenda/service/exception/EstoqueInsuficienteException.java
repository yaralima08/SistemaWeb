package br.ufscar.dc.compraevenda.service.exception;

/**
 * Exceção de domínio para cenários em que não há estoque suficiente para completar a compra.
 */
public class EstoqueInsuficienteException extends RuntimeException {

    /**
     * Key do i18n em messages*.properties.
     */
    public static final String MESSAGE_KEY = "pedido.estoque-insuficiente";

    public EstoqueInsuficienteException() {
        super(MESSAGE_KEY);
    }

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}



