package br.ufscar.dc.compraevenda.service.exception;

/**
 * Exceção de domínio para quando o cliente não possui endereço de entrega configurado.
 */
public class EnderecoNaoEncontradoException extends RuntimeException {

    /**
     * Key do i18n em messages*.properties.
     */
    public static final String MESSAGE_KEY = "pedido.endereco-nao-encontrado";

    public EnderecoNaoEncontradoException() {
        super(MESSAGE_KEY);
    }

    public EnderecoNaoEncontradoException(String message) {
        super(message);
    }
}


