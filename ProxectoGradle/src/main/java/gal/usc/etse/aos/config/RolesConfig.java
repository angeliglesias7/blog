package gal.usc.etse.aos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RolesConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
                "ROLE_Administrador > ROLE_Redactor "
                        + "ROLE_Redactor > ROLE_Moderador "
                        + "ROLE_Moderador > ROLE_Lector"
        );
        return hierarchy;
    }
}
