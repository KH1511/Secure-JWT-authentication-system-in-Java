package ma.enset.jwt_demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMS;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expiration
    ) {
        this.expirationMS = expiration;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    //*********Fonction pour generer le token
    public String generateToken(String username, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .setSubject(username)
                .compact();
    }



    //parseToken donne les claims (infos stokes dans payload)
    //methode va parser le token et extraire les donnees stockes dans le token
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    //
    public String extractUsername(String token) {
        Claims claims = parse(token).getBody();
        return claims.getSubject();
    }

    private boolean isExpired (String token) {
        return parse(token).getBody().getExpiration().before(new Date());
    }

    //********fonction pour verifier la validite d'un token en comparant username extrait a partir du token et passe en parametre
    public boolean isValid(String token, String username) {
        return extractUsername(token).equals(username);
    }



}
