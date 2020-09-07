package gal.usc.etse.aos.model.auth;

import java.util.List;

public class CredencialesPublicacion {

    private String titulo;
    private String cuerpo;
    private String resumen;
    private List<String> palabrasClave;

    public String getTitulo() {
        return titulo;
    }

    public CredencialesPublicacion setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public CredencialesPublicacion setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
        return this;
    }

    public String getResumen() {
        return resumen;
    }

    public CredencialesPublicacion setResumen(String resumen) {
        this.resumen = resumen;
        return this;
    }

    public List<String> getPalabrasClave() {
        return palabrasClave;
    }

    public CredencialesPublicacion setPalabrasClave(List<String> palabrasClave) {
        this.palabrasClave = palabrasClave;
        return this;
    }
    
    

}
