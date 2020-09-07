package gal.usc.etse.aos.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import gal.usc.etse.aos.model.Usuario;
import gal.usc.etse.aos.repository.UsuarioRepository;
import java.util.Optional;

@Service
public class UserAuthDetailsService implements UserDetailsService {

    private UsuarioRepository db;

    @Autowired
    UserAuthDetailsService(UsuarioRepository db) {
        this.db = db;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = db.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        String rol = user.get().getRol();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(rol);
        return new User(user.get().getUsername(), user.get().getPassword(), true, true, true, !user.get().isBloqueado(), authorities);
    }
}



