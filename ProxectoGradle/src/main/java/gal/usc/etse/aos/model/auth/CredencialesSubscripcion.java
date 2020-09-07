package gal.usc.etse.aos.model.auth;

import org.springframework.data.annotation.Id;

public class CredencialesSubscripcion {
    
    @Id
    private String subscripcion;
    private String tipo;

    public String getSubscripcion() {
        return subscripcion;
    }

    public CredencialesSubscripcion setSubscripcion(String subscripcion) {
        this.subscripcion = subscripcion;
        return this;
    }

    public String getTipo() {
        return tipo;
    }

    public CredencialesSubscripcion setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }   

}
