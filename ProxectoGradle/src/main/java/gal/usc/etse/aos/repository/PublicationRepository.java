package gal.usc.etse.aos.repository;

import gal.usc.etse.aos.model.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface PublicationRepository extends MongoRepository<Publicacion, String> {
    @Override
    boolean existsById(String id);
    
    @Override
    void deleteById(String id);

    @Override
    Optional<Publicacion> findById(String id);

    @Override
    List<Publicacion> findAll();

    @Override
    Page<Publicacion> findAll(Pageable pageable);
    
    @Override
    Publicacion save(Publicacion entity);

}
