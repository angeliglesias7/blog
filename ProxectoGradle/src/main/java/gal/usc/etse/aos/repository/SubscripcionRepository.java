/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.usc.etse.aos.repository;

import gal.usc.etse.aos.model.Subscripcion;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author angel
 */

public interface SubscripcionRepository extends MongoRepository<Subscripcion, ObjectId> {

    boolean existsBySubscripcion(String subscripcion);
    
    boolean existsBySubscripcionAndUsername(String subscripcion, String username);

    Optional<Subscripcion> findBySubscripcionAndUsername(String subscripcion, String username);

    void deleteBySubscripcion(String subscripcion);

    List<Subscripcion> findByUsername(String tipo);

    @Override
    List<Subscripcion> findAll();

    @Override
    Page<Subscripcion> findAll(Pageable pageable);

    @Override
    Subscripcion save(Subscripcion entity);
}

