package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HandlerException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus statusCode = HttpStatus.NOT_FOUND;

    public NotFoundException(String mensagem) {
        super(mensagem, statusCode);
    }

}
