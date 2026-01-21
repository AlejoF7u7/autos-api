package com.concesionario.autos_api.repository;

import com.concesionario.autos_api.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {

    List<Auto> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(String marca, String modelo);
}