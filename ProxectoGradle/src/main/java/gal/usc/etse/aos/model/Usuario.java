package gal.usc.etse.aos.model;

import gal.usc.etse.aos.model.auth.CredencialesUsuario;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "usuarios")
public class Usuario extends CredencialesUsuario{
    private String nombre;
    private String email;
    private String rol;
    private String fechaRegistro;
    private boolean bloqueado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }    

    public String getRol() {
        return rol;
    }

    public Usuario setRol(String rol) {
        this.rol = rol;
        return this;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public Usuario setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }   

    public boolean isBloqueado() {
        return bloqueado;
    }

    public Usuario setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
        return this;
    }

}


