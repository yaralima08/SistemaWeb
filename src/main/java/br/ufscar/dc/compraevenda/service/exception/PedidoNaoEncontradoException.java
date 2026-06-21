package br.ufscar.dc.compraevenda.service.exception;

/**
 * Exceção de domínio para pedido não encontrado.
 */
public class PedidoNaoEncontradoException extends RuntimeException {

    /**
     * Key do i18n em messages*.properties.
     * (fallback para error.general caso não exista no i18n)
     */
    public static final String MESSAGE_KEY = "error.general";

    public PedidoNaoEncontradoException() {
        super(MESSAGE_KEY);
    }

    public PedidoNaoEncontradoException(String message) {
        super(message);
    }
}


