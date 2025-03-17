package fraga.com.example.gerenciamento_pedido.security.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EUserRole {
    ADMIN ("ADMIN"),
    USER ("USER");

    private final String role;

    EUserRole(String role){
        this.role = role;
    }

    public static List<String> getAllRoles() {
        return Arrays.stream(EUserRole.values())
                .map(EUserRole::getRole)
                .collect(Collectors.toList());
    }
}
