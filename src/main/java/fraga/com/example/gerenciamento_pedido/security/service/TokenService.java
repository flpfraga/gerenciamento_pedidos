package fraga.com.example.gerenciamento_pedido.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final String secret = "qt5DRbuVnHHlpHmocTb0AJL9BsyQPpo1edUexquLo_s92FQqacCcw5MjAfY7LtBz4ri0qvyAGQ1bUsTzXLo6fw";

    /**
     * Gera um token JWT para o usuário.
     * 
     * @param user Usuário para o qual o token será gerado
     * @return Token JWT gerado
     * @throws RuntimeException se houver erro na geração do token
     */
    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    /**
     * Valida um token JWT.
     * 
     * @param token Token JWT a ser validado
     * @return Login do usuário associado ao token se válido, string vazia caso contrário
     */
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
