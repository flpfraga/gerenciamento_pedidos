package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;


public class ForbiddenException extends HandlerException {

    private static final long serialVersionUID = 1L;

    private static final HttpStatus statusCode = HttpStatus.FORBIDDEN;

    public ForbiddenException(String mensagem) {
        super(mensagem, statusCode);
    }

}
