package gal.usc.etse.aos.repository;

import gal.usc.etse.aos.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    Optional<Usuario> findByUsername(String username);

    @Override
    List<Usuario> findAll();

    @Override
    Page<Usuario> findAll(Pageable pageable);

    @Override
    Usuario save(Usuario entity);
}
