package fraga.com.example.gerenciamento_pedido.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class TokenService {
    @Value("${jwt.secret-key.token}")
    private String secret;

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
            log.info("m=generateToken user={}", user);
            return token;
        } catch (JWTCreationException exception) {
            log.error("m=generateToken user={} jwtCreationException={}", user, exception.getMessage());
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
            log.info("m=generateToken token={}", token);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            log.error("m=validateToken token={} jwtCreationException={}", token, exception.getMessage());
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
