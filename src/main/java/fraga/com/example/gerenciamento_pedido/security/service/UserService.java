package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.exception.ConflictException;
import fraga.com.example.gerenciamento_pedido.exception.NotFoundException;
import fraga.com.example.gerenciamento_pedido.exception.UnauthorizedException;
import fraga.com.example.gerenciamento_pedido.security.controller.AuthenticationRequest;
import fraga.com.example.gerenciamento_pedido.security.controller.LoginResponse;
import fraga.com.example.gerenciamento_pedido.security.controller.RegisterRequest;
import fraga.com.example.gerenciamento_pedido.security.controller.RegisterResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    /**
     * Realiza o login do usuário.
     * 
     * @param data Dados de autenticação (login e senha)
     * @return LoginResponse contendo o token JWT gerado
     * @throws UnauthorizedException se as credenciais forem inválidas ou usuário inativo
     * @throws NotFoundException se o usuário não for encontrado
     */
    @Transactional
    public LoginResponse login(AuthenticationRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = getAuthentication(usernamePassword);

        User user = (User) auth.getPrincipal();
        isActive(user);
        var token = tokenService.generateToken(user);
        return new LoginResponse(token);
    }

    private void isActive(User user){
        if (!user.isActive()){
            throw new UnauthorizedException("Usuário não autorizado.");
        }
    }

    /**
     * Obtém os detalhes do usuário atualmente autenticado.
     * 
     * @return UserDetails do usuário autenticado
     * @throws UnauthorizedException se não houver usuário autenticado
     */
    public UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal());
        }

        throw new UnauthorizedException("Usuário não autenticado");
    }

    private Authentication getAuthentication(UsernamePasswordAuthenticationToken usernamePassword) {
        try {
            return authenticationManager.authenticate(usernamePassword);
        } catch (InternalAuthenticationServiceException e) {
            throw new NotFoundException("Usuário não cadastrado");
        } catch (BadCredentialsException e){
            throw new UnauthorizedException("Usuário/Senha inválidos");
        }
    }

    /**
     * Constrói uma entidade User com base nos dados do RegisterRequest.
     * 
     * @param registerRequest Dados para criação do usuário
     * @return User entidade construída
     * @throws BusinessException se a role não for encontrada
     * @throws ConflictException se o usuário já estiver cadastrado
     */
    public User buildUser(RegisterRequest registerRequest){
        Set<Role> roles = getUserRoles(registerRequest);
        validAlreadyRegisterUser(registerRequest.login());
        return generatedUser(registerRequest, roles);
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * @param registerRequest Dados para criação do usuário
     * @return RegisterResponse com os dados do usuário registrado
     * @throws BusinessException se a role não for encontrada
     * @throws ConflictException se o usuário já estiver cadastrado
     */
    @Transactional
    public RegisterResponse register(@Valid RegisterRequest registerRequest) {
        User newUser = buildUser(registerRequest);
        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser, RegisterResponse.class);
    }

    private Set<Role> getUserRoles(RegisterRequest registerRequest) {
        Set<Role> roles = registerRequest.roles().stream()
                .map(roleEnum -> roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new BusinessException(("Role não encontrada."))))
                .collect(Collectors.toSet());
        return roles;
    }

    private static User generatedUser(RegisterRequest registerRequest, Set<Role> roles) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());
        return new User(registerRequest.login(), encryptedPassword, roles);
    }

    private void validAlreadyRegisterUser(String login) {
        if (userRepository.findByLogin(login) != null) {
            throw new ConflictException("Usuário já cadastrado");
        }
    }
}
