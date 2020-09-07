package gal.usc.etse.aos.repository;

import gal.usc.etse.aos.model.Comentario;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ComentarioRepository extends MongoRepository<Comentario, ObjectId> {

    boolean existsByUsername(String username);

    void deleteById(String id);

    void deleteByUsername(String username);

    List<Comentario> findByUsername(String username);
    
    List<Comentario> findByPublicacion(String publicacion);

    @Override
    List<Comentario> findAll();

    @Override
    Page<Comentario> findAll(Pageable pageable);
    
    @Override
    Comentario save(Comentario entity);
}
