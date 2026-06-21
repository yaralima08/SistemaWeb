package br.ufscar.dc.compraevenda.service.exception;

/**
 * Exceção de domínio para produto não encontrado.
 */
public class ProdutoNaoEncontradoException extends RuntimeException {

    /**
     * Key do i18n em messages*.properties.
     * (fallback para error.general caso não exista no i18n)
     */
    public static final String MESSAGE_KEY = "error.general";

    public ProdutoNaoEncontradoException() {
        super(MESSAGE_KEY);
    }

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}


