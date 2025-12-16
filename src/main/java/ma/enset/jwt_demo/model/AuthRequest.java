package ma.enset.jwt_demo.model;

import java.io.Serializable;

public record AuthRequest(String username, String password ) implements Serializable {

}
