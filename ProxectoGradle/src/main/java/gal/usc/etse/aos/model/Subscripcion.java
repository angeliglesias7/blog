/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.usc.etse.aos.model;

import gal.usc.etse.aos.model.auth.CredencialesSubscripcion;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author angel
 */


@Document(collection = "subscripcion")
public class Subscripcion extends CredencialesSubscripcion {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
