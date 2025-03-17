package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends HandlerException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus statusCode = HttpStatus.FORBIDDEN;

    public AuthorizationException(String mensagem) {
        super(mensagem, statusCode);
    }

}
