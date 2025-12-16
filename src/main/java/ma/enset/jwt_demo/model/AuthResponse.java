package ma.enset.jwt_demo.model;

import java.io.Serializable;

public record AuthResponse(String token) implements Serializable {
}

