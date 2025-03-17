package fraga.com.example.gerenciamento_pedido.exception;

import org.springframework.http.HttpStatus;


public class BusinessException extends HandlerException {

    private static final long serialVersionUID = 1L;

    private static final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public BusinessException(String mensagem) {
        super(mensagem, statusCode);
    }

}
