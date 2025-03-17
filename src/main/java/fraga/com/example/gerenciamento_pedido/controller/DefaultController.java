package fraga.com.example.gerenciamento_pedido.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DefaultController {

    default <T extends Object> ResponseEntity<DefaultResponse<T>> retornarResponse(final HttpStatus httpStatus,
                                                                                   final T response) {
        return ResponseEntity.status(httpStatus.value()).body(new DefaultResponse<>(httpStatus.value(), response));
    }

    /*
     * HTTP 200
     */
    default <T extends Object> ResponseEntity<DefaultResponse<T>> success(final T response) {
        return retornarResponse(HttpStatus.OK, response);
    }

}