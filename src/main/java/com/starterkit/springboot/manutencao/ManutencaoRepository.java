package com.starterkit.springboot.manutencao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    Optional<Manutencao> findByCodigoUnico(String codigo);

    

   
    
}