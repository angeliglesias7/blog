package gal.usc.etse.aos.controller;

import gal.usc.etse.aos.model.Comentario;
import gal.usc.etse.aos.model.Subscripcion;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import gal.usc.etse.aos.model.Usuario;
import gal.usc.etse.aos.repository.ComentarioRepository;
import gal.usc.etse.aos.repository.SubscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import gal.usc.etse.aos.repository.UsuarioRepository;
import java.text.SimpleDateFormat;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private UsuarioRepository db;
    private ComentarioRepository dbComentario;
    private SubscripcionRepository dbSubscripcion;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository db, ComentarioRepository dbComentario, SubscripcionRepository dbSubscripcion, PasswordEncoder passwordEncoder) {
        this.db = db;
        this.dbComentario = dbComentario;
        this.dbSubscripcion = dbSubscripcion;
        this.passwordEncoder = passwordEncoder;
    }

    //Servicio 3
    @PreAuthorize("permitAll()")
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Por defecto se mostrarán 5 usuarios. Se puede filtrar por rol (ROLE_Redactor), por ejemplo, para mostrar solo redactores
    public ResponseEntity<Page<Usuario>> getTodosUsuarios(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "5") int numero, @RequestParam(value = "rol", defaultValue = "") String rol) {
        Usuario usuario = new Usuario();        
        if (!rol.isEmpty()) {
            if (rol.equals("ROLE_Redactor")) {
                usuario.setRol(rol);
            }
        }
        return ResponseEntity.ok(db.findAll(Example.of(usuario), PageRequest.of(page, numero)));
    }

    //Servicio 4
    @PreAuthorize("permitAll()")
    @GetMapping(
            path = "/{username}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Obtener un usuario de tipo redactor.
    public ResponseEntity<Usuario> getUsuario(@PathVariable("username") String username) {
        //Forma más limpia de hacer comprobaciones de si un objeto es null o no.
        Optional<Usuario> usuario = db.findByUsername(username);
        //Se comprueba que existe
        if (usuario.isPresent()) {
            //Si es un redactor, se devuelve
            if (usuario.get().getRol().equals("ROLE_Redactor")) {
                return ResponseEntity.ok().body(usuario.get());
            } else {
                //Si es el mismo, se devuelve, si no se da un fallo de autorización
                String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                if (login.equals(username)) {
                    return ResponseEntity.ok().body(usuario.get());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Servicio 5
    @PreAuthorize("permitAll()")
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se crea un usuario de tipo Lector
    public ResponseEntity crearUsuarioLector(@RequestBody Usuario u) {
        if (db.existsByUsername(u.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            //Se crea en caso de que no haya un usuario con ese username
            Usuario usuario = new Usuario();
            usuario.setUsername(u.getUsername());
            usuario.setPassword(passwordEncoder.encode(u.getPassword()));
            usuario.setRol("ROLE_Lector");
            usuario.setNombre(u.getNombre());
            usuario.setEmail(u.getEmail());
            usuario.setBloqueado(false);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = simpleDateFormat.format(new Date());
            usuario.setFechaRegistro(fecha);
            db.save(usuario);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{username}").buildAndExpand(usuario.getUsername()).toUri();
            return ResponseEntity.created(location).body(usuario.setPassword("*********"));
        }
    }    

    //Servicio 9
    @PreAuthorize("principal == #username")
    @GetMapping(
            path = "/{username}/comentarios",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se obtienen todos los comentarios del usuario
    public ResponseEntity<Collection<Comentario>> getTodosComentarios(@PathVariable("username") String username) {
        //Se comprueba que exista el usuario
        if (!db.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dbComentario.findByUsername(username)
                    .stream()
                    .collect(Collectors.toList()));
        }
    }

    //Servicio 10
    @PreAuthorize("principal == #username")
    @PostMapping(
            path = "/{username}/subscripciones",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se crea una nueva subscripcion
    public ResponseEntity crearSubscripcion(@PathVariable("username") String username, @RequestBody Subscripcion s) {
        if (!db.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        } else {
            //Se comprueba que el usuario exista y que el usuario todavía no tenga una subscripcion con ese nombre
            if (dbSubscripcion.existsBySubscripcionAndUsername(s.getSubscripcion(), username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                Subscripcion subscripcion = new Subscripcion();
                subscripcion.setUsername(username);
                subscripcion.setTipo(s.getTipo());
                subscripcion.setSubscripcion(s.getSubscripcion().toLowerCase());
                dbSubscripcion.save(subscripcion);
                URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{username}/subscripciones/{subscripcion}").buildAndExpand(username, subscripcion.getSubscripcion()).toUri();
                return ResponseEntity.created(location).body(subscripcion);
            }
        }
    }

    //Servicio 11
    @PreAuthorize("principal == #username")
    @GetMapping(
            path = "/{username}/subscripciones",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se devuelven todas las subscripciones asociadas a un usuario
    public ResponseEntity<Collection<Subscripcion>> getTodasSubscripciones(@PathVariable("username") String username) {
        if (!db.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dbSubscripcion.findByUsername(username)
                    .stream()
                    .collect(Collectors.toList()));
        }
    }

    //Servicio 12
    @PreAuthorize("principal == #username")
    @DeleteMapping(path = "/{username}/subscripciones/{subscripcion}")
    //Se elimina una de las subscripciones que tiene un usuario
    public ResponseEntity eliminarSubscripcion(@PathVariable("username") String username, @PathVariable("subscripcion") String subscripcion) {
        if (!dbSubscripcion.existsBySubscripcionAndUsername(subscripcion, username)) {
            return ResponseEntity.notFound().build();
        } else {
            dbSubscripcion.deleteBySubscripcion(subscripcion);
            return ResponseEntity.noContent().build();
        }
    }

    //Servicio 13
    @PreAuthorize("principal == #username")
    @PutMapping(
            path = "/{username}/subscripciones/{subscripcion}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Se modifica el tipo de una de las descripciones que tenga el usuario
    public ResponseEntity modificarSubscripcion(@PathVariable("username") String username, @PathVariable("subscripcion") String subscripcion, @RequestBody Subscripcion s) {
        Optional<Subscripcion> subs = dbSubscripcion.findBySubscripcionAndUsername(subscripcion, username);
        if (subs.isPresent()) {
            subs.get().setTipo(s.getTipo());
            dbSubscripcion.save(subs.get());
            return ResponseEntity.ok().body(subs.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Servicio 8 y 14
    @PreAuthorize("principal == #username || hasRole('Moderador')")
    @DeleteMapping(path = "/{username}/comentarios")
    //Se eliminan todos los comentarios de un usuario. Esto lo puede hacer tanto el propio usuario o un usuario de tipo Moderador
    public ResponseEntity borrarTodosComentarios(@PathVariable("username") String username) {
        if (!dbComentario.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        } else {
            dbComentario.deleteByUsername(username);
            return ResponseEntity.noContent().build();
        }
    }

    //Servicio 15
    @PreAuthorize("hasRole('Moderador')")
    @PutMapping(path = "/{username}/suspender")
    //Se suspenderá la cuenta de un usuario. Se necesitan privilegios de Moderador. Se podrá el atributo bloqueado a true
    public ResponseEntity suspenderCuenta(@PathVariable("username") String username) {
        Optional<Usuario> usuario = db.findByUsername(username);
        if (usuario.isPresent()) {
            usuario.get().setBloqueado(true);
            db.save(usuario.get());
            return ResponseEntity.ok().body(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Servicio 20
    @PreAuthorize("principal == #username || hasRole('Administrador')")
    @PutMapping(
            path = "/{username}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //Este metodo permite modificar al propio usuario alguno de sus atributos (nombre, email y contraseña
    //Además, en caso de que sea administrador, puede tambien cambiar el rol del usuario
    public ResponseEntity cambiarRol(@PathVariable("username") String username, @RequestBody Usuario u) {
        Optional<Usuario> usuario = db.findByUsername(username);
        if (usuario.isPresent()) {
            //Se cambian los atributos que se quieran
            usuario.get().setPassword(passwordEncoder.encode(u.getPassword()));
            usuario.get().setNombre(u.getNombre());
            usuario.get().setEmail(u.getEmail());
            String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            Optional<Usuario> usuarioLogin = db.findByUsername(login);
            //Si además, el usuario que hizo login es administrador, puede cambiar el rol del usuario
            if (usuarioLogin.get().getRol().equals("ROLE_Administrador")) {
                usuario.get().setRol("ROLE_" + u.getRol());
            }
            db.save(usuario.get());
            return ResponseEntity.ok().body(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Servicio 6 y 21
    @PreAuthorize("principal == #username || hasRole('Administrador')")
    @DeleteMapping(path = "/{username}")
    //Se borrará la cuenta del Usuario. Esto lo puede hacer el propio usuario de la cuenta, de forma que elimina
    //su cuenta, o lo puede hacer un administrador con las cuentas de los demás
    public ResponseEntity borrarUsuario(@PathVariable("username") String username) {
        Optional<Usuario> usuario = db.findByUsername(username);
        if (usuario.isPresent()) {
            db.deleteByUsername(username);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
