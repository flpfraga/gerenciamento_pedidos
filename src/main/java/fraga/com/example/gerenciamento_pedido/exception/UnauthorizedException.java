package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;


public class UnauthorizedException extends HandlerException {

    private static final long serialVersionUID = 1L;

    private static final HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String mensagem) {
        super(mensagem, statusCode);
    }

}
