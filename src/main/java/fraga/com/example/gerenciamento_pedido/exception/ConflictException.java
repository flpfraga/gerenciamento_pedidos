package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;


public class ConflictException extends HandlerException {

    private static final long serialVersionUID = 1L;

    private static final HttpStatus statusCode = HttpStatus.CONFLICT;

    public ConflictException(String mensagem) {
        super(mensagem, statusCode);
    }

}
