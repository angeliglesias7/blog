/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.usc.etse.aos.model;

import org.springframework.data.mongodb.core.mapping.Document;
import gal.usc.etse.aos.model.auth.CredencialesPublicacion;
import org.springframework.data.annotation.Id;

/**
 *
 * @author angel
 */
@Document(collection = "publicaciones")
public class Publicacion extends CredencialesPublicacion {

    @Id
    private String id;
    private String username;
    private String fechaPublicacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfechaPublicacion() {
        return fechaPublicacion;
    }

    public void setfechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

}

