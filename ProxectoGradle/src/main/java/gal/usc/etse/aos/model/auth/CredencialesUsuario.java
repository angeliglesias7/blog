package gal.usc.etse.aos.model.auth;

import org.springframework.data.annotation.Id;

public class CredencialesUsuario {

    @Id
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public CredencialesUsuario setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CredencialesUsuario setPassword(String password) {
        this.password = password;
        return this;
    }
    
    

    

}
