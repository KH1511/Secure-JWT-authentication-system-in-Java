**Structure du projet JWT**

Le projet est organisé en packages logiques suivant l'architecture MVC typique de Spring Boot avec une couche de sécurité JWT. Voici la description de chaque package et de ses classes principales :

<img width="1747" height="862" alt="Image" src="https://github.com/user-attachments/assets/b0b121c2-f1dc-4add-9055-e9fe1df2eeed" />

1. Package ma.enset.jwt_demo.config
Contient la classe SecurityConfig
Cette classe est la configuration principale de Spring Security. Elle définit la stratégie de sécurité de l'application en configurant la chaîne de filtres (SecurityFilterChain). Elle désactive la protection CSRF (inutile avec JWT en mode stateless), autorise l'accès public aux endpoints d'authentification (/api/auth/**), exige l'authentification pour toutes les autres requêtes, et intègre le filtre JWT personnalisé avant le filtre d'authentification standard. Elle expose également l'AuthenticationManager comme bean pour l'utiliser dans le contrôleur d'authentification.

2. Package ma.enset.jwt_demo.controller
Contient la classe AuthController
Ce contrôleur REST gère les endpoints d'authentification et les ressources protégées. L'endpoint /api/auth/login permet aux utilisateurs de s'authentifier avec leur nom d'utilisateur et mot de passe. Il utilise l'AuthenticationManager pour valider les identifiants, puis génère un token JWT via JwtService. L'endpoint /api/auth/hello est protégé et ne sera accessible qu'avec un token JWT valide, servant d'exemple de ressource sécurisée.

3. Package ma.enset.jwt_demo.filter
Contient la classe JwtAuthFilter
Ce filtre personnalisé (OncePerRequestFilter) intercepte chaque requête HTTP pour vérifier la présence et la validité du token JWT. Il extrait le token de l'en-tête Authorization, valide sa signature et sa date d'expiration, puis charge les détails de l'utilisateur via MyUserDetailsService. Si le token est valide, il authentifie l'utilisateur dans le contexte de sécurité de Spring Security, permettant ainsi l'accès aux ressources protégées.

4. Package ma.enset.jwt_demo.model
Contient AuthRequest et AuthResponse

AuthRequest : Un simple record (classe immuable) qui modélise les données de requête d'authentification, contenant les champs username et password.

AuthResponse : Un record qui représente la réponse après une authentification réussie, contenant uniquement le token JWT généré.

5. Package ma.enset.jwt_demo.service
Contient JwtService et MyUserDetailsService

JwtService : Service responsable de toute la logique liée aux tokens JWT. Il génère des tokens signés avec une clé secrète, extrait le nom d'utilisateur, vérifie la validité et l'expiration des tokens.

MyUserDetailsService : Implémente UserDetailsService de Spring Security pour charger les utilisateurs. Dans cette version, il utilise un gestionnaire en mémoire (InMemoryUserDetailsManager) avec deux utilisateurs prédéfinis (USER et ADMIN). Cette classe peut être modifiée pour intégrer une base de données réelle.

