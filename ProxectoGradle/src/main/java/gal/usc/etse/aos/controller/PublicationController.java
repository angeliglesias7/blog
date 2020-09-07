/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.usc.etse.aos.controller;

import gal.usc.etse.aos.model.Comentario;
import gal.usc.etse.aos.model.Usuario;
import gal.usc.etse.aos.model.Publicacion;
import gal.usc.etse.aos.repository.ComentarioRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gal.usc.etse.aos.repository.PublicationRepository;
import gal.usc.etse.aos.repository.UsuarioRepository;
import java.net.URI;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("publicaciones")
public class PublicationController {

    private PublicationRepository db;
    private UsuarioRepository dbUsuario;
    private ComentarioRepository dbComentario;


    @Autowired
    public PublicationController(PublicationRepository db, UsuarioRepository dbUsuario, ComentarioRepository dbComentario) {
        this.db = db;
        this.dbUsuario = dbUsuario;
        this.dbComentario = dbComentario;
    }
    
    //Servicio 1
    @PreAuthorize("permitAll()")
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se devuelven todas las publicaciones que haya.
    public ResponseEntity<Collection<Publicacion>> obtenerTodasPublicaciones() {
        return ResponseEntity.ok(db.findAll().stream().collect(Collectors.toList()));
    }    
    
    //Servicio 7
    @PreAuthorize("hasRole('Lector')")
    @PostMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se añade un comentario a una publicación. Se necesitan permisos de Lector
    public ResponseEntity añadirComentario(@PathVariable("id") String id, @RequestBody Comentario comentario) {
        Comentario nuevoComentario = new Comentario();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = simpleDateFormat.format(new Date());        
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String idComentario = fecha + "-" + username + "-" + "comentario";
        //Se comprueba si existe el identificador del comentario
        if (db.existsById(idComentario)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            //Si no existe, se crea el comentario y se añade al repositorio de comentarios
            nuevoComentario.setId(idComentario);
            nuevoComentario.setPublicacion(id);
            nuevoComentario.setUsername(username);
            nuevoComentario.setFechaComentario(fecha);
            nuevoComentario.setComentario(comentario.getComentario());
            dbComentario.save(nuevoComentario);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(nuevoComentario.getId()).toUri();
            return ResponseEntity.created(location).body(nuevoComentario);
        }
    }
    
    //Servicio Especial
    @PreAuthorize("permitAll()")
    @GetMapping(
            path = "/{id}/comentarios",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se obtienen todos los comentarios del usuario
    public ResponseEntity<Collection<Comentario>> getTodosComentariosPublicacion(@PathVariable("id") String id) {
        //Se comprueba que exista el usuario
        if (!db.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dbComentario.findByPublicacion(id)
                    .stream()
                    .collect(Collectors.toList()));
        }
    }
    
    //Servicio 16
    @PreAuthorize("hasRole('Redactor')")
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se añade una publicación. Se necesitan permisos de redactor.
    public ResponseEntity crearPublicacion(@RequestBody Publicacion publicacion) {
        Publicacion nuevaPublicacion = new Publicacion();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = simpleDateFormat.format(new Date());
        String titulo = publicacion.getTitulo().replace(" ", "-").toLowerCase();
        titulo = Normalizer.normalize(titulo, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String idPublicacion = fecha + "-" + username + "-" + titulo;
        //Se comprueba si existe el identificador de la publicación
        if (db.existsById(idPublicacion)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            //Si no existe, se añade la nueva publicación al repositorio
            nuevaPublicacion.setId(idPublicacion);
            nuevaPublicacion.setTitulo(publicacion.getTitulo());
            nuevaPublicacion.setCuerpo(publicacion.getCuerpo());
            nuevaPublicacion.setUsername(username);
            nuevaPublicacion.setResumen(publicacion.getResumen());
            nuevaPublicacion.setfechaPublicacion(fecha);
            nuevaPublicacion.setPalabrasClave(publicacion.getPalabrasClave());
            db.save(nuevaPublicacion);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").buildAndExpand(nuevaPublicacion.getId()).toUri();
            return ResponseEntity.created(location).body(nuevaPublicacion);
        }
    }

    //Servicio 2 y 17
    @PreAuthorize("permitAll()")
    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se devuelve una publicación concreta
    public ResponseEntity<Publicacion> getPublicacion(@PathVariable("id") String id) {
        Optional<Publicacion> publicacion = db.findById(id);
        if (publicacion.isPresent()) {
            return ResponseEntity.ok().body(publicacion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Servicio 18
    @PreAuthorize("hasRole('Redactor')")
    @PutMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se modifican los datos de una publicacion. Se necesitan permisos de redactor
    public ResponseEntity modificarPublicacion(@PathVariable("id") String id, @RequestBody Publicacion p) {
        Optional<Publicacion> publicacion = db.findById(id);
        if (publicacion.isPresent()) {
            publicacion.get().setCuerpo(p.getCuerpo());            
            publicacion.get().setResumen(p.getResumen());
            publicacion.get().setPalabrasClave(p.getPalabrasClave());
            db.save(publicacion.get());
            return ResponseEntity.ok().body(publicacion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //Servicio 19 y 22
    @PreAuthorize("hasRole('Redactor')")
    @DeleteMapping(path = "/{id}")
    //Se elimina una publicación. Para ello, es necesario tener permisos de redactor.
    public ResponseEntity borrarPublicacion(@PathVariable("id") String id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Usuario> usuarioLogin = dbUsuario.findByUsername(login);
        Optional<Publicacion> usuarioPublicador = db.findById(id);
        if (usuarioLogin.isPresent() && usuarioPublicador.isPresent()) {
            //En caso de que el usuario que va a borrar es administrador, lo borra
            if (usuarioLogin.get().getRol().equals("ROLE_Administrador")) { 
                db.deleteById(id);
                return ResponseEntity.noContent().build();
                //Si el usuario que va a borrar (logueado) es el propio autor (el redactor), la puede borrar
            } else if (usuarioLogin.get().getUsername().equals(usuarioPublicador.get().getUsername())) { 
                db.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

}
