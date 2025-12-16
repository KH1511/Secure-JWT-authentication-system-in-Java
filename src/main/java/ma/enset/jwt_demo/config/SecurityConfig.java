package ma.enset.jwt_demo.config;

import ma.enset.jwt_demo.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuration pour définir la stratégie de sécurité de l'application.
 */
@Configuration
@EnableWebSecurity // Active la sécurité web de Spring Security
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Injection de notre filtre JWT personnalisé via le constructeur
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Définit la chaîne de filtres de sécurité (Security Filter Chain).
     * C'est ici qu'on définit qui peut accéder à quoi.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactivation du CSRF (Cross-Site Request Forgery)
                // Comme on utilise des tokens JWT, on n'a plus besoin de cette protection basée sur les sessions/cookies.
                //mode stateless : pas de probleme de csrf
                .csrf(csrf -> csrf.disable())

                // 2. Gestion des autorisations sur les requêtes HTTP
                .authorizeHttpRequests(reg -> reg
                        // Autorise tout le monde à accéder aux endpoints commençant par /api/auth/
                        // Utile pour les endpoints "login" ou "register"
                        .requestMatchers("/api/auth/**").permitAll()

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // 3. Ajout de notre filtre personnalisé JWT
                // On place 'jwtAuthFilter' AVANT le filtre standard de Spring (UsernamePasswordAuthenticationFilter).
                // Cela permet de vérifier le token JWT avant que Spring ne tente une authentification classique.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expose l'AuthenticationManager en tant que Bean pour qu'il puisse être utilisé
     * ailleurs dans l'application (notamment dans le contrôleur d'authentification).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}