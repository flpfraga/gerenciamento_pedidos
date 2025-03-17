package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizationService implements UserDetailsService {

        UserRepository repository;

        /**
         * Carrega um usuário pelo nome de usuário.
         * 
         * @param username Nome de usuário a ser buscado
         * @return UserDetails contendo os dados do usuário encontrado
         * @throws UsernameNotFoundException se o usuário não for encontrado
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return repository.findByLogin(username);
        }
}
